package com.lukelorusso.presentation_old.di

import javax.inject.Scope

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * A scoping annotation to permit objects whose lifetime should conform
 * to the life of the activity to be memorized in the correct component.
 */
@Scope
@Retention(AnnotationRetention.RUNTIME)
annotation class PerActivity
