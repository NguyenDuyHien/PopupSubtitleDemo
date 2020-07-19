package com.example.popupsubtitledemo.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.popupsubtitledemo.R
import com.example.popupsubtitledemo.service.PopupTextService
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment() {
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        show_popup_text_btn.setOnClickListener {
            startPopupTextService()
        }
    }

    private fun startPopupTextService() {
        val popupTextService = Intent(activity, PopupTextService::class.java)
        activity!!.stopService(popupTextService)
        activity!!.startService(popupTextService)
    }

    companion object {
        fun newInstance(): MainFragment {
            return MainFragment()
        }
    }
}