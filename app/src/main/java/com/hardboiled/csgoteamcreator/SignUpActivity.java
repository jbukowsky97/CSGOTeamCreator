package com.hardboiled.csgoteamcreator;

import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.LinkedList;


public class SignUpActivity extends AppCompatActivity {

    private EditText email;
    private EditText username;
    private EditText password;
    private Spinner rankSpinner;
    private EditText eseaName;
    private Spinner eseaRankSpinner;
    private Spinner roleSpinner;
    private Spinner weaponSpinner;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();

        email = (EditText) findViewById(R.id.email_text);
        username = (EditText) findViewById(R.id.username_text);
        password = (EditText) findViewById(R.id.password_text);
        rankSpinner = (Spinner) findViewById(R.id.rank_spinner);
        eseaName = (EditText) findViewById(R.id.esea_name_text);
        eseaRankSpinner = (Spinner) findViewById(R.id.esea_rank_spinner);
        roleSpinner = (Spinner) findViewById(R.id.role_spinner);
        weaponSpinner = (Spinner) findViewById(R.id.weapon_spinner);

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
        ArrayAdapter<CharSequence> weaponAdapter = ArrayAdapter.createFromResource(
                this, R.array.weapons, R.layout.spinner_item);
        weaponAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        weaponSpinner.setAdapter(weaponAdapter);


        Button signUp = (Button) findViewById(R.id.sign_up_button);
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailStr = email.getText().toString();
                final String usernameStr = username.getText().toString();
                String passwordStr = password.getText().toString();
                final String rankSpinnerStr = rankSpinner.getSelectedItem().toString();
                String eseaNameStr = eseaName.getText().toString();
                final String eseaRankSpinnerStr = eseaRankSpinner.getSelectedItem().toString();
                final String roleSpinnerStr = roleSpinner.getSelectedItem().toString();
                final String weaponSpinnerStr = weaponSpinner.getSelectedItem().toString();

                if (!emailStr.matches("[\\w\\.]+@\\w+\\.\\w+")) {
                    Snackbar.make(findViewById(R.id.activity_sign_up_id), "Enter a valid email",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (usernameStr.matches(".*\\W.*")) {
                    Snackbar.make(findViewById(R.id.activity_sign_up_id), "Username cannot have spaces or special characters",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                if (emailStr.isEmpty() || usernameStr.isEmpty() || passwordStr.isEmpty() || rankSpinnerStr.isEmpty() || eseaRankSpinnerStr.isEmpty() || roleSpinnerStr.isEmpty() || weaponSpinnerStr.isEmpty()) {
                    Snackbar.make(findViewById(R.id.activity_sign_up_id), "Please fill in all necessary values",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    return;
                }

                final String eseaNameFinal = (eseaNameStr.isEmpty()) ? "N/A" : eseaNameStr;

                firebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.activity_sign_up_id), "Successfully logged in",
                                    Snackbar.LENGTH_SHORT)
                                    .show();

                            HashMap<String, String> values = new HashMap<String, String>();
                            values.put("uid", task.getResult().getUser().getUid());
                            values.put("username", usernameStr);
                            values.put("rank", rankSpinnerStr);
                            values.put("eseaname", eseaNameFinal);
                            values.put("esearank", eseaRankSpinnerStr);
                            values.put("role", roleSpinnerStr);
                            values.put("weapon", weaponSpinnerStr);

                            databaseReference.child("users").child(usernameStr).setValue(values);
                        }else {
                            Snackbar.make(findViewById(R.id.activity_sign_up_id), "An error has occurred",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }
                    }
                });
            }
        });
    }
}
