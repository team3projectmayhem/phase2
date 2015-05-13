package com.example.dad.nsatracker;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
 * The start of the application. This is where you can log in, go to the register screen, or go to the forgot password screen.
 */
public class MainActivity extends ActionBarActivity {

    MainActivity that = this;

    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    EditText username;
    EditText password;

    private static final String ADDUSER_URL
            = "http://450.atwebpages.com/login.php?email=A&password=B";


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        username = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

    }


    @Override
    protected void onStop() {
        super.onStop();

        username = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        editor.putString("username", username.getText().toString());
        editor.putString("password", password.getText().toString());
        editor.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        sharedPref = this.getPreferences(Context.MODE_PRIVATE);
        username = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

        username.setText(sharedPref.getString("username", ""));
        password.setText(sharedPref.getString("password", ""));
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
     * Takes the user to the login activity.
     *
     * @param view the view passed in.
     */
    public void logIn(View view) {
        username = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);

        if(username.getText().toString().length() > 4 &&
                !username.getText().toString().equals("") &&
                !password.getText().toString().equals("")) {

            String newURL = ADDUSER_URL.replaceAll("A", username.getText().toString()).replaceAll("B", password.getText().toString()).replaceAll(" ", "");
            System.out.println(newURL);
            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[]{newURL});

        }

//        Intent x = new Intent(this, MyAccount.class);
//        startActivity(x);
    }

    /**
     * Takes the user to the register activity.
     *
     * @param view the view passed in.
     */
    public void register(View view) {
        Intent x = new Intent(this, UserAgreement.class);
        startActivity(x);
    }

    /**
     * Takes the user to the forgot password activity.
     *
     * @param view the view passed in.
     */
    public void forgotPassword(View view) {
        Intent x = new Intent(this, ForgotPassword.class);
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


            //text2 = (TextView) findViewById(R.id.textView);
            String mCourses = result;

            if (mCourses != null) {
                try {
                    JSONObject obj = new JSONObject();
                    JSONArray arr = new JSONArray("[" + mCourses + "]");

                    obj = arr.getJSONObject(0);

                    //String agreementText = obj.getString("result");
                    //text2 = (TextView) findViewById(R.id.textView);
                    //text2.setText(agreementText);

                    Context context = getApplicationContext();
                    String didWork = obj.getString("result");
                    String userID = obj.getString("userid");
                    if (obj.getString("result").equals("fail"))
                        didWork = didWork + "\n" + obj.getString("error");
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, didWork, duration);
                    toast.show();

                    if (obj.getString("result").equals("success")) {

                        Intent x = new Intent(that, MyAccount.class);
                        x.putExtra("userEmail", username.getText().toString());
                        x.putExtra("userid", userID);
                        startActivity(x);
                    }

                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

            }







        }
    }
}
