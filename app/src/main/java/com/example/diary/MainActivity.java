package com.example.diary;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private TextView nav_header;
    private NavigationView nav_view;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton floatingActionButton;
    private ListView listView;
    private Query query;
    private FirebaseListOptions<Entry> options;
    private ArrayList<String> title, date, text, userid;
    private ArrayList<Entry> entryArrayList;
    private FirebaseListAdapter firebaseListAdapter;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //instantiated objects
        drawerLayout = findViewById(R.id.drawer_layout);

        nav_header = findViewById(R.id.nav_header);
        floatingActionButton = findViewById(R.id.fab);
        listView = findViewById(R.id.lsvPosts);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_black_24dp);

        firebaseAuth = FirebaseAuth.getInstance();

        nav_view = findViewById(R.id.nav_view);
        View headerView = nav_view.getHeaderView(0);
        nav_header = headerView.findViewById(R.id.nav_header);
        nav_header.setText(user.getEmail());

        // handler for my drawer
        // when logout is clicked, the firebaseAuth.signout() is executed
        nav_view.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {

                            case R.id.item_logout: {
                                firebaseAuth.signOut();
                                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                                finish();
                                break;
                            }
                        }
                        //close

                        // set item as selected to persist highlight
                        //menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // Add code here to update the UI based on the item selected
                        // For example, swap UI fragments here

                        return true;
                    }
                });

        // add a new entry
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, EntryActivity.class));
            }
        });


        // arrays made for passing values to be edited
        title = new ArrayList<String>(20);
        date = new ArrayList<String>(30);
        text =  new ArrayList<String>(25);
        userid = new ArrayList<String>(25);
        entryArrayList = new ArrayList<Entry>(25);

        // Get a reference to our posts
        query = FirebaseDatabase.getInstance().getReference().child("entries").child(user.getUid());

        options = new FirebaseListOptions.Builder<Entry>()
                .setLayout(R.layout.list_item)
                .setQuery(query, Entry.class)
                .build();
        firebaseListAdapter = new FirebaseListAdapter(options) {
            @Override
            public void onDataChanged() {
                super.onDataChanged();
                title = null;
                title = new ArrayList<String>(20);
                date = null;
                date = new ArrayList<String>(30);
                text = null;
                text =  new ArrayList<String>(25);
                userid = null;
                userid = new ArrayList<String>(25);

            }
            
            @Override
            protected void populateView(@NonNull View v, @NonNull Object model, int position) {

                TextView Title = v.findViewById(R.id.toptext);
                TextView Date = v.findViewById(R.id.bottomtext);

                Entry ent = (Entry) model;
                entryArrayList.add(ent);
                Title.setText(ent.getTitle());
                Date.setText(ent.getDate());
                title.add(ent.getTitle());
                date.add(ent.getDate());
                text.add(ent.getText());
                userid.add(ent.getUserid());
            }
        };

        listView.setAdapter(firebaseListAdapter);

        // clicking an item in the listview will allow the user to edit a previous entry
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                intent.putExtra("title", title.get(position));
                intent.putExtra("date", date.get(position));
                intent.putExtra("text", text.get(position));
                intent.putExtra("userid", userid.get(position));
                startActivity(intent);
            }
        });


    }


    // opens the drawer when the user presses the hamburger icon
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // prevents the user from signing in while they are already in a user
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
        }
    }


    // listens to any changes made to the database
    @Override
    protected void onStart() {
        super.onStart();
        firebaseListAdapter.startListening();

    }

    // listens to any changes made to the database
    @Override
    protected void onStop() {
        super.onStop();
        firebaseListAdapter.stopListening();
    }



}
