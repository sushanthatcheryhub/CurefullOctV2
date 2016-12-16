package fragment.healthapp;


import android.animation.Animator;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
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
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.location.ActivityRecognition;

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
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import adpter.Health_Note_Landing_ListAdpter;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogHintScreenaLanding;
import dialog.DialogHintScreenaLandingQution;
import dialog.DialogHintScreenaPrescriptions;
import item.property.HealthNoteItems;
import stepcounter.MessengerService;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.MyConstants;
import utils.SeekArc;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLandingPage extends BaseBackHandlerFragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private View rootView;
    private RecyclerView recyclerView_notes;
    private TextView txt_calories, txt_water_level, txt_no_list_health_note, txt_name, txt_health_note, btn_set_goal, txt_date_time, txt_time, txt_to_time, btn_done, txt_click_here_add, txt_steps_counter;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    //    private CircularImageView circularImageView;
    public static final String TAG = "BasicSensorsApi";
    // [START auth_variable_references]
    private GoogleApiClient mClient = null;
    // [END auth_variable_references]
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private RequestQueue requestQueue;
    // [START mListener_variable_reference]
    // Need to hold a reference to this listener, as it's passed into the "unregister"
    // method in order to stop all sensors from sending data to this listener.
    private OnDataPointListener mListener;
    // [END mListener_variable_reference]
    private RelativeLayout realtive_click;
    private LinearLayout linear_health_app, liner_date_t, liner_to_time, linear_health_note;
    private ActivityRecognition arclient;
    private RelativeLayout realtive_notes;
    private TickerView ticker1, text_steps_count;
    private FloatingActionButton img_fab;
    ImageButton imageButton;
    LinearLayout revealView, layoutButtons, liner_click, linear_prescription_click, linear_lab_report_click, date_time_picker;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    private SeekArc seekArcComplete;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private EditText edt_deatils, edt_subject;
    private Health_Note_Landing_ListAdpter health_note_listAdpter;
    List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private String firstDate = "", firstTime = "", toFirstTime = "";
    SharedPreferences preferences;
    private ImageView img_pre, img_minus_icon, img_plus_icon;
    Messenger mService = null;
    boolean mIsBound;
    boolean isToStop = false;
    private int waterLevel = 0;
    private boolean isFirstTime = false;
    private boolean isSelectFrom = false;
    private int newFirstTime = 0;
    private int secondTime = 0;

    private ImageView imgg_question_white, imgg_question_red;

    @Override
    public boolean onBackPressed() {

        Log.e("aaya", ":-");

        List<Fragment> list = CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager().getFragments();
        if (list != null) {
            for (Fragment f : list) {
                if (f != null && f instanceof FragmentEditGoal) {
                    Log.e("FragmentEditGoal", ":-  FragmentEditGoal");
                    String gender = "";
                    if (AppPreference.getInstance().getMale()) {
                        gender = "MALE";
                    } else {
                        gender = "FEMALE";
                    }
                    if (!AppPreference.getInstance().getGoalHeightFeet().equalsIgnoreCase("0")) {
                        jsonUploadGenderDetails(String.valueOf(convertFeetandInchesToCentimeter(String.valueOf(AppPreference.getInstance().getGoalHeightFeet()), String.valueOf(AppPreference.getInstance().getGoalHeightInch()))), String.valueOf(AppPreference.getInstance().getGoalWeightKg()), AppPreference.getInstance().getGoalAge(), gender);

                    }
                }
            }
        }


        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        return super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_landing_page_new,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        CureFull.getInstanse().getActivityIsntanse().showLogo(false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);

        linear_health_note = (LinearLayout) rootView.findViewById(R.id.linear_health_note);
        imgg_question_white = (ImageView) rootView.findViewById(R.id.imgg_question_white);
        imgg_question_red = (ImageView) rootView.findViewById(R.id.imgg_question_red);
        txt_calories = (TextView) rootView.findViewById(R.id.txt_calories);
        img_minus_icon = (ImageView) rootView.findViewById(R.id.img_minus_icon);
        img_plus_icon = (ImageView) rootView.findViewById(R.id.img_plus_icon);
        txt_water_level = (TextView) rootView.findViewById(R.id.txt_water_level);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        img_pre = (ImageView) rootView.findViewById(R.id.img_pre);
        recyclerView_notes = (RecyclerView) rootView.findViewById(R.id.recyclerView_notes);
        txt_no_list_health_note = (TextView) rootView.findViewById(R.id.txt_no_list_health_note);
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
        linear_lab_report_click = (LinearLayout) rootView.findViewById(R.id.linear_lab_report_click);
        linear_prescription_click = (LinearLayout) rootView.findViewById(R.id.linear_prescription_click);
        liner_click = (LinearLayout) rootView.findViewById(R.id.liner_click);
        realtive_click = (RelativeLayout) rootView.findViewById(R.id.realtive_click);
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        text_steps_count = (TickerView) rootView.findViewById(R.id.text_steps_count);
        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        text_steps_count.setCharacterList(NUMBER_LIST);
        ticker1.setCharacterList(NUMBER_LIST);
        ticker1.setText("0" + "%");
        btn_set_goal = (TextView) rootView.findViewById(R.id.btn_set_goal);
        img_fab = (FloatingActionButton) rootView.findViewById(R.id.img_fab);
        pixelDensity = getResources().getDisplayMetrics().density;
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);

        txt_steps_counter = (TextView) rootView.findViewById(R.id.txt_steps_counter);

        linear_lab_report_click.setOnClickListener(this);
        linear_prescription_click.setOnClickListener(this);
        img_fab.setOnClickListener(this);
        realtive_click.setOnClickListener(this);
        liner_click.setOnClickListener(this);
        btn_set_goal.setOnClickListener(this);
        txt_date_time.setOnClickListener(this);
        liner_date_t.setOnClickListener(this);
        txt_to_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        txt_click_here_add.setOnClickListener(this);
        img_plus_icon.setOnClickListener(this);
        img_minus_icon.setOnClickListener(this);
        imgg_question_white.setOnClickListener(this);
        imgg_question_red.setOnClickListener(this);
        CureFull.getInstanse().getActivityIsntanse().setActionDrawerProfilePic(AppPreference.getInstance().getProfileImage());
//        if (AppPreference.getInstance().getStepStarts()) {
        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
        CureFull.getInstanse().getActivityIsntanse().startService(intent);

//        }
        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
        txt_health_note = (TextView) rootView.findViewById(R.id.txt_health_note);
        rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
        // Construct SwitchDateTimePicker
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_notes.setLayoutManager(mLayoutManager);
        CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(AppPreference.getInstance().getUserName() + "-" + AppPreference.getInstance().getcf_uuhid(), AppPreference.getInstance().getUserID());
        getAllHealthList();
        preferences.edit().putBoolean("destroy", false).commit();
        waterLevel = Integer.parseInt(AppPreference.getInstance().getWaterInTake());
        txt_water_level.setText("" + AppPreference.getInstance().getWaterInTake() + " Ltr");
        getDailyHealth();
        jsonUploadTarget();
        doBindService();

        if (AppPreference.getInstance().isFirstTimeScreen1()) {

        } else {
            AppPreference.getInstance().setIsFirstTimeScreen1(true);
            DialogHintScreenaLandingQution dialogHintScreenaLandingQution = new DialogHintScreenaLandingQution(CureFull.getInstanse().getActivityIsntanse());
            dialogHintScreenaLandingQution.setCanceledOnTouchOutside(true);
            dialogHintScreenaLandingQution.show();
        }


        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
        ticker1.setText("" + AppPreference.getInstance().getPercentage() + "%");
        seekArcComplete.setProgress(AppPreference.getInstance().getPercentage());
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);


        linear_health_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putString("subject", edt_subject.getText().toString().trim());
                bundle.putString("details", edt_deatils.getText().toString().trim());
                bundle.putString("Date", firstDate);
                bundle.putString("firstTime", firstTime);
                bundle.putString("toFirstTime", toFirstTime);
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthNote(), bundle, true);
            }
        });
        if (waterLevel == 0) {
            img_minus_icon.setVisibility(View.INVISIBLE);
        }
        return rootView;
    }


    public void showAdpter() {
        if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
            txt_no_list_health_note.setVisibility(View.GONE);
            recyclerView_notes.setVisibility(View.VISIBLE);
            health_note_listAdpter = new Health_Note_Landing_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), healthNoteItemses);
            recyclerView_notes.setAdapter(health_note_listAdpter);
            health_note_listAdpter.notifyDataSetChanged();
        } else {
            Log.e("no one", ":- no");
            isFabOpen = false;
            flag = true;
//            txt_no_list_health_note.setVisibility(View.VISIBLE);
            recyclerView_notes.setVisibility(View.GONE);

            txt_no_list_health_note.post(new Runnable() {
                @Override
                public void run() {
                    launchTwitter(rootView);
                }
            });
            animateFAB();
        }

    }


    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        int x = realtive_notes.getRight();
        int y = realtive_notes.getBottom();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());
        Log.e("flag ", ": " + flag);
        if (flag) {

//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);

            FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                    revealView.getLayoutParams();
            parameters.height = realtive_notes.getHeight();
            revealView.setLayoutParams(parameters);
            try {
                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
                anim.setDuration(700);
                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layoutButtons.setVisibility(View.VISIBLE);
                        layoutButtons.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                revealView.setVisibility(View.VISIBLE);
                anim.start();
            } catch (Exception e) {
                e.printStackTrace();
            }


            flag = false;
        } else {

//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
            try {
                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                anim.setDuration(400);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        revealView.setVisibility(View.GONE);
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                anim.start();
            } catch (Exception e) {
                e.printStackTrace();
            }

            flag = true;
        }
    }

    public void animateFAB() {
        Log.d("isFabOpen", ":- " + isFabOpen);
        if (isFabOpen) {
            img_fab.startAnimation(rotate_backward);
            isFabOpen = false;
            Log.d("Raj", "close");
            liner_to_time.setVisibility(View.GONE);
            liner_date_t.setVisibility(View.GONE);
            date_time_picker.setVisibility(View.GONE);
            txt_click_here_add.setVisibility(View.VISIBLE);
        } else {
            img_fab.startAnimation(rotate_forward);
            isFabOpen = true;
            Log.d("Raj", "open");

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgg_question_white:
                DialogHintScreenaLanding dialogHintScreenaLanding = new DialogHintScreenaLanding(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaLanding.show();
                break;

            case R.id.imgg_question_red:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_pre);
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheck(), true);
                DialogHintScreenaPrescriptions dialogHintScreenaPrescriptions = new DialogHintScreenaPrescriptions(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaPrescriptions.show();
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

            case R.id.realtive_click:
                if (AppPreference.getInstance().isLoginFirst()) {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentEditGoal(), true);
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentHealthAppNew(), true);
                }

                break;
            case R.id.liner_click:
                if (AppPreference.getInstance().isLoginFirst()) {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentEditGoal(), true);
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentHealthAppNew(), true);
                }
                break;
            case R.id.btn_set_goal:
//                DialogFullViewClickImage dialogFullViewPrescription = new DialogFullViewClickImage(getActivity());
//                dialogFullViewPrescription.show();
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentEditGoal(), true);


                break;
            case R.id.linear_lab_report_click:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLabTestReport(), true);
                break;
            case R.id.linear_prescription_click:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_pre);
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheck(), true);
                break;
            case R.id.img_fab:
                if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
                    animateFAB();
                    txt_no_list_health_note.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                }
                break;
            case R.id.txt_date_time:
                final int year, day;
                int month1;
                final Calendar c2 = Calendar.getInstance();
                if (firstDate.equalsIgnoreCase("")) {
                    year = c2.get(Calendar.YEAR);
                    month1 = c2.get(Calendar.MONTH);
                    day = c2.get(Calendar.DAY_OF_MONTH) + 1;
                } else {
                    Log.e("ye wala ", " ye wlal");
                    String[] dateFormat = firstDate.split("-");
                    year = Integer.parseInt(dateFormat[0]);
                    month1 = Integer.parseInt(dateFormat[1]);
                    day = Integer.parseInt(dateFormat[2]);
                    month1 = (month1 - 1);
                }

                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_DEVICE_DEFAULT_DARK, FragmentLandingPage.this, year, month1, day);
                newDateDialog.getDatePicker().setCalendarViewShown(false);
//                c.add(Calendar.DATE, 1);
                Date newDate = c2.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();

//                dateTimeFragment.show(getActivity().getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
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
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                jsonHealthNoteCheck();

                break;
            case R.id.txt_click_here_add:
                txt_click_here_add.setVisibility(View.GONE);
                date_time_picker.setVisibility(View.VISIBLE);
                liner_date_t.setVisibility(View.VISIBLE);
                liner_to_time.setVisibility(View.VISIBLE);
                break;
            case R.id.img_plus_icon:

                waterLevel++;
                if (waterLevel > 0) {
                    img_minus_icon.setVisibility(View.VISIBLE);
                }
                txt_water_level.setText(waterLevel + "Ltr");
                AppPreference.getInstance().setWaterInTake("" + waterLevel);
                break;
            case R.id.img_minus_icon:
                --waterLevel;
                txt_water_level.setText(waterLevel + "Ltr");
                AppPreference.getInstance().setWaterInTake("" + waterLevel);
                if (waterLevel == 0) {
                    img_minus_icon.setVisibility(View.INVISIBLE);
                }

                break;

        }
    }


    // For Step Counter


    public void doBindService() {
        CureFull.getInstanse().getActivityIsntanse().bindService(new Intent(CureFull.getInstanse().getActivityIsntanse(),
                MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
    }

    final Messenger mMessenger = new Messenger(new IncomingHandler());

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
            txt_steps_counter.setText(AppPreference.getInstance().getStepsCount());
            txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
            try {
                Message msg = Message.obtain(null,
                        MessengerService.MSG_REGISTER_CLIENT);
                msg.replyTo = mMessenger;
                mService.send(msg);
                msg = Message.obtain(null,
                        MessengerService.MSG_SET_VALUE, this.hashCode(), 0);
                mService.send(msg);
            } catch (RemoteException e) {
            }

            if (isToStop) {
                stop();
            }

//                Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), R.string.remote_service_connected,
//                        Toast.LENGTH_SHORT).show();
        }

        public void onServiceDisconnected(ComponentName className) {
            mService = null;
            txt_steps_counter.setText(AppPreference.getInstance().getStepsCount());
            txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
//            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), R.string.remote_service_disconnected,
//                    Toast.LENGTH_SHORT).show();
        }
    };


    private void stop() {
        Message msg = Message.obtain(null,
                MessengerService.STOP_FOREGROUND);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        doUnbindService();
    }

    void doUnbindService() {
        if (mIsBound) {
            if (mService != null) {
                try {
                    Message msg = Message.obtain(null,
                            MessengerService.MSG_UNREGISTER_CLIENT);
                    msg.replyTo = mMessenger;
                    mService.send(msg);
                } catch (RemoteException e) {
                }
            }

            // Detach our existing connection.
            CureFull.getInstanse().getActivityIsntanse().unbindService(mConnection);
            mIsBound = false;
            text_steps_count.setText(AppPreference.getInstance().getStepsCount());
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        int mnt = (monthOfYear + 1);
        try {
            txt_date_time.setText("" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth) + " " + formatMonth(String.valueOf(mnt)));
            firstDate = year + "-" + mnt + "-" + (dayOfMonth < 10 ? "0" + dayOfMonth : dayOfMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }


    class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:
                    Log.e("", "steps received in activity:" + msg.what);
                    Log.e("", "steps arg in activity:" + msg.arg1);
//                    txt_steps_counter.setText("Step Count : " + msg.arg1);

                    txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
                    AppPreference.getInstance().setStepsCount("" + msg.arg1);
                    float percentage = Utils.getPercentage(msg.arg1, AppPreference.getInstance().getStepsCountTarget());
                    Log.e("percentage", ":- " + percentage);
                    int b = (int) percentage;
                    Log.e("b", ":- " + b);
                    seekArcComplete.setProgress(b);
//                    setProgressUpdateAnimation(b);
                    AppPreference.getInstance().setPercentage(b);
                    ticker1.setText(b + "%");

                    String wirght = "";
                    Log.e("sdsd", "sdsd " + AppPreference.getInstance().getGoalWeightKg());
                    if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0.0") || AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase(null) || AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("nul")) {
                        wirght = "0";
                    } else {
                        wirght = AppPreference.getInstance().getGoalWeightKg();
                    }

                    int kg = Integer.parseInt(wirght);
                    double i2 = Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1);
                    txt_calories.setText("" + new DecimalFormat("##.##").format(i2) + " kcal");
                    AppPreference.getInstance().setCaloriesCount("" + new DecimalFormat("##.##").format(i2));
//                    txt_calories.setText("" + Utils.getCaloriesBurnt((int) (kg * 2.20462), msg.arg1) + " kcal");

                    break;
                default:
                    super.handleMessage(msg);
            }
        }
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
            Log.e("firstDate", ":- " + firstDate);
            date = firstDate;
        }
        String time = "";
        if (firstTime.equalsIgnoreCase("")) {
            time = getTodayTime();
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
                        Log.e("FragmentLogin, URL 3.", response.toString());
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
                            getAllHealthList();
                            isFabOpen = true;
                            animateFAB();
                            txt_no_list_health_note.post(new Runnable() {
                                @Override
                                public void run() {
                                    launchTwitter(rootView);
                                }
                            });
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
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }

        }) {


            @Override
            protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                try {
                    String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
//                    Log.e("headers", "" +  response.headers.get("a_t"));
                    JSONObject jsonResponse = new JSONObject(jsonString);
                    jsonResponse.put(MyConstants.PrefrenceKeys.HEADERS, new JSONObject(response.headers));
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
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getAllHealth, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            healthNoteItemses = ParseJsonData.getInstance().getHealthNoteListItem(response);
                            showAdpter();
                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            animateFAB();
                            txt_no_list_health_note.post(new Runnable() {
                                @Override
                                public void run() {
                                    launchTwitter(rootView);
                                }
                            });
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        animateFAB();
                        txt_no_list_health_note.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                launchTwitter(rootView);
                            }
                        }, 500);
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
    }

    private String updateTime(int hours, int mins) {


        int selctHour = hours;

        String timeSet = "";
        if (selctHour > 12) {
            selctHour -= 12;
            timeSet = "pm";
        } else if (selctHour == 0) {
            selctHour += 12;
            timeSet = "am";
        } else if (selctHour == 12) {
            timeSet = "pm";
        } else {
            timeSet = "am";
        }

        String minutes = "";
        if (mins < 10)
            minutes = "0" + mins;
        else
            minutes = String.valueOf(mins);

        // Append in a StringBuilder
        String aTime = new StringBuilder().append(selctHour).append(" ").append(timeSet).toString();

        return aTime;
    }

    public static String getTodayTime() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "HH:mm", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
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

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int mintues) {

        if (isFirstTime) {
            newFirstTime = hourOfDay;
            isSelectFrom = true;
            firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
            txt_time.setText("" + updateTime(hourOfDay, mintues));
            txt_to_time.setText("");
            toFirstTime = "";
        } else {
            secondTime = hourOfDay;
            Log.e("first ", " " + newFirstTime + " second:- " + secondTime);
            if (secondTime > newFirstTime) {
                toFirstTime = "" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                txt_to_time.setText("" + updateTime(hourOfDay, mintues));
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select greater than first time.");
            }

        }

    }

    public int getStepsCount(long startTime, long endTime) {
        DataSource ESTIMATED_STEP_DELTAS = new DataSource.Builder()
                .setDataType(DataType.TYPE_STEP_COUNT_DELTA)
                .setType(DataSource.TYPE_DERIVED)
                .setStreamName("estimated_steps")
                .setAppPackageName("com.google.android.gms").build();
        PendingResult<DataReadResult> pendingResult = Fitness.HistoryApi
                .readData(
                        mClient,
                        new DataReadRequest.Builder()
                                .aggregate(ESTIMATED_STEP_DELTAS,
                                        DataType.AGGREGATE_STEP_COUNT_DELTA)
                                .bucketByTime(1, TimeUnit.HOURS)
                                .setTimeRange(startTime, endTime,
                                        TimeUnit.MILLISECONDS).build());
        int steps = 0;
        DataReadResult dataReadResult = pendingResult.await();
        if (dataReadResult.getBuckets().size() > 0) {
            //Log.e("TAG", "Number of returned buckets of DataSets is: "
            //+ dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    for (DataPoint dp : dataSet.getDataPoints()) {
                        for (Field field : dp.getDataType().getFields()) {
                            steps += dp.getValue(field).asInt();
                        }
                    }
                }
            }
        } else if (dataReadResult.getDataSets().size() > 0) {
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                for (DataPoint dp : dataSet.getDataPoints()) {
                    for (Field field : dp.getDataType().getFields()) {
                        steps += dp.getValue(field).asInt();
                    }
                }
            }
        }
        return steps;
    }


    public void jsonUploadTarget() {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());

        String steps = AppPreference.getInstance().getStepsCount();
        String running = "0";
        String cycling = "0";
        String waterintake = AppPreference.getInstance().getWaterInTake();


        String dateTime = getTodayDateTime();
        String[] dateParts = dateTime.split(" ");
        String date = dateParts[0];
        String timeReal = dateParts[1];

        JSONObject data = JsonUtilsObject.toSaveHealthAppDetails(steps, running, cycling, waterintake, date, timeReal);
        Log.e("jsonUploadTarget", ": " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_HELTHAPP_DETALS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("Target, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
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
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
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

    public static String getTodayDateTime() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
            Log.e("", "formattedDate" + formattedDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


    public void jsonUploadGenderDetails(String height, String weight, String dateOfBirth, String gender) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.toSetGoalsDetails(height, weight, dateOfBirth, gender);
        Log.e("jsonUploadPrescription", ":- " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SET_GOALS_DEATILS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("GenderDetails, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
//                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
//                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
//                            }
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
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
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


    public static double convertFeetandInchesToCentimeter(String feet, String inches) {
        Log.e("feet:- ", ":- " + feet + " inches :-" + inches);
        double heightInFeet = 0;
        double heightInInches = 0;
        try {
            if (feet != null && feet.trim().length() != 0) {
                heightInFeet = Double.parseDouble(feet);
            }
            if (inches != null && inches.trim().length() != 0) {
                heightInInches = Double.parseDouble(inches);
            }
        } catch (NumberFormatException nfe) {

        }
        return (heightInFeet * 30.48) + Math.round(heightInInches * 2.54);
    }

    public String formatMonth(String month) throws ParseException {

        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (java.text.ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }


    private void getDailyHealth() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_HEALTH_DAILY_APP + CureFull.getInstanse().getActivityIsntanse().getTodayDate(),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getDailyHealth, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String steps = json1.getString("steps");
                                preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
                                AppPreference.getInstance().setStepsCount("" + steps);
                            } else {


                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    }

}