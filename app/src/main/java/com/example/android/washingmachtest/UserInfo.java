package com.example.android.washingmachtest;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

/**
 * Created by anuj on 15/1/17.
 */

public class UserInfo extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info);
        Intent i = getIntent();
        WashingMachine washingMachine = i.getParcelableExtra("WMInfo");

        TextView userName = (TextView) findViewById(R.id.user_name);
        userName.setText(washingMachine.getUserName());

        TextView roomNo = (TextView) findViewById(R.id.room_no);
        roomNo.setText(washingMachine.getUserRoom());
    }
}
