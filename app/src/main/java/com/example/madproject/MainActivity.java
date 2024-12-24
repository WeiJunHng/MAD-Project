package com.example.madproject;

import android.os.Bundle;
import android.widget.ListView;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_pag);
/*
        // ListView reference
        ListView listView = findViewById(R.id.AccountSettingsList);

        // List items
        String[] settingsItems = {
                "Privacy & Security",
                "Get Support",
                "Allow Notification",
                "Accessibility",
                "Sign Out",
                "Delete Account"
        };

        // Set custom adapter
        CustomAdapter adapter = new CustomAdapter(settingsItems);
        listView.setAdapter(adapter);

 */
    }
}
