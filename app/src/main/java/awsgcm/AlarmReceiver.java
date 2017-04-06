package awsgcm;

import android.app.ActivityManager;
import android.app.NotificationManager;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.content.WakefulBroadcastReceiver;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import asyns.JsonUtilsObject;
import operations.DbOperations;
import stepcounter.MessengerService;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.NotificationUtils;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Curefull on 02-02-2017.
 */

public class AlarmReceiver extends WakefulBroadcastReceiver {

    SharedPreferences preferences;
    RequestQueue requestQueue;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String action = intent.getAction();

//        NotificationUtils notificationUtils = new NotificationUtils(context);
//        notificationUtils.allGetNotfication("sdd", "ds", "123", "sdd");

        if (action.equalsIgnoreCase("steps")) {
            String stepsCount = "" + preferences.getInt("stepsIn", 0);
            String caloriesBurnt = "" + preferences.getString("CaloriesCount", "");
            String waterin = preferences.getString("waterTake", "");
//            String newTime = intent.getExtras().getString("newTime");
            preferences.edit().putInt("stepsIn", 0).commit();
            preferences.edit().putString("waterTake", "0").commit();
            preferences.edit().putString("CaloriesCount", "0").commit();
            preferences.edit().putInt("percentage", 0).commit();
            preferences.edit().putInt("firstLogin", 0).commit();
            if (preferences.getBoolean("resetStepReboot", true)) {
            } else {
                preferences.edit().putBoolean("resetStepReboot", true).commit();
            }
            preferences.edit().putBoolean("resetStepFirstTime", true).commit();
            if (CheckNetworkState.isNetworkAvailable(context)) {
                jsonUploadTargetSteps(context, stepsCount, caloriesBurnt, waterin);
            } else {
                ContentValues cv = new ContentValues();
                cv.put("steps_count", stepsCount);
                cv.put("runing", 0);
                cv.put("cycling", 0);
                cv.put("calories", "" + caloriesBurnt);
                cv.put("dateTime", "" + getTodayDateTime());
                cv.put("waterTake", waterin);
                cv.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                DbOperations operations = new DbOperations();
                operations.insertStepsCounts(context, cv);
            }
        } else if (action.equalsIgnoreCase("stepsService")) {
            if (isMyServiceRunning(context, MessengerService.class)) {
            } else {
                Intent background = new Intent(context, MessengerService.class);
                context.startService(background);
            }
        } else {
            String id = intent.getExtras().getString("perDayDosageDetailsId");
            String type = intent.getExtras().getString("type");
            if (type.equalsIgnoreCase("LAB_TEST_REMINDER")) {
                jsonUploadLabTest(context, id, action);
            } else if (type.equalsIgnoreCase("DOCTOR_FOLLOWUP_REMINDER")) {
                jsonUploadDoctorVist(context, id, action);
            } else {
                jsonUploadMedicine(context, id, action);
            }
        }


    }

    public static boolean isMyServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (services != null) {
            for (int i = 0; i < services.size(); i++) {
                if ((serviceClass.getName()).equals(services.get(i).service.getClassName()) && services.get(i).pid != 0) {
                    return true;
                }
            }
        }
        return false;
    }

    public void jsonUploadMedicine(final Context context, final String id, String action) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }

        JSONObject data = JsonUtilsObject.toNotificationMEdincie(id, action);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_NOTIFICATION_MEDICINE, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(Integer.parseInt(id));
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                headers.put("user_id", preferences.getString("user_id", ""));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }


    public void jsonUploadDoctorVist(final Context context, final String id, String action) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        JSONObject data = JsonUtilsObject.toNotificationDoctor(id, action);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_NOTIFICATION_DOCTOR, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(Integer.parseInt(id));
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                headers.put("user_id", preferences.getString("user_id", ""));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }


    public void jsonUploadLabTest(final Context context, final String id, String action) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        JSONObject data = JsonUtilsObject.toNotificationLabTest(id, action);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.GET_NOTIFICATION_LAB_TEST, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            NotificationManager manager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
                            manager.cancel(Integer.parseInt(id));
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
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                headers.put("user_id", preferences.getString("user_id", ""));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);

    }


    public void jsonUploadTargetSteps(Context context, String stepsCount, String caloriesBurnts, String waterin) {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context);
        }
        String steps = stepsCount;
        String running = "0";
        String cycling = "0";
        String waterintake = waterin;
        String caloriesBurnt = caloriesBurnts;
        String dateTime = getTodayDateTime();
        String[] dateParts = dateTime.split(" ");
        String date = dateParts[0];
        String timeReal = dateParts[1];

        JSONObject data = JsonUtilsObject.toSaveHealthAppDetails(steps, running, cycling, waterintake, caloriesBurnt, date, timeReal);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_HELTHAPP_DETALS, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
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
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                headers.put("user_id", preferences.getString("user_id", ""));
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }

    public static String getTodayDateTime() {
        String formattedDate = null;
        try {
            SimpleDateFormat initialformatter = new SimpleDateFormat(
                    "yyyy-MM-dd HH:mm", Locale.getDefault());
            java.util.Date today = Calendar.getInstance().getTime();
            formattedDate = initialformatter.format(today);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return formattedDate;
    }


}
