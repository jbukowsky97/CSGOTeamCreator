package com.hardboiled.csgoteamcreator;

import android.app.Activity;
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

public class HomeActivity extends AppCompatActivity {

    private TextView username;
    private ImageView mmRank;
    private TextView eseaName;
    private TextView eseaRank;
    private TextView role;
    private TextView weapon;
    private Button searchButton;
    private Button teamButton;
    private Button updateProfileButton;

    private HashMap<String, Integer> rankIcons;

    private User currentUser;

    private String uid;

    private DatabaseReference databaseReference;

    public static int CODE_CREATE_TEAM = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        initializeRankIcons();


    }

    @Override
    protected void onResume() {
        super.onResume();

        username = (TextView) findViewById(R.id.value_username);
        mmRank = (ImageView) findViewById(R.id.mm_rank_image);
        eseaName = (TextView) findViewById(R.id.value_esea_name);
        eseaRank = (TextView) findViewById(R.id.value_esea_rank);
        role = (TextView) findViewById(R.id.value_role);
        weapon = (TextView) findViewById(R.id.value_favorite_weapon);
        searchButton = (Button) findViewById(R.id.search_button);
        teamButton = (Button) findViewById(R.id.team_button);
        updateProfileButton = (Button) findViewById(R.id.home_update_profile);

        username.setVisibility(View.INVISIBLE);
        mmRank.setVisibility(View.INVISIBLE);
        eseaName.setVisibility(View.INVISIBLE);
        eseaRank.setVisibility(View.INVISIBLE);
        role.setVisibility(View.INVISIBLE);
        weapon.setVisibility(View.INVISIBLE);
        searchButton.setVisibility(View.INVISIBLE);
        teamButton.setVisibility(View.INVISIBLE);
        updateProfileButton.setVisibility(View.INVISIBLE);

        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        Query query = databaseReference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                findUser(dataSnapshot);
                setup();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent searchIntent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(searchIntent);
            }
        });

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfileIntent = new Intent(HomeActivity.this, EditProfileActivity.class);
                startActivityForResult(editProfileIntent, 2);
            }
        });
    }

    private void setup() {
        username.setText(currentUser.getUsername());
        mmRank.setImageResource(currentUser.getRankId());
        eseaName.setText(currentUser.getEseaName());
        eseaRank.setText(currentUser.getEseaRank());
        role.setText(currentUser.getRole());
        weapon.setText(currentUser.getWeapon());

        if (currentUser.getTeam().equals("N/A")) {
            teamButton.setText("Create Team");
            teamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent createTeam = new Intent(HomeActivity.this, CreateTeamActivity.class);
                    startActivityForResult(createTeam, CODE_CREATE_TEAM);
                }
            });
        }else {
            teamButton.setText(currentUser.getTeam());
            teamButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent teamIntent = new Intent(HomeActivity.this, TeamActivity.class);
                    teamIntent.putExtra("teamname", currentUser.getTeam());
                    startActivity(teamIntent);
                }
            });
        }

        username.setVisibility(View.VISIBLE);
        mmRank.setVisibility(View.VISIBLE);
        eseaName.setVisibility(View.VISIBLE);
        eseaRank.setVisibility(View.VISIBLE);
        role.setVisibility(View.VISIBLE);
        weapon.setVisibility(View.VISIBLE);
        searchButton.setVisibility(View.VISIBLE);
        teamButton.setVisibility(View.VISIBLE);
        updateProfileButton.setVisibility(View.VISIBLE);
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
                    currentUser = new User(uidLocal, username, rank, eseaName, eseaRank, role, weapon, team, leader);
                }
            }
        } else {
            Snackbar.make(findViewById(R.id.activity_profile_id), "Unable to load users",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CODE_CREATE_TEAM) {
            if (resultCode == Activity.RESULT_OK) {
                Intent teamIntent = new Intent(HomeActivity.this, TeamActivity.class);
                startActivity(teamIntent);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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
