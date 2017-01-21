package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.R;
import fragment.healthapp.FragmentHealthAppNewProgress;
import item.property.GraphViewDetails;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;
import widgets.MyCombinedChart;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class HorizontalAdapterNewProgress extends RecyclerView.Adapter<HorizontalAdapterNewProgress.MyViewHolder> {


    private List<GraphViewDetails> horizontalList;
    private Context context;
    private FragmentHealthAppNewProgress fragmentHealthAppNews;
    private String frequencys;

    public HorizontalAdapterNewProgress(ArrayList<GraphViewDetails> graphViewDetailses, Context context, FragmentHealthAppNewProgress fragmentHealthAppNew, String frequency) {
        this.horizontalList = graphViewDetailses;
        this.context = context;
        this.fragmentHealthAppNews = fragmentHealthAppNew;
        this.frequencys = frequency;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private ProgressBar vprogressbar;
        private TextView txt_date;

        public MyViewHolder(View view) {
            super(view);
            txt_date = (TextView) view.findViewById(R.id.txt_date);
            vprogressbar = (ProgressBar) view.findViewById(R.id.vprogressbar);
        }
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_graph_progess, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

        holder.vprogressbar.setProgress(Integer.parseInt(horizontalList.get(position).getCount()));

        if (frequencys.equalsIgnoreCase("daily")) {
            String date = horizontalList.get(position).getDate();
            if (!date.equalsIgnoreCase("")) {
                String[] dateFormat = date.split("-");
                int mYear = Integer.parseInt(dateFormat[0]);
                int mMonth = Integer.parseInt(dateFormat[1]);
                int mDay = Integer.parseInt(dateFormat[2]);
                try {
                    String completeDate = mDay + " " + Utils.formatMonth(String.valueOf(mMonth));
                    holder.txt_date.setText("" + completeDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (frequencys.equalsIgnoreCase("monthly")) {
            holder.txt_date.setText(MyConstants.IArrayData.mMonths[Integer.parseInt(horizontalList.get(position).getDate())]);
        } else {
            String dateTime = horizontalList.get(position).getDate();
            String[] dateParts = dateTime.split("to");
            String day1 = dateParts[0];
            String day2 = dateParts[1];

            String[] real1 = day1.split("-");
            String years = real1[0];
            String months = real1[1];
            String days1 = real1[2];

            String[] real2 = day2.split("-");
            String years1 = real2[0];
            String months1 = real2[1];
            String days2 = real2[2];


            try {
                holder.txt_date.setText("" + "(" + days1.trim() + "-" + (days2.trim()) + ") " + Utils.formatMonth(months));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (AppPreference.getInstance().getGraphDate().equalsIgnoreCase(horizontalList.get(position).getDate())) {
            holder.vprogressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progess_bar_selector_new));
        } else {
            holder.vprogressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progess_bar_selector));
        }

        holder.vprogressbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppPreference.getInstance().setGraphDate(horizontalList.get(position).getDate());
                holder.vprogressbar.setProgressDrawable(context.getResources().getDrawable(R.drawable.progess_bar_selector_new));
                fragmentHealthAppNews.valueFromGrpah(horizontalList.get(position).getCount(), horizontalList.get(position).getWaterIntake(), horizontalList.get(position).getCaloriesBurnt());
                notifyDataSetChanged();
            }
        });


    }


    @Override
    public int getItemCount() {
        if (horizontalList == null) {
            return 0;
        }
        return horizontalList.size();
    }


}