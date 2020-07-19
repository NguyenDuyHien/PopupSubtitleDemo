package com.example.popupsubtitledemo.ui

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.popupsubtitledemo.R
import com.example.popupsubtitledemo.ulti.REQUEST_OVERLAY_PERMISSION
import kotlinx.android.synthetic.main.fragment_get_draw_overlay_permission.*

class GetDrawOverlayPermissionFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_get_draw_overlay_permission, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        grant_permission_btn.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:${activity!!.packageName}"))
                activity!!.startActivityForResult(intent, REQUEST_OVERLAY_PERMISSION)
            }
        }
    }

    companion object {
        fun newInstance(): GetDrawOverlayPermissionFragment {
            return GetDrawOverlayPermissionFragment()
        }
    }
}