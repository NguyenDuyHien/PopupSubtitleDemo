package com.example.popupsubtitledemo.service

import android.annotation.SuppressLint
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Environment
import android.os.IBinder
import android.view.*
import android.widget.FrameLayout
import android.widget.VideoView
import com.example.popupsubtitledemo.R
import com.example.popupsubtitledemo.subtitle.widget.SimpleSubtitleView
import com.example.popupsubtitledemo.ulti.dpToPx

class PopupTextService : Service() {
    private lateinit var windowManager: WindowManager
    private var subtitleView: SimpleSubtitleView? = null
    private var videoView: VideoView? = null
    private var popupTextView: View? = null

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        windowManager = getSystemService(Context.WINDOW_SERVICE) as WindowManager
        addPopupTextView()
    }

    override fun onDestroy() {
        super.onDestroy()
        popupTextView?.let {
            windowManager.removeView(it)
            popupTextView = null
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun addPopupTextView() {
        val layoutParamsType = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                layoutParamsType,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE or WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT)

        params.gravity = Gravity.CENTER or Gravity.BOTTOM
        params.x = 0
        params.y = 70.dpToPx().toInt()

        val interceptorLayout = object : FrameLayout(this) {
            override fun dispatchKeyEvent(event: KeyEvent): Boolean {
                return false
            }
        }

        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        popupTextView = inflater.inflate(R.layout.layout_popup_text, interceptorLayout)
        windowManager.addView(popupTextView, params)
        subtitleView = popupTextView?.findViewById(R.id.subtitle_view)
//        videoView = popupTextView?.findViewById(R.id.video_view)
        val dir = this.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val path = dir?.absolutePath
//        videoView?.setOnPreparedListener {
//            subtitleView?.bindToMediaPlayer(it)
//
//        }
        subtitleView?.setSubtitlePath("$path/sample.srt")
        subtitleView?.start()
//        videoView?.setVideoPath("$path/sample.mp4")
//        videoView?.start()
    }
}