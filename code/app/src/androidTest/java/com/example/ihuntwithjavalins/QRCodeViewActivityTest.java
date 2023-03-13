package com.example.ihuntwithjavalins;

import android.app.Activity;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.ihuntwithjavalins.QRCode.QRCodeViewActivity;
import com.robotium.solo.Solo;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class QRCodeViewActivityTest {

    private Solo solo;

    @Rule
    public ActivityTestRule rule = new ActivityTestRule(QRCodeViewActivity.class, true, true);

    @Before
    public void setUp() throws Exception{
        solo = new Solo(InstrumentationRegistry.getInstrumentation(), rule.getActivity());
    }

    @Test
    public void start() throws Exception{
        Activity activity = rule.getActivity();
    }

    @Test
    public void deleteButton() {
        solo.assertCurrentActivity("Wrong Activity", QRCodeViewActivity.class);
        solo.clickOnButton("Remove this Code");
    }
}
