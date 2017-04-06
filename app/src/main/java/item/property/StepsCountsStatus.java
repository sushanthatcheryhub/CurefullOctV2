package item.property;

import android.database.Cursor;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class StepsCountsStatus implements MyConstants.JsonUtils {

    private int steps_count;
    private int runing;
    private int cycling;
    private String calories;
    private String dateTime;
    private String waterTake;
    private String cf_uuhid;
    private int status;

    public StepsCountsStatus() {
    }


    public StepsCountsStatus(Cursor cur) {
        if (cur == null)
            return;
        try {
            setSteps_count(cur.getInt(cur.getColumnIndex("steps_count")));
            setRuning(cur.getInt(cur.getColumnIndex("runing")));
            setCycling(cur.getInt(cur.getColumnIndex("cycling")));
            setCalories(cur.getString(cur.getColumnIndex("calories")));
            setDateTime(cur.getString(cur.getColumnIndex("date")));
            setWaterTake(cur.getString(cur.getColumnIndex("waterTake")));
            setCf_uuhid(cur.getString(cur.getColumnIndex("cf_uuhid")));
            setStatus(cur.getInt(cur.getColumnIndex("status")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public int getSteps_count() {
        return steps_count;
    }

    public void setSteps_count(int steps_count) {
        this.steps_count = steps_count;
    }

    public int getRuning() {
        return runing;
    }

    public void setRuning(int runing) {
        this.runing = runing;
    }

    public int getCycling() {
        return cycling;
    }

    public void setCycling(int cycling) {
        this.cycling = cycling;
    }

    public String getCalories() {
        return calories;
    }

    public void setCalories(String calories) {
        this.calories = calories;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getWaterTake() {
        return waterTake;
    }

    public void setWaterTake(String waterTake) {
        this.waterTake = waterTake;
    }

    public String getCf_uuhid() {
        return cf_uuhid;
    }

    public void setCf_uuhid(String cf_uuhid) {
        this.cf_uuhid = cf_uuhid;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
