package xiaweizi.com.myapplication.utils;

import android.graphics.Bitmap;
import android.util.LruCache;

import com.orhanobut.logger.Logger;

/**
 * @项目名: MyApplication
 * @包名: xiaweizi.com.myapplication.utils
 * @类名: MemoryCacheUtils
 * @创建时间: 2016-12-10  11:03
 * @创建者: 夏韦子
 */
public class MemoryCacheUtils {
    private static final String TAG = "MemoryCacheUtils";

    private LruCache<String, Bitmap> mMemoryCache;

    public MemoryCacheUtils () {
        long maxMemory = Runtime.getRuntime().maxMemory() / 8;//得到手机的最大允许内存的八分之一
        Logger.t(TAG).d("手机允许的单个应用的内存值：" + maxMemory);
        //需要传入允许的内存最大值，虚拟机默认内存为16M, 真机不一定相同
        mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory) {
            //用于计算每个条目的大小
            @Override
            protected int sizeOf (String key, Bitmap value) {
                int byteCount = value.getByteCount();
                Logger.t(TAG).d("每个bitmap对象的大小：" + byteCount);
                return byteCount;
            }
        };
}

    /**
     * 往内存中写图片
     *
     * @param url
     * @param bitmap
     */
    public void setBitmapToMemory (String url, Bitmap bitmap) {
        mMemoryCache.put(url, bitmap);
    }

    public Bitmap getBitmapFromMemory (String url) {
        Bitmap bitmap = mMemoryCache.get(url);
        return bitmap;
    }
}
