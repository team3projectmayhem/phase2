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
 * The registration page for the activity. Here a user can type in his details to make a new account.
 */
public class Register extends ActionBarActivity {

    Register that = this;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;


    EditText username;
    EditText password;
    EditText secretQuestion;
    EditText secretAnswer;

    private static final String ADDUSER_URL
            = "http://450.atwebpages.com/adduser.php?email=A&password=B&question=C%3F&answer=D";

    DatabaseStuff db = new DatabaseStuff(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.email_field);
        password = (EditText) findViewById(R.id.password_field);
        secretQuestion = (EditText) findViewById(R.id.secret_question);
        secretAnswer = (EditText) findViewById(R.id.secret_answer);

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
     * Takes the user to the user agreement activity.
     * The method makes sure that all the user data passed in will be valid.
     *
     * @param view the view passed in.
     */
    public void registerUser(View view) {

        Context context = getApplicationContext();
        String text = "";
        int duration = Toast.LENGTH_SHORT;
        Toast toast;

        if(!(username.getText().toString().length() > 5)) {
            text = "The username must be at least 6 characters long";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }else if (!(password.getText().toString().length() > 5)) {
            text = "The password must be at least 6 characters long";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }else if (!(secretQuestion.getText().toString().length() > 2)) {
            text = "The secret question must be at least 3 characters long";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }else if (!(secretAnswer.getText().toString().length() > 2)) {
            text = "The answer must be at least 3 characters long";
            toast = Toast.makeText(context, text, duration);
            toast.show();
        }

        if(username.getText().toString().length() > 5 &&
                password.getText().toString().length() > 5 &&
                secretQuestion.getText().toString().length() > 2 &&
                secretAnswer.getText().toString().length() > 2 &&
                !username.getText().toString().equals("") &&
                !password.getText().toString().equals("") &&
                !secretQuestion.getText().toString().equals("") &&
                !secretAnswer.getText().toString().equals("")) {
            username = (EditText) findViewById(R.id.email_field);
            password = (EditText) findViewById(R.id.password_field);
            secretQuestion = (EditText) findViewById(R.id.secret_question);
            secretAnswer = (EditText) findViewById(R.id.secret_answer);

            String newURL = ADDUSER_URL.replaceAll("A", username.getText().toString()).replaceAll("B", password.getText().toString())
                    .replaceAll("C", secretQuestion.getText().toString()).replaceAll("D", secretAnswer.getText().toString())
                    .replaceAll(" ", "%20");
            System.out.println(newURL);
            DownloadWebPageTask task = new DownloadWebPageTask();
            task.execute(new String[]{newURL});


//            Intent x = new Intent(this, UserAgreement.class);
//            startActivity(x);
        }



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
                    String text = obj.getString("result");
                    if (obj.getString("result").equals("fail"))
                        text = text + "\n" + obj.getString("error");
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    if (obj.getString("result").equals("success")) {
                        //User user = new User();
                        //sharedPref = that.getPreferences(Context.MODE_PRIVATE);
                        //String[] userSringArray = new String[4];
                        //editor = sharedPref.edit();
//                        userSringArray[0] = username.getText().toString();
//                        userSringArray[1] = password.getText().toString();
//                        userSringArray[2] = secretQuestion.getText().toString();
//                        userSringArray[3] = secretAnswer.getText().toString();
                        //editor.commit();


                        Intent x = new Intent(that, MainActivity.class);
                        //x.putExtra("userDataArray", userSringArray);
                        startActivity(x);
                    }


                } catch (JSONException e) {
                    System.out.println("JSON Exception");
                }

            }




        }
    }
}
