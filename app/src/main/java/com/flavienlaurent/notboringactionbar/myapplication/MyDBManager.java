package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by jjy on 2016-11-24.
 */

public class MyDBManager {
    static final String DB_MYDB = "MyDB.db"; //데이터베이스 명
    Context mContext = null;
    private static MyDBManager mDbManager = null;
    private SQLiteDatabase mDatabase = null;

    public static MyDBManager getInstance(Context context){
        if(mDbManager == null){
            mDbManager = new MyDBManager(context);
        }
        return mDbManager;
    }

    private MyDBManager(Context context){
        mContext = context;
        mDatabase = context.openOrCreateDatabase( DB_MYDB , Context.MODE_PRIVATE, null);
    }

    //테이블 생성(없다면)
    public void createChatTables(){
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS dialogs (ismine INTEGER, targetid TEXT, dialog TEXT);");
        mDatabase.execSQL("CREATE TABLE IF NOT EXISTS chatrooms (targetid TEXT UNIQUE, status TEXT);");//ㅍㅍ
        //status는 0,1,2 중 하나로 각각의 의미는 내가 채팅 요청한 상태, 상대가 요청한 상태, 채팅방 활성화 된 상태를 나타냄
        //2 일 때만 채팅방에 입장 가능하도록 구현하여야 함
    }

    //메세지 넣기
    public void insertMessages(String isMine, String targetId , String dialog){
        mDatabase.execSQL("INSERT INTO dialogs (ismine, targetid, dialog) " +
                "VALUES("
                + "'" + isMine + "'"
                + ",'" + targetId + "'"
                + ",'" + dialog + "'"
                + ");"
        );
    }

    //특정인과의 메세지 싹 지우기
    public void deleteMessagesById(String targetId){
        mDatabase.execSQL("DELETE FROM dialogs WHERE targetid = '" +targetId +"';");
    }

    //특정인과의 기존 메세지 싹 채팅방에 표시 (dialogs 테이블)
    public void displayEveryMessagesOnChatRoomActivity(String targetId){
        Cursor qResult = mDatabase.rawQuery("SELECT ismine, targetid, dialog FROM dialogs WHERE targetid = '" +targetId +"';",null);

        int cnt = qResult.getCount();
        qResult.moveToFirst();
        for(int i =0;i<cnt;i++){
            Boolean isMine = false;
            if(qResult.getInt(0) == 1) {
                isMine = true;
            }
            ChatRoomActivity.addListItem(isMine,qResult.getString(1),qResult.getString(2));
            if(i < cnt-1)
                qResult.moveToNext();
        }
        ChatRoomActivity.notifyItemsAdded();
    }

    //대화상대가 이미 db에 등록되었는지 체크(chatrooms 테이블)
    public int checkIfIdRegistered(String id){
        Cursor qResult = mDatabase.rawQuery("SELECT targetid FROM chatrooms WHERE targetid = '"+id+"';",null);
        return qResult.getCount();
    }

    //대화상대 등록 (chatrooms 테이블) status는 0 , 1, 2 //ㅍㅍ
    public void registerDialogTargetById(String targetId, String status){
        mDatabase.execSQL("INSERT OR IGNORE INTO chatrooms (targetid, status) " +
                "VALUES("
                + "'" + targetId + "'"
                + ",'" + status + "'"
                + ");"
        );
    }

    //채팅방 상태 체크
    public String checkChatRoomStatus(String id){
        Cursor qResult = mDatabase.rawQuery("SELECT status FROM chatrooms WHERE targetid = '"+id+"';",null);
        int cnt = qResult.getCount();
        if(cnt > 0){
            qResult.moveToFirst();
            return qResult.getString(0);
        }
        return "";
    }

    //채팅방 활성화 //ㅍㅍ
    public void activateDialogTargetById(String targetId){
        mDatabase.execSQL("update chatrooms set status = '2' where targetid = '"+ targetId +"';");
    }

    //대화상대목록 채팅방 목록에 싹 표시
    public void displayEveryRoomsOnChatRoomListActivity(){
        Cursor qResult = mDatabase.rawQuery("SELECT targetid FROM chatrooms;",null);
        int cnt = qResult.getCount();
        qResult.moveToFirst();
        for(int i =0;i<cnt;i++){
            ChatRoomListActivity.addListItemAndNotify(qResult.getString(0),"");
            if(i < cnt-1)
                qResult.moveToNext();
        }
    }

    //대화상대 삭제 (chatrooms 테이블)
    public void deleteDialogTargetById(String targetId){
        mDatabase.execSQL("DELETE FROM chatrooms WHERE targetid = '" +targetId +"';");
    }

}
