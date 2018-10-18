package com.pxl.teamy;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton btnAddPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Teamy");

        btnAddPost = findViewById(R.id.btnAddPost);

        btnAddPost.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent newPostIntent = new Intent(MainActivity.this    , NewPostActivity.class);
                startActivity(newPostIntent);
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            sentToLogin();
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


    private void sentToLogin() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, LoginActivity.class));
        // Zorgt ervoor dat je niet terug kunt met bv Back button
        finish();
    }

}
