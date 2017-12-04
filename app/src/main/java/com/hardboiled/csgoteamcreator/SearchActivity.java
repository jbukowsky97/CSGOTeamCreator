package com.hardboiled.csgoteamcreator;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SearchActivity extends AppCompatActivity {

    private Spinner searchSpinner;
    private RecyclerView recyclerView;
    private List<User> users;
    private HashMap<Integer, Integer> mmRankComparisons;
    private HashMap<String, Integer> eseaRankComparisons;
    private RVAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

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
        users.add(new User("AnnaKendrick", R.drawable.the_global_elite, "Entry Fragger", "A+"));
        users.add(new User("TaylorSwift", R.drawable.silver_3, "Lurker", "D+"));
        users.add(new User("AlexandraDaddario", R.drawable.silver_elite_master, "PlayMaker", "C"));
        users.add(new User("Jonah", R.drawable.master_guardian_1, "Awper", "B-"));
        users.add(new User("Sarah", R.drawable.legendary_eagle_master, "Support", "A"));
        users.add(new User("Hannah", R.drawable.gold_nova_master, "Entry Fragger", "B-"));
        users.add(new User("Julia", R.drawable.silver_elite_master, "Awper", "C+"));
        users.add(new User("Wesley", R.drawable.supreme_master_first_class, "Lurker", "A-"));
        users.add(new User("DogPoo", R.drawable.silver_elite, "Strat Caller", "D"));
        users.add(new User("Wrigley", R.drawable.silver_4, "Entry Fragger", "C+"));
        users.add(new User("FingerLickenGood", R.drawable.master_guardian_2, "Support", "B-"));
        users.add(new User("AnimalLover", R.drawable.master_guardian_elite, "Lurker", "None"));
        rvAdapter = new RVAdapter(users);
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
        rvAdapter = new RVAdapter(users);
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
    }
}
