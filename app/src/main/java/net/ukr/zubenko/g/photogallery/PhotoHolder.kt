package net.ukr.zubenko.g.photogallery

import android.content.Context
import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import android.content.Intent
import android.content.ClipData.newIntent
import android.widget.Toast


class PhotoHolder(private val context: Context,
                  private val mItemImageView: ImageView,
                  private var mGalleryItem: GalleryItem):
    RecyclerView.ViewHolder(mItemImageView),
    View.OnClickListener
{
    init {
        mItemImageView.setOnClickListener(::onClick)
    }

    override fun onClick(v: View) {
        val uri = mGalleryItem.photoPageUri
        val i =
            if (uri.scheme == "http" || uri.scheme == "https")
                PhotoPageActivity.newIntent(context, uri)
            else
                Intent(Intent.ACTION_VIEW, uri)

        context.startActivity(i)
    }

    fun bindGalleryItem(galleryItem: GalleryItem) {
        mGalleryItem = galleryItem
    }

    fun bindDrawable(drawable: Drawable) {
        mItemImageView.setImageDrawable(drawable)
    }
}