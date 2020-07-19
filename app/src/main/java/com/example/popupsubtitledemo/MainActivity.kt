package com.example.popupsubtitledemo

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.appcompat.app.AppCompatActivity
import com.example.popupsubtitledemo.ui.GetDrawOverlayPermissionFragment
import com.example.popupsubtitledemo.ui.MainFragment
import com.example.popupsubtitledemo.ulti.REQUEST_OVERLAY_PERMISSION
import com.example.popupsubtitledemo.ulti.addFragment
import com.example.popupsubtitledemo.ulti.removeFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        checkDrawOverlayPermission()
    }

    private fun checkDrawOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            when (Settings.canDrawOverlays(this)) {
                false -> addFragment(R.id.fragment_container, GetDrawOverlayPermissionFragment.newInstance())
                true -> {
                    removeFragment(GetDrawOverlayPermissionFragment.newInstance())
                    addFragment(R.id.fragment_container, MainFragment.newInstance())
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_OVERLAY_PERMISSION -> {
                checkDrawOverlayPermission()
            }
        }
    }
}
