package net.ukr.zubenko.g.photogallery

import android.graphics.drawable.Drawable
import android.support.v7.widget.RecyclerView
import android.widget.ImageView
import android.widget.TextView

class PhotoHolder(private val mItemImageView: ImageView): RecyclerView.ViewHolder(mItemImageView) {
    fun bindDrawable(drawable: Drawable) {
        mItemImageView.setImageDrawable(drawable)
    }
}