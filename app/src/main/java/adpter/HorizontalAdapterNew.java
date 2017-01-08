package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.CombinedData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import curefull.healthapp.models.Month;
import fragment.healthapp.FragmentHealthAppNew;
import item.property.GraphYearMonthDeatilsList;
import widgets.MyBarChart;
import widgets.MyCombinedChart;
import widgets.MyLineChart;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class HorizontalAdapterNew extends RecyclerView.Adapter<HorizontalAdapterNew.MyViewHolder> implements OnChartValueSelectedListener {


    private List<GraphYearMonthDeatilsList> horizontalList;
    private Context context;
    private MyCombinedChart mCharts;
    private FragmentHealthAppNew fragmentHealthAppNews;
    private ArrayList<BarEntry> days;
    private ArrayList<Entry> daysLine;
    private String frequencys;
    private int pos;

    public HorizontalAdapterNew(ArrayList<GraphYearMonthDeatilsList> graphViewDetailses, Context context, FragmentHealthAppNew fragmentHealthAppNew, String frequency) {
        this.horizontalList = graphViewDetailses;
        this.context = context;
        this.fragmentHealthAppNews = fragmentHealthAppNew;
        this.frequencys = frequency;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        IAxisValueFormatter xAxisValueFormatte;
        xAxisValueFormatte = mCharts.getXAxis().getValueFormatter();
        String stepsValue = String.valueOf((int) e.getY());

        Log.e("e","e "+e.getX());
        int water=((int)e.getX())-1;
        Log.e("water","water "+water);
        try {
            String waterValue=horizontalList.get(CureFull.getInstanse().getPostionGet()).getGraphViewDetailses().get(water).getWaterIntake();
            Log.e("real ", "pos" + CureFull.getInstanse().getPostionGet()+" water "+horizontalList.get(CureFull.getInstanse().getPostionGet()).getGraphViewDetailses().get((int)e.getX()).getWaterIntake());
            Log.e("getData", " " + pos + " " + xAxisValueFormatte.getFormattedValue(e.getX(), null) + " " + e.getY());
            fragmentHealthAppNews.valueFromGrpah(stepsValue,waterValue);
        }catch (Exception e1){

        }

    }

    @Override
    public void onNothingSelected() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyCombinedChart mCharts;
//        MyLineChart mChart1;

        public MyViewHolder(View view) {
            super(view);
            mCharts = (MyCombinedChart) view.findViewById(R.id.chart1);
//            if (frequencys.equalsIgnoreCase("daily")) {
//                mCharts.setMinimumWidth(900);
//            } else if (frequencys.equalsIgnoreCase("monthly")) {
//                mCharts.setMinimumWidth(900);
//            } else {
//                mCharts.setMinimumWidth(250);
//            }
//            mChart1 = (MyLineChart) view.findViewById(R.id.chart11);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
//        if (horizontalList == null || horizontalList.size() > 0)
//            return;

//        getbargraph(holder.mChart, horizontalList, position);
//        getlinegraph(holder.mChart1, horizontalList, position);

        CombinedData data = new CombinedData();

        data.setData(generateBarData(horizontalList, position));
        data.setData(generateLineData(horizontalList, position));
//        data.setData(generateBubbleData());
//        data.setData(generateScatterData());
//        data.setData(generateCandleData());
//        data.setValueTypeface(mTfLight);

//        xAxis.setAxisMaximum(data.getXMax() + 0.25f);
        holder.mCharts.clear();
        this.mCharts = holder.mCharts;
        this.pos = position;
        holder.mCharts.setGraphLineProperty(Integer.parseInt(horizontalList.get(position).getMonths()), 2016, frequencys, horizontalList, position);
//        holder.mCharts.setGraphProperty(Integer.parseInt(month.get(position).getMonths()), 2016);
        holder.mCharts.setOnChartValueSelectedListener(this);
        holder.mCharts.setData(data);
        holder.mCharts.animateXY(3000, 3000);
        holder.mCharts.invalidate();

    }


    @Override
    public int getItemCount() {
        if (horizontalList == null) {
            return 0;
        }
        return horizontalList.size();
    }


    public void getbargraph(MyBarChart mChart, List<GraphYearMonthDeatilsList> month, int position) {
        mChart.setOnChartValueSelectedListener(this);
        days = new ArrayList<>();
        if (frequencys.equalsIgnoreCase("daily")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                String[] dateParts = dateTime.split("-");
                String years = dateParts[0];
                String months = dateParts[1];
                String days1 = dateParts[2];
                days.add(new BarEntry(Integer.parseInt(days1), count));
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                days.add(new BarEntry(Integer.parseInt(dateTime), count));
            }
        }

        ArrayList<BarEntry> yVals1 = days;
        mChart.clear();
        mChart.setGraphProperty(Integer.parseInt(month.get(position).getMonths()), 2016);
        BarDataSet set1;

      /*  if (holder.mChart.getData() != null &&
                holder.mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) holder.mChart.getData().getDataSetByIndex(position);
            set1.setValues(yVals1);
            holder.mChart.getData().notifyDataChanged();
            holder.mChart.notifyDataSetChanged();
        } else*/
        {
            set1 = new BarDataSet(yVals1, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);
            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);

//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.9f);
            mChart.setFitBars(false);
            mChart.getXAxis().setAxisMaximum(data.getXMax() + 0.25f);
            mChart.animateXY(3000, 3000);
            mChart.setData(data);
        }
    }


    public void getlinegraph(MyLineChart mChart, List<GraphYearMonthDeatilsList> month, int position) {
        daysLine = new ArrayList<>();
        for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
            int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
            String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
            String[] dateParts = dateTime.split("-");
            String years = dateParts[0];
            String months = dateParts[1];
            String days1 = dateParts[2];
            daysLine.add(new Entry(Integer.parseInt(days1), count));
        }
        ArrayList<Entry> yVals1 = daysLine;
        mChart.clear();
        mChart.setGraphLineProperty(Integer.parseInt(month.get(position).getMonths()), 2016);

        LineDataSet set1;


      /*  if (holder.mChart.getData() != null &&
                holder.mChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) holder.mChart.getData().getDataSetByIndex(position);
            set1.setValues(yVals1);
            holder.mChart.getData().notifyDataChanged();
            holder.mChart.notifyDataSetChanged();
        } else*/
        {
            set1 = new LineDataSet(yVals1, "");
            set1.setAxisDependency(YAxis.AxisDependency.LEFT);
            set1.setColor(Color.WHITE);
            set1.setCircleColor(Color.WHITE);
            set1.setLineWidth(0.9f);
            set1.setCircleRadius(3f);
            set1.setFillAlpha(65);
            set1.setFillColor(ColorTemplate.getHoloBlue());
            set1.setHighLightColor(Color.rgb(244, 117, 117));
            set1.setDrawCircleHole(false);
            LineData data = new LineData(set1);
            data.setValueTextColor(Color.WHITE);
            data.setValueTextSize(9f);
            data.setDrawValues(false);
            mChart.getXAxis().setAxisMaximum(data.getXMax() + 0.25f);
            mChart.animateX(3000);
            // set data
            mChart.setData(data);

        }
    }


    private LineData generateLineData(List<GraphYearMonthDeatilsList> month, int position) {


        daysLine = new ArrayList<>();
        if (frequencys.equalsIgnoreCase("daily")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                String[] dateParts = dateTime.split("-");
                String years = dateParts[0];
                String months = dateParts[1];
                String days1 = dateParts[2];
                daysLine.add(new Entry(Integer.parseInt(days1), count));
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                daysLine.add(new Entry(Integer.parseInt(dateTime), count));
            }
        } else {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
//                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
//                String[] dateParts = dateTime.split("to");
//                String day1 = dateParts[0];
//                String day2 = dateParts[1];
//
//                String[] real1 = day1.split("-");
//                String years = real1[0];
//                String months = real1[1];
//                String days1 = real1[2];
//
//                String[] real2 = day2.split("-");
//                String years1 = real2[0];
//                String months1 = real2[1];
//                String days2 = real2[2];
//
//                String all = days1.trim() + days2.trim();
                daysLine.add(new BarEntry(i + 1, count));
            }
        }


        LineData d = new LineData();

//        ArrayList<Entry> entries = new ArrayList<Entry>();
//
//        for (int index = 0; index < itemcount; index++)
//            entries.add(new Entry(index + 0.5f, getRandom(25, 25)));

        LineDataSet set = new LineDataSet(daysLine, "");
        set.setColor(Color.rgb(240, 238, 70));
        set.setColor(Color.WHITE);
        set.setCircleColor(Color.WHITE);
        set.setLineWidth(0.9f);
        set.setCircleRadius(3f);
        set.setFillAlpha(65);
        set.setFillColor(ColorTemplate.getHoloBlue());
        set.setHighlightEnabled(false);
        set.setDrawCircleHole(false);
        set.setLineWidth(2.5f);
        set.setDrawValues(false);
        set.setValueTextSize(10f);
        set.setValueTextColor(Color.rgb(240, 238, 70));
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        d.addDataSet(set);

        return d;
    }

    private BarData generateBarData(List<GraphYearMonthDeatilsList> month, int position) {


        days = new ArrayList<>();
        if (frequencys.equalsIgnoreCase("daily")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                String[] dateParts = dateTime.split("-");
                String years = dateParts[0];
                String months = dateParts[1];
                String days1 = dateParts[2];
                days.add(new BarEntry(Integer.parseInt(days1), count));
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
                days.add(new BarEntry(Integer.parseInt(dateTime), count));
            }
        } else {
            for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
                int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
//                String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
//                String[] dateParts = dateTime.split("to");
//                String day1 = dateParts[0];
//                String day2 = dateParts[1];
//
//                String[] real1 = day1.split("-");
//                String years = real1[0];
//                String months = real1[1];
//                String days1 = real1[2];
//
//                String[] real2 = day2.split("-");
//                String years1 = real2[0];
//                String months1 = real2[1];
//                String days2 = real2[2];
//
//                String all = days1.trim() + days2.trim();
//                Log.e("all", " " + all);
                days.add(new BarEntry(i + 1, count));
            }
        }


//        ArrayList<BarEntry> entries1 = new ArrayList<BarEntry>();
//        ArrayList<BarEntry> entries2 = new ArrayList<BarEntry>();
//
//        for (int index = 0; index < itemcount; index++) {
//            entries1.add(new BarEntry(0, getRandom(25, 25)));
//
//            // stacked
//            entries2.add(new BarEntry(0, new float[]{getRandom(13, 12), getRandom(13, 12)}));
//        }

        BarDataSet set1 = new BarDataSet(days, "");
        set1.setColors(ColorTemplate.MATERIAL_COLORS);
        set1.setValueTextColor(Color.rgb(60, 220, 78));
        set1.setValueTextSize(10f);
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setHighlightEnabled(true);
        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        BarDataSet set2 = new BarDataSet(entries2, "");
//        set2.setStackLabels(new String[]{"Stack 1", "Stack 2"});
//        set2.setColors(new int[]{Color.rgb(61, 165, 255), Color.rgb(23, 197, 255)});
//        set2.setValueTextColor(Color.rgb(61, 165, 255));
//        set2.setValueTextSize(10f);
//        set2.setAxisDependency(YAxis.AxisDependency.LEFT);

        float barWidth = 0.9f;
        // (0.45 + 0.02) * 2 + 0.06 = 1.00 -> interval per "group"

        BarData d = new BarData(set1);
        d.setBarWidth(barWidth);
        // make this BarData object grouped
//        d.groupBars(0, groupSpace, barSpace); // start at x = 0

        return d;
    }
}