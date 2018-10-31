package p03.example.li.xuncha;

/**
 * Created by li on 2018/2/24.
 */

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.support.v4.util.LruCache;
import android.util.Log;

import libcore.io.DiskLruCache;
//import com.javen.volley.cache.DiskLruCache.Snapshot;

/**
 * 图片缓存帮助类
 * 包含内存缓存LruCache和磁盘缓存DiskLruCache
 *
 * @author Javen
 */
public class ImageCacheUtil {

    private String TAG = ImageCacheUtil.this.getClass().getSimpleName();
    private static DiskLruCache mDiskLruCache;
    //磁盘缓存大小
    private static final int DISKMAXSIZE = 100 * 1024 * 1024;

    public ImageCacheUtil(Context context) {
        try {
            // 获取DiskLruCahce对象
//            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(MyApplication.newInstance(), "xxxxx"), getAppVersion(MyApplication.newInstance()), 1, DISKMAXSIZE);
            mDiskLruCache = DiskLruCache.open(getDiskCacheDir(context.getApplicationContext(), "xxxxx"), getAppVersion(context), 1, DISKMAXSIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 从缓存（磁盘缓存）中获取Bitmap
     */
    public List<Bitmap> getBitmap(String url) {
            String key = MD5Utils.md5(url);
            try {
                List<Bitmap> abitmap = new ArrayList<Bitmap>();
                while (mDiskLruCache.get(key) != null) {
                    // 从DiskLruCahce取
                    DiskLruCache.Snapshot snapshot = mDiskLruCache.get(key);
                    Bitmap bitmap = null;
                    if (snapshot != null) {
                        bitmap = BitmapFactory.decodeStream(snapshot.getInputStream(0));
                        abitmap.add(bitmap);
                        url = url + "a";
                        key = MD5Utils.md5(url);
                        Log.i(TAG, "从DiskLruCahce获取");
                    }
                }
                return abitmap;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
    }

    /**
     * 存入缓存（内存缓存，磁盘缓存）
     */
    public void putBitmap(String url, Bitmap bitmap) {
        // 判断是否存在DiskLruCache缓存，若没有存入
        if (bitmap != null) {
            String key = MD5Utils.md5(url);
            try {
                while (mDiskLruCache.get(key) != null) {
                    url = url + "a";
                    key = MD5Utils.md5(url);
                }
                if (mDiskLruCache.get(key) == null) {
                    DiskLruCache.Editor editor = mDiskLruCache.edit(key);
                    if (editor != null) {
                        OutputStream outputStream = editor.newOutputStream(0);
                        if (bitmap.compress(CompressFormat.JPEG, 100, outputStream)) {
                            editor.commit();
                            System.out.println("写入成功");
                        } else {
                            editor.abort();
                        }
                    }
                    mDiskLruCache.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 该方法会判断当前sd卡是否存在，然后选择缓存地址
     *
     * @param context
     * @param uniqueName
     * @return
     */
    public static File getDiskCacheDir(Context context, String uniqueName) {
        String cachePath;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()) || !Environment.isExternalStorageRemovable()) {
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            cachePath = context.getCacheDir().getPath();
        }
        return new File(cachePath + File.separator + uniqueName);
    }

    /**
     * 获取应用版本号
     *
     * @param context
     * @return
     */
    public int getAppVersion(Context context) {
        try {
            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            e.printStackTrace();
        }
        return 1;
    }

}
