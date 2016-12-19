package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.Random;

import curefull.healthapp.R;
import curefull.healthapp.models.Month;
import widgets.MyBarChart;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class HorizontalAdapter1 extends RecyclerView.Adapter<HorizontalAdapter1.MyViewHolder> {


    private ArrayList<BarEntry> horizontalList = null;
    private Context context;


    public HorizontalAdapter1(ArrayList<Month> horizontalList, Context context) {
        this.horizontalList = new ArrayList<>();

        Random rnd = new Random();
        for (int i = 0; i < 366; i++) {
            int randomNum = rnd.nextInt((3000 - 500) + 1) + 500;
            this.horizontalList.add(new BarEntry(i + 1, randomNum));

        }


        this.context = context;
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
       /* Month month = horizontalList.get(position);
        if (month == null || month.getDays() == null)
            return;
        ArrayList<BarEntry> yVals1 = month.getDays();
*/

        BarDataSet set1;
        /*if (holder.mChart.getData() != null &&
                holder.mChart.getData().getDataSetCount1() > 0) {
            set1 = (BarDataSet) holder.mChart.getData().getDataSetByIndex(0);
            set1.setValues(yVals1);
            holder.mChart.getData().notifyDataChanged();
            holder.mChart.notifyDataSetChanged();
        } else */
        {
            set1 = new BarDataSet(horizontalList, "");
            set1.setColors(ColorTemplate.MATERIAL_COLORS);

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);

            BarData data = new BarData(dataSets);
            data.setValueTextSize(10f);
//            data.setValueTypeface(mTfLight);
            data.setBarWidth(0.4f);
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
        return 1;
    }
}