package com.sephiroth.androidmvcdemo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.sephiroth.androidmvcdemo.bean.ImageBean;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class ImageDownload {
    static final int SUCCESS = 200;
    static final int ERROR = 404;

    public void down(CallBack callBack, ImageBean imageBean) {
        new Thread(new DownloadImage(callBack, imageBean)).start();
    }

    private static class DownloadImage implements Runnable {

        private final CallBack callback;
        private final ImageBean imageBean;

        public DownloadImage(CallBack callBack, ImageBean imageBean) {
            this.callback = callBack;
            this.imageBean = imageBean;
        }

        @Override
        public void run() {
            try {
                URL url = new URL(imageBean.getRequestPath());
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(5000);
                httpURLConnection.setRequestMethod("GET");

                if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    InputStream inputStream = httpURLConnection.getInputStream();
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    showUi(SUCCESS, bitmap);
                } else {
                    showUi(ERROR, null);
                }
            } catch (Exception e) {
                e.printStackTrace();
                showUi(ERROR, null);
            }

        }

        private void showUi(int responseCode, Bitmap bitmap) {
            if (callback != null) {
                imageBean.setBitmap(bitmap);
                callback.callBack(responseCode, imageBean);
            }
        }
    }
}
