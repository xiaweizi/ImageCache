package xiaweizi.com.myapplication.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.orhanobut.logger.Logger;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * @项目名: MyApplication
 * @包名: xiaweizi.com.myapplication.utils
 * @类名: NetCacheUtils
 * @创建时间: 2016-12-10  11:11
 * @创建者: 夏韦子
 */
public class NetCacheUtils {
    private static final String TAG = "NetCacheUtils";
    private LocalCacheUtils  mLocalCacheUtils;
    private MemoryCacheUtils mMemoryCacheUtils;

    public NetCacheUtils (LocalCacheUtils mLocalCacheUtils, MemoryCacheUtils mMemoryCacheUtils) {
        this.mLocalCacheUtils = mLocalCacheUtils;
        this.mMemoryCacheUtils = mMemoryCacheUtils;
    }

    public void getBitmapFromNet(ImageView ivPic, String url){
        new BitmapTask().execute(ivPic, url);
    }

    class BitmapTask extends AsyncTask<Object, Void, Bitmap>{

        private ImageView ivPic;
        private String url;

        /**
         * 后台耗时操作，存在于子线程
         * @param params
         * @return
         */
        @Override
        protected Bitmap doInBackground (Object... params) {

            ivPic = (ImageView) params[0];
            url = (String) params[1];
            return downLoadBitmap(url);
        }

        @Override
        protected void onProgressUpdate (Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute (Bitmap bitmap) {
            if (bitmap != null){
                ivPic.setImageBitmap(bitmap);
                Logger.t(TAG).d("从网络中获取图片了");

                mLocalCacheUtils.setBitmapToLocal(url, bitmap);
                Logger.t(TAG).d("图片已经缓存到本地中");
                mMemoryCacheUtils.setBitmapToMemory(url, bitmap);
                Logger.t(TAG).d("图片保存到了内存中");
            }
        }
    }

    private Bitmap downLoadBitmap(String url){
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) new URL(url).openConnection();
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            int responseCode = conn.getResponseCode();
            Logger.t(TAG).d("网络回复码为：" + responseCode);
            if (responseCode == 200) {
                //图片压缩
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;//宽高压缩为原来的1/2；
                options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                return bitmap;
            }
        } catch (IOException e) {
            Logger.t(TAG).d(e);
        }finally {
            conn.disconnect();
        }
            return null;
    }
}
