package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.flavienlaurent.notboringactionbar.R;

import java.util.Date;

/**
 * Created by 도훈 on 2016-11-16.
 */
public class MyCardView extends CardView {
    CardView cardView;
    LinearLayout linearLayout;
    ImageView imageView;
    TextView nickname;
    TextView age;
    TextView area;
    RelativeLayout select;
    final static Integer maxFisherySize = 3;
    TextView result1;
    TextView result2;
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    //SQLite 생성
    DBHelper dbHelper;

    DBHelper myFisheryDB;


    private UserActionListener mUserActionListener;

    public interface UserActionListener {
//        public void onImageClicked();
        public void onSelectClicked();
//        public void onTextClicked();
    }

    public MyCardView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public MyCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public MyCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    public void setUserActionListener(UserActionListener l) {
        this.mUserActionListener = l;
    }

    public void setData(Item item) {
        Glide.with(getContext()).load(item.getImage()).into(imageView);
//Log.d("ff : ",""+item.getImage());
        nickname.setText(item.getNick());
    //    age.setText(item.getAge());
        area.setText(item.getarea());
    }

    public void init(Context context, AttributeSet attrs, int defStyleAttr) {
        myFisheryDB = new DBHelper(getContext(), "MyFishery.db", null, 1);
        //  dbHelper.ini();
//        myFisheryDB.ini();
        // Initialization
        String infService = Context.LAYOUT_INFLATER_SERVICE;
        LayoutInflater li = (LayoutInflater) getContext().getSystemService(infService);
        View v = li.inflate(R.layout.item_cardview, this, true);
        View vv = LayoutInflater.from(context).inflate(R.layout.item_cardview, this, false);
        cardView = (CardView) v.findViewById(R.id.cardview);
        imageView = (ImageView) findViewById(R.id.imageView);
        nickname = (TextView) findViewById(R.id.tv_content);
        area = (TextView) findViewById(R.id.tv_content3);
      //  age = (TextView) findViewById(R.id.tv_content2);
        select = (RelativeLayout) findViewById(R.id.select_btn);


        select.setOnClickListener(mOnclickListener);
    }
    View.OnClickListener mOnclickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if( view == select){
                 mUserActionListener.onSelectClicked();
            }
        }
    };

}