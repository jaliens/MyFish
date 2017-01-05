package com.flavienlaurent.notboringactionbar.myapplication.etc;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AnalogClock;
import android.widget.Button;
import android.widget.DigitalClock;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.bumptech.glide.Glide;
import com.flavienlaurent.notboringactionbar.R;
import com.flavienlaurent.notboringactionbar.myapplication.GetImage;
import com.flavienlaurent.notboringactionbar.myapplication.NoBoringActionBarActivity;

//import pe.sbk.viewpagersample.R;
//import pe.sbk.viewpagersample.myapplication.GetImage;

public class BkUtils {
	public static final int TYPE_TEXTVIEW = 0, TYPE_SCROLLVIEW=1, TYPE_LISTVIEW=2, TYPE_FISH=3;

	/**
	 *
	 * public static View getView(int type, Context con) 메소드
	 * 뷰 페이저에 추가할 뷰를 생성하여 반환한다
	 * @param type - 추가할 뷰 타입
	 * @param con - 뷰 생성에 사용할 Context
	 * @return 뷰 객체
	 */
	public static String nickname, age;
	public static Bitmap bm;
	public static int index;
	public BkUtils(String _nickname, String _age, Bitmap _bm, int _index){
		nickname = _nickname;
		age = _age;
		bm = _bm;
		index = _index;
	}

	public static View getView(NoBoringActionBarActivity.CustomFishInfo type, Context con) {
		Log.d("ingetview",""+nickname);
//		age = type.getCustom_age();
//		nickname = type.getCustom_nickname();
		bm = type.getCustom_bitmap();
		index = type.getCustom_index();

		return fish(con);
	}
	


//	Bitmap bm = (Bitmap)new ImageRoader().getBitmapImg("1.jpg");

	private static RelativeLayout fish(Context con) {
		//Log.d("nick",nickname);

		RelativeLayout rl = new RelativeLayout(con);
	//	LayoutInflater inflater = LayoutInflater.from(con);
	//	inflater.inflate(R.layout.show_people, rl);

		TextView tx_nickname = new TextView(con);
		tx_nickname.setText(nickname);
		tx_nickname.setTextSize(TypedValue.COMPLEX_UNIT_SP, 40);
		tx_nickname.setPadding(10,240,0,0);

		TextView tx_age = new TextView(con);
		tx_age.setText(age);
		tx_age.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		tx_age.setPadding(10,330,0,0);

/*
		RelativeLayout.LayoutParams bottomButtonParams = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.WRAP_CONTENT,
						200);
						//ViewGroup.LayoutParams.WRAP_CONTENT);
		bottomButtonParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, 1);

		Button SeeMore = new Button(con);
		SeeMore.setLayoutParams(bottomButtonParams);
		SeeMore.setText("더보기");
		SeeMore.setId(Integer.valueOf(2));

		SeeMore.setOnClickListener(new View.OnClickListener() {
									   @Override
									   public void onClick(View v) {
										   // 사용 안함
										   Intent intent = new Intent(this, SeeMore.class);
										   startActivity(intent);
										   finish();
									   }
								   });

	//	SeeMore.setId();
		//SeeMore.setGravity(80);

		RelativeLayout.LayoutParams bottomButtonParams1 = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.WRAP_CONTENT,
						200);
		bottomButtonParams1.addRule(RelativeLayout.BELOW,2);
		bottomButtonParams1.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,1);

		//ViewGroup.LayoutParams.WRAP_CONTENT);
		Button food = new Button(con);
		food.setLayoutParams(bottomButtonParams1);
		food.setText("밥주기");
*/
		ImageView iv = new ImageView(con);
		//Bitmap bm = new GetImage.ImageRoader().getBitmapImg("2.jpg");

		iv.setImageBitmap(bm);

		rl.addView(iv);
	//	rl.addView(SeeMore);
	//	rl.addView(food);
		rl.addView(tx_age);
		rl.addView(tx_nickname);
/*
			//LinearLayout ll = new LinearLayout(con);
		ScrollView sv = new ScrollView(con);
		RelativeLayout rl = new RelativeLayout(con);


		TextView tx = new TextView(con);
		tx.setText("블랙팬서");
		tx.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);

		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
		rl.setLayoutParams(params);

		RelativeLayout.LayoutParams textparam = new RelativeLayout.LayoutParams
				(ViewGroup.LayoutParams.MATCH_PARENT,
						ViewGroup.LayoutParams.MATCH_PARENT);
		tx.setLayoutParams(textparam);

	//	ll.setOrientation(LinearLayout.VERTICAL);
		ImageView iv = new ImageView(con);
		Bitmap bm = new GetImage.ImageRoader().getBitmapImg("1.jpg");
		iv.setImageBitmap(bm);


		ll.addView(iv);
		ll.addView(tx);

		sv.addView(ll);
*/
		return rl;
	}
	private static TextView getTextView(Context con) {
		int color = (int)(Math.random()*256);
		TextView tv = new TextView(con);
		tv.setBackgroundColor(Color.rgb(color, color, color));
		tv.setText("TextView Item");
		tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 24);
		
		color = 255 - color;
		tv.setTextColor(Color.rgb(color, color, color));
		tv.setGravity(Gravity.CENTER);
		return tv;
	}

	private static ScrollView getScrollView(Context con) {
		ScrollView sv = new ScrollView(con);
		
		LinearLayout ll = new LinearLayout(con);
		ll.setOrientation(LinearLayout.VERTICAL);
		
		ll.addView(getTextView(con));
		ll.addView(getAnalogClock(con));
		ll.addView(getDigitalClock(con));
		ll.addView(getImageView(con));
		
		sv.addView(ll);
		return sv;
	}
	
	/**
	 * ����Ʈ �並 �����Ͽ� ��ȯ�Ѵ�
	 * @param con - �� ������ ����� Context
	 * @return ����Ʈ ��
	 */
	private static ListView getListView(Context con) {
		ListView lv = new ListView(con);
		lv.setScrollingCacheEnabled(false);
		lv.setAnimationCacheEnabled(false);
		
		//lv.setAdapter(new BkListViewAdapter(con));
		/*
		public BkListViewAdapter(Context con) {
		mContext = con;
		mInflater = LayoutInflater.from(mContext);
		mItems = new String[2][];
		mItems[0] = mContext.getResources().getStringArray(R.array.avengers);
		mItems[1] = mContext.getResources().getStringArray(R.array.actor);
		}
		*/
		return  lv;
	}
	
	private static ImageView getImageView(Context con) {
		ImageView iv = new ImageView(con);
		//iv.setImageResource(R.drawable.bkstock);
		return iv;
	}
	
	private static AnalogClock getAnalogClock(Context con) {
		AnalogClock clock = new AnalogClock(con);
		return clock;
	}
	
	private static DigitalClock getDigitalClock(Context con){
		return new DigitalClock(con);
	}

}
