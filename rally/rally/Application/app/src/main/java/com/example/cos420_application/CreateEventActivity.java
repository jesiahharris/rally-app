
package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.fonts.FontFamily;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.view.View.OnClickListener;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;


public class CreateEventActivity extends AppCompatActivity{

    private EditText eventName;
    private EditText location;

    private EditText capacity;
    private DatePickerDialog datePickerDialog;
    private Button dateBtn;
    private Button timeBtn;
    private Button publishBtn;
    private Button homeBtn;
    int hour, minute;

    // reference to root of realtime database
    DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();


    public CreateEventActivity(){

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);
        initDatePicker();

        // event parameters passed in by user through edit texts
        eventName = findViewById(R.id.EventName);
        location = findViewById(R.id.Location);
        capacity = findViewById(R.id.Capacity);


        publishBtn = findViewById(R.id.publishEventBtn);
        dateBtn = findViewById(R.id.datePickerBtn);
        timeBtn = findViewById(R.id.timePickerBtn);
        // Nick commented this out since it was giving an error
        //homeBtn = findViewById(R.id.homePageBtn);

        // return to home page
        // I learned today 4/11 methods set to nonexistent assets cause a crash -Jesiah
//        homeBtn.setOnClickListener(new View.OnClickListener(){
//            public void onClick(View v){
//                openHomePage();
//            }
//        });

        //set text of dateBtn to today's date
        dateBtn.setText("Select Date");

        // method written by Jesiah
        // creates event in database with data provided by user on click of publish event button
        publishBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                try {
                    createEvent(eventName.getText().toString(), location.getText().toString(),
                            dateBtn.getText().toString(), timeBtn.getText().toString(),
                            capacity.getText().toString() + "");

                } catch (IOException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(CreateEventActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.map:
                        Intent intent0 = new Intent(CreateEventActivity.this, MapsActivity.class);
                        startActivity(intent0);
                        break;

                    case R.id.create:

                        break;

                    case R.id.profileNavView:
                        Intent intent2 = new Intent(CreateEventActivity.this, Profile.class);
                        startActivity(intent2);
                        break;
                }

                return false;
            }
        });
    }

    // method written by Jesiah
    // formats today's date into string to display in dateBtn
    private String getTodaysDate() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        month = month + 1;
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return makeDateString(day, month, year);
    }

    // method written by Jesiah
    // initializes date picker
    private void initDatePicker()
    {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener()
        {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1; // month starts at 0 by default, add 1
                String date = makeDateString(day, month, year);
                dateBtn.setText(date);
            }
        };

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        datePickerDialog = new DatePickerDialog(this, dateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
    }

    // method written by Jesiah
    // makes a formatted date string
    private String makeDateString(int day, int month, int year) {
        return getMonthFormat(month) + " " + day + " " + year;

    }

    // method written by Jesiah
    // makes a formatted AM/PM time string
    private String makeTimeString(int hour, int minute)
    {
        if(hour > 12)
        {
            if(minute < 10)
            {
                return (hour - 12) + ":0" + minute + ""+ "PM";
            }
            else
                return (hour - 12) + ":" + minute + ""+ "PM";

        }
        else if(hour < 12)
        {
            if(minute < 10)
            {
                return (hour) + ":0" + minute + ""+ "AM";
            }
        }
            return hour + "" + ":" + minute + "" + "AM";
    }


    // method written by Jesiah
    // converts numerical month to string month, ex) 1 = JAN
    public String getMonthFormat(int month) {
        if(month == 1)
            return "JAN";
        if(month == 2)
            return "FEB";
        if(month == 3)
            return "MAR";
        if(month == 4)
            return "APR";
        if(month == 5)
            return "MAY";
        if(month == 6)
            return "JUN";
        if(month == 7)
            return "JUL";
        if(month == 8)
            return "AUG";
        if(month == 9)
            return "SEP";
        if(month == 10)
            return "OCT";
        if(month == 11)
            return "NOV";
        if(month == 12)
            return "DEC";

        //default case
        return "JAN";
    }

    //method written by Jesiah
    //displays time picker
    public void openTimePicker(View view)
    {
        TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int selectedHour, int selectedMinute)
            {
                hour = selectedHour;
                minute = selectedMinute;
                timeBtn.setText(makeTimeString(hour, minute));
            }
        };

        //sets style to scrolling, theme holo light
        int style = AlertDialog.THEME_HOLO_LIGHT;

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, style, onTimeSetListener,
                hour, minute, false);
        timePickerDialog.setTitle("Select Time");
        timePickerDialog.show();
    }

    // method written by Jesiah
    // method to add event to realtime database
    public void createEvent(String name, String location, String date, String time, String capacity) throws IOException {

        Event event = new Event(name, location, date, time, capacity);

        // adds an "event" node in database, adds the event object
        // as a child of the event node
        mDatabase.child("events").child(name).setValue(event);
        FirebaseUser user;
        user = FirebaseAuth.getInstance().getCurrentUser();
        String userEmail = user.getEmail();
        userEmail = getUserEmail(userEmail);
        mDatabase.child("events").child(name).child("people_attending").child("Owner").setValue(userEmail).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(CreateEventActivity.this, "Success!",
                        Toast.LENGTH_SHORT).show();
            }
        })
        .addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(CreateEventActivity.this, "Please try again",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    //method written by Jesiah
    // displays date picker
    public void openDatePicker(View view)
    {
        datePickerDialog.show();
    }

    // Back to home page
    public void openHomePage(){
        Intent intent = new Intent(CreateEventActivity.this, HomePageActivity.class);
        startActivity(intent);
    }

    public String getUserEmail(String userEmail){

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

}
