package com.neo.licensio.ui.activities.homepage;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomappbar.BottomAppBar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import com.neo.licensio.R;
import com.neo.licensio.data.models.LoggedUser;
import com.neo.licensio.data.models.User;
import com.neo.licensio.network.JwtToken;
import com.neo.licensio.ui.activities.camera.Camera;
import com.neo.licensio.ui.activities.login.LoginActivity;
import com.neo.licensio.ui.activities.profile.ProfileActivity;
import com.neo.licensio.ui.activities.search.HistoryActivity;
import com.neo.licensio.ui.fragments.CollectionFragment;
import com.neo.licensio.ui.fragments.HomeFragment;
import com.neo.licensio.ui.fragments.SearchFragment;
import com.neo.licensio.ui.fragments.SettingsFragment;

public class Homepage extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView bottomNavigationView;
    private NavigationView navigationView;
    private FragmentContainerView fragmentContainerView;
    private FloatingActionButton fab;

    //user that logged in
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Intent intent = getIntent();
        user = intent.getParcelableExtra("User");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        fragmentContainerView = findViewById(R.id.fragmentContainerHome);
        int fragmentContainerViewId = R.id.fragmentContainerHome;
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setBackground(null);   //no background, only icons
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment fragment;
                if(itemId == R.id.homeItem){
                    fragment = new HomeFragment();
                    fragmentManager.beginTransaction().
                            replace(fragmentContainerViewId, fragment).
                            commit();
                    return true;
                } else if (itemId == R.id.searchItem){
                    fragment = new SearchFragment();
                    fragmentManager.beginTransaction().
                            replace(fragmentContainerViewId, fragment).
                            commit();
                    return true;
                } else if (itemId == R.id.collectionItem){
                    fragment = new CollectionFragment();
                    fragmentManager.beginTransaction().
                            replace(fragmentContainerViewId, fragment).
                            commit();
                    return true;
                } else if (itemId == R.id.settingsItem){
                    fragment = new SettingsFragment();
                    fragmentManager.beginTransaction().
                            replace(fragmentContainerViewId, fragment).
                            commit();
                    return true;
                }
                return false;
            }
        });

        //floating action button
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Homepage.this, Camera.class); //create Camera Activity
                Homepage.this.startActivity(intent);  //start activity
            }
        });

        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        ViewGroup parent = (ViewGroup) navigationView.getParent();
        if (parent != null) {
            parent.removeView(navigationView);
        }
        drawerLayout.addView(navigationView);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        View headerView = navigationView.getHeaderView(0);
        TextView profileIconInitials = headerView.findViewById(R.id.profileIcon);
        TextView profileUsername = headerView.findViewById(R.id.drawerUsernameTextView);
        TextView profileEmail = headerView.findViewById(R.id.drawerEmailTextView);

        String username = LoggedUser.getCurrentUser().getUsername();
        profileIconInitials.setText(username.substring(0,2).toUpperCase());
        profileUsername.setText(username);
        profileEmail.setText(LoggedUser.getCurrentUser().getEmailAddress());

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemId = item.getItemId();
                if (itemId == R.id.nav_account) {
                    Intent intent = new Intent(Homepage.this, ProfileActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_history) {
                    Intent intent = new Intent(Homepage.this, HistoryActivity.class);
                    startActivity(intent);
                    return true;
                } else if (itemId == R.id.nav_settings) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    Fragment fragment;
                    fragment = new SettingsFragment();
                    fragmentManager.beginTransaction().
                            replace(fragmentContainerViewId, fragment).
                            commit();
                    return true;
                } else if (itemId == R.id.nav_logout) {
                    Toast.makeText(Homepage.this, "Logged out successfully", Toast.LENGTH_SHORT).show();
                    JwtToken.setJwtToken(null);
                    LoggedUser.setCurrentUser(null);
                    finish();
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        actionBarDrawerToggle.onConfigurationChanged(newConfig);
    }

    public void setUser(User user){
        this.user = user;
    }

    public User getUser(){
        return this.user;
    }

}