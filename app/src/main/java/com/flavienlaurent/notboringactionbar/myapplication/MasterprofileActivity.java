package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.flavienlaurent.notboringactionbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2016-09-28.
 */
public class MasterprofileActivity extends AppCompatActivity {

    TextView master_nickname;
    TextView master_age;
    TextView master_area;
    TextView master_speech;
    ImageView master_img;
    ImageView master_imgblur;
    String key;
    Button master_btn;
    Integer flag = 0;

    String real_nickname;
    String real_age;
    String real_area;
    String real_speech;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masterprofile);

        master_nickname = (TextView) findViewById(R.id.master_nickname1);
        master_age = (TextView) findViewById(R.id.master_age1);
        master_area = (TextView) findViewById(R.id.master_area1);
        master_speech = (TextView) findViewById(R.id.master_speech1);
        master_img = (ImageView) findViewById(R.id.master_image1);
        master_btn = (Button) findViewById(R.id.master_btn);
        master_imgblur = (ImageView) findViewById(R.id.master_imageblur);
        Intent intent = getIntent();
        key = intent.getExtras().getString("key");

        getKeyPhp1();

        master_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(flag == 0){
                    Glide.with(MasterprofileActivity.this).
                            load("http://164.125.154.54/uploads/" + key + "/1.jpg").
                            bitmapTransform(new CropCircleTransformation( new CustomBitmapPool())).override(500, 500).
                            into(master_img);
                    flag++;
                } else if (flag == 1){
                    master_nickname.setText(real_nickname);
                    flag++;
                } else if (flag == 2) {
                    master_speech.setText(real_speech);
                    flag++;
                } else if (flag == 3) {
                    master_area.setText(real_area);
                    flag++;
                } else if (flag == 4) {
                    master_age.setText(real_age);
                    flag++;
                }
            }
        });
    }

    private void getKeyPhp1() {
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(MasterprofileActivity.this, "Please Wait", null, true, true);
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
                    String speech = jo.getString("Saying");
                    String area = jo.getString("Area");
                    String age = jo.getString("Birth");

                    Glide.with(MasterprofileActivity.this).
                            load("http://164.125.154.54/uploads/" + key + "/1.jpg").
                            bitmapTransform(new BlurTransformation(MasterprofileActivity.this), new CropCircleTransformation( new CustomBitmapPool())).override(500, 500).
                            into(master_img);

                    real_age = age;
                    real_area = area;
                    real_nickname = nickname;
                    real_speech = speech;

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String link = "http://164.125.154.54/getprofile.php";

                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key, "UTF-8");

                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute();
    }

    public class CustomBitmapPool implements BitmapPool {
        @Override
        public int getMaxSize() {
            return 0;
        }

        @Override
        public void setSizeMultiplier(float sizeMultiplier) {

        }

        @Override
        public boolean put(Bitmap bitmap) {
            return false;
        }

        @Override
        public Bitmap get(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public Bitmap getDirty(int width, int height, Bitmap.Config config) {
            return null;
        }

        @Override
        public void clearMemory() {
        }

        @Override
        public void trimMemory(int level) {
        }
    }

}