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
import java.text.SimpleDateFormat;
import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentHealthNote;
import item.property.HealthNoteItems;
import utils.CustomTypefaceSpan;
import utils.Utils;

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
        LinearLayout linearLayout = holder.liner_bottom;

        String dateTime = healthNoteItemses.get(position).getNote_date();
        String[] dateParts = dateTime.split("-");
        String years = dateParts[0];
        String months = dateParts[1];
        String days = dateParts[2];
        String times = healthNoteItemses.get(position).getNote_time();
        String[] dateParts1 = times.split(":");
        String hrs = dateParts1[0];
        String mins = dateParts1[1];

        if (healthNoteItemses.get(position).getNote_to_time().equalsIgnoreCase("null")) {
            try {
                Log.e("time", "" + hrs + ":- " + mins);
                txt_date_time.setText("" + days + " " + Utils.formatMonth(months) + "-" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            String times1 = healthNoteItemses.get(position).getNote_to_time();
            String[] dateParts11 = times1.split(":");
            String hrs1 = dateParts11[0];
            String mins1 = dateParts11[1];

            Log.e("hi", "hi" + times1);
            try {
                txt_date_time.setText("" + days + " " + Utils.formatMonth(months) + "\n" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)) + " to " + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String name = healthNoteItemses.get(position).getNote_heading();
        String comma = " : ";
        String gameName = healthNoteItemses.get(position).getDeatils();

        String meassgeTxt = name + comma + gameName;

        Spannable sb = new SpannableString(meassgeTxt);
        Typeface font = Typeface.createFromAsset(applicationContext.getAssets(), "Montserrat-Bold.ttf");
        sb.setSpan(new ForegroundColorSpan(applicationContext.getResources()
                        .getColor(R.color.white)), meassgeTxt.indexOf(name),
                meassgeTxt.indexOf(name) + name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new CustomTypefaceSpan("", font), meassgeTxt.indexOf(name), meassgeTxt.indexOf(name) + name.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                meassgeTxt.indexOf(name),
                meassgeTxt.indexOf(name) + name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(applicationContext.getResources()
                        .getColor(R.color.white)), meassgeTxt.indexOf(comma),
                meassgeTxt.indexOf(comma) + comma.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(applicationContext.getResources()
                        .getColor(R.color.white)), meassgeTxt.indexOf(gameName),
                meassgeTxt.indexOf(gameName) + gameName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_title.setText(sb);
        linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthNote(), true);
            }
        });
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date_time;
        public TextView txt_title;
        public LinearLayout liner_bottom;

        ItemViewHolder(View view) {
            super(view);
            this.liner_bottom = (LinearLayout) itemView.findViewById(R.id.liner_bottom);
            this.txt_date_time = (TextView) itemView
                    .findViewById(R.id.txt_date_time);
            this.txt_title = (TextView) itemView
                    .findViewById(R.id.txt_title);
        }
    }


}