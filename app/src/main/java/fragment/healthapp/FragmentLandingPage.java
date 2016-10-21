package fragment.healthapp;


import android.Manifest;
import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.fitness.Fitness;
import com.google.android.gms.fitness.FitnessStatusCodes;
import com.google.android.gms.fitness.data.DataPoint;
import com.google.android.gms.fitness.data.DataSource;
import com.google.android.gms.fitness.data.DataType;
import com.google.android.gms.fitness.data.Device;
import com.google.android.gms.fitness.data.Field;
import com.google.android.gms.fitness.data.Value;
import com.google.android.gms.fitness.request.DataSourcesRequest;
import com.google.android.gms.fitness.request.OnDataPointListener;
import com.google.android.gms.fitness.request.SensorRequest;
import com.google.android.gms.fitness.result.DataSourcesResult;
import com.google.android.gms.location.ActivityRecognition;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.AppPreference;
import utils.CircularImageView;
import utils.SeekArc;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLandingPage extends Fragment {

    private View rootView;
    private TextView txt_name, txt_health_note;
    //    private CircularImageView circularImageView;
    public static final String TAG = "BasicSensorsApi";
    // [START auth_variable_references]
    private GoogleApiClient mClient = null;
    // [END auth_variable_references]
    private static final int PERMISSION_REQUEST_ACCESS_FINE_LOCATION = 940;
    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    // [START mListener_variable_reference]
    // Need to hold a reference to this listener, as it's passed into the "unregister"
    // method in order to stop all sensors from sending data to this listener.
    private OnDataPointListener mListener;
    // [END mListener_variable_reference]

    private LinearLayout linear_health_app;
    private ActivityRecognition arclient;
    private RelativeLayout realtive_notes;
    private TickerView ticker1, text_steps_count;
    private FloatingActionButton img_fab;
    ImageButton imageButton;
    LinearLayout revealView, layoutButtons;
    Animation alphaAnimation;
    float pixelDensity;
    boolean flag = true;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();
    private SeekArc seekArcComplete;
    private Boolean isFabOpen = false;
    private Animation fab_open, fab_close, rotate_forward, rotate_backward;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_landing_page_new,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(false);
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        text_steps_count = (TickerView) rootView.findViewById(R.id.text_steps_count);
        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        text_steps_count.setCharacterList(NUMBER_LIST);
        ticker1.setCharacterList(NUMBER_LIST);
        ticker1.setText("60" + "%");


        img_fab = (FloatingActionButton) rootView.findViewById(R.id.img_fab);
        pixelDensity = getResources().getDisplayMetrics().density;
//
//        imageView = (ImageView) rootView.findViewById(R.id.imageView);
//        imageButton = (ImageButton) rootView.findViewById(R.id.launchTwitterAnimation);
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
//
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        img_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                animateFAB();
                launchTwitter(view);
            }
        });
//
        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
//        linear_health_app = (LinearLayout) rootView.findViewById(R.id.linear_health_app);

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


//        linear_health_app.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CureFull.getInstanse().getFlowInstanseAll()
//                        .replaceWithTopBottomAnimation(new FragmentHealthApp(), null, true);
//            }
//        });

//        CureFull.getInstanse().getFullImageLoader().startLazyLoading(AppPreference.getInstance().getFacebookProfileImage(), circularImageView);

        setProgressUpdateAnimation(60);

        rotate_forward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_forward);
        rotate_backward = AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_backward);
        return rootView;
    }

    public void setProgressUpdateAnimation(int value) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(seekArcComplete, "progress", 0, value);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }


    @Override
    public void onResume() {
        super.onResume();

        // This ensures that if the user denies the permissions then uses Settings to re-enable
        // them, the app will start working.
        buildFitnessClient();
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
        }
    }
    // [END auth_build_googleapiclient_beginning]

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
                buildFitnessClient();
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
        } else {
            img_fab.startAnimation(rotate_forward);
            isFabOpen = true;
            Log.d("Raj", "open");

        }

    }
}