package com.hardboiled.csgoteamcreator;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private EditText password;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    private Button login;
    private Button createAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.value_email);
        password = (EditText) findViewById(R.id.password_text);

        login = (Button) this.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons(false);

                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.login_activity_id), "Please enter an email and a password",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    setButtons(true);
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.login_activity_id), "Invalid credentials, please ensure the account exists.",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            setButtons(true);
                            return;
                        }else {
                            final Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            final String uid = task.getResult().getUser().getUid();
                            Query query = databaseReference.child("users");
                            query.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        for (DataSnapshot value : dataSnapshot.getChildren()) {
                                            if (((HashMap<String, String>) value.getValue()).containsValue(uid)) {
                                                for (Map.Entry<String, String> entry : ((HashMap<String, String>) value.getValue()).entrySet()) {
                                                    homeIntent.putExtra(entry.getKey(), entry.getValue());
                                                }
                                                break;
                                            }
                                        }
                                        startActivity(homeIntent);
                                        setButtons(true);
                                    } else {
                                        Snackbar.make(findViewById(R.id.activity_sign_up_id), "Unable to find user in database",
                                                Snackbar.LENGTH_SHORT)
                                                .show();
                                        setButtons(true);
                                        return;
                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    Snackbar.make(findViewById(R.id.activity_sign_up_id), "Operation cancelled.",
                                            Snackbar.LENGTH_SHORT)
                                            .show();
                                    setButtons(true);
                                    return;
                                }
                            });
                        }
                    }
                });
            }
        });

        createAccount = (Button) this.findViewById(R.id.create_account_button);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtons(false);
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
                setButtons(true);
            }
        });
    }

    private void setButtons(boolean enabled) {
        login.setEnabled(enabled);
        createAccount.setEnabled(enabled);
    }
}
