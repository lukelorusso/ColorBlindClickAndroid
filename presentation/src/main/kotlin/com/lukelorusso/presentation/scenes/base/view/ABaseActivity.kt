package com.lukelorusso.presentation.scenes.base.view

import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.lukelorusso.presentation.AndroidApplication
import com.lukelorusso.presentation.di.components.ActivityComponent
import com.lukelorusso.presentation.di.components.ApplicationComponent

/**
 * Copyright (C) 2020 Mikhael LOPEZ
 * Licensed under the Apache License Version 2.0
 * Base [AppCompatActivity] class for every activity in this application.
 */
abstract class ABaseActivity : AppCompatActivity() {

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
