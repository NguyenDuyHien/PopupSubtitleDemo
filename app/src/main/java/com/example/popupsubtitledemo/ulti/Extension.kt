package com.example.popupsubtitledemo.ulti

import android.content.res.Resources
import android.util.DisplayMetrics
import androidx.annotation.Dimension
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

const val REQUEST_OVERLAY_PERMISSION = 100

fun AppCompatActivity.addFragment(container: Int, _fragment: Fragment, tag: String = _fragment::class.java.simpleName) {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    fragment?: run {
        supportFragmentManager
                .beginTransaction()
                .add(container, _fragment, tag)
                .commit()
    }
}

fun AppCompatActivity.removeFragment(_fragment: Fragment, tag: String = _fragment::class.java.simpleName) {
    val fragment = supportFragmentManager.findFragmentByTag(tag)
    fragment?.let {
        supportFragmentManager
                .beginTransaction()
                .remove(it)
                .commit()
    }
}

@Dimension(unit = Dimension.PX) fun Number.dpToPx(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() * metrics.density
}

@Dimension(unit = Dimension.DP) fun Number.pxToDp(
    metrics: DisplayMetrics = Resources.getSystem().displayMetrics
): Float {
    return toFloat() / metrics.density
}