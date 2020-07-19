package com.example.popupsubtitledemo.subtitle;

import androidx.annotation.Nullable;
import com.example.popupsubtitledemo.subtitle.model.Subtitle;

import java.util.List;

public interface SubtitleEngine {

    void setSubtitlePath(String path);

    void start();

    void pause();

    void resume();

    void stop();

    void reset();

    void destroy();

//    void bindToMediaPlayer(MediaPlayer mediaPlayer);

    void setOnSubtitlePreparedListener(OnSubtitlePreparedListener listener);

    void setOnSubtitleChangeListener(OnSubtitleChangeListener listener);

    interface OnSubtitlePreparedListener {
        void onSubtitlePrepared(@Nullable List<Subtitle> subtitles);
    }

    interface OnSubtitleChangeListener {
        void onSubtitleChanged(@Nullable Subtitle subtitle);
    }

}