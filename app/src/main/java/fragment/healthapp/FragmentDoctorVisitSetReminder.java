package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.graphics.Paint;
import android.os.Build;
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

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
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
    private LinearLayout txt_reminder_every;
    private ListPopupWindow listPopupWindow;
    private boolean isNewReminder = true, btnClick = true;
    private String doctorFollowupReminderId = "";
    private boolean chkdoctorFollowupReminderId = false;
    private String commonid = "";

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
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._35dp));
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

            txt_date.setText("" + vBundle.getString("date"));
            startFrom = vBundle.getString("date");
            String[] newDate = startFrom.split("/");
            String day = newDate[0];
            String month = newDate[1];
            String year = newDate[2];
            startFrom = year + "-" + month + "-" + day;
            firstTime = vBundle.getString("time");
            txt_time_select.setText("" + firstTime);
            String[] newTime = firstTime.split(" ");
            firstTime = newTime[0];

            isNewReminder = false;
            doctorFollowupReminderId = vBundle.getString("doctorFollowupReminderId");
            chkdoctorFollowupReminderId = true;
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
                if (startFrom.equalsIgnoreCase("")) {
                    year = c1.get(Calendar.YEAR);
                    month1 = c1.get(Calendar.MONTH);
                    day = c1.get(Calendar.DAY_OF_MONTH);
                } else {
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
                newDateDialog.getDatePicker().setMinDate(newDate.getTime());
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (btnClick) {
                        btnClick = false;
                        setMedReminderDetails();
                    }

                } else {
                    commonid = String.valueOf(System.currentTimeMillis());
                    insertRemiderDoctorDetailsLocal(edt_test_name.getText().toString().trim(), edt_lab_name.getText().toString().trim(), startFrom, firstTime, doctorFollowupReminderId, isNewReminder, commonid);
                }

                break;
        }
    }

    private void insertRemiderDoctorDetailsLocal(String doctorname, String hospitalname, String startFrom, String firstTime, String doctorFollowupReminderId, boolean isNewReminder, String commonid) {
        if (!validateTestName()) {
            btnClick = true;
            return;
        }
        if (!validateLabName()) {
            btnClick = true;
            return;
        }

        if (!validateDate()) {
            btnClick = true;
            return;
        }

        if (!validateLabTime()) {
            btnClick = true;
            return;
        }

        String[] datee = startFrom.split("-");
        String dayy = datee[2];
        String monthh = datee[1];
        String yearr = datee[0];

        String[] timee = firstTime.split(":");
        String hourr = timee[0];
        String minute = timee[1];
        ContentValues values = new ContentValues();

        values.put("doctorName", doctorname);
        values.put("hospitalName", hospitalname);

        values.put("dayOfMonth", dayy);
        values.put("monthValue", monthh);
        values.put("year", yearr);
        values.put("hour", hourr);
        values.put("minute", minute);
        if (chkdoctorFollowupReminderId == true) {
            commonid = doctorFollowupReminderId;
            values.put("doctorFollowupReminderId", commonid);
            chkdoctorFollowupReminderId = false;
        } else {
            values.put("doctorFollowupReminderId", commonid);
        }
        values.put("status", "pending");//isNewReminder   changes after reminder notification

        values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
        values.put("isUploaded", "1");

        DbOperations.insertDoctorRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid);
        try {
            ContentValues cv = new ContentValues();
            cv.put("doctorName", doctorname);
            cv.put("isUploaded", "0");
            cv.put("cfuuhid", AppPreference.getInstance().getcf_uuhid());
            cv.put("common_id", commonid);
            cv.put("case_id", "2");   //1-medicine reminder doctor name   2-doctor reminder doctor name  3-lab reminder doctor name

            DbOperations.insertDoctorName(CureFull.getInstanse().getActivityIsntanse(), cv, commonid, AppPreference.getInstance().getcf_uuhid(), "2");

        } catch (Exception e) {
            e.getMessage();
        }
        CureFull.getInstanse().getActivityIsntanse().onBackPressed();


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
            btnClick = true;
            return;
        }
        if (!validateLabName()) {
            btnClick = true;
            return;
        }

        if (!validateDate()) {
            btnClick = true;
            return;
        }

        if (!validateLabTime()) {
            btnClick = true;
            return;
        }

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        JSONObject data = JsonUtilsObject.toSetDoctorVisitReminder(edt_test_name.getText().toString().trim(), edt_lab_name.getText().toString().trim(), startFrom, firstTime, doctorFollowupReminderId, isNewReminder);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_DOCTOR_VISIT_REM, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btnClick = true;
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


}