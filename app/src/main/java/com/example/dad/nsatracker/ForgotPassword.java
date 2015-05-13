package com.example.dad.nsatracker;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
 * The retrieving password part of the application. Sends the user a new password in case he forgets.
 */
public class ForgotPassword extends ActionBarActivity {

    EditText text;
    TextView text2;
    private static final String FORGOT_URL
            = "http://450.atwebpages.com/reset.php?email=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);


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

    /**
     * Sends a new password to the user by email.
     *
     * @param view the view passed in.
     */
    public void retrievePassword(View view) {
        text = (EditText) findViewById(R.id.email_field);
        String newURL = FORGOT_URL + text.getText().toString();
        System.out.println(newURL);
        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[]{newURL});

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


            String mCourses = result;

            if (mCourses != null) {
                try {
                    JSONObject obj = new JSONObject();
                    JSONArray arr = new JSONArray("[" + mCourses + "]");

                    obj = arr.getJSONObject(0);

                    Context context = getApplicationContext();
                    String text = obj.getString("result");
                    if (obj.getString("result").equals("fail"))
                        text = text + "\n" + obj.getString("error");
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();



                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

            }


        }
    }
}
