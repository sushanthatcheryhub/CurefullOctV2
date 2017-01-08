package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GoalInfo implements MyConstants.JsonUtils {

    private String gender;
    private String dateOfBirth;
    private String height;
    private String weight;
    private String targetStepCount;
    private String targetCaloriesToBurn;
    private String targetWaterInTake;
    private String glassSize;
    private String glassNumber;
    private String primaryId;

    public GoalInfo() {

    }


    public GoalInfo(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            JSONObject jsonResponse1 = jsonObject.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            setDateOfBirth(jsonResponse1.getString("dateOfBirth"));
            setGender(jsonResponse1.getString("gender"));
            setHeight(jsonResponse1.getString("height"));
            setWeight(jsonResponse1.getString("weight"));
            setTargetStepCount(jsonResponse1.getString("targetStepCount"));
            setTargetCaloriesToBurn(jsonResponse1.getString("targetCaloriesToBurn"));
            setTargetWaterInTake(jsonResponse1.getString("targetWaterInTake"));
            setGlassSize(jsonResponse1.getString("glassSize"));
            setGlassNumber(jsonResponse1.getString("glassNumber"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public GoalInfo(Cursor cur) {
        if (cur == null)
            return;
        try {
            setDateOfBirth(cur.getString(cur.getColumnIndex("dateOfBirth")));
            setGender(cur.getString(cur.getColumnIndex("gender")));
            setHeight(cur.getString(cur.getColumnIndex("height")));
            setWeight(cur.getString(cur.getColumnIndex("weight")));
            setTargetStepCount(cur.getString(cur.getColumnIndex("targetStepCount")));
            setTargetCaloriesToBurn(cur.getString(cur.getColumnIndex("targetCaloriesToBurn")));
            setTargetWaterInTake(cur.getString(cur.getColumnIndex("targetWaterInTake")));
            setGlassSize(cur.getString(cur.getColumnIndex("glassSize")));
            setGlassNumber(cur.getString(cur.getColumnIndex("glassNumber")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ContentValues getInsertingValue(JSONObject json) {
        try {
            JSONObject jsonResponse1 = json.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            ContentValues values = new ContentValues();
            setPrimaryId(AppPreference.getInstance().getcf_uuhid());
            values.put("edit_id", AppPreference.getInstance().getcf_uuhid());
            values.put("dateOfBirth", jsonResponse1.getString("dateOfBirth"));
            values.put("gender", jsonResponse1.getString("gender"));
            values.put("height", jsonResponse1.getString("height"));
            values.put("weight", jsonResponse1.getString("weight"));
            values.put("targetStepCount", jsonResponse1.getString("targetStepCount"));
            values.put("targetCaloriesToBurn", jsonResponse1.getString("targetCaloriesToBurn"));
            values.put("targetWaterInTake", jsonResponse1.getString("targetWaterInTake"));
            values.put("glassSize", jsonResponse1.getString("glassSize"));
            values.put("glassNumber", jsonResponse1.getString("glassNumber"));
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getTargetStepCount() {
        return targetStepCount;
    }

    public void setTargetStepCount(String targetStepCount) {
        this.targetStepCount = targetStepCount;
    }

    public String getTargetCaloriesToBurn() {
        return targetCaloriesToBurn;
    }

    public void setTargetCaloriesToBurn(String targetCaloriesToBurn) {
        this.targetCaloriesToBurn = targetCaloriesToBurn;
    }

    public String getTargetWaterInTake() {
        return targetWaterInTake;
    }

    public void setTargetWaterInTake(String targetWaterInTake) {
        this.targetWaterInTake = targetWaterInTake;
    }

    public String getGlassSize() {
        return glassSize;
    }

    public void setGlassSize(String glassSize) {
        this.glassSize = glassSize;
    }

    public String getGlassNumber() {
        return glassNumber;
    }

    public void setGlassNumber(String glassNumber) {
        this.glassNumber = glassNumber;
    }
}
