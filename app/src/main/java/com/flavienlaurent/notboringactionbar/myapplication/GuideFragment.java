package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
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
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2016-10-08.
 */

public class GuideFragment extends Fragment {
    final static Integer maxFisherySize = 3;
    TextView result1;
    TextView result2;

    //SQLite 생성
    DBHelper dbHelper;

    DBHelper myFisheryDB;

    ImageView select_blur1;

    private ImageView imageView;
    public TextView selectNickname;
    public TextView selectAge;
    public TextView selectSpeech;
    public TextView selectArea;
    public String key1 = null;
    public String[] key = {"0","0","0","0"};
    public String nickname = null;
    public String age = null;
    public String area = null;
    public String speech = null;
    long now = System.currentTimeMillis();
    Date date = new Date(now);

    Button btn_put1;
//    Button btn_cancel1;

    ImageView put1;
    ImageView cancel1;
    Button btn_gofishry;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setRetainInstance(true);

    }


    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        GetPhotoAndCheck1 getPhotoAndCheck1 = new GetPhotoAndCheck1();
        getPhotoAndCheck1.execute();


    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_guide, container, false);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        // 데이터 저장
        outState.putInt("data", Integer.parseInt(key1));
    }
//    @Override
//    public void onPrepareOptionsMenu(Menu menu) {
//        menu.findItem(R.id.).setVisible(false);
//        super.onPrepareOptionsMenu(menu);
//    }
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        dbHelper = new DBHelper(getView().getContext(), "People.db", null, 1);
        myFisheryDB = new DBHelper(getView().getContext(), "MyFishery.db", null, 1);
        dbHelper.ini();
        myFisheryDB.ini();

        select_blur1 = (ImageView) getView().findViewById(R.id.select_blur1);

        imageView = (ImageView) getView().findViewById(R.id.select_image1);
        selectNickname = (TextView) getView().findViewById(R.id.select_nickname1);
        selectAge = (TextView) getView().findViewById(R.id.select_age1);
        selectSpeech = (TextView) getView().findViewById(R.id.select_speech1);
        selectArea = (TextView) getView().findViewById(R.id.select_area1);

//        result1 = (TextView) getView().findViewById(R.id.result1);
//        result2 = (TextView) getView().findViewById(R.id.result2);

        put1 = (ImageView) getView().findViewById(R.id.check1);
        cancel1 = (ImageView) getView().findViewById(R.id.cancel1);

        btn_put1 = (Button) getView().findViewById(R.id.select_putbtn1);

     //   btn_cancel1 = (Button) getView().findViewById(R.id.select_cancelbtn1);

        btn_gofishry = (Button) getView().findViewById(R.id.select_gofishery);

        put1.setVisibility(View.INVISIBLE);
        select_blur1.setVisibility(View.INVISIBLE);
        cancel1.setVisibility(View.INVISIBLE);

        btn_gofishry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), NoBoringActionBarActivity.class);
                startActivity(intent);
            }
        });

        btn_put1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("정말 담으시겠습니까??").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                final String[] items = new String[maxFisherySize];

                                if(myFisheryDB.getCount() == maxFisherySize){
                                    //Toast.makeText(getActivity(),"어장이 꽉 찼습니다! 어장에서 한 명을 빼세요.", Toast.LENGTH_LONG);
                                    new AlertDialog.Builder(getActivity())
                                            .setTitle("어장이 꽉 찼습니다!")
                                            .setMessage("어장으로 가셔서 한 명을 빼주세요.")
                                            .setIcon(R.drawable.cancel)
                                            .show();
                                } else {
                                    myFisheryDB.insert(date.toString(), key1);  //확인버튼 누르면 해당 매칭된 사람을 내부 SQLite에 넣음.
                                 //   result1.setText(myFisheryDB.getResult()); //내부 SQLite를 출력하기 위한 코드

                                    select_blur1.setVisibility(View.VISIBLE);
                                    put1.setVisibility(View.VISIBLE);
//                                    btn_cancel1.setEnabled(false);
                                    btn_put1.setEnabled(false);
                                }
                                Log.d("count", String.valueOf(myFisheryDB.getCount()));
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });
/*
        btn_cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(getActivity());
                alert_confirm.setMessage("정말 담지 않으시겠습니까??").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                select_blur1.setVisibility(View.VISIBLE);
                                cancel1.setVisibility(View.VISIBLE);
                                btn_cancel1.setEnabled(false);
                                btn_put1.setEnabled(false);
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // 'No'
                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        */
    }

    class GetPhotoAndCheck1 extends AsyncTask<Void, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            key1 = s;
            if(key[0] == "0"){
                key[0] = key1;
            } else if (key[1] == "0"){
                key[1] = key1;
            } else if (key[2] == "0"){
                key[2] = key1;
            } else if (key[3] == "0"){
                key[3] = key1;
            }
            //Glide 라이브러리로 매칭된 사진을 가져옴
         //   Glide.with(GuideFragment.this).load("http://164.125.154.54/uploads/" + key1 + "/1.jpg").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).override(500, 500).into(imageView);
            //매칭된 사람의 프로필을 JSON으로 가져와서 파싱

            getKeyPhp1();
        }

                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            StringBuilder sb = null;
                            while (true) {
                                String link = "http://164.125.154.54/randomkey.php";

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

                        Log.d("key313", String.valueOf(dbHelper.check(sb.toString())));

                                Thread.sleep(100);

                            if (dbHelper.check(sb.toString())) { //true: DB에 없을 때
                                Log.d("key13", sb.toString());

                                dbHelper.insert(date.toString(), sb.toString());
                                break;
                            } else { // false: DB에 있을 때
                                Log.d("key2", sb.toString());
                                continue;
                            }
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

    //매칭된 사람의 프로필을 JSON으로 가져와서 파싱
    private void getKeyPhp1() {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();

                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray ja = new JSONArray(object.getString("result"));
                    JSONObject jo = ja.getJSONObject(0);
                    nickname = jo.getString("Nick_name");
                    speech = jo.getString("Saying");
                    area = jo.getString("Area");
                    age = jo.getString("Birth");

                    selectNickname.setText(nickname);
                    selectSpeech.setText(speech);
                    selectArea.setText(area);
                    selectAge.setText(age);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String link = "http://164.125.154.54/getprofile.php";

                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key1, "UTF-8");

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