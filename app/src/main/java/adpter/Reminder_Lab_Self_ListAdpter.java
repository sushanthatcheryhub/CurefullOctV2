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
import item.property.Lab_Test_Reminder_SelfListView;
import item.property.Reminder_SelfListView;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_Lab_Self_ListAdpter extends RecyclerView.Adapter<Reminder_Lab_Self_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<Lab_Test_Reminder_SelfListView> healthNoteItemses;

    public Reminder_Lab_Self_ListAdpter(Context applicationContexts,
                                        List<Lab_Test_Reminder_SelfListView> healthNoteItemses) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_lab_test_reminder_self_list, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_med_name = holder.txt_med_name;
        TextView txt_med_time = holder.txt_med_time;
        TextView txt_hospital = holder.txt_hospital;
        txt_med_time.setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
        txt_med_name.setText("" + healthNoteItemses.get(position).getRemMedicineName());
//        if (healthNoteItemses.get(position).isAfterMeal()) {
//            txt_hospital.setText("After Meal");
//        }
//        if (healthNoteItemses.get(position).isBeforeMeal()) {
//            txt_hospital.setText("Before Meal");
//        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_med_time;
        public TextView txt_hospital;

        ItemViewHolder(View view) {
            super(view);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
            this.txt_hospital = (TextView) itemView.findViewById(R.id.txt_hospital);
        }
    }


}