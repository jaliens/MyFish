/**
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.flavienlaurent.notboringactionbar.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import static com.flavienlaurent.notboringactionbar.myapplication.SelectActivity.myDBmgr;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
  //  public static MyDBManager myDBmgr;
    private static final String TAG = "MyFirebaseMsgService";

    /**
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging.
     */
    // [START receive_message]
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // [START_EXCLUDE]
        // There are two types of messages data messages and notification messages. Data messages are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data messages are the type
        // traditionally used with GCM. Notification messages are only received here in onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages containing both notification
        // and data payloads are treated as notification messages. The Firebase console always sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options
        // [END_EXCLUDE]

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d(TAG, "From: " + remoteMessage.getFrom());
        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }

        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.

        myDBmgr = MyDBManager.getInstance(this);

        //받은 메시지 처리
        String receiveString = remoteMessage.getData().get("message");//fcm메시지 내용 (ex: 2종엽messege) 2:아이디 길이, 종엽:아이디, message:메시지 내용
        int idLength = Integer.parseInt(receiveString.substring(0,1));//id의 길이 알아내기
        String sender = "";//보낸사람 아이디
        String msg ="";//순수 대화 메시지

        if(idLength != 0){//메시지가 들어온 경우
            sender = receiveString.substring(1,idLength+1);//보낸사람 아이디
            msg = receiveString.substring(idLength+1);//순수 대화 메시지
        }else{//대화 신청이 들어온 경우 //ㅍㅍ
            sender = receiveString.substring(1);
        }

        //내가 채팅요청한 상대에게서 채팅요청이 왔을 경우 채팅방 활성화
        if(myDBmgr.checkChatRoomStatus(sender).equals("0")){
            myDBmgr.activateDialogTargetById(sender);
        }

        //새로운 채팅상대일 경우 채팅방 목록에 추가를 위해 채팅방 목록 화면에 정보 전달
        if(myDBmgr.checkIfIdRegistered(sender) == 0) {
            if(ChatRoomListActivity.isActivated){
                Intent intent = new Intent();
                intent.setAction("jjy.myfishchatting.Broadcasting.action.NEW_CHAT_TARGET");
                intent.putExtra("targetid",sender);
                sendBroadcast(intent);
            }
        }

        if(idLength == 0) {
            SelectActivity.myDBmgr.registerDialogTargetById(sender, "1");//상대방을 sqlite chatrooms에 등록 //ㅍㅍ
        }
        if(idLength != 0) {
            //채팅방으로 받은 메시지 전송
            if (sender.equals(ChatRoomActivity.targetId) && ChatRoomActivity.isActivated) {
                Intent intent = new Intent();
                intent.setAction("jjy.myfishchatting.Broadcasting.action.MSG_CAME");
                intent.putExtra("targetid", sender);
                intent.putExtra("message", msg);
                sendBroadcast(intent);
            }
            sendNotification(msg, sender);//폰에 메시지 알림 보냄
            SelectActivity.myDBmgr.insertMessages("0", sender, msg);//받은 메시지를 sqlite에 저장

        }else{
            //채팅 신청이 왔음을 알리기
            //구현
            sendChatReqNotification(sender);
        }
    }
    // [END receive_message]

    /**
     * Create and show a simple notification containing the received FCM message.
     *
     * @param messageBody FCM message body received.
     */
    private void sendNotification(String messageBody, String sender) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("targetId",sender);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, sender.hashCode() /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(sender)
                .setContentText(messageBody)
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);


        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }

    private void sendChatReqNotification(String sender) {
        Intent intent = new Intent(this, ChatRoomListActivity.class);
        intent.putExtra("targetId",sender);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT
                | Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, sender.hashCode() /* Request code */, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.ic_stat_ic_notification)
                .setContentTitle(sender)
                .setContentText("채팅 신청이 왔어요")
                .setAutoCancel(false)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
