package com.example.android.washingmachtest;

/**
 * Created by anuj on 21/1/17.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import static com.example.android.washingmachtest.MainActivity.LOG_TAG;

public class CustomDialougeClass extends Dialog implements
        android.view.View.OnClickListener {

    private final String URL = "http://anuj08.5gbfree.com/CheckoutUser.php";
    public Activity c;
    public Dialog d;
    public Button yes, no;
    private WashingMachine mWashingMachine;

    public CustomDialougeClass(Activity a, WashingMachine washingMachine) {
        super(a);
        mWashingMachine = washingMachine;
        // TODO Auto-generated constructor stub
        this.c = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.custom_dialouge);
        yes = (Button) findViewById(R.id.btn_yes);
        no = (Button) findViewById(R.id.btn_no);
        yes.setOnClickListener(this);
        no.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                URL url = null;

                try {
                    url = new URL(URL);
                } catch (MalformedURLException e) {
                    Log.e(LOG_TAG, "Error forming url in createUrl");
                }

                new CheckoutUser().execute(url);
                //c.finish();
                break;
            case R.id.btn_no:
                dismiss();
                break;
            default:
                break;
        }
        dismiss();
    }

    private class CheckoutUser extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                //Open the connection
                URLConnection con = urls[0].openConnection();

                //activate the output
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write("wm_id=" + mWashingMachine.getWashingMachineId());
                writer.flush();
                // PrintStream ps = new PrintStream(con.getOutputStream());
                // ps.print("wm_id=11");
                // ps.print("&user_id=108");
                // ps.print("&time_left='09:06:03'");

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
            Intent UserInfo = new Intent(c, MainActivity.class);
            c.startActivity(UserInfo);
            c.finish();
        }
    }
}

