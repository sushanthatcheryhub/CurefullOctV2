package fragment.healthapp;


import android.animation.Animator;
import android.graphics.Paint;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import adpter.Reminder_Lab_Docotr_child_ListAdpter;
import adpter.Reminder_Lab_Docotr_child_ListAdpter_Localdb;
import adpter.Reminder_Lab_Self_ListAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.LabDoctorName;
import item.property.LabReportListView;
import item.property.LabTestReminderDoctorName;
import item.property.LabTestReminderListView;
import item.property.Lab_Test_Reminder_DoctorListView;
import item.property.Lab_Test_Reminder_SelfListView;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.HandlePermission;
import utils.MyConstants;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentReminderLabTest extends Fragment implements View.OnClickListener, PopupWindow.OnDismissListener {


    private View rootView;
    private RelativeLayout relative_bottom_next, realtive_today, rel_set_reminder, liner_filter_by, realtive_notesShort;
    private RecyclerView recyclerView_doctor, recyclerView_self;
    private Reminder_Lab_Docotr_child_ListAdpter reminder_medicine_docotr_child_listAdpter;
    private Reminder_Lab_Docotr_child_ListAdpter_Localdb reminder_medicine_docotr_child_listAdpter_localdb;
    private Reminder_Lab_Self_ListAdpter reminder_medicine_self_listAdpter;
    private TextView text_date, txt_date_dialog, txt_self, txt_no_medicine, txt_reminder, txt_status, txt_doctor_name_txt;
    boolean flagShort = true, isReset = true, isChecked = true;
    private LinearLayout revealViewShort, layoutButtonsShort, txt_filter_reminder, txt_filter_status, btn_reset, btn_apply;
    private float pixelDensity;
    private ListPopupWindow listPopupWindow4;
    private RadioGroup radioReminder;
    private RadioButton radioCurefull, radioSelf;

    private ImageView img_filter, img_calender, img_user_name;
    private RadioGroup radioStatus;
    private RadioButton radioPending, radioDone;
    private String date = "N/A", reminder = "N/A", status = "N/A", doctorName = "N/A";
    private ArrayList<LabDoctorName> LabDoctorName;
    private TextView btn_history, btn_next;
    private String startFrom = "";
    List<LabDoctorName> LabDoctorNamelocal = null;
    private boolean apply_flag = false;
    private boolean doctor_name_flag=false;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reminder_lab_test,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "");
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(false, "lab");
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);

        AppPreference.getInstance().setFragmentMedicine(false);
        AppPreference.getInstance().setFragmentDoctorVisit(false);
        AppPreference.getInstance().setFragmentLabTst(true);
        text_date = (TextView) rootView.findViewById(R.id.text_date);
        txt_date_dialog = (TextView) rootView.findViewById(R.id.txt_date_dialog);
        btn_history = (TextView) rootView.findViewById(R.id.btn_history);
        btn_next = (TextView) rootView.findViewById(R.id.btn_next);

        img_filter = (ImageView) rootView.findViewById(R.id.img_filter);
        img_calender = (ImageView) rootView.findViewById(R.id.img_calender);
        radioReminder = (RadioGroup) rootView.findViewById(R.id.radioReminder);
        radioCurefull = (RadioButton) rootView.findViewById(R.id.radioCurefull);
        radioSelf = (RadioButton) rootView.findViewById(R.id.radioSelf);
        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);
        radioStatus = (RadioGroup) rootView.findViewById(R.id.radioStatus);
        radioPending = (RadioButton) rootView.findViewById(R.id.radioPending);
        radioDone = (RadioButton) rootView.findViewById(R.id.radioDone);


        txt_filter_reminder = (LinearLayout) rootView.findViewById(R.id.txt_filter_reminder);
        txt_filter_status = (LinearLayout) rootView.findViewById(R.id.txt_filter_status);
        btn_reset = (LinearLayout) rootView.findViewById(R.id.btn_reset);
        btn_apply = (LinearLayout) rootView.findViewById(R.id.btn_apply);

        txt_reminder = (TextView) rootView.findViewById(R.id.txt_reminder);
        txt_status = (TextView) rootView.findViewById(R.id.txt_status);
        txt_doctor_name_txt = (TextView) rootView.findViewById(R.id.txt_doctor_name_txt);

        revealViewShort = (LinearLayout) rootView.findViewById(R.id.linearViewShort);
        layoutButtonsShort = (LinearLayout) rootView.findViewById(R.id.layoutButtonsShort);
        realtive_notesShort = (RelativeLayout) rootView.findViewById(R.id.realtive_notesShort);
        liner_filter_by = (RelativeLayout) rootView.findViewById(R.id.liner_filter_by);

        realtive_today = (RelativeLayout) rootView.findViewById(R.id.realtive_today);
        relative_bottom_next = (RelativeLayout) rootView.findViewById(R.id.relative_bottom_next);

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
        layoutButtonsShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        radioReminder.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isChecked) {
                    if (checkedId == R.id.radioCurefull) {
                        reminder = "curefull";
                    } else if (checkedId == R.id.radioSelf) {
                        reminder = "self";
                    }
                }

            }
        });
        radioStatus.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isChecked) {
                    if (checkedId == R.id.radioPending) {
                        status = "pending";
                    } else if (checkedId == R.id.radioDone) {
                        status = "complete";
                    }
                }

            }
        });
        txt_filter_reminder.setBackgroundResource(R.color.health_yellow);
        txt_filter_status.setBackgroundResource(R.color.transprent_new);
        txt_reminder.setTextColor(getResources().getColor(R.color.white));
        txt_status.setTextColor(getResources().getColor(R.color.health_dark_gray));
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
        getReminderLabTest();


        (rootView.findViewById(R.id.txt_doctor_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (LabDoctorName != null && LabDoctorName.size() > 0) {
                        rotatePhoneClockwise(img_user_name);
                        listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                        listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                                R.layout.adapter_list_doctor_data, getUserAsStringList(LabDoctorName)));
                        listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_doctor_name_txt));
                        listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._70dp));

                        listPopupWindow4.setModal(true);
                        listPopupWindow4.setOnDismissListener(FragmentReminderLabTest.this);
                        listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                        listPopupWindow4.show();
                    }
                } else {
                    if (LabDoctorNamelocal != null && LabDoctorNamelocal.size() > 0) {
                        rotatePhoneClockwise(img_user_name);
                        listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                        listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                                R.layout.adapter_list_doctor_data, getUserAsStringList(LabDoctorNamelocal)));
                        listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_doctor_name_txt));
                        listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._70dp));

                        listPopupWindow4.setModal(true);
                        listPopupWindow4.setOnDismissListener(FragmentReminderLabTest.this);
                        listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                        listPopupWindow4.show();
                    }
                }
            }
        });

        txt_date_dialog.setPaintFlags(txt_date_dialog.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        txt_doctor_name_txt.setSelected(true);
        btn_history.setVisibility(View.GONE);
        return rootView;
    }

    AdapterView.OnItemClickListener popUpItemClickUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.e("yes user", "yes user");
            rotatePhoneAntiClockwise(img_user_name);
            listPopupWindow4.dismiss();
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                if (LabDoctorName != null && LabDoctorName.size() > 0) {
//                realtive_today.setVisibility(View.GONE);
                    doctorName = LabDoctorName.get(position).getDoctorName();
                    txt_doctor_name_txt.setText("" + LabDoctorName.get(position).getDoctorName());
                    date = "N/A";
                    reminder = "N/A";
                    status = "N/A";
                    getReminderLabTest();
                }
            } else {
                if (LabDoctorNamelocal != null && LabDoctorNamelocal.size() > 0) {
//                realtive_today.setVisibility(View.GONE);
                    doctorName = LabDoctorNamelocal.get(position).getDoctorName();
                    txt_doctor_name_txt.setText("" + LabDoctorNamelocal.get(position).getDoctorName());
                    date = "N/A";
                    reminder = "N/A";
                    status = "N/A";
                    doctor_name_flag=true;
                    getReminderLabTest();
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_next:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                btn_history.setVisibility(View.VISIBLE);
                text_date.setVisibility(View.INVISIBLE);
                btn_history.setText("Previous");
                date = getNextDate(startFrom);
                startFrom = date;
                String[] dateAll = date.split("-");
                int years = Integer.parseInt(dateAll[0]);
                int mnt = Integer.parseInt(dateAll[1]);
                int dayOfMonth = Integer.parseInt(dateAll[2]);
                txt_date_dialog.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + "/" + (mnt < 10 ? "0" + mnt : mnt) + "/" + years);
                getReminderLabTest();

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
                    getReminderLabTest();
                } else {

                }
                if (Utils.getTodayDate().equalsIgnoreCase(startFrom)) {
                    btn_history.setVisibility(View.GONE);
                    text_date.setVisibility(View.VISIBLE);
                } else {
                    btn_history.setText("Previous");
                }

                break;
            case R.id.txt_filter_reminder:
                txt_filter_reminder.setBackgroundResource(R.color.health_yellow);
                txt_filter_status.setBackgroundResource(R.color.transprent_new);
                txt_reminder.setTextColor(getResources().getColor(R.color.white));
                txt_status.setTextColor(getResources().getColor(R.color.health_dark_gray));
                radioReminder.setVisibility(View.VISIBLE);
                radioStatus.setVisibility(View.GONE);
                break;
            case R.id.txt_filter_status:
                txt_filter_reminder.setBackgroundResource(R.color.transprent_new);
                txt_filter_status.setBackgroundResource(R.color.health_yellow);
                txt_reminder.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_status.setTextColor(getResources().getColor(R.color.white));
                radioReminder.setVisibility(View.GONE);
                radioStatus.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_reset:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                if (isReset) {
                    isReset = false;
                    isChecked = false;
                    apply_flag=false;
                    realtive_today.setVisibility(View.VISIBLE);
                    radioPending.setChecked(false);
                    radioCurefull.setChecked(false);
                    radioDone.setChecked(false);
                    radioSelf.setChecked(false);

                    radioPending.setSelected(false);
                    radioCurefull.setSelected(false);
                    radioSelf.setSelected(false);
                    radioDone.setSelected(false);
                    reminder = "N/A";
                    status = "N/A";
                    doctorName = "N/A";
                    launchTwitterShort(rootView);
                    txt_doctor_name_txt.setText("Doctor Name");
                    date = Utils.getTodayDate();
                    getReminderLabTest();
                }

                break;
            case R.id.btn_apply:
//                realtive_today.setVisibility(View.GONE);
                date = "N/A";
                apply_flag = true;
                launchTwitterShort(rootView);
                getReminderLabTest();
                break;
            case R.id.liner_filter_by:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
               // if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                        txt_no_medicine.post(new Runnable() {
                            @Override
                            public void run() {
                                launchTwitterShort(rootView);
                            }
                        });
                    }
                /*} else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);

                }*/


                break;
            case R.id.rel_set_reminder:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_calender, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLabTestSetReminder(), true);
                break;
        }
    }

    private void getReminderLabTest() {
        isChecked = true;
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LIST_OF_LAB_TEST + "cfuuhId=" + AppPreference.getInstance().getcf_uuhid() + "&date=" + date + "&status=" + status + "&reminderType=" + reminder + "&doctorName=" + doctorName,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            isReset = true;
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    LabTestReminderListView medicineReminderListView = ParseJsonData.getInstance().getReminderLabTestList(response.toString());
                                    if (medicineReminderListView != null) {
                                        txt_no_medicine.setVisibility(View.GONE);
                                        if (medicineReminderListView.getReminderDoctorNames() != null & medicineReminderListView.getReminder_selfListViews() != null) {
                                            txt_no_medicine.setVisibility(View.GONE);
                                            if (medicineReminderListView.getReminderDoctorNames().size() == 0 && medicineReminderListView.getReminder_selfListViews().size() == 0) {
                                                txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                                txt_no_medicine.setVisibility(View.VISIBLE);
                                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                                    relative_bottom_next.setVisibility(View.GONE);
                                                } else {
//                                                btn_next.setVisibility(View.GONE);
                                                }
                                            } else {
//                                            btn_next.setVisibility(View.VISIBLE);
                                            }
                                            if (medicineReminderListView.getReminderDoctorNames().size() > 0) {
                                                setDoctorAdpter(medicineReminderListView.getReminderDoctorNames());
                                                recyclerView_doctor.setVisibility(View.VISIBLE);
                                            } else {
                                                recyclerView_doctor.setVisibility(View.GONE);
                                            }
                                            if (medicineReminderListView.getReminder_selfListViews().size() > 0) {
                                                txt_self.setVisibility(View.VISIBLE);

                                                setSelfMedAdpter(medicineReminderListView.getReminder_selfListViews());
                                                recyclerView_self.setVisibility(View.VISIBLE);
                                            } else {
                                                txt_self.setVisibility(View.GONE);
                                                recyclerView_self.setVisibility(View.GONE);
                                            }
                                        } else {
                                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                                relative_bottom_next.setVisibility(View.GONE);
                                            }
                                            txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                                            txt_no_medicine.setVisibility(View.VISIBLE);
                                        }


                                    } else {
                                        if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                            relative_bottom_next.setVisibility(View.GONE);
                                        }
                                        txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                                        txt_no_medicine.setVisibility(View.VISIBLE);
                                    }

                                } else {
                                    if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                        relative_bottom_next.setVisibility(View.GONE);
                                    }
                                    txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

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
                            try {
                                LabTestReminderListView response = DbOperations.getLabTestReportReminder(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), date);

                                if (response.getReminder_selfListViews().size() > 0) {
                                    txt_self.setVisibility(View.VISIBLE);
                                    txt_no_medicine.setVisibility(View.GONE);
                                    setSelfMedAdpter(response.getReminder_selfListViews());
                                    recyclerView_self.setVisibility(View.VISIBLE);
                                } else {
                                    txt_self.setVisibility(View.GONE);
                                    recyclerView_self.setVisibility(View.GONE);
                                    if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                        relative_bottom_next.setVisibility(View.GONE);
                                    }
                                    txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                    txt_self.setVisibility(View.GONE);
                                    txt_no_medicine.setVisibility(View.VISIBLE);
                                }

                                LabTestReminderListView response_doctor = DbOperations.getLabTestReportReminderDoctor(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), date);
                                if (response_doctor.getReminderDoctorNamesLocal() != null) {
                                    if (response_doctor.getReminderDoctorNamesLocal().size() > 0) {
                                        setDoctorAdpterLocal(response_doctor.getReminderDoctorNamesLocal());
                                        recyclerView_doctor.setVisibility(View.VISIBLE);
                                        txt_no_medicine.setVisibility(View.GONE);
                                    } else {
                                        recyclerView_doctor.setVisibility(View.GONE);
                                        if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                            relative_bottom_next.setVisibility(View.GONE);
                                        }
                                        txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                        txt_self.setVisibility(View.GONE);
                                        txt_no_medicine.setVisibility(View.VISIBLE);
                                    }
                                }
                            } catch (Exception e) {
                                e.getMessage();
                            }

                           /* txt_no_medicine.setText(MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                            txt_no_medicine.setVisibility(View.VISIBLE);*/
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
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                    return headers;
                }
            };

            CureFull.getInstanse().getRequestQueue().add(postRequest);
        }else if(doctor_name_flag){


            try {
                LabTestReminderListView response = DbOperations.getLabTestReportReminderBasedDoctor(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), doctorName);
                if (response != null) {
                    if (response.getReminder_selfListViews().size() > 0) {
                        txt_self.setVisibility(View.VISIBLE);
                        txt_no_medicine.setVisibility(View.GONE);
                        setSelfMedAdpter(response.getReminder_selfListViews());
                        recyclerView_self.setVisibility(View.VISIBLE);
                    } else {
                        txt_self.setVisibility(View.GONE);
                        recyclerView_self.setVisibility(View.GONE);
                        if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                            relative_bottom_next.setVisibility(View.GONE);
                        }
                        txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                        txt_no_medicine.setVisibility(View.VISIBLE);
                    }
                } else {
                    txt_self.setVisibility(View.GONE);
                    recyclerView_self.setVisibility(View.GONE);

                }
                LabTestReminderListView response_doctor = DbOperations.getLabTestReportReminderDoctorBasedDoctorName(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), date);
                if (response_doctor != null) {
                    if (response_doctor.getReminderDoctorNamesLocal() != null) {
                        if (response_doctor.getReminderDoctorNamesLocal().size() > 0) {
                            setDoctorAdpterLocal(response_doctor.getReminderDoctorNamesLocal());
                            recyclerView_doctor.setVisibility(View.VISIBLE);
                            txt_no_medicine.setVisibility(View.GONE);
                        } else {
                            recyclerView_doctor.setVisibility(View.GONE);
                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                relative_bottom_next.setVisibility(View.GONE);
                            }
                            txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                            txt_self.setVisibility(View.GONE);
                            txt_no_medicine.setVisibility(View.VISIBLE);
                        }
                    }
                } else {
                    recyclerView_doctor.setVisibility(View.GONE);
                    txt_self.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                e.getMessage();
            }
            doctor_name_flag=false;
        }
        else {

            if(apply_flag){
                //date,reminder,status,doctorName;
                if(reminder.equalsIgnoreCase("self")) {
                    LabTestReminderListView response = DbOperations.getLabTestReportReminderAfterSelection(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), status);
                    if (response != null) {
                        if (response.getReminder_selfListViews().size() > 0) {
                            txt_self.setVisibility(View.VISIBLE);
                            txt_no_medicine.setVisibility(View.GONE);
                            setSelfMedAdpter(response.getReminder_selfListViews());
                            recyclerView_self.setVisibility(View.VISIBLE);
                            recyclerView_doctor.setVisibility(View.GONE);
                        } else {
                            txt_self.setVisibility(View.GONE);
                            recyclerView_self.setVisibility(View.GONE);
                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                relative_bottom_next.setVisibility(View.GONE);
                            }
                            txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                            txt_no_medicine.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txt_self.setVisibility(View.GONE);
                        recyclerView_self.setVisibility(View.GONE);
                        recyclerView_doctor.setVisibility(View.GONE);
                    }

                }else if(reminder.equalsIgnoreCase("curefull")){
                    LabTestReminderListView response_doctor = DbOperations.getLabTestReportReminderDoctorAfterSelection(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), status);
                    if (response_doctor != null) {
                        if (response_doctor.getReminderDoctorNamesLocal() != null) {
                            if (response_doctor.getReminderDoctorNamesLocal().size() > 0) {
                                setDoctorAdpterLocal(response_doctor.getReminderDoctorNamesLocal());
                                recyclerView_doctor.setVisibility(View.VISIBLE);
                                txt_no_medicine.setVisibility(View.GONE);
                                recyclerView_self.setVisibility(View.GONE);
                            } else {
                                recyclerView_doctor.setVisibility(View.GONE);
                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                    relative_bottom_next.setVisibility(View.GONE);
                                }
                                txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                txt_self.setVisibility(View.GONE);
                                txt_no_medicine.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerView_doctor.setVisibility(View.GONE);
                        txt_self.setVisibility(View.GONE);
                        recyclerView_self.setVisibility(View.GONE);
                    }
                }else{
                    //for status
                    LabTestReminderListView response = DbOperations.getLabTestReportReminderAfterSelection(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), status);
                    if (response != null) {
                        if (response.getReminder_selfListViews().size() > 0) {
                            txt_self.setVisibility(View.VISIBLE);
                            txt_no_medicine.setVisibility(View.GONE);
                            setSelfMedAdpter(response.getReminder_selfListViews());
                            recyclerView_self.setVisibility(View.VISIBLE);
                        } else {
                            txt_self.setVisibility(View.GONE);
                            recyclerView_self.setVisibility(View.GONE);
                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                relative_bottom_next.setVisibility(View.GONE);
                            }
                            txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                            txt_no_medicine.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txt_self.setVisibility(View.GONE);
                        recyclerView_self.setVisibility(View.GONE);

                    }

                    LabTestReminderListView response_doctor = DbOperations.getLabTestReportReminderDoctorAfterSelection(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), status);
                    if (response_doctor != null) {
                        if (response_doctor.getReminderDoctorNamesLocal() != null) {
                            if (response_doctor.getReminderDoctorNamesLocal().size() > 0) {
                                setDoctorAdpterLocal(response_doctor.getReminderDoctorNamesLocal());
                                recyclerView_doctor.setVisibility(View.VISIBLE);
                                txt_no_medicine.setVisibility(View.GONE);
                            } else {
                                recyclerView_doctor.setVisibility(View.GONE);
                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                    relative_bottom_next.setVisibility(View.GONE);
                                }
                                txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                txt_self.setVisibility(View.GONE);
                                txt_no_medicine.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerView_doctor.setVisibility(View.GONE);
                        txt_self.setVisibility(View.GONE);
                    }


                }
                    apply_flag=false;
                    isReset=true;

            }else {

                try {
                    LabTestReminderListView response = DbOperations.getLabTestReportReminder(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), date);
                    if (response != null) {
                        if (response.getReminder_selfListViews().size() > 0) {
                            txt_self.setVisibility(View.VISIBLE);
                            txt_no_medicine.setVisibility(View.GONE);
                            setSelfMedAdpter(response.getReminder_selfListViews());
                            recyclerView_self.setVisibility(View.VISIBLE);
                        } else {
                            txt_self.setVisibility(View.GONE);
                            recyclerView_self.setVisibility(View.GONE);
                            if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                relative_bottom_next.setVisibility(View.GONE);
                            }
                            txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");

                            txt_no_medicine.setVisibility(View.VISIBLE);
                        }
                    } else {
                        txt_self.setVisibility(View.GONE);
                        recyclerView_self.setVisibility(View.GONE);

                    }
                    LabTestReminderListView response_doctor = DbOperations.getLabTestReportReminderDoctor(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid(), date);
                    if (response_doctor != null) {
                        if (response_doctor.getReminderDoctorNamesLocal() != null) {
                            if (response_doctor.getReminderDoctorNamesLocal().size() > 0) {
                                setDoctorAdpterLocal(response_doctor.getReminderDoctorNamesLocal());
                                recyclerView_doctor.setVisibility(View.VISIBLE);
                                txt_no_medicine.setVisibility(View.GONE);
                            } else {
                                recyclerView_doctor.setVisibility(View.GONE);
                                if (!btn_history.getText().toString().equalsIgnoreCase("Previous")) {
                                    relative_bottom_next.setVisibility(View.GONE);
                                }
                                txt_no_medicine.setText("Help us remind you of Lab Test! Add a reminder");
                                txt_self.setVisibility(View.GONE);
                                txt_no_medicine.setVisibility(View.VISIBLE);
                            }
                        }
                    } else {
                        recyclerView_doctor.setVisibility(View.GONE);
                        txt_self.setVisibility(View.GONE);
                    }
                } catch (Exception e) {
                    e.getMessage();
                }
            }
        }

    }


    public void setDoctorAdpter(ArrayList<LabTestReminderDoctorName> reminder_doctorListViews) {
        reminder_medicine_docotr_child_listAdpter = new Reminder_Lab_Docotr_child_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews);
        recyclerView_doctor.setAdapter(reminder_medicine_docotr_child_listAdpter);
        reminder_medicine_docotr_child_listAdpter.notifyDataSetChanged();
    }

    public void setDoctorAdpterLocal(ArrayList<Lab_Test_Reminder_DoctorListView> reminder_doctorListViews) {

        reminder_medicine_docotr_child_listAdpter_localdb = new Reminder_Lab_Docotr_child_ListAdpter_Localdb(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews);
        recyclerView_doctor.setAdapter(reminder_medicine_docotr_child_listAdpter_localdb);
        reminder_medicine_docotr_child_listAdpter_localdb.notifyDataSetChanged();
    }

    public void setSelfMedAdpter(ArrayList<Lab_Test_Reminder_SelfListView> reminder_doctorListViews) {
        reminder_medicine_self_listAdpter = new Reminder_Lab_Self_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews, rootView);
        recyclerView_self.setAdapter(reminder_medicine_self_listAdpter);
        reminder_medicine_self_listAdpter.notifyDataSetChanged();
    }

    public void launchTwitterShort(View view) {

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
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LIST_DOCTOR_NAME_LAB_TEST + "" + AppPreference.getInstance().getcf_uuhid(),
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
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
        } else {
            LabDoctorNamelocal = DbOperations.getLabDoctorReminderListLocal(CureFull.getInstanse().getActivityIsntanse(),"3");

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
}