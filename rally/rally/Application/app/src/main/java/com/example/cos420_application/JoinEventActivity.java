package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class JoinEventActivity extends AppCompatActivity {
    DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup);

        Button popupJoin, popupCancel;
        TextView joinEvent;
        popupJoin = (Button) findViewById(R.id.joinBtn2);
        popupCancel = (Button) findViewById(R.id.cancelBtn);

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(JoinEventActivity.this, DetailView.class);
                intent.putExtra("name", getEventName());
                intent.putExtra("location", getIntent().getStringExtra("location"));
                intent.putExtra("date", getIntent().getStringExtra("date"));
                intent.putExtra("time", getIntent().getStringExtra("time"));
                intent.putExtra("capacity", getIntent().getStringExtra("capacity"));
                intent.putExtra("activity", getIntent().getStringExtra("activity"));
                startActivity(intent);
            }
        });


        popupJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName = getEventName();
                System.out.println(eventName);

                //mDatabase = FirebaseDatabase.getInstance().getReference().child("events").child(eventName).child("people_attending");
                mDatabase = FirebaseDatabase.getInstance().getReference().child("events").child(eventName).child("people_attending");
                //mDatabase.child("events").child(eventName).child("people_attending").child(userNameString).setValue(userEmail);
                //dialog.dismiss();
                mDatabase.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        boolean alreadyJoined = false;

                        String userEmail = getUserEmail();
                        String userName = getUserName();

                        for (DataSnapshot ds : snapshot.getChildren()) {
                            String email = ds.getValue().toString();
                            if (email.equals(userEmail)) {
                                System.out.println(email);
                                alreadyJoined = true;
                            }

                        }

                        System.out.println("I am right here!");

                        if (alreadyJoined == false) {
                            joinEvent();
                            Toast.makeText(JoinEventActivity.this, "Success!",
                                    Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(JoinEventActivity.this, DetailView.class);
                            intent.putExtra("name", getEventName());
                            intent.putExtra("location", getIntent().getStringExtra("location"));
                            intent.putExtra("date", getIntent().getStringExtra("date"));
                            intent.putExtra("time", getIntent().getStringExtra("time"));
                            intent.putExtra("capacity", getIntent().getStringExtra("capacity"));
                            intent.putExtra("activity", getIntent().getStringExtra("activity"));
                            startActivity(intent);
                        }

                        if (alreadyJoined){
                            Toast.makeText(JoinEventActivity.this, "Already Joined Event!",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

            public String getEventName(){
                String eventName = getIntent().getStringExtra("name");
                return eventName;
            }

            public String getUserEmail(){
                FirebaseUser user;
                user = FirebaseAuth.getInstance().getCurrentUser();
                String userEmail = user.getEmail();

                String[] emailArr = new String[userEmail.length()];

                for (int i = 0; i<userEmail.length(); i++){
                    emailArr[i] = userEmail.substring(i, i+1);
                }

                for (int j=emailArr.length-1; j > -1; j--){
                    if (emailArr[j].equals(".")){
                        emailArr[j] = ",";
                    }
                }

                String newEmailString = "";

                for (int k = 0; k<emailArr.length; k++){
                    newEmailString += emailArr[k];
                }

                return newEmailString;
            }

            public String getUserName(){
                EditText userName;
                userName = (EditText) findViewById(R.id.userName);
                String userNameString = userName.getText().toString();
                return userNameString;
            }

            public void joinEvent(){
                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.child("events").child(getEventName()).child("people_attending").child(getUserName()).setValue(getUserEmail());
            }
}