package com.example.cos420_application;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.view.View.OnClickListener;

import com.google.firebase.auth.FirebaseAuth;

public class HomePageActivity extends AppCompatActivity {

    private Button viewEventBtn;
    private Button createEventBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Button logout = (Button) findViewById(R.id.buttonLogout);
        EditText searchBar = (EditText) findViewById(R.id.searchBar);

        Button mapActivityBtn = (Button) findViewById(R.id.mapActivityBtn);
        mapActivityBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(HomePageActivity.this, MapsActivity.class);
                startActivity(startIntent);
            }
        });

        createEventBtn = findViewById(R.id.createEventBtn);
        createEventBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openCreateEvent();
            }


        });
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        viewEventBtn = findViewById(R.id.viewEventBtn);
        viewEventBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openViewEvent();
            }
        });
    }

    // Function written by Nick
    // For now, this transitions back to login activity
    // eventually it will handle the logic of forgetting the data of the logged in user
    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomePageActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // method written by Jesiah
    //Transitions from this activity to createEvent activity
    public void openCreateEvent() {
        Intent intent = new Intent(HomePageActivity.this, CreateEventActivity.class);
        startActivity(intent);
    }

    public void openViewEvent() {
        Intent intent = new Intent(HomePageActivity.this, ViewEventActivity.class);
        startActivity(intent);
    }
}