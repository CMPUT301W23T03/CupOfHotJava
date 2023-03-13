package com.example.ihuntwithjavalins.Scoreboard;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.ihuntwithjavalins.Player.Player;
import com.example.ihuntwithjavalins.QRCode.QRCode;
import com.example.ihuntwithjavalins.R;
import com.example.ihuntwithjavalins.common.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Transaction;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {

    ArrayList<Player> PlayerCodeList;
    ArrayAdapter<Player> PlayerCodeAdaptor;
    ArrayList<StoreNamePoints> StorageList=new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scoreboard_main);
        ListView codeList;


        codeList = findViewById(R.id.user_code_list);
        PlayerCodeList = new ArrayList<>();

        String[] names = {"IAmStan", "MrKatana", "Viraj"};



        QRCode q1 = new QRCode("10");
        QRCode q2 = new QRCode("2_432100");
        QRCode q3 = new QRCode("3_4323405-Jan-0");
        QRCode q4 = new QRCode("4_43234168");
        QRCode q5 = new QRCode("1_4352");
        QRCode q6 = new QRCode("2_491");
        QRCode q7 = new QRCode("3_4353");
        QRCode q8 = new QRCode("4_432344118");
        QRCode q9 = new QRCode("445");
        QRCode q10 = new QRCode("683");
        QRCode q11 = new QRCode("3_451");
        QRCode q12 = new QRCode("4_44568");
        QRCode q13 = new QRCode("1_2352");
        QRCode q14 = new QRCode("2_432344591");
        QRCode q15 = new QRCode("3_43234483");
        QRCode q16 = new QRCode("4_43234408");

        Player me = new Player("Viraj","test@gmail.com","5879372653","Alberta");
        me.addCode(q1);
        me.addCode(q2);
        me.addCode(q3);



        Player player1 = new Player("John Doe", "john.doe@example.com", "555-1234", "New York");
        player1.addCode(q1);
        player1.addCode(q4);
        player1.addCode(q7);

        Player player2 = new Player("Jane Smith", "jane.smith@example.com", "555-5678", "Los Angeles");
        player2.addCode(q2);
        player2.addCode(q5);
        player2.addCode(q8);

        Player player3 = new Player("Bob Johnson", "bob.johnson@example.com", "555-9876", "Chicago");
        player3.addCode(q3);
        player3.addCode(q6);
        player3.addCode(q9);

        Player player4 = new Player("Alice Lee", "alice.lee@example.com", "555-4321", "San Francisco");
        player4.addCode(q1);
        player4.addCode(q5);
        player4.addCode(q9);

        Player player5 = new Player("David Kim", "david.kim@example.com", "555-2468", "Seattle");
        player5.addCode(q2);
        player5.addCode(q6);
        player5.addCode(q10);

        PlayerCodeList.add(me);
        PlayerCodeList.add(player1);
        PlayerCodeList.add(player2);
        PlayerCodeList.add(player3);
        PlayerCodeList.add(player4);
        PlayerCodeList.add(player5);
        PlayerCodeAdaptor = new CustomListScoreBoard(ScoreboardActivity.this,PlayerCodeList);
        codeList.setAdapter(PlayerCodeAdaptor);


        codeList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                List<QRCode> codes = PlayerCodeList.get(position).getCodes();

//                Toast.makeText(ScoreboardActivity.this,Integer.toString(codes.size()) , Toast.LENGTH_SHORT).show();
                for(int i=0;i<codes.size();i++){

                    StoreNamePoints store = new StoreNamePoints(codes.get(i).getCodeName(),codes.get(i).getCodePoints(),true);
                    StorageList.add(store);

                }
//                Toast.makeText(ScoreboardActivity.this, StorageList.get(1).getCodeName(), Toast.LENGTH_SHORT).show();




                Intent intent = new Intent(ScoreboardActivity.this,ShowIndividualCodes.class);
//                Toast.makeText(ScoreboardActivity.this, PlayerCodeList.get(position).getUsername(), Toast.LENGTH_SHORT).show();
                String get_username = PlayerCodeList.get(position).getUsername();
                intent.putExtra("USER",get_username);
                intent.putExtra("codes",StorageList);

                startActivity(intent);
                StorageList.clear();
            }
        });

//        Toast.makeText(this, Integer.toString(me.getTotalCodes()), Toast.LENGTH_SHORT).show();

        Button points_btn = findViewById(R.id.sort_points_btn);
        points_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ScoreboardActivity.this, "Sort By Points", Toast.LENGTH_SHORT).show();
                Collections.sort(PlayerCodeList);
                PlayerCodeAdaptor = new CustomListScoreBoard(ScoreboardActivity.this,PlayerCodeList);
                codeList.setAdapter(PlayerCodeAdaptor);

            }
        });

        Button names_btn = findViewById(R.id.sort_name_btn);
        names_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(ScoreboardActivity.this, "Sort by names", Toast.LENGTH_SHORT).show();
                // Sort the player list by name
                Collections.sort(PlayerCodeList, new Comparator<Player>() {
                    @Override
                    public int compare(Player p1, Player p2) {
                        return p1.getUsername().compareTo(p2.getUsername());
                    }
                });

                // Update the adapter with the sorted list

                PlayerCodeAdaptor = new CustomListScoreBoard(ScoreboardActivity.this,PlayerCodeList);
                codeList.setAdapter(PlayerCodeAdaptor);


            }
        });

        Button search_btn = findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView searchEditText = findViewById(R.id.search_user);
                String searchQuery = searchEditText.getText().toString().toLowerCase();

                ArrayList<Player> searchResultsList = new ArrayList<>();

                for (Player player : PlayerCodeList) {
                    if (player.getUsername().toLowerCase().contains(searchQuery)) {
                        searchResultsList.add(player);
                    }
                }

                if (searchResultsList.size() == 0){
                    Toast.makeText(ScoreboardActivity.this, "Couldnt find any names starting with"+searchQuery, Toast.LENGTH_SHORT).show();
                }
                else {

                    // Display search results in the list view
                    PlayerCodeAdaptor = new CustomListScoreBoard(ScoreboardActivity.this, searchResultsList);
                    codeList.setAdapter(PlayerCodeAdaptor);
                    PlayerCodeAdaptor.notifyDataSetChanged();
                }

            }
        });








    }
}