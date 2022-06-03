package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

//    private Button myEventsBtn;
    private String email;
    private FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button logout = (Button) findViewById(R.id.buttonLogout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });

        Button viewEventBtn = (Button) findViewById(R.id.viewEventBtn);
        viewEventBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                openViewEvent();
            }
        });

//        myEventsBtn = findViewById(R.id.myEventsbtn);
//        myEventsBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openUserEvents();
//            }
//        });

        // accesses logged in user, users their email as the header for their profile
        user = FirebaseAuth.getInstance().getCurrentUser();
        email = user.getEmail();

        TextView title = (TextView) findViewById(R.id.profileTitle);
        title.setText("Profile \n"+ email);



        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.map:
                        Intent intent0 = new Intent(Profile.this, MapsActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.create:
                        Intent intent1 = new Intent(Profile.this, CreateEventActivity.class);
                        startActivity(intent1);
                        break;

                    case R.id.profileNavView:

                        break;
                }

                return false;
            }
        });
    }

    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(Profile.this, LoginActivity.class);
        startActivity(intent);
    }

    public void openViewEvent() {
        Intent intent = new Intent(Profile.this, ViewEventActivity.class);
        startActivity(intent);
    }

//    public void openUserEvents(){
//        Intent intent = new Intent(Profile.this, UserEventsActivity.class);
//        startActivity(intent);
//    }
}