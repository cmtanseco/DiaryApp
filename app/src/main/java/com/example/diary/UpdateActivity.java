package com.example.diary;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UpdateActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private Button btnUpdate;
    private EditText etUpdateTitle, etUpdateEntry;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        btnUpdate = findViewById(R.id.btnUpdate);
        etUpdateTitle = findViewById(R.id.etUpdateTitle);
        etUpdateEntry = findViewById(R.id.etUpdateEntry);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        etUpdateTitle.setText(getIntent().getStringExtra("title"));
        etUpdateEntry.setText(getIntent().getStringExtra("text"));

        firebaseAuth = FirebaseAuth.getInstance();
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = etUpdateTitle.getText().toString().trim();
                String entry = etUpdateEntry.getText().toString().trim();
                Log.d("uid", firebaseAuth.getUid());
                Log.d("uid", title);
                Log.d("uid", entry);
                UpdateEntry(firebaseAuth.getUid(), getIntent().getStringExtra("date"), title, entry);
                onBackPressed();
            }
        });
    }



    private void UpdateEntry(String userId, String date, String title, String text) {

        Entry entry = new Entry(userId, date, title, text);

        mDatabase.child("entries").child(userId).child(date).setValue(entry);


    }


}
