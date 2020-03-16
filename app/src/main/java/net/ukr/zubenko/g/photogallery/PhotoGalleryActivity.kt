package net.ukr.zubenko.g.photogallery

import android.content.Context
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import net.ukr.zubenko.g.criminalintent.SingleFragmentActivity
import android.content.Intent



class PhotoGalleryActivity : SingleFragmentActivity() {
    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, PhotoGalleryActivity::class.java)
        }
    }
    override fun createFragment() = PhotoGalleryFragment.newInstance()
}
