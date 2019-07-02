package net.ukr.zubenko.g.photogallery

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar

class PhotoGalleryFragment: Fragment() {
    private lateinit var mPhotoRecyclerView: RecyclerView
    private lateinit var mProgressWheel: ProgressBar
    private val mItems = mutableListOf<GalleryItem>()
    private var nextPage = 0
    private var isLoading = false
    private var isLayoutSet = false

    companion object {
        const val TAG = "PhotoGalleryFragment"
        fun newInstance() = PhotoGalleryFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        loadNewPage()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v = inflater.inflate(R.layout.fragment_photo_gallery, container, false)
        isLayoutSet = false
        mPhotoRecyclerView = v.findViewById(R.id.photo_recycler_view)
        mProgressWheel = v.findViewById(R.id.progress_wheel)
        mPhotoRecyclerView.adapter = PhotoAdapter(mItems, requireActivity())

        mPhotoRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (!isLoading && dy > 0 && !mPhotoRecyclerView.canScrollVertically(RecyclerView.FOCUS_DOWN)) {
                    isLoading = true
                    mProgressWheel.visibility = View.VISIBLE
                    loadNewPage()
                }
            }

        })

        mPhotoRecyclerView.viewTreeObserver?.addOnGlobalLayoutListener {
            if (!isLayoutSet) {
                isLayoutSet = true
                mPhotoRecyclerView.layoutManager = GridLayoutManager(requireContext(), mPhotoRecyclerView.width / 360)
            }
        }

        return v
    }

    private fun loadNewPage() {
        FetchPhotosTask(::setUpAdapter, nextPage).execute()
        nextPage++
    }

    private fun setUpAdapter(items: List<GalleryItem>) {
        if (isAdded) {
            mItems.addAll(items)
                (mPhotoRecyclerView.adapter as? PhotoAdapter)?.mGalleryItems?.addAll(items)
                mPhotoRecyclerView.adapter?.notifyDataSetChanged()
        }
        isLoading = false
        mProgressWheel.visibility = View.GONE
    }
}