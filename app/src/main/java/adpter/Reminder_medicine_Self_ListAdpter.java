package adpter;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentDoctorVisitSetReminder;
import fragment.healthapp.FragmentHealthNote;
import fragment.healthapp.FragmentReminderSetMedicine;
import interfaces.IOnOtpDoneDelete;
import item.property.HealthNoteItems;
import item.property.Reminder_SelfListView;
import utils.AppPreference;
import utils.CustomTypefaceSpan;
import utils.MyConstants;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_medicine_Self_ListAdpter extends RecyclerView.Adapter<Reminder_medicine_Self_ListAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<Reminder_SelfListView> healthNoteItemses;
    private RequestQueue requestQueue;
    private String checking;

    public Reminder_medicine_Self_ListAdpter(Context applicationContexts,
                                             List<Reminder_SelfListView> healthNoteItemses, String check) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_med_reminder__self_list, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_med_name = holder.txt_med_name;
        TextView txt_meal = holder.txt_meal;
        TextView txt_med_time = holder.txt_med_time;
        final ImageView img_edit_rem = holder.img_edit_rem;
        final ImageView img_editm_delete = holder.img_editm_delete;
        final CheckBox checkBox = holder.checkbox;
        if (checking.equalsIgnoreCase("No")) {
            img_edit_rem.setVisibility(View.VISIBLE);
            img_editm_delete.setVisibility(View.VISIBLE);
            checkBox.setVisibility(View.VISIBLE);
        } else {
            img_edit_rem.setVisibility(View.GONE);
            img_editm_delete.setVisibility(View.GONE);
            checkBox.setVisibility(View.GONE);
        }

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
        } else {
            txt_meal.setText("Before Meal");
        }


        img_editm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_editm_delete);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected medicine reminder ?", "Medicine", position);
                dialogDeleteAll.setiOnOtpDoneDelete(Reminder_medicine_Self_ListAdpter.this);
                dialogDeleteAll.show();

            }
        });


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
                bundle.putInt("quantity", healthNoteItemses.get(position).getQuantity());
                bundle.putString("noOfDays", "" + healthNoteItemses.get(position).getNoOfDays());
                bundle.putInt("interval", healthNoteItemses.get(position).getInterval());
                bundle.putString("noOfDosage", "" + healthNoteItemses.get(position).getNoOfDosage());
                bundle.putString("type", "" + healthNoteItemses.get(position).getType());
                bundle.putBoolean("beforeMeal", healthNoteItemses.get(position).isBeforeMeal());
                bundle.putBoolean("afterMeal", healthNoteItemses.get(position).isAfterMeal());
                bundle.putString("date", "" + (healthNoteItemses.get(position).getDate() < 10 ? "0" + healthNoteItemses.get(position).getDate() : healthNoteItemses.get(position).getDate()) + "/" + (healthNoteItemses.get(position).getMonth() < 10 ? "0" + healthNoteItemses.get(position).getMonth() : healthNoteItemses.get(position).getMonth()) + "/" + healthNoteItemses.get(position).getYear());
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentReminderSetMedicine(), bundle, true);
            }
        });


        if (healthNoteItemses.get(position).getStatus().equalsIgnoreCase("deactivate")) {
            checkBox.setChecked(false);
        } else {
            checkBox.setChecked(true);
        }


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    Log.e("check", ":- isChecked");
                    getDoctorVisitDelete(healthNoteItemses.get(position).getMedicineReminderId(), position, false, true);
                } else {
                    getDoctorVisitDelete(healthNoteItemses.get(position).getMedicineReminderId(), position, false, false);
                    Log.e("check", ":- not");
//                    healthNoteItemses.get(position).setSelected(false);
                }

            }
        });


        txt_med_name.setSelected(true);

    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getDoctorVisitDelete(healthNoteItemses.get(pos).getMedicineReminderId(), pos, true, false);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_meal;
        public TextView txt_med_time;
        public ImageView img_edit_rem, img_editm_delete;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.img_editm_delete = (ImageView) itemView.findViewById(R.id.img_editm_delete);
            this.img_edit_rem = (ImageView) itemView.findViewById(R.id.img_edit_rem);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_meal = (TextView) itemView
                    .findViewById(R.id.txt_meal);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.chkStateStep);
        }
    }

    private void getDoctorVisitDelete(String id, final int pos, final boolean isDeleted, boolean isOn) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.MEDICINCE_DELETE_ + id + "&isDeleted=" + isDeleted + "&isOn=" + isOn,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("Doctor Delete, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            if (isDeleted) {
                                healthNoteItemses.remove(pos);
                                notifyDataSetChanged();
                            }

                        } else {
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

}