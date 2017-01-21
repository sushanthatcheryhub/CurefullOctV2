package fragment.healthapp;


import android.animation.Animator;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import adpter.Reminder_medicine_Docotr_child_ListAdpter;
import adpter.Reminder_medicine_Self_ListAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
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
public class FragmentReminderMedicine extends Fragment implements View.OnClickListener {


    private View rootView;
    private RelativeLayout rel_set_reminder, liner_filter_by, realtive_notesShort;
    private RequestQueue requestQueue;
    private RecyclerView recyclerView_doctor, recyclerView_self;
    private Reminder_medicine_Docotr_child_ListAdpter reminder_medicine_docotr_child_listAdpter;
    private Reminder_medicine_Self_ListAdpter reminder_medicine_self_listAdpter;
    private TextView txt_self, txt_no_medicine, txt_reminder, txt_status;
    boolean flagShort = true;
    private LinearLayout revealViewShort, layoutButtonsShort, txt_filter_reminder, txt_filter_status, btn_reset, btn_apply;
    private float pixelDensity;

    private RadioGroup radioReminder;
    private RadioButton radioCurefull, radioSelf;


    private RadioGroup radioStatus;
    private RadioButton radioPending, radioDone;

    private ImageView img_filter, img_calender;
    private String reminder = "N/A", status = "N/A", doctorName = "N/A";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reminder_medicine,
                container, false);
        if (CureFull.getInstanse().getiGlobalIsbackButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalIsbackButtonVisible().isbackButtonVisible(false);
        }
        if (CureFull.getInstanse().getiGlobalTopBarButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalTopBarButtonVisible().isTobBarButtonVisible(false);
        }
        CureFull.getInstanse().getActivityIsntanse().selectedNav(3);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        pixelDensity = getResources().getDisplayMetrics().density;

        img_filter = (ImageView) rootView.findViewById(R.id.img_filter);
        img_calender = (ImageView) rootView.findViewById(R.id.img_calender);

        radioReminder = (RadioGroup) rootView.findViewById(R.id.radioReminder);
        radioCurefull = (RadioButton) rootView.findViewById(R.id.radioCurefull);
        radioSelf = (RadioButton) rootView.findViewById(R.id.radioSelf);

        radioStatus = (RadioGroup) rootView.findViewById(R.id.radioStatus);
        radioPending = (RadioButton) rootView.findViewById(R.id.radioPending);
        radioDone = (RadioButton) rootView.findViewById(R.id.radioDone);


        txt_filter_reminder = (LinearLayout) rootView.findViewById(R.id.txt_filter_reminder);
        txt_filter_status = (LinearLayout) rootView.findViewById(R.id.txt_filter_status);
        btn_reset = (LinearLayout) rootView.findViewById(R.id.btn_reset);
        btn_apply = (LinearLayout) rootView.findViewById(R.id.btn_apply);

        txt_reminder = (TextView) rootView.findViewById(R.id.txt_reminder);
        txt_status = (TextView) rootView.findViewById(R.id.txt_status);


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
        getReminderMedicine();
        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

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
                reminder = "N/A";
                status = "N/A";
                doctorName = "N/A";
                getReminderMedicine();
                break;
            case R.id.btn_apply:
                getReminderMedicine();
                break;
            case R.id.rel_set_reminder:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_calender);
                CureFull.getInstanse().getFlowInstanseAll()
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
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LIST_OF_MED + "cfuuhId=" + AppPreference.getInstance().getcf_uuhid() + "&date=" + Utils.getTodayDate() + "&status=" + status + "&reminderType=" + reminder + "&doctorName=" + doctorName,
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
                                        if (medicineReminderListView.getReminderDoctorNames().size() > 0&medicineReminderListView.getReminder_selfListViews().size() > 0) {
                                            txt_no_medicine.setVisibility(View.VISIBLE);
                                        }
                                        if (medicineReminderListView.getReminderDoctorNames().size() > 0) {
                                            setDoctorAdpter(medicineReminderListView.getReminderDoctorNames());
                                        }
                                        if (medicineReminderListView.getReminder_selfListViews().size() > 0) {
                                            txt_self.setVisibility(View.VISIBLE);
                                            setSelfMedAdpter(medicineReminderListView.getReminder_selfListViews());
                                        }
                                    } else {
                                        txt_no_medicine.setVisibility(View.VISIBLE);
                                    }


                                } else {
                                    txt_no_medicine.setVisibility(View.VISIBLE);
                                }

                            } else {
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


    public void setDoctorAdpter(ArrayList<ReminderDoctorName> reminder_doctorListViews) {
        reminder_medicine_docotr_child_listAdpter = new Reminder_medicine_Docotr_child_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews);
        recyclerView_doctor.setAdapter(reminder_medicine_docotr_child_listAdpter);
        reminder_medicine_docotr_child_listAdpter.notifyDataSetChanged();
    }

    public void setSelfMedAdpter(ArrayList<Reminder_SelfListView> reminder_doctorListViews) {
        reminder_medicine_self_listAdpter = new Reminder_medicine_Self_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), reminder_doctorListViews);
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
}