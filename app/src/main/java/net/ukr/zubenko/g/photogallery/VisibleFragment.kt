package net.ukr.zubenko.g.photogallery

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.app.Fragment
import android.util.Log
import android.widget.Toast

abstract class VisibleFragment: Fragment() {
    companion object {
        private val TAG = "VisibleFragment"
    }

    private val mOnShowNotification = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            // Получение означает, что пользователь видит приложение,
            // поэтому оповещение отменяется
            Log.i(TAG, "canceling notification");
            resultCode = Activity.RESULT_CANCELED;
        }
    }

    override fun onStart() {
        super.onStart()
        val filter = IntentFilter(PollService.ACTION_SHOW_NOTIFICATION)
        activity?.registerReceiver(mOnShowNotification, filter, PollService.PERM_PRIVATE, null)
    }

    override fun onStop() {
        super.onStop()
        activity?.unregisterReceiver(mOnShowNotification)
    }
}