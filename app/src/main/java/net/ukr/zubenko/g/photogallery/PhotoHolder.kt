package net.ukr.zubenko.g.photogallery

import android.support.v7.widget.RecyclerView
import android.widget.TextView

class PhotoHolder(val mTitleTextView: TextView): RecyclerView.ViewHolder(mTitleTextView) {
    fun bindGalleryItem(item: GalleryItem) {
        mTitleTextView.text = item.toString()
    }
}