package com.flavienlaurent.notboringactionbar.myapplication;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import static com.flavienlaurent.notboringactionbar.myapplication.SelectActivity.myDBmgr;
/**
 * Created by rufflez on 6/21/15.
 */
public class CoordinatorFragment extends Fragment{
    RecyclerView recyclerView;

    public static String php_nickname = null;
    public static String speech = null;
    public static String area = null;
    public static String tall = null;
    public static String smoke_or_not = null;
    public static String religion = null;
    public static String php_age = null;
    public static String key1;
    public static String drink = null;
    public static String body = null;
    public static String school = null;
    public static String job = null;
    public static Context context;
    public static SeeMore seeMore;
    public static int j=0;

    public static void send(){
        seeMore.setDduk_bap(j);
    }

    public String getTitle(){
        return php_nickname;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View rootView = inflater.inflate(R.layout.coordinator_layout, container, false);
        recyclerView = (RecyclerView)rootView.findViewById(R.id.recyclerView);

        context = getContext();
        seeMore = (SeeMore) getActivity();
        SeeMore getkey = new SeeMore();
        key1 = getkey.getKey();
        Log.d("key in fragment:",key1+"");
        setupRecyclerView(recyclerView);



        //jjy
        MyFirebaseFunctions.registerToken2Server();

        return rootView;
    }

    private void setupRecyclerView(RecyclerView recyclerView){
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
//        String[] data = {                "secondline"
//        };
        getKeyPhp1();

        String data = "test";
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(getActivity(),
                data));
    }

    public static class SimpleStringRecyclerViewAdapter extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder>{
        private String[] mValues;
        private Context mContext;
        private String test;


        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView mTextView;
            public Button age;
            public TextView locate;
            public Button tall;
            public Button religion;
            public Button smoke;
            public Button speech;
            public Button drink;
            public Button school;
            public Button body;
            public Button job;
            public Button go_chatroom;
            public ViewHolder(View view) {

                super(view);
                mView = view;
                mTextView = (TextView) view.findViewById(android.R.id.text1);
                age = (Button) itemView.findViewById(R.id.age_button);
                locate = (TextView) itemView.findViewById(R.id.locate);
                tall = (Button) itemView.findViewById(R.id.tall_button);
                religion = (Button) itemView.findViewById(R.id.religion_button);
                smoke   = (Button) itemView.findViewById(R.id.smoke_button);
                speech  = (Button) itemView.findViewById(R.id.speech_button);
                drink  = (Button) itemView.findViewById(R.id.drink_button);
                school  = (Button) itemView.findViewById(R.id.school_button);
                body  = (Button) itemView.findViewById(R.id.body_button);
                job  = (Button) itemView.findViewById(R.id.job_button);
                go_chatroom = (Button) itemView.findViewById(R.id.go_chatroom_btn);

                go_chatroom.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ////채팅방가기
                        go_chatroom.setBackgroundResource(R.drawable.go_chat_button_other);
                        ////
                        String targetId = "사람2";//php_nickname;
                        CoordinatorFragment.j = 10;
                        CoordinatorFragment.send();
//                Intent intent = new Intent();
//                intent.putExtra("will_register","no");

                        //클라이언트 DB에 대화상대 등록 (채팅 신청)
                        myDBmgr.registerDialogTargetById(targetId,"0");

                        //채팅 요청 보내기
                        MyFirebaseFunctions.sendChatReq(targetId);

                        //상대가 먼저 나에게 채팅 요청한 경우 채팅방 활성화
                        if(myDBmgr.checkChatRoomStatus(targetId).equals("1")){
                            myDBmgr.activateDialogTargetById(targetId);
                        }
                        context.startActivity(new Intent(context, ChatRoomListActivity.class));
                    }
                });

                drink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        drink.setBackgroundColor(Color.WHITE);
                        drink.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 1;
                        CoordinatorFragment.send();
                    }
                });
                school.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        school.setBackgroundColor(Color.WHITE);
                        school.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 2;
                        CoordinatorFragment.send();
                    }
                });
                body.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        body.setBackgroundColor(Color.WHITE);
                        body.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 1;
                        CoordinatorFragment.send();
                    }
                });
                job.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        job.setBackgroundColor(Color.WHITE);
                        job.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 2;
                        CoordinatorFragment.send();
                    }
                });



                age.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        age.setBackgroundColor(Color.WHITE);
                        age.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 4;
                        CoordinatorFragment.send();
                    }
                });

                tall.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        tall.setBackgroundColor(Color.WHITE);
                        tall.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 3;
                        CoordinatorFragment.send();
                    }
                });

                religion.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        religion.setBackgroundColor(Color.WHITE);
                        religion.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 1;
                        CoordinatorFragment.send();
                    }
                });

                smoke.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        smoke.setBackgroundColor(Color.WHITE);
                        smoke.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 1;
                        CoordinatorFragment.send();
                    }
                });

                speech.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        speech.setBackgroundColor(Color.WHITE);
                        speech.setTextColor(Color.BLACK);
                        CoordinatorFragment.j = 1;
                        CoordinatorFragment.send();
                    }
                });
            }

        }

        public String getValueAt(int position) {
            return mValues[position];
        }

        public SimpleStringRecyclerViewAdapter(Context context, String items) {
            mContext = context;
            test = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//            View view = LayoutInflater.from(parent.getContext())
//                    .inflate(android.R.layout.simple_list_item_1, parent, false);

            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);

            // Inflate the custom layout
            View contactView = inflater.inflate(R.layout.item_contact, parent, false);

            // Return a new holder instance
            ViewHolder viewHolder = new ViewHolder(contactView);


            return viewHolder;
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
//            holder.mTextView.setText(mValues[position]);
//            holder.mView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Snackbar.make(v, getValueAt(position), Snackbar.LENGTH_SHORT).show();
//                }
//            });

            ///
//            Contact contact = mContacts.get(position);
//            ViewHolder viewHolder = new ViewHolder(contactView);

            // Set item views based on your views and data model
            Button age_button = holder.age;
//            age_button.setBackgroundColor(Color.WHITE);
            age_button.setText(php_age);
            Log.d("php_age: ",php_age+"");
//            textView.setText(contact.getName());
            TextView locate_text = holder.locate;
            locate_text.setText(area);

        }

        @Override
        public int getItemCount() {
            return 1;
        }
    }

    public void getKeyPhp1() {
        class InsertData extends AsyncTask<String, Void, String> {

            ProgressDialog loading;
            //  public String php_nickname;
            //  public String php_age;
            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getContext(), "잠시만 기다려 주세요!", null, true, true);
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                loading.dismiss();
                try {
                    JSONObject object = new JSONObject(s);
                    JSONArray ja = new JSONArray(object.getString("result"));
                    JSONObject jo = ja.getJSONObject(0);

                    php_nickname = jo.getString("Nick_name");
//                    SeeMore nick = new SeeMore();
//                    nick.setTitle(php_nickname);
                    speech = jo.getString("Saying");
                    area = jo.getString("Area");
                    tall = jo.getString("Tall");
                    smoke_or_not = jo.getString("Smoke_or_not");
                    religion = jo.getString("Religion");
                    php_age = jo.getString("Age");

                    drink = jo.getString("Drink");
                    body = jo.getString("Body");
                    job = jo.getString("Job");
                    school = jo.getString("School");

                    SimpleStringRecyclerViewAdapter.ViewHolder viewHolder = new SimpleStringRecyclerViewAdapter.ViewHolder(getView());

                    // Set item views based on your views and data model
                    Button age_button = viewHolder.age;
                    age_button.setText(php_age);
                    age_button.setTextColor(Color.TRANSPARENT);

                    TextView locate_text = viewHolder.locate;
                    locate_text.setText(area);

                    Button tall_button = viewHolder.tall;
                    tall_button.setText(tall);
                    tall_button.setTextColor(Color.TRANSPARENT);

                    Button religion_button = viewHolder.religion;
                    religion_button.setText(religion);
                    religion_button.setTextColor(Color.TRANSPARENT);

                    Button smoke_button = viewHolder.smoke;
                    smoke_button.setText(smoke_or_not);
                    smoke_button.setTextColor(Color.TRANSPARENT);

                    Button speech_button = viewHolder.speech;
                    speech_button.setText(speech);
                    speech_button.setTextColor(Color.TRANSPARENT);

                    Button body_button = viewHolder.body;
                    body_button.setText(body);
                    body_button.setTextColor(Color.TRANSPARENT);

                    Button drink_button = viewHolder.drink;
                    drink_button.setText(drink);
                    drink_button.setTextColor(Color.TRANSPARENT);

                    Button job_button = viewHolder.job;
                    job_button.setText(job);
                    job_button.setTextColor(Color.TRANSPARENT);

                    Button school_button = viewHolder.school;
                    school_button.setText(school);
                    school_button.setTextColor(Color.TRANSPARENT);

//                    Button job_button = viewHolder.job;
//                    job_button.setText(job);
//                    job_button.setTextColor(Color.TRANSPARENT);

//                    Button school_button = viewHolder.school;
//                    school_button.setText(school);
//                    school_button.setTextColor(Color.TRANSPARENT);

//                    Button body_button = viewHolder.body;
//                    body_button.setText(body);
//                    body_button.setTextColor(Color.TRANSPARENT);

                    Log.d("json",php_nickname);
                    Log.d("json",php_age);
                    Log.d("json",speech);


                    //  Log.d("json",nickname);
                    // mTitleView.setText(php_nickname);
                    //잠시 닉넴은 없애음 필요함
                    //   select_nickname.setText(php_nickname);
                    //  select_age.setText(php_age);

                    //  select_speech1.setText(speech);
                    //   select_area1.setText(area);

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