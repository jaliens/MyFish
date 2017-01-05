package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by user on 2016-08-16.
 */

public class DBHelper extends SQLiteOpenHelper {
    // DBHelper 생성자로 관리할 DB 이름과 버전 정보를 받음
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    // DB를 새로 생성할 때 호출되는 함수
    @Override
    public void onCreate(SQLiteDatabase db) {
        // 새로운 테이블 생성
        /* 이름은 PEOPLE 이고, 자동으로 값이 증가하는 _id 정수형 기본키 컬럼과
        nickname 문자열 컬럼, create_at 문자열 컬럼으로 구성된 테이블을 생성. */
        db.execSQL("CREATE TABLE PEOPLE (_id INTEGER PRIMARY KEY AUTOINCREMENT, key TEXT, create_at TEXT);");
    }

    // DB 업그레이드를 위해 버전이 변경될 때 호출되는 함수
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String create_at, String key) {
        // 읽고 쓰기가 가능하게 DB 열기
        SQLiteDatabase db = getWritableDatabase();
        // DB에 입력한 값으로 행 추가
        db.execSQL("INSERT INTO PEOPLE VALUES(null, '" + key + "', '" + create_at + "');");
        db.close();
    }

    /*
    public void update(String nickname) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 항목과 일치하는 행의 닉네임 수정
        db.execSQL("UPDATE PEOPLE SET price=" + " WHERE nickname='" + nickname + "';");
        db.close();
    }
*/
    public void delete(String key) {
        SQLiteDatabase db = getWritableDatabase();
        // 입력한 닉네임과 일치하는 행 삭제
        db.execSQL("DELETE FROM PEOPLE WHERE key='" + key + "';");
        db.close();
    }

    public int getCount() {
        SQLiteDatabase db = getWritableDatabase();
        // 테이블의 row 수를 카운트

        Cursor cursor = db.rawQuery("SELECT count(_id) FROM PEOPLE;", null);
        cursor.moveToFirst();

        return cursor.getInt(0);
    }

    public String getResult() {
        // 읽기가 가능하게 DB 열기
        SQLiteDatabase db = getReadableDatabase();
        String result = "";

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT * FROM PEOPLE", null);
        while (cursor.moveToNext()) {
            result += cursor.getString(0)
                    + " : "
                    + cursor.getString(1)
                    + " | "
                    + cursor.getString(2)
                    + "\n";
        }
        db.close();
        return result;
    }

    public ArrayList<String> getKeys(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<String> result = new ArrayList<String>();

        int i=0;

        // DB에 있는 데이터를 쉽게 처리하기 위해 Cursor를 사용하여 테이블에 있는 모든 데이터 출력
        Cursor cursor = db.rawQuery("SELECT key FROM PEOPLE", null);
        while (cursor.moveToNext()) {
            result.add(cursor.getString(0));
            Log.d("db" + i, cursor.getString(0));
        }
        db.close();
        return result;
    }

    //중복검사
    public boolean check(String key){
        SQLiteDatabase db = getReadableDatabase();
        boolean flag;

        Cursor cursor = db.rawQuery("SELECT * FROM PEOPLE WHERE key='" + key + "';", null);
    if(cursor.getCount() == 0) { //DB에 없을때
        flag = true;
    }
    else { //DB에 있을때
        flag = false;
    }
    return flag;
}

    //DB 초기화
    public void ini(){
        SQLiteDatabase db = getReadableDatabase();

        db.execSQL("DELETE FROM PEOPLE;");
        db.execSQL("delete from sqlite_sequence where name='PEOPLE';");
    }
/*
    public String getFinalID(){
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = null;
        cursor = db.rawQuery("SELECT * FROM PEOPLE ORDER BY _id DESC limit 1;", null);
        return cursor.getString(0);
    }
*/
}