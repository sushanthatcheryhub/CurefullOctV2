package item.property;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GraphViewDetails {

    private String date;
    private String count;
    private String waterIntake;

    public GraphViewDetails() {
    }

    public GraphViewDetails(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setDate(jsonObject.getString("date"));
            setCount(jsonObject.getString("count"));
            setWaterIntake(jsonObject.getString("waterIntake"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }

    public String getWaterIntake() {
        return waterIntake;
    }

    public void setWaterIntake(String waterIntake) {
        this.waterIntake = waterIntake;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
