package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.flavienlaurent.notboringactionbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 2016-09-06.
 */

public class MasterActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private List<GamesItem> items = new ArrayList<>();
    private GamesItem[] item = new GamesItem[6];
    int leng=0;
    Bitmap bit1;
    Drawable drawable1;
    public String[] keys = {"2","4","5"};

    public String matchingKey = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_master);

        bit1 = new GetImage.ImageLoader().getBitmapImg("1/1.jpg");
        drawable1 = getDrawableFromBitmap(bit1);


        recyclerView = (RecyclerView) findViewById(R.id.recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(linearLayoutManager);

        linearLayoutManager = new GridLayoutManager(this, 2);
        recyclerView.setLayoutManager(linearLayoutManager);

        GetKey getKey = new GetKey();
        getKey.execute();
/*
        for (int i = 0; i < 4; i++)
            items.add(item[i]);*/
    }

    public Drawable getDrawableFromBitmap(Bitmap bitmap){
        Drawable d = new BitmapDrawable(bitmap);

        return d;
    }

    //매칭된 사람의 프로필을 JSON으로 가져와서 파싱
    private void getKeyPhp1(final String _key) {
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            String key = _key;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MasterActivity.this, "Please Wait", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {

                    JSONObject object = new JSONObject(s);
                    JSONArray ja = new JSONArray(object.getString("result"));
                    JSONObject jo = ja.getJSONObject(0);

                    String nickname = jo.getString("Nick_name");
                    String area = jo.getString("Area");
                    String age = jo.getString("Age");

                    key += "/1.jpg";

                    bit1 = new GetImage.ImageLoader().getBitmapImg(key);
                    drawable1 = getDrawableFromBitmap(bit1);
                    // Adapter 생성

                    Log.d("area", area);
                    Log.d("age", age);

//                    items.add(drawable1, nickname, area + " " + age,_key) ;
                    item[leng] = new GamesItem(drawable1, area + " " + age);
                    items.add(item[leng]);

                    recyclerView.setAdapter(new RecycleAdapter(getApplicationContext(), items, R.layout.activity_main));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String link = "http://164.125.154.54/getprofile.php";

                    //Log.d("key", _key);
                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(_key, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    Thread.sleep(100);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    Log.d("st", sb.toString());
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute();
    }

    //랜덤으로 key값을 뽑아와서 SQLite에서 중복검사. (첫번째 매칭)
    class GetKey extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            matchingKey = s;
            //keys = matchingKey.split("k");

         /*   adapter = new ListViewAdapter();
            // 리스트뷰 참조 및 Adapter달기
            listview = (ListView) findViewById(R.id.listview1);
            listview.setAdapter(adapter);
*/
            for(int i=0; i<keys.length; i++) {
                getKeyPhp1(keys[i]);
            }


        }

        @Override
        protected String doInBackground(Void... params) {
            try {
                StringBuilder sb = null;

                String link = "http://164.125.154.54/test2.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                sb = new StringBuilder();
                String line = null;

                // Read Server Response(랜덤으로 뽑아온 key 값을 읽어옴)
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }

    public static class GetImage {

        public final static class ImageLoader {

            private final String serverUrl = "http://164.125.154.54/uploads/";

            public ImageLoader() {

                new ThreadPolicy();
            }

            public Bitmap getBitmapImg(String imgStr) {

                Bitmap bitmapImg = null;
                String utf8Input = new String(Charset.forName("UTF-8").encode(imgStr).array());

                try {

                    URL url = new URL(serverUrl + utf8Input);
                    // Character is converted to 'UTF-8' to prevent broken

                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
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

        public final static class ThreadPolicy {

            // For smooth networking
            public ThreadPolicy() {

                StrictMode.ThreadPolicy policy =
                        new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
            }
        }
    }
}
