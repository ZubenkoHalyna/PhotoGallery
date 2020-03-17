package net.ukr.zubenko.g.photogallery

import android.content.BroadcastReceiver
import android.support.v4.app.NotificationManagerCompat
import android.app.Activity
import android.app.Notification
import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.util.Log


class NotificationReceiver: BroadcastReceiver() {
    companion object {
        private val TAG = "NotificationReceiver"
    }

    override fun onReceive(c: Context, i: Intent) {
        Log.i(TAG, "received result: $resultCode")
        if (resultCode != Activity.RESULT_OK) {
            // Активность переднего плана отменила рассылку
            return
        }
        val requestCode = i.getIntExtra(PollService.REQUEST_CODE, 0)
        val notification = i.getParcelableExtra<Parcelable>(PollService.NOTIFICATION) as Notification
        NotificationManagerCompat.from(c).notify(requestCode, notification)
    }
}