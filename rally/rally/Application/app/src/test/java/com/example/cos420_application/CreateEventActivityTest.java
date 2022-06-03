package com.example.cos420_application;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.core.Context;

import org.junit.Test;

import static org.junit.Assert.*;

public class CreateEventActivityTest {

    @Test
    public void testGetMonthFormat() {
        CreateEventActivity createEventActivity = new CreateEventActivity();

        String expected = "DEC";
        String output = createEventActivity.getMonthFormat(12);

        assertEquals(expected, output);

        expected = "JAN";
        output = createEventActivity.getMonthFormat(22);

        assertEquals(expected, output);
    }

    @Test
    public void testGetUserEmail() {
        CreateEventActivity createEventActivity = new CreateEventActivity();

        String expected = "peterm0017@hotmail,com";
        String output = createEventActivity.getUserEmail("peterm0017@hotmail.com");

        assertEquals(expected, output);

        expected = "peter.martin@maine,edu";
        output = createEventActivity.getUserEmail("peter.martin@maine.edu");

        assertEquals(expected, output);
    }
}