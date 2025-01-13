package com.example.madproject;

import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;

import com.example.madproject.data.db.AppDatabase;
import com.example.madproject.data.db.FirestoreManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.madproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private AppDatabase database;
    private FirestoreManager firestoreManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use ViewBinding for accessing views
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        database = AppDatabase.getDatabase(getApplicationContext());
        firestoreManager = new FirestoreManager(database);

        firestoreManager.clearUserTables();
        firestoreManager.syncUserTable();

        NavHostFragment host = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.FCVMain);
        NavController navController = host.getNavController();
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();

//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.BottomNavView, navController);
        binding.BottomNavView.setOnItemSelectedListener(item -> {
            NavOptions navOptions = new NavOptions.Builder()
                    .setPopUpTo(R.id.nav_graph, true)  // Clear back stack when navigating
                    .build();
            navController.navigate(item.getItemId(), null, navOptions);

//            switch (item.getItemId()) {
//                case R.id.navigation_home:
//                    navController.navigate(R.id.homeFragment, null, navOptions);
//                    break;
//                case R.id.navigation_profile:
//                    navController.navigate(R.id.profileFragment, null, navOptions);
//                    break;
//                case R.id.navigation_settings:
//                    navController.navigate(R.id.settingsFragment, null, navOptions);
//                    break;
//            }
            return true;
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        // Enable navigating up
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.FCVMain);
        NavController navController = navHostFragment.getNavController();
        return navController.navigateUp() || super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        // Handle menu item selections for navigation
        try {
            Navigation.findNavController(this, R.id.FCVMain).navigate(item.getItemId());
            return true;
        } catch (Exception ex) {
            return super.onOptionsItemSelected(item);
        }
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.FCVMain, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}