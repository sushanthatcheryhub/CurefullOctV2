package adpter;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentDoctorVisitSetReminder;
import interfaces.IOnOtpDoneDelete;
import item.property.Doctor_Visit_Reminder_SelfListView;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_Visit_Self_ListAdpter extends RecyclerView.Adapter<Reminder_Visit_Self_ListAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<Doctor_Visit_Reminder_SelfListView> healthNoteItemses;
    View rootView;
    public Reminder_Visit_Self_ListAdpter(Context applicationContexts,
                                          List<Doctor_Visit_Reminder_SelfListView> healthNoteItemses, View rootView) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
        this.rootView=rootView;
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
        final ImageView img_edit_rem = holder.img_edit_rem;
        final ImageView img_editm_delete = holder.img_editm_delete;
        final CheckBox checkBox = holder.checkbox;

        txt_med_time.setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
        txt_med_name.setText("Dr. " + healthNoteItemses.get(position).getDoctorName());
        txt_hospital.setText("" + healthNoteItemses.get(position).getRemMedicineName());

        if (healthNoteItemses.get(position).getStatus().equalsIgnoreCase("complete")) {
            img_edit_rem.setVisibility(View.GONE);
            txt_med_time.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            img_edit_rem.setVisibility(View.VISIBLE);
        }

        img_edit_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_edit_rem, 400, 0.9f, 0.9f);
                Bundle bundle = new Bundle();
                bundle.putString("doctorFollowupReminderId", healthNoteItemses.get(position).getDoctorFollowupReminderId());
                bundle.putString("doctorName", "" + healthNoteItemses.get(position).getDoctorName());
                bundle.putString("hospitalName", "" + healthNoteItemses.get(position).getRemMedicineName());
                bundle.putString("time", "" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
                bundle.putString("date", "" + (healthNoteItemses.get(position).getDate() < 10 ? "0" + healthNoteItemses.get(position).getDate() : healthNoteItemses.get(position).getDate()) + "/" + (healthNoteItemses.get(position).getMonth() < 10 ? "0" + healthNoteItemses.get(position).getMonth() : healthNoteItemses.get(position).getMonth()) + "/" + healthNoteItemses.get(position).getYear());
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentDoctorVisitSetReminder(), bundle, true);
            }
        });

        img_editm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_editm_delete, 400, 0.9f, 0.9f);
                    DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected lab doctor visit ?", "Doctor Visit", position);
                    dialogDeleteAll.setiOnOtpDoneDelete(Reminder_Visit_Self_ListAdpter.this);
                    dialogDeleteAll.show();
                }else{
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                }
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
                    getDoctorVisitDelete(healthNoteItemses.get(position).getDoctorFollowupReminderId(), position, false, true);
                    ContentValues cv = new ContentValues();
                    cv.put("doctorFollowupReminderId", healthNoteItemses.get(position).getDoctorFollowupReminderId());
                    cv.put("status", "activate");
                    cv.put("isUploaded","1");
                    DbOperations.insertDoctorRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), cv,  healthNoteItemses.get(position).getDoctorFollowupReminderId());

                } else {
                    getDoctorVisitDelete(healthNoteItemses.get(position).getDoctorFollowupReminderId(), position, false, false);
                    Log.e("check", ":- not");

                    ContentValues cv = new ContentValues();
                    cv.put("doctorFollowupReminderId", healthNoteItemses.get(position).getDoctorFollowupReminderId());
                    cv.put("status", "deactivate");
                    cv.put("isUploaded","1");
                    DbOperations.insertDoctorRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), cv,  healthNoteItemses.get(position).getDoctorFollowupReminderId());
//                    healthNoteItemses.get(position).setSelected(false);
                }

            }
        });


    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getDoctorVisitDelete(healthNoteItemses.get(pos).getDoctorFollowupReminderId(), pos, true, false);
            //delete from local also
            try {
                DbOperations.clearDoctorReminderFromLocal(healthNoteItemses.get(pos).getDoctorFollowupReminderId(), healthNoteItemses.get(pos).getDoctorName(), "doctorreminder");
            }catch (Exception e){
                e.getMessage();
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name, txt_hospital;
        public TextView txt_med_time;
        public ImageView img_edit_rem, img_editm_delete;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.img_editm_delete = (ImageView) itemView.findViewById(R.id.img_editm_delete);
            this.img_edit_rem = (ImageView) itemView.findViewById(R.id.img_edit_rem);
            this.txt_hospital = (TextView) itemView
                    .findViewById(R.id.txt_hospital);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.chkStateStep);
        }
    }

    private void getDoctorVisitDelete(String id, final int pos, final boolean isDeleted, boolean isOn) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DOCTOR_VIsit_DELETE_ + id + "&isDeleted=" + isDeleted + "&isOn=" + isOn,
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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }
}