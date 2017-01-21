package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentDoctorVisitSetReminder extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private View rootView;
    private TextView txt_date, txt_time_select, btn_set_reminder, txt_days_every;
    private EditText edt_test_name, edt_lab_name;
    private String startFrom = "";
    private String firstTime = "";
    private RequestQueue requestQueue;
    private LinearLayout txt_reminder_every;
    private ListPopupWindow listPopupWindow;
    private boolean isNewReminder = true;
    private String doctorFollowupReminderId="";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragemnt_reminder_set_doctor_visit,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        txt_reminder_every = (LinearLayout) rootView.findViewById(R.id.txt_reminder_every);
        txt_days_every = (TextView) rootView.findViewById(R.id.txt_days_every);
        btn_set_reminder = (TextView) rootView.findViewById(R.id.btn_set_reminder);
        edt_test_name = (EditText) rootView.findViewById(R.id.edt_test_name);
        edt_lab_name = (EditText) rootView.findViewById(R.id.edt_lab_name);

        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_time_select = (TextView) rootView.findViewById(R.id.txt_time_select);

        txt_days_every.setPaintFlags(txt_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_date.setPaintFlags(txt_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_time_select.setPaintFlags(txt_time_select.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_date.setOnClickListener(this);
        txt_time_select.setOnClickListener(this);
        btn_set_reminder.setOnClickListener(this);

        (rootView.findViewById(R.id.txt_reminder_every)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUp));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_days_every));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._25dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick1);
                listPopupWindow.show();
            }
        });


        Bundle vBundle = getArguments();
        if (vBundle != null) {
            edt_test_name.setText("" + vBundle.getString("doctorName"));
            edt_lab_name.setText("" + vBundle.getString("hospitalName"));
            firstTime = vBundle.getString("time");
            startFrom = vBundle.getString("date");
            txt_date.setText("" + startFrom);
            txt_time_select.setText("" + firstTime);
            isNewReminder = false;
            doctorFollowupReminderId=vBundle.getString("doctorFollowupReminderId");
        }


        return rootView;
    }

    AdapterView.OnItemClickListener popUpItemClick1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            txt_days_every.setText("" + MyConstants.IArrayData.listPopUp[position]);
//            duration = MyConstants.IArrayData.listPopUp[position];
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_date:
                final int year, day;
                int month1;
                final Calendar c1 = Calendar.getInstance();
                Log.e("Age ", ":- " + AppPreference.getInstance().getGoalAge());
                if (startFrom.equalsIgnoreCase("")) {
                    year = c1.get(Calendar.YEAR);
                    month1 = c1.get(Calendar.MONTH);
                    day = c1.get(Calendar.DAY_OF_MONTH);
                } else {
                    Log.e("ye wala ", " ye wlal");
                    String[] dateFormat = startFrom.split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentDoctorVisitSetReminder.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();
                break;

            case R.id.txt_time_select:
                final Calendar c2 = Calendar.getInstance();
                // Current Hour
                int hour1 = c2.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                int minute1 = c2.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(), this, hour1, minute1, false);
                timePickerDialog1.show();

                break;
            case R.id.btn_set_reminder:
                setMedReminderDetails();
                break;
        }
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int mintues) {

        firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues);
        txt_time_select.setText("" + Utils.updateTimeAMPM(hourOfDay, mintues));


    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        startFrom = "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        txt_date.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
    }


    private boolean validateTestName() {
        String email = edt_test_name.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Doctor Name");
            return false;
        }
        return true;
    }

    private boolean validateLabName() {
        String email = edt_lab_name.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Hospital/Clinic Name");
            return false;
        }
        return true;
    }

    private boolean validateDate() {
        String email = startFrom;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Date");
            return false;
        }
        return true;
    }

    private boolean validateLabTime() {
        String email = firstTime;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Time");
            return false;
        }
        return true;
    }


    public void setMedReminderDetails() {
        if (!validateTestName()) {
            return;
        }
        if (!validateLabName()) {
            return;
        }

        if (!validateDate()) {
            return;
        }

        if (!validateLabTime()) {
            return;
        }

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSetDoctorVisitReminder(edt_test_name.getText().toString().trim(), edt_lab_name.getText().toString().trim(), startFrom, firstTime, doctorFollowupReminderId, isNewReminder);
        Log.e("jsonUploadLabRem", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_DOCTOR_VISIT_REM, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("MedRemDetails, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                CureFull.getInstanse().getActivityIsntanse().onBackPressed();
                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("Remider, URL 3.", "Error: " + error.getMessage());
            }
        }) {
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
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


}