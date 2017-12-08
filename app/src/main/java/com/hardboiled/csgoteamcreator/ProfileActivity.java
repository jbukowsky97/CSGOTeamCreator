package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    private TextView usernameTextView;
    private ImageView mmRankImage;
    private TextView eseaNameTextView;
    private TextView eseaRankTextView;
    private TextView roleTextView;
    private TextView weaponTextView;
    private Button teamButton;
    private Button addTeamButton;
    private Button removeTeamButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        usernameTextView = (TextView) findViewById(R.id.profile_value_username);
        mmRankImage = (ImageView) findViewById(R.id.profile_mm_rank_image);
        eseaNameTextView = (TextView) findViewById(R.id.profile_value_esea_name);
        eseaRankTextView = (TextView) findViewById(R.id.profile_value_esea_rank);
        roleTextView = (TextView) findViewById(R.id.profile_value_role);
        weaponTextView = (TextView) findViewById(R.id.profile_value_favorite_weapon);
        teamButton = (Button) findViewById(R.id.profile_team_button);
        addTeamButton = (Button) findViewById(R.id.profile_add_team);
        removeTeamButton = (Button) findViewById(R.id.profile_remove_team);

        Intent i = this.getIntent();
        String username = i.getStringExtra("username");
        int rank = i.getIntExtra("rank", 0);
        String eseaName = i.getStringExtra("eseaname");
        String eseaRank = i.getStringExtra("esearank");
        String role = i.getStringExtra("role");
        String weapon = i.getStringExtra("weapon");
        String team = i.getStringExtra("team");
        boolean leader = i.getBooleanExtra("leader", false);

        usernameTextView.setText(username);
        mmRankImage.setImageResource(rank);
        eseaNameTextView.setText(eseaName);
        eseaRankTextView.setText(eseaRank);
        roleTextView.setText(role);
        weaponTextView.setText(weapon);

        if (team.equals("N/A")) {
            teamButton.setVisibility(View.INVISIBLE);
        }
        if (!leader) {
            addTeamButton.setVisibility(View.VISIBLE);
            addTeamButton.setVisibility(View.VISIBLE);
        }
    }
}
