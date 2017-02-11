package com.example.android.washingmachtest;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TimePicker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.Calendar;

import static android.content.Context.MODE_PRIVATE;
import static com.example.android.washingmachtest.MainActivity.LOG_TAG;

/**
 * Created by anuj on 15/1/17.
 */

public class TimePickerFragment extends android.support.v4.app.DialogFragment implements TimePickerDialog.OnTimeSetListener {

    final String ADD_TASK = "http://anuj08.5gbfree.com/Add_a_user_test.php";
    int mWMID;
    long mUID;
    long mInputTime;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current time as the default values for the picker
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            WashingMachine washingMachine = bundle.getParcelable("WMInfo");

            mWMID = washingMachine.getWashingMachineId();
        }

        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        // Create a new instance of TimePickerDialog and return it
        return new TimePickerDialog(getActivity(), this, hour, minute, true);
    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        mInputTime = hourOfDay * 60 * 60 + minute * 60;
        SharedPreferences uid = getContext().getSharedPreferences("uid", 0);

        mUID = uid.getLong("uid", MODE_PRIVATE);

        URL url = null;

        try {
            url = new URL(ADD_TASK);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error forming url in createUrl");
        }

        new EnterUser().execute(url);

        //Notification

        Context context = getActivity();
        Activity activity = getActivity();
        long set_time = System.currentTimeMillis() + mInputTime * 1000;

        Intent intent = new Intent(context, AlarmManagerBroadcastReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Log.v(LOG_TAG, "value of inputTime " + mInputTime);
        AlarmManager manager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            manager.set(AlarmManager.RTC_WAKEUP, set_time, alarmIntent);
        } else {
            manager.setExact(AlarmManager.RTC_WAKEUP, set_time, alarmIntent);
        }


        Log.v(LOG_TAG, "value of set time " + mInputTime * 1000);
        //Log.v(LOG_TAG, "AlarmManagerSet");

        activity.finish();
        Intent intent1 = new Intent(context, MainActivity.class);
        startActivity(intent1);

    }

    private class EnterUser extends AsyncTask<URL, Void, String> {
        protected String doInBackground(URL... urls) {

            String response = null;
            try {
                //Open the connection
                URLConnection con = urls[0].openConnection();

                //activate the output
                con.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
                writer.write("wm_id=" + mWMID + "&user_id=" + mUID + "&time_left=" + mInputTime);
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
        }
    }
}
