package widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import utils.MyDayAxisValueFormatter;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class MyLineChart extends LineChart {
    /**
     * @param context
     */
    public MyLineChart(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyLineChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param month 0 for jan and so on
     */
    public void setGraphLineProperty(int month, int year) {
        if (isInEditMode())
            return;
        // setOnChartValueSelectedListener(this);

        getDescription().setEnabled(false);

        // enable touch gestures
        setTouchEnabled(false);

        setDragDecelerationFrictionCoef(0.9f);

        // enable scaling and dragging
        setDragEnabled(false);
        setScaleEnabled(false);
        setDrawGridBackground(false);
        setHighlightPerDragEnabled(false);

        // if disabled, scaling can be done on x- and y-axis separately
        setPinchZoom(false);

        // set an alternative background color
//        setBackgroundColor(Color.LTGRAY);

        // add data

//        animateX(2500);

        // get the legend (only possible after setting data)
        Legend l = getLegend();

        // modify the legend ...
        l.setForm(Legend.LegendForm.NONE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
//        l.setYOffset(11f);


//        IAxisValueFormatter xAxisFormatter = new MyDayAxisValueFormatter(this, month, year, frequencys);
        XAxis xAxis = getXAxis();
        xAxis.setTextSize(11f);
        xAxis.setTextColor(Color.WHITE);

        xAxis.setDrawLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
//        xAxis.setValueFormatter(xAxisFormatter);


        YAxis leftAxis = getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
//        leftAxis.setAxisMaximum(200f);
        leftAxis.setDrawLabels(false);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setDrawAxisLine(false);
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularityEnabled(true);

        YAxis rightAxis = getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setDrawAxisLine(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawZeroLine(false);
        rightAxis.setGranularityEnabled(false);

    }
}
