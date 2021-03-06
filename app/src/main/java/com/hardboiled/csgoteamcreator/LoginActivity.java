package com.hardboiled.csgoteamcreator;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.SystemClock;
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

        createAlarm();

        firebaseAuth = FirebaseAuth.getInstance();

        email = (EditText) findViewById(R.id.value_email);
        password = (EditText) findViewById(R.id.password_text);

        login = (Button) this.findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);

                if (email.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
                    Snackbar.make(findViewById(R.id.login_activity_id), "Please enter an email and a password",
                            Snackbar.LENGTH_SHORT)
                            .show();
                    setButtonsEnabled(true);
                    return;
                }
                firebaseAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString()).addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            Snackbar.make(findViewById(R.id.login_activity_id), "Invalid credentials, please ensure the account exists.",
                                    Snackbar.LENGTH_SHORT)
                                    .show();
                            return;
                        }else {
                            final Intent homeIntent = new Intent(LoginActivity.this, HomeActivity.class);
                            databaseReference = FirebaseDatabase.getInstance().getReference();
                            startActivity(homeIntent);
                        }
                    }
                });
            }
        });

        createAccount = (Button) this.findViewById(R.id.create_account_button);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsEnabled(false);
                Intent signupIntent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(signupIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        setButtonsEnabled(true);
    }

    private void setButtonsEnabled(boolean enabled) {
        login.setEnabled(enabled);
        createAccount.setEnabled(enabled);
    }

    private void createAlarm() {
        // wait 10 seconds
//        long timeToAdd = 10000;
        // wait a day
        long timeToAdd = 86400000;
        // starting alarm
        Intent intent = new Intent(LoginActivity.this, AlarmReceiver.class);
        intent.putExtra("NotificationText", "some text");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(LoginActivity.this, 2, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + timeToAdd, pendingIntent);
        // end of alarm code
    }
}
