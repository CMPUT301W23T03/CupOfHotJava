package com.example.ihuntwithjavalins;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;

import com.example.ihuntwithjavalins.Camera.CameraScanActivity;
import com.example.ihuntwithjavalins.Map.OpenStreetMapActivity;
import com.example.ihuntwithjavalins.QRCode.QRCodeLibraryActivity;

import java.util.List;

public class NavBarAdapter extends BaseAdapter {

    private List<String> mData;
    private LayoutInflater mInflater;
    private Context context;

    public NavBarAdapter(Context context, List<String> data) {
        mData = data;
        this.context = context;
        mInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        if (rowView == null) {
            rowView = mInflater.inflate(R.layout.navbar_content, null);
        }

        Button cameraButton = rowView.findViewById(R.id.scan);
        Button mapButton = rowView.findViewById(R.id.nav_map);
        Button codesButton = rowView.findViewById(R.id.codes);
        Button scoreboardButton = rowView.findViewById(R.id.scoreboard);

        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, CameraScanActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, OpenStreetMapActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        codesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, QRCodeLibraryActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        });
        scoreboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle button 2 click event
            }
        });
        return rowView;
    }
}
