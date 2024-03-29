package com.example.popupsubtitledemo.subtitle.cache;

import androidx.annotation.Nullable;
import com.example.popupsubtitledemo.subtitle.model.Subtitle;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.List;

/**
 * @author AveryZhong.
 */

public class SubtitleCache {

    private HashMap<String, List<Subtitle>> mCache = new HashMap<>();

    public synchronized void put(String key, List<Subtitle> subtitles) {
        String md5Key = getMD5(key);
        if (md5Key == null) {
            return;
        }
        mCache.put(md5Key, subtitles);
    }

    @Nullable
    public List<Subtitle> get(String key) {
        String md5Key = getMD5(key);

        if (md5Key == null) {
            return null;
        }
        return mCache.get(md5Key);
    }

    private static String getMD5(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(str.getBytes());
            return new BigInteger(1, md.digest()).toString(16);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
