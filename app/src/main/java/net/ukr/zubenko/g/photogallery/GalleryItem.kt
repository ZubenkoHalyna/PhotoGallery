package net.ukr.zubenko.g.photogallery

import com.google.gson.annotations.SerializedName

data class GalleryItem(
    @SerializedName("id")
    val mId: String,
    @SerializedName("title")
    val mCaption: String,
    @SerializedName("url_s")
    val mUrl: String) {
    override fun toString() = mCaption
}