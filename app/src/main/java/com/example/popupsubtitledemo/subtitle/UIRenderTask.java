package com.example.popupsubtitledemo.subtitle;

import com.example.popupsubtitledemo.subtitle.model.Subtitle;
import com.example.popupsubtitledemo.subtitle.runtime.AppTaskExecutor;

public class UIRenderTask implements Runnable {

    private Subtitle mSubtitle;
    private SubtitleEngine.OnSubtitleChangeListener mOnSubtitleChangeListener;

    public UIRenderTask(final SubtitleEngine.OnSubtitleChangeListener l) {
        mOnSubtitleChangeListener = l;
    }

    @Override
    public void run() {
        if (mOnSubtitleChangeListener != null) {
            mOnSubtitleChangeListener.onSubtitleChanged(mSubtitle);
        }
    }

    public void execute(final Subtitle subtitle) {
        mSubtitle = subtitle;
        AppTaskExecutor.mainThread().execute(this);
    }
}
