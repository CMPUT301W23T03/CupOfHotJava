package com.example.ihuntwithjavalins;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihuntwithjavalins.Player.Player;
import com.example.ihuntwithjavalins.Player.PlayerDB;
import com.example.ihuntwithjavalins.common.DBConnection;
import com.google.firebase.firestore.FirebaseFirestore;

public class SignupActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText phoneEditText;
    private EditText emailEditText;
    private EditText regionEditText;
    private Button signupButton;
    private FirebaseFirestore db;
    private DBConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Get references to EditText views in layout
        usernameEditText = findViewById(R.id.signup_username);
        phoneEditText = findViewById(R.id.signup_phone);
        emailEditText = findViewById(R.id.signup_email);
        regionEditText = findViewById(R.id.signup_region);
        signupButton = findViewById(R.id.signup_button);

        // Get reference to Firestore instance
        db = FirebaseFirestore.getInstance();
        connection = new DBConnection(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();
                String phone = phoneEditText.getText().toString().trim();
                String email = emailEditText.getText().toString().trim();
                String region = regionEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Please enter a username");
                    return;
                }
                if (TextUtils.isEmpty(phone)) {
                    usernameEditText.setError("Please enter a phone number");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    usernameEditText.setError("Please enter a email");
                    return;
                }
                if (TextUtils.isEmpty(region)) {
                    usernameEditText.setError("Please enter a region");
                    return;
                }
                connection.setPlayerUsername(username);

                PlayerDB playerDB = new PlayerDB(connection);

                // Create new Player object
                Player player = new Player();
                player.setUsername(username);
                player.setPhoneNumber(phone);
                player.setEmail(email);
                player.setRegion(region);

                playerDB.addPlayer(player, (addedPlayer, success) -> {
                    if (success) {
                        Log.i("SignupActivity", "Player added successfully: " + addedPlayer.getUsername());
                    } else {
                        Log.e("SignupActivity", "Failed to add player.");
                    }
                });
            }
        });
    }
}