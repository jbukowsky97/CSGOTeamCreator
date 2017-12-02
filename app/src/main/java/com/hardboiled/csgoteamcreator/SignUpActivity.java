package com.hardboiled.csgoteamcreator;

import android.graphics.PorterDuff;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Spinner rankSpinner = (Spinner) findViewById(R.id.rank_spinner);
        rankSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> mmAdapter = ArrayAdapter.createFromResource(
                this, R.array.mm_ranks, R.layout.spinner_item);
        mmAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        rankSpinner.setAdapter(mmAdapter);

        Spinner eseaRankSpinner = (Spinner) findViewById(R.id.esea_rank_spinner);
        eseaRankSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> eseaAdapter = ArrayAdapter.createFromResource(
                this, R.array.esea_ranks, R.layout.spinner_item);
        eseaAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        eseaRankSpinner.setAdapter(eseaAdapter);

        Spinner roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        roleSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                this, R.array.positions, R.layout.spinner_item);
        roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        roleSpinner.setAdapter(roleAdapter);

        Spinner weaponSpinner = (Spinner) findViewById(R.id.weapon_spinner);
        weaponSpinner.getBackground().setColorFilter(getResources().getColor(R.color.white), PorterDuff.Mode.SRC_ATOP);
        ArrayAdapter<CharSequence> weaponAdapter = ArrayAdapter.createFromResource(
                this, R.array.weapons, R.layout.spinner_item);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);
    }
}
