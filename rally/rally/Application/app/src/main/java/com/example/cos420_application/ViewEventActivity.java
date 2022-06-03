
package com.example.cos420_application;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity {

    private Button backBtn;
    // reference to root of realtime database
    DatabaseReference mDatabase;
    ListView eventList;
    ArrayList<String> eventArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);

        backBtn = findViewById(R.id.backBtn);

        eventList = findViewById(R.id.eventList);
        eventList.setOnItemClickListener(eventClicked);

        initializeListView();

        //returns user to homepage
        backBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openHomePage();
            }

        });
    }

    private void initializeListView() {
        eventArrayList = new ArrayList<String>();
        // array adapter for list view
        final ArrayAdapter<String> eventListAdapter =
                new ArrayAdapter<String>(ViewEventActivity.this,
                        android.R.layout.simple_list_item_1, eventArrayList);

        // reference to root of firebase realtime db
        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");

        //adds adapter to list view
        eventList.setAdapter(eventListAdapter);

        // the ValueEventListener allows the app to access (listen to) the data in the database
        // this instance allows access to the "events" tree in the database
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                // iterates through each data point in the "events" tree
                // stores the parameters of each event, prints them to the listview
                for(DataSnapshot ds : snapshot.getChildren())
                {
                    String name = ds.child("name").getValue(String.class);
                    String date = ds.child("date").getValue(String.class);
                    String location = ds.child("location").getValue(String.class);
                    String time = ds.child("time").getValue(String.class);
                    String capacity = ds.child("capacity").getValue(String.class);
                    long attending = ds.child("people_attending").getChildrenCount();

                    String eventData = "Name: "+ name + "\nLocation: "+ location + "\nDate: "
                            + date + "\nTime: " + time + "\nCapacity: " + capacity.toString()
                            + "\nAttendees: " + attending;
                    eventArrayList.add(eventData);
                    eventListAdapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addListenerForSingleValueEvent(eventListener);

    }

    // transitions to homepage
    public void openHomePage() {
        Intent intent = new Intent(ViewEventActivity.this, Profile.class);
        startActivity(intent);
    }

    private AdapterView.OnItemClickListener eventClicked = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // Get the string that holds the event info
            String event = eventList.getItemAtPosition(position).toString();

            // get the indices of each of the parameters by finding the labels
            int nameStart = event.indexOf("Name: ") + 6;
            int locStart = event.indexOf("Location: ") + 10;
            int dateStart = event.indexOf("Date: ") + 6;
            int timeStart = event.indexOf("Time: ") + 6;
            int capacityStart = event.indexOf("Capacity: ") + 10;
            // get the parameters and store them
            String name = event.substring(nameStart, locStart - 11);
            String location = event.substring(locStart, dateStart - 7);
            String date = event.substring(dateStart, timeStart - 7);
            String time = event.substring(timeStart, capacityStart - 11);
            String capacity = event.substring(capacityStart);

            // go to the event detail activity and pass these values
            Intent i = new Intent(ViewEventActivity.this, DetailView.class);
            i.putExtra("name", name);
            i.putExtra("location", location);
            i.putExtra("date", date);
            i.putExtra("time", time);
            i.putExtra("capacity", capacity);
            i.putExtra("activity", "ViewEvent");
            startActivity(i);
        }
    };
}
