package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.flavienlaurent.notboringactionbar.R;

import static com.flavienlaurent.notboringactionbar.myapplication.SelectActivity.myDBmgr;

public class ChatRoomActivity extends AppCompatActivity {
    private ListView mListView = null;
    public static ChatRoomAdapter mAdapter = null;
    EditText editTxt_outgoingMsg;
    Button btn_sendMsg;

    public static String targetId = null;//상대방 아이디
    public static boolean isActivated = false;//엑티비티가 현재 활성화 상태인지

    BroadcastReceiver messageReceiver = null;//MyFireBaseMessagingService에서 보내는 메시지를 캐치하는 리시버

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        initGUIs();
        isActivated = true;
        String targetId = getIntent().getStringExtra("targetId");
        this.targetId = targetId;
        setBroadCastReceiver();
//        Toast.makeText(ChatRoomActivity.this, targetId, Toast.LENGTH_LONG).show();

        //기존 대화내역 가져오기
        mAdapter.mListItems.clear();
        myDBmgr.displayEveryMessagesOnChatRoomActivity(targetId);
        mListView.setSelection(mAdapter.getCount() - 1);

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
    public void onPause() {
        super.onPause();
        isActivated = false;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        isActivated = false;
    }

    void initGUIs(){
        mListView = (ListView) findViewById(R.id.chat_room);
        mAdapter = new ChatRoomAdapter(this);
        mListView.setAdapter(mAdapter);

        //보낼 메세지 입력 창
        editTxt_outgoingMsg=(EditText) findViewById(R.id.edittext_outgoingmsg);

        //보내기 버튼
        btn_sendMsg = (Button)findViewById(R.id.btn_sendmsg);
        btn_sendMsg.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                Toast.makeText(ChatRoomActivity.this, sex, Toast.LENGTH_LONG).show();
//                Toast.makeText(ChatRoomActivity.this, "전송버튼", Toast.LENGTH_LONG).show();
                String msg = editTxt_outgoingMsg.getText().toString();
                //채팅방에 보낸 메시지 띄움
                addListItemAndNotify(true,targetId,msg);
                mListView.setSelection(mAdapter.getCount() - 1);

                //서버로 메시지 전송
                MyFirebaseFunctions.sendMessage(targetId,msg);
                editTxt_outgoingMsg.setText("");

            }
        });
    }
    public static void addListItemAndNotify(Boolean isMine, String targetId, String dialog){
        mAdapter.addItem(isMine,targetId,dialog);
        mAdapter.notifyDataSetChanged();
    }

    public static void addListItem(Boolean isMine, String targetId, String dialog){
        mAdapter.addItem(isMine,targetId,dialog);
    }

    public static void notifyItemsAdded(){
        mAdapter.notifyDataSetChanged();
    }


    private void setBroadCastReceiver(){
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("jjy.myfishchatting.Broadcasting.action.MSG_CAME");
        messageReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String targetId = intent.getStringExtra("targetid");
                String msg = intent.getStringExtra("message");
                addListItemAndNotify(false,targetId,msg);
                mListView.setSelection(mAdapter.getCount() - 1);
            }
        };
        registerReceiver(messageReceiver,intentFilter);
    }
}
