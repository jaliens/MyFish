package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;
import com.flavienlaurent.notboringactionbar.myapplication.receiver.AlarmBraodCastReciever;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
//import github.chenupt.springindicator.SpringIndicator;
//import github.chenupt.springindicator.viewpager.ScrollerViewPager;

public class SelectActivity extends Fragment{
    public static MyDBManager myDBmgr;//jjy
    final int ITEM_SIZE = 4;
    static DBHelper dbHelper;
    public String key1 = "0";
    public String[] key = {"0","0","0","0"};
    public String[] nick = {"0","0","0","0"};
    long now = System.currentTimeMillis();
    static ArrayList<String> resultStr = new ArrayList<>();
    Date date = new Date(now);
    List<Item> items = new ArrayList<>();
    int value = 120;
    Item[] item = new Item[ITEM_SIZE];
    RecyclerView recyclerView = null;
    String area = "default";
    String age= "default";
    RecyclerAdapter adapter = null;
    private static SelectActivity ins;
    static boolean onlyOnce = true;
    TextView select_timer;
    public Button chatting_btn;
    public Button Master_btn;
    public Button Event_btn;
    public static SelectActivity getInstace(){
        return ins;
    }

    int hour, minute, second;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //    setRetainInstance(true);

        //jjy 사용자 아이디 자동 생성
        if(GlobalInfosForChat.userId == "") {
            Random random = new Random();
            GlobalInfosForChat.userId = "사람1";
        }

        //jjy
        myDBmgr = MyDBManager.getInstance(getContext());
        myDBmgr.createChatTables();
        //서버에 내id와 fcm토큰 등록
        MyFirebaseFunctions.registerToken2Server();
        
        ins = this;
        adapter  = new RecyclerAdapter(getActivity(), items, R.layout.activity_matching);

    }

    private void TIMER(Context context) {
       /* new CountDownTimer(Long.MAX_VALUE, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                value--;
                if (value == 0) {
                    value = 15;
                }
            }

            @Override
            public void onFinish() {

            }
        }.start();
*/
        if (!AlarmBraodCastReciever.isLaunched) {
            AlarmUtils.getInstance().startOneMinuteAlram(context);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        FragmentActivity    faActivity  = (FragmentActivity)    super.getActivity();
        FrameLayout llLayout2    = (FrameLayout)    inflater.inflate(R.layout.activity_matching, container, false);

        Master_btn = (Button) llLayout2.findViewById(R.id.button1);
        Master_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), MasterActivity.class));
            }
        });

        chatting_btn = (Button)llLayout2.findViewById(R.id.button2);
        chatting_btn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(), ChatRoomListActivity.class));
            }
        });

        Event_btn = (Button) llLayout2.findViewById(R.id.button4);
        Event_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), FlipActivity.class));
            }
        });


        recyclerView = (RecyclerView) llLayout2.findViewById(R.id.recyclerview);
        new CountDownTimer(Long.MAX_VALUE,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                hour = value / 3600;
                minute = value % 3600 / 60;
                second = value % 60 % 60;

                value--;
                select_timer.setText("남은 시간 : " + hour + "시간" + minute + "분" + second + "초");
                if(value == 0){
                    value = 40;
                }
            }
            @Override
            public void onFinish(){

            }
        }.start();

        select_timer = (TextView) llLayout2.findViewById(R.id.select_timer);

//        if(onlyOnce) {
//            Log.d("first","first");
//            TIMER_INIT();
//            getPerson();
//
////            TIMER(getView().getContext());
//            onlyOnce = false;
//        }
//        TIMER_REMOVE();
//        HI();
        return llLayout2;

    }

    public void REMOVE() {
        for(int i = 0; i < 4; i ++){
            key[i] = "0";
            nick[i] = "0";
        }
        items.clear();
        TabWidget.dirty2 = true;
//        TabWidget.callNotify();
    }


    public void TIMER_INIT() {
        if(onlyOnce) {
            int spanCount = 2; // 2 columns
            int spacing = 70; //px
            boolean includeEdge = true;
            recyclerView.addItemDecoration(new GridSpacingItemDecoration(spanCount, spacing, includeEdge));
        }
        GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        dbHelper = new DBHelper(getView().getContext(), "People.db", null, 1);
        dbHelper.ini();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("data", Integer.parseInt(key1));
    }

    @Override
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        if(onlyOnce) {
            TIMER_INIT();
            getPerson();
            onlyOnce = false;
        }
        TIMER(getActivity());

    }

    public void getPerson() {
        for(int i=0; i<4; i++){
            Log.d("getperson","getperson");
            GetPhotoAndCheck1 getkey = new GetPhotoAndCheck1();
            try {
                getkey.execute().get();
            }catch (Exception e){

            }
        }
    }

    class GetPhotoAndCheck1 extends AsyncTask<Void, Void, String> {
    ProgressDialog loading;

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        loading.dismiss();
        key1 = s;
        if(key[0] == "0"){
            key[0] = key1;
            getKeyPhp1();

        } else if (key[1] == "0"){
            key[1] = key1;
            getKeyPhp1();

        } else if (key[2] == "0"){
            key[2] = key1;
            getKeyPhp1();

        } else if (key[3] == "0"){
            key[3] = key1;
            getKeyPhp1();

        }
//       recyclerView.setAdapter(adapter);

    }

    @Override
    protected String doInBackground(Void... params) {
        try {
            StringBuilder sb = null;
            while (true) {
                String link = "http://164.125.154.54/randomkey.php";

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                sb = new StringBuilder();
                String line = null;

                // Read Server Response(랜덤으로 뽑아온 key 값을 읽어옴)
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                Log.d("key313", String.valueOf(dbHelper.check(sb.toString())));

                Thread.sleep(100);

                if (dbHelper.check(sb.toString())) { //true: DB에 없을 때
                    Log.d("key13", sb.toString());

                    dbHelper.insert(date.toString(), sb.toString());
                    break;
                } else { // false: DB에 있을 때
                    Log.d("key2", sb.toString());
                    continue;
                }
            }
            return sb.toString();
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
    }
}






    private void getKeyPhp1() {
        class InsertData extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait", null, true, true);

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                 String nickname = "default";

                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray ja = new JSONArray(object.getString("result"));
                    JSONObject jo = ja.getJSONObject(0);

                    area = jo.getString("Area");
                    age = jo.getString("Age");

                    if(nick[0]=="0") {
                        nick[0] = jo.getString("Nick_name");
                        item[0] = new Item("http://164.125.154.54/uploads/" + key[0] + "/1.jpg", nick[0], age, area, key[0]);

                        items.add(item[0]);
                        Log.d("nick:0->",""+item[0].getNick());
                        Log.d("nick:key''->",""+key1);

                    }else if(nick[1]=="0"){
                      //  key[1]=key1;
                        Log.d("nick:key''->",""+key1);

                        nick[1] = jo.getString("Nick_name");
                        Log.d("nick:1''->",""+nick[1]);
                        item[1] = new Item("http://164.125.154.54/uploads/"+key[1]+"/1.jpg", nick[1], age, area, key[1]);

                        items.add(item[1]);
                        Log.d("nick:1'''->",""+item[1].getNick());
                      //  Log.d("nick:key''->",""+key1);


                    }else if(nick[2]=="0"){
                    //    key[2]=key1;
                        Log.d("nick:key''->",""+key1);

                        nick[2] = jo.getString("Nick_name");
                        item[2] = new Item("http://164.125.154.54/uploads/"+key[2]+"/1.jpg", nick[2], age, area, key[2]);
                        items.add(item[2]);
                        nick[2]="1";
                        Log.d("nick:2->",""+item[2].getNick());

                    }else if(nick[3]=="0"){
                      //  key[3]=key1;
                        nick[3] = jo.getString("Nick_name");
                        item[3] = new Item("http://164.125.154.54/uploads/"+key[3]+"/1.jpg", nick[3], age, area, key[3]);
                        items.add(item[3]);

                        Log.d("nick:3->",""+item[3].getNick());

                    }
                            recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected String doInBackground(String... params) {
                try {
                    String link = "http://164.125.154.54/getprofile.php";

                    String data = URLEncoder.encode("key", "UTF-8") + "=" + URLEncoder.encode(key1, "UTF-8");
                    URL url = new URL(link);
                    URLConnection conn = url.openConnection();

                    conn.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    StringBuilder sb = new StringBuilder();
                    String line = null;

                    // Read Server Response
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                        break;
                    }
                    return sb.toString();
                } catch (Exception e) {
                    return new String("Exception: " + e.getMessage());
                }
            }
        }
        InsertData task = new InsertData();
        task.execute();
    }
}
