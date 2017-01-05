package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
import java.util.ArrayList;
import java.util.List;


public class SeeMore extends AppCompatActivity {
    @Override
    public void onBackPressed() {
        Intent intent3 = new Intent(SeeMore.this, TabWidget.class);
      //  intent3.putExtra("tel", num_textView.getText().toString());
    //    intent3.putExtra("cnum", 1234);
        startActivity(intent3);
        //    break;
        super.onBackPressed();
    }

    private DrawerLayout drawerLayout;
    private ViewPager viewPager;
    private String key1;
    public static String index;
    public static String title;
    public static String php_nickname;
    public static TextView dduk_bap;

    public CoordinatorFragment cd = new CoordinatorFragment();
    public ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());


    //    public void setTitle(String tt){
//        title = tt;
//        Log.d("get!",title+"");
//    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seemore);
        dduk_bap = (TextView) findViewById(R.id.ddukbab_seemore);
        dduk_bap.setText("76");
     //   Toolbar toolbar = (Toolbar)findViewById(R.id.mToolbar);
     //   setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
//        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
//        ab.setDisplayHomeAsUpEnabled(true);

        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
//
//        NavigationView navView = (NavigationView) findViewById(R.id.navigation_view);
//        if (navView != null){
//            setupDrawerContent(navView);
//        }



        ImageView face = (ImageView)findViewById(R.id.face);
        Intent intent = getIntent();
        index = intent.getExtras().getString("index");
        key1 = index;
        face.setImageBitmap( new GetImage.ImageRoader().getBitmapImg(   index  + "/1.jpg"));


        viewPager = (ViewPager)findViewById(R.id.tab_viewpager);
        if (viewPager != null){
            setupViewPager(viewPager);
        }


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabLayout);

        //

//        Log.d("index?L",index+"");

        tabLayout.setupWithViewPager(viewPager);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

    }


    public void setDduk_bap(int dduk){
        CharSequence cs = dduk_bap.getText();
        int mydduck = Integer.parseInt(cs.toString());
        dduk_bap.setText(mydduck - dduk+"");
    }

    public String getKey(){
        return this.index;
    }
    private void setupViewPager(ViewPager viewPager){

        InsertData task = new InsertData();
        task.execute();
//        CoordinatorFragment cd = new CoordinatorFragment();
//        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        Log.d("why",""+php_nickname);

    }

    private void setupDrawerContent(NavigationView navigationView){

    }

    static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager){
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title){
            mFragmentList.add(fragment);
            mFragmentTitleList.add(php_nickname);
        }

        @Override
        public CharSequence getPageTitle(int position){
            return mFragmentTitleList.get(position);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }


    class InsertData extends AsyncTask<String, Void, String> {

        ProgressDialog loading;
        //  public String php_nickname;
        //  public String php_age;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(SeeMore.this, "잠시만 기다려 주세요!", null, true, true);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            loading.dismiss();
            try {
                JSONObject object = new JSONObject(s);
                JSONArray ja = new JSONArray(object.getString("result"));
                JSONObject jo = ja.getJSONObject(0);

                php_nickname = jo.getString("Nick_name");
//                    Log.d("seenickname",php_nickname);
                adapter.addFrag(cd, php_nickname);
                viewPager.setAdapter(adapter);

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


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        switch (id){
//            case android.R.id.home:
//                if (drawerLayout.isDrawerOpen(GravityCompat.START)){
//                    drawerLayout.closeDrawer(GravityCompat.START);
//                } else {
//                    drawerLayout.openDrawer(GravityCompat.START);
//                }
//
//                return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}

//
//import android.app.Activity;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.os.AsyncTask;
//import android.os.Bundle;
//import android.util.Log;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.flavienlaurent.notboringactionbar.R;
//import com.flavienlaurent.notboringactionbar.myapplication.SeeMore;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//
///**
// * Created by 도훈 on 2016-08-17.
// */
//public class SeeMore extends Activity implements View.OnClickListener{
//    Button btn_intent;
//    TextView select_nickname, select_age;
//    ImageView face;
//    String key1 = null;
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.see_more);
//
//        Intent intent = getIntent();
//        String index = intent.getExtras().getString("index");
//        Log.d("sent index",index);
//        key1 = index;
//        face = (ImageView)findViewById(R.id.seemoreface);
//        face.setImageBitmap( new GetImage.ImageRoader().getBitmapImg(   index  + "/1.jpg"));
//      //  select_nickname = (TextView)findViewById(R.id.see_more_nickname);
//     //   select_age = (TextView)findViewById(R.id.see_more_age);
//
//
//        // String s_index = String.valueOf(index);
//
//        btn_intent = (Button)findViewById(R.id.chatroom);
//        btn_intent.setOnClickListener(this);
//        getKeyPhp1();
//    }
//    @Override
//    public void onClick(View v) {
//        // TODO Auto-generated method stub
//        Intent intent = new Intent(this, NoBoringActionBarActivity.class);
//        startActivity(intent);
//        finish();
//    }
//    @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, NoBoringActionBarActivity.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
//    }
//
//    /////////////////////////////////////key값을 이용해 프로필 가져오기
//    public void getKeyPhp1() {
//        class InsertData extends AsyncTask<String, Void, String> {
//
//            ProgressDialog loading;
//            public String php_nickname;
//            public String php_age;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(SeeMore.this, "잠시만 기다려 주세요!", null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                try {
//                    JSONObject object = new JSONObject(s);
//                    JSONArray ja = new JSONArray(object.getString("result"));
//                    JSONObject jo = ja.getJSONObject(0);
//                    php_nickname = jo.getString("nickname");
//                    String speech = jo.getString("speech");
//                    String area = jo.getString("area");
//                    php_age = jo.getString("age");
//                  //  Log.d("json",nickname);
//
//                    //잠시 닉넴은 없애음 필요함
//                 //   select_nickname.setText(php_nickname);
//                  //  select_age.setText(php_age);
//
//                    //  select_speech1.setText(speech);
//                 //   select_area1.setText(area);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                try {
//                    String link = "http://164.125.154.55/getprofile.php";
//
//                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key1, "UTF-8");
//
//                    URL url = new URL(link);
//                    URLConnection conn = url.openConnection();
//
//                    conn.setDoOutput(true);
//                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//
//                    wr.write(data);
//                    wr.flush();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;
//
//                    // Read Server Response
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                        break;
//                    }
//                    return sb.toString();
//                } catch (Exception e) {
//                    return new String("Exception: " + e.getMessage());
//                }
//            }
//        }
//        InsertData task = new InsertData();
//        task.execute();
//    }
//}
/*
 * Copyright 2014 Soichiro Kashima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

//
//import android.annotation.TargetApi;
//import android.app.ProgressDialog;
//import android.content.Intent;
//import android.content.res.Configuration;
//import android.os.AsyncTask;
//import android.os.Build;
//import android.os.Bundle;
//import android.support.v7.app.ActionBar;
//import android.util.Log;
//import android.view.View;
//import android.widget.AbsListView;
//import android.widget.ArrayAdapter;
//import android.widget.FrameLayout;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.flavienlaurent.notboringactionbar.R;
//import com.flavienlaurent.notboringactionbar.myapplication.BaseActivity;
//import com.github.ksoichiro.android.observablescrollview.ObservableListView;
//import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
//import com.github.ksoichiro.android.observablescrollview.ScrollState;
//import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
//import com.nineoldandroids.view.ViewHelper;
//import com.nineoldandroids.view.ViewPropertyAnimator;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//import org.json.JSONObject;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.io.OutputStreamWriter;
//import java.net.URL;
//import java.net.URLConnection;
//import java.net.URLEncoder;
//import java.util.ArrayList;
//
//public class SeeMore extends BaseActivity implements ObservableScrollViewCallbacks {
//
//    private static final float MAX_TEXT_SCALE_DELTA = 0.3f;
//
//    private ImageView face;
//    private View mOverlayView;
//    private View mListBackgroundView;
//    private TextView mTitleView;
//    private View mFab;
//    private int mActionBarSize;
//    private int mFlexibleSpaceShowFabOffset;
//    private int mFlexibleSpaceImageHeight;
//    private int mFabMargin;
//    private boolean mFabIsShown;
//    private String key1;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_flexiblespacewithimagelistview);
//        mFlexibleSpaceImageHeight = getResources().getDimensionPixelSize(R.dimen.flexible_space_image_height);
//        mFlexibleSpaceShowFabOffset = getResources().getDimensionPixelSize(R.dimen.flexible_space_show_fab_offset);
//        mActionBarSize = getActionBarSize();
//        Intent intent = getIntent();
//        String index = intent.getExtras().getString("index");
//        Log.d("sent index",index);
//        key1 = index;
//        face = (ImageView)findViewById(R.id.see_more_face);
//        face.setImageBitmap( new GetImage.ImageRoader().getBitmapImg(   index  + "/1.jpg"));
////        mImageView = findViewById(R.id.see_more_face);
//        mOverlayView = findViewById(R.id.overlay);
//        ObservableListView listView = (ObservableListView) findViewById(R.id.list_observer);
//        listView.setScrollViewCallbacks(this);
//        mTitleView = (TextView) findViewById(R.id.title);
//
//        // Set padding view for ListView. This is the flexible space.
//        View paddingView = new View(this);
//        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(AbsListView.LayoutParams.MATCH_PARENT,
//                mFlexibleSpaceImageHeight);
//        paddingView.setLayoutParams(lp);
//
//        // This is required to disable header's list selector effect
//        paddingView.setClickable(true);
//
//        listView.addHeaderView(paddingView);
//        setDummyData(listView);
//
//        setTitle(null);
//        mFab = findViewById(R.id.fab);
//        mFab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(SeeMore.this, "FAB is clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        mFabMargin = getResources().getDimensionPixelSize(R.dimen.margin_standard);
//        ViewHelper.setScaleX(mFab, 0);
//        ViewHelper.setScaleY(mFab, 0);
//
//        // mListBackgroundView makes ListView's background except header view.
//        mListBackgroundView = findViewById(R.id.list_background);
//        getKeyPhp1();
//
//
//    }
//
//    @Override
//    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
//        // Translate overlay and image
//        float flexibleRange = mFlexibleSpaceImageHeight - mActionBarSize;
//        int minOverlayTransitionY = mActionBarSize - mOverlayView.getHeight();
//        ViewHelper.setTranslationY(mOverlayView, ScrollUtils.getFloat(-scrollY, minOverlayTransitionY, 0));
//        ViewHelper.setTranslationY(face, ScrollUtils.getFloat(-scrollY / 2, minOverlayTransitionY, 0));
//
//        // Translate list background
//        ViewHelper.setTranslationY(mListBackgroundView, Math.max(0, -scrollY + mFlexibleSpaceImageHeight));
//
//        // Change alpha of overlay
//        ViewHelper.setAlpha(mOverlayView, ScrollUtils.getFloat((float) scrollY / flexibleRange, 0, 1));
//
//        // Scale title text
//        float scale = 1 + ScrollUtils.getFloat((flexibleRange - scrollY) / flexibleRange, 0, MAX_TEXT_SCALE_DELTA);
//        setPivotXToTitle();
//        ViewHelper.setPivotY(mTitleView, 0);
//        ViewHelper.setScaleX(mTitleView, scale);
//        ViewHelper.setScaleY(mTitleView, scale);
//
//        // Translate title text
//        int maxTitleTranslationY = (int) (mFlexibleSpaceImageHeight - mTitleView.getHeight() * scale);
//        int titleTranslationY = maxTitleTranslationY - scrollY;
//        ViewHelper.setTranslationY(mTitleView, titleTranslationY);
//
//        // Translate FAB
//        int maxFabTranslationY = mFlexibleSpaceImageHeight - mFab.getHeight() / 2;
//        float fabTranslationY = ScrollUtils.getFloat(
//                -scrollY + mFlexibleSpaceImageHeight - mFab.getHeight() / 2,
//                mActionBarSize - mFab.getHeight() / 2,
//                maxFabTranslationY);
//        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
//            // On pre-honeycomb, ViewHelper.setTranslationX/Y does not set margin,
//            // which causes FAB's OnClickListener not working.
//            FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mFab.getLayoutParams();
//            lp.leftMargin = mOverlayView.getWidth() - mFabMargin - mFab.getWidth();
//            lp.topMargin = (int) fabTranslationY;
//            mFab.requestLayout();
//        } else {
//            ViewHelper.setTranslationX(mFab, mOverlayView.getWidth() - mFabMargin - mFab.getWidth());
//            ViewHelper.setTranslationY(mFab, fabTranslationY);
//        }
//
//        // Show/hide FAB
//        if (fabTranslationY < mFlexibleSpaceShowFabOffset) {
//            hideFab();
//        } else {
//            showFab();
//        }
//    }
//
//    @Override
//    public void onDownMotionEvent() {
//    }
//
//    @Override
//    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
////        ActionBar ab = getSupportActionBar();
////        if (scrollState == ScrollState.UP) {
////            if (ab.isShowing()) {
////                ab.hide();
////            }
////        } else if (scrollState == ScrollState.DOWN) {
////            if (!ab.isShowing()) {
////                ab.show();
////            }
////        }
//    }
//
//    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
//    private void setPivotXToTitle() {
//        Configuration config = getResources().getConfiguration();
//        if (Build.VERSION_CODES.JELLY_BEAN_MR1 <= Build.VERSION.SDK_INT
//                && config.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL) {
//            ViewHelper.setPivotX(mTitleView, findViewById(android.R.id.content).getWidth());
//        } else {
//            ViewHelper.setPivotX(mTitleView, 0);
//        }
//    }
//
//    private void showFab() {
//        if (!mFabIsShown) {
//            ViewPropertyAnimator.animate(mFab).cancel();
//            ViewPropertyAnimator.animate(mFab).scaleX(1).scaleY(1).setDuration(200).start();
//            mFabIsShown = true;
//        }
//    }
//
//    private void hideFab() {
//        if (mFabIsShown) {
//            ViewPropertyAnimator.animate(mFab).cancel();
//            ViewPropertyAnimator.animate(mFab).scaleX(0).scaleY(0).setDuration(200).start();
//            mFabIsShown = false;
//        }
//    }
//
//
//        @Override
//    public void onBackPressed() {
//        Intent intent = new Intent(this, TabWidget.class);
//        startActivity(intent);
//        finish();
//        super.onBackPressed();
//    }
//
//    /////////////////////////////////////key값을 이용해 프로필 가져오기
//    public void getKeyPhp1() {
//        class InsertData extends AsyncTask<String, Void, String> {
//
//            ProgressDialog loading;
//          //  public String php_nickname;
//          //  public String php_age;
//            @Override
//            protected void onPreExecute() {
//                super.onPreExecute();
//                loading = ProgressDialog.show(SeeMore.this, "잠시만 기다려 주세요!", null, true, true);
//            }
//
//            @Override
//            protected void onPostExecute(String s) {
//                super.onPostExecute(s);
//                loading.dismiss();
//                try {
//                    JSONObject object = new JSONObject(s);
//                    JSONArray ja = new JSONArray(object.getString("result"));
//                    JSONObject jo = ja.getJSONObject(0);
//
//                    String php_nickname = jo.getString("Nick_name");
//                    String speech = jo.getString("Saying");
//                    String area = jo.getString("Area");
//                    String tall = jo.getString("Tall");
//                    String smoke_or_not = jo.getString("Smoke_or_not");
//                    String religion = jo.getString("Religion");
//
//                    String php_age = jo.getString("Birth");
//
//                    Log.d("json",php_nickname);
//                    Log.d("json",php_age);
//                    Log.d("json",speech);
//
//
//                    //  Log.d("json",nickname);
//                   // mTitleView.setText(php_nickname);
//                    //잠시 닉넴은 없애음 필요함
//                 //   select_nickname.setText(php_nickname);
//                  //  select_age.setText(php_age);
//
//                    //  select_speech1.setText(speech);
//                 //   select_area1.setText(area);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            protected String doInBackground(String... params) {
//                try {
//                    String link = "http://164.125.154.54/getprofile.php";
//                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key1, "UTF-8");
//
//                    URL url = new URL(link);
//                    URLConnection conn = url.openConnection();
//
//                    conn.setDoOutput(true);
//                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
//
//                    wr.write(data);
//                    wr.flush();
//
//                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//
//                    StringBuilder sb = new StringBuilder();
//                    String line = null;
//
//                    // Read Server Response
//                    while ((line = reader.readLine()) != null) {
//                        sb.append(line);
//                        break;
//                    }
//                    return sb.toString();
//                } catch (Exception e) {
//                    return new String("Exception: " + e.getMessage());
//                }
//            }
//        }
//        InsertData task = new InsertData();
//        task.execute();
//    }
//
//
//}