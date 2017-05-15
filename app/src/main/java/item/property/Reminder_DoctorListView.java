package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;

import static utils.MyConstants.JsonUtils.COMMON_ID;
import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.TEST_NAME;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_DoctorListView implements Parcelable {

    private String remMedicineName;
    private String doctorName;
    private boolean isBeforeMeal;
    private boolean isAfterMeal;
    private String medicineReminderId;
    private String noOfDaysInWeek;
    private int year;
    private int month;
    private int date;
    private int quantity;
    private int noOfDays;
    private int interval;
    private int noOfDosage;
    private String type;
    private String status;
    private ArrayList<ReminderMedicnceDoagePer> reminderMedicnceDoagePers;
    private String common_id;
    private String isUploaded;
    private String cfuuhId;
    private String currentdate;
    private String edit;
    private String enddate;
    private String alarmTime;

    public String getEnddate() {
        return enddate;
    }

    public String getAlarmTime() {
        return alarmTime;
    }

    public void setEnddate(String enddate) {

        this.enddate = enddate;
    }

    public void setAlarmTime(String alarmTime) {
        this.alarmTime = alarmTime;
    }

    public Reminder_DoctorListView() {

    }

    public Reminder_DoctorListView(Object jsonObject) {
        try {

            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            setDoctorName(jsonObject1.getString("doctorName"));

            setQuantity(jsonObject1.getInt("quantity"));
            setNoOfDays(jsonObject1.getInt("noOfDays"));
            setInterval(jsonObject1.getInt("interval"));
            setNoOfDosage(jsonObject1.getInt("noOfDosage"));
            setType(jsonObject1.getString("type"));
            setStatus(jsonObject1.getString("status"));
            setRemMedicineName(jsonObject1.getString("medicineName"));
            setNoOfDaysInWeek(jsonObject1.getString("noOfDaysInWeek"));
            setMedicineReminderId(jsonObject1.getString("medicineReminderId"));
            setBeforeMeal(jsonObject1.getBoolean("beforeMeal"));
            setAfterMeal(jsonObject1.getBoolean("afterMeal"));
            setReminderMedicnceTimes(jsonObject1.getJSONArray("dosagePerDateResponse"));
            JSONObject jsonObject3 = new JSONObject(jsonObject1.getString("dateOfMedicineTake"));
            setYear(jsonObject3.getInt("year"));
            setDate(jsonObject3.getInt("dayOfMonth"));
            setMonth(jsonObject3.getInt("monthValue"));
            try {
                setCurrentDate(jsonObject1.getString("date"));
            } catch (Exception e) {
                e.getMessage();
                setCurrentDate("0000-00-00");
            }
            setEnddate(jsonObject1.getString("enddate"));
            setAlarmTime(jsonObject1.getString("noOfDays"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public void setCfuuhId(String cfuuhId) {
        this.cfuuhId = cfuuhId;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public String getCfuuhId() {
        return cfuuhId;
    }

    public String getCurrentDate() {
        return currentdate;
    }

    public String getEdit() {
        return edit;
    }

    public void setCurrentDate(String currentdate) {
        this.currentdate = currentdate;

    }

    public void setEdit(String edit) {
        this.edit = edit;
    }

    public Reminder_DoctorListView(Cursor cur) {
        if (cur == null) {
            return;
        }
        try {

            setRemMedicineName(cur.getString(cur.getColumnIndex("medicineName")));
            setDoctorName(cur.getString(cur.getColumnIndex("doctorName")));
            setQuantity(cur.getInt(cur.getColumnIndex("quantity")));
            setNoOfDays(cur.getInt(cur.getColumnIndex("noOfDays")));
            setInterval(cur.getInt(cur.getColumnIndex("interval")));
            setNoOfDosage(cur.getInt(cur.getColumnIndex("noOfDosage")));
            setType(cur.getString(cur.getColumnIndex("type")));
            setStatus(cur.getString(cur.getColumnIndex("status")));
            setNoOfDaysInWeek(cur.getString(cur.getColumnIndex("noOfDaysInWeek")));
            setMedicineReminderId(cur.getString(cur.getColumnIndex("medicineReminderId")));
            if (cur.getInt(cur.getColumnIndex("beforeMeal")) == 1) {
                setBeforeMeal(true);
            } else {
                setBeforeMeal(false);
            }

            if (cur.getInt(cur.getColumnIndex("afterMeal")) == 1) {
                setAfterMeal(true);
            } else {
                setAfterMeal(false);
            }
            setYear(cur.getInt(cur.getColumnIndex("year")));
            setDate(cur.getInt(cur.getColumnIndex("dayOfMonth")));
            setMonth(cur.getInt(cur.getColumnIndex("monthValue")));

            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            setCfuuhId(cur.getString(cur.getColumnIndex("cfuuhId")));

            setCommonID(cur.getString(cur.getColumnIndex("common_id")));
            try {
                setCurrentDate(cur.getString(cur.getColumnIndex("currentdate")));
            } catch (Exception e) {
                e.getMessage();

            }
            setEdit(cur.getString(cur.getColumnIndex("edit")));

            setEnddate(cur.getString(cur.getColumnIndex("enddate")));
            setAlarmTime(cur.getString(cur.getColumnIndex("alarmTime")));
            // setReminderMedicnceTimes(jsonObject.getJSONArray("dosagePerDateResponse")));

            reminderMedicnceDoagePers = DbOperations.setReminderMedicineDosageLocal(CureFull.getInstanse().getActivityIsntanse(), cur.getString(cur.getColumnIndex(COMMON_ID)), cur.getString(cur.getColumnIndex("currentdate")));


        } catch (Exception e) {
            e.getMessage();
        }

    }

    public String getCommonID() {
        return common_id;
    }

    public void setCommonID(String common_id) {

        this.common_id = common_id;
    }


    public void getInsertingValue(Object jsonObject) {
        try {
            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            ContentValues values = new ContentValues();
            values.put(DOCTOR_NAME, jsonObject1.getString("doctorName"));
            values.put("quantity", jsonObject1.getInt("quantity"));
            values.put("noOfDays", jsonObject1.getInt("noOfDays"));
            values.put("interval", jsonObject1.getInt("interval"));
            values.put("noOfDosage", jsonObject1.getInt("noOfDosage"));
            values.put("type", jsonObject1.getString("type"));
            values.put("status", jsonObject1.getString("status"));
            values.put("medicineName", jsonObject1.getString("medicineName"));
            values.put("noOfDaysInWeek", jsonObject1.getString("noOfDaysInWeek"));
            values.put("medicineReminderId", jsonObject1.getString("medicineReminderId"));

            if (jsonObject1.getBoolean("beforeMeal")) {
                values.put("beforeMeal", 1);
            } else {
                values.put("beforeMeal", 0);
            }
            if (jsonObject1.getBoolean("afterMeal")) {
                values.put("afterMeal", 1);
            } else {
                values.put("afterMeal", 0);
            }
            //values.put("dosagePerDateResponse", jsonObject.getString("dosagePerDateResponse"));

            JSONObject jsonObject3 = new JSONObject(jsonObject1.getString("dateOfMedicineTake"));
            values.put("year", jsonObject3.getInt("year"));
            values.put("dayOfMonth", jsonObject3.getInt("dayOfMonth"));
            values.put("monthValue", jsonObject3.getInt("monthValue"));

            values.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
            values.put("isUploaded", "0");//card.getInsertingValue(symptomslistArray.getJSONObject(i));
            try {
                values.put("currentdate", jsonObject1.getString("date"));
            } catch (Exception e) {
                e.getMessage();
                values.put("currentdate", "0000-00-00");
            }
            values.put("edit", "0");
            setCommonID(jsonObject1.getString("medicineReminderId"));
            values.put(COMMON_ID, getCommonID());
            values.put("alarmTime", jsonObject1.getString("alarmTime"));
            values.put("enddate", jsonObject1.getString("enddate"));
            try {
                DbOperations.insertMedicineRemiderReportByDoctor(CureFull.getInstanse().getActivityIsntanse(), values, jsonObject1.getString("medicineReminderId"), jsonObject1.getString("date"));
            } catch (Exception e) {
                DbOperations.insertMedicineRemiderReportByDoctor(CureFull.getInstanse().getActivityIsntanse(), values, jsonObject1.getString("medicineReminderId"), "");
            }
            try {
                setReminderMedicnceTimesLocal(jsonObject1.getJSONArray("dosagePerDateResponse"), getCommonID());
            } catch (Exception e) {
                e.getMessage();
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void setReminderMedicnceTimesLocal(JSONArray symptomslistArray, String commonID) {
        if (symptomslistArray == null)
            return;
        ReminderMedicnceDoagePer card = null;
        this.reminderMedicnceDoagePers = new ArrayList<ReminderMedicnceDoagePer>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new ReminderMedicnceDoagePer(symptomslistArray.getJSONObject(i));
                this.reminderMedicnceDoagePers.add(card);
                card.getInsertingValue(symptomslistArray.getJSONObject(i), commonID, i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getRemMedicineName() {
        return remMedicineName;
    }

    public void setRemMedicineName(String remMedicineName) {
        this.remMedicineName = remMedicineName;
    }

    public boolean isBeforeMeal() {
        return isBeforeMeal;
    }

    public void setBeforeMeal(boolean beforeMeal) {
        isBeforeMeal = beforeMeal;
    }

    public boolean isAfterMeal() {
        return isAfterMeal;
    }

    public void setAfterMeal(boolean afterMeal) {
        isAfterMeal = afterMeal;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getMedicineReminderId() {
        return medicineReminderId;
    }

    public void setMedicineReminderId(String medicineReminderId) {
        this.medicineReminderId = medicineReminderId;
    }

    public String getNoOfDaysInWeek() {
        return noOfDaysInWeek;
    }

    public void setNoOfDaysInWeek(String noOfDaysInWeek) {
        this.noOfDaysInWeek = noOfDaysInWeek;
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getNoOfDays() {
        return noOfDays;
    }

    public void setNoOfDays(int noOfDays) {
        this.noOfDays = noOfDays;
    }

    public int getInterval() {
        return interval;
    }

    public void setInterval(int interval) {
        this.interval = interval;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getNoOfDosage() {
        return noOfDosage;
    }

    public void setNoOfDosage(int noOfDosage) {
        this.noOfDosage = noOfDosage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public ArrayList<ReminderMedicnceDoagePer> getReminderMedicnceDoagePers() {
        return reminderMedicnceDoagePers;
    }

    public void setReminderMedicnceDoagePers(ArrayList<ReminderMedicnceDoagePer> reminderMedicnceDoagePers) {
        this.reminderMedicnceDoagePers = reminderMedicnceDoagePers;
    }

    public void setReminderMedicnceTimes(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        ReminderMedicnceDoagePer card = null;
        this.reminderMedicnceDoagePers = new ArrayList<ReminderMedicnceDoagePer>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new ReminderMedicnceDoagePer(symptomslistArray.getJSONObject(i));
                this.reminderMedicnceDoagePers.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public Reminder_DoctorListView(Parcel in) {
    }

    public static final Parcelable.Creator<Reminder_DoctorListView> CREATOR = new Parcelable.Creator<Reminder_DoctorListView>() {
        public Reminder_DoctorListView createFromParcel(Parcel in) {
            return new Reminder_DoctorListView(in);
        }

        public Reminder_DoctorListView[] newArray(int size) {
            return new Reminder_DoctorListView[size];
        }
    };
}
