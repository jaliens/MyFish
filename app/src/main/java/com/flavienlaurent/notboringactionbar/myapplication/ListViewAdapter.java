package com.flavienlaurent.notboringactionbar.myapplication;

/**
 * Created by User on 2016-08-11.
 */
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;

import java.util.ArrayList;

public class ListViewAdapter extends BaseAdapter {
    private static final int ITEM_VIEW_TYPE_STRS = 0 ;
    private static final int ITEM_VIEW_TYPE_IMGS = 1 ;
    private static final int ITEM_VIEW_TYPE_MAX = 2 ;
    // Adapter에 추가된 데이터를 저장하기 위한 ArrayList
    private ArrayList<ListItem> listViewItemList = new ArrayList<ListItem>() ;

    // ListViewAdapter의 생성자
    public ListViewAdapter() {

    }


    /////////////////////////////////////////////////
    @Override
    public int getViewTypeCount() {
        return ITEM_VIEW_TYPE_MAX ;
    }

    // position 위치의 아이템 타입 리턴.
    @Override
    public int getItemViewType(int position) {
        return listViewItemList.get(position).getType() ;
    }

    //////////////////////////////////////////////////////////////////////////////

    // Adapter에 사용되는 데이터의 개수를 리턴. : 필수 구현
    @Override
    public int getCount() {
        return listViewItemList.size() ;
    }

    // position에 위치한 데이터를 화면에 출력하는데 사용될 View를 리턴. : 필수 구현
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
     //   final int pos = position;
        final Context context = parent.getContext();
        int viewType = getItemViewType(position);

        // "listview_item" Layout을 inflate하여 convertView 참조 획득.
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //   convertView = inflater.inflate(R.layout.listview_item, parent, false);
            ListItem listViewItem = listViewItemList.get(position);


            switch (viewType) {
                case ITEM_VIEW_TYPE_STRS:
                    convertView = inflater.inflate(R.layout.list_item, parent, false);

                    // 화면에 표시될 View(Layout이 inflate된)으로부터 위젯에 대한 참조 획득

                    ImageView iconImageView = (ImageView) convertView.findViewById(R.id.imageView1);
                    TextView titleTextView = (TextView) convertView.findViewById(R.id.textView1);
                    TextView descTextView = (TextView) convertView.findViewById(R.id.textView2);

                 //페이져 추가
                 //   LinearLayout linearLayout = (LinearLayout) convertView.findViewById(R.id.page_mark);
                 //   ViewPager viewPager = (ViewPager) convertView.findViewById(R.id.pager);

                    // Data Set(listViewItemList)에서 position에 위치한 데이터 참조 획득
                   // ListItem listViewItem = listViewItemList.get(position);

                    // 아이템 내 각 위젯에 데이터 반영

                    iconImageView.setImageBitmap(listViewItem.getIcon());
                    titleTextView.setText(listViewItem.getTitle());
                    descTextView.setText(listViewItem.getDesc());

                    break;
                case ITEM_VIEW_TYPE_IMGS:
                    convertView = inflater.inflate(R.layout.list_item2, parent, false);

                    ImageView fishIconView = (ImageView) convertView.findViewById(R.id.imagefish);

                    fishIconView.setImageDrawable(listViewItem.getIconD());


                    break;

            }
        }

        return convertView;
    }

    // 지정한 위치(position)에 있는 데이터와 관계된 아이템(row)의 ID를 리턴. : 필수 구현
    @Override
    public long getItemId(int position) {
        return position ;
    }

    // 지정한 위치(position)에 있는 데이터 리턴 : 필수 구현
    @Override
    public Object getItem(int position) {
        return listViewItemList.get(position) ;
    }

    // 아이템 데이터 추가를 위한 함수. 개발자가 원하는대로 작성 가능.
    public void addItem(Bitmap icon, String title, String desc) {
        ListItem item = new ListItem();

        item.setType(ITEM_VIEW_TYPE_STRS);
        item.setIcon(icon);
        item.setTitle(title);
        item.setDesc(desc);

        listViewItemList.add(item);
    }
    public void addItem(Drawable icon) {
        ListItem item = new ListItem();

        item.setType(ITEM_VIEW_TYPE_IMGS);
        item.setIconDrawable(icon);

        listViewItemList.add(item);
    }
}
