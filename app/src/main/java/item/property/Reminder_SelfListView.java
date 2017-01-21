package item.property;

import org.json.JSONObject;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class Reminder_SelfListView {

    private String remMedicineName;
    private boolean isBeforeMeal;
    private boolean isAfterMeal;
    private String timeToTake;

    public Reminder_SelfListView() {

    }

    public Reminder_SelfListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setRemMedicineName(jsonObject.getString("medicineName"));
            setBeforeMeal(jsonObject.getBoolean("beforeMeal"));
            setAfterMeal(jsonObject.getBoolean("afterMeal"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("medicineReminderAlarmDetailsResponse"));
            setTimeToTake(jsonObject1.getString("timeToTakeMedicineInDay"));
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
}
