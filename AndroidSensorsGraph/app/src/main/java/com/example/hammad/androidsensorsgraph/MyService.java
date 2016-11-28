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
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import au.com.bytecode.opencsv.CSVWriter;
public class MyService extends Service implements SensorEventListener {
    private SensorManager mSensorManager;
    private Sensor sensors[];   //0-2 accelerometer 3-5 gyroscope 6-8 magnetometer
    float array[]= new float[9];
    private HandlerThread mHandlerThread;
    private Handler mHandler;
    static String MY_ACTION="MY_ACTION";
    private String res;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    public void onCreate() {
        super.onCreate();
        sensors = new Sensor [3];

                mHandlerThread = new HandlerThread("LocalServiceThread");
                mHandlerThread.start();
                mHandler = new Handler(mHandlerThread.getLooper());
            }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
            sensors[0] = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            mSensorManager.registerListener(this, sensors[0] , SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE) != null) {
            sensors[1]  = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
            mSensorManager.registerListener(this, sensors[1] , SensorManager.SENSOR_DELAY_NORMAL);
        }

        if (mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD) != null) {
            sensors[2]  = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
            mSensorManager.registerListener(this,  sensors[2] , SensorManager.SENSOR_DELAY_NORMAL);
        }
        return START_STICKY;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    @Override
    public void onSensorChanged(final SensorEvent event) {
                Sensor oSensor = event.sensor;
        synchronized (this ) {
            if (oSensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                array[0] = event.values[0];
                array[1] = event.values[1];
                array[2] = event.values[2];
            } else if (oSensor.getType() == Sensor.TYPE_GYROSCOPE) {
                array[3] = event.values[0];
                array[4] = event.values[1];
                array[5] = event.values[2];
            } else if (oSensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                array[6] = event.values[0];
                array[7] = event.values[1];
                array[8] = event.values[2];
            }
        }
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String currentDateandTime = sdf.format(new Date());
                res = String.valueOf(currentDateandTime + "#" + array[0]) + "#" + String.valueOf( array[1]) + "#" + String.valueOf( array[2]) + "#"
                        + String.valueOf( array[3]) + "#" + String.valueOf( array[4]) + "#" + String.valueOf( array[5]) + "#" + String.valueOf( array[6]) + "#"
                        + String.valueOf( array[7]) + "#" + String.valueOf( array[8]) + "#";
                new AsyncTaskClass().execute();
    }
    public class AsyncTaskClass extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            CSVWriter writer = null;
            try {
                //Log.d("check","pasla");
                //Environment.getExternalStorageDirectory().getPath();



                    writer = new CSVWriter(new FileWriter("/sdcard/AsyncClasss.csv", true), ',');
                    String[] entries = res.split("#"); // array of your values
                    writer.writeNext(entries);
                    writer.close();

            } catch (IOException e) {
                //error
            }
            return res;
        }
        protected void onPostExecute(String result) {

                    float[] a= new float[9];
                    int i=0;
                        for(float x:a)
                        {
                            a[i]=array[i];
                            i++;
                        }
                    Intent intent = new Intent();
                    intent.setAction(MY_ACTION);
                    intent.putExtra("DATAPASSED", a);
                    sendBroadcast(intent);
                }
            }

        protected void onProgressUpdate()
        {
        }
    }

