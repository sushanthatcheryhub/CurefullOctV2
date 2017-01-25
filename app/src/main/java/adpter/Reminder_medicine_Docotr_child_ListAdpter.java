package adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.LinearLayoutManager;
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
import item.property.ReminderDoctorName;
import item.property.Reminder_DoctorListView;
import utils.CustomTypefaceSpan;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_medicine_Docotr_child_ListAdpter extends RecyclerView.Adapter<Reminder_medicine_Docotr_child_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<ReminderDoctorName> healthNoteItemses;
    private String checking;
    public Reminder_medicine_Docotr_child_ListAdpter(Context applicationContexts,
                                                     List<ReminderDoctorName> healthNoteItemses, String check) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_doctor_reminder, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_doctor_self = holder.txt_doctor_self;
        RecyclerView recyclerView = holder.recyclerView_doctor;
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(applicationContext);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setHasFixedSize(true);
        txt_doctor_self.setText("Prescribed by - Dr." + healthNoteItemses.get(position).getDoctorName());
        Reminder_doctor_ListAdpter adapter = new Reminder_doctor_ListAdpter(applicationContext, healthNoteItemses.get(position).getReminder_doctorListViews(),checking);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_doctor_self;
        public RecyclerView recyclerView_doctor;

        ItemViewHolder(View view) {
            super(view);
            this.txt_doctor_self = (TextView) itemView
                    .findViewById(R.id.txt_doctor_self);
            this.recyclerView_doctor = (RecyclerView) itemView
                    .findViewById(R.id.recyclerView_doctor);
        }
    }


}