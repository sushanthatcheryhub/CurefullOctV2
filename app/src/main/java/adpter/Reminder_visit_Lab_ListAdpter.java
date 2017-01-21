package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.Lab_Test_Reminder_DoctorListView;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_visit_Lab_ListAdpter extends RecyclerView.Adapter<Reminder_visit_Lab_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<Lab_Test_Reminder_DoctorListView> healthNoteItemses;

    public Reminder_visit_Lab_ListAdpter(Context applicationContexts,
                                         List<Lab_Test_Reminder_DoctorListView> healthNoteItemses) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_lab_test_doctor_reminder_list, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_med_name = holder.txt_med_name;
        TextView txt_med_time = holder.txt_med_time;

        txt_med_time.setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
        txt_med_name.setText("" + healthNoteItemses.get(position).getRemMedicineName());
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_med_time;

        ItemViewHolder(View view) {
            super(view);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
        }
    }


}