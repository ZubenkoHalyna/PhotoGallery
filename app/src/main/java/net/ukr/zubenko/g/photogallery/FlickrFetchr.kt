package net.ukr.zubenko.g.photogallery

import android.net.Uri
import android.util.Log
import com.google.gson.Gson
import org.json.JSONException
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL


class FlickrFetchr {

    companion object {
        private const val TAG = "FlickrFetchr"
        private const val API_KEY = "896315495eba9cebbb139ccaee07fb72"

        private class SerializedItem(val photos: GalleryItemArray) {
            class GalleryItemArray(val photo: Array<GalleryItem>)
        }
    }

    fun getUrlBytes(urlSpec: String): ByteArray {
        val url = URL(urlSpec)
        val connection = url.openConnection() as HttpURLConnection

        try {
            val out = ByteArrayOutputStream()
            val inStream = connection.inputStream
            if (connection.responseCode != HTTP_OK) {
                throw IOException("${connection.responseMessage}: with $urlSpec")
            }
            val buffer = ByteArray(1024)
            var bytesRead = inStream.read(buffer)
            while (bytesRead > 0) {
                out.write(buffer, 0, bytesRead)
                bytesRead = inStream.read(buffer)
            }
            out.close()
            return out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

    fun getUrlString(urlSpec: String): String {
        return String(getUrlBytes(urlSpec))
    }

    fun fetchItems(page: Int): List<GalleryItem> {
        var list = listOf<GalleryItem>()
        try {
            val url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .appendQueryParameter("page", page.toString())
                .build().toString()
            val jsonString = getUrlString(url)
            Log.i(TAG, "Received JSON: $jsonString")
            list = parseItems(jsonString)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException){
            Log.e(TAG, "Failed to parse JSON", je)
        }
        return list
    }

    private fun parseItems(jsonString: String): List<GalleryItem> {
        return Gson().fromJson(jsonString, SerializedItem::class.java).photos.photo.toList()
    }
}