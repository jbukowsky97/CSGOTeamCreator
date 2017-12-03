package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Intent intent = this.getIntent();
        System.out.println("***********               " + intent.getStringExtra("username"));
        System.out.println("***********               " + intent.getStringExtra("uid"));
        System.out.println("***********               " + intent.getStringExtra("rank"));
        System.out.println("***********               " + intent.getStringExtra("eseaname"));
        System.out.println("***********               " + intent.getStringExtra("esearank"));
    }
}
