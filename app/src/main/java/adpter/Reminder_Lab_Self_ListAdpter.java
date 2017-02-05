package adpter;

import android.content.Context;
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
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentDoctorVisitSetReminder;
import fragment.healthapp.FragmentLabTestSetReminder;
import interfaces.IOnOtpDoneDelete;
import item.property.Lab_Test_Reminder_SelfListView;
import item.property.Reminder_SelfListView;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_Lab_Self_ListAdpter extends RecyclerView.Adapter<Reminder_Lab_Self_ListAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<Lab_Test_Reminder_SelfListView> healthNoteItemses;
    private RequestQueue requestQueue;

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
        final ImageView img_edit_rem = holder.img_edit_rem;
        final ImageView img_editm_delete = holder.img_editm_delete;
        final CheckBox checkBox = holder.checkbox;


        txt_med_time.setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
        txt_med_name.setText("" + healthNoteItemses.get(position).getRemMedicineName());
        if (healthNoteItemses.get(position).isAfterMeal()) {
            txt_hospital.setText("After Meal");
        } else {
            txt_hospital.setText("Before Meal");
        }
        img_editm_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_editm_delete);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected lab test reminder ?", "Lab Test", position);
                dialogDeleteAll.setiOnOtpDoneDelete(Reminder_Lab_Self_ListAdpter.this);
                dialogDeleteAll.show();

            }
        });
        img_edit_rem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_edit_rem);
                Bundle bundle = new Bundle();
                bundle.putString("labTestReminderId", healthNoteItemses.get(position).getLabTestReminderId());
                bundle.putString("doctorName", "" + healthNoteItemses.get(position).getDoctorName());
                bundle.putString("labName", "" + healthNoteItemses.get(position).getLabName());
                bundle.putString("testName", "" + healthNoteItemses.get(position).getRemMedicineName());
                bundle.putBoolean("isAfterMeal", healthNoteItemses.get(position).isAfterMeal());
                bundle.putString("time", "" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(healthNoteItemses.get(position).getHour(), healthNoteItemses.get(position).getMintue()));
                bundle.putString("date", "" + (healthNoteItemses.get(position).getDate() < 10 ? "0" + healthNoteItemses.get(position).getDate() : healthNoteItemses.get(position).getDate()) + "/" + (healthNoteItemses.get(position).getMonth() < 10 ? "0" + healthNoteItemses.get(position).getMonth() : healthNoteItemses.get(position).getMonth()) + "/" + healthNoteItemses.get(position).getYear());
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLabTestSetReminder(), bundle, true);
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
                    getDoctorVisitDelete(healthNoteItemses.get(position).getLabTestReminderId(), position, false, true);
                } else {
                    getDoctorVisitDelete(healthNoteItemses.get(position).getLabTestReminderId(), position, false, false);
                    Log.e("check", ":- not");
//                    healthNoteItemses.get(position).setSelected(false);
                }

            }
        });
    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getDoctorVisitDelete(healthNoteItemses.get(pos).getLabTestReminderId(), pos, true, false);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_med_name;
        public TextView txt_med_time;
        public TextView txt_hospital;
        public ImageView img_edit_rem, img_editm_delete;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.img_editm_delete = (ImageView) itemView.findViewById(R.id.img_editm_delete);
            this.img_edit_rem = (ImageView) itemView.findViewById(R.id.img_edit_rem);
            this.txt_med_name = (TextView) itemView
                    .findViewById(R.id.txt_med_name);
            this.txt_med_time = (TextView) itemView.findViewById(R.id.txt_med_time);
            this.txt_hospital = (TextView) itemView.findViewById(R.id.txt_hospital);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.chkStateStep);
        }
    }

    private void getDoctorVisitDelete(String id, final int pos, final boolean isDeleted, boolean isOn) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.LAB_TEST_DELETE_ + id + "&isDeleted=" + isDeleted + "&isOn=" + isOn,
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