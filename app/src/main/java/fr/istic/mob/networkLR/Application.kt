package fr.istic.mob.networkLR

import android.app.Application
import io.paperdb.Paper

class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        Paper.init(this);

    }
}