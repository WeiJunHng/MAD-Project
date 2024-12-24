package com.example.madproject;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Load discussion forum fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.FLFragmentContainer, new DiscussionForum())
                    .commit();
        }

        BottomNavigationView bottomNavigationView = findViewById(R.id.BNVNavigationBar);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            Fragment selectedFragment = null;

            switch (item.getItemId()) {
//                case R.id.nav_news:
//                    selectedFragment = new NewsFragment(); // Navigate to NewsFragment
//                    break;

                case R.id.nav_precaution:
                    selectedFragment = new Precaution(); // Navigate to PrecautionFragment
                    break;

//                case R.id.nav_home:
//                    selectedFragment = new HomeFragment(); // Navigate to HomeFragment
//                    break;

                case R.id.nav_posts:
                    selectedFragment = new DiscussionForum(); // Navigate to DiscussionForum
                    break;

//                case R.id.nav_profile:
//                    selectedFragment = new ProfileFragment(); // Navigate to ProfileFragment
//                    break;
            }

            if (selectedFragment != null) {
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.FLFragmentContainer, selectedFragment)
                        .commit();
            }

            return true;
        });

    }
}