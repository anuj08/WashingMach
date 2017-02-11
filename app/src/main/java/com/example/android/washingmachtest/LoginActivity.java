package com.example.android.washingmachtest;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;


/**
 * Created by anuj on 16/1/17.
 */

public class LoginActivity extends AppCompatActivity {

    final String LOG_TAG = LoginActivity.class.getName();

    final String ADD_USER = "http://anuj08.5gbfree.com/insert_new_user.php";
    private String mName;
    private String mRoom;
    private long mID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        SharedPreferences settings = getSharedPreferences("prefs", 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean("firstRun", false);
        editor.apply();
    }

    public void rollTheBarrel(View view) {

        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        if (!isConnected) {

            EditText userName = (EditText) findViewById(R.id.login_name);
            userName.setVisibility(View.GONE);

            EditText roomNo = (EditText) findViewById(R.id.login_room);
            roomNo.setVisibility(View.GONE);

            TextView noNet = (TextView) findViewById(R.id.no_internet);
            noNet.setText("Check your internet connection");

            view.setVisibility(View.GONE);

            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("loginSuccess", false);
            editor.apply();
        } else {

            view.setVisibility(View.GONE);

            EditText userName = (EditText) findViewById(R.id.login_name);
            mName = userName.getText().toString();

            EditText roomNo = (EditText) findViewById(R.id.login_room);
            mRoom = roomNo.getText().toString();

            mID = createUID();

            Log.v(LOG_TAG, "Create UID callled");

            SharedPreferences uid = getSharedPreferences("uid", 0);
            SharedPreferences.Editor uidEditor = uid.edit();
            uidEditor.putLong("uid", mID);
            uidEditor.apply();

            URL url = null;

            try {
                url = new URL(ADD_USER);
            } catch (MalformedURLException e) {
                Log.e(LOG_TAG, "Error forming url in createUrl");
            }

            new DownloadFilesTask().execute(url);

        }

    }

    private long createUID() {
        long time = System.currentTimeMillis() % 1000000;
        return time;
    }

    private class DownloadFilesTask extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                //Open the connection
                URLConnection con = urls[0].openConnection();

                //activate the output
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write("user_id=" + mID + "&user_name=" + mName + "&room_no=" + mRoom);
                writer.flush();

                InputStream inputStream = null;
                inputStream = con.getInputStream();
                response = readFromStream(inputStream);
                writer.close();

            } catch (IOException e) {
                System.out.println("IO Exception");
            }

            return response;
        }

        private String readFromStream(InputStream inputStream) {
            StringBuilder output = new StringBuilder();

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                BufferedReader reader = new BufferedReader(inputStreamReader);
                try {
                    String line = reader.readLine();                    //IO
                    while (line != null) {
                        output.append(line);
                        line = reader.readLine();                       //IO
                    }
                } catch (IOException e) {
                    System.out.println("iOexception in readFromStream");
                }
            }
            return output.toString();
        }

        protected void onPostExecute(String result) {
            Context context1 = getApplicationContext();
            CharSequence text1 = "Successfully signed in";
            int duration1 = Toast.LENGTH_LONG;
            Log.v(LOG_TAG, result);

            Toast toast1 = Toast.makeText(context1, text1, duration1);
            toast1.show();

            SharedPreferences settings = getSharedPreferences("prefs", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean("loginSuccess", true);
            editor.apply();

            Intent mainActivity = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(mainActivity);
            finish();
        }
    }
}

