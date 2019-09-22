package com.sephiroth.androidmvcdemo;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.sephiroth.androidmvcdemo.bean.ImageBean;

public class MainActivity extends AppCompatActivity implements CallBack {
    private static final String PATH = "http://portrait8.sinaimg.cn/2926314135/blog/180";
    private ImageView imageView;
    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(@NonNull Message message) {
            switch (message.what) {
                case ImageDownload.SUCCESS:
                    imageView.setImageBitmap((Bitmap) message.obj);
                    break;
                case ImageDownload.ERROR:
                    Toast.makeText(MainActivity.this, "下载失败", Toast.LENGTH_LONG).show();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        imageView = findViewById(R.id.imageView);
    }

    /**
     * 获取图片按钮点击事件
     *
     * @param view Bottom
     */
    public void getImage(View view) {
        ImageBean imageBean = new ImageBean();
        imageBean.setRequestPath(PATH);
        new ImageDownload().down(this, imageBean);
    }

    @Override
    public void callBack(int responseCode, ImageBean imageBean) {
        Message message = handler.obtainMessage(responseCode);
        message.obj = imageBean.getBitmap();
        handler.sendMessageDelayed(message, 500);
    }

}
