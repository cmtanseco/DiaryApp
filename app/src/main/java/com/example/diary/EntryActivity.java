
package com.example.diary;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EntryActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button btnEntry;
    private EditText etTitle, etEntry;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        //instantiate objects and database
        mDatabase = FirebaseDatabase.getInstance().getReference();

        btnEntry = findViewById(R.id.btnEntry);
        etTitle = findViewById(R.id.etTitle);
        etEntry = findViewById(R.id.etEntry);

        etTitle.setText(getIntent().getStringExtra("title"));
        etEntry.setText(getIntent().getStringExtra("text"));

        firebaseAuth = FirebaseAuth.getInstance();
        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etTitle.getText().toString().trim();
                String entry = etEntry.getText().toString().trim();
                Log.d("uid", firebaseAuth.getUid());
                Log.d("uid", findCurrentDate());
                Log.d("uid", title);
                Log.d("uid", entry);
                writeNewEntry(firebaseAuth.getUid(), findCurrentDate(), title, entry);
                onBackPressed();
            }
        });
    }


    // Writes a new entry, each entry is seperated by user and then by date
    private void writeNewEntry(String userId, String date, String title, String text) {

        Entry entry = new Entry(userId, date, title, text);

        mDatabase.child("entries").child(userId).child(date).setValue(entry);


    }

    //finds current date
    private String findCurrentDate(){
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        return date;
    }
}
