package com.example.cos420_application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText password;
    private EditText confirmPassword;
    private EditText email;
    private Button cancel;
    private Button submit;

    private TextView emailError;
    private TextView passwordError;

    private FirebaseAuth auth;
    private static final String TAG = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        password = (EditText)findViewById(R.id.inputPasswordRegister);
        confirmPassword = (EditText)findViewById(R.id.inputPasswordConfirmRegister);
        email = (EditText)findViewById(R.id.inputEmailRegister);

        auth = FirebaseAuth.getInstance();

        cancel = (Button)findViewById(R.id.buttonCancelRegister);
        submit = (Button)findViewById(R.id.buttonSubmitRegister);

        emailError = (TextView)findViewById(R.id.textEmailError);
        passwordError = (TextView)findViewById(R.id.textPasswordError);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit(password.getText().toString(),
                        email.getText().toString(),confirmPassword.getText().toString());
            }
        });
    }

    // Written by Nick
    // This returns to the login screen
    private void cancel() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Written by Nick
    // This function checks that the input is valid and adds the user to the database
    private void submit(String password, String email, String confirmPassword) {
        Boolean isValid = true;
        passwordError.setVisibility(View.INVISIBLE);
        passwordError.setVisibility(View.INVISIBLE);


        if (password.equals("") || confirmPassword.equals("")) {
            isValid = false;
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Password is required");
        }
        else if (!password.equals(confirmPassword)) {
            isValid = false;
            passwordError.setVisibility(View.VISIBLE);
            passwordError.setText("Please confirm password");
        }

        // TODO Add logic for email format

        if (isValid) {
            auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = auth.getCurrentUser();
                                RegisterActivity.this.startActivity(
                                        new Intent(RegisterActivity.this, MapsActivity.class));
                            } else {
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                Toast.makeText(RegisterActivity.this, "Authentication failed.",
                                        Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }


    }
}