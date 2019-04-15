package com.example.diary;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import org.w3c.dom.Text;

public class RegisterActivity extends AppCompatActivity {

    private EditText etRegEmail, etRegUsername, etRegPassword;
    private Button btnRegister;
    private TextView txvSignIn;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etRegEmail = findViewById(R.id.etRegEmail);
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnRegister = findViewById(R.id.btnRegister);
        txvSignIn = findViewById(R.id.txvSignIn);

        firebaseAuth = FirebaseAuth.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    String user_email = etRegEmail.getText().toString().trim();
                    String user_password = etRegPassword.getText().toString().trim();

                    firebaseAuth.createUserWithEmailAndPassword(user_email, user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                Toast.makeText(RegisterActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            }
                            else
                            {
                                Toast.makeText(RegisterActivity.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        txvSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
            }
        });
    }

    private Boolean validate() {
        Boolean result = false;

        String username = etRegUsername.getText().toString();
        String password = etRegPassword.getText().toString();
        String email = etRegEmail.getText().toString();

        if(username.isEmpty() || password.isEmpty() || email.isEmpty()) {
            Toast.makeText(this, "Please complete all the fields", Toast.LENGTH_SHORT).show();
        }
        else
        {
            result = true;
        }

        return result;
    }


}
