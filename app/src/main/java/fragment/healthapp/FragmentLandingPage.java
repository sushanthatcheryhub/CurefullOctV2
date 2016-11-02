package fragment.healthapp;


import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import android.view.animation.LinearInterpolator;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

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
import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.Bucket;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSet;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataReadRequest;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DailyTotalResult;
import com.google.android.gms.fitness.result.DataReadResult;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.ActivityRecognition;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
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
import item.property.HealthNoteItems;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.MyConstants;
import utils.SeekArc;
import utils.SwitchDateTimeDialogFragment;

import static java.text.DateFormat.getDateInstance;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLandingPage extends BaseBackHandlerFragment implements View.OnClickListener, TimePickerDialog.OnTimeSetListener {

    private View rootView;
    private RecyclerView recyclerView_notes;
    private TextView txt_no_list_health_note, txt_name, txt_health_note, btn_set_goal, txt_date_time, txt_time, txt_to_time, btn_done, txt_click_here_add;
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
    private LinearLayout linear_health_app, liner_date_t, liner_to_time;
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
    private SwitchDateTimeDialogFragment dateTimeFragment;
    private EditText edt_deatils, edt_subject;
    private Health_Note_Landing_ListAdpter health_note_listAdpter;
    List<HealthNoteItems> healthNoteItemses = new ArrayList<HealthNoteItems>();
    private String firstDate = "", firstTime = "", toFirstTime = "";
    SharedPreferences preferences;
    @Override
    public boolean onBackPressed() {
        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        return super.onBackPressed();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_landing_page_new,
                container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        CureFull.getInstanse().getActivityIsntanse().showLogo(false);
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
        ticker1.setText("60" + "%");
        btn_set_goal = (TextView) rootView.findViewById(R.id.btn_set_goal);
        img_fab = (FloatingActionButton) rootView.findViewById(R.id.img_fab);
        pixelDensity = getResources().getDisplayMetrics().density;
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        linear_lab_report_click.setOnClickListener(this);
        linear_prescription_click.setOnClickListener(this);
        img_fab.setOnClickListener(this);
        realtive_click.setOnClickListener(this);
        liner_click.setOnClickListener(this);
        btn_set_goal.setOnClickListener(this);
        txt_date_time.setOnClickListener(this);
        txt_to_time.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        txt_click_here_add.setOnClickListener(this);

        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);

        txt_health_note = (TextView) rootView.findViewById(R.id.txt_health_note);

//        txt_name = (TextView) rootView.findViewById(R.id.txt_name);
//        circularImageView = (CircularImageView) rootView.findViewById(R.id.circularImageView);
//        txt_name.setText("Hi ! " + AppPreference.getInstance().getFacebookUserName());
        Log.e("image", ": " + AppPreference.getInstance().getFacebookProfileImage());
//        URL url = null;
//        try {
//            url = new URL(AppPreference.getInstance().getFacebookProfileImage());
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            circularImageView.setImageBitmap(bmp);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        // When permissions are revoked the app is restarted so onCreate is sufficient to check for
        // permissions core to the Activity's functionality.
        if (!checkPermissions()) {
            requestPermissions();
        }


//        CureFull.getInstanse().getFullImageLoader().startLazyLoading(AppPreference.getInstance().getFacebookProfileImage(), circularImageView);

        setProgressUpdateAnimation(60);

        rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);

        // Construct SwitchDateTimePicker
        dateTimeFragment = (SwitchDateTimeDialogFragment) getActivity().getSupportFragmentManager().findFragmentByTag(TAG_DATETIME_FRAGMENT);
        if (dateTimeFragment == null) {
            dateTimeFragment = SwitchDateTimeDialogFragment.newInstance(
                    getString(R.string.label_datetime_dialog),
                    getString(R.string.positive_button_datetime_picker),
                    getString(R.string.negative_button_datetime_picker)
            );
        }
        // Assign values we want

        String time = getTodayTime();
        if (!time.equalsIgnoreCase("")) {
            String[] dateParts1 = time.split(":");
            String hrs = dateParts1[0];
            String mins = dateParts1[1];
            dateTimeFragment.setHour(Integer.parseInt(hrs));
            dateTimeFragment.setMinute(Integer.parseInt(mins));
        } else {
            dateTimeFragment.setHour(12);
            dateTimeFragment.setMinute(12);
        }

        // Set listener for get Date
        dateTimeFragment.setOnButtonClickListener(new SwitchDateTimeDialogFragment.OnButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Date date) {

                Log.e("date ", ":- " + date.toString());


                String dateTime = date.toString();
                String[] dateParts = dateTime.split(" ");
                String days = dateParts[0];
                String month = dateParts[1];
                String dates = dateParts[2];
                String time = dateParts[3];
                String year = dateParts[5];
                String times = time.toString();
                txt_date_time.setText("" + dates + " " + month);
                String[] dateParts1 = times.split(":");
                String hrs = dateParts1[0];
                String mins = dateParts1[1];

                firstDate = "" + year + "-" + MyConstants.getMonthName(month) + "-" + dates;
                firstTime = hrs + ":" + mins;
                txt_time.setText("" + updateTime(Integer.parseInt(hrs), Integer.parseInt(mins)));
            }

            @Override
            public void onNegativeButtonClick(Date date) {
                txt_date_time.setText("");
            }
        });

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_notes.setLayoutManager(mLayoutManager);
//        txt_no_list_health_note.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showAdpter();
//            }
//        }, 100);

        CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(AppPreference.getInstance().getUserName(), AppPreference.getInstance().getUserID());
        getAllHealthList();
        preferences.edit().putBoolean("destroy", false).commit();
        CureFull.getInstanse().getActivityIsntanse().startFitService();
        return rootView;
    }

    public void setProgressUpdateAnimation(int value) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(seekArcComplete, "progress", 0, value);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }

    public void showAdpter() {
        if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
            txt_no_list_health_note.setVisibility(View.GONE);
            recyclerView_notes.setVisibility(View.VISIBLE);
            health_note_listAdpter = new Health_Note_Landing_ListAdpter(CureFull.getInstanse().getActivityIsntanse(), healthNoteItemses);
            recyclerView_notes.setAdapter(health_note_listAdpter);
            health_note_listAdpter.notifyDataSetChanged();
        } else {
            txt_no_list_health_note.setVisibility(View.VISIBLE);
            recyclerView_notes.setVisibility(View.GONE);
            animateFAB();
            launchTwitter(rootView);
        }

    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        mClient.stopAutoManage(getActivity());
//        mClient.disconnect();
//    }

    @Override
    public void onResume() {
        super.onResume();

        // This ensures that if the user denies the permissions then uses Settings to re-enable
        // them, the app will start working.
//        buildFitnessClient();
    }
    // [END auth_oncreate_setup]

    // [START auth_build_googleapiclient_beginning]

    /**
     * Build a {@link GoogleApiClient} that will authenticate the user and allow the application
     * to connect to Fitness APIs. The scopes included should match the scopes your app needs
     * (see documentation for details). Authentication will occasionally fail intentionally,
     * and in those cases, there will be a known resolution, which the OnConnectionFailedListener()
     * can address. Examples of this include the user never having signed in before, or having
     * multiple accounts on the device and needing to specify which account to use, etc.
     */
    private void buildFitnessClient() {
        Log.e(TAG, "Done");
        if (mClient == null && checkPermissions()) {
            mClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(Fitness.SENSORS_API)
                    .addScope(new Scope(Scopes.FITNESS_BODY_READ))
                    .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
                    .addConnectionCallbacks(
                            new GoogleApiClient.ConnectionCallbacks() {
                                @Override
                                public void onConnected(Bundle bundle) {
                                    Log.e(TAG, "Connected!!!");
                                    // Now you can make calls to the Fitness APIs.
//                                    subscribe();
                                    findFitnessDataSources();
//                                    getWeather();
//                                    new ViewWeekStepCountTask().execute();
//                                    Calendar cal = Calendar.getInstance();
//                                    Date now = new Date();
//                                    cal.setTime(now);
//                                    long endTime = cal.getTimeInMillis();
//                                    cal.add(Calendar.WEEK_OF_YEAR, -1);
//                                    long startTime = cal.getTimeInMillis();
//                                    java.text.DateFormat dateFormat = getDateInstance();
//                                    Log.e(TAG, "Range Start: " + dateFormat.format(startTime));
//                                    Log.e(TAG, "Range End: " + dateFormat.format(endTime));
                                }

                                @Override
                                public void onConnectionSuspended(int i) {
                                    // If your connection to the sensor gets lost at some point,
                                    // you'll be able to determine the reason and react to it here.
                                    if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
                                        Log.e(TAG, "Connection lost.  Cause: Network Lost.");
                                    } else if (i
                                            == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
                                        Log.e(TAG,
                                                "Connection lost.  Reason: Service Disconnected");
                                    }
                                }
                            }
                    )
                    .enableAutoManage(getActivity(), 0, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(ConnectionResult result) {
                            Log.e(TAG, "Google Play services connection failed. Cause: " +
                                    result.toString());
                        }
                    })
                    .build();
        } else {
            Log.e(TAG, "check");
        }
    }


    private class ViewWeekStepCountTask extends AsyncTask<Void, Void, Void> {
        protected Void doInBackground(Void... params) {
//            displayLastWeeksData();
            displayStepDataForToday();
            return null;
        }
    }


    public void displayLastWeeksData() {
        Calendar cal = Calendar.getInstance();
        Date now = new Date();
        cal.setTime(now);
        long endTime = cal.getTimeInMillis();
        cal.add(Calendar.WEEK_OF_YEAR, -1);
        long startTime = cal.getTimeInMillis();

        java.text.DateFormat dateFormat = DateFormat.getDateInstance();
        Log.e("History", "Range Start: " + dateFormat.format(startTime));
        Log.e("History", "Range End: " + dateFormat.format(endTime));

//Check how many steps were walked and recorded in the last 7 days
        DataReadRequest readRequest = new DataReadRequest.Builder()
                .aggregate(DataType.TYPE_STEP_COUNT_DELTA, DataType.AGGREGATE_STEP_COUNT_DELTA)
                .bucketByTime(1, TimeUnit.DAYS)
                .setTimeRange(startTime, endTime, TimeUnit.MILLISECONDS)
                .build();


        DataReadResult dataReadResult = Fitness.HistoryApi.readData(mClient, readRequest).await(1, TimeUnit.MINUTES);

        //Used for aggregated data
        if (dataReadResult.getBuckets().size() > 0) {
            Log.e("History", "Number of buckets: " + dataReadResult.getBuckets().size());
            for (Bucket bucket : dataReadResult.getBuckets()) {
                List<DataSet> dataSets = bucket.getDataSets();
                for (DataSet dataSet : dataSets) {
                    showDataSet(dataSet);
                }
            }
        }


//Used for non-aggregated data
        else if (dataReadResult.getDataSets().size() > 0) {
            Log.e("History", "Number of returned DataSets: " + dataReadResult.getDataSets().size());
            for (DataSet dataSet : dataReadResult.getDataSets()) {
                showDataSet(dataSet);
            }
        }

    }

    private void displayStepDataForToday() {
        DailyTotalResult result = Fitness.HistoryApi.readDailyTotal(mClient, DataType.TYPE_STEP_COUNT_CUMULATIVE).await(1, TimeUnit.MINUTES);
        showDataSet(result.getTotal());
    }

    private void showDataSet(DataSet dataSet) {
        Log.e("History", "Data returned for Data type: " + dataSet.getDataType().getName());
        DateFormat dateFormat = DateFormat.getDateInstance();
        DateFormat timeFormat = DateFormat.getTimeInstance();

        for (DataPoint dp : dataSet.getDataPoints()) {
            Log.e("History", "Data point:");
            Log.e("History", "\tType: " + dp.getDataType().getName());
            Log.e("History", "\tStart: " + dateFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            Log.e("History", "\tEnd: " + dateFormat.format(dp.getEndTime(TimeUnit.MILLISECONDS)) + " " + timeFormat.format(dp.getStartTime(TimeUnit.MILLISECONDS)));
            for (Field field : dp.getDataType().getFields()) {
                Log.e("History", "\tField: " + field.getName() +
                        " Value: " + dp.getValue(field));
            }
        }
    }

    public void subscribe() {
        // To create a subscription, invoke the Recording API. As soon as the subscription is
        // active, fitness data will start recording.
        // [START subscribe_to_datatype]
        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    }
                });

        Fitness.RecordingApi.subscribe(mClient, DataType.TYPE_STEP_COUNT_CUMULATIVE)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            if (status.getStatusCode()
                                    == FitnessStatusCodes.SUCCESS_ALREADY_SUBSCRIBED) {
                                Log.i(TAG, "Existing subscription for activity detected.");
                            } else {
                                Log.i(TAG, "Successfully subscribed!");
                            }
                        } else {
                            Log.i(TAG, "There was a problem subscribing.");
                        }
                    }
                });
        // [END subscribe_to_datatype]
    }


    /**
     * Find available data sources and attempt to register on a specific {@link DataType}.
     * If the application cares about a data type but doesn't care about the source of the data,
     * this can be skipped entirely, instead calling
     * {@link com.google.android.gms.fitness.SensorsApi
     * #register(GoogleApiClient, SensorRequest, DataSourceListener)},
     * where the {@link SensorRequest} contains the desired data type.
     */
    private void findFitnessDataSources() {
        // [START find_data_sources]
        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
                // At least one datatype must be specified.
                .setDataTypes(
                        DataType.TYPE_STEP_COUNT_CUMULATIVE,
                        DataType.TYPE_DISTANCE_CUMULATIVE,
                        DataType.TYPE_CYCLING_WHEEL_RPM,
                        DataType.TYPE_SPEED, DataType.TYPE_LOCATION_SAMPLE, DataType.TYPE_CALORIES_EXPENDED, DataType.AGGREGATE_STEP_COUNT_DELTA, DataType.TYPE_CALORIES_CONSUMED, DataType.TYPE_LOCATION_TRACK
                )
                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
                .build())
                .setResultCallback(new ResultCallback<DataSourcesResult>() {
                    @Override
                    public void onResult(DataSourcesResult dataSourcesResult) {
                        Log.e(TAG, "Result: " + dataSourcesResult.getStatus().toString());
                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
                            Device device = dataSource.getDevice();
                            String fields = dataSource.getDataType().getFields().toString();
                            Log.e("dfd", "fdfd" + device.getManufacturer() + " " + device.getModel() + " [" + dataSource.getDataType().getName() + " " + fields + "]");
                            final DataType dataType = dataSource.getDataType();
                            Log.e("dataType", " " + dataType.toString());

                            if (dataType.equals(DataType.TYPE_STEP_COUNT_CUMULATIVE) ||
                                    dataType.equals(DataType.TYPE_DISTANCE_CUMULATIVE)) {

                                Fitness.SensorsApi.add(mClient,
                                        new SensorRequest.Builder()
                                                .setDataSource(dataSource)
                                                .setDataType(dataSource.getDataType())
                                                .setSamplingRate(10, TimeUnit.SECONDS)
                                                .build(),
                                        new OnDataPointListener() {
                                            @Override
                                            public void onDataPoint(DataPoint dataPoint) {
                                                String msg = "onDataPoint: ";
                                                for (final Field field : dataPoint.getDataType().getFields()) {
                                                    Value value = dataPoint.getValue(field);
                                                    msg += "onDataPoint: " + field.getName() + "=" + value.asInt() + ", ";
                                                    final int val = value.asInt();
                                                    Log.e("hi", "Filed:- " + msg);
                                                    getActivity().runOnUiThread(new Runnable() {
                                                        @Override
                                                        public void run() {
//                                                            txt_ehr_upload.setText("" + field.getName());
                                                            text_steps_count.setText("" + val);
                                                            txt_health_note.setText("" + getDistanceRun(val) + " KM");
                                                        }
                                                    });
                                                    Log.e("all ", ":- " + msg);
                                                }


                                            }
                                        })
                                        .setResultCallback(new ResultCallback<Status>() {
                                            @Override
                                            public void onResult(Status status) {
                                                if (status.isSuccess()) {
                                                    Log.i(TAG, "Listener registered!");
                                                } else {
                                                    Log.i(TAG, "Listener not registered.");
                                                }
                                            }
                                        });
                            }
                        }
                    }
                });
        // [END find_data_sources]
    }


    private void registerFitnessDataListenerLocation(DataSource dataSource, DataType dataType) {
        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (Field field : dataPoint.getDataType().getFields()) {
                    Value val = dataPoint.getValue(field);
                    Log.i(TAG, "Detected DataPoint field: " + field.getName());
                    Log.i(TAG, "Detected DataPoint value: " + val);
                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(10, TimeUnit.SECONDS)
                        .build(),
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.i(TAG, "Listener registered!");
                        } else {
                            Log.i(TAG, "Listener not registered.");
                        }
                    }
                });
        // [END register_data_listener]
    }

    /**
     * Register a listener with the Sensors API for the provided {@link DataSource} and
     * {@link DataType} combo.
     */
    private void registerFitnessDataListener(DataSource dataSource, DataType dataType) {
        // [START register_data_listener]
        mListener = new OnDataPointListener() {
            @Override
            public void onDataPoint(DataPoint dataPoint) {
                for (final Field field : dataPoint.getDataType().getFields()) {
                    final Value val = dataPoint.getValue(field);
                    Log.e(TAG, "Detected DataPoint field: " + field.getName());
                    Log.e(TAG, "Detected DataPoint value: " + val);
                    if (field.getName().compareToIgnoreCase("steps") == 0) {

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                Toast.makeText(getActivity(), "field " + field.getName() + " value " + val, Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
//                    Toast.makeText(getActivity(),"field "+ field.getName() +"value "+val,Toast.LENGTH_SHORT).show();
                }
            }
        };

        Fitness.SensorsApi.add(
                mClient,
                new SensorRequest.Builder()
                        .setDataSource(dataSource) // Optional but recommended for custom data sets.
                        .setDataType(dataType) // Can't be omitted.
                        .setSamplingRate(10, TimeUnit.SECONDS)
                        .build(),
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e(TAG, "Listener registered!");
                        } else {
                            Log.e(TAG, "Listener not registered.");
                        }
                    }
                });
        // [END register_data_listener]
    }

    /**
     * Unregister the listener with the Sensors API.
     */
    private void unregisterFitnessDataListener() {
        if (mListener == null) {
            // This code only activates one listener at a time.  If there's no listener, there's
            // nothing to unregister.
            return;
        }

        // [START unregister_data_listener]
        // Waiting isn't actually necessary as the unregister call will complete regardless,
        // even if called from within onStop, but a callback can still be added in order to
        // inspect the results.
        Fitness.SensorsApi.remove(
                mClient,
                mListener)
                .setResultCallback(new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (status.isSuccess()) {
                            Log.e(TAG, "Listener was removed!");
                        } else {
                            Log.e(TAG, "Listener was not removed.");
                        }
                    }
                });
        // [END unregister_data_listener]
    }


    /**
     *  Initialize a custom log class that outputs both to in-app targets and logcat.
     */

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {
            Log.e(TAG, "Displaying permission rationale to provide additional context.");
        } else {
            Log.e(TAG, "Requesting permission");
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        Log.e(TAG, "onRequestPermissionResult");
        if (requestCode == REQUEST_PERMISSIONS_REQUEST_CODE) {
            if (grantResults.length <= 0) {
                // If user interaction was interrupted, the permission request is cancelled and you
                // receive empty arrays.
                Log.e(TAG, "User interaction was cancelled.");
            } else if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission was granted.
//                buildFitnessClient();
            } else {
                // Permission denied.

                // In this Activity we've chosen to notify the user that they
                // have rejected a core permission for the app since it makes the Activity useless.
                // We're communicating this message in a Snackbar since this is a sample app, but
                // core permissions would typically be best requested during a welcome-screen flow.

                // Additionally, it is important to remember that a permission might have been
                // rejected without asking the user for permission (device policy or "Never ask
                // again" prompts). Therefore, a user interface affordance is typically implemented
                // when permissions are denied. Otherwise, your app could appear unresponsive to
                // touches or interactions which have required permissions.

            }
        }
    }

    public float getDistanceRun(long steps) {
        float distance = (float) (steps * 78) / (float) 100000;
        return distance;
    }

    private void getWeather() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSION_REQUEST_ACCESS_FINE_LOCATION);
        } else {
            Awareness.SnapshotApi.getWeather(mClient)
                    .setResultCallback(new ResultCallback<WeatherResult>() {
                        @Override
                        public void onResult(@NonNull WeatherResult weatherResult) {
                            if (weatherResult.getStatus().isSuccess()) {
                                Weather weather = weatherResult.getWeather();

                                int[] conditions = weather.getConditions();
                                StringBuilder stringBuilder = new StringBuilder();
                                if (conditions.length > 0) {
                                    for (int i = 0; i < conditions.length; i++) {
                                        if (i > 0) {
                                            stringBuilder.append(", ");
                                        }
                                        stringBuilder.append(retrieveConditionString(conditions[i]));

                                    }
                                }

//                                mConditionsText.setText(getString(R.string.text_conditions,
//                                        stringBuilder.toString()));

                                float humidity = weather.getHumidity();

                                float temperature = weather.getTemperature(Weather.CELSIUS);

                                float dewPoint = weather.getDewPoint(Weather.CELSIUS);

                                float feelsLikeTemperature = weather.getFeelsLikeTemperature(Weather.CELSIUS);

//                                txt_data_analytics.setText("" + stringBuilder.toString() + " humidity: " + weather.getHumidity() + " temperature: " + weather.getTemperature(Weather.CELSIUS) + " dewPoint:" + dewPoint + " feelsLikeTemperature: " + feelsLikeTemperature);
                            } else {
                                Snackbar.make(rootView,
                                        getString(R.string.error_weather),
                                        Snackbar.LENGTH_LONG).show();
                            }
                        }
                    });
        }
    }

    private String retrieveConditionString(int condition) {
        switch (condition) {
            case Weather.CONDITION_CLEAR:
                return getString(R.string.condition_clear);
            case Weather.CONDITION_CLOUDY:
                return getString(R.string.condition_cloudy);
            case Weather.CONDITION_FOGGY:
                return getString(R.string.condition_foggy);
            case Weather.CONDITION_HAZY:
                return getString(R.string.condition_hazy);
            case Weather.CONDITION_ICY:
                return getString(R.string.condition_icy);
            case Weather.CONDITION_RAINY:
                return getString(R.string.condition_rainy);
            case Weather.CONDITION_SNOWY:
                return getString(R.string.condition_snowy);
            case Weather.CONDITION_STORMY:
                return getString(R.string.condition_stormy);
            case Weather.CONDITION_WINDY:
                return getString(R.string.condition_windy);
            default:
                return getString(R.string.condition_unknown);
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

        if (flag) {

//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);

            FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                    revealView.getLayoutParams();
            parameters.height = realtive_notes.getHeight();
            revealView.setLayoutParams(parameters);

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

            flag = false;
        } else {

//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);

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
            flag = true;
        }
    }

    public void animateFAB() {

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
            case R.id.realtive_click:
                CureFull.getInstanse().getFlowInstanseAll()
                        .add(new FragmentHealthAppNew(), true);
                break;
            case R.id.liner_click:
                CureFull.getInstanse().getFlowInstanseAll()
                        .add(new FragmentHealthAppNew(), true);
                break;
            case R.id.btn_set_goal:
                CureFull.getInstanse().getFlowInstanseAll()
                        .add(new FragmentEditGoal(), true);
                break;
            case R.id.linear_lab_report_click:
                CureFull.getInstanse().getFlowInstanseAll()
                        .add(new FragmentLabTestReport(), true);
                break;
            case R.id.linear_prescription_click:
//
//                CureFull.getInstanse().getFlowInstanseAll()
//                        .add(new FragmentPrescriptionCheck(), true);
                CureFull.getInstanse().getFlowInstanseAll()
                        .add(new FragmentHealthNote(), true);

                break;
            case R.id.img_fab:
                if (healthNoteItemses != null && healthNoteItemses.size() > 0) {
                    animateFAB();
                    launchTwitter(view);
                }

                break;
            case R.id.txt_date_time:
                dateTimeFragment.show(getActivity().getSupportFragmentManager(), TAG_DATETIME_FRAGMENT);
                break;
            case R.id.txt_to_time:

                final Calendar c = Calendar.getInstance();
                // Current Hour
                int hour = c.get(Calendar.HOUR_OF_DAY);
                // Current Minute
                int minute = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(getActivity(), this, hour, minute, false);
                timePickerDialog.show();
                break;
            case R.id.btn_done:
                if (!validateSubject()) {
                    return;
                }

                if (!validateDeatils()) {
                    return;
                }

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
        } else {
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
                        if (responseStatus == 100) {
                            getAllHealthList();
                            isFabOpen = true;
                            animateFAB();
                            launchTwitter(rootView);
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
                        Log.e("getSymptomsList, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == 100) {
                            healthNoteItemses = ParseJsonData.getInstance().getHealthNoteListItem(response);
                            showAdpter();
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
        toFirstTime = "" + hourOfDay + ":" + mintues;
        txt_to_time.setText("" + updateTime(hourOfDay, mintues));
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
}