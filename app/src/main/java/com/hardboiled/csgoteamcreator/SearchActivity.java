package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class SearchActivity extends AppCompatActivity {

    private Spinner searchSpinner;
    private RecyclerView recyclerView;
    private List<User> users;
    private HashMap<Integer, Integer> mmRankComparisons;
    private HashMap<String, Integer> eseaRankComparisons;
    private HashMap<String, Integer> rankIcons;
    private RVAdapter rvAdapter;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        System.out.println(uid);

        this.initializeRankComparisons();

        searchSpinner = (Spinner) findViewById(R.id.search_spinner);
        recyclerView = (RecyclerView) findViewById(R.id.card_recycler);
        final String[] search_types = getResources().getStringArray(R.array.search_types);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        searchSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> searchAdapter = ArrayAdapter.createFromResource(
                this, R.array.search_types, R.layout.spinner_item);
        searchAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(searchAdapter);

        users = new LinkedList<User>();

        Query query = databaseReference.child("users");
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                initializeUsers(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                return;
            }
        });

        rvAdapter = new RVAdapter(new MyOnClickListener(), users);
        recyclerView.setAdapter(rvAdapter);



        searchSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String searchType = search_types[i];
                sort_users(searchType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private class MyOnClickListener implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            int itemPosition = recyclerView.getChildLayoutPosition(view);
            User user = rvAdapter.getUser(itemPosition);
            Intent profileIntent = new Intent(SearchActivity.this, ProfileActivity.class);
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

    private void initializeUsers(DataSnapshot dataSnapshot) {
        if (dataSnapshot.exists()) {
            for (DataSnapshot value : dataSnapshot.getChildren()) {
                String uidLocal = "";
                String username = "";
                String rank = "";
                String eseaName = "";
                String eseaRank = "";
                String role = "";
                String weapon = "";
                String team = "N/A";
                boolean leader = false;
                for (Map.Entry<String, String> userfield : ((HashMap<String, String>) value.getValue()).entrySet()) {
                    if (userfield.getKey().equals("uid")) {
                        uidLocal = userfield.getValue();
                    }else if (userfield.getKey().equals("username")) {
                        username = userfield.getValue();
                    }else if (userfield.getKey().equals("rank")) {
                        rank = userfield.getValue();
                    }else if (userfield.getKey().equals("eseaname")) {
                        eseaName = userfield.getValue();
                    }else if (userfield.getKey().equals("esearank")) {
                        eseaRank = userfield.getValue();
                    }else if (userfield.getKey().equals("role")) {
                        role = userfield.getValue();
                    }else if (userfield.getKey().equals("weapon")) {
                        weapon = userfield.getValue();
                    }else if (userfield.getKey().equals("team")) {
                        team = userfield.getValue();
                    }else if (userfield.getKey().equals("leader")) {
                        leader = (userfield.getValue().equals("true")) ? true : false;
                    }
                }
                if (!uid.equals(uidLocal)) {
                    users.add(new User(uid, username, rankIcons.get(rank), eseaName, eseaRank, role, weapon, team, leader));
                }
            }
            sort_users("MM Rank (High to Low)");
        } else {
            Snackbar.make(findViewById(R.id.activity_sign_up_id), "Unable to load users",
                    Snackbar.LENGTH_SHORT)
                    .show();
            return;
        }
    }

    private void sort_users(String searchType) {
        if (searchType.equals("MM Rank (High to Low)")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if (mmRankComparisons.get(o1.getRankId()) < mmRankComparisons.get(o2.getRankId())) {
                        return 1;
                    }else if (mmRankComparisons.get(o1.getRankId()) > mmRankComparisons.get(o2.getRankId())) {
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });

        }else if (searchType.equals("MM Rank (Low to High)")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if (mmRankComparisons.get(o1.getRankId()) < mmRankComparisons.get(o2.getRankId())) {
                        return -1;
                    }else if (mmRankComparisons.get(o1.getRankId()) > mmRankComparisons.get(o2.getRankId())) {
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
        }else if (searchType.equals("ESEA Rank (High to Low)")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if (eseaRankComparisons.get(o1.getEseaRank()) < eseaRankComparisons.get(o2.getEseaRank())) {
                        return 1;
                    }else if (eseaRankComparisons.get(o1.getEseaRank()) > eseaRankComparisons.get(o2.getEseaRank())) {
                        return -1;
                    }else {
                        return 0;
                    }
                }
            });

        }else if (searchType.equals("ESEA Rank (Low to High)")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    if (eseaRankComparisons.get(o1.getEseaRank()) < eseaRankComparisons.get(o2.getEseaRank())) {
                        return -1;
                    }else if (eseaRankComparisons.get(o1.getEseaRank()) > eseaRankComparisons.get(o2.getEseaRank())) {
                        return 1;
                    }else {
                        return 0;
                    }
                }
            });
        }else if (searchType.equals("Role")) {
            Collections.sort(users, new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getRole().compareTo(o2.getRole());
                }
            });
        }
        rvAdapter = new RVAdapter(new MyOnClickListener(), users);
        recyclerView.swapAdapter(rvAdapter, false);
    }

    private void initializeRankComparisons() {
        mmRankComparisons = new HashMap<Integer, Integer>();
        mmRankComparisons.put(R.drawable.silver_1, 1);
        mmRankComparisons.put(R.drawable.silver_2, 2);
        mmRankComparisons.put(R.drawable.silver_3, 3);
        mmRankComparisons.put(R.drawable.silver_4, 4);
        mmRankComparisons.put(R.drawable.silver_elite, 5);
        mmRankComparisons.put(R.drawable.silver_elite_master, 6);
        mmRankComparisons.put(R.drawable.gold_nova_1, 7);
        mmRankComparisons.put(R.drawable.gold_nova_2, 8);
        mmRankComparisons.put(R.drawable.gold_nova_3, 9);
        mmRankComparisons.put(R.drawable.gold_nova_master, 10);
        mmRankComparisons.put(R.drawable.master_guardian_1, 11);
        mmRankComparisons.put(R.drawable.master_guardian_2, 12);
        mmRankComparisons.put(R.drawable.master_guardian_elite, 13);
        mmRankComparisons.put(R.drawable.distinguished_master_guadian, 14);
        mmRankComparisons.put(R.drawable.legendary_eagle, 15);
        mmRankComparisons.put(R.drawable.legendary_eagle_master, 16);
        mmRankComparisons.put(R.drawable.supreme_master_first_class, 17);
        mmRankComparisons.put(R.drawable.the_global_elite, 18);
        eseaRankComparisons = new HashMap<String, Integer>();
        eseaRankComparisons.put("None", 0);
        eseaRankComparisons.put("D-", 1);
        eseaRankComparisons.put("D", 2);
        eseaRankComparisons.put("D+", 3);
        eseaRankComparisons.put("C-", 4);
        eseaRankComparisons.put("C", 5);
        eseaRankComparisons.put("C+", 6);
        eseaRankComparisons.put("B-", 7);
        eseaRankComparisons.put("B", 8);
        eseaRankComparisons.put("B+", 9);
        eseaRankComparisons.put("A-", 10);
        eseaRankComparisons.put("A", 11);
        eseaRankComparisons.put("A+", 12);
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
