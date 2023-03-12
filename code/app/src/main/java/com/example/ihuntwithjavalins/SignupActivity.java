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
    private Button signupButton;
    private FirebaseFirestore db;
    private DBConnection connection;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);

        // Get references to EditText views in layout
        usernameEditText = findViewById(R.id.signup_username);
        signupButton = findViewById(R.id.signup_button);

        // Get reference to Firestore instance
        db = FirebaseFirestore.getInstance();
        connection = new DBConnection(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = usernameEditText.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    usernameEditText.setError("Please enter a username");
                    return;
                }
                connection.setPlayerUsername(username);

                PlayerDB playerDB = new PlayerDB(connection);

                // Create new Player object
                Player player = new Player();
                player.setUsername(username);

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