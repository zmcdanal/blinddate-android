package com.ethereal.blinddate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class BlindDateApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        val appCheck = com.google.firebase.appcheck.FirebaseAppCheck.getInstance()
        if (BuildConfig.DEBUG) {
            // Local dev & emulator
            appCheck.installAppCheckProviderFactory(
                com.google.firebase.appcheck.debug.DebugAppCheckProviderFactory.getInstance()
            )
        } else {
            // Real users
            appCheck.installAppCheckProviderFactory(
                com.google.firebase.appcheck.playintegrity.PlayIntegrityAppCheckProviderFactory.getInstance()
            )
        }
    }
}