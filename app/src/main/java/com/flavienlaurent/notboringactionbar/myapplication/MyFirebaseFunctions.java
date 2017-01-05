package com.flavienlaurent.notboringactionbar.myapplication;

import com.google.firebase.iid.FirebaseInstanceId;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.net.URI;
import java.util.ArrayList;

/**
 * Created by jjy on 2016-11-23.
 */

public class MyFirebaseFunctions {
    static String getUserId(){
        //임시로 사용
        //return GlobalInfosForChat.userId;
        return "사람1";
    }

    //서버에 id와 fcm토큰 등록
    static void registerToken2Server(){
        try {
            new Thread() {
                public void run() {
                    try {
                        URI url = new URI("http://164.125.154.54/registerfcm.php");
                        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

                        nameValuePairs.add(new BasicNameValuePair("id", MyFirebaseFunctions.getUserId()));
                        nameValuePairs.add(new BasicNameValuePair("fcmToken", refreshedToken));

                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                        HttpResponse response = httpclient.execute(httpPost);

//                        EntityUtils.toString(response.getEntity());

//                        HttpEntity resEntity = response.getEntity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
        }
    }

    //상대방에게 메세지(대화) 전송
    //메시지 형식 : 보낸사람아이디길이 보낸사람아이디 메시지내용 이 셋을 붙여서 보낸다.
    static void sendMessage(final String targetId, final String message){
        try {
            new Thread() {
                public void run() {
                    try {
                        URI url = new URI("http://164.125.154.54/push_message.php");
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

//                        MainActivity.myDBmgr.registerDialogTargetById(targetId);//상대방을 sqlite chatrooms에 등록
                        SelectActivity.myDBmgr.insertMessages("1",targetId,message);//메시지를 sqlite dialogs에 등록

                        String sender = MyFirebaseFunctions.getUserId();

                        nameValuePairs.add(new BasicNameValuePair("targetId", targetId));
                        nameValuePairs.add(new BasicNameValuePair("message", String.valueOf(sender.length())+sender+message));

                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                        HttpResponse response = httpclient.execute(httpPost);

//                        ChatRoomActivity.sex = EntityUtils.toString(response.getEntity());
//                        HttpEntity resEntity = response.getEntity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
        }
    }

    //상대방에게 채팅 신청 보내기
    static void sendChatReq(final String targetId){
        try {
            new Thread() {
                public void run() {
                    try {
                        URI url = new URI("http://164.125.154.54/push_message.php");
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httpPost = new HttpPost(url);
                        ArrayList<BasicNameValuePair> nameValuePairs = new ArrayList<BasicNameValuePair>();

//                        MainActivity.myDBmgr.registerDialogTargetById(targetId);//상대방을 sqlite chatrooms에 등록
                        String sender = MyFirebaseFunctions.getUserId();

                        nameValuePairs.add(new BasicNameValuePair("targetId", targetId));
                        nameValuePairs.add(new BasicNameValuePair("message", "0"+sender));

                        httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs, HTTP.UTF_8));
                        HttpResponse response = httpclient.execute(httpPost);

//                        ChatRoomActivity.sex = EntityUtils.toString(response.getEntity());
//                        HttpEntity resEntity = response.getEntity();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }.start();
        } catch (Exception e) {
        }
    }



//    if(myDBmgr.checkChatRoomStatus(sender).equals("0")){
//        myDBmgr.activateDialogTargetById(sender);
//    }
}
