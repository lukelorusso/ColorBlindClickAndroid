# README #

## Presentation ##

This is the source code of the published Android application: `-=:[ ColorBlindClick ]:=-`  

> https://play.google.com/store/apps/details?id=com.lukelorusso.colorblindclick

![Demo](press/demo.gif)

ColorBlindClick is designed to help all colorblind people to distinguish colors. It's very simple to use, just point an object and click to get the name, RGB and hash of the color.

An iOS version is also available.

In this project you can find


* Pure [Kotlin](https://kotlinlang.org/) code

* [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html)

* [Jetpack Compose UI](https://developer.android.com/compose)

* [Jetpack CameraX](https://developer.android.com/media/camera/camerax) that provides a Camera PreviewView

* [Koin](https://insert-koin.io/) for dependency injection

* [Retrofit + Json](https://square.github.io/retrofit/) `kotlinx.serialization` to consume web services

* [Timber](https://github.com/JakeWharton/timber) for logging

- - -

This app DOES NOT provide any sort of color database. In fact, it simply takes the target pixel's color, gets its hash representation, and then consumes APIs from web services to retrieve the corresponding color definition.  
If you find any discrepancy between the real color and its definition, you should refer to the color API provider:
 - for English speakers: https://www.thecolorapi.com/
 - for Italian/French speakers: https://savedev.altervista.org/SD-Frontend/colorblindclick/index.php

If you find, instead, any issue about the app itself, like bugs, crashes or incompatibilities, feel free to contact me!

- - -

## Copyright ##

The App: Copyright 2025 LUCA LORUSSO - https://lukelorusso.com/  
