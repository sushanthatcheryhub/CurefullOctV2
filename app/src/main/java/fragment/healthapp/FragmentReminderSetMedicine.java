package fragment.healthapp;


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import ElasticVIews.ElasticAction;
import adpter.AdapterAddMedicine;
import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import customsTextViews.CustomTextViewOpenSanRegular;
import item.property.LabReportImageListView;
import item.property.MedicineReminderItem;
import item.property.ReminderMedicnceDoagePer;
import item.property.ReminderMedicnceTime;
import operations.DbOperations;
import toggle.button.MultiSelectToggleGroup;
import toggle.button.ToggleButtonGroup;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentReminderSetMedicine extends Fragment implements View.OnClickListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private TextView txt_set_reminder, edt_years, txt_duration, txt_dogaes, txt_duration_txt;
    private ListView list_view_current_visit;
    private static int APPEND_HEIGHT_TO_LIST = 2;
    private ArrayList<MedicineReminderItem> listCurrent = new ArrayList<>();
    private String startFrom = "";
    private ListPopupWindow listPopupWindow;
    private String duration = "";
    private String doages = "";
    private ArrayList<ReminderMedicnceDoagePer> reminderMedicnceTimes = null;
    private LinearLayout liner_date_select, linear_page_count, liner_reminder_visible;
    private String addDays = "";
    private CustomTextViewOpenSanRegular[] view_text_page;
    private double interval;
    private boolean isNewReminder = true, isVisible = false, isEdit = false, btnClick = true;
    private String medicineReminderId = "";
    private String alramTimeEdit="";
    private String enddateEdit="";
    private MultiSelectToggleGroup multiSelect;
    private RelativeLayout relative_schedule, relative_bottom_area, reltvi_new;
    private ImageView img_rotate;
    private ScrollView scrollView;
    private String commonid;
    private String dosagePerDayDetailsId1;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragemnt_reminder_med,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        scrollView = (ScrollView) rootView.findViewById(R.id.top_view);
        multiSelect =
                (MultiSelectToggleGroup) rootView.findViewById(R.id.multi_selection_group);
        reltvi_new = (RelativeLayout) rootView.findViewById(R.id.reltvi_new);
        relative_bottom_area = (RelativeLayout) rootView.findViewById(R.id.relative_bottom_area);
        relative_schedule = (RelativeLayout) rootView.findViewById(R.id.relative_schedule);
        liner_reminder_visible = (LinearLayout) rootView.findViewById(R.id.liner_reminder_visible);
        linear_page_count = (LinearLayout) rootView.findViewById(R.id.linear_page_count);
        txt_duration_txt = (TextView) rootView.findViewById(R.id.txt_duration_txt);
        edt_years = (TextView) rootView.findViewById(R.id.edt_years);
        txt_duration = (TextView) rootView.findViewById(R.id.txt_duration);
        txt_dogaes = (TextView) rootView.findViewById(R.id.txt_dogaes);
        txt_set_reminder = (TextView) rootView.findViewById(R.id.txt_set_reminder);
        liner_date_select = (LinearLayout) rootView.findViewById(R.id.liner_date_select);
        list_view_current_visit = (ListView) rootView.findViewById(R.id.list_view_current_visit);
        img_rotate = (ImageView) rootView.findViewById(R.id.img_rotate);

        scrollView.setOnTouchListener(new View.OnTouchListener() {
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
        relative_schedule.setOnClickListener(this);
        (rootView.findViewById(R.id.liner_duration)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listPopUp));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_duration));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._35dp));
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
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._35dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClick2);
                listPopupWindow.show();
            }
        });

        edt_years.setPaintFlags(edt_years.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_duration.setPaintFlags(txt_duration.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_dogaes.setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);


        Bundle vBundle = getArguments();
        if (vBundle != null) {
            rotatePhoneClockwise(img_rotate);
            isVisible = true;
            relative_bottom_area.setVisibility(View.VISIBLE);

            isEdit = true;
            MedicineReminderItem reminderItem = new MedicineReminderItem();
            reminderItem.setId(1);
            reminderItem.setShow(true);
            reminderItem.setType(vBundle.getString("type"));
            reminderItem.setDoctorName(vBundle.getString("doctorName"));
            reminderItem.setMedicineName(vBundle.getString("medicineName"));
            reminderItem.setInterval(vBundle.getInt("quantity"));
            reminderItem.setBaMealAfter(vBundle.getBoolean("afterMeal"));
            reminderItem.setBaMealBefore(vBundle.getBoolean("beforeMeal"));
            listCurrent.add(reminderItem);
            setAdapterCurrentVisit();
            duration = vBundle.getString("noOfDays");
            txt_duration.setText("" + duration);
            doages = vBundle.getString("noOfDosage");
            txt_dogaes.setText("" + doages);
            interval = vBundle.getInt("interval");
            edt_years.setText("" + vBundle.getString("date"));
            startFrom = vBundle.getString("date");
            String[] newDate = startFrom.split("/");
            String day = newDate[0];
            String month = newDate[1];
            String year = newDate[2];
            startFrom = year + "-" + month + "-" + day;
            isNewReminder = false;
            reminderMedicnceTimes = vBundle.getParcelableArrayList("timeToTakeMedicne");
            liner_reminder_visible.setVisibility(View.VISIBLE);
            linear_page_count.removeAllViews();
            //showPage(0, reminderMedicnceTimes);
            medicineReminderId = vBundle.getString("medicineReminderId");
            String noOfDaysInWeek = vBundle.getString("noOfDaysInWeek");
            addDays = noOfDaysInWeek;
            String[] weeks = noOfDaysInWeek.split(",");
            Set<Integer> singlesSet = new HashSet<>();
            if (weeks != null & weeks.length > 0) {
                for (int i = 0; i < weeks.length; i++) {
                    switch (weeks[i]) {
                        case "SUN":
                            singlesSet.add(0);
                            break;
                        case "MON":
                            singlesSet.add(1);
                            break;
                        case "TUE":
                            singlesSet.add(2);
                            break;
                        case "WED":
                            singlesSet.add(3);
                            break;
                        case "THU":
                            singlesSet.add(4);
                            break;
                        case "FRI":
                            singlesSet.add(5);
                            break;
                        case "SAT":
                            singlesSet.add(6);
                            break;
                    }

                }
                multiSelect.setCheckedPositions(singlesSet);
            }
            try {
                alramTimeEdit = vBundle.getString("alarmTime");
                enddateEdit = vBundle.getString("enddate");
                if(CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())){
                    showPage(0, reminderMedicnceTimes);
                }else {
                    showPageLocal(0, alramTimeEdit);
                }
                //dosagePerDayDetailsId1 = vBundle.getString("dosagePerDayDetailsId");
            } catch (Exception e) {
                e.getMessage();
            }
        } else {
            setReminderMediceList();
        }


        multiSelect.setOnCheckedPositionChangeListener(
                new ToggleButtonGroup.OnCheckedPositionChangeListener() {
                    @Override
                    public void onCheckedPositionChange(Set<Integer> checkedPositions) {

                        if (duration.equalsIgnoreCase("")) {
                            multiSelect.uncheckAll();
                            return;
                        }
                        if (checkedPositions.size() > Integer.parseInt(duration)) {
                            addDays = "";
                            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please don't select more than duration");
                            multiSelect.uncheckAll();
                            return;
                        }
                        addDays = "";
                        for (int value : checkedPositions) {
                            addDays += MyConstants.IArrayData.listDays[value] + ",";
                        }
                        if (addDays.endsWith(",")) {
                            addDays = addDays.substring(0, addDays.length() - 1);
                        }


                    }
                });

        return rootView;
    }

    private void showPageLocal(final int totalPage, String alramTimeEdit) {
        String med = "";
        String[] newTimesplit = alramTimeEdit.split(",");
        for (int i2 = 0; i2 < newTimesplit.length; i2++) {
            String[] time = newTimesplit[i2].split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            med += hour + ":" + minute + ",";
        }

        /*for (int i = 0; i < timeToTakeMedicne.size(); i++) {
            if (timeToTakeMedicne.get(i).getReminderMedicnceTimes() != null) {
                for (int j = 0; j < timeToTakeMedicne.get(i).getReminderMedicnceTimes().size(); j++) {
                    int hrs1 = timeToTakeMedicne.get(i).getReminderMedicnceTimes().get(j).getHour();
                    int mins1 = timeToTakeMedicne.get(i).getReminderMedicnceTimes().get(j).getMinute();
                    med += hrs1 + ":" + mins1 + ",";
                }
            }

        }*/
        String[] timeToMedo = med.split(",");
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 0);
        view_text_page = new CustomTextViewOpenSanRegular[timeToMedo.length];

        for (int i = 0; i < timeToMedo.length; i++) {
            view_text_page[i] = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
//            CustomTextViewOpenSanRegular view_text_page = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
            view_text_page[i].setGravity(Gravity.CENTER_VERTICAL);
            view_text_page[i].setTextSize(12);
            view_text_page[i].setSingleLine(true);
            view_text_page[i].setId(i);
            view_text_page[i].setLayoutParams(params);
//                Log.e("i ", " " + i);
            if (i == (timeToMedo.length - 1)) {
                String[] text = timeToMedo[i].split(":");
                view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(text[0]), Integer.parseInt(text[1])) + ".");
            } else {
                String[] text = timeToMedo[i].split(":");
                view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(text[0]), Integer.parseInt(text[1])) + " | ");
            }
            view_text_page[i].setTextColor(Color.parseColor("#a5a5a5"));
            view_text_page[i].setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            final int finalI = i;
            view_text_page[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String time = "" + view_text_page[finalI].getText();
//                        Log.e("time", " " + time);
                    if (time.endsWith(" am | ")) {
                        time = time.substring(0, time.length() - 5);
                    } else if (time.endsWith(" pm | ")) {
                        time = time.substring(0, time.length() - 5);
                    } else if (time.endsWith(" am.")) {
                        time = time.substring(0, time.length() - 4);
                    } else if (time.endsWith(" pm.")) {
                        time = time.substring(0, time.length() - 4);
                    }
                    String[] hello = time.split(":");
                    String hour = hello[0];
                    String mintue = hello[1];

                    int hour1 = Integer.parseInt(hour);
                    // Current Minute
                    int minute1 = Integer.parseInt(mintue.trim());

                    TimePickerDialog timePickerDialog1 = new TimePickerDialog(CureFull.getInstanse().getActivityIsntanse(), new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            if (finalI == (totalPage - 1)) {
                                view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + ".");
                            } else {
                                view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + " | ");

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


    AdapterView.OnItemClickListener popUpItemClick1 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            txt_duration.setText("" + MyConstants.IArrayData.listPopUp[position]);
            duration = MyConstants.IArrayData.listPopUp[position];
            multiSelect.uncheckAll();
        }
    };

    AdapterView.OnItemClickListener popUpItemClick2 = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            liner_reminder_visible.setVisibility(View.VISIBLE);
            linear_page_count.removeAllViews();
            txt_dogaes.setText("" + MyConstants.IArrayData.listPopUp[position]);
            reminderMedicnceTimes = null;
            doages = MyConstants.IArrayData.listPopUp[position];
            showPage(Integer.parseInt(doages), reminderMedicnceTimes);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.relative_schedule:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                if (isVisible) {
                    rotatePhoneAntiClockwise(img_rotate);
                    isVisible = false;
                    relative_bottom_area.setVisibility(View.GONE);
                } else {
                    scrollView.post(new Runnable() {
                        public void run() {
                            scrollView.smoothScrollTo(0, reltvi_new.getHeight());
                        }
                    });
                    rotatePhoneClockwise(img_rotate);
                    isVisible = true;
                    relative_bottom_area.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.txt_set_reminder:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (btnClick) {
                        btnClick = false;
                        if (isEdit) {
                            setMedReminderDetailsEdit();
                        } else {
                            setMedReminderDetails();
                        }
                    }

                } else {

                    if (btnClick) {
                        btnClick = false;
                        if (isEdit) {
                            setMedReminderDetailsEditLocal();
                        } else {

                            setMedReminderDetailsLocal();
                        }
                    }
                    //CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }

                break;

            case R.id.liner_date_select:
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
                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentReminderSetMedicine.this, year, month1, day);
                newDateDialog.getDatePicker().setSpinnersShown(true);
//                c.add(Calendar.DATE, 1);
                Date newDate = c1.getTime();
                newDateDialog.getDatePicker().setMinDate(newDate.getTime());
                newDateDialog.show();


                break;


        }
    }

    private void setMedReminderDetailsEditLocal() {
        for (int i = 0; i < listCurrent.size(); i++) {
            if (listCurrent.get(i).getType().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Type");
                return;
            } else if (listCurrent.get(i).getDoctorName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Doctor Name");
                return;
            } else if (listCurrent.get(i).getMedicineName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Medicine Name");
                return;
            } else if (listCurrent.get(i).getInterval() == 0) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Quantity");
                return;
            } else if (listCurrent.get(i).isBaMealBefore() == false && listCurrent.get(i).isBaMealAfter() == false) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Meal");
                return;
            }
        }
        if (!validateDate()) {
            btnClick = true;
            return;
        }
        if (!validateDuration()) {
            btnClick = true;
            return;
        }
        if (!validateDays()) {
            btnClick = true;
            return;
        }
        if (!validateDoages()) {
            btnClick = true;
            return;
        }
        String newTime = "";
        String hello = "";

        for (int i = 0; i < view_text_page.length; i++) {
            hello = "" + view_text_page[i].getText();
            if (i == (view_text_page.length - 1)) {
                newTime += get24hrsFormat(hello.substring(0, hello.length() - 1));
            } else {
                if (hello.endsWith(" am | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                } else if (hello.endsWith(" pm | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                }
            }

        }

        String[] datee = startFrom.split("-");
        String dayy = datee[2];
        String monthh = datee[1];
        String yearr = datee[0];


        commonid = String.valueOf(System.currentTimeMillis());

        String date = startFrom;
        ContentValues values = new ContentValues();


        values.put("medicineName", listCurrent.get(0).getMedicineName());
        values.put("doctorName", listCurrent.get(0).getDoctorName());
        values.put("quantity", listCurrent.get(0).getInterval());
        values.put("noOfDays", duration);
        values.put("interval", interval);
        values.put("noOfDosage", doages);
        values.put("type", listCurrent.get(0).getType());
        values.put("status", "pending");
        values.put("noOfDaysInWeek", addDays);
        if (isEdit) {

            commonid = medicineReminderId;
            values.put("medicineReminderId", commonid);
            values.put("edit", "1");
            isEdit = false;
        } else {
            values.put("medicineReminderId", commonid);
            values.put("edit", "0");
        }


        if (listCurrent.get(0).isBaMealAfter() == true) {
            values.put("afterMeal", 1);
        } else {
            values.put("afterMeal", 0);
        }
        if (listCurrent.get(0).isBaMealBefore() == true) {
            values.put("beforeMeal", 1);
        } else {
            values.put("beforeMeal", 0);
        }
        values.put("dayOfMonth", dayy);
        values.put("monthValue", monthh);
        values.put("year", yearr);
        values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
        values.put("isUploaded", "1");
        values.put("common_id", commonid);

        values.put("currentdate", date);//current date ya any other date confusion //Utils.getTodayDate()

        values.put("enddate", enddateEdit);
        values.put("alarmTime", newTime);
        DbOperations.insertMedicineRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid);//, date

        try {
            ContentValues cv = new ContentValues();
            cv.put("doctorName", listCurrent.get(0).getDoctorName());
            cv.put("isUploaded", "0");
            cv.put("cfuuhid", AppPreference.getInstance().getcf_uuhid());
            cv.put("common_id", commonid);
            cv.put("case_id", "1");   //1-medicine reminder doctor name   2-doctor reminder doctor name  3-lab reminder doctor name

            DbOperations.insertDoctorName(CureFull.getInstanse().getActivityIsntanse(), cv, commonid, AppPreference.getInstance().getcf_uuhid(), "1");

        } catch (Exception e) {
            e.getMessage();
        }


        CureFull.getInstanse().getActivityIsntanse().onBackPressed();


    }

    private void setMedReminderDetailsLocal() {

        for (int i = 0; i < listCurrent.size(); i++) {
            if (listCurrent.get(i).getType().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Type");
                return;
            } else if (listCurrent.get(i).getDoctorName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Doctor Name");
                return;
            } else if (listCurrent.get(i).getMedicineName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Medicine Name");
                return;
            } else if (listCurrent.get(i).getInterval() == 0) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Quantity");
                return;
            } else if (listCurrent.get(i).isBaMealBefore() == false && listCurrent.get(i).isBaMealAfter() == false) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Meal");
                return;
            }
        }
        if (!validateDate()) {
            btnClick = true;
            return;
        }
        if (!validateDuration()) {
            btnClick = true;
            return;
        }
        if (!validateDays()) {
            btnClick = true;
            return;
        }
        if (!validateDoages()) {
            btnClick = true;
            return;
        }
        String newTime = "";
        String hello = "";

        for (int i = 0; i < view_text_page.length; i++) {
            hello = "" + view_text_page[i].getText();
            if (i == (view_text_page.length - 1)) {
                newTime += get24hrsFormat(hello.substring(0, hello.length() - 1));
            } else {
                if (hello.endsWith(" am | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                } else if (hello.endsWith(" pm | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                }
            }

        }

        String[] datee = startFrom.split("-");
        String dayy = datee[2];
        String monthh = datee[1];
        String yearr = datee[0];

        for (int i = 0; i < listCurrent.size(); i++) {
            commonid = String.valueOf(System.currentTimeMillis());
            int days = Integer.parseInt(duration);
            //String date = startFrom;
            String endDate = "";
            for (int i1 = 0; i1 < days; i1++) {
                if (i1 == 0) {
                    endDate = Utils.getNextDate(startFrom);//for next date
                } else {
                    endDate = Utils.getNextDate(endDate);//for next date
                }

            }
            ContentValues values = new ContentValues();


            values.put("medicineName", listCurrent.get(i).getMedicineName());
            values.put("doctorName", listCurrent.get(i).getDoctorName());
            values.put("quantity", listCurrent.get(i).getInterval());
            values.put("noOfDays", duration);
            values.put("interval", interval);
            values.put("noOfDosage", doages);
            values.put("type", listCurrent.get(i).getType());
            values.put("status", "pending");///doubt
            values.put("noOfDaysInWeek", addDays);
            if (isEdit) {

                commonid = medicineReminderId;
                values.put("medicineReminderId", commonid);
                values.put("edit", "1");
                isEdit = false;
            } else {
                values.put("medicineReminderId", commonid);
                values.put("edit", "0");
            }


            if (listCurrent.get(i).isBaMealAfter() == true) {
                values.put("afterMeal", 1);
            } else {
                values.put("afterMeal", 0);
            }
            if (listCurrent.get(i).isBaMealBefore() == true) {
                values.put("beforeMeal", 1);
            } else {
                values.put("beforeMeal", 0);
            }
            values.put("dayOfMonth", dayy);
            values.put("monthValue", monthh);
            values.put("year", yearr);
            values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
            values.put("isUploaded", "1");
            values.put("common_id", commonid);

            values.put("currentdate", startFrom);//start date
            values.put("enddate", endDate);
            values.put("alarmTime", newTime);
            //newTime
            DbOperations.insertMedicineRemiderLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid);

            try {
                ContentValues cv = new ContentValues();
                cv.put("doctorName", listCurrent.get(i).getDoctorName());
                cv.put("isUploaded", "0");
                cv.put("cfuuhid", AppPreference.getInstance().getcf_uuhid());
                cv.put("common_id", commonid);
                cv.put("case_id", "1");   //1-medicine reminder doctor name   2-doctor reminder doctor name  3-lab reminder doctor name

                DbOperations.insertDoctorName(CureFull.getInstanse().getActivityIsntanse(), cv, commonid, AppPreference.getInstance().getcf_uuhid(), "1");

            } catch (Exception e) {
                e.getMessage();
            }

        }


        CureFull.getInstanse().getActivityIsntanse().onBackPressed();

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
        reminderItem.setShow(false);
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
        for (int i = 0; i < listCurrent.size(); i++) {
            if (listCurrent.get(i).getType().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Type");
                return;
            } else if (listCurrent.get(i).getDoctorName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Doctor Name");
                return;
            } else if (listCurrent.get(i).getMedicineName().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Medicine Name");
                return;
            } else if (listCurrent.get(i).getInterval() == 0) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Quantity");
                return;
            } else if (listCurrent.get(i).isBaMealBefore() == false && listCurrent.get(i).isBaMealAfter() == false) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Meal");
                return;
            }
        }
        if (!validateDate()) {
            btnClick = true;
            return;
        }
        if (!validateDuration()) {
            btnClick = true;
            return;
        }
        if (!validateDays()) {
            btnClick = true;
            return;
        }
        if (!validateDoages()) {
            btnClick = true;
            return;
        }
        String newTime = "";
        String hello = "";

        for (int i = 0; i < view_text_page.length; i++) {
            hello = "" + view_text_page[i].getText();
            if (i == (view_text_page.length - 1)) {
                newTime += get24hrsFormat(hello.substring(0, hello.length() - 1));
            } else {
                if (hello.endsWith(" am | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                } else if (hello.endsWith(" pm | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                }
            }

        }


        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        JSONObject data = JsonUtilsObject.setRemMedAdd(startFrom, duration, doages, addDays, listCurrent, newTime, interval);
//        Log.e("jsonUploadMedRem", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_MEDICINE_REM, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btnClick = true;
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        Log.e("MedRemDetails, URL 3.", response.toString());
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
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


    public void setMedReminderDetailsEdit() {

        for (int i = 0; i < listCurrent.size(); i++) {
            if (listCurrent.get(i).getType().equalsIgnoreCase("")) {
                btnClick = true;
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Type");
                return;
            } else if (listCurrent.get(i).getDoctorName().equalsIgnoreCase("")) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Doctor Name");
                btnClick = true;
                return;
            } else if (listCurrent.get(i).getMedicineName().equalsIgnoreCase("")) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Fill Medicine Name");
                btnClick = true;
                return;
            } else if (listCurrent.get(i).getInterval() == 0) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Quantity");
                btnClick = true;
                return;
            } else if (listCurrent.get(i).isBaMealBefore() == false && listCurrent.get(i).isBaMealAfter() == false) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Select Meal");
                btnClick = true;
                return;
            }

        }
        if (!validateDate()) {
            btnClick = true;
            return;
        }
        if (!validateDuration()) {
            btnClick = true;
            return;
        }
        if (!validateDays()) {
            btnClick = true;
            return;
        }
        if (!validateDoages()) {
            btnClick = true;
            return;
        }
        String newTime = "";
        String hello = "";

        for (int i = 0; i < view_text_page.length; i++) {
            hello = "" + view_text_page[i].getText();
            if (i == (view_text_page.length - 1)) {
                newTime += get24hrsFormat(hello.substring(0, hello.length() - 1));
            } else {
                if (hello.endsWith(" am | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                } else if (hello.endsWith(" pm | ")) {
                    newTime += get24hrsFormat(hello.substring(0, hello.length() - 2)) + ",";
                }
            }

        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        JSONObject data = JsonUtilsObject.setRemMedEdit(medicineReminderId, startFrom, duration, doages, addDays, listCurrent, newTime, interval, "pending");
//        Log.e("jsonUploadMedEdit", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.EDIT_MEDICINE_REM, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        btnClick = true;
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        Log.e("MedRemDetails, URL 3.", response.toString());
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

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
//        Log.e("state", " " + "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth));
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
    public void showPage(final int totalPage, ArrayList<ReminderMedicnceDoagePer> timeToTakeMedicne) {

        if (timeToTakeMedicne == null) {
            interval = (Double.parseDouble("" + 14) / Double.parseDouble("" + totalPage));
//            Log.e("value", "" + new DecimalFormat("##.#").format(interval) + " " + (14 / totalPage));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            view_text_page = new CustomTextViewOpenSanRegular[totalPage];
            double text = 0;
            for (int i = 0; i < totalPage; i++) {
                if (i == 0) {
                    text = 9;
                } else {
                    double value = Double.parseDouble(new DecimalFormat("#.#").format(interval));
//                    Log.e("value ", " " + value);
                    text = text + value;
                }

                text = Double.parseDouble(new DecimalFormat("#.#").format(text));

                view_text_page[i] = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
//            CustomTextViewOpenSanRegular view_text_page = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
                view_text_page[i].setGravity(Gravity.CENTER_VERTICAL);
                view_text_page[i].setTextSize(12);
                view_text_page[i].setSingleLine(true);
                view_text_page[i].setId(i);
                view_text_page[i].setLayoutParams(params);
//                Log.e("i ", " " + i);
                if (i == (totalPage - 1)) {
                    String[] time = String.valueOf(text).split("\\.");
                    String hrs = time[0];
                    String min = "0";
                    if (time.length > 1) {
                        min = time[1] + "0";
                        if (Integer.parseInt(min) >= 50) {
                            min = "" + 30;
                        }
                    }
                    view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(hrs), Integer.parseInt(min)) + ".");
                } else {
//                    Log.e("new ", " " + text);
                    String[] time = String.valueOf(text).split("\\.");
                    String hrs = time[0];
                    String min = "0";
                    if (time.length > 1) {
                        min = time[1] + "0";
                        if (Integer.parseInt(min) >= 50) {
                            min = "" + 30;
                        }
                    }
                    view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(hrs), Integer.parseInt(min)) + " | ");
                }
                view_text_page[i].setTextColor(Color.parseColor("#a5a5a5"));
                view_text_page[i].setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                final int finalI = i;
                view_text_page[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String time = "" + view_text_page[finalI].getText();
//                        Log.e("time", " " + time);
                        if (time.endsWith(" am | ")) {
                            time = time.substring(0, time.length() - 6);
                        } else if (time.endsWith(" pm | ")) {
                            time = time.substring(0, time.length() - 6);
                        } else if (time.endsWith(" am.")) {
                            time = time.substring(0, time.length() - 4);
                        } else if (time.endsWith(" pm.")) {
                            time = time.substring(0, time.length() - 4);
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
                                    view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + ".");
                                } else {
                                    view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + " | ");

                                }
                            }
                        }, hour1, minute1, false);
                        timePickerDialog1.show();
//                    pageNo = view.getId();

                    }
                });
                linear_page_count.addView(view_text_page[i]);
            }
        } else {

            String med = "";

            for (int i = 0; i < timeToTakeMedicne.size(); i++) {
                if (timeToTakeMedicne.get(i).getReminderMedicnceTimes() != null) {
                    for (int j = 0; j < timeToTakeMedicne.get(i).getReminderMedicnceTimes().size(); j++) {
                        int hrs1 = timeToTakeMedicne.get(i).getReminderMedicnceTimes().get(j).getHour();
                        int mins1 = timeToTakeMedicne.get(i).getReminderMedicnceTimes().get(j).getMinute();
                        med += hrs1 + ":" + mins1 + ",";
                    }
                }

            }
            String[] timeToMedo = med.split(",");
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            params.setMargins(0, 0, 0, 0);
            view_text_page = new CustomTextViewOpenSanRegular[timeToMedo.length];

            for (int i = 0; i < timeToMedo.length; i++) {
                view_text_page[i] = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
//            CustomTextViewOpenSanRegular view_text_page = new CustomTextViewOpenSanRegular(CureFull.getInstanse().getActivityIsntanse());
                view_text_page[i].setGravity(Gravity.CENTER_VERTICAL);
                view_text_page[i].setTextSize(12);
                view_text_page[i].setSingleLine(true);
                view_text_page[i].setId(i);
                view_text_page[i].setLayoutParams(params);
//                Log.e("i ", " " + i);
                if (i == (timeToMedo.length - 1)) {
                    String[] text = timeToMedo[i].split(":");
                    view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(text[0]), Integer.parseInt(text[1])) + ".");
                } else {
                    String[] text = timeToMedo[i].split(":");
                    view_text_page[i].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(Integer.parseInt(text[0]), Integer.parseInt(text[1])) + " | ");
                }
                view_text_page[i].setTextColor(Color.parseColor("#a5a5a5"));
                view_text_page[i].setPaintFlags(txt_dogaes.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
                final int finalI = i;
                view_text_page[i].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String time = "" + view_text_page[finalI].getText();
//                        Log.e("time", " " + time);
                        if (time.endsWith(" am | ")) {
                            time = time.substring(0, time.length() - 5);
                        } else if (time.endsWith(" pm | ")) {
                            time = time.substring(0, time.length() - 5);
                        } else if (time.endsWith(" am.")) {
                            time = time.substring(0, time.length() - 4);
                        } else if (time.endsWith(" pm.")) {
                            time = time.substring(0, time.length() - 4);
                        }
                        String[] hello = time.split(":");
                        String hour = hello[0];
                        String mintue = hello[1];

                        int hour1 = Integer.parseInt(hour);
                        // Current Minute
                        int minute1 = Integer.parseInt(mintue.trim());

                        TimePickerDialog timePickerDialog1 = new TimePickerDialog(CureFull.getInstanse().getActivityIsntanse(), new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                if (finalI == (totalPage - 1)) {
                                    view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + ".");
                                } else {
                                    view_text_page[finalI].setText("" + CureFull.getInstanse().getActivityIsntanse().updateTimeSpace(hourOfDay, minute) + " | ");

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


    public String get24hrsFormat(String time) {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            String[] times = time.split(" ");
            String tme = times[0];
            String ampm = times[1];
            if (ampm.trim().equalsIgnoreCase("am")) {
                tme = tme + " a.m.";
            } else {
                tme = tme + " p.m.";
            }
            String timeNew = "";
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm aa", Locale.UK);
            Date date = null;
            try {
                date = parseFormat.parse(tme);
                timeNew = displayFormat.format(date);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return timeNew;
        } else {
            SimpleDateFormat displayFormat = new SimpleDateFormat("HH:mm");
            SimpleDateFormat parseFormat = new SimpleDateFormat("hh:mm a");
            Date date = null;
            try {
                date = parseFormat.parse(time);
//                Log.e("format ", parseFormat.format(date) + " = " + displayFormat.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            return displayFormat.format(date);
        }


    }

    private void rotatePhoneClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_anti_rotate_anim);
        imageView.startAnimation(rotate);
    }

    private void rotatePhoneAntiClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_rotate_anim);
        imageView.startAnimation(rotate);
    }
}



