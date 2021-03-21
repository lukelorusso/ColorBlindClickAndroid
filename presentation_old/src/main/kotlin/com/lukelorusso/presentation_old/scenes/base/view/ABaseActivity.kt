package com.lukelorusso.presentation_old.scenes.base.view

import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.presentation_old.AndroidApplication
import com.lukelorusso.presentation_old.di.components.ActivityComponent
import com.lukelorusso.presentation_old.di.components.ApplicationComponent

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Base [AppCompatActivity] class for every activity in this application.
 */
abstract class ABaseActivity(@LayoutRes contentLayoutId: Int) : AppCompatActivity(contentLayoutId) {

    private val applicationComponent: ApplicationComponent by lazy {
        (application as AndroidApplication).appComponent
    }

    val activityComponent: ActivityComponent by lazy {
        applicationComponent.activityComponent().create(this)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle toolbar back arrow click here
        if (item.itemId == android.R.id.home) {
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

}
