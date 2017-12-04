package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends AppCompatActivity {

    private TextView username;
    private ImageView mmRank;
    private TextView eseaName;
    private TextView eseaRank;
    private TextView role;
    private TextView weapon;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        username = (TextView) findViewById(R.id.value_username);
        mmRank = (ImageView) findViewById(R.id.mm_rank_image);
        eseaName = (TextView) findViewById(R.id.value_esea_name);
        eseaRank = (TextView) findViewById(R.id.value_esea_rank);
        role = (TextView) findViewById(R.id.value_role);
        weapon = (TextView) findViewById(R.id.value_favorite_weapon);
        searchButton = (Button) findViewById(R.id.search_button);

        Intent i = this.getIntent();
        username.setText(i.getStringExtra("username"));
        String mmRankStr = i.getStringExtra("rank");
        if (mmRankStr.equals("Silver 1")) {
            mmRank.setImageResource(R.drawable.silver_1);
        }else if (mmRankStr.equals("Silver 2")) {
            mmRank.setImageResource(R.drawable.silver_2);
        }else if (mmRankStr.equals("Silver 3")) {
            mmRank.setImageResource(R.drawable.silver_3);
        }else if (mmRankStr.equals("Silver 4")) {
            mmRank.setImageResource(R.drawable.silver_4);
        }else if (mmRankStr.equals("Silver Elite")) {
            mmRank.setImageResource(R.drawable.silver_elite);
        }else if (mmRankStr.equals("Silver Elite Master")) {
            mmRank.setImageResource(R.drawable.silver_elite_master);
        }else if (mmRankStr.equals("Gold Nova 1")) {
            mmRank.setImageResource(R.drawable.gold_nova_1);
        }else if (mmRankStr.equals("Gold Nova 2")) {
            mmRank.setImageResource(R.drawable.gold_nova_2);
        }else if (mmRankStr.equals("Gold Nova 3")) {
            mmRank.setImageResource(R.drawable.gold_nova_3);
        }else if (mmRankStr.equals("Gold Nova Master")) {
            mmRank.setImageResource(R.drawable.gold_nova_master);
        }else if (mmRankStr.equals("Master Guardian 1")) {
            mmRank.setImageResource(R.drawable.master_guardian_1);
        }else if (mmRankStr.equals("Master Guardian 2")) {
            mmRank.setImageResource(R.drawable.master_guardian_2);
        }else if (mmRankStr.equals("Master Guardian Elite")) {
            mmRank.setImageResource(R.drawable.master_guardian_elite);
        }else if (mmRankStr.equals("Distinguished Master Guardian")) {
            mmRank.setImageResource(R.drawable.distinguished_master_guadian);
        }else if (mmRankStr.equals("Legendary Eagle")) {
            mmRank.setImageResource(R.drawable.legendary_eagle);
        }else if (mmRankStr.equals("Legendary Eagle Master")) {
            mmRank.setImageResource(R.drawable.legendary_eagle_master);
        }else if (mmRankStr.equals("Supreme Master First Class")) {
            mmRank.setImageResource(R.drawable.supreme_master_first_class);
        }else if (mmRankStr.equals("The Global Elite")) {
            mmRank.setImageResource(R.drawable.the_global_elite);
        }
        eseaName.setText(i.getStringExtra("eseaname"));
        eseaRank.setText(i.getStringExtra("esearank"));
        role.setText(i.getStringExtra("role"));
        weapon.setText(i.getStringExtra("weapon"));

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        Intent loginIntent = new Intent(HomeActivity.this, LoginActivity.class);
        loginIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(loginIntent);
    }
}
