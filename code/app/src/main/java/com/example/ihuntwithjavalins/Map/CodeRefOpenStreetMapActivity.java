package com.example.ihuntwithjavalins.Map;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import com.example.ihuntwithjavalins.R;

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
 * Represents an activity that displays a map using OpenStreetMap and allows for displaying the user's location,
 * as well as adding overlays like a scale bar and compass.
 */
public class CodeRefOpenStreetMapActivity extends AppCompatActivity {

    /** Request code used when requesting permissions. */
    private final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    /** Map view used to display the map. */
    private MapView map = null;
    //    private MyLocationNewOverlay mLocationOverlay;
//    private GpsMyLocationProvider mGPSLocationProvider;
    /** Compass overlay used to display a compass on the map. */
    private CompassOverlay mCompassOverlay;
    /** Scale bar overlay used to display a scale bar on the map. */
    private ScaleBarOverlay mScaleBarOverlay;
    /** Location tracker used to track the user's location. */
    LocationTrack locationTrack;
    /** Back button used to exit the activity. */
    Button backButton;

    //custom BACK button control (since back doesnt work when map enabled) (*still doesnt work!!!)
    /** Overrides the default back button behavior to finish the activity. */
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    //custom BACK button control (END)
    /**
     * Called when the activity is created. Initializes the map and its overlays.
     * @param savedInstanceState A Bundle containing the saved instance state of the activity.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


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

        //inflate and create the map
        setContentView(R.layout.open_street_map);

        backButton = findViewById(R.id.map_backButton);

        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        String[] permissions = {
                // if you need to show the current location, uncomment the line below
                android.Manifest.permission.ACCESS_FINE_LOCATION,
                // WRITE_EXTERNAL_STORAGE is required in order to show the map
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,

                android.Manifest.permission.ACCESS_COARSE_LOCATION //added
//                android.Manifest.permission.ACCESS_WIFI_STATE,
//                android.Manifest.permission.INTERNET,
//                android.Manifest.permission.ACCESS_NETWORK_STATE,
        };
        requestPermissionsIfNecessary(permissions);


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
//        GeoPoint Point_uofa = new GeoPoint(53.52730, -113.52841);

        double latitude = -113.5;
        double longitude = 53.5;
        Bundle extras = getIntent().getExtras();
        String savedCodeLat = extras.getString("imageSavedCodeLat");//The key argument here must match that used in the other activity
        String savedCodeLon = extras.getString("imageSavedCodeLon");//The key argument here must match that used in the other activity
        latitude = Double.parseDouble(savedCodeLat);
        longitude = Double.parseDouble(savedCodeLon);
        GeoPoint myGPS_point = new GeoPoint(latitude, longitude); // current 'location tracker' point
        ArrayList<OverlayItem> items = new ArrayList<OverlayItem>();
//        items.add(new OverlayItem("NREF building poster1", "450", new GeoPoint(53.52670, -113.52895))); // Lat/Lon decimal degrees 'd'
        //this item's location map point 'item'
        OverlayItem myGPSoverlayItem = new OverlayItem("Code Location", "here", myGPS_point);
        items.add(myGPSoverlayItem);

        //the overlay
        ItemizedOverlayWithFocus<OverlayItem> mOverlay = new ItemizedOverlayWithFocus<OverlayItem>(items,
                new ItemizedIconOverlay.OnItemGestureListener<OverlayItem>() {
                    @Override
                    public boolean onItemSingleTapUp(final int index, final OverlayItem item) {
                        //do something
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
        mapController.setCenter(myGPS_point);


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
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
    /**
     * This method is called when the app requests permissions from the user.
     * It requests permissions for the app using the provided parameters.
     * @param requestCode an integer representing the request code
     * @param permissions an array of strings representing the permissions requested
     * @param grantResults an array of integers representing the results of the permissions requests
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
    /**
     * This method requests permissions if necessary for the app to function properly.
     * If the app does not have the necessary permissions, it requests the permissions using ActivityCompat.requestPermissions.
     * @param permissions an array of strings representing the permissions the app needs
     */
    private void requestPermissionsIfNecessary(String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


}

