package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
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

import java.text.DecimalFormat;
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

import static utils.MyConstants.JsonUtils.CFUUHIDs;
import static utils.MyConstants.JsonUtils.COMMON_ID;
import static utils.MyConstants.JsonUtils.COUNT_OF_FILES;
import static utils.MyConstants.JsonUtils.DATE_OF_UPLOAD;
import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.REPORT_DATE;
import static utils.MyConstants.JsonUtils.REPORT_ID;
import static utils.MyConstants.JsonUtils.TEST_NAME;
import static utils.MyConstants.JsonUtils.UPLOAD_BY;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLabTestSetReminder extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {
    private View rootView;
    private TextView txt_date, txt_time_select, btn_set_reminder;
    private EditText edt_test_name, edt_lab_name, edt_doctor_name;
    private String startFrom = "";
    private RadioGroup radioMeal;
    private RadioButton radioBeforeMeal, radioAfterMeal;
    private boolean isBeforeMeal = false, isAfterMeal = false, btnClick = true;
    private String firstTime = "";
    private String labTestReminderId = "";
    private boolean isNewReminder = true;
    private boolean chklabreminderid=false;
    private String commonid="";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragemnt_reminder_lab_test,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        btn_set_reminder = (TextView) rootView.findViewById(R.id.btn_set_reminder);
        edt_test_name = (EditText) rootView.findViewById(R.id.edt_test_name);
        edt_lab_name = (EditText) rootView.findViewById(R.id.edt_lab_name);
        edt_doctor_name = (EditText) rootView.findViewById(R.id.edt_doctor_name);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_time_select = (TextView) rootView.findViewById(R.id.txt_time_select);

        radioMeal = (RadioGroup) rootView.findViewById(R.id.radioMeal);
        radioBeforeMeal = (RadioButton) rootView.findViewById(R.id.radioBeforeMeal);
        radioAfterMeal = (RadioButton) rootView.findViewById(R.id.radioAfterMeal);

        txt_date.setPaintFlags(txt_date.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_time_select.setPaintFlags(txt_time_select.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_date.setOnClickListener(this);
        txt_time_select.setOnClickListener(this);
        btn_set_reminder.setOnClickListener(this);
        radioMeal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioBeforeMeal) {
                    isBeforeMeal = true;
                    isAfterMeal = false;
                } else if (checkedId == R.id.radioAfterMeal) {
                    isBeforeMeal = false;
                    isAfterMeal = true;
                }
            }
        });


        Bundle vBundle = getArguments();
        if (vBundle != null) {
            edt_doctor_name.setText("" + vBundle.getString("doctorName"));
            edt_test_name.setText("" + vBundle.getString("testName"));
            edt_lab_name.setText("" + vBundle.getString("labName"));
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
            labTestReminderId = vBundle.getString("labTestReminderId");
            chklabreminderid=true;
            if (vBundle.getBoolean("isAfterMeal")) {
                isAfterMeal = true;
                radioAfterMeal.setChecked(true);
            } else {
                radioBeforeMeal.setChecked(true);
                isAfterMeal = false;
            }
        }

        return rootView;
    }


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
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentLabTestSetReminder.this, year, month1, day);
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
                    commonid=String.valueOf(System.currentTimeMillis());
                    insertRemiderLabDetailsLocal(edt_doctor_name.getText().toString().trim(), edt_test_name.getText().toString().trim(), edt_lab_name.getText().toString().trim(), startFrom, firstTime, labTestReminderId, isNewReminder, isAfterMeal,commonid);
                    //CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }
                break;
        }
    }

    private void insertRemiderLabDetailsLocal(String st_doctor_name, String st_test_name, String st_lab_name, String startFrom, String firstTime, String labTestReminderId, boolean NewReminder, boolean AfterMeal,String commonid) {
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

        if (!validateWhen()) {
            btnClick = true;
            return;
        }

        String[] datee=startFrom.split("-");
        String dayy=datee[2];
        String monthh=datee[1];
        String yearr=datee[0];

        String[] timee=firstTime.split(":");
        String hourr=timee[0];
        String minute=timee[1];
        ContentValues values = new ContentValues();

        values.put("doctorName", st_doctor_name);
        values.put("testName", st_test_name);
        values.put("labName",st_lab_name);
        values.put("dayOfMonth", dayy);
        values.put("monthValue", monthh);
        values.put("year", yearr);
        values.put("hour", hourr);
        values.put("minute", minute);
        if (chklabreminderid == true) {
            commonid=labTestReminderId;
            values.put("labTestReminderId", commonid);//
            chklabreminderid=false;
        }else{
            values.put("labTestReminderId", commonid);//labTestReminderId
        }
        values.put("labTestStatus","pending");//NewReminder   changes after reminder notification
        values.put("afterMeal", AfterMeal);
        values.put("cfuuhId",AppPreference.getInstance().getcf_uuhid());
        values.put("isUploaded","1");

        DbOperations.insertLabTestRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), values,commonid);
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

        if (!validateWhen()) {
            btnClick = true;
            return;
        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        JSONObject data = JsonUtilsObject.toSetLabTestReminder(edt_doctor_name.getText().toString().trim(), edt_test_name.getText().toString().trim(), edt_lab_name.getText().toString().trim(), startFrom, firstTime, labTestReminderId, isNewReminder, isAfterMeal);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_LAB_TEST_REM, data,
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
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                VolleyLog.e("Remider, URL 3.", "Error: " + error.getMessage());
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


    private boolean validateTestName() {
        String email = edt_test_name.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Test Name");
            return false;
        }
        return true;
    }

    private boolean validateLabName() {
        String email = edt_lab_name.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Lab Name");
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

    private boolean validateWhen() {
        if (isAfterMeal == false && isBeforeMeal == false) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Meal");
            return false;
        }
        return true;
    }


}