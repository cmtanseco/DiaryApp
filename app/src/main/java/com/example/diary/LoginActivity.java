package com.example.diary;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);

        firebaseAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);



        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        user = null;

        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Authenticate(etUsername.getText().toString(), etPassword.getText().toString());
            }
        });
    }



    private void Authenticate(String username, String userpassword) {

        progressDialog.setMessage("Checking the spaghetti code...");
        progressDialog.show();


        firebaseAuth.signInWithEmailAndPassword(username, userpassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }else{
                    progressDialog.dismiss();
                    Toast.makeText(LoginActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }
}
