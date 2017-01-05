package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;

import java.util.ArrayList;

/**
 * Created by jjy on 2016-11-25.
 */

public class ChatRoomListAdapter extends BaseAdapter {
    private class ViewHolder {
        public ImageView chatRoomListEachItem_imgView_targetPicture;
        public TextView chatRoomListEachItem_txtView_targetId;
        public TextView chatRoomListEachItem_txtView_dialog;

    }

    private Context mContext = null;
    public static ArrayList<ChatRoomListItemModel> mListItems = new ArrayList<ChatRoomListItemModel>();

    public ChatRoomListAdapter(Context mContext) {
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
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.chat_room_list_each_item, null);

            holder.chatRoomListEachItem_imgView_targetPicture = (ImageView) convertView.findViewById(R.id.chatRoomListEachItem_imgview_targetpicture);
            holder.chatRoomListEachItem_txtView_targetId = (TextView) convertView.findViewById(R.id.chatRoomListEachItem_txtview_targetid);
            holder.chatRoomListEachItem_txtView_dialog = (TextView) convertView.findViewById(R.id.chatRoomListEachItem_txtview_dialog);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        ChatRoomListItemModel mListItem = mListItems.get(position);

        if (mListItem.targetPicture != null) {
            holder.chatRoomListEachItem_imgView_targetPicture.setVisibility(View.VISIBLE);
            holder.chatRoomListEachItem_imgView_targetPicture.setImageDrawable(mListItem.targetPicture);

        } else {
            holder.chatRoomListEachItem_imgView_targetPicture.setVisibility(View.GONE);
        }

        holder.chatRoomListEachItem_txtView_targetId.setText(mListItem.targetId);
        holder.chatRoomListEachItem_txtView_dialog.setText(mListItem.dialog);

        // convertView.setMinimumHeight(holder.photoBckgnd.getHeight()+holder.period.getHeight());

        return convertView;
    }

    // codes needed for list view item management

    public static void addItem(Drawable targetPicture, String targetId, String dialog) {
        ChatRoomListItemModel addingItem = null;
        addingItem = new ChatRoomListItemModel();
        addingItem.targetPicture = targetPicture;
        addingItem.targetId = targetId;
        addingItem.dialog = dialog;

        mListItems.add(addingItem);
    }

    public void remove(int position) {
        mListItems.remove(position);
    }

}
