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
        private val FETCH_RECENTS_METHOD = "flickr.photos.getRecent"
        private val SEARCH_METHOD = "flickr.photos.search"
        private val ENDPOINT: Uri = Uri
            .parse("https://api.flickr.com/services/rest/")
            .buildUpon()
            .appendQueryParameter("api_key", API_KEY)
            .appendQueryParameter("format", "json")
            .appendQueryParameter("nojsoncallback", "1")
            .appendQueryParameter("extras", "url_s")
            .build()

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

    private fun buildUrl(method: String, query: String, page: Int): String {
        val uriBuilder = ENDPOINT.buildUpon()
            .appendQueryParameter("method", method)
            .appendQueryParameter("page", page.toString())

        if (method == SEARCH_METHOD) {
            uriBuilder.appendQueryParameter("text", query)
        }
        return uriBuilder.build().toString()
    }

    private fun downloadGalleryItems(url: String): List<GalleryItem> {
        var list = listOf<GalleryItem>()
        try {
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

    fun fetchRecentPhotos(page: Int): List<GalleryItem> {
        val url = buildUrl(FETCH_RECENTS_METHOD, "", page)
        return downloadGalleryItems(url)
    }

    fun searchPhotos(query: String, page: Int): List<GalleryItem> {
        val url = buildUrl(SEARCH_METHOD, query, page)
        return downloadGalleryItems(url)
    }

    private fun parseItems(jsonString: String): List<GalleryItem> {
        return Gson().fromJson(jsonString, SerializedItem::class.java).photos.photo.toList()
    }
}