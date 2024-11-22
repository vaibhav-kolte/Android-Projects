package com.vkolte.mywishlistapp

import android.app.Application
import com.vkolte.mywishlistapp.data.Graph

class WishListApp : Application() {
    override fun onCreate() {
        super.onCreate()
        Graph.provide(this)
    }
}