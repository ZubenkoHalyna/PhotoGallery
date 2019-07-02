package net.ukr.zubenko.g.photogallery

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.Fragment
import net.ukr.zubenko.g.criminalintent.SingleFragmentActivity

class PhotoGalleryActivity : SingleFragmentActivity() {
    override fun createFragment() = PhotoGalleryFragment.newInstance()
}
