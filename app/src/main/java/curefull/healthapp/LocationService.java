package curefull.healthapp;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.awareness.Awareness;
import com.google.android.gms.awareness.snapshot.WeatherResult;
import com.google.android.gms.awareness.state.Weather;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import java.util.List;
import java.util.Locale;

import utils.AppPreference;
import utils.CheckNetworkState;
import utils.NotificationUtils;

/**
 * Created by Sushant Hatcheryhub on 12-10-2016.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    public static final String BROADCAST_ACTION = "HelloWorld";
    private static final int TWO_MINUTES = 1000 * 60 * 2;
    public LocationManager locationManager;
    public MyLocationListener listener;
    public Location previousBestLocation = null;
    Intent intent;
    int counter = 0;
    private GoogleApiClient mClient;
    SharedPreferences preferences;
    private int numSteps;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("start", "start");
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        mClient = new GoogleApiClient.Builder(LocationService.this)
                .addConnectionCallbacks(LocationService.this)
                .addOnConnectionFailedListener(LocationService.this).addApi(Awareness.API).build();
        mClient.connect();
        intent = new Intent(BROADCAST_ACTION);
    }


    @Override
    public void onStart(Intent intent, int startId) {
        Log.e("start1", "start1");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        listener = new MyLocationListener();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 4000, 0, listener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    protected boolean isBetterLocation(Location location, Location currentBestLocation) {
        if (currentBestLocation == null) {
            // A new location is always better than no location
            return true;
        }

        // Check whether the new location fix is newer or older
        long timeDelta = location.getTime() - currentBestLocation.getTime();
        boolean isSignificantlyNewer = timeDelta > TWO_MINUTES;
        boolean isSignificantlyOlder = timeDelta < -TWO_MINUTES;
        boolean isNewer = timeDelta > 0;

        // If it's been more than two minutes since the current location, use the new location
        // because the user has likely moved
        if (isSignificantlyNewer) {
            return true;
            // If the new location is more than two minutes older, it must be worse
        } else if (isSignificantlyOlder) {
            return false;
        }

        // Check whether the new location fix is more or less accurate
        int accuracyDelta = (int) (location.getAccuracy() - currentBestLocation.getAccuracy());
        boolean isLessAccurate = accuracyDelta > 0;
        boolean isMoreAccurate = accuracyDelta < 0;
        boolean isSignificantlyLessAccurate = accuracyDelta > 200;

        // Check if the old and new location are from the same provider
        boolean isFromSameProvider = isSameProvider(location.getProvider(),
                currentBestLocation.getProvider());

        // Determine location quality using a combination of timeliness and accuracy
        if (isMoreAccurate) {
            return true;
        } else if (isNewer && !isLessAccurate) {
            return true;
        } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
            return true;
        }
        return false;
    }


    /**
     * Checks whether two providers are the same
     */
    private boolean isSameProvider(String provider1, String provider2) {
        if (provider1 == null) {
            return provider2 == null;
        }
        return provider1.equals(provider2);
    }


    @Override
    public void onDestroy() {
        // handler.removeCallbacks(sendUpdatesToUI);
        super.onDestroy();
        Log.e("STOP_SERVICE", "DONE");
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(listener);
    }

    public static Thread performOnBackgroundThread(final Runnable runnable) {
        final Thread t = new Thread() {
            @Override
            public void run() {
                try {
                    runnable.run();
                } finally {

                }
            }
        };
        t.start();
        return t;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e("Service", "Connected!!!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e("Service", "onConnectionSuspended!!!");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("Service", "onConnectionFailed!!!");
    }

    public class MyLocationListener implements LocationListener {

        public void onLocationChanged(final Location loc) {

            numSteps++;
            Toast.makeText(LocationService.this, " " + numSteps, Toast.LENGTH_SHORT).show();
            Log.e("***********", "Location changed" + loc.getLatitude() + " " + loc.getLongitude());

            String lat = String.valueOf(loc.getLatitude());
            String log = String.valueOf(loc.getLongitude());

            Log.e("location", "" + preferences.getString("Latitude", ""));

            if (!preferences.getString("Latitude", "").equalsIgnoreCase("")) {
                Location locationA = new Location("point A");
                locationA.setLatitude(Double.parseDouble(preferences.getString("Latitude", "")));
                locationA.setLongitude(Double.parseDouble(preferences.getString("Longitude", "")));
                Location locationB = new Location("point B");
                locationB.setLatitude(loc.getLatitude());
                locationB.setLongitude(loc.getLongitude());
                float distance = locationB.distanceTo(locationA);

                preferences.edit().putString("Latitude", lat).commit();
                preferences.edit().putString("Longitude", log).commit();
                Log.e("distance", " " +distance);
//                if (Math.round(distance) == 20) {
//                    if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
//                        Geocoder gcd = new Geocoder(LocationService.this, Locale.getDefault());
//                        try {
//                            List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//                            if (addresses.size() > 0) {
//                                preferences.edit().putString("Latitude", lat).commit();
//                                preferences.edit().putString("Longitude", log).commit();
//                                Log.e("location ", ":- " + addresses.get(0).getLocality());
//                                getWeather("" + addresses.get(0).getLocality());
////                    AppPreference.getInstance().setNearLocation("" + addresses.get(0).getLocality());
////                    CureFull.getInstanse().getActivityIsntanse().buildFitnessClient();
//                            }
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }

            } else {

                preferences.edit().putString("Latitude", lat).commit();
                preferences.edit().putString("Longitude", log).commit();
//                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
//                    Geocoder gcd = new Geocoder(LocationService.this, Locale.getDefault());
//                    try {
//                        List<Address> addresses = gcd.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);
//                        if (addresses.size() > 0) {
//
//                            Log.e("location ", ":- " + addresses.get(0).getLocality());
//                            getWeather("" + addresses.get(0).getLocality());
//                        }
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
            }


//            if (isBetterLocation(loc, previousBestLocation)) {
//                loc.getLatitude();
//                loc.getLongitude();
//                intent.putExtra("Latitude", loc.getLatitude());
//                intent.putExtra("Longitude", loc.getLongitude());
//                intent.putExtra("Provider", loc.getProvider());
//                sendBroadcast(intent);
//            }
        }

        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }


        public void onProviderEnabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }


        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

    }


    public void getWeather(final String loca) {
        Log.e("sds 1", "sff: ");
        Log.e("sds", "sff: " + mClient);
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
                            String locArea = preferences.getString("locArea", "");
                            if (!locArea.equalsIgnoreCase(loca)) {
                                preferences.edit().putString("locArea", loca).commit();
                                NotificationUtils notificationUtils = new NotificationUtils(LocationService.this);
                                notificationUtils.notificationWhetherReport(loca, "" + stringBuilder.toString(), " Humidity :" + humidity, "" + temperature);
                            } else if (locArea.equalsIgnoreCase("")) {
                                preferences.edit().putString("locArea", loca).commit();
                                NotificationUtils notificationUtils = new NotificationUtils(LocationService.this);
                                notificationUtils.notificationWhetherReport(loca, "" + stringBuilder.toString(), " Humidity :" + humidity, "" + temperature);

                            }


                        } else {
                        }
                    }
                });
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


}