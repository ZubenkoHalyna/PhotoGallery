package net.ukr.zubenko.g.photogallery

import android.net.Uri
import android.util.Log
import org.json.JSONException
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.HttpURLConnection.HTTP_OK
import java.net.URL


class FlickrFetchr {

    companion object {
        private const val TAG = "FlickrFetchr"
        private const val API_KEY = "896315495eba9cebbb139ccaee07fb72"
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

    fun fetchItems(): List<GalleryItem> {
        var list = listOf<GalleryItem>()
        try {
            val url = Uri.parse("https://api.flickr.com/services/rest/")
                .buildUpon()
                .appendQueryParameter("method", "flickr.photos.getRecent")
                .appendQueryParameter("api_key", API_KEY)
                .appendQueryParameter("format", "json")
                .appendQueryParameter("nojsoncallback", "1")
                .appendQueryParameter("extras", "url_s")
                .build().toString()
            val jsonString = getUrlString(url)
            Log.i(TAG, "Received JSON: $jsonString")
            val jsonBody = JSONObject(jsonString)
            list = parseItems(jsonBody)
        } catch (ioe: IOException) {
            Log.e(TAG, "Failed to fetch items", ioe)
        } catch (je: JSONException){
            Log.e(TAG, "Failed to parse JSON", je)
        }
        return list
    }

    private fun parseItems(jsonBody: JSONObject): List<GalleryItem> {
        val photosJsonObject = jsonBody.getJSONObject("photos")
        val photoJsonArray = photosJsonObject.getJSONArray("photo")
        val items = mutableListOf<GalleryItem>()

        for (i in 0 until photoJsonArray.length()) {
            val photoJsonObject = photoJsonArray.getJSONObject(i)
            val item = GalleryItem(photoJsonObject.getString("id"),
                                   photoJsonObject.getString("title"),
                                   photoJsonObject.getString("url_s")?:"")
            items.add(item)
        }
        return items.toList()
    }
}