package item.property;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_SelfListView implements Parcelable {

    private String remMedicineName;
    private boolean isBeforeMeal;
    private boolean isAfterMeal;
    private String medicineReminderId;
    private String doctorName;
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

    public Reminder_SelfListView() {

    }

    public Reminder_SelfListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setRemMedicineName(jsonObject.getString("medicineName"));
            setDoctorName(jsonObject.getString("doctorName"));
            setQuantity(jsonObject.getInt("quantity"));
            setNoOfDays(jsonObject.getInt("noOfDays"));
            setInterval(jsonObject.getInt("interval"));
            setNoOfDosage(jsonObject.getInt("noOfDosage"));
            setType(jsonObject.getString("type"));
            setStatus(jsonObject.getString("status"));
            setNoOfDaysInWeek(jsonObject.getString("noOfDaysInWeek"));
            setMedicineReminderId(jsonObject.getString("medicineReminderId"));
            setBeforeMeal(jsonObject.getBoolean("beforeMeal"));
            setAfterMeal(jsonObject.getBoolean("afterMeal"));
            setReminderMedicnceTimes(jsonObject.getJSONArray("dosagePerDateResponse"));
            JSONObject jsonObject3 = new JSONObject(jsonObject.getString("dateOfMedicineTake"));
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


    public String getMedicineReminderId() {
        return medicineReminderId;
    }

    public void setMedicineReminderId(String medicineReminderId) {
        this.medicineReminderId = medicineReminderId;
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

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getNoOfDaysInWeek() {
        return noOfDaysInWeek;
    }

    public void setNoOfDaysInWeek(String noOfDaysInWeek) {
        this.noOfDaysInWeek = noOfDaysInWeek;
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

    public Reminder_SelfListView(Parcel in) {
    }

    public static final Parcelable.Creator<Reminder_SelfListView> CREATOR = new Parcelable.Creator<Reminder_SelfListView>() {
        public Reminder_SelfListView createFromParcel(Parcel in) {
            return new Reminder_SelfListView(in);
        }

        public Reminder_SelfListView[] newArray(int size) {
            return new Reminder_SelfListView[size];
        }
    };
}
