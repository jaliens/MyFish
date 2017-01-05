package com.flavienlaurent.notboringactionbar.myapplication;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.flavienlaurent.notboringactionbar.R;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class question extends Activity implements OnItemSelectedListener{


    public RequestManager mGlideRequestManager;
    public String key1="1";
    String s;
    String[] array;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.question);
        s = "자장면za탕수육za짬뽕za칼국수";
        array = s.split("za");
        dumpArray(array);

/* 출력 결과:

array[0] = 자장면
array[1] = 탕수육
array[2] = 짬뽕
array[3] = 칼국수

*/
        ImageView img = (ImageView)findViewById(R.id.q_img);
        img.setImageResource(R.drawable.fish);

        mGlideRequestManager = Glide.with(this);
        mGlideRequestManager.load( "http://164.125.154.55/uploads/1/1.jpg").bitmapTransform(new CropCircleTransformation(getApplicationContext())).into(img);

        // Spinner element
        Spinner spinner = (Spinner) findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // 질문들 리스트
        List<String> categories = new ArrayList<String>();
        categories.add("질문1");
        categories.add("질문2");
        categories.add("질문3");
        categories.add("질문4");
        categories.add("질문5");
        categories.add("질문6");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);




            final EditText edittext=(EditText)findViewById(R.id.edittext);
            Button button=(Button)findViewById(R.id.button);
            final TextView textView=(TextView)findViewById(R.id.textview);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textView.setText(edittext.getText());
                }
            });


    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        //item에 선택된 질문이 저장됨

        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }

    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub

    }

    public static void dumpArray(String[] array) {
        for (int i = 0; i < array.length; i++)
            System.out.format("array[%d] = %s%n", i, array[i]);
    }






    /////


}