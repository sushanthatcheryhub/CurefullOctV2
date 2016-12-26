package adpter;

import android.content.Context;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.util.ArrayList;

import curefull.healthapp.R;
import curefull.healthapp.models.Month;
import widgets.MyBarChart;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> implements OnChartValueSelectedListener {


    private ArrayList<Month> horizontalList = null;
    private Context context;
    MyBarChart mCharts;
    protected RectF mOnValueSelectedRectF = new RectF();

    public HorizontalAdapter(ArrayList<Month> horizontalList, Context context) {
        this.horizontalList = horizontalList;
        this.context = context;
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {
        if (e == null)
            return;
        RectF bounds = mOnValueSelectedRectF;
        mCharts.getBarBounds((BarEntry) e, bounds);
        MPPointF position = mCharts.getPosition(e, YAxis.AxisDependency.LEFT);
        Log.e("bounds", bounds.toString());
        Log.e("getData", " " + mCharts.getAxis(YAxis.AxisDependency.LEFT));
        Log.e("x-index",
                "low: " + mCharts.getLowestVisibleX() + ", high: "
                        + mCharts.getHighestVisibleX());

        MPPointF.recycleInstance(position);
    }

    @Override
    public void onNothingSelected() {

    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        MyBarChart mChart;

        public MyViewHolder(View view) {
            super(view);
            mChart = (MyBarChart) view.findViewById(R.id.chart1);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.vertical_menu, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        Month month = horizontalList.get(position);
        if (month == null || month.getDays() == null)
            return;
        this.mCharts = holder.mChart;
        holder.mChart.setOnChartValueSelectedListener(this);
        ArrayList<BarEntry> yVals1 = month.getDays();
        holder.mChart.clear();
        holder.mChart.setGraphProperty(position, 2016);
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
            holder.mChart.setFitBars(true);
            holder.mChart.animateXY(3000, 3000);

            holder.mChart.setData(data);

        }


    }

    private void setData(Month month) {
    }


    @Override
    public int getItemCount() {
        if (horizontalList == null)
            return 0;
        return horizontalList.size();
    }
}