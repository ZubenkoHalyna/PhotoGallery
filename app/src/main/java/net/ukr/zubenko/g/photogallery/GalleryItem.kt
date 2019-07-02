package net.ukr.zubenko.g.photogallery

data class GalleryItem(val mId: String, val mCaption: String, val mUrl: String) {
    override fun toString() = mCaption
}