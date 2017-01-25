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
import fragment.healthapp.FragmentReminderSetMedicine;
import item.property.Reminder_DoctorListView;
import item.property.Reminder_SelfListView;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_doctor_ListAdpter extends RecyclerView.Adapter<Reminder_doctor_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<Reminder_DoctorListView> healthNoteItemses;
    private String checking;
    public Reminder_doctor_ListAdpter(Context applicationContexts,
                                      List<Reminder_DoctorListView> healthNoteItemses, String check) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
        this.checking = check;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_med_reminder_list, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_med_name = holder.txt_med_name;
        TextView txt_meal = holder.txt_meal;
        TextView txt_med_time = holder.txt_med_time;
        final ImageView img_edit_rem = holder.img_edit_rem;

        if (checking.equalsIgnoreCase("No")) {
            img_edit_rem.setVisibility(View.VISIBLE);
        } else {
            img_edit_rem.setVisibility(View.GONE);
        }


        String med = "";
        String[] test = healthNoteItemses.get(position).getTimeToTake().trim().split(",");
        if (test != null && test.length > 0) {
            for (int i = 0; i < test.length; i++) {
                String[] dateParts11 = test[i].split(":");
                String hrs1 = dateParts11[0];
                String mins1 = dateParts11[1];
                med += CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)) + " | ";
            }
        }
        txt_med_time.setText("" + med);
        txt_med_name.setText("" + healthNoteItemses.get(position).getRemMedicineName());
        if (healthNoteItemses.get(position).isAfterMeal()) {
            txt_meal.setText("After Meal");
        }
        if (healthNoteItemses.get(position).isBeforeMeal()) {
            txt_meal.setText("Before Meal");
        }

        img_edit_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_edit_rem);
                Bundle bundle = new Bundle();
                bundle.putString("medicineReminderId", healthNoteItemses.get(position).getMedicineReminderId());
                bundle.putString("doctorName", "" + healthNoteItemses.get(position).getDoctorName());
                bundle.putString("medicineName", "" + healthNoteItemses.get(position).getRemMedicineName());
                bundle.putString("timeToTakeMedicne", "" + healthNoteItemses.get(position).getTimeToTake());
                bundle.putString("noOfDaysInWeek", "" + healthNoteItemses.get(position).getNoOfDaysInWeek());
                bundle.putBoolean("beforeMeal", healthNoteItemses.get(position).isBeforeMeal());
                bundle.putBoolean("afterMeal", healthNoteItemses.get(position).isAfterMeal());
                bundle.putString("date", "" + (healthNoteItemses.get(position).getDate() < 10 ? "0" + healthNoteItemses.get(position).getDate() : healthNoteItemses.get(position).getDate()) + "/" + (healthNoteItemses.get(position).getMonth() < 10 ? "0" + healthNoteItemses.get(position).getMonth() : healthNoteItemses.get(position).getMonth()) + "/" + healthNoteItemses.get(position).getYear());
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentReminderSetMedicine(), bundle, true);
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_meal;
        public TextView txt_med_time;
        public ImageView img_edit_rem;

        ItemViewHolder(View view) {
            super(view);
            this.img_edit_rem = (ImageView) itemView.findViewById(R.id.img_edit_rem);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_meal = (TextView) itemView
                    .findViewById(R.id.txt_meal);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
        }
    }


}