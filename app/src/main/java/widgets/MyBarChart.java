//package widgets;
//
//import android.content.Context;
//import android.util.AttributeSet;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.components.Legend;
//import com.github.mikephil.charting.components.XAxis;
//import com.github.mikephil.charting.components.YAxis;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//
//import graph.MyAxisValueFormatter;
//import graph.XYMarkerView;
//import utils.MyDayAxisValueFormatter;
//
///**
// * Created by Sushant Hatcheryhub on 18-12-2016.
// */
//
//public class MyBarChart extends BarChart {
//    /**
//     * @param context
//     */
//    public MyBarChart(Context context) {
//        super(context);
//    }
//
//    /**
//     * @param context
//     * @param attrs
//     */
//    public MyBarChart(Context context, AttributeSet attrs) {
//        super(context, attrs);
//    }
//
//    /**
//     * @param month 0 for jan and so on
//     */
//    public void setGraphProperty(int month, int year) {
//        if (isInEditMode())
//            return;
//        // setOnChartValueSelectedListener(this);
//
//        setDrawBarShadow(false);
//        setDrawValueAboveBar(true);
//        getDescription().setEnabled(false);
//        setMaxVisibleValueCount(10);
//        setPinchZoom(false);
//        setScrollContainer(false);
//        setDrawGridBackground(false);
//        //setBorderColor(Color.BLACK);
//
//
////        IAxisValueFormatter xAxisFormatter = new MyDayAxisValueFormatter(this, month, year, frequencys);
//
//        XAxis xAxis = getXAxis();
//        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
////        xAxis.setTypeface(mTfLight);
//        xAxis.setDrawGridLines(false);
//        xAxis.setGranularityEnabled(false);
//        xAxis.setGranularity(0f);
//        xAxis.setLabelCount(16);
////        xAxis.setValueFormatter(xAxisFormatter);
//
//        IAxisValueFormatter custom = new MyAxisValueFormatter();
//
//        YAxis leftAxis = getAxisLeft();
////        leftAxis.setTypeface(mTfLight);
//        leftAxis.setLabelCount(8, false);
//        leftAxis.setDrawLabels(true);
//        leftAxis.setEnabled(false);
//        leftAxis.setValueFormatter(custom);
//        leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
//        // This two will remove the background Grid
//
//        getAxisLeft().setDrawGridLines(false);
//        getXAxis().setDrawGridLines(false);
//
//        // Disabling the Zoom Feature, actually I disabled the touch,
//        // if you can't touch then obviously the zoom is disabled.
//
//        setTouchEnabled(true);
//        setDrawMarkers(false);
//        setPinchZoom(false);
//        setDoubleTapToZoomEnabled(false);
//        setScaleEnabled(false);
//
//        YAxis rightAxis = getAxisRight();
//        rightAxis.setDrawGridLines(false);
//
////        rightAxis.setTypeface(mTfLight);
//        rightAxis.setDrawLabels(false);
//        rightAxis.setValueFormatter(custom);
//        rightAxis.setEnabled(false);
////        rightAxis.setSpaceTop(15f);
//        rightAxis.setAxisMinimum(0f);
//
//
//        Legend l = getLegend();
//        l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
//        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
//        l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
//        l.setDrawInside(false);
//        l.setForm(Legend.LegendForm.NONE);
//        l.setTextSize(11f);
//
////        XYMarkerView mv = new XYMarkerView(getContext(), xAxisFormatter);
////        mv.setChartView(this);
////        setMarker(mv);
//
//    }
//}
