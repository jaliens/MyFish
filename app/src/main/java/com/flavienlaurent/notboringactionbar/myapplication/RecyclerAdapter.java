package com.flavienlaurent.notboringactionbar.myapplication;

/**
 * Created by 도훈 on 2016-11-16.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.flavienlaurent.notboringactionbar.R;

import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    Context context;
    final int maxFisherySize = 4;
    List<Item> items;
    int item_layout;
    DBHelper myFisheryDB;
    long now = System.currentTimeMillis();
    Date date = new Date(now);

    public void callNotify(){
        notifyDataSetChanged();
    }
    public RecyclerAdapter(Context context, List<Item> items, int item_layout) {
        this.context = context;
        this.items = items;
        this.item_layout = item_layout;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View card = new MyCardView(parent.getContext());
        return new ViewHolder(card);

    }

        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            final Item item = items.get(position);
            MyCardView cardView = holder.cardView;
            cardView.setData(item);
            myFisheryDB = new DBHelper(context, "MyFishery.db", null, 1);
            myFisheryDB.ini();

        cardView.setUserActionListener(new MyCardView.UserActionListener() {

            public void onSelectClicked(){

                    AlertDialog.Builder alert_confirm = new AlertDialog.Builder(context);
                    alert_confirm.setMessage("정말 담으시겠습니까??").setCancelable(false).setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final String[] items = new String[maxFisherySize];

                                    if(myFisheryDB.getCount() == maxFisherySize){
                                        //Toast.makeText(getActivity(),"어장이 꽉 찼습니다! 어장에서 한 명을 빼세요.", Toast.LENGTH_LONG);
                                        new AlertDialog.Builder(context)
                                                .setTitle("어장이 꽉 찼습니다!")
                                                .setMessage("어장으로 가셔서 한 명을 빼주세요.")
                                                .setIcon(R.drawable.cancel)
                                                .show();
                                    } else {

                                        myFisheryDB.insert(date.toString(), item.getKey() );  //확인버튼 누르면 해당 매칭된 사람을 내부 SQLite에 넣음.
                                        TabWidget.dirty = true;

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
//
//            @Override
//            public void onTextClicked() {
//            //    Toast.makeText(context, item.getTitle(), Toast.LENGTH_SHORT).show();
//            }
        });
    }


    @Override
    public int getItemCount() {
        return this.items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MyCardView cardView;

        public ViewHolder(View card) {
            super(card);
            cardView = (MyCardView)card;
        }


    }
}