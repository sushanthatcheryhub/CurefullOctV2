//package curefull.healthapp;
//
//import android.app.IntentService;
//import android.app.PendingIntent;
//import android.content.Intent;
//import android.content.IntentSender;
//import android.content.SharedPreferences;
//import android.os.Bundle;
//import android.os.Handler;
//import android.preference.PreferenceManager;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.Scopes;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Scope;
//import com.google.android.gms.common.api.Status;
//import com.google.android.gms.fitness.Fitness;
//import com.google.android.gms.fitness.data.DataPoint;
//import com.google.android.gms.fitness.data.DataSource;
//import com.google.android.gms.fitness.data.DataType;
//import com.google.android.gms.fitness.data.Device;
//import com.google.android.gms.fitness.data.Field;
//import com.google.android.gms.fitness.data.Value;
//import com.google.android.gms.fitness.request.DataSourcesRequest;
//import com.google.android.gms.fitness.request.OnDataPointListener;
//import com.google.android.gms.fitness.request.SensorRequest;
//import com.google.android.gms.fitness.result.DataSourcesResult;
//import com.google.android.gms.location.ActivityRecognition;
//import com.google.android.gms.location.ActivityRecognitionResult;
//import com.google.android.gms.location.DetectedActivity;
//
//import java.util.List;
//import java.util.concurrent.TimeUnit;
//
///**
// * Created by Sushant Hatcheryhub on 02-11-2016.
// */
//
//public class FitGoogleService extends IntentService {
//    public static final String TAG = "BasicSensorsApi";
//    private GoogleApiClient mClient = null;
//    private boolean mTryingToConnect = false;
//    SharedPreferences preferences;
//
//    public FitGoogleService() {
//        super("FitGoogleService");
//    }
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        Log.e(TAG, "onCreate");
//        preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        if (mClient == null) {
//            Log.e(TAG, "not");
//            buildFitnessClient();
//        }
//
//    }
//
//
//    @Override
//    protected void onHandleIntent(Intent intent) {
//        Log.e(TAG, "onHandleIntent");
//        if (!mClient.isConnected()) {
//            mTryingToConnect = true;
//            mClient.connect();
//
//            //Wait until the service either connects or fails to connect
//            while (mTryingToConnect) {
//                try {
//                    Thread.sleep(100, 0);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//
//        if (mClient.isConnected()) {
//            Log.e(TAG, "done done");
//            findFitnessDataSources();
//        } else {
//            //Not connected
//            Log.e(TAG, "Fit wasn't able to connect, so the request failed.");
//        }
//    }
//
//    private void handleDetectedActivities(List<DetectedActivity> probableActivities) {
//        for (DetectedActivity activity : probableActivities) {
//            switch (activity.getType()) {
//                case DetectedActivity.IN_VEHICLE: {
//                    Log.e("ActivityRecogition", "In Vehicle: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.ON_BICYCLE: {
//                    Log.e("ActivityRecogition", "On Bicycle: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.ON_FOOT: {
//                    Log.e("ActivityRecogition", "On Foot: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.RUNNING: {
//                    Log.e("ActivityRecogition", "Running: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.STILL: {
//                    Log.e("ActivityRecogition", "Still: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.TILTING: {
//                    Log.e("ActivityRecogition", "Tilting: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.WALKING: {
//                    Log.e("ActivityRecogition", "Walking: " + activity.getConfidence());
//                    break;
//                }
//                case DetectedActivity.UNKNOWN: {
//                    Log.e("ActivityRecogition", "Unknown: " + activity.getConfidence());
//                    break;
//                }
//            }
//        }
//    }
//
//
//    private void buildFitnessClient() {
//        Log.e(TAG, "Done");
//        mClient = new GoogleApiClient.Builder(this)
//                .addApi(Fitness.SENSORS_API)
//                .addApi(ActivityRecognition.API)
//                .addScope(new Scope(Scopes.FITNESS_BODY_READ))
//                .addScope(new Scope(Scopes.FITNESS_LOCATION_READ))
//                .addScope(new Scope(Scopes.FITNESS_ACTIVITY_READ))
//                .addConnectionCallbacks(
//                        new GoogleApiClient.ConnectionCallbacks() {
//                            @Override
//                            public void onConnected(Bundle bundle) {
//                                Log.e(TAG, "Connected!!!");
//                                mTryingToConnect = false;
//                                findFitnessDataSources();
//
//                            }
//
//                            @Override
//                            public void onConnectionSuspended(int i) {
//                                // If your connection to the sensor gets lost at some point,
//                                // you'll be able to determine the reason and react to it here.
//                                if (i == GoogleApiClient.ConnectionCallbacks.CAUSE_NETWORK_LOST) {
//                                    Log.e(TAG, "Connection lost.  Cause: Network Lost.");
//                                } else if (i
//                                        == GoogleApiClient.ConnectionCallbacks.CAUSE_SERVICE_DISCONNECTED) {
//                                    Log.e(TAG,
//                                            "Connection lost.  Reason: Service Disconnected");
//                                }
//                            }
//                        }
//                )
//                .addOnConnectionFailedListener(
//                        new GoogleApiClient.OnConnectionFailedListener() {
//                            // Called whenever the API client fails to connect.
//                            @Override
//                            public void onConnectionFailed(ConnectionResult result) {
//                                mTryingToConnect = false;
//                                Log.e(TAG,
//                                        "Connection failed." + result.toString());
//                                if (preferences.getBoolean("destroy", false)) {
//                                    Log.e(TAG,
//                                            "if");
//                                } else {
//                                    Log.e(TAG,
//                                            "else");
//                                    if (result.hasResolution()) {
//                                        try {
//                                            // !!!
//                                            result.startResolutionForResult(CureFull.getInstanse().getActivityIsntanse(), 1);
//                                        } catch (IntentSender.SendIntentException e) {
//
////                                        mClient.connect();
////                                        buildFitnessClient();
//                                        }
//                                    }
//                                    new Handler().postDelayed(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Log.e(TAG,
//                                                    "Handler");
//                                            if (mClient.isConnected()) {
//
//                                            } else {
//                                                mClient.connect();
//                                                findFitnessDataSources();
//                                            }
//                                        }
//                                    }, 9000);
//                                }
//
//
//                            }
//                        }
//                )
//                .build();
//    }
//
//
//    private void findFitnessDataSources() {
//        // [START find_data_sources]
//        // Note: Fitness.SensorsApi.findDataSources() requires the ACCESS_FINE_LOCATION permission.
//        Fitness.SensorsApi.findDataSources(mClient, new DataSourcesRequest.Builder()
//                // At least one datatype must be specified.
//                .setDataTypes(
//                        DataType.TYPE_STEP_COUNT_CUMULATIVE,
//                        DataType.TYPE_DISTANCE_CUMULATIVE,
//                        DataType.TYPE_CYCLING_WHEEL_RPM,
//                        DataType.AGGREGATE_STEP_COUNT_DELTA,
//                        DataType.TYPE_DISTANCE_DELTA
//                )
//                .setDataSourceTypes(DataSource.TYPE_RAW, DataSource.TYPE_DERIVED)
//                .build())
//                .setResultCallback(new ResultCallback<DataSourcesResult>() {
//                    @Override
//                    public void onResult(DataSourcesResult dataSourcesResult) {
//                        Log.e(TAG, "Result: " + dataSourcesResult.getStatus().toString());
//                        for (DataSource dataSource : dataSourcesResult.getDataSources()) {
//                            Device device = dataSource.getDevice();
//                            String fields = dataSource.getDataType().getFields().toString();
//                            Log.e("dfd", "fdfd" + device.getManufacturer() + " " + device.getModel() + " [" + dataSource.getDataType().getName() + " " + fields + "]");
//                            final DataType dataType = dataSource.getDataType();
//                            Log.e("dataType", " " + dataType.toString());
//
//                            if (dataType.equals(DataType.TYPE_STEP_COUNT_CUMULATIVE) ||
//                                    dataType.equals(DataType.TYPE_DISTANCE_CUMULATIVE)) {
//
//                                Fitness.SensorsApi.add(mClient,
//                                        new SensorRequest.Builder()
//                                                .setDataSource(dataSource)
//                                                .setDataType(dataSource.getDataType())
//                                                .setSamplingRate(10, TimeUnit.SECONDS)
//                                                .build(),
//                                        new OnDataPointListener() {
//                                            @Override
//                                            public void onDataPoint(DataPoint dataPoint) {
//                                                String msg = "onDataPoint: ";
//                                                for (final Field field : dataPoint.getDataType().getFields()) {
//                                                    Value value = dataPoint.getValue(field);
//                                                    msg += "onDataPoint: " + field.getName() + "=" + value.asInt() + ", ";
//                                                    final int val = value.asInt();
//                                                    Log.e("hi", "Filed:- " + msg);
//
//                                                    new Handler(getMainLooper()).post(new Runnable() {
//                                                        @Override
//                                                        public void run() {
//                                                            Toast.makeText(FitGoogleService.this, " value " + val, Toast.LENGTH_SHORT).show();
//                                                            Log.e("Steps ", ":- " + val);
//                                                        }
//                                                    });
////                                                    new Thread(new Runnable() {
////                                                        @Override
////                                                        public void run() {
////                                                            Log.e("Steps ", ":- " + val);
////                                                        }
////                                                    });
////                                                 new MainActivity().runOnUiThread(new Runnable() {
////                                                        @Override
////                                                        public void run() {
//////                                                            txt_ehr_upload.setText("" + field.getName());
////                                                            Log.e("Steps ", ":- " + val);
//////                                                            txt_health_note.setText("" + getDistanceRun(val) + " KM");
////                                                        }
////                                                    });
//                                                    Log.e("all ", ":- " + msg);
//                                                }
//
//
//                                            }
//                                        })
//                                        .setResultCallback(new ResultCallback<Status>() {
//                                            @Override
//                                            public void onResult(Status status) {
//                                                if (status.isSuccess()) {
//                                                    Log.i(TAG, "Listener registered!");
//                                                } else {
//                                                    Log.i(TAG, "Listener not registered.");
//                                                }
//                                            }
//                                        });
//                            }
//                        }
//                    }
//                });
//        // [END find_data_sources]
//    }
//
//
//}
