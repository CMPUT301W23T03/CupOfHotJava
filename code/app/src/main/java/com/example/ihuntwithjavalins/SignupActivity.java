package com.example.ihuntwithjavalins;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihuntwithjavalins.Player.Player;
import com.example.ihuntwithjavalins.Player.PlayerDB;
import com.example.ihuntwithjavalins.common.DBConnection;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
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
                    phoneEditText.setError("Please enter a phone number");
                    return;
                }
                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Please enter an email");
                    return;
                }
                if (TextUtils.isEmpty(region)) {
                    regionEditText.setError("Please enter a region");
                    return;
                }

                db.collection("Users").document(username).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                // The username already exists in Firestore
                                Log.d("Username", "This username is already taken");
                                Toast.makeText(SignupActivity.this, "This username is already taken. Please choose a different username.", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                // The username is available in Firestore
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
                        }
                        else {
                            Log.e("SignupActivity", "Error getting document: ", task.getException());
                        }
                    }
                });
            }
        });

    }
}