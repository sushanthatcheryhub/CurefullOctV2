package fragment.healthapp;


import android.animation.Animator;
import android.app.DatePickerDialog;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.Reminder_medicine_Docotr_child_ListAdpter;
import adpter.Reminder_medicine_Self_ListAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.LabDoctorName;
import item.property.MedicineReminderListView;
import item.property.ReminderDoctorName;
import item.property.Reminder_SelfListView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.HandlePermission;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentReminderMedicine extends Fragment implements View.OnClickListener, PopupWindow.OnDismissListener, DatePickerDialog.OnDateSetListener {


    private View rootView;
    private RelativeLayout relative_bottom_next, realtive_today, rel_set_reminder, liner_filter_by, realtive_notesShort, txt_doctor_name;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView_doctor, recyclerView_self;
    private Reminder_medicine_Docotr_child_ListAdpter reminder_medicine_docotr_child_listAdpter;
    private Reminder_medicine_Self_ListAdpter reminder_medicine_self_listAdpter;
    private TextView text_date, txt_date_dialog, txt_self, txt_no_medicine, txt_reminder, txt_status, txt_doctor_name_txt;
    boolean flagShort = true, isReset = true;
    private LinearLayout liner_dialog, revealViewShort, layoutButtonsShort, txt_filter_reminder, txt_filter_status, btn_reset, btn_apply;
    private float pixelDensity;
    private ListPopupWindow listPopupWindow4;
    private RadioGroup radioReminder;
    private RadioButton radioCurefull, radioSelf;
    private String startFrom = "";
    private RadioGroup radioStatus;
    private RadioButton radioPending, radioDone;

    private ImageView img_filter, img_calender, img_user_name;
    private String date = "N/A", reminder = "N/A", status = "N/A", doctorName = "N/A";
    private ArrayList<LabDoctorName> LabDoctorName;

    private TextView btn_history, btn_next;
    private int pageNo = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reminder_medicine,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(false);


        AppPreference.getInstance().setFragmentMedicine(true);
        AppPreference.getInstance().setFragmentDoctorVisit(false);
        AppPreference.getInstance().setFragmentLabTst(false);

        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(false);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(false);

        CureFull.getInstanse().getActivityIsntanse().selectedNav(3);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        pixelDensity = getResources().getDisplayMetrics().density;
        text_date = (TextView) rootView.findViewById(R.id.text_date);
        txt_date_dialog = (TextView) rootView.findViewById(R.id.txt_date_dialog);
        btn_history = (TextView) rootView.findViewById(R.id.btn_history);
        btn_next = (TextView) rootView.findViewById(R.id.btn_next);

        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);
        img_filter = (ImageView) rootView.findViewById(R.id.img_filter);
        img_calender = (ImageView) rootView.findViewById(R.id.img_calender);

        radioReminder = (RadioGroup) rootView.findViewById(R.id.radioReminder);
        radioCurefull = (RadioButton) rootView.findViewById(R.id.radioCurefull);
        radioSelf = (RadioButton) rootView.findViewById(R.id.radioSelf);

        radioStatus = (RadioGroup) rootView.findViewById(R.id.radioStatus);
        radioPending = (RadioButton) rootView.findViewById(R.id.radioPending);
        radioDone = (RadioButton) rootView.findViewById(R.id.radioDone);

        liner_dialog = (LinearLayout) rootView.findViewById(R.id.liner_dialog);
        txt_filter_reminder = (LinearLayout) rootView.findViewById(R.id.txt_filter_reminder);
        txt_filter_status = (LinearLayout) rootView.findViewById(R.id.txt_filter_status);
        btn_reset = (LinearLayout) rootView.findViewById(R.id.btn_reset);
        btn_apply = (LinearLayout) rootView.findViewById(R.id.btn_apply);
        txt_doctor_name_txt = (TextView) rootView.findViewById(R.id.txt_doctor_name_txt);
        txt_reminder = (TextView) rootView.findViewById(R.id.txt_reminder);
        txt_status = (TextView) rootView.findViewById(R.id.txt_status);
        relative_bottom_next = (RelativeLayout) rootView.findViewById(R.id.relative_bottom_next);
        realtive_today = (RelativeLayout) rootView.findViewById(R.id.realtive_today);
        txt_doctor_name = (RelativeLayout) rootView.findViewById(R.id.txt_doctor_name);
        revealViewShort = (LinearLayout) rootView.findViewById(R.id.linearViewShort);
        layoutButtonsShort = (LinearLayout) rootView.findViewById(R.id.layoutButtonsShort);
        realtive_notesShort = (RelativeLayout) rootView.findViewById(R.id.realtive_notesShort);
        liner_filter_by = (RelativeLayout) rootView.findViewById(R.id.liner_filter_by);
        txt_no_medicine = (TextView) rootView.findViewById(R.id.txt_no_medicine);
        txt_self = (TextView) rootView.findViewById(R.id.txt_self);
        rel_set_reminder = (RelativeLayout) rootView.findViewById(R.id.rel_set_reminder);
        rel_set_reminder.setOnClickListener(this);
        recyclerView_doctor = (RecyclerView) rootView.findViewById(R.id.recyclerView_doctor);
        recyclerView_self = (RecyclerView) rootView.findViewById(R.id.recyclerView_self);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_doctor.setLayoutManager(mLayoutManager);
        recyclerView_doctor.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager1 = new LinearLayoutManager(getActivity());
        recyclerView_self.setLayoutManager(mLayoutManager1);
        recyclerView_doctor.setHasFixedSize(true);
        liner_filter_by.setOnClickListener(this);
        realtive_today.setOnClickListener(this);
        btn_history.setOnClickListener(this);
        btn_next.setOnClickListener(this);

        txt_filter_reminder.setOnClickListener(this);
        txt_filter_status.setOnClickListener(this);
        btn_reset.setOnClickListener(this);
        btn_apply.setOnClickListener(this);
        revealViewShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTwitterShort(rootView);
            }
        });


        radioReminder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioCurefull) {
                    reminder = "curefull";
                } else if (checkedId == R.id.radioSelf) {
                    reminder = "self";
                }
            }
        });
        radioStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioPending) {
                    status = "pending";
                } else if (checkedId == R.id.radioDone) {
                    status = "complete";
                }
            }
        });
        txt_filter_reminder.setBackgroundResource(R.color.health_yellow);
        txt_filter_status.setBackgroundResource(R.color.transprent_new);
        txt_reminder.setTextColor(getResources().getColor(R.color.health_red_drawer));
        txt_status.setTextColor(getResources().getColor(R.color.health_yellow));
        radioReminder.setVisibility(View.VISIBLE);
        radioStatus.setVisibility(View.GONE);
        String[] dateAll = Utils.getTodayDate().split("-");
        int year = Integer.parseInt(dateAll[0]);
        int mnt = Integer.parseInt(dateAll[1]);
        int dayOfMonth = Integer.parseInt(dateAll[2]);
        txt_date_dialog.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
        startFrom = Utils.getTodayDate();
        date = Utils.getTodayDate();
        getDoctorName();
        getReminderMedicine();

        (rootView.findViewById(R.id.txt_doctor_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (LabDoctorName != null && LabDoctorName.size() > 0) {
                    rotatePhoneClockwise(img_user_name);
                    listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getUserAsStringList(LabDoctorName)));
                    listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_doctor_name_txt));
                    listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._70dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow4.setModal(true);
                    listPopupWindow4.setOnDismissListener(FragmentReminderMedicine.this);
                    listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                    listPopupWindow4.show();
                }

            }
        });
        txt_date_dialog.setPaintFlags(txt_date_dialog.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_doctor_name_txt.setSelected(true);
        return rootView;
    }

    AdapterView.OnItemClickListener popUpItemClickUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("yes user", "yes user");
            rotatePhoneAntiClockwise(img_user_name);
            listPopupWindow4.dismiss();
            if (LabDoctorName != null && LabDoctorName.size() > 0) {
//                realtive_today.setVisibility(View.GONE);
                doctorName = LabDoctorName.get(position).getDoctorName();
                txt_doctor_name_txt.setText("" + LabDoctorName.get(position).getDoctorName());
                date = "N/A";
                reminder = "N/A";
                status = "N/A";
                getReminderMedicine();
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

//            case R.id.realtive_today:
//                final int year, day;
//                int month1;
//                final Calendar c1 = Calendar.getInstance();
//                Log.e("Age ", ":- " + AppPreference.getInstance().getGoalAge());
//                if (startFrom.equalsIgnoreCase("")) {
//                    year = c1.get(Calendar.YEAR);
//                    month1 = c1.get(Calendar.MONTH);
//                    day = c1.get(Calendar.DAY_OF_MONTH);
//                } else {
//                    Log.e("ye wala ", " ye wlal");
//                    String[] dateFormat = startFrom.split("-");
//                    year = Integer.parseInt(dateFormat[0]);
//                    month1 = Integer.parseInt(dateFormat[1]);
//                    day = Integer.parseInt(dateFormat[2]);
//                    month1 = (month1 - 1);
//                }
//                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentReminderMedicine.this, year, month1, day);
//                newDateDialog.getDatePicker().setSpinnersShown(true);
////                c.add(Calendar.DATE, 1);
//                Date newDate = c1.getTime();
////                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
//                newDateDialog.show();
//
//                break;
            case R.id.btn_next:
                if (btn_next.getText().toString().equalsIgnoreCase("Next ")) {
                    btn_next.setText("Next");
                    getReminderMedicine();
                } else {
                    text_date.setVisibility(View.INVISIBLE);
                    btn_history.setText("Previous");
                    date = getNextDate(startFrom);
                    startFrom = date;
                    String[] dateAll = date.split("-");
                    int years = Integer.parseInt(dateAll[0]);
                    int mnt = Integer.parseInt(dateAll[1]);
                    int dayOfMonth = Integer.parseInt(dateAll[2]);
                    txt_date_dialog.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + years);
                    getReminderMedicine();
                }
                break;

            case R.id.btn_history:
                if (btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                    date = getNextPrevious(startFrom);
                    startFrom = date;
                    String[] dateAll1 = date.split("-");
                    int years1 = Integer.parseInt(dateAll1[0]);
                    int mnt1 = Integer.parseInt(dateAll1[1]);
                    int dayOfMonth1 = Integer.parseInt(dateAll1[2]);
                    txt_date_dialog.setText("" + (dayOfMonth1 < 10 ? "0" + dayOfMonth1 : dayOfMonth1) + "/" + (mnt1 < 10 ? "0" + mnt1 : mnt1) + "/" + years1);
                    getReminderMedicine();
                } else {
                    btn_next.setText("Next ");
                    liner_dialog.setVisibility(View.GONE);
                    realtive_today.setVisibility(View.GONE);
                    btn_history.setVisibility(View.GONE);
                    getHistoryMedicine();
                }
                if (Utils.getTodayDate().equalsIgnoreCase(startFrom)) {
                    btn_history.setText("History");
                    text_date.setVisibility(View.VISIBLE);
                } else {
                    btn_history.setText("Previous");
                }

                break;

            case R.id.txt_filter_reminder:
                txt_filter_reminder.setBackgroundResource(R.color.health_yellow);
                txt_filter_status.setBackgroundResource(R.color.transprent_new);
                txt_reminder.setTextColor(getResources().getColor(R.color.health_red_drawer));
                txt_status.setTextColor(getResources().getColor(R.color.health_yellow));
                radioReminder.setVisibility(View.VISIBLE);
                radioStatus.setVisibility(View.GONE);
                break;
            case R.id.txt_filter_status:
                txt_filter_reminder.setBackgroundResource(R.color.transprent_new);
                txt_filter_status.setBackgroundResource(R.color.health_yellow);
                txt_reminder.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_status.setTextColor(getResources().getColor(R.color.health_red_drawer));
                radioReminder.setVisibility(View.GONE);
                radioStatus.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_reset:
                if (isReset) {

                    isReset = false;
                    realtive_today.setVisibility(View.VISIBLE);
                    launchTwitterShort(rootView);
                    radioPending.setChecked(false);
                    radioCurefull.setChecked(false);
                    radioDone.setChecked(false);
                    radioSelf.setSelected(false);
                    reminder = "N/A";
                    status = "N/A";
                    doctorName = "N/A";
                    txt_doctor_name_txt.setText("Doctor Name");
                    date = Utils.getTodayDate();
                    getReminderMedicine();
                }

                break;
            case R.id.btn_apply:
//                realtive_today.setVisibility(View.GONE);
                date = "N/A";
                launchTwitterShort(rootView);
                getReminderMedicine();
                break;
            case R.id.rel_set_reminder:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_calender);
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentReminderSetMedicine(), true);
                break;
            case R.id.liner_filter_by:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_filter);
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                        txt_no_medicine.post(new Runnable() {
                            @Override
                            public void run() {
                                launchTwitterShort(rootView);
                            }
                        });
                    }
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);

                }


                break;
        }
    }


    private void getReminderMedicine() {
        liner_dialog.setVisibility(View.VISIBLE);
        realtive_today.setVisibility(View.VISIBLE);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        Log.e("reminder", " " + reminder);
        Log.e("date", " " + date);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LIST_OF_MED + "cfuuhId=" + AppPreference.getInstance().getcf_uuhid() + "&date=" + date + "&status=" + status + "&reminderType=" + reminder + "&doctorName=" + doctorName,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        isReset = true;
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("rem, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                MedicineReminderListView medicineReminderListView = ParseJsonData.getInstance().getReminderMedicineList(response.toString());
                                if (medicineReminderListView != null) {
                                    txt_no_medicine.setVisibility(View.GONE);
                                    if (medicineReminderListView.getReminderDoctorNames() != null & medicineReminderListView.getReminder_selfListViews() != null) {
                                        txt_no_medicine.setVisibility(View.GONE);
                                        if (medicineReminderListView.getReminderDoctorNames().size() == 0 && medicineReminderListView.getReminder_selfListViews().size() == 0) {
                                            txt_no_medicine.setVisibility(View.VISIBLE);
                                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                                relative_bottom_next.setVisibility(View.VISIBLE);
                                            } else {
//                                                btn_next.setVisibility(View.GONE);
                                            }
                                        } else {
//                                            btn_next.setVisibility(View.VISIBLE);
                                            btn_history.setVisibility(View.VISIBLE);
                                        }
                                        if (medicineReminderListView.getReminderDoctorNames().size() > 0) {
                                            setDoctorAdpter(medicineReminderListView.getReminderDoctorNames(), "No");
                                            recyclerView_doctor.setVisibility(View.VISIBLE);
                                            relative_bottom_next.setVisibility(View.VISIBLE);

                                        } else {
                                            recyclerView_doctor.setVisibility(View.GONE);
                                        }
                                        if (medicineReminderListView.getReminder_selfListViews().size() > 0) {
                                            txt_self.setVisibility(View.VISIBLE);
                                            setSelfMedAdpter(medicineReminderListView.getReminder_selfListViews(), "No");
                                            recyclerView_self.setVisibility(View.VISIBLE);
                                            relative_bottom_next.setVisibility(View.VISIBLE);
                                        } else {
                                            txt_self.setVisibility(View.GONE);
                                            recyclerView_self.setVisibility(View.GONE);
                                        }
                                    } else {
                                        if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                            relative_bottom_next.setVisibility(View.VISIBLE);
                                        }
                                        txt_no_medicine.setVisibility(View.VISIBLE);
                                    }


                                } else {
                                    if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                        relative_bottom_next.setVisibility(View.GONE);
                                    }
                                    txt_no_medicine.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                    relative_bottom_next.setVisibility(View.GONE);
                                }
                                txt_no_medicine.setVisibility(View.VISIBLE);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        isReset = true;
                        Log.e("error", " " + error.getMessage());
                        txt_no_medicine.setVisibility(View.VISIBLE);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
    }

    public void setDoctorAdpter(ArrayList<ReminderDoctorName> reminder_doctorListViews, String no) {
        reminder_medicine_docotr_child_listAdpter = new Reminder_medicine_Docotr_child_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews, no);
        recyclerView_doctor.setAdapter(reminder_medicine_docotr_child_listAdpter);
        reminder_medicine_docotr_child_listAdpter.notifyDataSetChanged();
    }

    public void setSelfMedAdpter(ArrayList<Reminder_SelfListView> reminder_doctorListViews, String no) {
        reminder_medicine_self_listAdpter = new Reminder_medicine_Self_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews, no);
        recyclerView_self.setAdapter(reminder_medicine_self_listAdpter);
        reminder_medicine_self_listAdpter.notifyDataSetChanged();
    }


    public void launchTwitterShort(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */


        int x = realtive_notesShort.getLeft();
        int y = realtive_notesShort.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notesShort.getWidth(), realtive_notesShort.getHeight());
        try {
            if (flagShort) {
//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                            revealViewShort.getLayoutParams();
                    parameters.height = realtive_notesShort.getHeight();
                    revealViewShort.setLayoutParams(parameters);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewShort, x, y, 0, hypotenuse);
                    anim.setDuration(700);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            layoutButtonsShort.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    revealViewShort.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    revealViewShort.setVisibility(View.VISIBLE);
                    layoutButtonsShort.setVisibility(View.VISIBLE);
                }


                flagShort = false;
            } else {
//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewShort, x, y, hypotenuse, 0);
                    anim.setDuration(400);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealViewShort.setVisibility(View.GONE);
                            layoutButtonsShort.setVisibility(View.GONE);
                        }

                        @Override
                        public void onAnimationCancel(Animator animator) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animator) {

                        }
                    });

                    anim.start();
                } else {
                    revealViewShort.setVisibility(View.GONE);
                    layoutButtonsShort.setVisibility(View.GONE);
                }

                flagShort = true;
            }
        } catch (Exception e) {

        }


    }


    private void getDoctorName() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LIST_DOCTOR_NAME_MEDICINE + "" + AppPreference.getInstance().getcf_uuhid(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSymptomsList, URL 1.", response);
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
                            LabDoctorName = ParseJsonData.getInstance().getLabDoctorName(response);
                        } else {
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    private void rotatePhoneClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_anti_rotate_anim);
        imageView.startAnimation(rotate);
    }

    private void rotatePhoneAntiClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_rotate_anim);
        imageView.startAnimation(rotate);
    }

    @Override
    public void onDismiss() {
        rotatePhoneAntiClockwise(img_user_name);
    }


    private List<String> getUserAsStringList(List<LabDoctorName> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (LabDoctorName logy : result)
                list.add(logy.getDoctorName());
        return list;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        startFrom = "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        date = "" + year + "-" + (mnt < 10 ? "0" + mnt : mnt) + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        txt_date_dialog.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + year);
        getReminderMedicine();
        if (Utils.getTodayDate().equalsIgnoreCase(startFrom)) {
            btn_history.setText("History");
            text_date.setVisibility(View.VISIBLE);
        } else {
            btn_history.setText("Previous");
        }

    }

    public String getNextDate(String date) {
        String dt = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, 1);  // number of days to add
            dt = sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }


    public String getNextPrevious(String date) {
        String dt = date;  // Start date
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
            c.add(Calendar.DATE, -1);  // number of days to add
            dt = sdf.format(c.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dt;
    }


    private void getHistoryMedicine() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.MEDICINCE_HISTORY_API + "" + AppPreference.getInstance().getcf_uuhid() + "&pageNo=" + pageNo + "&noOfRecord=10&historyDays=7",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("rem, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                MedicineReminderListView medicineReminderListView = ParseJsonData.getInstance().getReminderMedicineList(response.toString());
                                if (medicineReminderListView != null) {
                                    txt_no_medicine.setVisibility(View.GONE);
                                    if (medicineReminderListView.getReminderDoctorNames() != null & medicineReminderListView.getReminder_selfListViews() != null) {
                                        txt_no_medicine.setVisibility(View.GONE);
                                        if (medicineReminderListView.getReminderDoctorNames().size() == 0 && medicineReminderListView.getReminder_selfListViews().size() == 0) {
                                            txt_no_medicine.setVisibility(View.VISIBLE);
                                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                                relative_bottom_next.setVisibility(View.GONE);
                                            } else {
                                                btn_next.setVisibility(View.VISIBLE);
                                            }
                                        }
                                        if (medicineReminderListView.getReminderDoctorNames().size() > 0) {
                                            setDoctorAdpter(medicineReminderListView.getReminderDoctorNames(), "Yes");
                                            recyclerView_doctor.setVisibility(View.VISIBLE);
                                            relative_bottom_next.setVisibility(View.VISIBLE);
                                            btn_next.setVisibility(View.VISIBLE);
                                        } else {
                                            recyclerView_doctor.setVisibility(View.GONE);
                                        }
                                        if (medicineReminderListView.getReminder_selfListViews().size() > 0) {
                                            txt_self.setVisibility(View.VISIBLE);
                                            setSelfMedAdpter(medicineReminderListView.getReminder_selfListViews(), "Yes");
                                            recyclerView_self.setVisibility(View.VISIBLE);
                                            relative_bottom_next.setVisibility(View.VISIBLE);
                                            btn_next.setVisibility(View.VISIBLE);
                                        } else {
                                            txt_self.setVisibility(View.GONE);
                                            recyclerView_self.setVisibility(View.GONE);
                                        }
                                    } else {
                                        if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                            relative_bottom_next.setVisibility(View.GONE);
                                        }
                                        txt_no_medicine.setVisibility(View.VISIBLE);
                                    }


                                } else {
                                    if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                        relative_bottom_next.setVisibility(View.GONE);
                                    }
                                    txt_no_medicine.setVisibility(View.VISIBLE);
                                }

                            } else {
                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                    relative_bottom_next.setVisibility(View.GONE);
                                }
                                txt_no_medicine.setVisibility(View.VISIBLE);
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
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("error", " " + error.getMessage());
                        txt_no_medicine.setVisibility(View.VISIBLE);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
    }

}