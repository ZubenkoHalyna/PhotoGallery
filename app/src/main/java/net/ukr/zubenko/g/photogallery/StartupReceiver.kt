package net.ukr.zubenko.g.photogallery

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log


class StartupReceiver: BroadcastReceiver() {
    companion object {
        private val TAG = "StartupReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        Log.i(TAG, "Received broadcast intent: " + intent.action!!)
    }
}