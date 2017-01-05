package com.flavienlaurent.notboringactionbar.myapplication;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.StrictMode;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import android.app.ActionBar;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.RectF;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SlidingPaneLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.flavienlaurent.notboringactionbar.R;
import com.flavienlaurent.notboringactionbar.myapplication.etc.BkUtils;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Random;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class NoBoringActionBarActivity extends Fragment implements View.OnClickListener{

    ////// 프로필 더보기, 먹이주기 버튼, 방출하기 버튼///
    Button food_btn;
    Button shop_btn;
    Button out_btn;
    Button master_btn;
    ////////////////////////////////
    ///php에서 얻어오기
    public String php_nickname;
    public String php_age;
    String key1;
    /////
    //커스텀 리스트뷰
    ListView listview ;
    private ListViewAdapter adapter;
    ///

    public RequestManager mGlideRequestManager;

    ///합치는 함수 변수들
    private final int MAX = 10;
    private int mPrevPosition;
    private ViewPager mPager;
    private LinearLayout mPageMark;
    private BkPagerAdapter mAdapter;

    ///SQLite
    public ArrayList<String> result = new ArrayList<String>();
    ///////
     DBHelper myFisheryDB = null;
    /////add fish
    private SlidingUpPanelLayout mLayout;

    private RelativeLayout mainLayout;
    private RelativeLayout fishLayout;
    /////////////

    @Override
    public void onResume() {
        super.onResume();
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();
        final LinearLayout        llLayout    = (LinearLayout)    inflater.inflate(R.layout.activity_noboringactionbar, container, false);

       // food_btn = (Button) llLayout.findViewById(R.id.food);
//        shop_btn = (Button) llLayout.findViewById(R.id.button4);
        out_btn = (Button) llLayout.findViewById(R.id.out);
//        master_btn = (Button) llLayout.findViewById(R.id.button1);
        //////////물고기 inflate
        mainLayout = (RelativeLayout)llLayout.findViewById(R.id.background);
        fishLayout = (RelativeLayout)llLayout.findViewById(R.id.dragView);
        //fishLayout = (LinearLayout)findViewById(R.id.test_fish);

        LayoutInflater view = LayoutInflater.from(getActivity());
        view.inflate(R.layout.test_fish, null);

        final ImageView bg_bubble = new ImageView(this.getContext());
        bg_bubble.setImageResource(R.drawable.bg_bubble);
        RelativeLayout.LayoutParams bubble_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        bubble_lp.setMargins(100,1300,0,0);
        bg_bubble.setLayoutParams(bubble_lp);
        mainLayout.addView(bg_bubble);
        Animation bg_anim = AnimationUtils.loadAnimation(getActivity(), R.anim.bg_bubble);
        bg_bubble.startAnimation(bg_anim);

        final ImageView bg_fish = new ImageView(this.getContext());
        bg_fish.setImageResource(R.drawable.bg_fishes);
        RelativeLayout.LayoutParams fishes_lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);
        fishes_lp.setMargins(50,1100,0,0);
        bg_fish.setLayoutParams(fishes_lp);
        mainLayout.addView(bg_fish);
        Animation bg_fish_anim = AnimationUtils.loadAnimation(getContext(), R.anim.bg_fishes);
        bg_fish.startAnimation(bg_fish_anim);
        // // // // // // // ///custom 물고기들을 추가하는곳
        // // // // // // // // // // // // // // // // // //
        // // // // // // // // // // // // // // // // // //
        myFisheryDB = new DBHelper(NoBoringActionBarActivity.this.getContext(), "MyFishery.db", null, 1);
        result = myFisheryDB.getKeys();

        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this.getContext());
//        shop_btn.setOnClickListener(this);
        out_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alt_bld.setMessage("정말 내보내실 건가요 ?").setCancelable(
                        false).setPositiveButton("네",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                myFisheryDB.delete(result.get(mPrevPosition));
                                result = myFisheryDB.getKeys();
                                TabWidget.dirty = true;
                                TabWidget.callNotify();
                            }
                        }).setNegativeButton("아뇨",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Action for 'NO' Button
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = alt_bld.create();
                // Title for AlertDialog
                alert.setTitle("흘려보내기");

                // Icon for AlertDialog
                alert.setIcon(R.drawable.fish);
                alert.show();
            }
        });

//        master_btn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//                Intent intent = new Intent(getActivity().getApplication(), MasterActivity.class);
//                startActivity(intent);
//
//            }
//        });


        for(int i =0; i < result.size(); i ++){
            final ImageView imageView = new ImageView(getContext());

            //setting image resource
            //setting image position
            RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT,RelativeLayout.LayoutParams.WRAP_CONTENT);


            /////////////////////////////
            mainLayout.addView(imageView);
            //  fishLayout.addView(imageView);
            //물고기 애니메이션 추가!!!
            switch(i%result.size()){
                case 0:
                    imageView.setImageResource(R.drawable.goldfish);
                    lp.setMargins(0,0,0,0);
                    imageView.setId(i);
                    //여긴 아이디 0번임
                    //Set id 해주는것이 동적으로 할당하기 위한것...

                    ///////////////////////ANIMATION
                    ////////////////////////////////
                    Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim);
                    anim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim);
                            anim.setAnimationListener(this);
                            imageView.startAnimation(anim);
                            //  hidden_imageView.startAnimation(anim);

                        }
                        @Override
                        public void onAnimationRepeat(Animation animation) {
                            imageView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Log.d("click","click");
                                }
                            });
                        }
                    });
                    imageView.startAnimation(anim);

                    ///////////////////////ANIMATION
                    ////////////////////////////////
                    break;
                case 1:
                    lp.setMargins(100,300,0,0);
                    imageView.setImageResource(R.drawable.fish_other);
                    imageView.setLayoutParams(lp);
                    imageView.setId(i);
                    Animation anim2 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim2);
                    anim2.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //      Log.d("repeat","repeat");
                            Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim2);
                            anim.setAnimationListener(this);
                            imageView.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    imageView.startAnimation(anim2);
                    break;
                case 2:
                    lp.setMargins(500,1000,0,0);
                    imageView.setImageResource(R.drawable.fish_oct);
                    imageView.setLayoutParams(lp);
                    imageView.setId(i);
                    Animation anim3 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim4);
                    anim3.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            //      Log.d("repeat","repeat");
                            Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim4);
                            anim.setAnimationListener(this);
                            imageView.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    imageView.startAnimation(anim3);
                    break;
                case 3:
                    lp.setMargins(200,1100,0,0);
                    imageView.setImageResource(R.drawable.fish_turtle);
                    imageView.setLayoutParams(lp);
                    imageView.setId(i);
                    Animation anim4 = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim3);
                    anim4.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {}
                        @Override
                        public void onAnimationEnd(Animation animation) {
                            Animation anim = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.translate_anim3);
                            anim.setAnimationListener(this);
                            imageView.startAnimation(anim);
                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {}
                    });
                    imageView.startAnimation(anim4);
                    break;
            }


        }

        mGlideRequestManager = Glide.with(this);
////////////////////////////////////////////////////////////////////////////////
        ////////////////////////////////////합친부분/////////////

        mPageMark = (LinearLayout)llLayout.findViewById(R.id.page_mark);
        mPager = (ViewPager)llLayout.findViewById(R.id.pager);
        mAdapter = new BkPagerAdapter(getActivity().getApplication());
        mPager.setAdapter(mAdapter);
        mLayout = (SlidingUpPanelLayout) llLayout.findViewById(R.id.sliding_layout);

        final ImageView[] fishes = new ImageView[20];
        final int[] fish_images = new int[20];
        final int[] fish_low_images = new int[20];
        fish_images[0] = R.drawable.goldfish;
        fish_images[1] = R.drawable.fish_other;
        fish_images[2] = R.drawable.fish_oct;
        fish_images[3] = R.drawable.fish_turtle;
        fish_low_images[0] = R.drawable.goldfish_low;
        fish_low_images[1] = R.drawable.yulfish_low;
        fish_low_images[2] = R.drawable.fish_oct_low;
        fish_low_images[3] = R.drawable.fish_turtle_low;

        mLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {}

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {

                if(newState == SlidingUpPanelLayout.PanelState.COLLAPSED){
                    for(int i = 0; i < result.size(); i++){
                        //사람 순서대로 들어간 물고기 이미지 fishes[i]
                        //모든걸 흐릿한 사진으로 바꾼다.
                        //      Log.d("result size : ",result.size()+"");
                        fishes[i] = (ImageView)llLayout.findViewById(i);
                        fishes[i].setImageResource(fish_images[i]);
                    }
                }else if(newState == SlidingUpPanelLayout.PanelState.EXPANDED) {
                    if (result.size() != 0) {
                        //여기부터는 슬라이드를 위로 올렸을때 "화면"의 물고기를 선택한 해당 물고기로 만드는것.
                        for (int i = 0; i < result.size(); i++) {
                            //사람 순서대로 들어간 물고기 이미지 fishes[i]
                            //모든걸 흐릿한 사진으로 바꾼다.
                            //      Log.d("result size : ",result.size()+"");
                            fishes[i] = (ImageView) llLayout.findViewById(i);
                            fishes[i].setImageResource(fish_low_images[i]);
                        }

                        fishes[mPrevPosition].setImageResource(fish_images[mPrevPosition]);


                    }
                }
            }
        });


        mLayout.setFadeOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override public void onPageSelected(int position) {
                mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_not_bubble_small);
                mPageMark.getChildAt(position).setBackgroundResource(R.drawable.page_select_bubble_small);
                mPrevPosition = position;
//                //이건 슬라이드가 올라와있는 상태에서
                //페이지를 왼쪽/오른쪽으로 넘길때 바뀌는 화면의 물고기.
                for(int i = 0; i < 4; i++){
                    //사람 순서대로 들어간 물고기 이미지 fishes[i]
                    //모든걸 흐릿한 사진으로 바꾼다.
                    fishes[i] = (ImageView)llLayout.findViewById(i);
                    //       fishes[i].setImageResource(fish_images[i]);
                }
                /////////////////////////////////////////////////////
                //지금 선택된 물고기를 골라서
                Log.d("position fishes,size : ",position+ " ," + result.size() );

                if(position == 0){
                    fishes[position].setImageResource(fish_images[position]);
                    if(result.size() != 1)
                        fishes[position+1].setImageResource(fish_low_images[position+1]);
//                }else if(position == result.size()-1){
                }else if(position == result.size()-1){
                    fishes[position].setImageResource(fish_images[position]);
                    if(result.size() != 1)
                        fishes[position-1].setImageResource(fish_low_images[position-1]);
                }else{
                    fishes[position].setImageResource(fish_images[position]);
                    fishes[position-1].setImageResource(fish_low_images[position-1]);
                    fishes[position+1].setImageResource(fish_low_images[position+1]);
                }

                ////////////////
             /*
                switch (position){
                    case 0:
                        ImageView page_fish = (ImageView)findViewById(0);
                        page_fish.setImageResource(R.drawable.goldfish);
                        ImageView page_fish2 = (ImageView)findViewById((position+1)%2);
                        page_fish2.setImageResource(R.drawable.yulfish_low);
                        //0이 선택됐을때 다른 물고기(fish_other_low)는 회색인데, 실제로는 for문을 돌려야할듯

                        break;
                    case 1:
                        ImageView page_fish3 = (ImageView)findViewById(position);
                        page_fish3.setImageResource(R.drawable.fish_other);
                        ImageView page_fish4 = (ImageView)findViewById((position+1)%2);
                        page_fish4.setImageResource(R.drawable.goldfish_low);
                        break;

                }
                */
//이건 해당 위치 외의 모든것을 시커멓게 만드는 것

                //Log.d("changepage",""+mPrevPosition);
                //Page를 선택했을때 현재 Open된 페이지를 select로 바꾸고,
                //이전에 설정해 놓은 위치에 not_select로 바꾼다.
            }
            @Override public void onPageScrolled(int position, float positionOffest, int positionOffsetPixels) {}
            @Override public void onPageScrollStateChanged(int state) {}
        });

        //페이지가 선택된 부분이 아닌 Oncreate인
        //즉, 그냥 초기 화면
        mPrevPosition = 0;

        for(Integer loop = 0; loop < result.size(); loop ++){
            key1 = result.get(loop);
//            Log.d("count in main:",""+result.size());
          //  getKeyPhp1();
            mAdapter.addItem(new CustomFishInfo(new GetImage.ImageRoader().getBitmapImg(key1+"/1.jpg") , loop ) );
            addPageMark(loop);

        }
        //불꺼진 동그라미를 새로 추가하는 부분

       // int imageresource = getResources().getIdentifier("@drawable/page_select_bubble_small", "drawable", getActivity().getPackageName());

     //  mPageMark.getChildAt(mPrevPosition).setBackgroundResource(R.drawable.page_select_bubble_small);
//        mPageMark.getChildAt(mPrevPosition).setBackground(ContextCompat.getDrawable(getActivity(),R.drawable.page_select_bubble_small));
        //image.setImageResource(imageresource);
        //초기에 불켜진 동그라미


        adapter = new ListViewAdapter();
        return llLayout;
    }


    ////////////////////////////새로넣은것
    private void addPageMark(int loop){
        ImageView iv = new ImageView(getActivity().getApplicationContext());
        iv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        iv.setBackgroundResource(R.drawable.page_not_bubble_small);
        mPageMark.addView(iv);
        key1 = result.get(loop);
    }

    @Override
    public void onClick(View view) {

        // TODO Auto-generated method stub
        switch(view.getId()){
            case R.id.button4:
                  startActivity(new Intent(this.getActivity(), SelectActivity.class));
                Log.d("sfdsafa","sadffas");
                break;
        }
    }



    public class CustomFishInfo{
        Bitmap custom_bitmap;
        int custom_index;

        public CustomFishInfo(Bitmap s3, int index){
            this.custom_bitmap = s3;
            this.custom_index = index;
        }
        public Bitmap getCustom_bitmap(){return custom_bitmap;}
        public int getCustom_index(){return custom_index;}
}
    //Pager 아답터 구현
    private class BkPagerAdapter extends PagerAdapter {
        private Context mContext;
        private ArrayList<CustomFishInfo> mItems;
        public BkPagerAdapter( Context con) {
            super(); mContext = con;
            mItems = new ArrayList<>();

        }
        //mContext 에 받아온 context를 넣어준다.

        //뷰 페이저의 아이템 갯수는 리스트의 갯수
        //나중에 뷰 페이저에 아이템을 추가하면 리스트에 아이템의 타입을 추가 후 새로 고침하게 되면
        //자동으로 뷰 페이저의 갯수도 늘어난다.
        @Override public int getCount() { return mItems == null ? 0 : mItems.size(); }

        //뷰페이저에서 사용할 뷰객체 생성/등록



        @Override public Object instantiateItem(View pager, int position)
        {
//슬라이드 뷰를 만들때 객체를 미리 등록하는곳. 어떤 물고기를 쓸지도 여기다가 미리 설정 해놔야한다.
            LayoutInflater inflater = getActivity().getLayoutInflater();
            View _v = null;
            _v = inflater.inflate(R.layout.glide_test, null);
            ImageView img = (ImageView)_v.findViewById(R.id.test);
            //  mGlideRequestManager.load( "http://164.125.154.55/uploads/"+key1+"/1.jpg").bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(img);

            RoundedBitmapDrawable image = RoundedBitmapDrawableFactory.create(getResources(),mAdapter.mItems.get(position).getCustom_bitmap());
            image.setCircular(true);

            img.setImageDrawable(image);

            img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Intent intent = new Intent(NoBoringActionBarActivity.this.getActivity(), SeeMore.class);
                    intent.putExtra("index", result.get(mPrevPosition));
                    Log.d("putExtraition : ", mPrevPosition+","+result.get(mPrevPosition));
                    startActivity(intent);
                    getActivity().finish();
                }
            });
            ImageView fish_img = (ImageView)_v.findViewById(R.id.in_fish);
            fish_img.setImageResource(R.drawable.test_fish);

            //0번이면 0번째 위치의 사람에 해당하는 물고기를 설정해주는곳.
            switch(position){
                case 0:
                    fish_img.setImageResource(R.drawable.fish_hole_goldfish);
                    break;
                case 1:
                    fish_img.setImageResource(R.drawable.fish_hole_yul);
                    break;
                case 2:
                    fish_img.setImageResource(R.drawable.fish_oct_hole);
                    break;
                case 3:
                    fish_img.setImageResource(R.drawable.fish_turtle_hole);
                    break;
                default:
                    break;
            }


            CustomFishInfo get_item = mItems.get(position);
            TextView nickname = (TextView)_v.findViewById(R.id.in_nickname);
            TextView age = (TextView)_v.findViewById(R.id.in_age);

            nickname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
            nickname.setTextColor(Color.WHITE);

            age.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
            age.setTextColor(Color.WHITE);

            //해당 포지션의 아이템 뷰를 type으로 설정한뒤, 그 type에 맞는 Context를 설정한다.
            View v = BkUtils.getView(mItems.get(position) , mContext);
            ((ViewPager)pager).addView(_v);


            return _v;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View)object);
        }

        @Override public boolean isViewFromObject(View view, Object obj) { return view == obj; }

        @Override public void finishUpdate(View arg0) {}
        @Override public void restoreState(Parcelable arg0, ClassLoader arg1) {}
        @Override public Parcelable saveState() { return null; }
        @Override public void startUpdate(View arg0) {}



        public void addItem(CustomFishInfo e){
            mItems.add(e);                //리스트 아이템 목록에 추가
            notifyDataSetChanged();        //아답터에 데이터 변경되었다고 알림. 알아서 새로고침
        }
    }

}