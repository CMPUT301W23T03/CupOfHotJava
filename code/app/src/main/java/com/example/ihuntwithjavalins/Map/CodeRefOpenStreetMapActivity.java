package com.example.ihuntwithjavalins.Map;

import static android.content.ContentValues.TAG;
import static com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.ihuntwithjavalins.Player.Player;
import com.example.ihuntwithjavalins.QRCode.QRCode;

import com.example.ihuntwithjavalins.R;

import com.google.android.gms.location.FusedLocationProviderClient;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.ItemizedOverlayWithFocus;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.ScaleBarOverlay;
import org.osmdroid.views.overlay.compass.CompassOverlay;
import org.osmdroid.views.overlay.compass.InternalCompassOrientationProvider;

import java.util.ArrayList;

/**
 * The OpenStreetMapActivity class is an activity that displays a map using osmdroid library.
 * It loads the osmdroid configuration and inflates the map layout, which contains the map and some UI elements.
 * It also handles location tracking, map zooming and panning, and adding overlays to the map.
 * Design Patterns:
 * factory pattern - TileSourceFactory in MapView
 * singleton pattern - configuration class using getInstance()
 * observer pattern - ItemizedOverlayWithFocus observes the users interactions and notifies the listener
 * command pattern - view.onClickListener
 */
public class CodeRefOpenStreetMapActivity extends AppCompatActivity {
    private String TAGmap = "Sample"; // used as string tag for debug-log messaging
    private MapView map = null;
    private CompassOverlay mCompassOverlay;
    private ScaleBarOverlay mScaleBarOverlay;
    private Button backButton;
    private FusedLocationProviderClient fusedLocationClient;
    private Player myPlayer;
    private ArrayList<Player> playerList;
    private QRCode focusedCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAGmap, "playerlist 1");
        //(handle permissions first, before map is created. not depicted here)
        //load/initialize the osmdroid configuration, this can be done
        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));
        //setting this before the layout is inflated is a good idea
        //it 'should' ensure that the map has a writable location for the map cache, even without permissions
        //if no tiles are displayed, you can try overriding the cache path using Configuration.getInstance().setCachePath
        //see also StorageUtils
        //note, the load method also sets the HTTP User Agent to your application's package name, abusing osm's
        //tile servers will get you banned based on this string

        // Get the intent from the previous activity
        Intent myIntent = getIntent();
        focusedCode = (QRCode) myIntent.getSerializableExtra("imageSavedCode");

        //inflate and create the map
        setContentView(R.layout.open_street_map);

        backButton = findViewById(R.id.map_backButton);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        //How to add Map 'Scale bar' overlay
        final DisplayMetrics dm = ctx.getResources().getDisplayMetrics();
        mScaleBarOverlay = new ScaleBarOverlay(map);
        mScaleBarOverlay.setCentred(true);
        //play around with these values to get the location on screen in the right place for your application
        mScaleBarOverlay.setScaleBarOffset(dm.widthPixels / 2, 10);
        map.getOverlays().add(this.mScaleBarOverlay);
        //How to add Map 'Scale bar' overlay (END)

        //How to add a 'compass' overlay
        this.mCompassOverlay = new CompassOverlay(ctx, new InternalCompassOrientationProvider(ctx), map);
        this.mCompassOverlay.enableCompass();
        map.getOverlays().add(this.mCompassOverlay);
        //How to add a compass overlay (END)

        //Map controller
        IMapController mapController = map.getController();
        mapController.setZoom(18.5);






        // tracking my location
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        //delay timer to move (used click instead, needs to be improved)
        new Handler().postDelayed(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                //example map points (adding to array of points)
                ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
//        GeoPoint Point_uofa = new GeoPoint(53.52730, -113.52841);
//        items.add(new OverlayItem("NREF building poster1", "450", new GeoPoint(53.52670, -113.52895))); // Lat/Lon decimal degrees 'd'
                items.add(new OverlayItem(focusedCode.getCodeName(), focusedCode.getCodePoints(), new GeoPoint(Float.parseFloat(focusedCode.getCodeLat()), Float.parseFloat(focusedCode.getCodeLon())))); // Lat/Lon decimal degrees
//                items.add(new OverlayItem("tree poster3", "2454", new GeoPoint(53.52744, -113.52723))); // Lat/Lon decimal degrees
//                items.add(new OverlayItem("CSC building poster4", "12", new GeoPoint(53.52694, -113.52740))); // Lat/Lon decimal degrees
//                items.add(new OverlayItem("DICE building poster5", "76", new GeoPoint(53.52793, -113.52888))); // Lat/Lon decimal degrees
//
//                ArrayList<QRCode> pointsCodeList = new ArrayList<>();
//                ArrayList<String> pointsCodeListStrings = new ArrayList<>();
//                for (Player player : playerList) {
////                    items.add(new OverlayItem(player.getUsername(), player.getRegion(), new GeoPoint(53.52793, -113.52888))); // Lat/Lon decimal degrees
//                    ArrayList<QRCode> tempCodeList = (ArrayList<QRCode>) player.getCodes();
//                    for (QRCode code : tempCodeList) {
//                        if (!pointsCodeListStrings.contains(code.getCodeHash())) {
//                            pointsCodeListStrings.add(code.getCodeHash());
//                            pointsCodeList.add(code);
//                        }
//                    }
//                }
//                Log.d(TAGmap, "geopoints size: " + pointsCodeListStrings.size());
//                for (QRCode code : pointsCodeList) {
//                    items.add(new OverlayItem(code.getCodeName(), code.getCodePoints(), new GeoPoint(Float.parseFloat(code.getCodeLat()), Float.parseFloat(code.getCodeLon())))); // Lat/Lon decimal degrees
//                }

//                CancellationTokenSource cancellationTokenSource = new CancellationTokenSource();
//                final GeoPoint[] myGPS_point = new GeoPoint[1];
//                fusedLocationClient.getCurrentLocation(PRIORITY_HIGH_ACCURACY, cancellationTokenSource.getToken()) // ignore this error
//                        .addOnSuccessListener(OpenStreetMapActivity.this, new OnSuccessListener<Location>() {
//                            @Override
//                            public void onSuccess(Location location) {
//                                // Got last known location. In some rare situations this can be null.
//                                if (location != null) {
//                                    myGPS_point[0] = new GeoPoint(location.getLatitude(), location.getLongitude()); // current 'location tracker' point
//                                    //my location map point 'item'
//                                    OverlayItem myGPSoverlayItem = new OverlayItem("My Location", " ", myGPS_point[0]);
//                                    items.add(myGPSoverlayItem);
//                                    mapController.setCenter(myGPS_point[0]);
//                                } else {
////                            Log.w(TAG, "No current location could be found");
//                                }
//                            }
//                        });


                //the overlay
                ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                        new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                            @Override
                            public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                                mapController.setCenter(item.getPoint());
                                return true;
                            }

                            @Override
                            public boolean onItemLongPress(final int index, final OverlayItem item) {
                                mapController.setCenter(item.getPoint());
                                return false;
                            }
                        }, ctx);
                mOverlay.setFocusItemsOnTap(true);
                map.getOverlays().add(mOverlay); // add 'item' array of points

//                OverlayItem myGPSoverlayItem = new OverlayItem("My Location", " ", myGPS_point[0]);
//                items.add(myGPSoverlayItem);
                mapController.setCenter(items.get(0).getPoint());
            }
        }, 1000);



    }


    @Override
    public void onResume() {
        super.onResume();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().load(this, PreferenceManager.getDefaultSharedPreferences(this));
        map.onResume(); //needed for compass, my location overlays, v6.0.0 and up
    }

    @Override
    public void onPause() {
        super.onPause();
        //this will refresh the osmdroid configuration on resuming.
        //if you make changes to the configuration, use
        //SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //Configuration.getInstance().save(this, prefs);
        map.onPause();  //needed for compass, my location overlays, v6.0.0 and up
    }

}

