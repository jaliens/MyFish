package com.flavienlaurent.notboringactionbar.myapplication;
import android.app.Application;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

public class GetImage {

    public static class ImageRoader {
        public static String encode(String input) {
            StringBuilder resultStr = new StringBuilder();
            for (char ch : input.toCharArray()) {
                if (isUnsafe(ch)) {
                    resultStr.append('%');
                    resultStr.append(toHex(ch / 16));
                    resultStr.append(toHex(ch % 16));
                } else {
                    resultStr.append(ch);
                }
            }
            return resultStr.toString();
        }

        private static char toHex(int ch) {
            return (char) (ch < 10 ? '0' + ch : 'A' + ch - 10);
        }

        private static boolean isUnsafe(char ch) {
            if (ch > 128 || ch < 0)
                return true;
            return " %$&+,/:;=?@<>#%".indexOf(ch) >= 0;
        }
        private final String serverUrl = "http://164.125.154.54/uploads/";

        public ImageRoader() {

            new ThreadPolicy();
        }

        public  Bitmap getBitmapImg(String imgStr) {
         //   Log.d("slash?", imgStr);
            Bitmap bitmapImg = null;
           // imgStr = "164!F1";
            System.setProperty("org.apache.tomcat.util.buf.UDecoder.ALLOW_ENCODED_SLASH", "true");
         //   StringBuilder resultStr = new StringBuilder();
            String utf8Input = new String(Charset.forName("UTF-8").encode(imgStr).array());

            try {
                URL url = new URL(serverUrl + utf8Input);
           //     URL test = new URL( URLEncoder().encode(imgStr,"UTF-8") );
          //      Log.d("fsadffd",url+"");
                // Character is converted to 'UTF-8' to prevent broken

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(false);
                conn.setDoInput(true);
                conn.connect();
                InputStream is = conn.getInputStream();
                bitmapImg = BitmapFactory.decodeStream(is);

            } catch (IOException ioe) {
                ioe.printStackTrace();
            }
            return bitmapImg;
        }
    }

    public static class ThreadPolicy {

        // For smooth networking
        public ThreadPolicy() {

            StrictMode.ThreadPolicy policy =
                    new StrictMode.ThreadPolicy.Builder().permitAll().build();

            StrictMode.setThreadPolicy(policy);
        }
    }
}