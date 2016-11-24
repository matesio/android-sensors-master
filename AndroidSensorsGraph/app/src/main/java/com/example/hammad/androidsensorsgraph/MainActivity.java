package com.example.hammad.androidsensorsgraph;
/**
 * Created by hammad on 10/23/16.
 */
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.androidplot.Plot;
import com.androidplot.util.PlotStatistics;
import com.androidplot.util.Redrawer;
import com.androidplot.xy.BoundaryMode;
import com.androidplot.xy.FastLineAndPointRenderer;
import com.androidplot.xy.PanZoom;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.StepMode;
import com.androidplot.xy.XYGraphWidget;
import com.androidplot.xy.XYPlot;
import java.text.DecimalFormat;
import java.util.Arrays;
public class MainActivity extends AppCompatActivity {
    private static final int HISTORY_SIZE = 100;
    MyReceiver myReceiver;
    private XYPlot aprHistoryPlot = null;
    private SimpleXYSeries[] series;
    private SimpleXYSeries accelerometerSeriesx = null, accelerometerSeriesy = null, accelerometerSeriesz = null;
    private SimpleXYSeries gyroscopeSeriesx = null, gyroscopeSeriesy = null, gyroscopeSeriesz = null;
    private SimpleXYSeries magnetometerSeriesx = null, magnetometerSeriesy = null, magnetometerSeriesz = null;
    private Redrawer redrawer;
    private PanZoom panZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        aprHistoryPlot = (XYPlot) findViewById(R.id.aprHistoryPlot);
        series = new SimpleXYSeries[9];
        series[0] = new SimpleXYSeries("xa");
        series[0].useImplicitXVals();
        series[1] = new SimpleXYSeries("ya");
        series[1].useImplicitXVals();
        series[2] = new SimpleXYSeries("za");
        series[2].useImplicitXVals();

        series[3] = new SimpleXYSeries("xg");
        series[3].useImplicitXVals();
        series[4] = new SimpleXYSeries("yg");
        series[4].useImplicitXVals();
        series[5] = new SimpleXYSeries("zg");
        series[5].useImplicitXVals();
        series[6] = new SimpleXYSeries("xm");
        series[6].useImplicitXVals();
        series[7] = new SimpleXYSeries("ym");
        series[7].useImplicitXVals();
        series[8] = new SimpleXYSeries("zm");
        series[8].useImplicitXVals();
        aprHistoryPlot.setRangeBoundaries(-100, 100, BoundaryMode.FIXED);
        aprHistoryPlot.setDomainBoundaries(0, HISTORY_SIZE, BoundaryMode.FIXED);
        aprHistoryPlot.addSeries(series[0],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 0, 255), null, null, null));
        aprHistoryPlot.addSeries(series[1],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 255, 0), null, null, null));
        aprHistoryPlot.addSeries(series[2],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255, 0, 0), null, null, null));
        aprHistoryPlot.addSeries(series[3],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255, 255, 0), null, null, null));
        aprHistoryPlot.addSeries(series[4],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 255, 255), null, null, null));
        aprHistoryPlot.addSeries(series[5],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(255, 0, 255), null, null, null));
        aprHistoryPlot.addSeries(series[6],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(128, 0, 0), null, null, null));
        aprHistoryPlot.addSeries(series[7],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(128, 128, 0), null, null, null));
        aprHistoryPlot.addSeries(series[8],
                new FastLineAndPointRenderer.Formatter(
                        Color.rgb(0, 128, 0), null, null, null));
        aprHistoryPlot.setDomainStepMode(StepMode.INCREMENT_BY_VAL);
        aprHistoryPlot.setDomainStepValue(HISTORY_SIZE);
        aprHistoryPlot.setLinesPerRangeLabel(1);
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
        histStats.setAnnotatePlotEnabled(true);

        redrawer = new Redrawer(
                Arrays.asList(new Plot[]{aprHistoryPlot}),
                999, true);
        //      redrawer.setMaxRefreshRate((float) 100);
        //.. redrawer.run();
        aprHistoryPlot.animate();
        panZoom = PanZoom.attach(aprHistoryPlot);

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
                startService(intent);
            }
        }).start();
    }

    public void generateit(final float x, final float y, final float z, final float xG, final float yG, final float zG, final float xM, final float yM, final float zM, final float timeStamp) {

                if (series[0].size() > HISTORY_SIZE) {
                    series[0].removeFirst();
                    series[1].removeFirst();
                    series[2].removeFirst();
                    series[3].removeFirst();
                    series[4].removeFirst();
                    series[5].removeFirst();
                    series[6].removeFirst();
                    series[7].removeFirst();
                    series[8].removeFirst();
                }




                series[0].addLast(timeStamp, x);

                series[1].addLast(timeStamp, y);

                series[2].addLast(timeStamp, z);

                series[3].addLast(timeStamp, xG);

                series[4].addLast(timeStamp, yG);

                series[5].addLast(timeStamp, zG);

                series[6].addLast(timeStamp, xM);

                series[7].addLast(timeStamp, yM);

                series[8].addLast(timeStamp, zM);
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


            float data[] = arg1.getFloatArrayExtra("DATAPASSED");
            float[] arr = new float[9];
            arr[0] = data[0];
            arr[1] = data[1];
            arr[3] = data[3];
            arr[4] = data[4];
            arr[5] = data[5];
            arr[6] = data[6];
            arr[7] = data[7];
            arr[8] = data[8];
            long time = System.currentTimeMillis();
            final float timeStamp = (float) ((Float.parseFloat(String.valueOf(time))) / 1000.0);
            generateit(arr[0], arr[1], arr[2], arr[3], arr[4], arr[5], arr[6], arr[7], arr[8], timeStamp);


        }
    }
}