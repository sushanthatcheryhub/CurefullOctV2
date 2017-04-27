package item.property;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import operations.DatabaseHelper;
import operations.DbOperations;
import utils.AppPreference;

import static utils.MyConstants.IDataBaseTableKeys.TABLE_LAB_REMINDER_SELF;
import static utils.MyConstants.JsonUtils.COMMON_ID;
import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.IMAGE_NUMBER;
import static utils.MyConstants.JsonUtils.REPORT_IMAGE;
import static utils.MyConstants.JsonUtils.TEST_NAME;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Lab_Test_Reminder_SelfListView {

    private String remMedicineName;
    private String doctorName;
    private int hour;
    private int mintue;
    private boolean afterMeal;
    private String labTestReminderId;
    private int year;
    private int month;
    private int date;
    private String labName;
    private String status;
    private String isUploaded;
    private String cfuuhId;

    public String getCfuuhId() {
        return cfuuhId;
    }

    public void setCfuuhId(String cfuuhId) {
        this.cfuuhId = cfuuhId;
    }

    public Lab_Test_Reminder_SelfListView() {

    }

    public Lab_Test_Reminder_SelfListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setDoctorName(jsonObject.getString("doctorName"));
            setRemMedicineName(jsonObject.getString("testName"));
            setLabName(jsonObject.getString("labName"));
            setStatus(jsonObject.getString("labTestStatus"));
            setLabTestReminderId(jsonObject.getString("labTestReminderId"));
            JSONObject jsonObject2 = new JSONObject(jsonObject.getString("labTestTime"));
            setHour(jsonObject2.getInt("hour"));
            setMintue(jsonObject2.getInt("minute"));


            setAfterMeal(jsonObject.getBoolean("afterMeal"));

            JSONObject jsonObject3 = new JSONObject(jsonObject.getString("labTestDate"));
            setYear(jsonObject3.getInt("year"));
            setDate(jsonObject3.getInt("dayOfMonth"));
            setMonth(jsonObject3.getInt("monthValue"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public Lab_Test_Reminder_SelfListView(Cursor cur) {
        if (cur == null) {
            return;
        }
        try {

            setDoctorName(cur.getString(cur.getColumnIndex("doctorName")));
            setRemMedicineName(cur.getString(cur.getColumnIndex("testName")));
            setLabName(cur.getString(cur.getColumnIndex("labName")));
            setStatus(cur.getString(cur.getColumnIndex("labTestStatus")));
            setLabTestReminderId(cur.getString(cur.getColumnIndex("labTestReminderId")));

            setHour(cur.getInt(cur.getColumnIndex("hour")));
            setMintue(cur.getInt(cur.getColumnIndex("minute")));

            if (cur.getInt(cur.getColumnIndex("afterMeal")) == 1) {
                setAfterMeal(true);
            } else {
                setAfterMeal(false);
            }
            //setAfterMeal(cur.getString(cur.getColumnIndex("afterMeal")));

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

    public boolean isAfterMeal() {
        return afterMeal;
    }

    public void setAfterMeal(boolean afterMeal) {
        this.afterMeal = afterMeal;
    }

    public String getLabTestReminderId() {
        return labTestReminderId;
    }

    public void setLabTestReminderId(String labTestReminderId) {
        this.labTestReminderId = labTestReminderId;
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

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void getInsertingValue(JSONObject jsonObject) {
        try {
            ContentValues values = new ContentValues();
            values.put(DOCTOR_NAME, jsonObject.getString("doctorName"));
            values.put(TEST_NAME, jsonObject.getString("testName"));
            values.put("labName", jsonObject.getString("labName"));
            values.put("labTestStatus", jsonObject.getString("labTestStatus"));
            values.put("labTestReminderId", jsonObject.getString("labTestReminderId"));

            if (jsonObject.getBoolean("afterMeal")) {
                values.put("afterMeal", 1);
            } else {
                values.put("afterMeal", 0);
            }
            //values.put("afterMeal", jsonObject.getBoolean("afterMeal"));

            values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());

            JSONObject jsonObject2 = new JSONObject(jsonObject.getString("labTestTime"));
            values.put("hour", jsonObject2.getInt("hour"));
            values.put("minute", jsonObject2.getInt("minute"));

            JSONObject jsonObject3 = new JSONObject(jsonObject.getString("labTestDate"));
            values.put("year", jsonObject3.getInt("year"));
            values.put("dayOfMonth", jsonObject3.getInt("dayOfMonth"));
            values.put("monthValue", jsonObject3.getInt("monthValue"));
            values.put("isUploaded", "0");
            DbOperations.insertLabTestRemiderReport(CureFull.getInstanse().getActivityIsntanse(), values, jsonObject.getString("labTestReminderId"));

        } catch (Exception e) {
            e.printStackTrace();

        }


    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }
}
