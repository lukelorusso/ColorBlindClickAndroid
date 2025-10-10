package com.lukelorusso.presentation.helper

/**
 * Copyright (C) 2024 Luke Lorusso
 * Licensed under the Apache License Version 2.0
 */
abstract class TrackerHelper {

    object Action {
        const val DELETED_ITEM = "DELETED_ITEM"
        const val DELETED_ALL_ITEMS = "DELETED_ALL_ITEMS"
        const val GOTO_ABOUT_APP_PAGE = "GOTO_ABOUT_APP_PAGE"
        const val GOTO_HOME_PAGE = "GOTO_HOME_PAGE"
        const val GOTO_HELP_PAGE = "GOTO_HELP_PAGE"
        const val GOTO_ABOUT_ME_PAGE = "GOTO_ABOUT_ME_PAGE"
        const val GOTO_SETTINGS = "GOTO_SETTINGS"
        const val GOTO_IMAGE_PICKER = "GOTO_IMAGE_PICKER"
        const val SHARED_TEXT = "SHARED_TEXT"
        const val SHARED_PREVIEW = "SHARED_PREVIEW"
        const val PERSISTENCE_EXCEPTION = "PERSISTENCE_EXCEPTION"
    }

    abstract fun track(action: String)
}
