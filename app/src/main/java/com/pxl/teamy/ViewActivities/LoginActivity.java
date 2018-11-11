package com.pxl.teamy.ViewActivities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.pxl.teamy.R;

public class LoginActivity extends AppCompatActivity implements TextWatcher, CompoundButton.OnCheckedChangeListener {
    private EditText loginTxtEmail;
    private EditText loginTxtPassword;
    private Button loginBtnLogin;
    private Button loginBtnRegister;
    private FirebaseAuth mAuth;
    private ProgressBar loginProgress;
    private CheckBox cb_rememberLogin;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    private static final String PREF_NAME = "prefs";
    private static final String KEY_REMEMBER = "remember";
    private static final String KEY_USERNAME = "username";
    private static final String KEY_PASS = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        loginTxtEmail = (EditText) findViewById(R.id.login_txtEmail);
        loginTxtPassword = (EditText) findViewById(R.id.login_txtPassword);
        loginBtnLogin = (Button) findViewById(R.id.login_btnLogin);
        loginBtnRegister = (Button) findViewById(R.id.login_btnRegister);
        loginProgress = (ProgressBar) findViewById(R.id.login_progress);
        cb_rememberLogin = (CheckBox) findViewById(R.id.cb_rememberLogin);
        sharedPreferences = getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        if (sharedPreferences.getBoolean(KEY_REMEMBER, false))
            cb_rememberLogin.setChecked(true);
        else
            cb_rememberLogin.setChecked(false);

        loginTxtEmail.setText(sharedPreferences.getString(KEY_USERNAME, ""));
        loginTxtPassword.setText(sharedPreferences.getString(KEY_PASS, ""));
        loginTxtEmail.addTextChangedListener(this);
        loginTxtPassword.addTextChangedListener(this);
        cb_rememberLogin.setOnCheckedChangeListener(this);

        //Buttons en txtinput waarden initialiseren
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
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        managePrefs();
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        managePrefs();
    }


    private void managePrefs() {
        if (cb_rememberLogin.isChecked()) {
            editor.putString(KEY_USERNAME, loginTxtEmail.getText().toString().trim());
            editor.putString(KEY_PASS, loginTxtPassword.getText().toString().trim());
            editor.putBoolean(KEY_REMEMBER, true);
            editor.commit();
        } else {
            editor.putBoolean(KEY_REMEMBER, false);
            editor.remove(KEY_PASS);//editor.putString(KEY_PASS,"");
            editor.remove(KEY_USERNAME);//editor.putString(KEY_USERNAME, "");
            editor.commit();
        }
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
