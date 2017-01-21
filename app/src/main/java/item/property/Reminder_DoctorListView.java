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

    public Reminder_DoctorListView() {

    }

    public Reminder_DoctorListView(Object jsonObject) {
        try {

            Gson gson = new Gson();
            String json = gson.toJson(jsonObject);
            JSONObject jsonObject1 = new JSONObject(json);
            setDoctorName(jsonObject1.getString("doctorName"));
            setRemMedicineName(jsonObject1.getString("medicineName"));
            setBeforeMeal(jsonObject1.getBoolean("beforeMeal"));
            setAfterMeal(jsonObject1.getBoolean("afterMeal"));
            Log.e("doctor", " " + jsonObject1.getString("doctorName"));
            JSONObject jsonObject2 = new JSONObject(jsonObject1.getString("medicineReminderAlarmDetailsResponse"));
            setTimeToTake(jsonObject2.getString("timeToTakeMedicineInDay"));
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
}
