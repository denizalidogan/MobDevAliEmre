package com.pxl.teamy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainActivity extends AppCompatActivity {

    private Toolbar mainToolbar;
    private FirebaseAuth mAuth;
    private FirebaseFirestore firebaseFirestore;
    private String current_user_id;

    private FloatingActionButton btnAddPost;

    private BottomNavigationView mainBottemNav;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();




        mainToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(mainToolbar);

        getSupportActionBar().setTitle("Teamy");


        mainBottemNav = findViewById(R.id.mainBottemNav);

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
        } else{
            //Checken of de gebruiker wel foto enz heeft.

            current_user_id = mAuth.getCurrentUser().getUid();

            firebaseFirestore.collection("Users").document(current_user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()){

                        if(!task.getResult().exists()){

                            Intent setupIntent = new Intent(MainActivity.this, SetupActivity.class);
                            startActivity(setupIntent);
                            finish();
                        }
                    }
                        else{
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


    private void sentToLogin() {
        // Activiteit wordt aangemaakt
        startActivity(new Intent(this, LoginActivity.class));
        // Zorgt ervoor dat je niet terug kunt met bv Back button
        finish();
    }

}
