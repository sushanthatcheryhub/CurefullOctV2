package item.property;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GraphView {

    private String fromDate;
    private String toDate;
    private String count;
    private String waterIntake;
    private String type;

    public GraphView() {
    }

    public GraphView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setFromDate(jsonObject.getString("fromDate"));
            setFromDate(jsonObject.getString("toDate"));
            setFromDate(jsonObject.getString("count"));
            setFromDate(jsonObject.getString("waterIntake"));
            setFromDate(jsonObject.getString("type"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
