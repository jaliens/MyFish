package com.flavienlaurent.notboringactionbar.myapplication;

import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.flavienlaurent.notboringactionbar.R;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Date;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by user on 2016-12-22.
 */

public class FlipActivity extends AppCompatActivity {

    private AnimatorSet mSetRightOut;
    private AnimatorSet mSetLeftIn;
    private AnimatorSet mSetRightOut1;
    private AnimatorSet mSetLeftIn1;
    private View mCardFrontLayout;
    private View mCardBackLayout;
    private View mCardFrontLayout1;
    private View mCardBackLayout1;
    ImageView back, back1;
    ImageView yes1, yes2;
    ImageView no1, no2;
    FrameLayout card1, card2;
    Button open, put1, put2;
    String key1;
    int maxFisherySize = 3;
    DBHelper myFisheryDB;
    DBHelper dbHelper;

    TextView test;
    TextView test2;

    long now = System.currentTimeMillis();
    Date date = new Date(now);

    boolean select = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flip);

        findViews();
        loadAnimations();
        changeCameraDistance();
    }

    private void changeCameraDistance() {
        int distance = 8000;
        float scale = getResources().getDisplayMetrics().density * distance;

        mCardFrontLayout.setCameraDistance(scale);
        mCardBackLayout.setCameraDistance(scale);

        mCardFrontLayout1.setCameraDistance(scale);
        mCardBackLayout1.setCameraDistance(scale);
    }

    private void loadAnimations() {
        mSetRightOut = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);

        mSetRightOut1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.out_animation);
        mSetLeftIn1 = (AnimatorSet) AnimatorInflater.loadAnimator(this, R.animator.in_animation);
    }

    private void findViews() {

        myFisheryDB = new DBHelper(FlipActivity.this, "MyFishery.db", null, 1);
        dbHelper = new DBHelper(FlipActivity.this, "People.db", null, 1);  //내가 역대 받았던 이성 DB

        myFisheryDB.ini();
        dbHelper.ini();

        for(int i=0; i<2; i++){
            GetKeyAndSetPhoto getKeyAndSetPhoto = new GetKeyAndSetPhoto();
            getKeyAndSetPhoto.execute();
        }

        test = (TextView) findViewById(R.id.test);
        test2 = (TextView) findViewById(R.id.test2);

        put1 = (Button) findViewById(R.id.put1);
        put2 = (Button) findViewById(R.id.put2);

        put1.setEnabled(false);
        put2.setEnabled(false);

        open = (Button) findViewById(R.id.open);
        open.setEnabled(false);

        yes1 = (ImageView) findViewById(R.id.yes1);
        yes2 = (ImageView) findViewById(R.id.yes2);

        no1 = (ImageView) findViewById(R.id.no1);
        no2 = (ImageView) findViewById(R.id.no2);

        back = (ImageView) findViewById(R.id.back);
        back1 = (ImageView) findViewById(R.id.back1);
        mCardBackLayout = findViewById(R.id.card_back);
        mCardFrontLayout = findViewById(R.id.card_front);

        mCardBackLayout1 = findViewById(R.id.card_back1);
        mCardFrontLayout1 = findViewById(R.id.card_front1);

        card1 = (FrameLayout) findViewById(R.id.flipCard1);
        card2 = (FrameLayout) findViewById(R.id.flipCard2);

        //왼쪽 매칭된 사람 버튼
        card1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FlipActivity.this);
                alert_confirm.setMessage("정말로 선택하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                yes1.setVisibility(View.VISIBLE); //왼쪽 선택했다는 표시
                                no2.setVisibility(View.VISIBLE); //오른쪽 선택x 표시

                                card1.setOnClickListener(null); // 리스너 해제
                                card2.setOnClickListener(null);
                                open.setEnabled(true);
                                select = false;
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        //오른쪽 매칭된 사람 버튼
        card2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FlipActivity.this);
                alert_confirm.setMessage("정말로 선택하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                yes2.setVisibility(View.VISIBLE); //왼쪽 선택했다는 표시
                                no1.setVisibility(View.VISIBLE); //오른쪽 선택x 표시

                                card1.setOnClickListener(null);
                                card2.setOnClickListener(null); // 리스너 해제
                                open.setEnabled(true);
                                select = true;
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        //카드 확인하기
        open.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                back.setVisibility(View.VISIBLE);
                back1.setVisibility(View.VISIBLE);

                mSetRightOut.setTarget(mCardFrontLayout);
                mSetLeftIn.setTarget(mCardBackLayout);

                mSetRightOut1.setTarget(mCardFrontLayout1);
                mSetLeftIn1.setTarget(mCardBackLayout1);

                mSetRightOut.start();
                mSetLeftIn.start();

                mSetRightOut1.start();
                mSetLeftIn1.start();

                if(select == false){
                    put1.setEnabled(true);
                } else {
                    put2.setEnabled(true);
                }
                open.setEnabled(false);
                test2.setText(dbHelper.getResult());

            }
        });

        put1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alert_confirm = new AlertDialog.Builder(FlipActivity.this);
                alert_confirm.setMessage("정말로 선택하시겠습니까?").setCancelable(false).setPositiveButton("확인",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which){
                                if(myFisheryDB.getCount() == maxFisherySize){
                                    //Toast.makeText(getActivity(),"어장이 꽉 찼습니다! 어장에서 한 명을 빼세요.", Toast.LENGTH_LONG);
                                    new android.app.AlertDialog.Builder(FlipActivity.this)
                                            .setTitle("어장이 꽉 찼습니다!")
                                            .setMessage("어장으로 가셔서 한 명을 빼주세요.")
                                            .setIcon(R.drawable.no)
                                            .show();
                                } else {
                                    myFisheryDB.insert(date.toString(), key1);  //확인버튼 누르면 해당 매칭된 사람을 내부 SQLite에 넣음.
                                    test.setText(myFisheryDB.getResult()); //내부 SQLite를 출력하기 위한 코드

                                }
                                Log.d("count", String.valueOf(myFisheryDB.getCount()));
                            }
                        }).setNegativeButton("취소",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                return;
                            }
                        });
                AlertDialog alert = alert_confirm.create();
                alert.show();
            }
        });

        put2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(myFisheryDB.getCount() == maxFisherySize){
                    //Toast.makeText(getActivity(),"어장이 꽉 찼습니다! 어장에서 한 명을 빼세요.", Toast.LENGTH_LONG);
                    new android.app.AlertDialog.Builder(FlipActivity.this)
                            .setTitle("어장이 꽉 찼습니다!")
                            .setMessage("어장으로 가셔서 한 명을 빼주세요.")
                            .setIcon(R.drawable.no)
                            .show();
                } else {
                    myFisheryDB.insert(date.toString(), key1);  //확인버튼 누르면 해당 매칭된 사람을 내부 SQLite에 넣음.
                    test.setText(myFisheryDB.getResult()); //내부 SQLite를 출력하기 위한 코드
                }
                Log.d("count", String.valueOf(myFisheryDB.getCount()));
            }
        });
    }


/*
    public void flipCard1(View view) {
        if (!mIsBackVisible1) {
            mSetRightOut.setTarget(mCardFrontLayout);
            mSetLeftIn.setTarget(mCardBackLayout);

            mSetRightOut.start();
            mSetLeftIn.start();

            mIsBackVisible1 = true;
        } else {
            mSetRightOut.setTarget(mCardBackLayout);
            mSetLeftIn.setTarget(mCardFrontLayout);

            mSetRightOut.start();
            mSetLeftIn.start();

            mIsBackVisible1 = false;
        }
    }
*/

    class GetKeyAndSetPhoto extends AsyncTask<Void, Void, String> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(FlipActivity.this, "Please Wait", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            key1 = s;

            //Glide 라이브러리로 매칭된 사진을 가져옴
            if(back.getDrawable() == null){
                Glide.with(FlipActivity.this).load("http://164.125.154.54/uploads/" + key1 + "/1.jpg").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).override(500, 500).into(back);
            } else {
                Glide.with(FlipActivity.this).load("http://164.125.154.54/uploads/" + key1 + "/1.jpg").bitmapTransform(new CropCircleTransformation(new CustomBitmapPool())).override(500, 500).into(back1);
            }


            //매칭된 사람의 프로필을 JSON으로 가져와서 파싱

            //getKeyPhp1();
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