package com.hardboiled.csgoteamcreator;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Created by wesley on 12/8/17.
 */

public class NotificationView extends Activity {
    // Declare Variable
    String title;
    String text;
    TextView txtTitle;
    TextView txtText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notificationview);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);

        // Retrive the data from MainActivity.java
        Intent i = getIntent();

        title = i.getStringExtra("title");
        text = i.getStringExtra("text");

        // Locate the TextView
        txtTitle = (TextView) findViewById(R.id.title);
        txtText = (TextView) findViewById(R.id.text);

        // Set the data into TextView
        txtTitle.setText(title);
        txtText.setText(text);
    }
}