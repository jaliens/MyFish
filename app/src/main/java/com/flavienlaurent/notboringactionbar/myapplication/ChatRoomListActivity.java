package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.flavienlaurent.notboringactionbar.R;

import static com.flavienlaurent.notboringactionbar.myapplication.SelectActivity.myDBmgr;

public class ChatRoomListActivity extends Activity {
    private ListView mListView = null;
    public static ChatRoomListAdapter mAdapter = null;

    public static boolean isActivated = false;//엑티비티가 현재 활성화 상태인지
    BroadcastReceiver newChatTargetReceiver = null;//MyFireBaseMessagingService에서 보내는 메시지를 캐치하는 리시버

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room_list);
        //GUI 설정
        initGUIs();
        isActivated = true;
        setBroadcastReceiver();

        //기존 채팅목록 가져오기
        mAdapter.mListItems.clear();
        myDBmgr.displayEveryRoomsOnChatRoomListActivity();
    }

    @Override
    public void onResume() {
        super.onResume();
        isActivated = true;
    }

    @Override
    protected void onStop() {
        super.onStop();
        isActivated = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivated = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isActivated = false;
    }

    public static void addListItemAndNotify(String targetId, String dialog){
        mAdapter.addItem(null,targetId,dialog);
        mAdapter.notifyDataSetChanged();
    }

    void initGUIs(){
        mListView = (ListView) findViewById(R.id.chat_room_list);
        mAdapter = new ChatRoomListAdapter(this);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //클릭한 채팅방이 활성화 되어 이따면 채팅방으로 이동
                final String targetId = mAdapter.mListItems.get(position).targetId;
                String chatRoomStatus = myDBmgr.checkChatRoomStatus(targetId);
                Bundle extras = new Bundle();
                extras.putString("targetId", targetId);

                // 인텐트를 생성한다.
                final Intent intent = new Intent(ChatRoomListActivity.this, ChatRoomActivity.class);

                // 위에서 만든 Bundle을 인텐트에 넣는다.
                intent.putExtras(extras);

                if(chatRoomStatus.equals("2")) {
                    startActivity(intent);
                }else if(chatRoomStatus.equals("1")){
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomListActivity.this);
                    // 제목셋팅
                    alertDialogBuilder.setTitle(targetId+"님이 대화를 신청하셨어요");
                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("수락하실래요?")
                            .setCancelable(false)
                            .setPositiveButton("수락",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //상대가 먼저 나에게 채팅 요청한 경우 채팅방 활성화
                                            if(myDBmgr.checkChatRoomStatus(targetId).equals("1")){
                                                myDBmgr.activateDialogTargetById(targetId);
                                                MyFirebaseFunctions.sendChatReq(targetId);
                                            }
                                            startActivity(intent);
                                        }
                                    })
                            .setNegativeButton("거절",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            //미구현
                                            dialog.cancel();
                                        }
                                    }
                            );
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();

                }else{
                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ChatRoomListActivity.this);
                    // 제목셋팅
                    alertDialogBuilder.setTitle("상대방이 아직 수락하지 않았어요");
                    // AlertDialog 셋팅
                    alertDialogBuilder
                            .setMessage("더 기다려주세요")
                            .setCancelable(false)
                            .setPositiveButton("확인",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });
                    AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                }
            }
        });
    }

    public void setBroadcastReceiver (){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("jjy.myfishchatting.Broadcasting.action.NEW_CHAT_TARGET");
        newChatTargetReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String targetId = intent.getStringExtra("targetid");
                addListItemAndNotify(targetId,"");
            }
        };
        registerReceiver(newChatTargetReceiver,intentFilter);
    }
}
