package com.flavienlaurent.notboringactionbar.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.flavienlaurent.notboringactionbar.R;

/**
 * Created by user on 2016-08-18.
 */
public class MainActivity extends AppCompatActivity {
    Button membership;
    Button matching;
    Button master;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        membership = (Button) findViewById(R.id.btnMembership);
        matching = (Button) findViewById(R.id.btnMatching);
        master = (Button) findViewById(R.id.btnMaster);

        membership.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), MembershipActivity.class);
                startActivity(intent);
            }
        });

        matching.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), SelectActivity.class);
                startActivity(intent);
            }
        });

        master.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplication(), MasterActivity.class);
                startActivity(intent);
            }
        });


    }
}
