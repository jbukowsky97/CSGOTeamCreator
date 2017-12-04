package com.hardboiled.csgoteamcreator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class CardViewActivity extends AppCompatActivity {

    private TextView username;
    private ImageView rank;
    private TextView role;
    private TextView eseaRank;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card_view);

        username = (TextView) findViewById(R.id.card_username);
        rank = (ImageView) findViewById(R.id.card_mm_rank);
        role = (TextView) findViewById(R.id.card_role_value);
        eseaRank = (TextView) findViewById(R.id.card_esea_rank_value);
    }
}
