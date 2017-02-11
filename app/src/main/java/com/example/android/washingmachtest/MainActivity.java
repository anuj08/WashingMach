package com.example.android.washingmachtest;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<ArrayList<WashingMachine>> {

    public static final String LOG_TAG = MainActivity.class.getName();
    private static final String Washing_INFO_RESPONSE = "http://anuj08.5gbfree.com/getWmInfo3.php";
    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        SharedPreferences settings = getSharedPreferences("prefs", 0);
        boolean firstRun = settings.getBoolean("firstRun", true);
        boolean loginSuccess = settings.getBoolean("loginSuccess", true);
        if (firstRun || !loginSuccess) {
            // here run your first-time instructions, for example :
            Intent login = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(login);
            finish();

        }
        //Log.v(LOG_TAG," onCreate(); called");
        setContentView(R.layout.activity_main);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());
        LoaderManager loaderManager = getLoaderManager();
        loaderManager.initLoader(1, null, this);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }

    @Override
    public Loader<ArrayList<WashingMachine>> onCreateLoader(int id, Bundle args) {

        //Log.v(LOG_TAG, "onCreateLoader has been called");
        return new VMLoader(this, Washing_INFO_RESPONSE);

    }

    @Override
    public void onLoadFinished(Loader<ArrayList<WashingMachine>> loader, ArrayList<WashingMachine> washingMachines) {
        //Log.v(LOG_TAG, "onLoadFinished has been called");
        setDisplay(washingMachines);


        ConnectivityManager cm =
                (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null &&
                activeNetwork.isConnectedOrConnecting();

        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progress_bar);
        progressBar.setVisibility(View.GONE);

        if (!isConnected) {
            TextView textView = (TextView) findViewById(R.id.no_net);
            textView.setText(R.string.no_net);

            TextView textview1 = (TextView) findViewById(R.id.mach1);
            textview1.setVisibility(View.GONE);

            TextView textview2 = (TextView) findViewById(R.id.mach2);
            textview2.setVisibility(View.GONE);

            TextView textview3 = (TextView) findViewById(R.id.mach3);
            textview3.setVisibility(View.GONE);

            TextView textview4 = (TextView) findViewById(R.id.mach4);
            textview4.setVisibility(View.GONE);

            TextView textview5 = (TextView) findViewById(R.id.mach5);
            textview5.setVisibility(View.GONE);

            TextView textview6 = (TextView) findViewById(R.id.mach6);
            textview6.setVisibility(View.GONE);

            TextView textview7 = (TextView) findViewById(R.id.mach7);
            textview7.setVisibility(View.GONE);

            TextView textview8 = (TextView) findViewById(R.id.mach8);
            textview8.setVisibility(View.GONE);


        }

    }

    @Override
    public void onLoaderReset(Loader<ArrayList<WashingMachine>> loader) {
        //Log.v(LOG_TAG, "onLoaderReset has been called");

    }

    private void setDisplay(ArrayList<WashingMachine> washingMachines) {

        if (washingMachines.isEmpty()) {
            return;
        } else
            for (int i = 0; i < washingMachines.size(); i++) {
                WashingMachine washingMachine = washingMachines.get(i);
                //Log.v("damn","Washing machine id: " + washingMachine.getWashingMachineId() + "\nTime left: " + washingMachine.getTimeLeft() + " washing state " + washingMachine.getState());
//                Log.v(LOG_TAG, washingMachine.getTimeLeft() + "");
//                Log.v(LOG_TAG,washingMachine.getUserName());
//                Log.v(LOG_TAG,washingMachine.getUserRoom());
            /*
            Color scheme : passing three means time left greater than 0, passing two means time up, passing 1 means vacant
             */

                if (washingMachine.getState()) {           // when false means no user
                    if (washingMachine.getTimeLeft() > 0) {
                        fixColor(3, washingMachine.getWashingMachineId(), washingMachine);
                        Log.v(LOG_TAG, i + " WashingM is red");
                    } else {
                        fixColor(2, washingMachine.getWashingMachineId(), washingMachine);
                        //Log.v(LOG_TAG,i + " WashingM is red");
                    }
                } else {
                    fixColor(1, washingMachine.getWashingMachineId(), washingMachine);
                    //Log.v(LOG_TAG,i + " WashingM is red");
                }
            }

    }

    private void fixColor(int colorIndex, int machineNo, WashingMachine washingMachine) {

        int color;

        switch (colorIndex) {
            case 1:
                color = ContextCompat.getColor(this, R.color.magnitude1);
                break;
            case 2:
                color = ContextCompat.getColor(this, R.color.magnitude2);
                break;
            case 3:
                color = ContextCompat.getColor(this, R.color.magnitude3);
                break;
            default:
                color = ContextCompat.getColor(this, R.color.blank);

        }

        switch (machineNo) {
            case 11:
                TextView textview1 = (TextView) findViewById(R.id.mach1);
                GradientDrawable magnitudeCircle1 = (GradientDrawable) textview1.getBackground();
                magnitudeCircle1.setColor(color);
                setClick(colorIndex, washingMachine, textview1);
                ImageView imageView = (ImageView) findViewById(R.id.image);
                imageView.setImageResource(R.drawable.test);
                break;
            case 12:
                TextView textview2 = (TextView) findViewById(R.id.mach2);
                GradientDrawable magnitudeCircle2 = (GradientDrawable) textview2.getBackground();
                magnitudeCircle2.setColor(color);
                setClick(colorIndex, washingMachine, textview2);
                break;
            case 21:
                TextView textview3 = (TextView) findViewById(R.id.mach3);
                GradientDrawable magnitudeCircle3 = (GradientDrawable) textview3.getBackground();
                magnitudeCircle3.setColor(color);
                setClick(colorIndex, washingMachine, textview3);
                break;
            case 22:
                TextView textview4 = (TextView) findViewById(R.id.mach4);
                GradientDrawable magnitudeCircle4 = (GradientDrawable) textview4.getBackground();
                magnitudeCircle4.setColor(color);
                setClick(colorIndex, washingMachine, textview4);
                break;
            case 41:
                TextView textview5 = (TextView) findViewById(R.id.mach5);
                GradientDrawable magnitudeCircle5 = (GradientDrawable) textview5.getBackground();
                magnitudeCircle5.setColor(color);
                setClick(colorIndex, washingMachine, textview5);
                break;
            case 42:
                TextView textview6 = (TextView) findViewById(R.id.mach6);
                GradientDrawable magnitudeCircle6 = (GradientDrawable) textview6.getBackground();
                magnitudeCircle6.setColor(color);
                setClick(colorIndex, washingMachine, textview6);
                break;
            case 51:
                TextView textview7 = (TextView) findViewById(R.id.mach7);
                GradientDrawable magnitudeCircle7 = (GradientDrawable) textview7.getBackground();
                magnitudeCircle7.setColor(color);
                setClick(colorIndex, washingMachine, textview7);
                textview7.setVisibility(View.GONE);
                break;
            case 52:
                TextView textview8 = (TextView) findViewById(R.id.mach8);
                GradientDrawable magnitudeCircle8 = (GradientDrawable) textview8.getBackground();
                magnitudeCircle8.setColor(color);
                setClick(colorIndex, washingMachine, textview8);
                textview8.setVisibility(View.GONE);
                break;
        }
    }

    private void setClick(int color, final WashingMachine washingMachine, TextView view) {

        switch (color) {
            case 1:
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        showTimePickerDialog(view, washingMachine);
                        //Log.v(LOG_TAG, "came back");
                    }
                });

                break;
            case 2:
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        SharedPreferences uid = getSharedPreferences("uid", 0);

                        long UID = uid.getLong("uid", MODE_PRIVATE);

                        if (washingMachine.getmUserId() == UID) {
                            CustomDialougeClass cdd = new CustomDialougeClass(MainActivity.this, washingMachine);
                            cdd.show();
                        } else {
                            Intent UserInfo = new Intent(MainActivity.this, UserInfo.class);
                            UserInfo.putExtra("WMInfo", washingMachine);
                            startActivity(UserInfo);
                        }

                    }
                });
                break;
            case 3:
                view.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        Context context1 = getApplicationContext();
                        CharSequence text1 = washingMachine.getUserName() + "\n" + washingMachine.getUserRoom() + "\nTime left: " + formatTime(washingMachine);
                        int duration1 = Toast.LENGTH_SHORT;

                        Toast toast1 = Toast.makeText(context1, text1, duration1);
                        toast1.show();
                    }
                });
                break;


        }
    }

    public void showTimePickerDialog(View v, WashingMachine washingMachine) {
        DialogFragment newFragment = new TimePickerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable("WMInfo", washingMachine);
        newFragment.setArguments(bundle);
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }

    private String formatTime(WashingMachine washingMachine) {
        long timeInsecs = washingMachine.getTimeLeft();
        long hours = timeInsecs / 3600;
        long minutes = (timeInsecs - hours * 3600) / 60 + 1;

        return "" + hours + " hours " + minutes + " minutes";
    }

    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onFling(MotionEvent event1, MotionEvent event2,
                               float velocityX, float velocityY) {

            float sensitivityDown = 700;
            float sensitivitySwipe = 50;
            if (event2.getY() - event1.getY() > sensitivityDown) {
                Intent intent1 = new Intent(MainActivity.this, MainActivity.class);
                startActivity(intent1);
                finish();
            }
            ConnectivityManager cm =
                    (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
            boolean isConnected = activeNetwork != null &&
                    activeNetwork.isConnectedOrConnecting();
            if (isConnected) {
                ImageView imageView = (ImageView) findViewById(R.id.image);

                if (event1.getX() - event2.getX() > sensitivitySwipe) {
                    TextView textview1 = (TextView) findViewById(R.id.mach1);
                    textview1.setVisibility(View.GONE);

                    TextView textview2 = (TextView) findViewById(R.id.mach2);
                    textview2.setVisibility(View.GONE);

                    TextView textview3 = (TextView) findViewById(R.id.mach3);
                    textview3.setVisibility(View.GONE);

                    TextView textview4 = (TextView) findViewById(R.id.mach4);
                    textview4.setVisibility(View.GONE);

                    TextView textview5 = (TextView) findViewById(R.id.mach5);
                    textview5.setVisibility(View.GONE);

                    TextView textview6 = (TextView) findViewById(R.id.mach6);
                    textview6.setVisibility(View.GONE);

                    TextView textview7 = (TextView) findViewById(R.id.mach7);
                    textview7.setVisibility(View.VISIBLE);

                    TextView textview8 = (TextView) findViewById(R.id.mach8);
                    textview8.setVisibility(View.VISIBLE);

                    imageView.setImageResource(R.drawable.test2);
                } else if (event2.getX() - event1.getX() > sensitivitySwipe) {
                    TextView textview1 = (TextView) findViewById(R.id.mach1);
                    textview1.setVisibility(View.VISIBLE);

                    TextView textview2 = (TextView) findViewById(R.id.mach2);
                    textview2.setVisibility(View.VISIBLE);

                    TextView textview3 = (TextView) findViewById(R.id.mach3);
                    textview3.setVisibility(View.VISIBLE);

                    TextView textview4 = (TextView) findViewById(R.id.mach4);
                    textview4.setVisibility(View.VISIBLE);

                    TextView textview5 = (TextView) findViewById(R.id.mach5);
                    textview5.setVisibility(View.VISIBLE);

                    TextView textview6 = (TextView) findViewById(R.id.mach6);
                    textview6.setVisibility(View.VISIBLE);

                    TextView textview7 = (TextView) findViewById(R.id.mach7);
                    textview7.setVisibility(View.GONE);

                    TextView textview8 = (TextView) findViewById(R.id.mach8);
                    textview8.setVisibility(View.GONE);

                    imageView.setImageResource(R.drawable.test);
                }
            }
            return true;
        }

    }

}