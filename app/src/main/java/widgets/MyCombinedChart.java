package widgets;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;

import com.github.mikephil.charting.charts.CombinedChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.List;

import item.property.GraphYearMonthDeatilsList;
import utils.MyDayAxisValueFormatter;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class MyCombinedChart extends CombinedChart {
    /**
     * @param context
     */
    public MyCombinedChart(Context context) {
        super(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public MyCombinedChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * @param month      0 for jan and so on
     * @param frequencys
     * @param horizontalList
     * @param position
     */
    public void setGraphLineProperty(int month, int year, String frequencys, List<GraphYearMonthDeatilsList> horizontalList, int position) {
        if (isInEditMode())
            return;
        // setOnChartValueSelectedListener(this);

        setDrawBarShadow(false);
        setDrawValueAboveBar(true);
        getDescription().setEnabled(false);
        setMaxVisibleValueCount(10);
        setPinchZoom(false);
        setScrollContainer(false);
        setDrawGridBackground(false);
//        setMinimumWidth(900);
        setDrawMarkerViews(false);
        setDrawMarkers(false);
        setPinchZoom(false);
        setDoubleTapToZoomEnabled(false);
        setScaleEnabled(false);
//        setHighlightPerTapEnabled(false);
        setTouchEnabled(true);

//        getDescription().setEnabled(false);
//        setBackgroundColor(Color.WHITE);
//        setDrawGridBackground(false);
//        setDrawBarShadow(false);
        setHighlightFullBarEnabled(false);

        // draw bars behind lines
//        setDrawOrder(new DrawOrder[]{
//                DrawOrder.BAR, DrawOrder.BUBBLE, DrawOrder.CANDLE, DrawOrder.LINE, DrawOrder.SCATTER
//        });

        Legend l = getLegend();
        l.setWordWrapEnabled(false);
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
        l.setDrawInside(false);
        l.setForm(Legend.LegendForm.NONE);

        YAxis rightAxis = getAxisRight();
        rightAxis.setDrawGridLines(false);
        rightAxis.setDrawLabels(false);
        rightAxis.setEnabled(false);
        rightAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)

        YAxis leftAxis = getAxisLeft();
        leftAxis.setDrawGridLines(false);
        leftAxis.setDrawLabels(true);
        leftAxis.setEnabled(false);
        leftAxis.setAxisMinimum(1f); // this replaces setStartAtZero(true)

        IAxisValueFormatter xAxisFormatter = new MyDayAxisValueFormatter(this, month, year, frequencys,horizontalList,position);
        XAxis xAxis = getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
//        if (frequencys.equalsIgnoreCase("daily")) {
//            xAxis.setAxisMinimum(1f);
//        } else if (frequencys.equalsIgnoreCase("monthly")) {
//            xAxis.setAxisMinimum(1f);
//        } else {
//            xAxis.setAxisMinimum(1f);
//
//        }
        xAxis.setAxisMinimum(0.5f);

        xAxis.setGranularityEnabled(true);
        xAxis.setDrawGridLines(false);
        if (frequencys.equalsIgnoreCase("daily")) {
            xAxis.setLabelCount(18);
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            xAxis.setLabelCount(12);
        } else {
            xAxis.setLabelCount(6);
        }
        xAxis.setGranularityEnabled(true);
        xAxis.setValueFormatter(xAxisFormatter);

    }
}
