package com.hardboiled.csgoteamcreator;

import android.app.Activity;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class EditProfileActivity extends AppCompatActivity {

    private HashMap<String, Integer> rankIcons;
    private HashMap<String, Integer> stringNums;
    private HashMap<Integer, Integer> mmRankNums;

    private User currentUser;

    private EditText url;
    private Spinner rankSpinner;
    private EditText eseaName;
    private Spinner eseaRankSpinner;
    private Spinner roleSpinner;
    private Spinner weaponSpinner;
    private Button updateProfileButton;

    private String uid;

    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initializeRankIcons();
    }

    @Override
    protected void onResume() {
        super.onResume();

        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        url = (EditText) findViewById(R.id.edit_profile_url_text);
        rankSpinner = (Spinner) findViewById(R.id.edit_profile_rank_spinner);
        eseaName = (EditText) findViewById(R.id.edit_profile_esea_name_text);
        eseaRankSpinner = (Spinner) findViewById(R.id.edit_profile_esea_rank_spinner);
        roleSpinner = (Spinner) findViewById(R.id.edit_profile_role_spinner);
        weaponSpinner = (Spinner) findViewById(R.id.edit_profile_weapon_spinner);
        updateProfileButton = (Button) findViewById(R.id.edit_profile_update_button);

        url.setVisibility(View.INVISIBLE);
        rankSpinner.setVisibility(View.INVISIBLE);
        eseaName.setVisibility(View.INVISIBLE);
        eseaRankSpinner.setVisibility(View.INVISIBLE);
        roleSpinner.setVisibility(View.INVISIBLE);
        weaponSpinner.setVisibility(View.INVISIBLE);
        updateProfileButton.setVisibility(View.INVISIBLE);

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

        rankSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> mmAdapter = ArrayAdapter.createFromResource(
                this, R.array.mm_ranks, R.layout.spinner_item);
        mmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankSpinner.setAdapter(mmAdapter);

        eseaRankSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> eseaAdapter = ArrayAdapter.createFromResource(
                this, R.array.esea_ranks, R.layout.spinner_item);
        eseaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eseaRankSpinner.setAdapter(eseaAdapter);

        roleSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                this, R.array.positions, R.layout.spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        weaponSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        final ArrayAdapter<CharSequence> weaponAdapter = ArrayAdapter.createFromResource(
                this, R.array.weapons, R.layout.spinner_item);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);

        updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (url.getText().toString().matches(".*\\W.*")) {
                    Snackbar.make(findViewById(R.id.activity_edit_profile_id), "Steam URL cannot have spaces or special characters",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                databaseReference.child("users").child(currentUser.getUsername()).child("rank").setValue(rankSpinner.getSelectedItem().toString());
                databaseReference.child("users").child(currentUser.getUsername()).child("eseaname").setValue(eseaName.getText().toString());
                databaseReference.child("users").child(currentUser.getUsername()).child("esearank").setValue(eseaRankSpinner.getSelectedItem().toString());
                databaseReference.child("users").child(currentUser.getUsername()).child("weapon").setValue(weaponSpinner.getSelectedItem().toString());
                databaseReference.child("users").child(currentUser.getUsername()).child("role").setValue(roleSpinner.getSelectedItem().toString());
                setResult(Activity.RESULT_OK);
                finish();
            }
        });

    }
    
    private void setup() {
        rankSpinner.setSelection(mmRankNums.get(currentUser.getRankId()));
        eseaRankSpinner.setSelection(stringNums.get(currentUser.getEseaRank()));
        roleSpinner.setSelection(stringNums.get(currentUser.getRole()));
        weaponSpinner.setSelection(stringNums.get(currentUser.getWeapon()));
        //url.setText(currentUser.getUrl());
        eseaName.setText(currentUser.getEseaName());

        url.setVisibility(View.VISIBLE);
        rankSpinner.setVisibility(View.VISIBLE);
        eseaName.setVisibility(View.VISIBLE);
        eseaRankSpinner.setVisibility(View.VISIBLE);
        roleSpinner.setVisibility(View.VISIBLE);
        weaponSpinner.setVisibility(View.VISIBLE);
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
        mmRankNums = new HashMap<Integer, Integer>();
        mmRankNums.put(R.drawable.silver_1, 0);
        mmRankNums.put(R.drawable.silver_2, 1);
        mmRankNums.put(R.drawable.silver_3, 2);
        mmRankNums.put(R.drawable.silver_4, 3);
        mmRankNums.put(R.drawable.silver_elite, 4);
        mmRankNums.put(R.drawable.silver_elite_master, 5);
        mmRankNums.put(R.drawable.gold_nova_1, 6);
        mmRankNums.put(R.drawable.gold_nova_2, 7);
        mmRankNums.put(R.drawable.gold_nova_3, 8);
        mmRankNums.put(R.drawable.gold_nova_master, 9);
        mmRankNums.put(R.drawable.master_guardian_1, 10);
        mmRankNums.put(R.drawable.master_guardian_2, 11);
        mmRankNums.put(R.drawable.master_guardian_elite, 12);
        mmRankNums.put(R.drawable.distinguished_master_guadian, 13);
        mmRankNums.put(R.drawable.legendary_eagle, 14);
        mmRankNums.put(R.drawable.legendary_eagle_master, 15);
        mmRankNums.put(R.drawable.supreme_master_first_class, 16);
        mmRankNums.put(R.drawable.the_global_elite, 17);
        stringNums = new HashMap<String, Integer>();
        stringNums.put("None", 0);
        stringNums.put("D-", 1);
        stringNums.put("D", 2);
        stringNums.put("D+", 3);
        stringNums.put("C-", 4);
        stringNums.put("C", 5);
        stringNums.put("C+", 6);
        stringNums.put("B-", 7);
        stringNums.put("B", 8);
        stringNums.put("B+", 9);
        stringNums.put("A-", 10);
        stringNums.put("A", 11);
        stringNums.put("A+", 12);
        stringNums.put("Entry Fragger", 0);
        stringNums.put("Playmaker", 1);
        stringNums.put("Strat Caller", 2);
        stringNums.put("Support", 3);
        stringNums.put("Awper", 4);
        stringNums.put("Lurker", 5);
        stringNums.put("AK-47", 0);
        stringNums.put("M4A4", 1);
        stringNums.put("M4A1-S", 2);
        stringNums.put("AWP", 3);
        stringNums.put("Desert Eagle", 4);
        stringNums.put("CZ75 Auto", 5);
        stringNums.put("P250", 6);
        stringNums.put("USP-S", 7);
        stringNums.put("Glock-18", 8);
    }
}
