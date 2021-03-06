package com.example.dad.nsatracker;

import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;


/**
 * DEAD CODE. NOT USED UNTIL PHASE 3
 */
public class MapActivity extends ActionBarActivity implements OnMapReadyCallback {
    private LocationLog mLocationLog;
    private GoogleMap mGoogleMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
//        mLocationLog = (LocationLog) getIntent().getParcelableExtra("locations");
//        MapFragment mapFragment = (MapFragment) getFragmentManager()
//                .findFragmentById(R.id.map);
//        mGoogleMap = mapFragment.getMap();
//        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap map) {
        /*if (mGoogleMap != null) {
        Log.i("Map Activity", "Inside mGoogleMap");
        LatLng latlng = new LatLng(47.2528768,-122.4442906 ); // Tacoma coordinates
        Marker marker = mGoogleMap.addMarker(new MarkerOptions()
        .position(latlng)
        .title("Tacoma")
        .snippet("Tacoma is the coolest city!!"));
        // Move the camera instantly to tacoma with a zoom of 15.
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, 15));
        }*/
        // Seattle coordinates - 47.6097, -122.3331
        if (mLocationLog != null) {
            List<Location> locations = mLocationLog.getLocationList();
            Location location = locations.get(0);
            LatLng firstLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            for (int i=0; i<locations.size(); i++) {
                Marker marker = mGoogleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(locations.get(i).getLatitude()
                                , locations.get(i).getLongitude()))
                        .title("My Locations"));
            }
            mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLatLng, 15));
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
