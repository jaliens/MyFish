package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.widget.TabHost;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;
import com.ogaclejapan.smarttablayout.SmartTabLayout;

/**
 * Created by 도훈 on 2016-11-17.
 */
public class TabWidget extends FragmentActivity {
    public static TabHost tabHost;
    public static boolean dirty = false;
    public static boolean dirty2 = false;
    static boolean once = true;
    static int previousValue = 0;
    static int previousState = -1;
    static boolean flag = true;
    public TextView dduk_bap;
//    private static TabWidget ins;


    ViewPager viewPager;
    FragmentManager manager = null;
    public static FragmentStatePagerAdapter adapter = null;

//    public TabWidget getInstance(){
//        return this.ins;
//    }


    public static void callNotify(){
        adapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
             //앱실행이 처음이 아닐때
            setContentView(R.layout.main);


        dduk_bap = (TextView)findViewById(R.id.ddukbab);
        dduk_bap.setText("77");
        ActionBar bar = getActionBar();
//for color
//for image
     //   bar.setBackgroundDrawable(getResources().getDrawable(R.drawable.titlebar_v2));

            viewPager = (ViewPager) findViewById(R.id.viewpager);
            final SelectActivity newfrag2 = new SelectActivity();
            final NoBoringActionBarActivity newfrag = new NoBoringActionBarActivity();
            adapter = new FragmentStatePagerAdapter(getSupportFragmentManager()) {


                @Override
                public int getItemPosition(Object object) {
//                int position = viewPager.getCurrentItem();
                    int returnvalue = POSITION_UNCHANGED;
//                if(flag) {
//                    //두번 작동해서 첫번째에만 작동하도록.
//                    if (previousValue != position) {
//                        once = false;
//                        if (position == 1 && dirty) {
//                            dirty = false;
////                        Log.d("call notify", "here" + getApplicationContext().getPackageName());
//                            returnvalue = POSITION_NONE;
//                        } else if (position == 0) {
////                        Log.d("call notify", "position :" + position + getApplicationContext().getPackageName());
//                            returnvalue = POSITION_UNCHANGED;
//                        }
//                    } else if (dirty) {
//                        dirty = false;
//                        if (position == 1) {
//                            returnvalue = POSITION_NONE;
//                        }
//                    }
//                    previousState = returnvalue;
//                    flag = false;
//                }else{
//                    returnvalue = previousState;
//                    flag = true;
//                }
//                Log.d("returnvalue :",returnvalue+", to -> position :"+position);

                    if(flag){
                        returnvalue = -1;
                        flag = false;
                    }else{
                        returnvalue = -2;
                        flag = true;
                    }
                    return returnvalue;
                }

                @Override
                public CharSequence getPageTitle(int position) {
                    if(position == 0)  return "";
                    else               return "";
                }

                @Override
                public Fragment getItem(int position) {
                    if (position == 0) {
                        dirty = false;
                        return newfrag2;
                    } else
                        return newfrag;
                }

                @Override
                public int getCount() {
                    return 2;
                }
            };


            viewPager.setAdapter(adapter);
            final SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagertab);
            viewPagerTab.setViewPager(viewPager);
            viewPagerTab.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
                @Override
                public void onPageSelected(int position) {
                    viewPager.setCurrentItem(position, true);
                    adapter.notifyDataSetChanged();
                }
            });
        }
}