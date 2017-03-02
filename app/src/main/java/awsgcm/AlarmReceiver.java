package awsgcm;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import asyns.JsonUtilsObject;
import curefull.healthapp.CureFull;
import operations.DbOperations;
import stepcounter.MessengerService;
import utils.CheckNetworkState;
import utils.MyConstants;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * Created by Curefull on 02-02-2017.
 */

public class AlarmReceiver extends BroadcastReceiver {

    SharedPreferences preferences;
    RequestQueue requestQueue;

    @Override
    public void onReceive(Context context, Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
        String action = intent.getAction();
        if (action.equalsIgnoreCase("steps")) {
            String stepsCount = intent.getExtras().getString("stepsCount");
            String caloriesBurnt = intent.getExtras().getString("caloriesBurnt");
            String waterin = intent.getExtras().getString("waterin");
            String newTime = intent.getExtras().getString("newTime");
            if (CheckNetworkState.isNetworkAvailable(context)) {
                jsonUploadTargetSteps(context, stepsCount, caloriesBurnt, waterin);
            } else {
                if (newTime.equalsIgnoreCase("11:59 pm")) {
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
