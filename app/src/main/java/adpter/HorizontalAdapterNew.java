package adpter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
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

import curefull.healthapp.R;
import curefull.healthapp.models.Month;
import fragment.healthapp.FragmentHealthAppNew;
import item.property.GraphYearMonthDeatilsList;
import widgets.MyBarChart;
import widgets.MyLineChart;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class HorizontalAdapterNew extends RecyclerView.Adapter<HorizontalAdapterNew.MyViewHolder> implements OnChartValueSelectedListener {


    private List<GraphYearMonthDeatilsList> horizontalList;
    private Context context;
    private MyBarChart mCharts;
    private FragmentHealthAppNew fragmentHealthAppNews;
    private ArrayList<BarEntry> days;
    private ArrayList<Entry> daysLine;

    public HorizontalAdapterNew(ArrayList<GraphYearMonthDeatilsList> graphViewDetailses, Context context, FragmentHealthAppNew fragmentHealthAppNew) {
        this.horizontalList = graphViewDetailses;
        this.context = context;
        this.fragmentHealthAppNews = fragmentHealthAppNew;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        IAxisValueFormatter xAxisValueFormatte;
        xAxisValueFormatte = mCharts.getXAxis().getValueFormatter();
        String stepsValue = String.valueOf(e.getY());
        Log.e("getData", " " + xAxisValueFormatte.getFormattedValue(e.getX(), null) + " " + e.getY());
        fragmentHealthAppNews.valueFromGrpah(stepsValue);
    }

    @Override
    public void onNothingSelected() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyBarChart mChart;
        MyLineChart mChart1;

        public MyViewHolder(View view) {
            super(view);
            mChart = (MyBarChart) view.findViewById(R.id.chart1);
            mChart1 = (MyLineChart) view.findViewById(R.id.chart11);
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
        this.mCharts = holder.mChart;
        getbargraph(holder.mChart, horizontalList, position);
        getlinegraph(holder.mChart1, horizontalList, position);

    }

    private void setData(Month month) {
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
        for (int i = 0; i < month.get(position).getGraphViewDetailses().size(); i++) {
            int count = Integer.parseInt(month.get(position).getGraphViewDetailses().get(i).getCount());
            String dateTime = month.get(position).getGraphViewDetailses().get(i).getDate();
            String[] dateParts = dateTime.split("-");
            String years = dateParts[0];
            String months = dateParts[1];
            String days1 = dateParts[2];
            days.add(new BarEntry(Integer.parseInt(days1), count));
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
}