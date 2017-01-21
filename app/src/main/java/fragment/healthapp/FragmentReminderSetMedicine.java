package fragment.healthapp;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.ListPopupWindow;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import adpter.AdapterAddMedicine;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import customsTextViews.CustomTextViewOpenSanRegular;
import item.property.MedicineReminderItem;
import toggle.button.MultiSelectToggleGroup;
import toggle.button.ToggleButtonGroup;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentReminderSetMedicine extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private TextView txt_set_reminder, edt_years, txt_duration, txt_dogaes, txt_duration_txt;
    private RequestQueue requestQueue;
    private ListView list_view_current_visit;
    private static int APPEND_HEIGHT_TO_LIST = 5;
    private ArrayList<MedicineReminderItem> listCurrent = new ArrayList<>();
    private String startFrom = "";
    private ListPopupWindow listPopupWindow;
    private String duration = "";
    private String doages = "";
    private LinearLayout liner_date_select, linear_page_count, liner_reminder_visible;
    String addDays = "";
    private CustomTextViewOpenSanRegular[] view_text_page;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragemnt_reminder_med,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        liner_reminder_visible = (LinearLayout) rootView.findViewById(R.id.liner_reminder_visible);
        linear_page_count = (LinearLayout) rootView.findViewById(R.id.linear_page_count);
        txt_duration_txt = (TextView) rootView.findViewById(R.id.txt_duration_txt);
        edt_years = (TextView) rootView.findViewById(R.id.edt_years);
        txt_duration = (TextView) rootView.findViewById(R.id.txt_duration);
        txt_dogaes = (TextView) rootView.findViewById(R.id.txt_dogaes);
        txt_set_reminder = (TextView) rootView.findViewById(R.id.txt_set_reminder);
        liner_date_select = (LinearLayout) rootView.findViewById(R.id.liner_date_select);
        list_view_current_visit = (ListView) rootView.findViewById(R.id.list_view_current_visit);

        list_view_current_visit.setOnTouchListener(new ListView.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                int action = event.getAction();
                switch (action) {
                    case MotionEvent.ACTION_DOWN:
                        // Disallow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        break;

                    case MotionEvent.ACTION_UP:
                        // Allow ScrollView to intercept touch events.
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                }

                // Handle ListView touch events.
                v.onTouchEvent(event);
                return true;
            }
        });
        txt_set_reminder.setOnClickListener(this);
        liner_date_select.setOnClickListener(this);

        (rootView.findViewById(R.id.liner_duration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUp));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_duration));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._25dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick1);
                listPopupWindow.show();
            }
        });

        (rootView.findViewById(R.id.liner_doages)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUpDogase));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_dogaes));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._25dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick2);
                listPopupWindow.show();
            }
        });
        setupMultiSelectToggleGroup();
        setReminderMediceList();
        edt_years.setPaintFlags(edt_years.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_duration.setPaintFlags(txt_duration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_dogaes.setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        return rootView;
    }


    private void setupMultiSelectToggleGroup() {
        final MultiSelectToggleGroup multiSelect =
                (MultiSelectToggleGroup) rootView.findViewById(R.id.multi_selection_group);
        multiSelect.setOnCheckedPositionChangeListener(
                new ToggleButtonGroup.OnCheckedPositionChangeListener() {
                    @Override
                    public void onCheckedPositionChange(Set<Integer> checkedPositions) {
                        addDays = "";
                        for (int value : checkedPositions) {
                            addDays += MyConstants.IArrayData.listDays[value] + ",";
                        }
                        if (addDays.endsWith(",")) {
                            addDays = addDays.substring(0, addDays.length() - 1);
                        }
                    }
                });
    }

    AdapterView.OnItemClickListener popUpItemClick1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            txt_duration.setText("" + MyConstants.IArrayData.listPopUp[position]);
            duration = MyConstants.IArrayData.listPopUp[position];
        }
    };

    AdapterView.OnItemClickListener popUpItemClick2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            liner_reminder_visible.setVisibility(View.VISIBLE);
            linear_page_count.removeAllViews();
            txt_dogaes.setText("" + MyConstants.IArrayData.listPopUp[position]);
            doages = MyConstants.IArrayData.listPopUp[position];
            showPage(Integer.parseInt(doages));
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txt_set_reminder:
                setMedReminderDetails();
                break;

            case R.id.liner_date_select:
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
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentReminderSetMedicine.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();


                break;


        }
    }

    public void updateAdd(int removed_id) {
        int id = (removed_id + 1);
        MedicineReminderItem patient = new MedicineReminderItem();
        patient.setId(id);
        listCurrent.add(patient);
        setAdapterCurrentVisit();
    }

    public void updateRemove(int position) {
        listCurrent.remove(position);
        setAdapterCurrentVisit();
    }


    private void setReminderMediceList() {
        MedicineReminderItem reminderItem = new MedicineReminderItem();
        reminderItem.setId(1);
        reminderItem.setInterval(0);
        reminderItem.setBaMealAfter(false);
        reminderItem.setBaMealBefore(false);
        listCurrent.add(reminderItem);
        setAdapterCurrentVisit();
    }

    private void setAdapterCurrentVisit() {
//        Log.e("setAdapterCurrentVisit", "how many");
        list_view_current_visit.setAdapter(null);
        AdapterAddMedicine adapter = new AdapterAddMedicine(FragmentReminderSetMedicine.this, CureFull.getInstanse().getActivityIsntanse(), listCurrent);
        list_view_current_visit.setAdapter(adapter);
        setListViewHeightBasedOnChildren(list_view_current_visit);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {
        AdapterAddMedicine listAdapter = (AdapterAddMedicine) listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight() + APPEND_HEIGHT_TO_LIST;
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public void setMedReminderDetails() {

        if (!validateDate()) {
            return;
        }
        if (!validateDuration()) {
            return;
        }
        if (!validateDays()) {
            return;
        }
        if (!validateDoages()) {
            return;
        }
        String newTime = "";
        String hello = "";
        for (int i = 0; i < view_text_page.length; i++) {
            hello = "" + view_text_page[i].getText();
            if (i == (view_text_page.length - 1)) {
                newTime += hello.substring(0, hello.length() - 3);
            } else {
                if (hello.endsWith("am, ")) {
                    newTime += hello.substring(0, hello.length() - 4) + ",";
                } else if (hello.endsWith("pm, ")) {
                    newTime += hello.substring(0, hello.length() - 4) + ",";
                }
            }

        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.setRemMed(startFrom, duration, doages, addDays, listCurrent, newTime);
        Log.e("jsonUploadMedRem", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_MEDICINE_REM, data,
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        startFrom = "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        edt_years.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
    }

    private boolean validateDate() {
        String email = startFrom;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Date");
            return false;
        }
        return true;
    }

    private boolean validateDoages() {
        String email = doages;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Doages");
            return false;
        }
        return true;
    }

    private boolean validateDuration() {
        String email = duration;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Duration");
            return false;
        }
        return true;
    }

    private boolean validateDays() {
        String email = addDays;
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Days");
            return false;
        }
        return true;
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void showPage(final int totalPage) {

        int value = (14 / totalPage);
        Log.e("value", "" + value);


        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        view_text_page = new CustomTextViewOpenSanRegular[totalPage];
        int text = 0;
        for (int i = 0; i < totalPage; i++) {
            if (i == 0) {
                text = 9;
            } else {
                text = text + value;
            }

            view_text_page[i] = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
//            CustomTextViewOpenSanRegular view_text_page = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
            view_text_page[i].setGravity(Gravity.CENTER_VERTICAL);
            view_text_page[i].setTextSize(12);
            view_text_page[i].setSingleLine(true);
            view_text_page[i].setId(i);
            view_text_page[i].setLayoutParams(params);
            Log.e("i ", " " + i);
            if (i == (totalPage - 1)) {
                view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTime(text, 0) + ".");
            } else {
                view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTime(text, 0) + ", ");
            }
            view_text_page[i].setTextColor(Color.parseColor("#fdb832"));
            view_text_page[i].setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            final int finalI = i;
            view_text_page[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String time = "" + view_text_page[finalI].getText();
                    Log.e("time", " " + time);
                    if (time.endsWith("am, ")) {
                        time = time.substring(0, time.length() - 4);
                    } else if (time.endsWith("pm, ")) {
                        time = time.substring(0, time.length() - 4);
                    } else if (time.endsWith("am.")) {
                        time = time.substring(0, time.length() - 3);
                    } else if (time.endsWith("pm.")) {
                        time = time.substring(0, time.length() - 3);
                    }
                    String[] hello = time.split(":");
                    String hour = hello[0];
                    String mintue = hello[1];

                    int hour1 = Integer.parseInt(hour);
                    // Current Minute
                    int minute1 = Integer.parseInt(mintue);

                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(CureFull.getInstanse().getActivityIsntanse(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (finalI == (totalPage - 1)) {
                                view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTime(hourOfDay, minute) + ".");
                            } else {
                                view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTime(hourOfDay, minute) + ", ");

                            }
                        }
                    }, hour1, minute1, false);
                    timePickerDialog1.show();
//                    pageNo = view.getId();

                }
            });
            linear_page_count.addView(view_text_page[i]);
        }


    }


}