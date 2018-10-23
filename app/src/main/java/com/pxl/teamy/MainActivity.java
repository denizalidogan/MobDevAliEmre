package com.pxl.teamy;

import android.content.Intent;
<<<<<<< HEAD
import android.os.Bundle;
<<<<<<< HEAD
import android.support.design.widget.FloatingActionButton;
=======
>>>>>>> parent of fc48c02... Revert "Layout changes"
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
<<<<<<< HEAD
import android.view.View;
=======
>>>>>>> parent of fc48c02... Revert "Layout changes"
=======
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
>>>>>>> parent of b270a6b... Layout changes

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

<<<<<<< HEAD
<<<<<<< HEAD
    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FloatingActionButton btnAddPost;
=======

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;

>>>>>>> parent of fc48c02... Revert "Layout changes"

=======
>>>>>>> parent of b270a6b... Layout changes
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Teamy");

        btnAddPost = findViewById(R.id.btnAddPost);

<<<<<<< HEAD
        btnAddPost.setOnClickListener(new View.OnClickListener() {

<<<<<<< HEAD
            public void onClick(View v) {
=======
        mAuth = FirebaseAuth.getInstance();

        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Teamy");
>>>>>>> parent of fc48c02... Revert "Layout changes"
=======
>>>>>>> parent of b270a6b... Layout changes

                Intent newPostIntent = new Intent(MainActivity.this    , NewPostActivity.class);
                startActivity(newPostIntent);
            }

        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
<<<<<<< HEAD
        if (currentUser == null) {
            sentToLogin();
<<<<<<< HEAD
=======


>>>>>>> parent of fc48c02... Revert "Layout changes"
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


<<<<<<< HEAD
=======

>>>>>>> parent of fc48c02... Revert "Layout changes"
            case R.id.action_btnSettings:

                Intent settingsIntent = new Intent(MainActivity.this, SetupActivity.class);
                startActivity(settingsIntent);

                return true;

<<<<<<< HEAD
=======

>>>>>>> parent of fc48c02... Revert "Layout changes"
            default:
                return false;

        }

    }

    private void logOut() {
<<<<<<< HEAD
        mAuth.signOut();
        sentToLogin();
    }


    private void sentToLogin() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, LoginActivity.class));
        // Zorgt ervoor dat je niet terug kunt met bv Back button
        finish();
    }

=======
=======
        if(currentUser == null){
            // Activiteit wordt aangemaakt
            startActivity(new Intent( this, LoginActivity.class));
            // Zorgt ervoor dat je niet terug kunt met bv Back button
            finish();

        }

>>>>>>> parent of b270a6b... Layout changes

    }
<<<<<<< HEAD


    private void sentToLogin() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, LoginActivity.class));
        // Zorgt ervoor dat je niet terug kunt met bv Back button
        finish();
    }

>>>>>>> parent of fc48c02... Revert "Layout changes"
=======
>>>>>>> parent of b270a6b... Layout changes
}
