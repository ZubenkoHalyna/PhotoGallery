package net.ukr.zubenko.g.photogallery

import android.app.IntentService
import android.content.Context
import android.content.Intent
import android.util.Log
import android.net.ConnectivityManager
import java.nio.file.Files.size
import java.util.concurrent.TimeUnit
import android.os.SystemClock
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.ClipData.newIntent
import android.content.ClipData.newIntent
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.app.NotificationCompat
import android.content.ClipData.newIntent








class PollService:IntentService(TAG) {
    companion object {
        private val TAG = "PollService"
        private val POLL_INTERVAL_MS = TimeUnit.MINUTES.toMillis(15)

        fun newIntent(context: Context): Intent {
            return Intent(context, PollService::class.java)
        }

        fun setServiceAlarm(context: Context, isOn: Boolean) {
            val i = newIntent(context)
            val pi = PendingIntent.getService(context, 0, i, 0)
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            if (isOn) {
                alarmManager.setRepeating(
                    AlarmManager.ELAPSED_REALTIME,
                    SystemClock.elapsedRealtime(), POLL_INTERVAL_MS, pi
                )
            } else {
                alarmManager.cancel(pi)
                pi.cancel()
            }
        }

        fun isServiceAlarmOn(context: Context): Boolean {
            val pi = PendingIntent.getService(context, 0, newIntent(context), PendingIntent.FLAG_NO_CREATE)
            return pi != null
        }
    }

    override fun onHandleIntent(intent: Intent) {
        if (!isNetworkAvailableAndConnected()) {
            return
        }
        val query = QueryPreferences.getStoredQuery(this)
        val lastResultId = QueryPreferences.getLastResultId(this)
        val items: List<GalleryItem>
        if (query.isEmpty()) {
            items = FlickrFetchr().fetchRecentPhotos(0)
        } else {
            items = FlickrFetchr().searchPhotos(query, 0)
        }
        if (items.isEmpty()) {
            return
        }
        val resultId = items[0].mId
            if (resultId == lastResultId) {
            Log.i(TAG, "Got an old result: $resultId")
        } else {
            Log.i(TAG, "Got a new result: $resultId")

                val resources = resources
                val i = PhotoGalleryActivity.newIntent(this)
                val pi = PendingIntent.getActivity(this, 0, i, 0)
                val notification = NotificationCompat.Builder(this)
                    .setTicker(resources.getString(R.string.new_pictures_title))
                    .setSmallIcon(android.R.drawable.ic_menu_report_image)
                    .setContentTitle(resources.getString(R.string.new_pictures_title))
                    .setContentText(resources.getString(R.string.new_pictures_text))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .build()
                val notificationManager = NotificationManagerCompat.from(this)
                notificationManager.notify(0, notification)
        }
        QueryPreferences.setLastResultId(this, resultId)
    }

    private fun isNetworkAvailableAndConnected(): Boolean {
        val cm = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return cm.activeNetworkInfo?.isConnected ?: false
    }
}