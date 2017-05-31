package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.HttpHeaderParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.Activity_Today_Trends_Home;
import fragment.healthapp.FragmentHealthNote;
import item.property.HealthNoteItems;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;


public class DialogAddCureNote extends Dialog implements View.OnClickListener,TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener, MyConstants.JsonUtils {

    private View v = null;
    Context context;
    private EditText edt_subject, edt_deatils;
    private ImageView dialog_cancel;
    private TextView txt_date_time, txt_time, txt_to_time,btn_done;
    private List<HealthNoteItems> healthNoteItemsesDummy;
    private List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private String firstDate = "", firstTime = "", toFirstTime = "";
    private int newFirstTime = 0;
    private boolean isFirstTime = false;
    private boolean isSelectFrom = false;
    int dbYear = 0;
    private int newFirstTimeMintues = 0;

    public DialogAddCureNote(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_add_curenote);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        edt_subject = (EditText) findViewById(R.id.edt_subject);
        edt_deatils = (EditText) findViewById(R.id.edt_deatils);
        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
        txt_time = (TextView) findViewById(R.id.txt_time);
        txt_date_time = (TextView) findViewById(R.id.txt_date_time);
        txt_to_time = (TextView) findViewById(R.id.txt_to_time);
        btn_done= (TextView) findViewById(R.id.btn_done);
        AppPreference.getInstance().setIsEditGoalPage(false);
        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(true);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(false);

        dialog_cancel.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        txt_date_time.setOnClickListener(this);
        txt_to_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        txt_date_time.setText("     ");
        txt_time.setText("      ");
        txt_to_time.setText("      ");
       /* Bundle value = getArguments();
        if (value != null) {
            edt_subject.setText("" + value.getString("subject"));
            edt_deatils.setText("" + value.getString("details"));
            String date = "" + value.getString("Date");
            String time = "" + value.getString("firstTime");
            if (!date.equalsIgnoreCase("") || !time.equalsIgnoreCase("")) {

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
                if(!time.equalsIgnoreCase("")){
                    String[] dateParts112 = time.split(":");
                    String hrs = dateParts112[0];
                    String mins = dateParts112[1];
                    txt_time.setText("" + Utils.updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
                    toFirstTime = value.getString("toFirstTime");
                    if(!toFirstTime.equalsIgnoreCase("")){
                        String[] dateParts113 = toFirstTime.split(":");
                        String hrs1 = dateParts113[0];
                        String mins1 = dateParts113[1];
                        txt_to_time.setText("" + Utils.updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)));
                    }

                }

            }


        }*/

      /*  if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            healthNoteItemsesDummy = DbOperations.getOfflineNoteList(CureFull.getInstanse().getActivityIsntanse());
            if (healthNoteItemsesDummy != null && healthNoteItemsesDummy.size() > 0) {

                offlineDataSent(healthNoteItemsesDummy);
            } else {
                getAllHealthList();
            }
        } else {
            getAllHealthList();
        }
*/
        CureFull.getInstanse().getActivityIsntanse().clickImage(v);
        edt_subject.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    edt_deatils.requestFocus();
                }
                return false;
            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.txt_time:
                if (firstDate.equalsIgnoreCase("")) {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select Date First.");
                } else {
                    isFirstTime = true;
                    final Calendar c1 = Calendar.getInstance();
                    // Current Hour
                    int hour1 = c1.get(Calendar.HOUR_OF_DAY);
                    // Current Minute
                    int minute1 = c1.get(Calendar.MINUTE);
                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(CureFull.getInstanse().getActivityIsntanse(), this, hour1, minute1, false);
                    timePickerDialog1.show();
                }
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
                    String[] dateFormat = firstDate.split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }

                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, DialogAddCureNote.this, year, month1, day);
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
                    TimePickerDialog timePickerDialog = new TimePickerDialog(CureFull.getInstanse().getActivityIsntanse(), this, hour, minute, false);
                    timePickerDialog.show();
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select first from time.");

                }

                break;
            case R.id.btn_done:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                if (!validateSubject()) {
                    return;
                }
                if (!validateDeatils()) {
                    return;
                }
                btn_done.setEnabled(false);

                healthNoteItemses = null;
                healthNoteItemses = new ArrayList<HealthNoteItems>();
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                jsonHealthNoteCheck();
                break;



        }
    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int mintues) {
        String todayTime = Utils.getTodayTime();
        String[] dfd = todayTime.split(":");
        String hrs = dfd[0];
        String mins = dfd[1];

        String date = Utils.getTodayDate();
        String[] dateFormat = date.split("-");
        int mYear = Integer.parseInt(dateFormat[0]);
        int mMonth = Integer.parseInt(dateFormat[1]);
        int mDay = Integer.parseInt(dateFormat[2]);
        date = mYear + "-" + (mMonth < 10 ? "0" + mMonth : mMonth) + "-" + (mDay < 10 ? "0" + mDay : mDay);

        if (isFirstTime) {
            if (firstDate.equalsIgnoreCase("")) {
                if (hourOfDay > Integer.parseInt(hrs)) {
                    newFirstTime = hourOfDay;
                    newFirstTimeMintues = mintues;
                    isSelectFrom = true;
                    firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                    txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    txt_to_time.setText("     ");
                    toFirstTime = "";
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select greater than Current time.");
                }
            } else {
                if (date.equalsIgnoreCase(firstDate)) {
                    if (hourOfDay < Integer.parseInt(hrs) + 1 & mintues < Integer.parseInt(mins) + 1) {
                        newFirstTime = hourOfDay;
                        newFirstTimeMintues = mintues;
                        isSelectFrom = true;
                        firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                        txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                        txt_to_time.setText("    ");
                        toFirstTime = "";
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select less than Current time.");

                    }
                } else {
                    newFirstTime = hourOfDay;
                    newFirstTimeMintues = mintues;
                    isSelectFrom = true;
                    firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                    txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    txt_to_time.setText("     ");
                    toFirstTime = "";
                }
            }


        } else {
            if (date.equalsIgnoreCase(firstDate)) {
                try {
                    String string1 = Utils.getTodayTime();
                    Date time1 = new SimpleDateFormat("HH:mm").parse(string1);
                    Calendar calendar1 = Calendar.getInstance();
                    calendar1.setTime(time1);
                    calendar1.add(Calendar.DATE, 1);

                    String string2 = newFirstTime + ":" + newFirstTimeMintues;
                    Date time2 = new SimpleDateFormat("HH:mm").parse(string2);
                    Calendar calendar2 = Calendar.getInstance();
                    calendar2.setTime(time2);
                    calendar2.add(Calendar.DATE, 1);

                    String someRandomTime = hourOfDay + ":" + mintues;
                    Date d = new SimpleDateFormat("HH:mm").parse(someRandomTime);
                    Calendar calendar3 = Calendar.getInstance();
                    calendar3.setTime(d);
                    calendar3.add(Calendar.DATE, 1);

                    Date x = calendar3.getTime();

//                    Log.e("value ", "" + " " + calendar3.getTime() + " " + calendar2.getTime() + " " + calendar1.getTime());

                    if (x.before(calendar1.getTime()) && x.after(calendar2.getTime())) {
                        //checkes whether the current time is between 14:49:00 and 20:11:13.
                        toFirstTime = "" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                        txt_to_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    } else if (x.before(calendar2.getTime())) {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select greater than first time.");

                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select less than current time.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {


                try {
                    String dateInString = firstDate + " " + newFirstTime + ":" + newFirstTimeMintues;
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date d1 = sdf.parse(dateInString);

                    String someRandomTime = firstDate + " " + hourOfDay + ":" + mintues;
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                    Date d = sdf1.parse(someRandomTime);

//                    Log.e("value ", "" + " " + calendar3.getTime() + " " + calendar2.getTime() + " " + calendar1.getTime());

                    if (d1.before(d)) {
                        toFirstTime = "" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                        txt_to_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + "Please select greater than first time.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }
    public void jsonHealthNoteCheck() {

        String date = "";
        if (firstDate.equalsIgnoreCase("")) {
            date = Utils.getTodayDate();
            String[] dateFormat = date.split("-");
            int mYear = Integer.parseInt(dateFormat[0]);
            int mMonth = Integer.parseInt(dateFormat[1]);
            int mDay = Integer.parseInt(dateFormat[2]);
            dbYear = mYear;
            date = mYear + "-" + (mMonth < 10 ? "0" + mMonth : mMonth) + "-" + (mDay < 10 ? "0" + mDay : mDay);
        } else {
            date = firstDate;
            String getYear[]=date.split("-");
            dbYear = Integer.parseInt(getYear[0]);
        }
        String time = "";
        if (firstTime.equalsIgnoreCase("")) {
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
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            JSONObject data = JsonUtilsObject.toAddHealthNote(edt_subject.getText().toString().trim(), edt_deatils.getText().toString().trim(), date, time, toTime);
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.HEALTH_NOTE_ADD, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            btn_done.setEnabled(true);
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                firstTime = "";
                                toFirstTime = "";
                                firstDate = "";
                               /* offset = 0;
                                getAllHealthList();*/
                                txt_time.setText("           ");
                                txt_to_time.setText("        ");
                                txt_date_time.setText("       ");
                              /*  liner_to_time.setVisibility(View.GONE);
                                liner_date_t.setVisibility(View.GONE);
                                date_time_picker.setVisibility(View.GONE);
                                txt_click_here_add.setVisibility(View.VISIBLE);*/
                                edt_subject.setText("");
                                edt_deatils.setText("");

                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {
                    btn_done.setEnabled(true);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(v, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                    VolleyLog.e("health, URL 3.", "Error: " + error.getMessage());
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
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                    return headers;
                }

            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        } else {
            btn_done.setEnabled(true);


            ContentValues cv = new ContentValues();
            cv.put("dateOfNote", date);
            cv.put("fromTime", time);
            cv.put("subject", edt_subject.getText().toString().trim());
            cv.put("details", edt_deatils.getText().toString().trim());
            cv.put("toTime", toTime);
            cv.put("year", dbYear);
            cv.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
            DbOperations operations = new DbOperations();
            operations.insertOfflineNoteList(CureFull.getInstanse().getActivityIsntanse(), cv);
            DbOperations operations1 = new DbOperations();
            ContentValues values = new ContentValues();

            int idss = CureFull.getIdss();
            idss = idss + 1;
            values.put(ID, idss);
            values.put(NOTE_DATE, date);
            values.put(NOTE_HEADING, edt_subject.getText().toString().trim());
            values.put(NOTE_DEATILS, edt_deatils.getText().toString().trim());
            values.put(NOTE_TIME, time);
            values.put(NOTE_TIME_TO, toTime);
            values.put(YEAR, dbYear);
            values.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
            values.put("is_offline", "1");
            operations1.insertNoteList(CureFull.getInstanse().getActivityIsntanse(), values, idss);
            CureFull.setIdss(idss);
            firstTime = "";
            toFirstTime = "";
            firstDate = "";
            txt_time.setText("     ");
            txt_to_time.setText("     ");
            txt_date_time.setText("     ");

            edt_subject.setText("");
            edt_deatils.setText("");
           // getAllHealthList();
        }

    }
    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        try {
            dbYear = year;
            txt_date_time.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " + Utils.formatMonth(String.valueOf(mnt)));
            firstDate = year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private boolean validateSubject() {
        String email = edt_subject.getText().toString().trim();
        if (email.isEmpty()) {
            edt_subject.setError("Subject cannot be left blank.");
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
            edt_deatils.setError("Details cannot be left blank.");
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
}