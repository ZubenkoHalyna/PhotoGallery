package net.ukr.zubenko.g.photogallery

import android.os.AsyncTask
import android.util.Log

class FetchPhotosTask(private val mCallBack: (List<GalleryItem>) -> Unit,
                      private val page: Int,
                      private val mQuery: String) :
    AsyncTask<Void, Void, List<GalleryItem>>() {

    override fun doInBackground(vararg params: Void): List<GalleryItem> {
        return if (mQuery.isEmpty()) {
            FlickrFetchr().fetchRecentPhotos(page)
        } else {
            Log.d(PhotoGalleryFragment.TAG + "mQuery", "QueryTextChange: $mQuery")
            FlickrFetchr().searchPhotos(mQuery, page)
        }
    }

    override fun onPostExecute(result: List<GalleryItem>?) {
        result?.let {
            mCallBack(result)
        }
    }
}