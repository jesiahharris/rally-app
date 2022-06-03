package com.example.cos420_application;

import android.app.Activity;
import android.content.Context;
import android.os.SystemClock;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.annotation.ContentView;
import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.rule.ActivityTestRule;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerActions.open;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.espresso.Espresso.pressBack;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.*;

public class LoginActivityTest {

    @Rule
    public ActivityTestRule<LoginActivity> lActivityTestRule = new ActivityTestRule<LoginActivity>(LoginActivity.class);

    private LoginActivity lActivity = null;



    @Before
    public void setUp() throws Exception {
        lActivity = lActivityTestRule.getActivity();
    }


    @Test
    public void testLogin(){
        /*onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.inputEmailRegister)).perform(typeText("thismail@email.com"));
        onView(withId(R.id.inputPasswordRegister)).perform(typeText("password"));
        onView(withId(R.id.inputPasswordConfirmRegister)).perform(typeText("password"));
        Espresso.pressBack();
        onView(withId(R.id.buttonSubmitRegister)).perform(click());*/


        onView(withId(R.id.inputEmail)).perform(typeText("thisEmail@email.com"));
        onView(withId(R.id.inputPassword)).perform(typeText("password"));
        onView(withId(R.id.buttonLogin)).perform(click());

        SystemClock.sleep(6000);
        //onView(withId(R.id.bottomNavView_Bar)).perform(NavigationViewActions.navigateTo(R.id.profileNavView));
        onView(withId(R.id.temporaryButton)).perform(click());
        onView(withId(R.id.profileTitle)).check(matches(withText("Profile \nthisemail@email.com")));

    }

    @Test
    public void testRegister(){
        onView(withId(R.id.buttonRegister)).perform(click());
        onView(withId(R.id.inputEmailRegister)).perform(typeText("thmai@email.com"));
        onView(withId(R.id.inputPasswordRegister)).perform(typeText("password"));
        onView(withId(R.id.inputPasswordConfirmRegister)).perform(typeText("password"));
        Espresso.pressBack();
        onView(withId(R.id.buttonSubmitRegister)).perform(click());
    }

    @Test

    public void testJoinEvent(){
        onView(withId(R.id.inputEmail)).perform(typeText("thisEmail@email.com"));
        onView(withId(R.id.inputPassword)).perform(typeText("password"));
        onView(withId(R.id.buttonLogin)).perform(click());


        SystemClock.sleep(6000);
        //onView(withId(R.id.bottomNavView_Bar)).perform(NavigationViewActions.navigateTo(R.id.profileNavView));
        onView(withId(R.id.temporaryButton)).perform(click());

        onView(withId(R.id.viewEventBtn)).perform(click());

        onData(anything()).inAdapterView(withId(R.id.eventList)).atPosition(1).perform(click());

        onView(withId(R.id.joinBtn)).perform(click());
        onView(withId(R.id.userName)).perform(typeText("New Guy"));
        Espresso.pressBack();
        onView(withId(R.id.joinBtn2)).perform(click());
    }

    /*public static void setDate(int datePickerLaunchViewId, int year, int monthOfYear, int dayOfMonth) {
        onView(withParent(withId(buttonContainer)), withId(datePickerLaunchViewId)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(year, monthOfYear, dayOfMonth));
        onView(withId(android.R.id.button1)).perform(click());
    }*/

    @Test
    public void testCreateEvent(){
        onView(withId(R.id.inputEmail)).perform(typeText("thisEmail@email.com"));
        onView(withId(R.id.inputPassword)).perform(typeText("password"));
        onView(withId(R.id.buttonLogin)).perform(click());


        SystemClock.sleep(6000);
        onView(withId(R.id.bottomNavView_Bar)).perform(click());

        onView(withId(R.id.EventName)).perform(typeText("Test Event"));

        onView(withId(R.id.Location)).perform(typeText("Big Island, Hawaii"));

        onView(withId(R.id.datePickerBtn)).perform(click());
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2021, 5, 10));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.timePickerBtn)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(10, 0));
        onView(withId(android.R.id.button1)).perform(click());

        onView(withId(R.id.Capacity)).perform(typeText("50"));

        Espresso.pressBack();
        onView(withId(R.id.publishEventBtn)).perform(click());

    }


    @After
    public void tearDown() throws Exception {
        lActivity = null;
    }
}