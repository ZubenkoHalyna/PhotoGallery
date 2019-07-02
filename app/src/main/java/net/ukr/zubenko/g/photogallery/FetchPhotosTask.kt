package net.ukr.zubenko.g.photogallery

import android.os.AsyncTask

class FetchPhotosTask(private val mCallBack: (List<GalleryItem>) -> Unit) : AsyncTask<Void, Void, List<GalleryItem>>() {
    override fun doInBackground(vararg params: Void): List<GalleryItem> = FlickrFetchr().fetchItems()

    override fun onPostExecute(result: List<GalleryItem>?) {
        result?.let {
            mCallBack(result)
        }
    }
}