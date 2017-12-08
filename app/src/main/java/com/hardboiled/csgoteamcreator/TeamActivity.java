package com.hardboiled.csgoteamcreator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.LinkedList;

public class TeamActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RVAdapter rvAdapter;
    private LinkedList<User> users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        recyclerView = (RecyclerView) findViewById(R.id.team_recycler_view);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);

        users = new LinkedList<User>();
//        users.add(new User("AnnaKendrick", R.drawable.the_global_elite, "Entry Fragger", "A+"));
//        users.add(new User("TaylorSwift", R.drawable.silver_3, "Lurker", "D+"));
//        users.add(new User("AlexandraDaddario", R.drawable.silver_elite_master, "PlayMaker", "C"));
//        users.add(new User("Jonah", R.drawable.master_guardian_1, "Awper", "B-"));
//        users.add(new User("Wesley", R.drawable.supreme_master_first_class, "Lurker", "A-"));
        //rvAdapter = new RVAdapter(, users);
        //recyclerView.setAdapter(rvAdapter);
    }
}
