package com.hardboiled.csgoteamcreator;

import android.app.Activity;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class CreateTeamActivity extends AppCompatActivity {

    private EditText teamNameText;
    private Button createTeamButton;
    private DatabaseReference databaseReference;
    private HashMap<String, Integer> rankIcons;
    private User currentUser;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_team);

        initializeRankIcons();
    }

    @Override
    protected void onStart() {
        super.onStart();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        teamNameText = (EditText) findViewById(R.id.new_team_text);
        createTeamButton = (Button) findViewById(R.id.create_new_team_button);

        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);
                if (teamNameText.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.activity_create_team_id), "Please fill in all necessary values",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                Query query = databaseReference.child("users");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        findUser(dataSnapshot);
                        databaseReference.child("users").child(currentUser.getUsername()).child("team").setValue(teamNameText.getText().toString());
                        databaseReference.child("users").child(currentUser.getUsername()).child("leader").setValue("true");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });
                setButtonsEnabled(true);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        teamNameText = (EditText) findViewById(R.id.new_team_text);
        createTeamButton = (Button) findViewById(R.id.create_new_team_button);

        createTeamButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (teamNameText.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.activity_create_team_id), "Please fill in all necessary values",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }
                Query query = databaseReference.child("users");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        findUser(dataSnapshot);
                        databaseReference.child("users").child(currentUser.getUsername()).child("team").setValue(teamNameText.getText().toString());
                        databaseReference.child("users").child(currentUser.getUsername()).child("leader").setValue("true");
                        setResult(Activity.RESULT_OK);
                        finish();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        return;
                    }
                });
            }
        });
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
        createTeamButton.setEnabled(enabled);
    }
}
