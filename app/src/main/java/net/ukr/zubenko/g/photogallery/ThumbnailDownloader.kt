package net.ukr.zubenko.g.photogallery

import android.annotation.SuppressLint
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import java.util.concurrent.ConcurrentHashMap
import android.os.Message
import android.graphics.BitmapFactory
import android.graphics.Bitmap
import java.io.IOException
import android.text.method.TextKeyListener.clear
import android.widget.Toast


class ThumbnailDownloader<T>(val mResponseHandler: Handler): HandlerThread(TAG) {
    private var mHasQuit = false
    private lateinit var mRequestHandler: Handler
    private val mRequestMap = ConcurrentHashMap<T, String>()
    lateinit var mThumbnailDownloadListener: ThumbnailDownloadListener<T>

    companion object {
        private const val TAG = "ThumbnailDownloader"
        private const val MESSAGE_DOWNLOAD = 0

        interface ThumbnailDownloadListener<T> {
            fun onThumbnailDownloaded(target: T, thumbnail: Bitmap)
        }
    }

    override fun quit(): Boolean {
        mHasQuit = true
        return super.quit()
    }

    fun queueThumbnail(target: T, url: String?) {
        Log.i(TAG, "Got a URL: $url")
        if (url == null) {
            mRequestMap.remove(target)
        }
        else {
            mRequestMap[target] = url
            mRequestHandler.obtainMessage(MESSAGE_DOWNLOAD, target).sendToTarget()
        }
    }

    override fun onLooperPrepared() {
        mRequestHandler =
        @SuppressLint("HandlerLeak")
        object : Handler() {
            override fun handleMessage(msg: Message) {
                if (msg.what == MESSAGE_DOWNLOAD) {
                    val target = msg.obj as T
                    Log.i(TAG, "Got a request for URL: " + mRequestMap[target])
                    handleRequest(target)
                }
            }
        }
    }

    private fun handleRequest(target: T) {
        try {
            val url = mRequestMap[target] ?: return
            val bitmapBytes = FlickrFetchr().getUrlBytes(url)
            val bitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.size)
            Log.i(TAG, "Bitmap created")

            mResponseHandler.post {
                if (mRequestMap[target] == url && !mHasQuit) {
                    mRequestMap.remove(target)
                    mThumbnailDownloadListener.onThumbnailDownloaded(target, bitmap)
                }
            }

        } catch (ioe: IOException) {
            Log.e(TAG, "Error downloading image", ioe)
        }

    }

    fun clearQueue() {
        mRequestHandler.removeMessages(MESSAGE_DOWNLOAD)
        mRequestMap.clear()
    }
}