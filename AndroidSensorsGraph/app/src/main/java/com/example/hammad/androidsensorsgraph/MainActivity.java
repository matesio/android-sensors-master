package com.example.hammad.androidsensorsgraph;
/**
 * Created by hammad on 10/23/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.androidplot.Plot;
import com.androidplot.util.PlotStatistics;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.FastLineAndPointRenderer;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.RectRegion;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import java.text.DecimalFormat;
import java.util.Arrays;
import static java.lang.Thread.sleep;
public class MainActivity extends AppCompatActivity {
    private static final int HISTORY_SIZE = 1000;
    MyReceiver myReceiver;
    String[] arrayData;
    private XYPlot aprLevelsPlot = null;
    private XYPlot aprHistoryPlot = null;
    private SimpleXYSeries accelerometerSeriesx = null, accelerometerSeriesy = null, accelerometerSeriesz = null;
    private SimpleXYSeries gyroscopeSeriesx = null, gyroscopeSeriesy = null, gyroscopeSeriesz = null;
    private SimpleXYSeries magnetometerSeriesx = null, magnetometerSeriesy = null, magnetometerSeriesz = null;
    private Redrawer redrawer;

    private PanZoom panZoom;
    private Spinner panSpinner;
    private Spinner zoomSpinner;
    private PointF minXY;
    private PointF maxXY;
    private static final int SERIES_ALPHA = 255;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        aprHistoryPlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
        accelerometerSeriesx = new SimpleXYSeries("xa");
        accelerometerSeriesx.useImplicitXVals();
        accelerometerSeriesy = new SimpleXYSeries("ya");
        accelerometerSeriesy.useImplicitXVals();
        accelerometerSeriesz= new SimpleXYSeries("za");
        accelerometerSeriesz.useImplicitXVals();

        gyroscopeSeriesx = new SimpleXYSeries("xg");
        gyroscopeSeriesx.useImplicitXVals();
        gyroscopeSeriesy = new SimpleXYSeries("yg");
        gyroscopeSeriesy.useImplicitXVals();
        gyroscopeSeriesz = new SimpleXYSeries("zg");
        gyroscopeSeriesz.useImplicitXVals();
        magnetometerSeriesx = new SimpleXYSeries("xm");
        magnetometerSeriesx.useImplicitXVals();
        magnetometerSeriesy = new SimpleXYSeries("ym");
        magnetometerSeriesy.useImplicitXVals();
        magnetometerSeriesz = new SimpleXYSeries("zm");
        magnetometerSeriesz.useImplicitXVals();
        aprHistoryPlot.setRangeBoundaries(-5, 5, BoundaryMode.FIXED);
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        aprHistoryPlot.addSeries(accelerometerSeriesx,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 0, 255), null, null, null));
        aprHistoryPlot.addSeries(accelerometerSeriesy,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 255, 0), null, null, null));
        aprHistoryPlot.addSeries(accelerometerSeriesz,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255, 0, 0), null, null, null));
        aprHistoryPlot.addSeries(gyroscopeSeriesx,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255,255,0), null, null, null));
        aprHistoryPlot.addSeries(gyroscopeSeriesy,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0,255,255), null, null, null));
        aprHistoryPlot.addSeries(gyroscopeSeriesz,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255,0,255), null, null, null));
        aprHistoryPlot.addSeries(magnetometerSeriesx,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(128,0,0), null, null, null));
        aprHistoryPlot.addSeries(magnetometerSeriesy,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(128,128,0), null, null, null));
        aprHistoryPlot.addSeries(magnetometerSeriesz,
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0,128,0), null, null, null));
        aprHistoryPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        aprHistoryPlot.setDomainStepValue(HISTORY_SIZE);
        aprHistoryPlot.setLinesPerRangeLabel(3);
        aprHistoryPlot.setDomainLabel("");
        aprHistoryPlot.getDomainTitle().pack();
        aprHistoryPlot.setRangeLabel("");
        aprHistoryPlot.getRangeTitle().pack();
        aprHistoryPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.LEFT).
                setFormat(new DecimalFormat("#"));
        aprHistoryPlot.getGraph().getLineLabelStyle(XYGraphWidget.Edge.BOTTOM).
                setFormat(new DecimalFormat("#"));
        final PlotStatistics histStats = new PlotStatistics(100, false);
        aprHistoryPlot.addListener(histStats);
     //   histStats.setAnnotatePlotEnabled(true);
        redrawer = new Redrawer(
                Arrays.asList(new Plot[]{aprHistoryPlot}),
                100, false);
        aprHistoryPlot.animate();
        final RectRegion bounds = aprHistoryPlot.getBounds();
        minXY = new PointF(bounds.getMinX().floatValue(), bounds.getMinY().floatValue());
        maxXY = new PointF(bounds.getMaxX().floatValue(), bounds.getMaxY().floatValue());
        panZoom = PanZoom.attach(aprHistoryPlot);
    }
    private void initSpinners() {
        panSpinner.setAdapter(
                new ArrayAdapter<>(this, R.layout.spinner_item, PanZoom.Pan.values()));
        panSpinner.setSelection(panZoom.getPan().ordinal());
        panSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                panZoom.setPan(PanZoom.Pan.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing to do
            }
        });

        zoomSpinner.setAdapter(
                new ArrayAdapter<>(this, R.layout.spinner_item, PanZoom.Zoom.values()));
        zoomSpinner.setSelection(panZoom.getZoom().ordinal());
        zoomSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                panZoom.setZoom(PanZoom.Zoom.values()[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // nothing to do
            }
        });
    }



    public void onResume() {
        super.onResume();
        redrawer.start();
    }
    @Override
    public void onPause() {
        redrawer.pause();
        super.onPause();
    }
    @Override
    public void onDestroy() {
        redrawer.finish();
        super.onDestroy();
    }

    protected void onStart() {
        super.onStart();
        new Thread(new Runnable() {
            @Override
            public void run() {
                myReceiver = new MyReceiver();
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction(com.example.hammad.androidsensorsgraph.MyService.MY_ACTION);
                registerReceiver(myReceiver, intentFilter);
                Intent intent = new Intent(MainActivity.this, com.example.hammad.androidsensorsgraph.MyService.class);
                //Start Service
                startService(intent);
            }
        }).start();
    }
    public void generateit(final float x, final float y, final float z, final float xG, final float yG, final float zG, final float xM, final float yM, final float zM, final float timeStamp) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if ( accelerometerSeriesx.size() > HISTORY_SIZE) {
                    accelerometerSeriesx.removeFirst();
                    accelerometerSeriesy.removeFirst();
                    accelerometerSeriesz.removeFirst();
                    gyroscopeSeriesx.removeFirst();
                    gyroscopeSeriesy.removeFirst();
                    gyroscopeSeriesz.removeFirst();
                    magnetometerSeriesx.removeFirst();
                    magnetometerSeriesy.removeFirst();
                    magnetometerSeriesz.removeFirst();
                }
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accelerometerSeriesx.addLast(timeStamp, x/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accelerometerSeriesy.addLast(timeStamp, y/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                accelerometerSeriesz.addLast(timeStamp, z/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gyroscopeSeriesx.addLast(timeStamp, xG/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gyroscopeSeriesy.addLast(timeStamp, yG/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                gyroscopeSeriesz.addLast(timeStamp, zG/20);
                try {
                    sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                magnetometerSeriesx.addLast(timeStamp, xM/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                magnetometerSeriesy.addLast(timeStamp, yM/20);
                try {
                    sleep(0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                magnetometerSeriesz.addLast(timeStamp, zM/20);
            }
        }).start();
    }
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        unregisterReceiver(myReceiver);
        super.onStop();
    }
    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context arg0, final Intent arg1) {
            // TODO Auto-generated method stub
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String data = arg1.getStringExtra("DATAPASSED");
                    arrayData = data.split("#");
                    float x = Float.parseFloat(arrayData[1]);
                    float y = Float.parseFloat(arrayData[2]);
                    float z = Float.parseFloat(arrayData[3]);
                    float xG = Float.parseFloat(arrayData[4]);
                    float yG = Float.parseFloat(arrayData[5]);
                    float zG = Float.parseFloat(arrayData[6]);
                    float xM = Float.parseFloat(arrayData[7]);
                    float yM = Float.parseFloat(arrayData[8]);
                    float zM = Float.parseFloat(arrayData[9]);
                    String time = arrayData[0].replaceAll(":", "");
                    final float timeStamp = (float) ((Float.parseFloat(time)) / 1000.0);
///            aprHistoryPlot.redraw();
                    generateit(x, y, z, xG, yG, zG, xM, yM, zM, timeStamp);
                }
            }).start();

        }
    }
}