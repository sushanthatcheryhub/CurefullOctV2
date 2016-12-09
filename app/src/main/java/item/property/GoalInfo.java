package item.property;

import android.database.Cursor;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GoalInfo {

    private String gender;
    private String dateOfBirth;
    private String height;
    private String weight;
    private String targetStepCount;
    private String targetCaloriesToBurn;
    private String targetWaterInTake;
    private String glassSize;

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

        } catch (Exception e) {
            e.printStackTrace();
        }
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
}
