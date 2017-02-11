package com.example.android.washingmachtest;

/**
 * Created by anuj on 10/1/17.
 */

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


public final class QueryUtils {

    private final static String LOG_TAG = QueryUtils.class.getName();

    private QueryUtils() {
    }

    /**
     * Return a list of {@link WashingMachine} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<WashingMachine> extractEarthquakes(String stringUrl) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<WashingMachine> washingMachines = new ArrayList<>();
        String jsonResponse = fetchEarthquakeData(stringUrl);
        Log.v(LOG_TAG, jsonResponse);

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {


            JSONObject root = new JSONObject(jsonResponse);

            int success = root.getInt("success");
            if (success == 0)
                Log.e(LOG_TAG, "query was unsuccessful");
            else
                Log.v(LOG_TAG, "query successful");

            JSONArray products = root.optJSONArray("products");

            for (int i = 0; i < products.length(); i++) {

                JSONObject values = products.optJSONObject(i);

                int wm_id = values.getInt("wm_id");
                Log.v(LOG_TAG, "Value of wmid" + wm_id);

                long user_id = values.getLong("user_id");
                //Log.v(LOG_TAG,"value of user id" + user_id);

                long time_started = values.getLong("time_started");
                //Log.v(LOG_TAG,"Value of time started" + time_started );

                long time_left = values.getLong("time_left");
                //Log.v(LOG_TAG,"Value of time left" + time_left );

                long time = (time_started + time_left - (System.currentTimeMillis() / 1000));
                //Log.v(LOG_TAG,"Value of time" + time );

                String user_name = values.getString("user_name");
                //Log.v(LOG_TAG,"Value of username" + user_name);

                String room_no = values.getString("room_no");
                //Log.v(LOG_TAG,"Value of userRoom" + room_no);

                if (user_name.equals("null")) {
                    //Log.v("damn", "username is null");
                    washingMachines.add(new WashingMachine(wm_id, false));

                } else {
                    washingMachines.add(new WashingMachine(wm_id, true, time, user_id, user_name, room_no));
                }
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return washingMachines;
    }

    private static String fetchEarthquakeData(String string) {

        //Log.v(LOG_TAG, "fetchEarthquakeData called");

        URL url = createUrl(string);

        return makeHttpRequest(url);
    }

    private static URL createUrl(String string) {

        URL url = null;

        try {
            url = new URL(string);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error forming url in createUrl");
        }

        return url;
    }

    private static String makeHttpRequest(URL url) {

        String jsonResponse = "";

        if (url == null)
            return jsonResponse;

        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            connection = (HttpURLConnection) url.openConnection();          //IOException
            Log.v(LOG_TAG, "connect successful");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");                            //Protocol Exception
            connection.connect();                                          //IOException


            if (connection.getResponseCode() == 200) {                        //IOException
                inputStream = connection.getInputStream();                  //IOException
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Response code " + connection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "IOException in makeHttpRequest()");
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e(LOG_TAG, "IOException while closing the inputString in makeHttpRequest");
                }

            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) {
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
                Log.e(LOG_TAG, "IOException in readline, in readFromStream()");
            }
        }

        return output.toString();
    }

}
