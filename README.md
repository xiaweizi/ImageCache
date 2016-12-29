##第一次装B，希望不喜勿喷...
*先附上我的源码Github地址 
https://github.com/xiaweizi/ImageCache.git*
#####图片的缓存虽然现在已经有不少的框架，但是我还是想自己学习一下图片的缓存机制。图片缓存的大概逻辑就是：当需要获取图片的时候，首先判断**内存**是否有，有---加载，无---从**本地**中获取图片，有--加载，无--从网络中下载，然后缓存到内存和本地中。大概流程图如下：


![Paste_Image.png](http://upload-images.jianshu.io/upload_images/4043475-2af93392e70190ab.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

##首先从易到难
####1. 内存缓存
	１. 新建LruCache对象
	private LruCache<String, Bitmap> mMemoryCache;
	2. 在构造函数中初始化LruCache
	        final long maxMemory = Runtime.getRuntime().maxMemory() / 8;
	        mMemoryCache = new LruCache<String, Bitmap>((int) maxMemory){
	            @Override
	            protected int sizeOf (String key, Bitmap value) {
	                int byteCount = value.getByteCount();
	                Log.i(TAG, "每个Bitmap大小：" + byteCount + "每个程序最大内存" + maxMemory);
	                return byteCount;
	            }
	        };
	3. 利用键值对的方式存到内存中
	    public void setBitmapToMemory(Bitmap bitmap, String url){
	        Log.i(TAG, "图片已经保存到内存中: ");
	        mMemoryCache.put(url, bitmap);
	    }
	4. 从内存中取出图片
	    public Bitmap getBitmapFromMemory(String url){
	        return mMemoryCache.get(url);
        }
#####2.本地缓存
	1. 创建要缓存的路径
	private static final String CACHE_PATH = Environment.getExternalStorageDirectory().getAbsolutePath();
	2. 利用输入流进行文件的本地存储，其中对文件名进行了MD5加密
        try {
            Log.i(TAG, "cachePath: " + CACHE_PATH);
            String filename = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH, filename);

            File parentFile = file.getParentFile();
            if (!parentFile.exists()){
                parentFile.mkdirs();
            }
            Log.i(TAG, "图片存到本地中: ");
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(file));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i(TAG, "setBitmapToLocal: " + e);
        }
	3. 利用输出流进行文件的读取
        try {
            String fileName = MD5Encoder.encode(url);
            File file = new File(CACHE_PATH, fileName);

            Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(file));
            Log.i(TAG, "从本地获取图片: ");
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
#####3.从网络中获取图片
	1. 利用构造函数获取LocalCacheUtil和MemoryCacheUtil对象
    public NetCache (LocalCache mLocache, MemoryCache mMemoryCache) {
        this.mLocache = mLocache;
        this.mMemoryCache = mMemoryCache;
    }
	2. 新建私有类，从url中获取图片
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setConnectTimeout(4000);
                conn.setReadTimeout(4000);
                conn.setRequestMethod("GET");
                int responseCode = conn.getResponseCode();
                if (responseCode == 200){
                    //图片压缩
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 2;//宽高压缩为原来的1/2
                    options.inPreferredConfig = Bitmap.Config.ARGB_4444;
                    Bitmap bitmap = BitmapFactory.decodeStream(conn.getInputStream(), null, options);
                    return bitmap;
                }
            } catch (IOException e) {
                e.printStackTrace();
                Log.i(TAG, "downLoadBitmap: " + e);
            }finally {
                conn.disconnect();
            }
        return null;
        }
	3. 利用AsyncTask的方式异步加载数据
    class BitmapTask extends AsyncTask<Object, Integer, Bitmap>{

        @Override
        protected void onPreExecute () {
            Log.i(TAG, "onPreExecute: ");
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate (Integer... values) {
            Log.i(TAG, "onProgressUpdate: ");
            super.onProgressUpdate(values);
        }

        @Override
        protected Bitmap doInBackground (Object... params) {
            Log.i(TAG, "doInBackground: ");
            iv = (ImageView) params[0];
            url = (String) params[1];
            Bitmap bitmap = downLoadBitmap(url);
            return bitmap;
        }

        @Override
        protected void onPostExecute (Bitmap bitmap) {
            Log.i(TAG, "onPostExecute: ");
            if (bitmap != null){
                Log.i(TAG, "已经从网络中获取到图片: ");
                iv.setImageBitmap(bitmap);

                Log.i(TAG, "url: " + url + "Bitmap" + bitmap.getByteCount());
                mMemoryCache.setBitmapToMemory(bitmap, url);
                mLocache.setBitmapToLocal(bitmap, url);

            }
        }
#####4.最后创建一个util进行逻辑的封装，即一开始的流程图
    public class MyBitmapUtil {

    private static final String TAG = "MyBitmapUtil----->";
    private LocalCache mLocal;
    private MemoryCache mMemory;
    private NetCache mNet;

    public MyBitmapUtil () {
        mLocal = new LocalCache();
        mMemory = new MemoryCache();
        mNet = new NetCache(mLocal, mMemory);
    }

    public void disPlay(ImageView iv, String url){
        iv.setImageResource(R.mipmap.ic_launcher);
        Bitmap bitmap = null;

        bitmap = mMemory.getBitmapFromMemory(url);
        if (bitmap != null){
            iv.setImageBitmap(bitmap);
            return;
        }
        bitmap = mLocal.getBitmapFromLocal(url);
        if (bitmap != null){
            iv.setImageBitmap(bitmap);
            mMemory.setBitmapToMemory(bitmap, url);
            return;
        }

        mNet.getBitmapFromNet(iv, url);
    }


    }

*最后附上我的源码Github地址 
https://github.com/xiaweizi/ImageCache.git*
