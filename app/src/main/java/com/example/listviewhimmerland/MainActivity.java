package com.example.listviewhimmerland;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;

    public FloatingActionButton floatingActionButton;
    
    EditText edt1;
    Button btn1;

    ListView myListView;
    ArrayList<String> myArrayList = new ArrayList<>();
    DatabaseReference mRef;

    // Write a message to the database
    FirebaseDatabase database;
    DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        floatingActionButton = findViewById(R.id.fab);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddPost.class);
                startActivity(intent);
            }
        });


        tabLayout = findViewById(R.id.tablayout);
        viewPager = findViewById(R.id.viewpager);

        tabLayout.setupWithViewPager(viewPager);

        VPAdapter vpAdapter = new VPAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        vpAdapter.addFragment(new Fragment1(), "Forside");
        vpAdapter.addFragment(new Fragment2(), "Begivenheder");
        vpAdapter.addFragment(new Fragment3(), "Byttebørs");
        viewPager.setAdapter(vpAdapter);

         // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://listviewhimmerland2-c34d1-default-rtdb.europe-west1.firebasedatabase.app/");
        DatabaseReference myRef = database.getReference("message");

        final ArrayAdapter<String> myArrayAdapter = new ArrayAdapter<String>(MainActivity.this,android.R.layout.simple_list_item_1,myArrayList);

        // Reference to the ListView
        myListView = (ListView) findViewById(R.id.listview1);
        myListView.setAdapter(myArrayAdapter);
//
//
//
        mRef = database.getReference("messages");

        /*edt1 = (EditText) findViewById(R.id.edittext1);
        btn1 = (Button) findViewById(R.id.button1);

         // Pushes input to database with a new_node
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input =edt1.getText().toString();
                DatabaseReference new_node = mRef.push();
                Map<String,String> mymap = new HashMap<String,String>();
                mymap.put("title","My Title");
                mymap.put("text",input);
                new_node.setValue(mymap);
            }
        });*/

        // Writes messages from database via input
        mRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                GenericTypeIndicator<Map<String, String>> genericTypeIndicator = new GenericTypeIndicator<Map<String, String>>() {};
                Map<String, String> mymap = dataSnapshot.getValue(genericTypeIndicator );
                myArrayList.add((String) mymap.get("text"));
                myArrayAdapter.notifyDataSetChanged();

                
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                myArrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}