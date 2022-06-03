package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailView extends AppCompatActivity {

    /*private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private EditText userName;
    private Button popupJoin, popupCancel;
    private TextView joinEvent;*/

    //DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        TextView nameText = (TextView) findViewById(R.id.name);
        TextView locationText = (TextView) findViewById(R.id.location);
        TextView dateText = (TextView) findViewById(R.id.date);
        TextView timeText = (TextView) findViewById(R.id.time);
        TextView capacityText = (TextView) findViewById(R.id.capacity);
        Button backButton = (Button) findViewById(R.id.backButton);
        Button joinBtn = (Button) findViewById(R.id.joinBtn);

        String name = getIntent().getStringExtra("name");
        String location = getIntent().getStringExtra("location");
        String date = getIntent().getStringExtra("date");
        String time = getIntent().getStringExtra("time");
        String capacity = getIntent().getStringExtra("capacity");
        final String lastActivity = getIntent().getStringExtra("activity");

        nameText.setText(name);
        locationText.setText(location);
        dateText.setText(date);
        timeText.setText(time);
        capacityText.setText(capacity);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i;
                // Can come from either the view event activity or the maps, so sent a
                // string with the intent to indicate where to take the user back to
                if (lastActivity.equals("ViewEvent")) {
                    i = new Intent(DetailView.this, ViewEventActivity.class);
                }
                else {
                    i = new Intent(DetailView.this, MapsActivity.class);
                }
                startActivity(i);
            }
        });

        joinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //createJoinEventDialog();
                Intent intent = new Intent(DetailView.this, JoinEventActivity.class);
                intent.putExtra("name", getEventName());
                intent.putExtra("location", getIntent().getStringExtra("location"));
                intent.putExtra("date", getIntent().getStringExtra("date"));
                intent.putExtra("time", getIntent().getStringExtra("time"));
                intent.putExtra("capacity", getIntent().getStringExtra("capacity"));
                intent.putExtra("activity", getIntent().getStringExtra("activity"));
                startActivity(intent);
            }
        });

    }

    /*public void createJoinEventDialog(){
        dialogBuilder = new AlertDialog.Builder(this);
        final View joinPopupView = getLayoutInflater().inflate(R.layout.popup, null);

        userName = (EditText) joinPopupView.findViewById(R.id.userName);
        popupJoin = (Button) joinPopupView.findViewById(R.id.joinBtn2);
        popupCancel = (Button) joinPopupView.findViewById(R.id.cancelBtn);

        dialogBuilder.setView(joinPopupView);
        dialog = dialogBuilder.create();
        dialog.show();

        popupCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        popupJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String eventName = getEventName();

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
                                if (email.equals(userEmail)){
                                    System.out.println(email);
                                    alreadyJoined = true;
                            }

                        }

                        if (alreadyJoined == false){
                            joinEvent();
                            Toast.makeText(DetailView.this, "Success!",
                                    Toast.LENGTH_SHORT).show();
                            dialog.dismiss();
                        }

                        else{
                            Toast.makeText(DetailView.this, "Already Joined Event!",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }*/

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
                break;
            }
        }

        String newEmailString = "";

        for (int k = 0; k<emailArr.length; k++){
            newEmailString += emailArr[k];
        }

        return newEmailString;
    }

/*    public String getUserName(){
        String userNameString = userName.getText().toString();
        return userNameString;
    }*/

    /*public void joinEvent(){
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("events").child(getEventName()).child("people_attending").child(getUserName()).setValue(getUserEmail());
    }*/
}