package xiaweizi.com.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


/**
 * @项目名: MyApplication
 * @包名: xiaweizi.com.myapplication.utils
 * @类名: LocalCacheUtils
 * @创建时间: 2016-12-10  10:37
 * @创建者: 夏韦子
 * 三级缓存之本地缓存
 */
public class LocalCacheUtils {

    private static final String TAG        = "LocalCacheUtils-->";
    private static final String CACHE_PATH =
            Environment.getExternalStorageDirectory().getAbsolutePath();

    public Bitmap getBitmapFromLocal (String url) {
        Logger.t(TAG).d("本地缓存的路径:" + CACHE_PATH);
        try {
            String fileName = null;//把图片的url当作文件名，并进行MD5加密

            fileName = MD5Encoder.encode(url);
            Logger.t(TAG).d("本地缓存的文件名称" + fileName);

            File   file   = new File(CACHE_PATH, fileName);
            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 从网络获取图片后，保存之本地缓存
     */
    public void setBitmapToLocal (String url, Bitmap bitmap) {
        try {
            String fileName = MD5Encoder.encode(url);
            File   file     = new File(CACHE_PATH, fileName);

            //通过得到文件的父文件，判断父文件是否存在
            File parentFile = file.getParentFile();
            if (!parentFile.exists()) {
                parentFile.mkdirs();
            }
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
