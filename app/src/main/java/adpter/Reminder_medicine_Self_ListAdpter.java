package adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentHealthNote;
import item.property.HealthNoteItems;
import item.property.Reminder_SelfListView;
import utils.CustomTypefaceSpan;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_medicine_Self_ListAdpter extends RecyclerView.Adapter<Reminder_medicine_Self_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<Reminder_SelfListView> healthNoteItemses;

    public Reminder_medicine_Self_ListAdpter(Context applicationContexts,
                                             List<Reminder_SelfListView> healthNoteItemses) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_med_reminder__self_list, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_med_name = holder.txt_med_name;
        TextView txt_meal = holder.txt_meal;
        TextView txt_med_time = holder.txt_med_time;

        txt_med_name.setText("" + healthNoteItemses.get(position).getRemMedicineName().trim());

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
        if (med.endsWith(" | ")) {
            Log.e("time", "" + med);
            med = med.substring(0, med.length() - 2);
        }

        txt_med_time.setText("" + med);
        if (healthNoteItemses.get(position).isAfterMeal()) {
            txt_meal.setText("After Meal");
        }
        if (healthNoteItemses.get(position).isBeforeMeal()) {
            txt_meal.setText("Before Meal");
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_meal;
        public TextView txt_med_time;

        ItemViewHolder(View view) {
            super(view);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_meal = (TextView) itemView
                    .findViewById(R.id.txt_meal);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
        }
    }


}