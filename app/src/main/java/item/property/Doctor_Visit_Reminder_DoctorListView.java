package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import com.google.gson.Gson;

import org.json.JSONObject;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;

import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.TEST_NAME;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Doctor_Visit_Reminder_DoctorListView {

    private String remMedicineName;
    private String doctorName;
    private int hour;
    private int mintue;
    private int year;
    private int month;
    private int date;
    private String doctorFollowupReminderId;
    private String status;
    private String isUploaded;
    private String cfuuhId;

    public String getIsUploaded() {
        return isUploaded;
    }

    public String getCfuuhId() {
        return cfuuhId;
    }

    public void setIsUploaded(String isUploaded) {

        this.isUploaded = isUploaded;
    }

    public void setCfuuhId(String cfuuhId) {
        this.cfuuhId = cfuuhId;
    }

    public Doctor_Visit_Reminder_DoctorListView() {

    }

    public Doctor_Visit_Reminder_DoctorListView(Object jsonObject) {
        try {

            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            setDoctorName(jsonObject1.getString("doctorName"));
            setRemMedicineName(jsonObject1.getString("hospitalName"));
            setStatus(jsonObject1.getString("status"));
            setDoctorFollowupReminderId(jsonObject1.getString("doctorFollowupReminderId"));
            JSONObject jsonObject3 = new JSONObject(jsonObject1.getString("followupDate"));
            setYear(jsonObject3.getInt("year"));
            setDate(jsonObject3.getInt("dayOfMonth"));
            setMonth(jsonObject3.getInt("monthValue"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("followupTime"));
            setHour(jsonObject2.getInt("hour"));
            setMintue(jsonObject2.getInt("minute"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Doctor_Visit_Reminder_DoctorListView(Cursor cur, String notinuse) {
        if (cur == null) {
            return;
        }
        try {

            setDoctorName(cur.getString(cur.getColumnIndex("doctorName")));
            setRemMedicineName(cur.getString(cur.getColumnIndex("hospitalName")));
            setStatus(cur.getString(cur.getColumnIndex("status")));
            setDoctorFollowupReminderId(cur.getString(cur.getColumnIndex("doctorFollowupReminderId")));
            setHour(cur.getInt(cur.getColumnIndex("hour")));
            setMintue(cur.getInt(cur.getColumnIndex("minute")));
            setYear(cur.getInt(cur.getColumnIndex("year")));
            setDate(cur.getInt(cur.getColumnIndex("dayOfMonth")));
            setMonth(cur.getInt(cur.getColumnIndex("monthValue")));
            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            setCfuuhId(cur.getString(cur.getColumnIndex("cfuuhId")));
        } catch (Exception e) {
            e.getMessage();
        }

    }


    public String getRemMedicineName() {
        return remMedicineName;
    }

    public void setRemMedicineName(String remMedicineName) {
        this.remMedicineName = remMedicineName;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMintue() {
        return mintue;
    }

    public void setMintue(int mintue) {
        this.mintue = mintue;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public String getDoctorFollowupReminderId() {
        return doctorFollowupReminderId;
    }

    public void setDoctorFollowupReminderId(String doctorFollowupReminderId) {
        this.doctorFollowupReminderId = doctorFollowupReminderId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void getInsertingValue(Object jsonObject) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            ContentValues values = new ContentValues();
            values.put(DOCTOR_NAME, jsonObject1.getString("doctorName"));
            values.put("hospitalName", jsonObject1.getString("hospitalName"));//remMedicineName

            values.put("status", jsonObject1.getString("status"));
            values.put("doctorFollowupReminderId", jsonObject1.getString("doctorFollowupReminderId"));

            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("followupTime"));
            values.put("hour", jsonObject2.getInt("hour"));
            values.put("minute", jsonObject2.getInt("minute"));

            JSONObject jsonObject3 = new JSONObject(jsonObject1.getString("followupDate"));
            values.put("year", jsonObject3.getInt("year"));
            values.put("dayOfMonth", jsonObject3.getInt("dayOfMonth"));
            values.put("monthValue", jsonObject3.getInt("monthValue"));
            values.put("isUploaded", "0");
            values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());

            DbOperations.insertDoctorRemiderByCurefull(CureFull.getInstanse().getActivityIsntanse(), values, jsonObject1.getString("doctorFollowupReminderId"));


        } catch (Exception e) {
            e.getMessage();
        }

    }
}
