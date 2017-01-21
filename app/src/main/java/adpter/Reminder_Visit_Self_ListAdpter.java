package adpter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentDoctorVisitSetReminder;
import item.property.Doctor_Visit_Reminder_SelfListView;
import item.property.Lab_Test_Reminder_SelfListView;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_Visit_Self_ListAdpter extends RecyclerView.Adapter<Reminder_Visit_Self_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<Doctor_Visit_Reminder_SelfListView> healthNoteItemses;

    public Reminder_Visit_Self_ListAdpter(Context applicationContexts,
                                          List<Doctor_Visit_Reminder_SelfListView> healthNoteItemses) {
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
        ImageView img_edit_rem = holder.img_edit_rem;

        txt_med_time.setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
        txt_med_name.setText("Dr. " + healthNoteItemses.get(position).getDoctorName());
        txt_hospital.setText("" + healthNoteItemses.get(position).getRemMedicineName());

        img_edit_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("doctorFollowupReminderId", healthNoteItemses.get(position).getDoctorFollowupReminderId());
                bundle.putString("doctorName", "" + healthNoteItemses.get(position).getDoctorName());
                bundle.putString("hospitalName", "" + healthNoteItemses.get(position).getRemMedicineName());
                bundle.putString("time", "" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
                bundle.putString("date", "" + (healthNoteItemses.get(position).getDate() < 10 ? "0" + healthNoteItemses.get(position).getDate() : healthNoteItemses.get(position).getDate()) + "/" + (healthNoteItemses.get(position).getMonth() < 10 ? "0" + healthNoteItemses.get(position).getMonth() : healthNoteItemses.get(position).getMonth()) + "/" + healthNoteItemses.get(position).getYear());
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentDoctorVisitSetReminder(), bundle, true);
            }
        });

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name, txt_hospital;
        public TextView txt_med_time;
        public ImageView img_edit_rem;

        ItemViewHolder(View view) {
            super(view);
            this.img_edit_rem = (ImageView) itemView.findViewById(R.id.img_edit_rem);
            this.txt_hospital = (TextView) itemView
                    .findViewById(R.id.txt_hospital);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
        }
    }


}