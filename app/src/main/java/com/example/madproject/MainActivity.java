package com.example.madproject;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.madproject.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Use ViewBinding for accessing views
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Get NavHostFragment and NavController
        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.FCVMain);
        NavController navController = navHostFragment.getNavController();

        // Configure AppBar to sync with Navigation
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.discussionForum,
                R.id.chatListFragment,
                R.id.precautionFragment)
                .build();

        // Connect BottomNavigationView with NavController
        NavigationUI.setupWithNavController(binding.BottomNavView, navController);
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
            NavController navController = Navigation.findNavController(this, R.id.FCVMain);
            navController.navigate(item.getItemId());
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return super.onOptionsItemSelected(item);
        }
    }
}
