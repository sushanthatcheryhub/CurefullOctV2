package item.property;

import com.google.gson.Gson;

import org.json.JSONObject;

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

    public Doctor_Visit_Reminder_DoctorListView() {

    }

    public Doctor_Visit_Reminder_DoctorListView(Object jsonObject) {
        try {

            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            setDoctorName(jsonObject1.getString("doctorName"));
            setRemMedicineName(jsonObject1.getString("hospitalName"));
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
}
