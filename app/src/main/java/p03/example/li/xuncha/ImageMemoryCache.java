package p03.example.li.xuncha;

/**
 * Created by li on 2018/2/21.
 */

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import android.graphics.Bitmap;
/****
  * 内存中的缓存
 ****/
public class ImageMemoryCache {
    private static final int HARD_CACHE_CAPACITY = 30;
    private HashMap<String, Bitmap> mHardBitmapCache;
    private final static ConcurrentHashMap<String, SoftReference<Bitmap>> mSoftBitmapCache =
            new ConcurrentHashMap<String, SoftReference<Bitmap>>(
                   HARD_CACHE_CAPACITY / 2);
    public ImageMemoryCache() {
        mHardBitmapCache = new LinkedHashMap<String, Bitmap>(
                HARD_CACHE_CAPACITY / 2, 0.75f, true) {
            private static final long serialVersionUID = 1L;

            @Override
            protected boolean removeEldestEntry(
                    LinkedHashMap.Entry<String, Bitmap> eldest) {
                if (size() > HARD_CACHE_CAPACITY) {
                    // Entries push-out of hard reference cache are transferred
                    // to soft reference cache
                    mSoftBitmapCache.put(eldest.getKey(),
                            new SoftReference<Bitmap>(eldest.getValue()));
                    return true;
                } else
                    return false;
            }
        };
    }
    /**
     * 从缓存中获取图片
     * */
    public List<Bitmap> getBitmapFromCache(String url) {
        List<Bitmap> abitmap = new ArrayList<Bitmap>();
        // 先从mHardBitmapCache缓存中获取
        synchronized (mHardBitmapCache) {
            while (mHardBitmapCache.containsKey(url)) {
                final Bitmap bitmap = mHardBitmapCache.get(url);
                if (bitmap != null) {
                    mHardBitmapCache.remove(url);
                    mHardBitmapCache.put(url, bitmap);
                    abitmap.add(bitmap);
                }
                url = url + "a";
                System.out.println("size: " + abitmap.size());
                System.out.println("getUrl: " + abitmap.size());
            }
        }
        // 如果mHardBitmapCache中找不到，到mSoftBitmapCache中找
        while (mSoftBitmapCache.contains(url)) {
            SoftReference<Bitmap> bitmapReference = mSoftBitmapCache.get(url);
            if (bitmapReference != null) {
                final Bitmap bitmap = bitmapReference.get();
                if (bitmap != null) {
                    // 将图片移回硬缓存
                    mHardBitmapCache.put(url, bitmap);
                    mSoftBitmapCache.remove(url);
                    abitmap.add(bitmap);
                } else {
                    mSoftBitmapCache.remove(url);
                }
            }
            url = url + "a";
        }
        if (abitmap.size() != 0)
        {
            return abitmap;
        }
        return null;
    }
    /*** 添加图片到缓存 ***/
        public void addBitmapToCache(String url, Bitmap bitmap) {
        if (bitmap != null) {
            while (mHardBitmapCache.containsKey(url))
            {
                url = url + "a";
            }
            synchronized (mHardBitmapCache) {
                mHardBitmapCache.put(url, bitmap);
                System.out.println("key: " + url);
            }
        }
    }
}

