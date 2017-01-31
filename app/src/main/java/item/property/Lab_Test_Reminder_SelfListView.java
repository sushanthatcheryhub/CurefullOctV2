package item.property;

import org.json.JSONObject;

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
}
