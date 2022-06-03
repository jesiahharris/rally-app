// this page will first display events to the user that the user has created
// later it will display in one text box the user's hosted events, and in another text box
// the events the user has joined

package com.example.cos420_application;

import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserEventsActivity extends AppCompatActivity {
    // reference to root of realtime database
    DatabaseReference mDatabase;
    // need 2 list views, one for hosted events (owner) one for shared events (shared_with)
    ListView ownerList;
    ListView sharedList;
    ArrayList<String> ownerArrayList;
    ArrayList<String> sharedArrayList;
    private String email;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_events);

        // assign buttons and listviews here


        // accesses logged in user, stores their email
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();
    }



    private void initializeOwnerListView() {
        ownerArrayList = new ArrayList<String>();
        // array adapter for list view
        final ArrayAdapter<String> ownerListAdapter =
                new ArrayAdapter<String>(UserEventsActivity.this,
                        android.R.layout.simple_list_item_1, ownerArrayList);

        // reference to root of firebase realtime db
        mDatabase = FirebaseDatabase.getInstance().getReference().child("events");

        //adds adapter to list view
        ownerList.setAdapter(ownerListAdapter);

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

                        // arrayList for people_attending
                        ArrayList<String> people_attending = new ArrayList<String>();

                        // add each child of people_attending to arrayList
                        for(DataSnapshot child : ds.child("people_attending").getChildren())
                        {
                            people_attending.add(child.getValue(String.class));
                        }

                        String eventData = "Name: " + name + "\nLocation: " + location + "\nDate: "
                                + date + "\nTime: " + time + "\nCapacity: " + capacity.toString()
                                + "\nAttendees: " + attending;
                    Log.d("ArrayList", people_attending.toString());
                    }


                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }

        };
        mDatabase.addListenerForSingleValueEvent(eventListener);

    }
}
