package com.pxl.teamy.ViewActivities;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.Color;
import com.pxl.teamy.R;
import com.pxl.teamy.ViewFragments.AccountFragment;
import com.pxl.teamy.ViewFragments.DetailFragment;
import com.pxl.teamy.ViewFragments.HomeFragment;
import com.pxl.teamy.ViewFragments.NotificationFragment;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;
    private FloatingActionButton btnAddPost;
    private BottomNavigationView mainBottemNav;
    private HomeFragment homeFragment;
    private DetailFragment detailFragment;
    private NotificationFragment notificationFragment;
    private AccountFragment accountFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() != null) {
            mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
            setSupportActionBar(mainToolbar);

            //  getSupportActionBar().setTitle("Teamy");
            mainBottemNav = findViewById(R.id.mainBottomNav);

            //FRAGMENTS
            homeFragment = new HomeFragment();
            notificationFragment = new NotificationFragment();
            accountFragment = new AccountFragment();
            detailFragment = new DetailFragment();
            initializeFragment();


            mainBottemNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment currentFragment = getSupportFragmentManager().findFragmentById(R.id.main_container);

                    switch (item.getItemId()) {
                        case R.id.bottom_action_home:
                            getSupportActionBar().setTitle("Teamy");
                            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.gradient_bg_toolbar));
                            replaceFragment(homeFragment, currentFragment);
                            getSupportActionBar().show();
                            return true;

                        case R.id.bottom_action_myaccount:
                            getSupportActionBar().setTitle("My Account");
                            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xff12c2e9));
                            replaceFragment(accountFragment, currentFragment);
                            return true;

                        case R.id.bottom_action_notification:
                            getSupportActionBar().setTitle("Notifications");
                            getSupportActionBar().setBackgroundDrawable(ContextCompat.getDrawable(getBaseContext(), R.drawable.gradient_bg_toolbar));

                            getSupportActionBar().show();
                            replaceFragment(notificationFragment, currentFragment);
                            return true;

                        case R.id.bottom_action_add:
                            sentToAddEvent();
                            return true;

                        default:
                            return false;
                    }
                }
            });
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {

            sentToLogin();
        } else {
            //Checken of de gebruiker wel foto enz heeft.
            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        if (!task.getResult().exists()) {

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    } else {
                        String errorMessage = task.getException().getMessage();
                        Toast.makeText(MainActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_btnLogout:
                logOut();
                return true;
            case R.id.action_btnSettings:
                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);
                return true;
            default:
                return false;
        }
    }

    private void logOut() {
        mAuth.signOut();
        sentToLogin();
    }


    private void sentToAddEvent() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, NewPostActivity.class));

    }


    private void sentToLogin() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, LoginActivity.class));
        // Zorgt ervoor dat je niet terug kunt met bv Back button
        finish();
    }

    private void initializeFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.main_container, homeFragment);
        fragmentTransaction.add(R.id.main_container, notificationFragment);
        fragmentTransaction.add(R.id.main_container, accountFragment);
        fragmentTransaction.add(R.id.main_container, detailFragment);

        fragmentTransaction.hide(notificationFragment);
        fragmentTransaction.hide(accountFragment);
        fragmentTransaction.hide(detailFragment);
        fragmentTransaction.commit();
    }

    public void replaceFragment(Fragment fragment, Fragment currentFragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment == homeFragment) {
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);

        }

        if (fragment == detailFragment) {
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(homeFragment);

        }

        if (fragment == accountFragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(notificationFragment);
            fragmentTransaction.hide(detailFragment);

        }

        if (fragment == notificationFragment) {
            fragmentTransaction.hide(homeFragment);
            fragmentTransaction.hide(accountFragment);
            fragmentTransaction.hide(detailFragment);
        }
        fragmentTransaction.show(fragment);
        fragmentTransaction.commit();

    }


    @Override
    public void onBackPressed() {
        //Include the code here
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
