package net.ukr.zubenko.g.photogallery

import android.app.Activity
import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import android.widget.ImageView

class PhotoAdapter(val mGalleryItems: MutableList<GalleryItem>,
                   private val mThumbnailDownloader: ThumbnailDownloader<PhotoHolder>,
                   private val activity: Activity,
                   private val container: ViewGroup?,
                   private val context: Context) : RecyclerView.Adapter<PhotoHolder>() {
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): PhotoHolder {
        val imageView = activity.layoutInflater.inflate(R.layout.gallery_item, container, false)
            .findViewById<ImageView>(R.id.item_image_view)
        return PhotoHolder(context, imageView, mGalleryItems[p1])
    }

    override fun getItemCount() = mGalleryItems.size

    override fun onBindViewHolder(photoHolder: PhotoHolder, position: Int) {
        val galleryItem = mGalleryItems[position]
        val placeholder = activity.resources.getDrawable(R.drawable.bill_up_close, activity.theme)
        photoHolder.bindDrawable(placeholder)
        photoHolder.bindGalleryItem(galleryItem)
        mThumbnailDownloader.queueThumbnail(photoHolder, galleryItem.mUrl)
    }
}