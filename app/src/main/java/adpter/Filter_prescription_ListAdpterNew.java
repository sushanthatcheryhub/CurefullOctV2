package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.text.ParseException;
import java.util.List;

import curefull.healthapp.R;
import fragment.healthapp.FragmentPrescriptionCheck;
import fragment.healthapp.FragmentPrescriptionCheckNew;
import utils.AppPreference;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Filter_prescription_ListAdpterNew extends RecyclerView.Adapter<Filter_prescription_ListAdpterNew.ItemViewHolder> {


    Context applicationContext;
    List<String> healthNoteItemses;
    private RequestQueue requestQueue;
    private String filterNames;
    private FragmentPrescriptionCheckNew prescriptionCheck;

    public Filter_prescription_ListAdpterNew(FragmentPrescriptionCheckNew fragmentPrescriptionCheck, Context applicationContexts,
                                             List<String> healthNoteItemses, String filterName) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
        this.filterNames = filterName;
        this.prescriptionCheck = fragmentPrescriptionCheck;
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
        final CheckBox checkBox = holder.checkbox;

        if (filterNames.equalsIgnoreCase("date")) {

            if (AppPreference.getInstance().getFilterDate().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
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
                    txt_name.setText("" + completeDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        } else if (filterNames.equalsIgnoreCase("doctor")) {
            txt_name.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterDoctor().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        } else if (filterNames.equalsIgnoreCase("disease")) {
            txt_name.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterDiese().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        } else if (filterNames.equalsIgnoreCase("uploadBy")) {
            txt_name.setText("" + healthNoteItemses.get(position).toString());
            if (AppPreference.getInstance().getFilterUploadBy().equalsIgnoreCase(healthNoteItemses.get(position).toString())) {
                checkBox.setChecked(true);
            } else {
                checkBox.setChecked(false);
            }

        } else {
            txt_name.setText("" + healthNoteItemses.get(position).toString());
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("check", ":- right");
                if (checkBox.isChecked()) {
                    Log.e("check", ":- isChecked");
//                    AppPreference.getInstance().setcf_uuhidNeew(healthNoteItemses.get(position).getCfUuhid());

                    if (filterNames.equalsIgnoreCase("date")) {
                        AppPreference.getInstance().setFilterDate(healthNoteItemses.get(position).toString());
                    } else if (filterNames.equalsIgnoreCase("doctor")) {
                        AppPreference.getInstance().setFilterDoctor(healthNoteItemses.get(position).toString());
                    } else if (filterNames.equalsIgnoreCase("disease")) {
                        AppPreference.getInstance().setFilterDiese(healthNoteItemses.get(position).toString());
                    } else if (filterNames.equalsIgnoreCase("uploadBy")) {
                        AppPreference.getInstance().setFilterUploadBy(healthNoteItemses.get(position).toString());
                    } else {
                    }
                    prescriptionCheck.callFilterAgain();
                } else {
                }
                notifyDataSetChanged();
            }
        });

    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_name;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.txt_name = (TextView) itemView
                    .findViewById(R.id.txt_filter_name);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }


}