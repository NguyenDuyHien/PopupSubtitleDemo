package com.example.popupsubtitledemo.subtitle;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import androidx.annotation.Nullable;
import com.example.popupsubtitledemo.subtitle.cache.SubtitleCache;
import com.example.popupsubtitledemo.subtitle.model.Subtitle;
import com.example.popupsubtitledemo.subtitle.model.TimedTextObject;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

public class DefaultSubtitleEngine implements SubtitleEngine {
    private static final String TAG = DefaultSubtitleEngine.class.getSimpleName();
    private static final int MSG_REFRESH = 0x888;
    private static final int REFRESH_INTERVAL = 100;

    @Nullable
    private HandlerThread mHandlerThread;
    @Nullable
    private Handler mWorkHandler;
    @Nullable
    private List<Subtitle> mSubtitles;
    private UIRenderTask mUIRenderTask;
//    private MediaPlayer mMediaPlayer;
    private SubtitleCache mCache;
    private SubtitleEngine.OnSubtitlePreparedListener mOnSubtitlePreparedListener;
    private SubtitleEngine.OnSubtitleChangeListener mOnSubtitleChangeListener;
    private volatile long currentPlayPosition;

    public DefaultSubtitleEngine() {
        mCache = new SubtitleCache();

    }

//    @Override
//    public void bindToMediaPlayer(final MediaPlayer mediaPlayer) {
//        mMediaPlayer = mediaPlayer;
//    }

    @Override
    public void setSubtitlePath(final String path) {
        doOnSubtitlePathSet();
        if (TextUtils.isEmpty(path)) {
            Log.w(TAG, "loadSubtitleFromRemote: path is null.");
            return;
        }
        mSubtitles = mCache.get(path);
        if (mSubtitles != null && !mSubtitles.isEmpty()) {
            Log.d(TAG, "from cache.");
            notifyPrepared();
            return;
        }
        SubtitleLoader.loadSubtitle(path, new SubtitleLoader.Callback() {
            @Override
            public void onSuccess(final TimedTextObject timedTextObject) {
                if (timedTextObject == null) {
                    Log.d(TAG, "onSuccess: timedTextObject is null.");
                    return;
                }
                final TreeMap<Integer, Subtitle> captions = timedTextObject.captions;
                if (captions == null) {
                    Log.d(TAG, "onSuccess: captions is null.");
                    return;
                }
                mSubtitles = new ArrayList<>(captions.values());
                notifyPrepared();
                mCache.put(path, new ArrayList<>(captions.values()));
            }

            @Override
            public void onError(final Exception exception) {
                Log.e(TAG, "onError: " + exception.getMessage());
            }
        });
    }

    private void doOnSubtitlePathSet() {
        reset();
        createWorkThread();
    }

    @Override
    public void reset() {
        stopWorkThread();
        mSubtitles = null;
        mUIRenderTask = null;

    }

    @Override
    public void start() {
        Log.d(TAG, "start: ");
//        if (mMediaPlayer == null) {
//            Log.w(TAG, "MediaPlayer is not bind, You must bind MediaPlayer to "
//                    + SubtitleEngine.class.getSimpleName()
//                    + " before start() method be called,"
//                    + " you can do this by call " +
//                    "bindToMediaPlayer(MediaPlayer mediaPlayer) method.");
//            return;
//        }
        if (mWorkHandler != null) {
            mWorkHandler.removeMessages(MSG_REFRESH);
            mWorkHandler.sendEmptyMessageDelayed(MSG_REFRESH, REFRESH_INTERVAL);
        }

    }

    @Override
    public void pause() {
        if (mWorkHandler != null) {
            mWorkHandler.removeMessages(MSG_REFRESH);
        }
    }

    @Override
    public void resume() {
        start();
    }

    @Override
    public void stop() {
        if (mWorkHandler != null) {
            mWorkHandler.removeMessages(MSG_REFRESH);
        }
    }

    @Override
    public void destroy() {
        Log.d(TAG, "destroy: ");
        stopWorkThread();
        mSubtitles = null;
        mUIRenderTask = null;

    }

    private void createWorkThread() {
        currentPlayPosition = 0;
        mHandlerThread = new HandlerThread("SubtitleFindThread");
        mHandlerThread.start();
        mWorkHandler = new Handler(mHandlerThread.getLooper(), new Handler.Callback() {
            @Override
            public boolean handleMessage(final Message msg) {
                try {
                    long delay = REFRESH_INTERVAL;
//                    if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
//                        long position = mMediaPlayer.getCurrentPosition();
//                        Subtitle subtitle = SubtitleFinder.find(position, mSubtitles);
//                        notifyRefreshUI(subtitle);
//                        if (subtitle != null) {
//                            delay = subtitle.end.mseconds - position;
//                        }
//                    }
                    Subtitle subtitle = SubtitleFinder.find(currentPlayPosition, mSubtitles);
                    notifyRefreshUI(subtitle);
                    if (subtitle != null) {
                        delay = subtitle.end.mseconds - currentPlayPosition;
                    }
                    currentPlayPosition += REFRESH_INTERVAL;
                    if (mWorkHandler != null) {
                        mWorkHandler.sendEmptyMessageDelayed(MSG_REFRESH, delay);
                    }
                } catch (Exception e) {
                    // ignored
                }
                return true;
            }
        });
    }

    private void stopWorkThread() {
        if (mHandlerThread != null) {
            mHandlerThread.quit();
            mHandlerThread = null;
        }
        if (mWorkHandler != null) {
            mWorkHandler.removeCallbacksAndMessages(null);
            mWorkHandler = null;
        }
    }

    private void notifyRefreshUI(final Subtitle subtitle) {
        if (mUIRenderTask == null) {
            mUIRenderTask = new UIRenderTask(mOnSubtitleChangeListener);
        }
        mUIRenderTask.execute(subtitle);
    }

    private void notifyPrepared() {
        if (mOnSubtitlePreparedListener != null) {
            mOnSubtitlePreparedListener.onSubtitlePrepared(mSubtitles);
        }
    }

    @Override
    public void setOnSubtitlePreparedListener(final SubtitleEngine.OnSubtitlePreparedListener listener) {
        mOnSubtitlePreparedListener = listener;
    }

    @Override
    public void setOnSubtitleChangeListener(final SubtitleEngine.OnSubtitleChangeListener listener) {
        mOnSubtitleChangeListener = listener;
    }

}
