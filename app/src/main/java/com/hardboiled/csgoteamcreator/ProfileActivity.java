package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

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
    private Button openUrlButton;

    private DatabaseReference databaseReference;

    private User currentUser;
    private User userProfile;

    private HashMap<String, Integer> rankIcons;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initializeRankIcons();

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        usernameTextView = (TextView) findViewById(R.id.profile_value_username);
        mmRankImage = (ImageView) findViewById(R.id.profile_mm_rank_image);
        eseaNameTextView = (TextView) findViewById(R.id.profile_value_esea_name);
        eseaRankTextView = (TextView) findViewById(R.id.profile_value_esea_rank);
        roleTextView = (TextView) findViewById(R.id.profile_value_role);
        weaponTextView = (TextView) findViewById(R.id.profile_value_favorite_weapon);
        teamButton = (Button) findViewById(R.id.profile_team_button);
        addTeamButton = (Button) findViewById(R.id.profile_add_team);
        removeTeamButton = (Button) findViewById(R.id.profile_remove_team);
        openUrlButton = (Button) findViewById(R.id.url_button);

        Intent i = this.getIntent();
        String uidLocal = i.getStringExtra("uid");
        String username = i.getStringExtra("username");
        int rank = i.getIntExtra("rank", 0);
        String eseaName = i.getStringExtra("eseaname");
        String eseaRank = i.getStringExtra("esearank");
        String role = i.getStringExtra("role");
        String weapon = i.getStringExtra("weapon");
        String team = i.getStringExtra("team");
        boolean leader = i.getBooleanExtra("leader", false);
        String url = i.getStringExtra("url");
        userProfile = new User(uidLocal, username, rank, eseaName, eseaRank, role, weapon, team, leader, url);

        usernameTextView.setText(username);
        mmRankImage.setImageResource(rank);
        eseaNameTextView.setText(eseaName);
        eseaRankTextView.setText(eseaRank);
        roleTextView.setText(role);
        weaponTextView.setText(weapon);

        if (team.equals("N/A")) {
            teamButton.setVisibility(View.INVISIBLE);
        }

        addTeamButton.setVisibility(View.INVISIBLE);
        removeTeamButton.setVisibility(View.INVISIBLE);

        Query query = databaseReference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findUser(dataSnapshot);
                setButtons();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        setButtonsEnabled(true);
    }

    private void findUser(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot value : dataSnapshot.getChildren()) {
                if (((HashMap<String, String>) value.getValue()).get("uid").equals(uid)) {
                    HashMap<String, String> userHash = ((HashMap<String, String>) value.getValue());
                    String uidLocal = userHash.get("uid");
                    String username = userHash.get("username");
                    int rank = rankIcons.get(userHash.get("rank"));
                    String eseaName = userHash.get("eseaname");
                    String eseaRank = userHash.get("esearank");
                    String role = userHash.get("role");
                    String weapon = userHash.get("weapon");
                    String team = (userHash.containsKey("team")) ? userHash.get("team") : "N/A";
                    boolean leader = (userHash.containsKey("leader")) ? (userHash.get("leader").equals("true")) ? true : false : false;
                    String url = userHash.get("url");
                    currentUser = new User(uidLocal, username, rank, eseaName, eseaRank, role, weapon, team, leader, url);
                }
            }
        } else {
            Snackbar.make(findViewById(R.id.activity_profile_id), "Unable to load users",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    private void setButtons() {
        System.out.println(currentUser.getUid() + "\t" + userProfile.getUid());
        if (currentUser.getUid().equals(userProfile.getUid())) {
            return;
        }
        if (currentUser.isLeader() && userProfile.getTeam().equals("N/A")) {
            addTeamButton.setVisibility(View.VISIBLE);
        }
        if (currentUser.isLeader() && !currentUser.getTeam().equals("N/A") && userProfile.getTeam().equals(currentUser.getTeam())) {
            removeTeamButton.setVisibility(View.VISIBLE);
        }

        addTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);
                addTeamButton.setVisibility(View.INVISIBLE);
                databaseReference.child("users").child(userProfile.getUsername()).child("team").setValue(currentUser.getTeam());
                databaseReference.child("users").child(userProfile.getUsername()).child("leader").setValue("false");
                removeTeamButton.setVisibility(View.VISIBLE);
            }
        });
        removeTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);
                removeTeamButton.setVisibility(View.INVISIBLE);
                databaseReference.child("users").child(userProfile.getUsername()).child("team").setValue("N/A");
                databaseReference.child("users").child(userProfile.getUsername()).child("leader").setValue("false");
                addTeamButton.setVisibility(View.VISIBLE);
            }
        });
        openUrlButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);
                Intent urlIntent = new Intent(ProfileActivity.this, SteamActivity.class);
                urlIntent.putExtra("url", userProfile.getUrl());
                startActivity(urlIntent);
            }
        });
        if (userProfile.getTeam().equals("N/A")) {
            teamButton.setVisibility(View.INVISIBLE);
        }else {
            teamButton.setText(userProfile.getTeam());
            teamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    setButtonsEnabled(false);
                    Intent teamIntent = new Intent(ProfileActivity.this, TeamActivity.class);
                    teamIntent.putExtra("teamname", userProfile.getTeam());
                    startActivity(teamIntent);
                }
            });
        }
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

    private void setButtonsEnabled(boolean enabled) {
        addTeamButton.setEnabled(enabled);
        removeTeamButton.setEnabled(enabled);
        teamButton.setEnabled(enabled);
        openUrlButton.setEnabled(enabled);
    }
}
