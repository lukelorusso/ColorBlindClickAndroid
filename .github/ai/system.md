# Technical System Constraints

## Tech Stack
- Framework: Android SDK (minSdk, compileSdk/targetSdk in `gradle/libs.versions.toml`), built with Android Gradle Plugin 9 + Gradle 9 (Kotlin DSL, version catalog `gradle/libs.versions.toml`)
- Language: Kotlin (see `gradle/libs.versions.toml`), JVM toolchain 22
- UI: Jetpack Compose (Material 3) hosted inside `Fragment`/`ViewPager2` screens driven by a single `MainActivity`; `ViewBinding` still enabled for legacy XML views (`res/layout`)
- Camera: Jetpack CameraX (`camera-core`, `camera-camera2`, `camera-lifecycle`, `camera-view`) via a `PreviewView` for the pixel-picking screen
- Async: Kotlin Coroutines + `StateFlow` (no RxJava, no LiveData except where already present for legacy interop)
- DI: Koin (`koin-core`, `koin-android`), one module per Gradle module (`domainModule`, `dataModule`/`dataTestModule`/`dataCommonModule`, `presentationModule`), wired in `AndroidApplication`
- Networking: Retrofit 3 + OkHttp3 + `kotlinx.serialization` (Json) to consume the two color-decoding web services (thecolorapi.com, savedev.altervista.org)
- Persistence: Room DB (history of decoded colors) and Jetpack DataStore Preferences (settings), abstracted behind `data/datasource` + `data/manager`;
- Images: Coil for async image loading; `ZoomableImageBox` (JitPack) for pinch-to-zoom previews
- Observability: Timber for logging, Firebase Analytics + Crashlytics for telemetry/crash reporting (gated by `ENABLE_ANALYTICS` BuildConfig flag, disabled in debug)
- Testing: JUnit 4, `kotlinx-coroutines-test`, `koin-test`; unit tests only (no instrumented/UI test suite in this repo)

## Directory Structure
This is a multi-module Gradle project following Clean Architecture, split into three modules declared in `settings.gradle`: `:presentation`, `:domain`, `:data`. Dependency direction is strictly `presentation -> domain` and `presentation -> data -> domain` (domain has no dependency on the other two).

- `domain/src/main/kotlin/.../domain/`
  - `entity/` — plain domain entities
  - `repository/` — repository interfaces (contracts), implemented in `data`
  - `usecase/` — one `UseCase` subclass per application action (e.g. `DecodeColorHexUseCase`, `GetSavedColorListUseCase`), grouped flat, not by feature
  - `usecase/base/` — `UseCase` abstract base class and `Logger` interface
  - `exception/` — domain-specific exceptions
  - `di/DomainModule.kt` — Koin module wiring every use case
- `data/src/main/kotlin/.../data/`
  - `datasource/` (+ `impl/`, `impl/datastore/android`, `impl/roomdb/{android,dao,dto}`) — `DatabaseDataSource`/`PreferencesDataSource` contracts and their DataStore/Room implementations
  - `net/` (+ `api/`, `dto/`, `mapper/`) — Retrofit API interfaces, network DTOs, and mappers from DTO to domain entities
  - `repository/` — repository implementations backing the `domain` interfaces
  - `manager/` — thin façades over data sources (`DatabaseManager`, `PreferencesManager`)
  - `extensions/`, `di/` — Kotlin extensions and Koin modules (`DataModule`, `DataCommonModule`, `DataTestModule`)
- `presentation/src/main/kotlin/.../presentation/`
  - `ui/<feature>/` — one package per screen/feature (`capture`, `history`, `preview`, `settings`, `info`, `imagepicker`, `error`, `icons`, `theme`), each typically containing a `Fragment` (or `Activity`), an `AppViewModel` subclass, a `UiState` data class, and a `Router`
  - `ui/base/` — shared Compose components and MVVM base classes (`AppViewModel`, `AppRouter`, `AppCardDialogFragment`, `ContentState`, dialogs, toolbars)
  - `di/PresentationModule.kt` — Koin module wiring view models and app-level singletons (Logger, ErrorMessageFactory, TrackerHelper)
  - `error/`, `helper/` (+ `impl/`), `logger/`, `extensions/` — cross-cutting presentation utilities
  - `res/` — Compose still coexists with XML resources (`layout/`, `values*/` incl. `fr`/`it` translations, `drawable*/`, `mipmap*/`, `xml/`)
- `data/src/test`, `domain` (no tests), `presentation/src/test` — unit tests mirror the main source package structure
- `proguard/` — release-only ProGuard rule files (`dto-rules.pro`, `model-rules.pro`, `okhttp-rules.pro`, `retrofit-rules.pro`)
- `press/` — store assets (e.g. `demo.gif`)

## Mandatory Coding Conventions
- Always place new code in the correct module: pure business rules and contracts in `domain`, I/O and persistence/network implementations in `data`, UI/state/navigation in `presentation`. Never let `domain` depend on `data` or `presentation`.
- Every use case is a single-responsibility class extending `UseCase<Params, Result>` in `domain/usecase/` and must be registered with `factory { ... }` in `DomainModule.kt`.
- Every repository is exposed as an interface in `domain/repository/` and implemented in `data/repository/`, registered in `DataCommonModule.kt` (or `DataModule.kt`/`DataTestModule.kt` for platform-specific data sources).
- Every screen follows the existing MVVM shape: a `Fragment`/`Activity`, a `ViewModel` extending `AppViewModel<UiState>`, an immutable `UiState` data class, and (where navigation is needed) a `Router` extending `AppRouter`. Register new view models in `PresentationModule.kt`.
- Any screen calling `viewModel.initRouter(activity, fragment)` (typically in `onCreate`) must also call `viewModel.clearRouter()` from that same owner's `onDestroy()`, to release the held `Activity`/`Fragment` reference as soon as the owner is destroyed rather than relying solely on `AppViewModel.onCleared()` (see `CaptureFragment`, `HistoryFragment`, `InfoFragment`, `PreviewDialogFragment`, `SettingsDialogFragment`, `ImagePickerActivity`).
- Use Koin `factory { }` for stateless dependencies and `viewModel { }` for view models; do not introduce another DI mechanism (e.g. Hilt/Dagger).
- Use Kotlin Coroutines + `StateFlow` for async/state; do not introduce RxJava or LiveData in new code.
- Prefer Jetpack Compose for all new UI; XML layouts are legacy and should only be touched when directly required.
- All network/database access must go through the `data` module's `datasource`/`manager`/`repository` layers — never call Retrofit, Room, or DataStore APIs directly from `presentation` or `domain`.
- Any new Retrofit `@Query`/`@Field` parameter carrying a device identifier or other sensitive value (e.g. `udid` in `SaveDevApi`) must be added to `OkHttpClientFactory`'s `SENSITIVE_QUERY_PARAMS` list so the debug-only `HttpLoggingInterceptor` redacts it before it reaches Logcat/bug reports.
- Log via the injected `Logger` interface (backed by Timber), not `println`/`Log.d` directly; `UseCase` already logs invocation, params, and result — avoid duplicate logging inside `run()`.
- Gate any analytics/crash-reporting side effect behind the existing `TrackerHelper`/`ENABLE_ANALYTICS` mechanism; never call Firebase APIs directly from UI code.
- New unit tests belong under the matching `src/test/kotlin/.../<package>` mirror path, using JUnit 4 + `kotlinx-coroutines-test` (`MainDispatcherRule`) + `koin-test`; presentation tests may extend `AppTest` for shared Koin test setup.
- Follow standard Kotlin/Android naming: PascalCase for classes/composables, camelCase for functions/properties, one top-level class per file named after that class.
