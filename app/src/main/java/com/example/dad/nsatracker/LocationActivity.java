package com.example.dad.nsatracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * This class shows all your location data points.
 */
public class LocationActivity extends ActionBarActivity {
    private LocationLog mLocationLog;
    private Button mButton;
    private TextView locationTextView;
    private String locations;
    private String userID;

    private static final String ADDUSER_URL
            = "http://450.atwebpages.com/view.php?uid=A&start=B&end=C";




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        locationTextView = (TextView) findViewById(R.id.textView2);
        locations = this.getIntent().getStringExtra("locations");
        locationTextView.setText(locations);
        //mLocationLog = new LocationLog();
        userID = this.getIntent().getStringExtra("userid");
        mButton = (Button) findViewById(R.id.map_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(v.getContext(), MapActivity.class);
                i.putExtra("locations", mLocationLog);
                startActivity(i);
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_location, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh(View view) {

        //Latitude  Longitude  Speed  Heading  Source (unique id)  Timestamp
//        Random ran = new Random();
//        List<Location> locationList = mLocationLog.getLocationList();
//        StringBuffer buff = new StringBuffer();
//        for(Location l: locationList) {
//            buff.append("Latitude: " + l.getLatitude() + "; ");
//            buff.append("Longitude: " + l.getLongitude() + "; ");
//            buff.append("Speed: " + l.getSpeed() + "; ");
//            buff.append("Heading: " + "North" + "; ");
//            buff.append("Source: " + ran.nextInt(999999999) + "; ");
//            buff.append("Timestamp: " + l.getTime() + "; ");
//            buff.append("\n");
//        }

//        ArrayList<HashMap<String, String>> allLocations = db.getAllContacts();
//        for(HashMap<String, String> l : allLocations) {
//            StringBuffer buff = new StringBuffer();
//
//            buff.append("Latitude: " + l.get("latitude") + "; ");
//            buff.append("Longitude: " + l.get("longitude") + "; ");
//            buff.append("Speed: " + l.get("speed") + "; ");
//            buff.append("Heading: " + "North" + "; ");
//            buff.append("Source: " + l.get("source") + "; ");
//            buff.append("Timestamp: " + l.get("time") + "; ");
//            buff.append("\n");
//
//        }

        if(userID != null) {
            String newURL = ADDUSER_URL.replaceAll("A", userID);
            newURL = newURL.replaceAll("B", "0");
            newURL = newURL.replaceAll("C", "99999999999999");//Webservices is not working when it comes to getting all the locations as the correct times so I hard coded it to grab everything.
            newURL = newURL.replaceAll(" ", "%20");
            System.out.println(newURL);
            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[]{newURL});
        }


        //locationTextView.setText(locations);
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
            String mCourses = result;
            StringBuffer buff = new StringBuffer();
            if (mCourses != null) {
                try {
                    JSONObject obj = new JSONObject(mCourses);
                    JSONArray arr = obj.getJSONArray("points");

                    for(int i = 0; i < arr.length(); i++) {
                        buff.append("Latitude: " + arr.getJSONObject(i).get("lat") + "; ");
                        buff.append("Longitude: " + arr.getJSONObject(i).get("lon") + "; ");
                        buff.append("Speed: " + arr.getJSONObject(i).get("speed") + "; ");
                        buff.append("Heading: " + arr.getJSONObject(i).get("heading") + "; ");
                        buff.append("Timestamp: " + arr.getJSONObject(i).get("time") + "; ");
                        buff.append("\n");

                    }

                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

                locationTextView.setText(buff.toString());

            }
        }
    }
}