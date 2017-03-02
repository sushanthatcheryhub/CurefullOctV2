package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import curefull.healthapp.R;
import fragment.healthapp.FragmentLabTestReport;
import utils.AppPreference;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Filter_Reports_ListAdpter extends RecyclerView.Adapter<Filter_Reports_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<String> healthNoteItemses;
    private String filterNames;
    private FragmentLabTestReport fragmentLabTestReport;

    public Filter_Reports_ListAdpter(FragmentLabTestReport fragmentLabTestReport, Context applicationContexts,
                                     List<String> healthNoteItemses, String filterName) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
        this.filterNames = filterName;
        this.fragmentLabTestReport = fragmentLabTestReport;
    }


    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_filter, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_name = holder.txt_name;
        final CheckedTextView checkBox = holder.checkbox;

        if (filterNames.equalsIgnoreCase("date")) {

            if (AppPreference.getInstance().getFilterDateReports().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

            String date = healthNoteItemses.get(position).toString();
            if (!date.equalsIgnoreCase("")) {
                String[] dateFormat = date.split("-");
                int mYear = Integer.parseInt(dateFormat[0]);
                int mMonth = Integer.parseInt(dateFormat[1]);
                int mDay = Integer.parseInt(dateFormat[2]);
                try {
                    String completeDate = mDay + " " + Utils.formatMonth(String.valueOf(mMonth)) + "," + mYear;
                    checkBox.setText("" + completeDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (filterNames.equalsIgnoreCase("doctor")) {
            checkBox.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterDoctorReports().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        } else if (filterNames.equalsIgnoreCase("uploadBy")) {
            checkBox.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterUploadBy().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        } else if (filterNames.equalsIgnoreCase("disease")) {
            checkBox.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterDieseReports().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!checkBox.isChecked()) {
                    if (filterNames.equalsIgnoreCase("date")) {
                        AppPreference.getInstance().setFilterDateReports(healthNoteItemses.get(position).toString());
                    } else if (filterNames.equalsIgnoreCase("doctor")) {
                        AppPreference.getInstance().setFilterDoctorReports(healthNoteItemses.get(position).toString());
                    } else if (filterNames.equalsIgnoreCase("disease")) {
                        AppPreference.getInstance().setFilterDieseReports(healthNoteItemses.get(position).toString());
                    }else if (filterNames.equalsIgnoreCase("uploadBy")) {
                        AppPreference.getInstance().setFilterUploadBy(healthNoteItemses.get(position).toString());
                    } else {
                    }
                    fragmentLabTestReport.callFilterAgain();
                } else {
                }
                notifyDataSetChanged();
            }
        });

    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;
        public CheckedTextView checkbox;

        ItemViewHolder(View view) {
            super(view);
//            this.txt_name = (TextView) itemView
//                    .findViewById(R.id.txt_filter_name);
            this.checkbox = (CheckedTextView) itemView.findViewById(R.id.checkbox);
        }
    }


}