package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;

import java.util.ArrayList;

/**
 * Created by jjy on 2016-11-27.
 */

public class ChatRoomAdapter extends BaseAdapter {
//    private class ViewHolder {
////        public TextView chatRoomEachItem_txtView_targetId;
////        public TextView chatRoomEachItem_txtView_dialog;
//        public TextView dialog;
//    }

    private Context mContext = null;
    public static ArrayList<ChatRoomItemModel> mListItems = new ArrayList<ChatRoomItemModel>();

    public ChatRoomAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return mListItems.size();
    }

    @Override
    public Object getItem(int position) {
        return mListItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    //position: getView는 리스트 항목 수 만큼 호출되며 각 항목 마다 position 값이 있다.
    //convertView에 리스트 항목으로 표시될 뷰를 객체화 시켜서 넣어야 함.
    //convertView의 setTag(holder) 를 호출하여 holder를 통해서 리스트의 각 항목 아이템들에 접근할 수 있게 된다.
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        ChatRoomItemModel mListItem = mListItems.get(position);
//        ViewHolder holder;
        TextView txtV;
//        if (convertView == null) {
//        holder = new ViewHolder();
        if (mListItem.isMine) {
            convertView = inflater.inflate(R.layout.chat_right, null);
            txtV = (TextView) convertView.findViewById(R.id.chat_right_txtbx);
        }else{
            convertView = inflater.inflate(R.layout.chat_left, null);
            txtV = (TextView) convertView.findViewById(R.id.chat_left_txtbx);
        }

//        convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }

        txtV.setText(mListItem.dialog);

        return convertView;
    }

    // codes needed for list view item management

    public void addItem(Boolean isMine, String targetId, String dialog) {
        ChatRoomItemModel addingItem = null;
        addingItem = new ChatRoomItemModel();
        addingItem.isMine = isMine;
        addingItem.targetId = targetId;
        addingItem.dialog = dialog;

        mListItems.add(addingItem);
    }

    public void remove(int position) {
        mListItems.remove(position);
    }

}
