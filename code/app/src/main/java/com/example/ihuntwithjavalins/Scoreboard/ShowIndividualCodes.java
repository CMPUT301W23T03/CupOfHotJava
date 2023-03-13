package com.example.ihuntwithjavalins.Scoreboard;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihuntwithjavalins.R;

/**
 * ShowIndividualCodes is responsible for showing the details of each QRCode
 */
public class ShowIndividualCodes extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_main_individual);
    }
}