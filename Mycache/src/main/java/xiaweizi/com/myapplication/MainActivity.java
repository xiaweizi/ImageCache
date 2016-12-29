package xiaweizi.com.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import xiaweizi.com.myapplication.utils.MyBitmapUtils;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_cache;
    private MyBitmapUtils utils;
    private String url = "http://ossweb-img.qq.com/upload/adw/image/1481265915/1481265915.jpg";
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_cache = (ImageView) findViewById(R.id.iv_cache);
        utils = new MyBitmapUtils();
    }

    public void start(View view){
        utils.disPlay(iv_cache, url);
    }
}
