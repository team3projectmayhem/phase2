package com.example.dad.nsatracker;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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
 * The agreement part of the application. This is where the user sees what he is agreeing to.
 */
public class UserAgreement extends ActionBarActivity {

    private static final String AGREEMENT_URL
            = "http://450.atwebpages.com/agreement.php";

    TextView text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_agreement);
    }

    protected void onStart() {
        super.onStart();

        DownloadWebPageTask task = new DownloadWebPageTask();
        task.execute(new String[]{AGREEMENT_URL});

    }


        @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_user_agreement, menu);
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
     * Takes the user to the my account activity.
     *
     * @param view the view passed in.
     */
    public void agree(View view) {


        Intent x = new Intent(this, Register.class);
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


            text = (TextView) findViewById(R.id.textView2);



            String mCourses = result;

            if (mCourses != null) {
                try {
                    JSONObject obj = new JSONObject();
                    JSONArray arr = new JSONArray("[" + mCourses + "]");
                    //Log.v("Jsonnnn3453", mCourses);
                    //Log.v("Jsonnnn", arr.toString());
                    obj = arr.getJSONObject(0);

                    //Log.v("Jsonnnn", obj.toString());
                    String agreementText = obj.getString("agreement");
                    text = (TextView) findViewById(R.id.textView2);
                    text.setText(agreementText);

                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

            }


        }
    }
}
