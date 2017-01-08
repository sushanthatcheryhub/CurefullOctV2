package fragment.healthapp;


import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.ValueAnimator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.WeakHashMap;

import adpter.Health_Note_Landing_ListAdpter;
import adpter.Health_Note_ListAdpter;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogHintScreenaNote;
import item.property.HealthNoteItems;
import operations.DbOperations;
import sticky.header.ExpandableStickyListHeadersListView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.SwitchDateTimeDialogFragment;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHealthNote extends Fragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    private EditText edt_deatils, edt_subject;
    private TextView txt_date_time, txt_time, txt_to_time, btn_done, txt_click_here_add;
    private LinearLayout liner_date_t, liner_to_time;
    private LinearLayout date_time_picker;
    private String firstDate = "", firstTime = "", toFirstTime = "";
    private RequestQueue requestQueue;
    private List<HealthNoteItems> healthNoteItemsesDummy;
    private List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private ExpandableStickyListHeadersListView mListView;
    private WeakHashMap<View, Integer> mOriginalViewHeightPool = new WeakHashMap<View, Integer>();
    private Health_Note_ListAdpter adapterRecentNew;
    private RelativeLayout realtive_no_health;
    private LinearLayout txt_prescription, txt_heath_app, txt_lab_reports;
    private boolean isFirstTime = false;
    private boolean isSelectFrom = false;
    private int newFirstTime = 0;
    private int secondTime = 0;
    private ImageView img_question_note;
    private int offset = 0;
    private boolean isloadMore = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_note,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        img_question_note = (ImageView) rootView.findViewById(R.id.img_question_note);
        txt_prescription = (LinearLayout) rootView.findViewById(R.id.txt_prescription);
        txt_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_heath_app);
        txt_lab_reports = (LinearLayout) rootView.findViewById(R.id.txt_lab_reports);


        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        realtive_no_health = (RelativeLayout) rootView.findViewById(R.id.realtive_no_health);
        txt_click_here_add = (TextView) rootView.findViewById(R.id.txt_click_here_add);
        btn_done = (TextView) rootView.findViewById(R.id.btn_done);
        txt_to_time = (TextView) rootView.findViewById(R.id.txt_to_time);
        txt_time = (TextView) rootView.findViewById(R.id.txt_time);
        txt_date_time = (TextView) rootView.findViewById(R.id.txt_date_time);
        edt_deatils = (EditText) rootView.findViewById(R.id.edt_deatils);
        edt_subject = (EditText) rootView.findViewById(R.id.edt_subject);
        liner_to_time = (LinearLayout) rootView.findViewById(R.id.liner_to_time);
        liner_date_t = (LinearLayout) rootView.findViewById(R.id.liner_date_t);
        date_time_picker = (LinearLayout) rootView.findViewById(R.id.date_time_picker);
        txt_date_time.setOnClickListener(this);
        txt_to_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        txt_click_here_add.setOnClickListener(this);
        img_question_note.setOnClickListener(this);
        liner_date_t.setOnClickListener(this);
        txt_lab_reports.setOnClickListener(this);
        txt_heath_app.setOnClickListener(this);
        txt_prescription.setOnClickListener(this);


        mListView = (ExpandableStickyListHeadersListView) rootView.findViewById(R.id.list);
        //custom expand/collapse animation
        mListView.setAnimExecutor(new AnimationExecutor());


        Bundle value = getArguments();
        if (value != null) {
            Log.e("value", " value");
            edt_subject.setText("" + value.getString("subject"));
            edt_deatils.setText("" + value.getString("details"));
            String date = "" + value.getString("Date");
            String time = "" + value.getString("firstTime");
            if (!date.equalsIgnoreCase("") || !time.equalsIgnoreCase("")) {
                Log.e("date", " " + date + " time:- " + time);
                txt_click_here_add.setVisibility(View.GONE);
                date_time_picker.setVisibility(View.VISIBLE);
                liner_date_t.setVisibility(View.VISIBLE);
                liner_to_time.setVisibility(View.VISIBLE);
                firstDate = date;

                String[] dateParts11 = firstDate.split("-");
                String yr = dateParts11[0];
                String mnt = dateParts11[1];
                String day = dateParts11[2];
                try {
                    txt_date_time.setText("" + (Integer.parseInt(day) < 10 ? "0" + day : day) + " " + Utils.formatMonth(mnt));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                firstTime = time;
                String[] dateParts112 = time.split(":");
                String hrs = dateParts112[0];
                String mins = dateParts112[1];
                txt_time.setText("" + Utils.updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
                toFirstTime = value.getString("toFirstTime");
                String[] dateParts113 = toFirstTime.split(":");
                String hrs1 = dateParts113[0];
                String mins1 = dateParts113[1];
                txt_to_time.setText("" + Utils.updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)));
            }


        }

        getAllHealthList();


        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        edt_subject.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_deatils.requestFocus();
                }
                return false;
            }
        });

        return rootView;
    }

    private boolean validateSubject() {
        String email = edt_subject.getText().toString().trim();
        if (email.isEmpty()) {
            edt_subject.setError("Name cannot be left blank.");
            requestFocus(edt_subject);
            return false;
        } else {
            edt_subject.setError(null);
        }
        return true;
    }

    private boolean validateDeatils() {
        String email = edt_deatils.getText().toString().trim();
        if (email.isEmpty()) {
            edt_deatils.setError("Deatils cannot be left blank.");
            requestFocus(edt_deatils);
            return false;
        } else {
            edt_deatils.setError(null);
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            CureFull.getInstanse().getActivityIsntanse().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.img_question_note:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_question_note);
                DialogHintScreenaNote dialogHintScreenaNote = new DialogHintScreenaNote(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaNote.show();
                break;
            case R.id.liner_date_t:
                isFirstTime = true;
                final Calendar c1 = Calendar.getInstance();
                // Current Hour
                int hour1 = c1.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                int minute1 = c1.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog1 = new TimePickerDialog(getActivity(), this, hour1, minute1, false);
                timePickerDialog1.show();
                break;
            case R.id.txt_prescription:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheck(), true);
                break;
            case R.id.txt_heath_app:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthAppNew(), true);
                break;
            case R.id.txt_lab_reports:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLabTestReport(), true);
                break;
            case R.id.txt_date_time:
                final int year, day;
                int month1;
                final Calendar c2 = Calendar.getInstance();
                if (firstDate.equalsIgnoreCase("")) {
                    year = c2.get(Calendar.YEAR);
                    month1 = c2.get(Calendar.MONTH);
                    day = c2.get(Calendar.DAY_OF_MONTH);
                } else {
                    Log.e("ye wala ", " ye wlal");
                    String[] dateFormat = firstDate.split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }

                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentHealthNote.this, year, month1, day);
                newDateDialog.getDatePicker().setCalendarViewShown(false);
//                c.add(Calendar.DATE, 1);
                Date newDate = c2.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();

                break;
            case R.id.txt_to_time:

                if (isSelectFrom) {
                    isFirstTime = false;
                    final Calendar c = Calendar.getInstance();
                    // Current Hour
                    int hour = c.get(Calendar.HOUR_OF_DAY);
                    // Current Minute
                    int minute = c.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, false);
                    timePickerDialog.show();
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select first from time.");

                }

                break;
            case R.id.btn_done:
                if (!validateSubject()) {
                    return;
                }

                if (!validateDeatils()) {
                    return;
                }
                isloadMore = false;
                offset = 0;
                healthNoteItemses = null;
                healthNoteItemses = new ArrayList<HealthNoteItems>();
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                jsonHealthNoteCheck();

                break;
            case R.id.txt_click_here_add:
                txt_click_here_add.setVisibility(View.GONE);
                date_time_picker.setVisibility(View.VISIBLE);
                liner_date_t.setVisibility(View.VISIBLE);
                liner_to_time.setVisibility(View.VISIBLE);
                break;


        }
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int mintues) {
        if (isFirstTime) {
            newFirstTime = hourOfDay;
            isSelectFrom = true;
            firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
            txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
            txt_to_time.setText("");
            toFirstTime = "";
        } else {
            secondTime = hourOfDay;
            Log.e("first ", " " + newFirstTime + " second:- " + secondTime);
            if (secondTime > newFirstTime) {
                toFirstTime = "" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                txt_to_time.setText("" + Utils.updateTime(hourOfDay, mintues));
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select greater than first time.");
            }

        }

    }


    public void jsonHealthNoteCheck() {
        Log.e("aacce tok", ":- " + AppPreference.getInstance().getAt());
        Log.e("a_t ", ":- " + AppPreference.getInstance().getAt());
        Log.e("r_t ", ":- " + AppPreference.getInstance().getRt());
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        String date = "";
        if (firstDate.equalsIgnoreCase("")) {
            date = getTodayDate();
            String[] dateFormat = date.split("-");
            int mYear = Integer.parseInt(dateFormat[0]);
            int mMonth = Integer.parseInt(dateFormat[1]);
            int mDay = Integer.parseInt(dateFormat[2]);
            date = mYear + "-" + (mMonth < 10 ? "0" + mMonth : mMonth) + "-" + (mDay < 10 ? "0" + mDay : mDay);
        } else {
            date = firstDate;
        }
        String time = "";
        if (firstTime.equalsIgnoreCase("")) {
            Log.e("getTodayTime", ":- " + Utils.getTodayTime());
            time = Utils.getTodayTime();
        } else {
            time = firstTime;
        }
        String toTime = "";
        if (toFirstTime.equalsIgnoreCase("")) {
            toTime = "";
        } else {
            toTime = toFirstTime;
        }
        JSONObject data = JsonUtilsObject.toAddHealthNote(edt_subject.getText().toString().trim(), edt_deatils.getText().toString().trim(), date, time, toTime);
        Log.e("data", ":- " + data.toString());
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.HEALTH_NOTE_ADD, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("health, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.e("responseStatus :- ", String.valueOf(responseStatus));
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            firstTime = "";
                            toFirstTime = "";
                            firstDate = "";
                            getAllHealthList();
                            txt_time.setText("");
                            txt_to_time.setText("");
                            txt_date_time.setText("");
                            liner_to_time.setVisibility(View.GONE);
                            liner_date_t.setVisibility(View.GONE);
                            date_time_picker.setVisibility(View.GONE);
                            txt_click_here_add.setVisibility(View.VISIBLE);
                            edt_subject.setText("");
                            edt_deatils.setText("");

                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("health, URL 3.", "Error: " + error.getMessage());
            }

        }) {


            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
//                    Log.e("headers", "" +  response.headers.get("a_t"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put(MyConstants.JsonUtils.HEADERS, new JSONObject(response.headers));
                    return Response.success(jsonResponse,
                            HttpHeaderParser.parseCacheHeaders(response));
                } catch (UnsupportedEncodingException e) {
                    return Response.error(new ParseError(e));
                } catch (JSONException je) {
                    return Response.error(new ParseError(je));
                }
            }

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


    private void getAllHealthList() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
            Log.e("url", " " + MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=10&offset=" + offset);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=10&offset=" + offset,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            Log.e("get note, URL 1.", response);

                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                healthNoteItemsesDummy = ParseJsonData.getInstance().getHealthNoteListItem(response);
                                if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
                                    if (healthNoteItemsesDummy.size() < 10) {
                                        isloadMore = true;
                                    }
                                    healthNoteItemses.addAll(healthNoteItemsesDummy);
                                    showAdpter();
                                }

                            } else {
                                healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
                                healthNoteItemses.addAll(healthNoteItemsesDummy);
                                showAdpter();
//                                realtive_no_health.setVisibility(View.VISIBLE);
//                                mListView.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
                            healthNoteItemses.addAll(healthNoteItemsesDummy);
                            showAdpter();
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
        } else {
            healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
            healthNoteItemses.addAll(healthNoteItemsesDummy);
            showAdpter();
        }

    }

    public static String getTodayDate() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public void showAdpter() {
        if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
            realtive_no_health.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adapterRecentNew = new Health_Note_ListAdpter(CureFull.getInstanse().getActivityIsntanse(),
                    healthNoteItemses, FragmentHealthNote.this);
            mListView.setAdapter(adapterRecentNew);
            adapterRecentNew.notifyDataSetChanged();
        } else {
            realtive_no_health.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

    }

    public void showAdpternew() {
        if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
            realtive_no_health.setVisibility(View.GONE);
            mListView.setVisibility(View.VISIBLE);
            adapterRecentNew = new Health_Note_ListAdpter(CureFull.getInstanse().getActivityIsntanse(),
                    healthNoteItemses, FragmentHealthNote.this);
            adapterRecentNew.notifyDataSetChanged();
        } else {
            realtive_no_health.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }

    }

    public void checkSize() {

        if (healthNoteItemses.size() == 0) {
            realtive_no_health.setVisibility(View.VISIBLE);
            mListView.setVisibility(View.GONE);
        }
    }

    public void callWebServiceAgain(int offsets) {


        if (isloadMore) {
            Log.e("isloadMore", "" + isloadMore);
        } else {
            Log.e("offsect", "" + offset);
            offset = +offsets;
            Log.e("off", "" + offsets);
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
                StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=10&offset=" + offset,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                Log.e("getSymptomsList, URL 1.", response);

                                int responseStatus = 0;
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response.toString());
                                    responseStatus = json.getInt("responseStatus");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    healthNoteItemsesDummy = ParseJsonData.getInstance().getHealthNoteListItem(response);
                                    if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
                                        if (healthNoteItemsesDummy.size() < 10) {
                                            isloadMore = true;
                                        }
                                        healthNoteItemses.addAll(healthNoteItemsesDummy);
                                        adapterRecentNew.notifyDataSetChanged();
//                                            showAdpternew();
                                    } else {
                                        if (healthNoteItemsesDummy == null) {
                                            isloadMore = true;
                                        }
                                    }
                                } else {
//                                healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
//                                if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
//                                    healthNoteItemses.addAll(healthNoteItemsesDummy);
//                                    showAdpter();
//                                }

//                                realtive_no_health.setVisibility(View.VISIBLE);
//                                mListView.setVisibility(View.GONE);
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
//                                healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
//                                if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {
//                                    healthNoteItemses.addAll(healthNoteItemsesDummy);
//                                    showAdpter();
//                                }
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
            } else {
//                healthNoteItemsesDummy = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
//                healthNoteItemses.addAll(healthNoteItemsesDummy);
//                showAdpter();
            }
        }
    }


    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        try {
            txt_date_time.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " + Utils.formatMonth(String.valueOf(mnt)));
            firstDate = year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    //animation executor
    class AnimationExecutor implements ExpandableStickyListHeadersListView.IAnimationExecutor {

        @Override
        public void executeAnim(final View target, final int animType) {
            if (ExpandableStickyListHeadersListView.ANIMATION_EXPAND == animType && target.getVisibility() == View.VISIBLE) {
                return;
            }
            if (ExpandableStickyListHeadersListView.ANIMATION_COLLAPSE == animType && target.getVisibility() != View.VISIBLE) {
                return;
            }
            if (mOriginalViewHeightPool.get(target) == null) {
                mOriginalViewHeightPool.put(target, target.getHeight());
            }
            final int viewHeight = mOriginalViewHeightPool.get(target);
            float animStartY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? 0f : viewHeight;
            float animEndY = animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND ? viewHeight : 0f;
            final ViewGroup.LayoutParams lp = target.getLayoutParams();
            ValueAnimator animator = ValueAnimator.ofFloat(animStartY, animEndY);
            animator.setDuration(200);
            target.setVisibility(View.VISIBLE);
            animator.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animator) {
                }

                @Override
                public void onAnimationEnd(Animator animator) {
                    if (animType == ExpandableStickyListHeadersListView.ANIMATION_EXPAND) {
                        target.setVisibility(View.VISIBLE);
                    } else {
                        target.setVisibility(View.GONE);
                    }
                    target.getLayoutParams().height = viewHeight;
                }

                @Override
                public void onAnimationCancel(Animator animator) {

                }

                @Override
                public void onAnimationRepeat(Animator animator) {

                }
            });
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    lp.height = ((Float) valueAnimator.getAnimatedValue()).intValue();
                    target.setLayoutParams(lp);
                    target.requestLayout();
                }
            });
            animator.start();

        }
    }
}