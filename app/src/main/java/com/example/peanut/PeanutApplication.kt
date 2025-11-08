package com.example.peanut

import android.app.Application

class PeanutApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // ทำให้ Pet Manager ทำงานก่อน
        PetManager.init(this)
    }
}