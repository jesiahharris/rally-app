package com.example.cos420_application;

import androidx.test.rule.ActivityTestRule;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static org.junit.Assert.*;

public class MapsActivityTest {

    @Rule
    public ActivityTestRule<MapsActivity> mActivityTestRule = new ActivityTestRule<MapsActivity>(MapsActivity.class);

    private MapsActivity mActivity = null;



    @Before
    public void setUp() throws Exception {
        mActivity = mActivityTestRule.getActivity();
    }


    @Test
    public void testDisplay(){
        assertNotNull(mActivity.findViewById(R.id.mapFrag));

    }


    @After
    public void tearDown() throws Exception {
        mActivity = null;
    }


}