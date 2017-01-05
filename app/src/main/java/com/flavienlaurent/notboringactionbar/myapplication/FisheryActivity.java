package com.flavienlaurent.notboringactionbar.myapplication;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.flavienlaurent.notboringactionbar.R;

import java.util.ArrayList;

/**
 * Created by user on 2016-08-18.
 */
public class FisheryActivity extends AppCompatActivity {

    TextView tt;
    TextView tt1;
    Button btnFishery;
    EditText editTextFishery;
    String deleteKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fishery);

        btnFishery = (Button) findViewById(R.id.btnFisheryDelete);
        editTextFishery = (EditText) findViewById(R.id.editTextFishery);

        tt = (TextView) findViewById(R.id.tez);
        tt1 = (TextView) findViewById(R.id.tea);
        final DBHelper myFisheryDB = new DBHelper(FisheryActivity.this, "MyFishery.db", null, 1);

        tt.setText(myFisheryDB.getResult());


        ArrayList<String> result = myFisheryDB.getKeys();
        for(int i=0 ;i<result.size(); i++){
            Log.d("keee", result.get(i));
        }

        Log.d("final", Integer.toString(myFisheryDB.getKeys().size()));


        btnFishery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteKey = editTextFishery.getText().toString();
                myFisheryDB.delete(deleteKey);
                tt.setText(myFisheryDB.getResult());

            }
        });
    }
}