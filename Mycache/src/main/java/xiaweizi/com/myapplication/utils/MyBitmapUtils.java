package xiaweizi.com.myapplication.utils;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import xiaweizi.com.myapplication.R;

/**
 * @项目名: MyApplication
 * @包名: xiaweizi.com.myapplication.utils
 * @类名: MyBitmapUtils
 * @创建时间: 2016-12-10  11:25
 * @创建者: 夏韦子
 */
public class MyBitmapUtils {
    private static final String TAG = "MyBitmapUtils";
    private NetCacheUtils netCacheUtils;
    private LocalCacheUtils localCacheUtils;
    private MemoryCacheUtils memoryCacheUtils;

    public MyBitmapUtils () {
        localCacheUtils = new LocalCacheUtils();
        memoryCacheUtils = new MemoryCacheUtils();
        netCacheUtils = new NetCacheUtils(localCacheUtils, memoryCacheUtils);
    }

    public void disPlay(ImageView ivPic, String url){
        ivPic.setImageResource(R.mipmap.ic_launcher);
        Bitmap bitmap = null;

        //内存缓存
        bitmap = memoryCacheUtils.getBitmapFromMemory(url);
        if (bitmap != null){
            ivPic.setImageBitmap(bitmap);
            Logger.t(TAG).i("从内存中获取图片！");
            return;
        }

        bitmap = localCacheUtils.getBitmapFromLocal(url);
        if (bitmap != null){
            ivPic.setImageBitmap(bitmap);
            Logger.t(TAG).i("从本地获取内存！");
            memoryCacheUtils.setBitmapToMemory(url, bitmap);
            Logger.t(TAG).d("从本地获取，但未从内存中获取数据------>缓存到内存中");
            return;
        }
        netCacheUtils.getBitmapFromNet(ivPic, url);
        Logger.t(TAG).d("未从内存和本地获取到数据，只能从网络中加载");
    }
}
