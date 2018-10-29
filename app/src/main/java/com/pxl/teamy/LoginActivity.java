package com.pxl.teamy;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText loginTxtEmail;
    private EditText loginTxtPassword;
    private Button loginBtnLogin;
    private Button loginBtnRegister;

    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        //Buttons en txtinput waarden initialiseren
        loginTxtEmail = (EditText) findViewById(R.id.login_txtEmail);
        loginTxtPassword = (EditText) findViewById(R.id.login_txtPassword);
        loginBtnLogin = (Button) findViewById(R.id.login_btnLogin);
        loginBtnRegister = (Button) findViewById(R.id.login_btnRegister);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);

        loginBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);

            }
        });

        loginBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String loginEmail = loginTxtEmail.getText().toString();
                String loginPass = loginTxtPassword.getText().toString();


                if (!TextUtils.isEmpty(loginEmail) && !TextUtils.isEmpty(loginPass)) {
                    loginProgress.setVisibility(View.VISIBLE);

                    //oncomplete listener om feedback op resultaat te kunnen plakken
                    mAuth.signInWithEmailAndPassword(loginEmail, loginPass).addOnCompleteListener((new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {

                                sendToMain();

                            } else {

                                String errorMessage = task.getException().getMessage();
                                Toast.makeText(LoginActivity.this, "Error : " + errorMessage, Toast.LENGTH_LONG).show();
                            }
                            loginProgress.setVisibility(View.INVISIBLE);
                        }
                    }));
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser != null)
            sendToMain();
    }

    private void sendToMain() {
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}
