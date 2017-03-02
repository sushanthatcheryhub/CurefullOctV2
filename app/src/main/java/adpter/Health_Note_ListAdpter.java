package adpter;

import android.content.Context;
import android.os.AsyncTask;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentHealthNote;
import interfaces.IOnOtpDoneDelete;
import item.property.HealthNoteItems;
import operations.DbOperations;
import sticky.header.StickyListHeadersAdapter;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;

public class Health_Note_ListAdpter extends BaseAdapter implements
        StickyListHeadersAdapter, IOnOtpDoneDelete {
    Context applicationContext;
    List<HealthNoteItems> healthNoteItemses;
    private RequestQueue requestQueue;
    FragmentHealthNote fragmentHealthNotes;

    public Health_Note_ListAdpter(Context applicationContexts,
                                  List<HealthNoteItems> patientList, FragmentHealthNote fragmentHealthNote) {
        this.healthNoteItemses = patientList;
        this.applicationContext = applicationContexts;
        this.fragmentHealthNotes = fragmentHealthNote;
    }

    @Override
    public int getCount() {
        if (healthNoteItemses == null) {
            return 0;
        }

        return healthNoteItemses.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return healthNoteItemses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adpter_health_note_new,
                    null);
            holder = new ViewHolder();
//            holder.recent_date = (TextView) convertView.findViewById(R.id.recent_date);

            holder.txt_date_time = (TextView) convertView
                    .findViewById(R.id.txt_date_time);
            holder.txt_title = (TextView) convertView
                    .findViewById(R.id.txt_title);
//            holder.txt_deatils=(TextView)convertView.findViewById(R.id.txt_deatils);
//            holder.txt_deatils = (TextView) convertView
//                    .findViewById(R.id.txt_deatils);
            holder.img_delete = (LinearLayout) convertView.findViewById(R.id.img_delete);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        String dateTime = healthNoteItemses.get(position).getNote_date();
        String[] dateParts = dateTime.split("-");
        String years = dateParts[0];
        String months = dateParts[1];
        String days = dateParts[2];
        String times = healthNoteItemses.get(position).getNote_time();
        String[] dateParts1 = times.split(":");
        String hrs = dateParts1[0];
        String mins = dateParts1[1];
//        try {
//            holder.txt_date_time.setText("" + CureFull.getInstanse().getActivityIsntanse().formatMonth(months) + " " + days + "-" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }

        if (healthNoteItemses.get(position).getNote_to_time().equalsIgnoreCase("null") || healthNoteItemses.get(position).getNote_to_time().equalsIgnoreCase("")) {
            try {
                holder.txt_date_time.setText("" + days + " " + Utils.formatMonth(months) + "\n" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            String times1 = healthNoteItemses.get(position).getNote_to_time();
            String[] dateParts11 = times1.split(":");
            String hrs1 = dateParts11[0];
            String mins1 = dateParts11[1];
            try {
                holder.txt_date_time.setText("" + days + " " + Utils.formatMonth(months) + "\n" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)) + " to " + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String text = "<font color=#fdb832>" + healthNoteItemses.get(position).getNote_heading() + ":" + "</font> <font color=#ffffff>" + healthNoteItemses.get(position).getDeatils() + "</font>";
        holder.txt_title.setText(Html.fromHtml(text));

        holder.img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (healthNoteItemses.get(position).getIs_offline() == 1) {
                        DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Health Note ?", "Health Note", position);
                        dialogDeleteAll.setiOnOtpDoneDelete(Health_Note_ListAdpter.this);
                        dialogDeleteAll.show();
                    } else {
                        Toast.makeText(applicationContext, "Your in Off-Line Mode Can't Delete", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Health Note ?", "Health Note", position);
                    dialogDeleteAll.setiOnOtpDoneDelete(Health_Note_ListAdpter.this);
                    dialogDeleteAll.show();
                }


            }
        });


        if (position == healthNoteItemses.size() - 1) {
            fragmentHealthNotes.callWebServiceAgain(healthNoteItemses.size());
        }

        return convertView;
    }

    @Override
    public View getHeaderView(int position, View convertView, ViewGroup parent) {
        HeaderViewHolder holder;
        LayoutInflater mInflater = (LayoutInflater) applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            holder = new HeaderViewHolder();
            convertView = mInflater.inflate(R.layout.header, parent, false);
            holder.text = (TextView) convertView.findViewById(R.id.recent_date);
            convertView.setTag(holder);
        } else {
            holder = (HeaderViewHolder) convertView.getTag();
        }
        String date = "Year - " + healthNoteItemses.get(position).getNote_year();
        holder.text.setText(date.subSequence(0, 11));
        return convertView;
    }

    @Override
    public long getHeaderId(int position) {
        String date = "Year - " + healthNoteItemses.get(position).getNote_year();
        return date.subSequence(0, 11).charAt(10);
    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                getAllHealthListRemove(healthNoteItemses.get(pos).getNote_id(), pos);
            } else {
                DbOperations.deleteNoteListing(healthNoteItemses.get(pos).getNote_id());
                healthNoteItemses.remove(pos);
                notifyDataSetChanged();
                fragmentHealthNotes.checkSize();
            }

        }

    }

    public static class ViewHolder {
        public TextView txt_date_time;
        public TextView txt_title, txt_deatils;
        public LinearLayout img_delete;

    }


    public String formatMonth(String month) throws ParseException {

        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    class HeaderViewHolder {
        TextView text;
    }

    private void getAllHealthListRemove(final int id, final int postis) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        }
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.HEALTH_LIST_DELETE + id,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        Log.e("getHealthAdpter, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            DbOperations.deleteNoteListing(id);
                            healthNoteItemses.remove(postis);
                            notifyDataSetChanged();
                            fragmentHealthNotes.checkSize();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}