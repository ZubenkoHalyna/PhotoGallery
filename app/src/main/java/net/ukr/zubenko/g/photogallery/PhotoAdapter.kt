package net.ukr.zubenko.g.photogallery

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.TextView

class PhotoAdapter(val mGalleryItems: MutableList<GalleryItem>, private val activity: Activity) :
    RecyclerView.Adapter<PhotoHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoHolder {
        return PhotoHolder(TextView(activity))
    }

    override fun getItemCount() = mGalleryItems.size

    override fun onBindViewHolder(photoHolder: PhotoHolder, position: Int) {
        photoHolder.bindGalleryItem(mGalleryItems[position])
    }
}