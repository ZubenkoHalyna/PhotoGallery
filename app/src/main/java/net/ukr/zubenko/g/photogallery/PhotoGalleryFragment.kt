package net.ukr.zubenko.g.photogallery

import android.annotation.SuppressLint
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.os.AsyncTask
import android.util.Log
import java.io.IOException
import kotlin.reflect.KFunction1


class PhotoGalleryFragment: Fragment() {
    private lateinit var mPhotoRecyclerView: RecyclerView

    companion object {
        const val TAG = "PhotoGalleryFragment"
        fun newInstance() = PhotoGalleryFragment()


    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        FetchPhotosTask(::setUpAdapter).execute()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view)
        mPhotoRecyclerView.layoutManager = GridLayoutManager(requireActivity(), 3)

        return v
    }

    private fun setUpAdapter(items: List<GalleryItem>) {
        if (isAdded) {
            mPhotoRecyclerView.adapter = PhotoAdapter(items, requireActivity())
        }
    }
}