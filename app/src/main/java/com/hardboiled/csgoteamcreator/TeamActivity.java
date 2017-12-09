package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;

public class TeamActivity extends AppCompatActivity {

    private TextView teamNameView;
    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private LinkedList<User> users;

    private DatabaseReference databaseReference;

    private HashMap<String, Integer> rankIcons;

    private String teamName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        databaseReference = FirebaseDatabase.getInstance().getReference();

        Intent i = getIntent();
        teamName = i.getStringExtra("teamname");

        teamNameView = (TextView) findViewById(R.id.team_name_value);
        recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);

        initializeRankIcons();
    }
    @Override
    protected void onResume() {
        super.onResume();
        setup();
    }

    private void setup() {

        teamNameView.setText(teamName);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        users = new LinkedList<User>();

        final Query query = databaseReference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initializeTeam(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });
    }

    private void initializeTeam(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot value : dataSnapshot.getChildren()) {
                HashMap<String, String> userHash = ((HashMap<String, String>) value.getValue());
                if (userHash.containsKey("team") && userHash.get("team").equals(teamName)) {
                    String uidLocal = userHash.get("uid");
                    String username = userHash.get("username");
                    int rank = rankIcons.get(userHash.get("rank"));
                    String eseaName = userHash.get("eseaname");
                    String eseaRank = userHash.get("esearank");
                    String role = userHash.get("role");
                    String weapon = userHash.get("weapon");
                    String team = (userHash.containsKey("team")) ? userHash.get("team") : "N/A";
                    boolean leader = (userHash.containsKey("leader")) ? (userHash.get("leader").equals("true")) ? true : false : false;
                    users.add(new User(uidLocal, username, rank, eseaName, eseaRank, role, weapon, team, leader));
                }
            }
            rvAdapter = new RVAdapter(new MyOnClickListener(), users);
            recyclerView.setAdapter(rvAdapter);
        } else {
            Snackbar.make(findViewById(R.id.team_activity_id), "Unable to load users",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            User user = rvAdapter.getUser(itemPosition);
            Intent profileIntent = new Intent(TeamActivity.this, ProfileActivity.class);
            profileIntent.putExtra("uid", user.getUid());
            profileIntent.putExtra("username", user.getUsername());
            profileIntent.putExtra("rank", user.getRankId());
            profileIntent.putExtra("eseaname", user.getEseaName());
            profileIntent.putExtra("esearank", user.getEseaRank());
            profileIntent.putExtra("role", user.getRole());
            profileIntent.putExtra("weapon", user.getWeapon());
            profileIntent.putExtra("team", user.getTeam());
            profileIntent.putExtra("leader", user.isLeader());
            startActivity(profileIntent);
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
}
