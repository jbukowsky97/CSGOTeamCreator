package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashMap;

public class HomeActivity extends AppCompatActivity {

    private TextView username;
    private ImageView mmRank;
    private TextView eseaName;
    private TextView eseaRank;
    private TextView role;
    private TextView weapon;
    private Button searchButton;
    private Button teamButton;

    private HashMap<String, Integer> rankIcons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeRankIcons();

        username = (TextView) findViewById(R.id.value_username);
        mmRank = (ImageView) findViewById(R.id.mm_rank_image);
        eseaName = (TextView) findViewById(R.id.value_esea_name);
        eseaRank = (TextView) findViewById(R.id.value_esea_rank);
        role = (TextView) findViewById(R.id.value_role);
        weapon = (TextView) findViewById(R.id.value_favorite_weapon);
        searchButton = (Button) findViewById(R.id.search_button);
        teamButton = (Button) findViewById(R.id.team_button);

        Intent i = this.getIntent();

        username.setText(i.getStringExtra("username"));
        String mmRankStr = i.getStringExtra("rank");
        mmRank.setImageResource(rankIcons.get(mmRankStr));
        eseaName.setText(i.getStringExtra("eseaname"));
        eseaRank.setText(i.getStringExtra("esearank"));
        role.setText(i.getStringExtra("role"));
        weapon.setText(i.getStringExtra("weapon"));

        String team = i.getStringExtra("team");
        boolean leader = i.getBooleanExtra("leader", false);

        if (team.equals("N/A")) {
            teamButton.setText("Create Team");
        }else {
            teamButton.setText(team);
        }

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });
    }

    private void initializeRankIcons() {
        rankIcons = new HashMap<String, Integer>();
        rankIcons.put("Silver 1", R.drawable.silver_1);
        rankIcons.put("Silver 2", R.drawable.silver_2);
        rankIcons.put("Silver 3", R.drawable.silver_3);
        rankIcons.put("Silver 4", R.drawable.silver_4);
        rankIcons.put("Silver Elite", R.drawable.silver_elite);
        rankIcons.put("Silver Elite Master", R.drawable.silver_elite_master);
        rankIcons.put("Gold Nova 1", R.drawable.gold_nova_1);
        rankIcons.put("Gold Nova 2", R.drawable.gold_nova_2);
        rankIcons.put("Gold Nova 3", R.drawable.gold_nova_3);
        rankIcons.put("Gold Nova Master", R.drawable.gold_nova_master);
        rankIcons.put("Master Guardian 1", R.drawable.master_guardian_1);
        rankIcons.put("Master Guardian 2", R.drawable.master_guardian_2);
        rankIcons.put("Master Guardian Elite", R.drawable.master_guardian_elite);
        rankIcons.put("Distinguished Master Guardian", R.drawable.distinguished_master_guadian);
        rankIcons.put("Legendary Eagle", R.drawable.legendary_eagle);
        rankIcons.put("Legendary Eagle Master", R.drawable.legendary_eagle_master);
        rankIcons.put("Supreme Master First Class", R.drawable.supreme_master_first_class);
        rankIcons.put("The Global Elite", R.drawable.the_global_elite);
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
