package curefull.healthapp;

import android.app.IntentService;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.util.Log;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import asyns.JsonUtilsObject;
import item.property.Doctor_Visit_Reminder_DoctorListView;
import item.property.Doctor_Visit_Reminder_SelfListView;
import item.property.LabReportImageListView;
import item.property.LabReportListView;
import item.property.Lab_Test_Reminder_DoctorListView;
import item.property.Lab_Test_Reminder_SelfListView;
import item.property.MedicineReminderItem;
import item.property.PrescriptionImageListView;
import item.property.PrescriptionListView;
import item.property.Reminder_DoctorListView;
import item.property.Reminder_SelfListView;
import operations.DbOperations;

import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.NotificationUtils;
import utils.Utils;


/**
 * Created by Curefull on 16-04-2017.
 */

public class SchedulingService extends IntentService {
    private int valueUpload = 0;
    SharedPreferences preferences;

    public SchedulingService() {
        super("SchedulingService");
    }


    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(Intent intent) {
        preferences = PreferenceManager.getDefaultSharedPreferences(this);
        if (CheckNetworkState.isNetworkAvailable(this)) {
            DbOperations db = new DbOperations();
            //for prescription
            final List<PrescriptionListView> prescriptionData = db.getPrescriptionData(this, "1");
            for (int i = 0; i < prescriptionData.size(); i++) {

                JSONObject data = JsonUtilsObject.toSaveUploadPrescriptionMetadata(prescriptionData.get(i).getPrescriptionDate(), prescriptionData.get(i).getDoctorName(), prescriptionData.get(i).getDiseaseName());

                final int finalI = i;
                final int finalI1 = i;
                final int finalI2 = i;
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_UPLOAD_PRESCRIPTION_METADATA, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int responseStatus = 0;
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response.toString());
                                    responseStatus = json.getInt("responseStatus");
                                    if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                        JSONObject json1 = new JSONObject(json.getString("payload"));
                                        String prescriptionId = json1.getString("prescriptionId");
                                        String cfUuhidId = json1.getString("cfUuhidId");
                                        if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                            getSaveUploadPrescriptionMetadata(prescriptionId, cfUuhidId, prescriptionData.get(finalI).getPrescriptionImageFollowUpListViews().get(0).getPrescriptionImageListViews(), prescriptionData.get(finalI).getCommonID(), prescriptionData.get(finalI).getIsUploaded());
                                        }
                                    } else {

                                        try {
                                            JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                            JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //this.showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);

                    }
                }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("a_t", preferences.getString("a_t", ""));
                        headers.put("r_t", preferences.getString("r_t", ""));
                        headers.put("user_name", preferences.getString("user_name", ""));
                        headers.put("email_id", preferences.getString("email_id", ""));
                        headers.put("cf_uuhid", prescriptionData.get(finalI).getCfUuhid());
                        headers.put("user_id", preferences.getString("user_id", ""));

                        return headers;
                    }
                };
                CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
            }


//for lab report
            final List<LabReportListView> labData = db.getLabData(this, "1");
            for (int ilab = 0; ilab < labData.size(); ilab++) {

                JSONObject data = JsonUtilsObject.toSaveUploadLabReposrtMetadata(labData.get(ilab).getReportDate(), labData.get(ilab).getDoctorName(), labData.get(ilab).getTestName());

                final int finalI = ilab;
                final int finalI1 = ilab;
                final int finalI2 = ilab;
                final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_UPLOAD_LAB_REPORTS_METADATA, data,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                int responseStatus = 0;
                                JSONObject json = null;
                                try {
                                    json = new JSONObject(response.toString());
                                    responseStatus = json.getInt("responseStatus");

                                    if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                        JSONObject json1 = new JSONObject(json.getString("payload"));
                                        String prescriptionId = json1.getString("labReportId");
                                        String cfUuhidId = json1.getString("cfUuhidId");
                                        if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                            getSaveUploadLabMetadata(prescriptionId, cfUuhidId, labData.get(finalI).getLabReportImageListViews(), labData.get(finalI).getCommonID(), labData.get(finalI).getIsUploaded());

                                        }
                                    } else {

                                        try {
                                            JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                            JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
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
                        headers.put("cf_uuhid", labData.get(finalI).getCfUuhid());
                        headers.put("user_id", preferences.getString("user_id", ""));


                        return headers;
                    }
                };
                CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);


            }
//for lab reminder
            lab_notification_sync_self();
            lab_notification_sync_digitization();
            ///doctor reminder
            doctor_notification_sync_self();
            doctor_notification_sync_digitization();

            medicine_notification_sync_self();
            medicine_notification_sync_digitization();
            birthdaynotificaion();
        } else {
            //for sqlite notification
            lab_notification_from_sqlite();
            lab_notification_from_digitization();

            doctor_notification_from_sqlite();
            doctor_notification_from_digitization();

            medicine_notification_from_sqlite();
            medicine_notification_from_digitization();

            birthdaynotificaion();

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void birthdaynotificaion() {
        String[] today_date = Utils.getTodayDate().split("-");
        String year = today_date[0];
        String month = today_date[1];
        String day = today_date[2];
        String day_month_current = day + "-" + month;

        String getDOB_from_preference[] = preferences.getString("Age", "0").split("-");//2006-12-11
        String month_dob = getDOB_from_preference[1];
        String day_dob = getDOB_from_preference[2];
        String day_month_dob = day_dob + "-" + month_dob;

        if (day_month_current.equalsIgnoreCase(day_month_dob)) {

            if (preferences.getString("yearr", year).equalsIgnoreCase(year)) {

            } else {
                preferences.edit().putString("chk_Birthday_notification", "0").commit();
            }

            if (preferences.getString("chk_Birthday_notification", "0").equalsIgnoreCase("0")) {
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.birthdayNotification("Hii buddy, Today is your birthday");
                preferences.edit().putString("chk_Birthday_notification", "1").commit();
                preferences.edit().putString("yearr", year).commit();
            }
        }

    }

    private void medicine_notification_sync_digitization() {
        final List<Reminder_DoctorListView> medicinereminder = DbOperations.getMedicineReminderbyDigitization(this, "1");

        JSONObject data = JsonUtilsObject.setRemMedAddDigitization(medicinereminder, "0");//medicinereminder.get(i1).getYear() + "-" + monthh + "-" + dayy, String.valueOf(medicinereminder.get(i1).getNoOfDays()), String.valueOf(medicinereminder.get(i1).getNoOfDosage()), medicinereminder.get(i1).getNoOfDaysInWeek(), listCurrent, time, medicinereminder.get(i1).getInterval(), medicinereminder.get(i1).getCfuuhId(),medicinereminder.get(i1).getStatus(),medicinereminder.get(i1).getMedicineReminderId(),medicinereminder.get(i1).getReminderMedicnceDoagePers(),
        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_MEDICINE_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            //CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int i1 = 0; i1 < medicinereminder.size(); i1++) {
                                        DbOperations.clearMedicineReminderbyself(medicinereminder.get(i1).getMedicineReminderId(), "1", "digitization");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        }
    }

    private void medicine_notification_sync_self() {
        ////medicine reminder upload
        final List<Reminder_SelfListView> medicinereminder = DbOperations.getMedicineReminderbySelf(this, "1");

        JSONObject data = JsonUtilsObject.setRemMedAddLocal(medicinereminder, "1");//medicinereminder.get(i1).getYear() + "-" + monthh + "-" + dayy, String.valueOf(medicinereminder.get(i1).getNoOfDays()), String.valueOf(medicinereminder.get(i1).getNoOfDosage()), medicinereminder.get(i1).getNoOfDaysInWeek(), listCurrent, time, medicinereminder.get(i1).getInterval(), medicinereminder.get(i1).getCfuuhId(),medicinereminder.get(i1).getStatus(),medicinereminder.get(i1).getMedicineReminderId(),medicinereminder.get(i1).getReminderMedicnceDoagePers(),
        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_MEDICINE_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            //CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int i1 = 0; i1 < medicinereminder.size(); i1++) {
                                        DbOperations.clearMedicineReminderbyself(medicinereminder.get(i1).getMedicineReminderId(), "1", "self");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    Log.e("erroe", error.toString());
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        }
    }

    private void doctor_notification_sync_self() {
        final List<Doctor_Visit_Reminder_SelfListView> doctorreminder = DbOperations.getDoctorReminderbySelf(this, "1");

        JSONObject data = JsonUtilsObject.toSetDoctorVisitReminderLocal("1", doctorreminder);
//doctorreminder.get(ilrs).getDoctorName().toString().trim(), doctorreminder.get(ilrs).getRemMedicineName().toString().trim(), doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), isNewReminder, doctorreminder.get(ilrs).getCfuuhId(), doctorreminder.get(ilrs).getStatus(),
        // final int finalIlrs = ilrs;
        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_DOCTOR_VISIT_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // this.showProgressBar(false);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
                                        DbOperations.clearDoctorReminderbyself(doctorreminder.get(ilrs).getDoctorFollowupReminderId(), "1", "self");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    // this.showProgressBar(false);
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);

        }
    }

    private void lab_notification_sync_self() {
        final List<Lab_Test_Reminder_SelfListView> labreminder = DbOperations.getLabReminderbySelf(this, "1");

        JSONObject data = JsonUtilsObject.toSetLabTestReminderfromLocal("1", labreminder);//labreminder.get(ilrs).getDoctorName().toString().trim(), labreminder.get(ilrs).getRemMedicineName().toString().trim(), labreminder.get(ilrs).getLabName().toString().trim(), labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, labreminder.get(ilrs).getLabTestReminderId(), isNewReminder, labreminder.get(ilrs).isAfterMeal(), labreminder.get(ilrs).getCfuuhId(), labreminder.get(ilrs).getStatus(),

        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_LAB_TEST_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // this.showProgressBar(false);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {
                                        DbOperations.clearLabReminderbyself(labreminder.get(ilrs).getLabTestReminderId(), "1", "self");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        }

    }


    private void doctor_notification_sync_digitization() {
        final List<Doctor_Visit_Reminder_DoctorListView> doctorreminder = DbOperations.getDoctorReminderbyDigitization(this, "1");

        JSONObject data = JsonUtilsObject.toSetDoctorVisitReminderLocalDigitization("0", doctorreminder);//doctorreminder.get(ilrs).getDoctorName().toString().trim(), doctorreminder.get(ilrs).getRemMedicineName().toString().trim(), doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), isNewReminder, doctorreminder.get(ilrs).getCfuuhId(), doctorreminder.get(ilrs).getStatus(),
        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_DOCTOR_VISIT_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
                                        DbOperations.clearDoctorReminderbyself(doctorreminder.get(ilrs).getDoctorFollowupReminderId(), "1", "digitization");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    // this.showProgressBar(false);
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        }


    }

    private void lab_notification_sync_digitization() {
        final List<Lab_Test_Reminder_DoctorListView> labreminder = DbOperations.getLabReminderbyDigitiation(this, "1");

        JSONObject data = JsonUtilsObject.toSetLabTestReminderfromDigitization("0", labreminder);//labreminder.get(ilrs).getDoctorName().toString().trim(), labreminder.get(ilrs).getRemMedicineName().toString().trim(), labreminder.get(ilrs).getLabName().toString().trim(), labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, labreminder.get(ilrs).getLabTestReminderId(), isNewReminder, labreminder.get(ilrs).isAfterMeal(), labreminder.get(ilrs).getCfuuhId(), labreminder.get(ilrs).getStatus(),
        if (data.length() > 0) {
            final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.ADD_LAB_TEST_REM_LOCAL_SYNC, data,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {

                            // this.showProgressBar(false);
                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                                if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                    for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {
                                        DbOperations.clearLabReminderbyself(labreminder.get(ilrs).getLabTestReminderId(), "1", "digitization");
                                    }
                                } else {
                                    try {
                                        JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                        JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }


                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) {

                    // CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                    //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));

                    return headers;
                }
            };
            CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
        }


    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void medicine_notification_from_digitization() {
        final List<Reminder_DoctorListView> medicinereminder = DbOperations.getMedicineReminderbyDigitization(this, "1");
        for (int i1 = 0; i1 < medicinereminder.size(); i1++) {
            String hour = "";
            String minute = "";
            String monthh = "";
            String dayy = "";

            if (medicinereminder.get(i1).getMonth() < 10) {

                monthh = "0" + medicinereminder.get(i1).getMonth();
            } else {
                monthh = "" + medicinereminder.get(i1).getMonth();
            }

            if (medicinereminder.get(i1).getDate() < 10) {

                dayy = "0" + medicinereminder.get(i1).getDate();
            } else {
                dayy = "" + medicinereminder.get(i1).getDate();
            }
            String[] alarm_time = medicinereminder.get(i1).getAlarmTime().split(",");

            int sizee = alarm_time.length;

            for (int y = 0; y < sizee; y++) {

                String[] timee = alarm_time[y].split(":");
                String hourr = timee[0];
                String minutee = timee[1];
                if (Integer.parseInt(hourr) < 10) {

                    hour = "0" + Integer.parseInt(hourr);
                } else {
                    hour = "" + Integer.parseInt(hourr);
                }

                if (Integer.parseInt(minutee) < 10) {

                    minute = "0" + Integer.parseInt(minutee);
                } else {
                    minute = "" + Integer.parseInt(minutee);
                }

                String local_db_date_time = medicinereminder.get(i1).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
                String date = Utils.getTodayDate();
                String time = Utils.getTodayTime();
                String date_time = date + " " + time;
                try {
                    if (local_db_date_time.equals(date_time)) {
                        if (medicinereminder.get(i1).getStatus().equalsIgnoreCase("activate") || medicinereminder.get(i1).getStatus().equalsIgnoreCase("pending")) {
                            String txt_detail = preferences.getString("user_name", "") + ", please take your dose of " + medicinereminder.get(i1).getRemMedicineName() + ". Each dose is important.";
                            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                            notificationUtils.allGetNotficationMedicine(preferences.getString("user_name", ""), txt_detail, medicinereminder.get(i1).getMedicineReminderId(), "MEDICINE_FOLLOWUP_REMINDER_CUREFULL", medicinereminder.get(i1).getCurrentDate(), hour + ":" + minute);//medicinereminder.get(i1).getReminderMedicnceDoagePers().get(z).getDosagePerDayDetailsId()
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }


        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void medicine_notification_from_sqlite() {
        final List<Reminder_SelfListView> medicinereminder = DbOperations.getMedicineReminderbySelf(this, "1");
        for (int i1 = 0; i1 < medicinereminder.size(); i1++) {
            String hour = "";
            String minute = "";
            String monthh = "";
            String dayy = "";

            if (medicinereminder.get(i1).getMonth() < 10) {

                monthh = "0" + medicinereminder.get(i1).getMonth();
            } else {
                monthh = "" + medicinereminder.get(i1).getMonth();
            }

            if (medicinereminder.get(i1).getDate() < 10) {

                dayy = "0" + medicinereminder.get(i1).getDate();
            } else {
                dayy = "" + medicinereminder.get(i1).getDate();
            }
            String[] alarm_time = medicinereminder.get(i1).getAlarmTime().split(",");

            int sizee = alarm_time.length;

            //  for (int z = 0; z < medicinereminder.get(i1).getReminderMedicnceDoagePers().size(); z++) {
            //    int sizee = medicinereminder.get(i1).getReminderMedicnceDoagePers().get(z).getReminderMedicnceTimes().size();
            for (int y = 0; y < sizee; y++) {

                String[] timee = alarm_time[y].split(":");
                String hourr = timee[0];
                String minutee = timee[1];
                if (Integer.parseInt(hourr) < 10) {

                    hour = "0" + Integer.parseInt(hourr);
                } else {
                    hour = "" + Integer.parseInt(hourr);
                }

                if (Integer.parseInt(minutee) < 10) {

                    minute = "0" + Integer.parseInt(minutee);
                } else {
                    minute = "" + Integer.parseInt(minutee);
                }

                String local_db_date_time = medicinereminder.get(i1).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
                String date = Utils.getTodayDate();
                String time = Utils.getTodayTime();
                String date_time = date + " " + time;
                try {
                    if (local_db_date_time.equals(date_time)) {
                        if (medicinereminder.get(i1).getStatus().equalsIgnoreCase("activate") || medicinereminder.get(i1).getStatus().equalsIgnoreCase("pending")) {
                            String txt_detail = preferences.getString("user_name", "") + ", please take your dose of " + medicinereminder.get(i1).getRemMedicineName() + ". Each dose is important.";
                            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                            notificationUtils.allGetNotficationMedicine(preferences.getString("user_name", ""), txt_detail, medicinereminder.get(i1).getMedicineReminderId(), "MEDICINE_FOLLOWUP_REMINDER", medicinereminder.get(i1).getCurrentDate(), hour + ":" + minute);//medicinereminder.get(i1).getReminderMedicnceDoagePers().get(z).getDosagePerDayDetailsId()
                        }
                    }
                } catch (Exception e) {
                    e.getMessage();
                }

            }

            //  }

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void doctor_notification_from_digitization() {
        final List<Doctor_Visit_Reminder_DoctorListView> doctorreminder = DbOperations.getDoctorReminderbyDigitization(this, "1");
        for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
            boolean isNewReminder = false;
            String monthh = "";
            String dayy = "";
            String hour = "";
            String minute = "";
            if (doctorreminder.get(ilrs).getMonth() < 10) {

                monthh = "0" + doctorreminder.get(ilrs).getMonth();
            } else {
                monthh = "" + doctorreminder.get(ilrs).getMonth();
            }

            if (doctorreminder.get(ilrs).getDate() < 10) {

                dayy = "0" + doctorreminder.get(ilrs).getDate();
            } else {
                dayy = "" + doctorreminder.get(ilrs).getDate();
            }

            if (doctorreminder.get(ilrs).getHour() < 10) {

                hour = "0" + doctorreminder.get(ilrs).getHour();
            } else {
                hour = "" + doctorreminder.get(ilrs).getHour();
            }

            if (doctorreminder.get(ilrs).getMintue() < 10) {

                minute = "0" + doctorreminder.get(ilrs).getMintue();
            } else {
                minute = "" + doctorreminder.get(ilrs).getMintue();
            }

            String local_db_date_time = doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
            //Log.e("localdatedoctor",local_db_date_time);
            String date = Utils.getTodayDate();
            String time = Utils.getTodayTime();

            String date_time = date + " " + time;
            try {
                if (local_db_date_time.equals(date_time)) {
                    if (doctorreminder.get(ilrs).getStatus().equalsIgnoreCase("activate") || doctorreminder.get(ilrs).getStatus().equalsIgnoreCase("pending")) {
                        String txt_detail = preferences.getString("user_name", "") + " ,you have a doctor visit on " + doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + ". Plan your day accordingly";
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.allGetNotfication(preferences.getString("user_name", ""), txt_detail, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), "DOCTOR_FOLLOWUP_REMINDER_DOCTOR");
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void doctor_notification_from_sqlite() {
        //doctor reminder notification from local db;
        final List<Doctor_Visit_Reminder_SelfListView> doctorreminder = DbOperations.getDoctorReminderbySelf(this, "1");
        for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
            boolean isNewReminder = false;
            String monthh = "";
            String dayy = "";
            String hour = "";
            String minute = "";
            if (doctorreminder.get(ilrs).getMonth() < 10) {

                monthh = "0" + doctorreminder.get(ilrs).getMonth();
            } else {
                monthh = "" + doctorreminder.get(ilrs).getMonth();
            }

            if (doctorreminder.get(ilrs).getDate() < 10) {

                dayy = "0" + doctorreminder.get(ilrs).getDate();
            } else {
                dayy = "" + doctorreminder.get(ilrs).getDate();
            }

            if (doctorreminder.get(ilrs).getHour() < 10) {

                hour = "0" + doctorreminder.get(ilrs).getHour();
            } else {
                hour = "" + doctorreminder.get(ilrs).getHour();
            }

            if (doctorreminder.get(ilrs).getMintue() < 10) {

                minute = "0" + doctorreminder.get(ilrs).getMintue();
            } else {
                minute = "" + doctorreminder.get(ilrs).getMintue();
            }

            String local_db_date_time = doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
            //Log.e("localdatedoctor",local_db_date_time);
            String date = Utils.getTodayDate();
            String time = Utils.getTodayTime();

            String date_time = date + " " + time;
            try {
                if (local_db_date_time.equals(date_time)) {
                    if (doctorreminder.get(ilrs).getStatus().equalsIgnoreCase("activate") || doctorreminder.get(ilrs).getStatus().equalsIgnoreCase("pending")) {
                        String txt_detail = preferences.getString("user_name", "") + " ,you have a doctor visit on " + doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + ". Plan your day accordingly";
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.allGetNotfication(preferences.getString("user_name", ""), txt_detail, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), "DOCTOR_FOLLOWUP_REMINDER");
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    //only generate notification in time
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void lab_notification_from_digitization() {
        final List<Lab_Test_Reminder_DoctorListView> labreminder = DbOperations.getLabReminderbyDigitiation(this, "1");

        for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {

            String monthh = "";
            String dayy = "";
            String hour = "";
            String minute = "";

            if (labreminder.get(ilrs).getMonth() < 10) {
                monthh = "0" + labreminder.get(ilrs).getMonth();
            } else {
                monthh = "" + labreminder.get(ilrs).getMonth();
            }
            if (labreminder.get(ilrs).getDate() < 10) {
                dayy = "0" + labreminder.get(ilrs).getDate();
            } else {
                dayy = "" + labreminder.get(ilrs).getDate();
            }
            if (labreminder.get(ilrs).getHour() < 10) {
                hour = "0" + labreminder.get(ilrs).getHour();
            } else {
                hour = "" + labreminder.get(ilrs).getHour();
            }

            if (labreminder.get(ilrs).getMintue() < 10) {

                minute = "0" + labreminder.get(ilrs).getMintue();
            } else {
                minute = "" + labreminder.get(ilrs).getMintue();
            }

            String local_db_date_time = labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
            Log.e("localdateLAB", local_db_date_time);
            String date = Utils.getTodayDate();
            String time = Utils.getTodayTime();

            String date_time = date + " " + time;
            Log.e("dateLAB", date_time);
            try {
                if (local_db_date_time.equals(date_time)) {
                    if (labreminder.get(ilrs).getStatus().equalsIgnoreCase("activate") || labreminder.get(ilrs).getStatus().equalsIgnoreCase("pending")) {
                        String txt_detail = preferences.getString("user_name", "") + ", your lab test for " + labreminder.get(ilrs).getLabName() + " is due on " + labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + ". Do Visit. Stay Healthy.";
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.allGetNotfication(preferences.getString("user_name", ""), txt_detail, labreminder.get(ilrs).getLabTestReminderId(), "LAB_TEST_REMINDER_DOCTOR");
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void lab_notification_from_sqlite() {
        //lab reminder notification from local db;
        final List<Lab_Test_Reminder_SelfListView> labreminder = DbOperations.getLabReminderbySelf(this, "1");

        for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {

            String monthh = "";
            String dayy = "";
            String hour = "";
            String minute = "";

            if (labreminder.get(ilrs).getMonth() < 10) {
                monthh = "0" + labreminder.get(ilrs).getMonth();
            } else {
                monthh = "" + labreminder.get(ilrs).getMonth();
            }
            if (labreminder.get(ilrs).getDate() < 10) {
                dayy = "0" + labreminder.get(ilrs).getDate();
            } else {
                dayy = "" + labreminder.get(ilrs).getDate();
            }
            if (labreminder.get(ilrs).getHour() < 10) {
                hour = "0" + labreminder.get(ilrs).getHour();
            } else {
                hour = "" + labreminder.get(ilrs).getHour();
            }

            if (labreminder.get(ilrs).getMintue() < 10) {

                minute = "0" + labreminder.get(ilrs).getMintue();
            } else {
                minute = "" + labreminder.get(ilrs).getMintue();
            }

            String local_db_date_time = labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + " " + hour + ":" + minute;
            Log.e("localdateLAB", local_db_date_time);
            String date = Utils.getTodayDate();
            String time = Utils.getTodayTime();

            String date_time = date + " " + time;
            Log.e("dateLAB", date_time);
            try {
                if (local_db_date_time.equals(date_time)) {
                    if (labreminder.get(ilrs).getStatus().equalsIgnoreCase("activate") || labreminder.get(ilrs).getStatus().equalsIgnoreCase("pending")) {
                        String txt_detail = preferences.getString("user_name", "") + ", your lab test for " + labreminder.get(ilrs).getLabName() + " is due on " + labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy + ". Do Visit. Stay Healthy.";
                        NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                        notificationUtils.allGetNotfication(preferences.getString("user_name", ""), txt_detail, labreminder.get(ilrs).getLabTestReminderId(), "LAB_TEST_REMINDER");
                    }
                }
            } catch (Exception e) {
                e.getMessage();
            }
        }
    }


    private void getSaveUploadPrescriptionMetadata(final String prescriptionId, final String cfUuhidId, final ArrayList<PrescriptionImageListView> file, final String commonID, final String isUploaded) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.TEMPORY_CREDENTIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (!json.getString("payload").equals(null)) {
                                    JSONObject json1 = new JSONObject(json.getString("payload"));
                                    String accessKeyID = json1.getString("accessKeyID");
                                    String secretAccessKey = json1.getString("secretAccessKey");
                                    String sessionToken = json1.getString("sessionToken");
                                    if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                        valueUpload = 0;
                                        uploadFile(prescriptionId, cfUuhidId, accessKeyID, secretAccessKey, sessionToken, MyConstants.AWSType.BUCKET_NAME + "/" + preferences.getString("cf_uuhid", "") + MyConstants.AWSType.FOLDER_PRECREPTION_NAME, file, commonID, isUploaded);
                                    }
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

                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();

                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                // headers.put("cf_uuhid", cfuuhidId);
                headers.put("user_id", preferences.getString("user_id", ""));
                headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    public void uploadFile(final String prescriptionId, final String cfUuhidId, String accessKeyID, String secretAccessKey, String sessionToken, String bucketName, final ArrayList<PrescriptionImageListView> imageFile, final String commonID, final String isUploaded) {
        String imageUploadUrl = null;

        BasicSessionCredentials credentials =
                new BasicSessionCredentials(accessKeyID,
                        secretAccessKey,
                        sessionToken);
        final AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        try {

            if (!(s3client.doesBucketExist(bucketName))) {
                // Note that CreateBucketRequest does not specify region. So bucket is
                // created in the region specified in the client.
                s3client.createBucket(new CreateBucketRequest(
                        bucketName));
            }


        } catch (Exception e) {

        }

        for (int i = 0; i < imageFile.size(); i++) {
            TransferUtility transferUtility = new TransferUtility(s3client, this);
            // Request server-side encryption.
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            try {
                File fileUpload = new File(compressImage(imageFile.get(i).getPrescriptionImage()));
                String[] spiltName = new File(imageFile.get(i).getPrescriptionImage()).getName().split("\\.");
                String getName = spiltName[1];
                String name = prescriptionId + "-" + cfUuhidId + "-" + imageFile.get(i).getImageNumber() + "." + getName;
                final TransferObserver observer = transferUtility.upload(
                        bucketName,
                        name,
                        fileUpload, CannedAccessControlList.PublicRead
                );
                final int finalI = i;
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        switch (state.name()) {
                            case "COMPLETED":
                                valueUpload += 1;
//                                Log.e("check", " - " + finalI + " size " + imageFile.size() + "value " + valueUpload);
                                imageFile.get(finalI).setPrescriptionImage("https://s3.ap-south-1.amazonaws.com/" + MyConstants.AWSType.BUCKET_NAME + "/" + preferences.getString("cf_uuhid", "") + "/prescription/" + observer.getKey());
                                if (valueUpload == (imageFile.size())) {
//                                    Log.e("call ", "call ");
                                    if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                        jsonSaveUploadedPrescriptionData(prescriptionId, cfUuhidId, imageFile, commonID, isUploaded);
                                    }
                                }
                                break;

                        }


                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public void jsonSaveUploadedPrescriptionData(String prescriptionId, final String cfuuhidId, final ArrayList<PrescriptionImageListView> file, final String commonID, final String isUploaded) {
        JSONObject data = JsonUtilsObject.toSaveUploadedPrescriptionDataToserver(prescriptionId, cfuuhidId, file);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UPLOADED_PRESCRETION_DATA, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        //this.showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");

                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {

                                DbOperations.clearPrescriptionData(commonID, isUploaded);

                            } else {

                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
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
                headers.put("cf_uuhid", cfuuhidId);
                headers.put("user_id", preferences.getString("user_id", ""));
                //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    private void getSaveUploadLabMetadata(final String prescriptionId, final String cfUuhidId, final ArrayList<LabReportImageListView> file, final String commomID, final String isUploaded) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.TEMPORY_CREDENTIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (!json.getString("payload").equals(null)) {
                                    JSONObject json1 = new JSONObject(json.getString("payload"));
                                    String accessKeyID = json1.getString("accessKeyID");
                                    String secretAccessKey = json1.getString("secretAccessKey");
                                    String sessionToken = json1.getString("sessionToken");
                                    if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                        valueUpload = 0;
                                        uploadFileLab(prescriptionId, cfUuhidId, accessKeyID, secretAccessKey, sessionToken, MyConstants.AWSType.BUCKET_NAME + "/" + preferences.getString("cf_uuhid", "") + MyConstants.AWSType.FOLDER_LAB_REPORT_NAME, file, commomID, isUploaded);
                                    }
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
                        //dialogLoader.hide();
                        error.printStackTrace();
                    }
                }
        ) {

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
        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    public void uploadFileLab(final String prescriptionId, final String cfUuhidId, String accessKeyID, String secretAccessKey, String sessionToken, String bucketName, final ArrayList<LabReportImageListView> imageFile, final String commomID, final String isUploaded) {

        BasicSessionCredentials credentials =
                new BasicSessionCredentials(accessKeyID,
                        secretAccessKey,
                        sessionToken);
        final AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        try {

            if (!(s3client.doesBucketExist(bucketName))) {
                // Note that CreateBucketRequest does not specify region. So bucket is
                // created in the region specified in the client.
                s3client.createBucket(new CreateBucketRequest(
                        bucketName));
            }


        } catch (Exception e) {

        }

        for (int i = 0; i < imageFile.size(); i++) {
            TransferUtility transferUtility = new TransferUtility(s3client, this);
            // Request server-side encryption.
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            try {
                File fileUpload = new File(compressImage(imageFile.get(i).getReportImage()));
                String[] spiltName = new File(imageFile.get(i).getReportImage()).getName().split("\\.");
                String getName = spiltName[1];
                String name = prescriptionId + "-" + cfUuhidId + "-" + imageFile.get(i).getImageNumber() + "." + getName;
                final TransferObserver observer = transferUtility.upload(
                        bucketName,
                        name,
                        fileUpload, CannedAccessControlList.PublicRead
                );
                final int finalI = i;
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        switch (state.name()) {
                            case "COMPLETED":
                                valueUpload += 1;
//
                                imageFile.get(finalI).setReportImage("https://s3.ap-south-1.amazonaws.com/" + MyConstants.AWSType.BUCKET_NAME + "/" + preferences.getString("cf_uuhid", "") + "/labReport/" + observer.getKey());
                                if (valueUpload == (imageFile.size())) {
//                                    Log.e("call ", "call ");
                                    if (CheckNetworkState.isNetworkAvailable(SchedulingService.this)) {
                                        jsonSaveUploadedLabData(prescriptionId, cfUuhidId, imageFile, commomID, isUploaded);
                                    }
                                }
                                break;

                        }


                    }

                    @Override
                    public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                    }

                    @Override
                    public void onError(int id, Exception ex) {
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }


    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = "";
        if (CheckNetworkState.isNetworkAvailable(this)) {
            filename = getFilename();
        } else {
            filename = getFilenameLocal();
        }
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 98, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Prescription");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public String getFilenameLocal() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/PrescriptionLocal");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = this.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    public void jsonSaveUploadedLabData(String prescriptionId, final String cfuuhidId, final ArrayList<LabReportImageListView> file, final String commonID, final String isUploaded) {
        JSONObject data = JsonUtilsObject.toSaveUploadedLabData(prescriptionId, cfuuhidId, file);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UPLOADED_LAB_REPORTS_DATA, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {

                        // this.showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");

                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                DbOperations.clearLabData(commonID, isUploaded);

                            } else {

                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                // this.showProgressBar(false);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("cf_uuhid", cfuuhidId);

                headers.put("a_t", preferences.getString("a_t", ""));
                headers.put("r_t", preferences.getString("r_t", ""));
                headers.put("user_name", preferences.getString("user_name", ""));
                headers.put("email_id", preferences.getString("email_id", ""));
                //headers.put("cf_uuhid", preferences.getString("cf_uuhid", ""));
                headers.put("user_id", preferences.getString("user_id", ""));
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }
}
