package com.example.dad.nsatracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;

/**
 * The account page for the application. From here you enter a start date and end date to go see your data.
 */
public class MyAccount extends ActionBarActivity {

    private String userID;
    private MyAccount that = this;

    private SharedPreferences sharedPref;
    private TextView userDetails; //delete later

    private LocationLog mLocationLog;
    private Button mButton;
    private TextView locationTextView;
    private DatabaseStuff db = new DatabaseStuff(this);

    private LocationManager locationManager;
    private LocationListener locationListener;

    private static final String ADDUSER_URL
            = "http://450.atwebpages.com/logAdd.php?lat=Z&lon=B&speed=C&heading=D&timestamp=E&source=";


    @Override
    public void onBackPressed() {}//user can't press back on this page.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        mLocationLog = new LocationLog();

        if(this.getIntent().getStringExtra("userid") == null) {
            Intent x = new Intent(this, MainActivity.class);
            startActivity(x);
        }
        userID = this.getIntent().getStringExtra("userid");


        // Acquire a reference to the system Location Manager
        locationManager = (LocationManager) this.getSystemService(
                Context.LOCATION_SERVICE);
        // Define a listener that responds to location updates
       locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                Log.i("LOCATION SERVICES", location.toString());//DELETE THIS LATER!!!!!!
                mLocationLog.addLocation(location);

                HashMap<String, String> map = new HashMap<>();
                map.put("source", "" + userID);
                map.put("latitude", Double.toString(location.getLatitude()));
                map.put("longitude", Double.toString(location.getLongitude()));
                map.put("speed", Double.toString(location.getSpeed()));
                map.put("heading", Float.toString(location.getBearing()));
                map.put("timestamp", Double.toString(location.getTime()));

                db.insertContact(map);

                long value = location.getTime();
                String time = Long.toString(value);
                if(userID != null) {
                    String newURL = (ADDUSER_URL.replaceAll("Z", Double.toString(location.getLatitude()))
                            .replaceAll("B", Double.toString(location.getLongitude()))
                            .replaceAll("C", Double.toString(location.getSpeed()))
                            .replaceAll("D", Float.toString(location.getBearing()))
                            .replaceAll("E", time));
                    newURL = newURL + userID;
                    newURL = newURL.replaceAll(" ", "%20");
                    System.out.println(newURL);
                    DownloadWebPageTask task = new DownloadWebPageTask();
                    task.execute(new String[]{newURL});
                }




            }
            public void onStatusChanged(String provider, int status, Bundle extras) {}
            public void onProviderEnabled(String provider) {}
            public void onProviderDisabled(String provider) {}
        };
        // Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                5000, 0, locationListener);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_my_account, menu);
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        userID = this.getIntent().getStringExtra("userid");
        String userName = this.getIntent().getStringExtra("userEmail");

        userDetails = (TextView) findViewById(R.id.account_details);
        userDetails.setText("userid: " + userID
                + "\n" + "userEmail: " + userName);

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

    /**
     * Takes the user to the my trajectory activity.
     *
     * @param view the view passed in.
     */
    public void go(View view) {

        List<Location> locationList = mLocationLog.getLocationList();
        StringBuffer buff = new StringBuffer();
        for(Location l: locationList) {
            buff.append("Latitude: " + l.getLatitude() + "; ");
            buff.append("Longitude: " + l.getLongitude() + "; ");
            buff.append("Speed: " + l.getSpeed() + "; ");
            buff.append("Heading: " + l.getBearing() + "; ");
            buff.append("Source: " + userID + "; ");
            buff.append("Timestamp: " + l.getTime() + "; ");
            buff.append("\n");
        }

        Intent x = new Intent(this, LocationActivity.class);
        x.putExtra("locations", buff.toString());//change later
        x.putExtra("locationsParc", mLocationLog);
        x.putExtra("userid", userID);
        //x.putExtra("allLocations", buff2.toString());
        startActivity(x);
    }

    /**
     * Takes the user back to the main activity and logs the user out.
     *
     * @param view the view passed in.
     */
    public void logout(View view) {
        locationManager.removeUpdates(locationListener);
        locationListener = null;

        Intent x = new Intent(this, MainActivity.class);
        startActivity(x);
    }

    private class DownloadWebPageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            System.out.println("URL: " + urls);

            String response = "";
            for (String url : urls) {
                DefaultHttpClient client = new DefaultHttpClient();
                HttpGet httpGet = new HttpGet(url);
                try {
                    HttpResponse execute = client.execute(httpGet);
                    InputStream content = execute.getEntity().getContent();

                    BufferedReader buffer = new BufferedReader(new InputStreamReader(content));
                    String s = "";
                    while ((s = buffer.readLine()) != null) {
                        response += s;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            System.out.println("test result: " + result);
        }
    }


}
