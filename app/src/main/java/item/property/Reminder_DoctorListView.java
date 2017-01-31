package item.property;

import android.util.Log;

import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONObject;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_DoctorListView {

    private String remMedicineName;
    private String doctorName;
    private boolean isBeforeMeal;
    private boolean isAfterMeal;
    private String timeToTake;
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
            Log.e("doctor", " " + jsonObject1.getString("doctorName"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("medicineReminderAlarmDetailsResponse"));
            setTimeToTake(jsonObject2.getString("timeToTakeMedicineInDay"));

            JSONObject jsonObject3 = new JSONObject(jsonObject1.getString("dateOfMedicineTake"));
            setYear(jsonObject3.getInt("year"));
            setDate(jsonObject3.getInt("dayOfMonth"));
            setMonth(jsonObject3.getInt("monthValue"));

        } catch (Exception e) {
            e.printStackTrace();
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

    public String getTimeToTake() {
        return timeToTake;
    }

    public void setTimeToTake(String timeToTake) {
        this.timeToTake = timeToTake;
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
}
