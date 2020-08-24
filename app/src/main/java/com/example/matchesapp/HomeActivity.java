package com.example.matchesapp;

import android.os.Bundle;

import com.example.matchesapp.Fragment.AllMatchesFragment;
import com.example.matchesapp.Fragment.SavedMatchesFragment;

import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;

import androidx.drawerlayout.widget.DrawerLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class HomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    NavigationView nav_view;
    androidx.appcompat.app.ActionBarDrawerToggle t;
    private NavigationView navigationView;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        navigation();
    }

    private void init() {

        drawerLayout = findViewById(R.id.drawerLayout);
        toolbar = findViewById(R.id.toolbar);
        nav_view = findViewById(R.id.nav_view);
    }

    public void navigation() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        nav_view.setItemIconTintList(null);
        toggle.syncState();

        nav_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                switch (id) {

                    case R.id.nav_all_matches:
                        toolbar.setTitle("All Matches");
//                        startActivity(new Intent(HomeActivity.this, HomeActivity.class));
                        loadFragment(new AllMatchesFragment());
                        drawerLayout.closeDrawers();
                        break;

                    case R.id.nav_saved_matches:
                        toolbar.setTitle("Saved Matches");

                        //                        startActivity(new Intent(HomeActivity.this, ProfileActivity.class));
                        loadFragment(new SavedMatchesFragment());
                        drawerLayout.closeDrawers();
                        break;


                }

                return true;
            }
        });
    }


    public void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.frameLayout, fragment);
        transaction.commit();
    }

}
