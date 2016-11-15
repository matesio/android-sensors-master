package com.example.hammad.androidsensorsgraph;

/**
 * Created by hammad on 10/23/16.
 */

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import au.com.bytecode.opencsv.CSVWriter;
import com.example.hammad.androidsensorsgraph.R;

public class MyService extends Service implements SensorEventListener {

    private SensorManager mSensorManager;
    // private SensorManager GSensorManager;

    private Sensor mAccelerometer;
    private Sensor mGyroScope;
    private Sensor mMagento;
    public float x, y, z;
    private String values;
    boolean sensorAvailable = false;
    ArrayList sensorData = new ArrayList();
    private float xG, yG, zG;
    private float xM, yM, zM;
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    static String MY_ACTION="MY_ACTION";
    private String res;
  // com.example.hammad.androidsensorsgraph.R.AccelData data;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void onCreate() {
        super.onCreate();
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandlerThread = new HandlerThread("LocalServiceThread");
                mHandlerThread.start();

                mHandler = new Handler(mHandlerThread.getLooper());
            }
        }).start();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            mGyroScope = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, mGyroScope, SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            mMagento = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this, mMagento, SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    @Override
    public void onSensorChanged(final SensorEvent event) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Sensor oSensor = event.sensor;
                if (oSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                    x = event.values[0];
                    y = event.values[1];
                    z = event.values[2];
                } else if (oSensor.getType() == Sensor.TYPE_GYROSCOPE) {
                    xG = event.values[0];
                    yG = event.values[1];
                    yM = event.values[2];
                } else if (oSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                    xM = event.values[0];
                    yM = event.values[1];
                    zM = event.values[2];
                }

                long timeStamp = System.currentTimeMillis();
                long startTime = timeStamp;
                long timeStampAb = timeStamp - startTime;
                //data = new com.example.hammad.androidsensorsgraph.AccelData(timeStampAb, x, y, z, xG, yG, zG, xM, yM, zM);
               // sensorData.add(data.toString());  //added data to the list
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                Log.d("Time", currentDateandTime);
                res = String.valueOf(currentDateandTime + "#" + x) + "#" + String.valueOf(y) + "#" + String.valueOf(z) + "#"
                        + String.valueOf(xG) + "#" + String.valueOf(yG) + "#" + String.valueOf(zG) + "#" + String.valueOf(xM) + "#"
                        + String.valueOf(yM) + "#" + String.valueOf(zM) + "#";
                Log.d("test", res);
                // Toast.makeText(this, "Still running as a test service", Toast.LENGTH_SHORT)
                //       .show();
                //A.AsyncTaskClass
                new AsyncTaskClass().execute();
            }
        }).start();

    }
    //    else if(GSensorManager!=null)
    protected String getAccelerometerReading() {
        return values = String.format("%7f" + ", %7f" + ", %7f", x, y, z);
    }
    protected void printValues() {
        Toast.makeText(this, "values" + values, Toast.LENGTH_SHORT)
                .show();
    }
    public class AsyncTaskClass extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            publishProgress("Sleeping...");
            CSVWriter writer = null;
            try {
                //Log.d("check","pasla");
                //Environment.getExternalStorageDirectory().getPath();
                writer = new CSVWriter(new FileWriter("/sdcard/AsyncClasss.csv", true), ',');
                String[] entries = res.split("#"); // array of your values
                writer.writeNext(entries);
                //FileWriter
                writer.close();
            } catch (IOException e) {
                //error

            }
            return res;
        }
        protected void onPostExecute(String result) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    // int i =12;
                    intent.putExtra("DATAPASSED", res);
                    sendBroadcast(intent);
                }
            }).start();
            // execution of result of Long time consuming operation
            //result_view.setText("Data Stored!");

        }

        protected void onProgressUpdate()
        {

        }
    }
}


