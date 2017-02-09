package fragment.healthapp;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.ParseError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.Volley;

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

import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogHintScreenaLanding;
import dialog.DialogHintScreenaLandingQution;
import dialog.DialogHintScreenaPrescriptions;
import item.property.HealthNoteItems;
import item.property.StepsCountsItems;
import operations.DbOperations;
import stepcounter.MessengerService;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.CustomTypefaceSpan;
import utils.HandlePermission;
import utils.MyConstants;
import utils.SeekArc;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLandingPage extends BaseBackHandlerFragment implements MyConstants.JsonUtils, View.OnClickListener, TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private View rootView;
    private static TextView txt_calories;
    private TextView txt_water_level;
    private TextView txt_name;
    private TextView txt_health_note;
    private TextView btn_set_goal;
    private TextView txt_date_time;
    private TextView txt_time, txt_date_times, txt_title;
    private TextView txt_to_time;
    private TextView btn_done;
    private TextView txt_click_here_add;
    private static TextView txt_steps_counter;
    private static final String TAG_DATETIME_FRAGMENT = "TAG_DATETIME_FRAGMENT";
    private static final String STATE_TEXTVIEW = "STATE_TEXTVIEW";
    //    private CircularImageView circularImageView;
    public static final String TAG = "BasicSensorsApi";
    // [START auth_variable_references]
    // [END auth_variable_references]
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;
    private RequestQueue requestQueue;
    // [START mListener_variable_reference]
    // Need to hold a reference to this listener, as it's passed into the "unregister"
    // method in order to stop all sensors from sending data to this listener.
    // [END mListener_variable_reference]
    private LinearLayout linear_health_app, liner_date_t, liner_to_time, linear_health_note;
    private RelativeLayout realtive_notes;
    private static TickerView ticker1;
    private static TickerView text_steps_count;
    ImageButton imageButton;
    LinearLayout liner_bottomd, revealView, layoutButtons, liner_click, linear_prescription_click, linear_lab_report_click, date_time_picker;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    private static SeekArc seekArcComplete;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;
    private EditText edt_deatils, edt_subject;
    List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private String firstDate = "", firstTime = "", toFirstTime = "";
    static SharedPreferences preferences;
    private ImageView img_pre, img_minus_icon, img_plus_icon;
    static Messenger mService = null;
    static boolean mIsBound;
    static boolean isToStop = false;
    private int waterLevel = 0;
    private boolean isFirstTime = false;
    private boolean isSelectFrom = false;
    private int newFirstTime = 0;
    private int secondTime = 0;
    private int newFirstTimeMintues = 0;
    private int secondTimeMintues = 0;
    int dbYear = 0;
    private ImageView imgg_question_white, imgg_question_red;
    private List<StepsCountsItems> stepsCountsItemses;

    @Override
    public boolean onBackPressed() {
        return true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_landing_page_new,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true);

        AppPreference.getInstance().setIsLogin(true);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showRelativeActionBar(true);


        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        CureFull.getInstanse().getActivityIsntanse().showLogo(false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);

        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(false);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(false);


        txt_date_times = (TextView) rootView.findViewById(R.id.txt_date_times);
        txt_title = (TextView) rootView.findViewById(R.id.txt_title);
        liner_bottomd = (LinearLayout) rootView.findViewById(R.id.liner_bottomd);
        linear_health_note = (LinearLayout) rootView.findViewById(R.id.linear_health_note);
        imgg_question_white = (ImageView) rootView.findViewById(R.id.imgg_question_white);
        imgg_question_red = (ImageView) rootView.findViewById(R.id.imgg_question_red);
        txt_calories = (TextView) rootView.findViewById(R.id.txt_calories);
        img_minus_icon = (ImageView) rootView.findViewById(R.id.img_minus_icon);
        img_plus_icon = (ImageView) rootView.findViewById(R.id.img_plus_icon);
        txt_water_level = (TextView) rootView.findViewById(R.id.txt_water_level);
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        img_pre = (ImageView) rootView.findViewById(R.id.img_pre);
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
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        text_steps_count = (TickerView) rootView.findViewById(R.id.text_steps_count);
        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        text_steps_count.setCharacterList(NUMBER_LIST);
        ticker1.setCharacterList(NUMBER_LIST);
        ticker1.setText("0" + "%");
        btn_set_goal = (TextView) rootView.findViewById(R.id.btn_set_goal);
        pixelDensity = getResources().getDisplayMetrics().density;
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);

        txt_steps_counter = (TextView) rootView.findViewById(R.id.txt_steps_counter);

        linear_lab_report_click.setOnClickListener(this);
        linear_prescription_click.setOnClickListener(this);
        liner_click.setOnClickListener(this);
        btn_set_goal.setOnClickListener(this);
        txt_date_time.setOnClickListener(this);
//        liner_date_t.setOnClickListener(this);
        txt_time.setOnClickListener(this);
        txt_to_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        txt_click_here_add.setOnClickListener(this);
        img_plus_icon.setOnClickListener(this);
        img_minus_icon.setOnClickListener(this);
        imgg_question_white.setOnClickListener(this);
        imgg_question_red.setOnClickListener(this);
        CureFull.getInstanse().getActivityIsntanse().setActionDrawerProfilePic(AppPreference.getInstance().getProfileImage());
//        if (AppPreference.getInstance().getStepStarts()) {


//        }
        alphaAnimation = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.alpha_anim);
        txt_health_note = (TextView) rootView.findViewById(R.id.txt_health_note);
        rotate_forward = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.rotate_backward);
        // Construct SwitchDateTimePicker
        CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(AppPreference.getInstance().getUserName() + "-" + AppPreference.getInstance().getcf_uuhid(), AppPreference.getInstance().getUserID());
        getAllHealthList();
        preferences.edit().putBoolean("destroy", false).commit();


        if (AppPreference.getInstance().isFirstTimeSteps()) {
            stepsCountsItemses = DbOperations.getOfflineSteps(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhid());
            if (stepsCountsItemses != null && stepsCountsItemses.size() > 0) {
                Log.e("db", "db");
                for (int i = 0; i < stepsCountsItemses.size(); i++) {
                    jsonUploadTargetSteps(CureFull.getInstanse().getActivityIsntanse(), stepsCountsItemses.get(i).getSteps_count(), stepsCountsItemses.get(i).getCalories(), stepsCountsItemses.get(i).getWaterTake(), stepsCountsItemses.get(i).getDateTime(), i, stepsCountsItemses.size());
                }
            } else {
                jsonUploadTarget();
            }

        } else {
            getDailyHealth();
            AppPreference.getInstance().setIsFirstTimeSteps(true);
        }

        if (AppPreference.getInstance().isFirstTimeScreen1()) {
        } else {
            Log.e("hello", ":- " + " hello");
            if (AppPreference.getInstance().getHintScreen().equalsIgnoreCase("1")) {
            } else {
                AppPreference.getInstance().setIsFirstTimeScreen1(true);
                DialogHintScreenaLandingQution dialogHintScreenaLandingQution = new DialogHintScreenaLandingQution(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaLandingQution.setCanceledOnTouchOutside(true);
                dialogHintScreenaLandingQution.show();
            }

        }


        ticker1.setText("" + AppPreference.getInstance().getPercentage() + "%");
        seekArcComplete.setProgress(AppPreference.getInstance().getPercentage());
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);


        linear_health_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                Log.e("yes", "yes");
                Log.e("subject", "yes" + edt_subject.getText().toString().trim());
                bundle.putString("subject", edt_subject.getText().toString().trim());
                bundle.putString("details", edt_deatils.getText().toString().trim());
                bundle.putString("Date", firstDate);
                bundle.putString("firstTime", firstTime);
                bundle.putString("toFirstTime", toFirstTime);
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentHealthNote(), bundle, true);
            }
        });

        if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {

        }


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


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgg_question_white:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(imgg_question_white);
                DialogHintScreenaLanding dialogHintScreenaLanding = new DialogHintScreenaLanding(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaLanding.show();
                break;

            case R.id.imgg_question_red:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(imgg_question_red);
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentPrescriptionCheckNew(), true);
                DialogHintScreenaPrescriptions dialogHintScreenaPrescriptions = new DialogHintScreenaPrescriptions(CureFull.getInstanse().getActivityIsntanse());
                dialogHintScreenaPrescriptions.show();
                break;


            case R.id.txt_time:
                if (firstDate.equalsIgnoreCase("")) {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select Date First.");
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

//            case R.id.realtive_click:
//                if (AppPreference.getInstance().isEditGoal()) {
//                    CureFull.getInstanse().getFlowInstanse()
//                            .replace(new FragmentHealthAppNew(), true);
//
//                } else {
//                    CureFull.getInstanse().getFlowInstanse()
//                            .replace(new FragmentEditGoal(), true);
//                }
//
//                break;
            case R.id.liner_click:
                if (AppPreference.getInstance().isEditGoal()) {
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentHealthAppNewProgress(), false);
                } else {
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentEditGoal(), false);

                }
                break;
            case R.id.btn_set_goal:
//                DialogFullViewClickImage dialogFullViewPrescription = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse());
//                dialogFullViewPrescription.show();
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentEditGoal(), false);


                break;
            case R.id.linear_lab_report_click:
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLabTestReport(), false);
                break;
            case R.id.linear_prescription_click:
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentPrescriptionCheckNew(), false);
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

                DatePickerDialog newDateDialog = new DatePickerDialog(CureFull.getInstanse().getActivityIsntanse(), AlertDialog.THEME_HOLO_LIGHT, FragmentLandingPage.this, year, month1, day);
                newDateDialog.getDatePicker().setCalendarViewShown(false);
//                c.add(Calendar.DATE, 1);
                Date newDate = c2.getTime();
                newDateDialog.getDatePicker().setMaxDate(newDate.getTime());
                newDateDialog.show();

//                dateTimeFragment.show(CureFull.getInstanse().getActivityIsntanse().getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
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
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_plus_icon);
                getIncreseWaterInTake("true");
                break;
            case R.id.img_minus_icon:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_minus_icon);
                getIncreseWaterInTake("false");
                break;

        }
    }


    // For Step Counter


    public static void doBindService() {
        CureFull.getInstanse().getActivityIsntanse().bindService(new Intent(CureFull.getInstanse().getActivityIsntanse(),
                MessengerService.class), mConnection, Context.BIND_AUTO_CREATE);
        mIsBound = true;
//        preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
        txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
        txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
    }

    static final Messenger mMessenger = new Messenger(new IncomingHandler());

    private static ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = new Messenger(service);
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText(AppPreference.getInstance().getStepsCount());
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
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText(AppPreference.getInstance().getStepsCount());
            txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
//            Toast.makeText(CureFull.getInstanse().getActivityIsntanse(), R.string.remote_service_disconnected,
//                    Toast.LENGTH_SHORT).show();
        }
    };

    //stopStepService
    // ye method ko waha se call karlo,
    public static void stopStepService() {
        if (!mIsBound) {
            isToStop = true;
            doBindService();
            return;
        }
        stop();

    }

    // no
    private static void stop() {
        Message msg = Message.obtain(null,
                MessengerService.STOP_FOREGROUND);
        try {
            mService.send(msg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        doUnbindService();
    }

    static void doUnbindService() {
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
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            text_steps_count.setText(AppPreference.getInstance().getStepsCount());
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


    static class IncomingHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MessengerService.MSG_SET_VALUE:

//                                        AppPreference.getInstance().setStepsCount("" + steps);
                    txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//                    txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
//                    AppPreference.getInstance().setStepsCount("" + msg.arg1);
                    preferences.edit().putInt("stepsIn", msg.arg1).commit();
                    float percentage = Utils.getPercentage(msg.arg1, AppPreference.getInstance().getStepsCountTarget());
                    int b = (int) percentage;
                    seekArcComplete.setProgress(b);
                    AppPreference.getInstance().setPercentage(b);
                    ticker1.setText(b + "%");
                    double wirght = 0;
                    Log.e("sdsd", "sdsd " + AppPreference.getInstance().getGoalWeightKg());
                    if (AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("0") || AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase(null) || AppPreference.getInstance().getGoalWeightKg().equalsIgnoreCase("")) {
                        wirght = 40;
                    } else {
                        wirght = Double.parseDouble(AppPreference.getInstance().getGoalWeightKg());
                    }
                    double i2 = Utils.getCaloriesBurnt((wirght * 2.20462), msg.arg1);
                    txt_calories.setText("" + new DecimalFormat("##.##").format(i2) + " kcal");
                    AppPreference.getInstance().setCaloriesCount("" + new DecimalFormat("###.###").format(i2));
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

        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
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
            Log.e("firstDate", ":- " + firstDate);
            date = firstDate;
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
                                txt_time.setText("   ");
                                txt_to_time.setText("   ");
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
        } else {
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
            txt_time.setText("  ");
            txt_to_time.setText("  ");
            txt_date_time.setText("");
            liner_to_time.setVisibility(View.GONE);
            liner_date_t.setVisibility(View.GONE);
            date_time_picker.setVisibility(View.GONE);
            txt_click_here_add.setVisibility(View.VISIBLE);
            edt_subject.setText("");
            edt_deatils.setText("");
            getAllHealthList();
        }

    }


    private void getAllHealthList() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.HEALTH_LIST_NOTE + "limit=4&offset=0",
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
                                Log.e("in", " :-" + " in");
                                healthNoteItemses = ParseJsonData.getInstance().getHealthNoteListItem(response);
                                if (healthNoteItemses == null || healthNoteItemses.size() == 0) {
                                    healthNoteItemses = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
                                    if (healthNoteItemses != null & healthNoteItemses.size() > 0) {
                                        liner_bottomd.setVisibility(View.VISIBLE);
                                        noteData();
                                    } else {
                                        liner_bottomd.setVisibility(View.GONE);
                                    }
                                } else {
                                    if (healthNoteItemses != null & healthNoteItemses.size() > 0) {
                                        liner_bottomd.setVisibility(View.VISIBLE);
                                        noteData();
                                    } else {
                                        liner_bottomd.setVisibility(View.GONE);
                                    }
                                }

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
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            healthNoteItemses = DbOperations.getNoteList(CureFull.getInstanse().getActivityIsntanse());
                            if (healthNoteItemses != null & healthNoteItemses.size() > 0) {
                                liner_bottomd.setVisibility(View.VISIBLE);
                                noteData();
                            } else {
                                liner_bottomd.setVisibility(View.GONE);
                            }
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
            healthNoteItemses = DbOperations.getNoteListLanding(CureFull.getInstanse().getActivityIsntanse());
            if (healthNoteItemses != null & healthNoteItemses.size() > 0) {
                liner_bottomd.setVisibility(View.VISIBLE);
                noteData();
            } else {
                liner_bottomd.setVisibility(View.GONE);
            }
        }

    }


    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int mintues) {
        String todayTime = Utils.getTodayTime();
        String[] dfd = todayTime.split(":");
        String hrs = dfd[0];
        String mins = dfd[1];

        String date = getTodayDate();
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
                    txt_to_time.setText("");
                    toFirstTime = "";
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select greater than Current time.");
                }
            } else {
                if (date.equalsIgnoreCase(firstDate)) {
                    if (hourOfDay < Integer.parseInt(hrs) + 1 & mintues < Integer.parseInt(mins) + 1) {
                        newFirstTime = hourOfDay;
                        newFirstTimeMintues = mintues;
                        isSelectFrom = true;
                        firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                        txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                        txt_to_time.setText("");
                        toFirstTime = "";
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select less than Current time.");

                    }
                } else {
                    newFirstTime = hourOfDay;
                    newFirstTimeMintues = mintues;
                    isSelectFrom = true;
                    firstTime = (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                    txt_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    txt_to_time.setText("  ");
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

                    Log.e("value ", "" + " " + calendar3.getTime() + " " + calendar2.getTime() + " " + calendar1.getTime());

                    if (x.before(calendar1.getTime()) && x.after(calendar2.getTime())) {
                        //checkes whether the current time is between 14:49:00 and 20:11:13.
                        toFirstTime = "" + (hourOfDay < 10 ? "0" + hourOfDay : hourOfDay) + ":" + (mintues < 10 ? "0" + mintues : mintues) + ":" + "00";
                        txt_to_time.setText("" + Utils.updateTime(hourOfDay, mintues));
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select greater than first time.");

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
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please select greater than first time.");

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


        }


    }


    public void jsonUploadTarget() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());

            String steps = "" + preferences.getInt("stepsIn", 0);
            String running = "0";
            String cycling = "0";
            String waterintake = AppPreference.getInstance().getWaterInTake();
            String caloriesBurnt = AppPreference.getInstance().getCaloriesCount();
            String dateTime = getTodayDateTime();
            String[] dateParts = dateTime.split(" ");
            String date = dateParts[0];
            String timeReal = dateParts[1];

            JSONObject data = JsonUtilsObject.toSaveHealthAppDetails(steps, running, cycling, waterintake, caloriesBurnt, date, timeReal);
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
                                getDailyHealth();
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
        } else {

        }

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


    private void getDailyHealth() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            Log.e("date ", "date " + CureFull.getInstanse().getActivityIsntanse().getTodayDate());
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
                                    if (!json.getString("payload").equals(null)) {
                                        JSONObject json1 = new JSONObject(json.getString("payload"));
                                        JSONObject json2 = new JSONObject(json1.getString("dailyDetails"));
                                        String steps = json2.getString("steps");
                                        String waterIntakeDone = json2.getString("waterIntakeDone");
//                                        boolean goalSet = json2.getBoolean("goalSet");
                                        AppPreference.getInstance().setWaterInTake("" + waterIntakeDone);
                                        preferences.edit().putString("waterin", "" + waterIntakeDone).commit();
                                        String waterIntakeLeft1 = "0";
                                        if (json2.getString("waterIntakeLeft").equals(null) || json2.getString("waterIntakeLeft").equalsIgnoreCase("0") || json2.getString("waterIntakeLeft").equalsIgnoreCase("0.0") || json2.getString("waterIntakeLeft").equalsIgnoreCase(null) || json2.getString("waterIntakeLeft").equalsIgnoreCase("null")) {
                                            waterIntakeLeft1 = "0";
                                        } else {
                                            waterIntakeLeft1 = json2.getString("waterIntakeLeft");
                                        }
                                        waterLevel = Integer.parseInt(AppPreference.getInstance().getWaterInTake());
                                        if (waterLevel == 0) {
                                            img_minus_icon.setVisibility(View.INVISIBLE);
                                        }
                                        txt_water_level.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(AppPreference.getInstance().getWaterInTake()))) + " L");
                                        AppPreference.getInstance().setWaterInTakeLeft("" + waterIntakeLeft1);
                                        preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
                                        txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//                                        txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
                                        txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");

                                        if (!json1.getString("targetAndProfileDetails").equalsIgnoreCase("null")) {
                                            AppPreference.getInstance().setIsEditGoal(true);
                                            AppPreference.getInstance().setIsEditGoalPage(true);
                                            JSONObject json3 = new JSONObject(json1.getString("targetAndProfileDetails"));
                                            AppPreference.getInstance().setStepsCountTarget(Integer.parseInt(json3.getString("targetStepCount")));
                                            AppPreference.getInstance().setWaterInTakeTarget(json3.getString("targetWaterInTake"));
                                        }


                                        Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
                                        CureFull.getInstanse().getActivityIsntanse().startService(intent);
                                        doBindService();
                                    }

                                } else {
                                    Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
                                    CureFull.getInstanse().getActivityIsntanse().startService(intent);
                                    doBindService();

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
                            CureFull.getInstanse().getActivityIsntanse().startService(intent);
                            doBindService();
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
//            preferences.edit().putInt("stepsIn", Integer.parseInt(steps)).commit();
//                                        AppPreference.getInstance().setStepsCount("" + steps);
            txt_steps_counter.setText("" + preferences.getInt("stepsIn", 0));
//            txt_steps_counter.setText("" + AppPreference.getInstance().getStepsCount());
            txt_calories.setText("" + AppPreference.getInstance().getCaloriesCount() + " kcal");
            Intent intent = new Intent(CureFull.getInstanse().getActivityIsntanse(), MessengerService.class);
            CureFull.getInstanse().getActivityIsntanse().startService(intent);
            doBindService();
        }

    }


    private void getIncreseWaterInTake(String isture) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.INCRESE_WATER_INTAKE + isture,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getIncres, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (!json.getString("payload").equals(null)) {
                                    JSONObject json1 = new JSONObject(json.getString("payload"));
                                    String waterIntakeDone1 = json1.getString("waterIntakeDone");
                                    String waterIntakeLeft1 = "0";
                                    if (json1.getString("waterIntakeLeft").equals(null) || json1.getString("waterIntakeLeft").equalsIgnoreCase(null) || json1.getString("waterIntakeLeft").equalsIgnoreCase("null")) {
                                        waterIntakeLeft1 = "0";
                                    } else {
                                        waterIntakeLeft1 = json1.getString("waterIntakeLeft");
                                    }
                                    AppPreference.getInstance().setWaterInTake("" + waterIntakeDone1);
                                    preferences.edit().putString("waterin", "" + waterIntakeDone1).commit();
                                    AppPreference.getInstance().setWaterInTakeLeft("" + waterIntakeLeft1);
                                    waterLevel = Integer.parseInt(AppPreference.getInstance().getWaterInTake());
                                    if (waterLevel == 0) {
                                        img_minus_icon.setVisibility(View.INVISIBLE);
                                    } else {
                                        img_minus_icon.setVisibility(View.VISIBLE);
                                    }
                                    txt_water_level.setText("" + new DecimalFormat("###.##").format(Utils.getMlToLiter(Integer.parseInt(AppPreference.getInstance().getWaterInTake()))) + " L");

                                }

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


    public void noteData() {
        String dateTime = healthNoteItemses.get(0).getNote_date();
        String[] dateParts = dateTime.split("-");
        String years = dateParts[0];
        String months = dateParts[1];
        String days = dateParts[2];
        String times = healthNoteItemses.get(0).getNote_time();
        String[] dateParts1 = times.split(":");
        String hrs = dateParts1[0];
        String mins = dateParts1[1];

        Log.e("new value", "" + healthNoteItemses.get(0).getNote_to_time());

        if (healthNoteItemses.get(0).getNote_to_time().equalsIgnoreCase("null") || healthNoteItemses.get(0).getNote_to_time().equalsIgnoreCase("")) {
            try {
                Log.e("time", "" + hrs + ":- " + mins);
                txt_date_times.setText("" + days + " " + Utils.formatMonth(months) + "-" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            String times1 = healthNoteItemses.get(0).getNote_to_time();
            String[] dateParts11 = times1.split(":");
            String hrs1 = dateParts11[0];
            String mins1 = dateParts11[1];

            Log.e("hi", "hi" + times1);
            try {
                txt_date_time.setText("" + days + " " + Utils.formatMonth(months) + "\n" + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)) + " to " + CureFull.getInstanse().getActivityIsntanse().updateTime(Integer.parseInt(hrs1), Integer.parseInt(mins1)));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        String name = healthNoteItemses.get(0).getNote_heading();
        String comma = " : ";
        String gameName = healthNoteItemses.get(0).getDeatils();

        String meassgeTxt = name + comma + gameName;

        Spannable sb = new SpannableString(meassgeTxt);
        Typeface font = Typeface.createFromAsset(CureFull.getInstanse().getActivityIsntanse().getAssets(), "Montserrat-Bold.ttf");
        sb.setSpan(new ForegroundColorSpan(CureFull.getInstanse().getActivityIsntanse().getResources()
                        .getColor(R.color.health_yellow)), meassgeTxt.indexOf(name),
                meassgeTxt.indexOf(name) + name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new CustomTypefaceSpan("", font), meassgeTxt.indexOf(name), meassgeTxt.indexOf(name) + name.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        sb.setSpan(new StyleSpan(android.graphics.Typeface.BOLD),
                meassgeTxt.indexOf(name),
                meassgeTxt.indexOf(name) + name.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(CureFull.getInstanse().getActivityIsntanse().getResources()
                        .getColor(R.color.health_yellow)), meassgeTxt.indexOf(comma),
                meassgeTxt.indexOf(comma) + comma.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        sb.setSpan(new ForegroundColorSpan(CureFull.getInstanse().getActivityIsntanse().getResources()
                        .getColor(R.color.health_yellow)), meassgeTxt.indexOf(gameName),
                meassgeTxt.indexOf(gameName) + gameName.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        txt_title.setText(sb);
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


    public void jsonUploadTargetSteps(Context context, int stepsCount, String caloriesBurnts, String waterin, final String dateTimes, final int i, final int size) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(context);
        String steps = "" + stepsCount;
        String running = "0";
        String cycling = "0";
        String waterintake = waterin;
        String caloriesBurnt = caloriesBurnts;
        String dateTime = dateTimes;
        String[] dateParts = dateTime.split(" ");
        String date = dateParts[0];
        String timeReal = dateParts[1];

        JSONObject data = JsonUtilsObject.toSaveHealthAppDetails(steps, running, cycling, waterintake, caloriesBurnt, date, timeReal);
        Log.e("jsonUploadTarget", ": " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_HELTHAPP_DETALS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                            DbOperations.deleteSteps(dateTimes);
                            if (i == (size - 1)) {
                                getDailyHealth();
                            }
                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
        requestQueue.add(jsonObjectRequest);
    }

}