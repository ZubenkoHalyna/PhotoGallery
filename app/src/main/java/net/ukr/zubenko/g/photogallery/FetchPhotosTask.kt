package net.ukr.zubenko.g.photogallery

import android.os.AsyncTask

class FetchPhotosTask(private val mCallBack: (List<GalleryItem>) -> Unit, private val page: Int) :
    AsyncTask<Void, Void, List<GalleryItem>>() {

    override fun doInBackground(vararg params: Void): List<GalleryItem> = FlickrFetchr().fetchItems(page)

    override fun onPostExecute(result: List<GalleryItem>?) {
        result?.let {
            mCallBack(result)
        }
    }
}