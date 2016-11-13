package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.HealthNoteItems;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Health_Note_Landing_ListAdpter extends RecyclerView.Adapter<Health_Note_Landing_ListAdpter.ItemViewHolder> {


    Context applicationContext;
    List<HealthNoteItems> healthNoteItemses;

    public Health_Note_Landing_ListAdpter(Context applicationContexts,
                                          List<HealthNoteItems> healthNoteItemses) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_health_note_landing, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_date_time = holder.txt_date_time;
        TextView txt_title = holder.txt_title;
        TextView txt_deatils = holder.txt_deatils;

        String dateTime = healthNoteItemses.get(position).getNote_date();
        String[] dateParts = dateTime.split("-");
        String years = dateParts[0];
        String months = dateParts[1];
        String days = dateParts[2];
        String times = healthNoteItemses.get(position).getNote_time();
        String[] dateParts1 = times.split(":");
        String hrs = dateParts1[0];
        String mins = dateParts1[1];
        try {
            txt_date_time.setText("" + CureFull.getInstanse().getActivityIsntanse().formatMonth(months) + " " + days + "-" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        txt_title.setText("" + healthNoteItemses.get(position).getNote_heading());
        txt_deatils.setText("" + healthNoteItemses.get(position).getDeatils());
        txt_deatils.setSelected(true);
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date_time;
        public TextView txt_title;
        public TextView txt_deatils;

        ItemViewHolder(View view) {
            super(view);

            this.txt_date_time = (TextView) itemView
                    .findViewById(R.id.txt_date_time);
            this.txt_title = (TextView) itemView
                    .findViewById(R.id.txt_title);
            this.txt_deatils = (TextView) itemView
                    .findViewById(R.id.txt_deatils);
        }
    }


}