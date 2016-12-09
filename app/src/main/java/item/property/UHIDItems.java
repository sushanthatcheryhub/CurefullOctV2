package item.property;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UHIDItems {

    private String name;
    private String mobileNumber;
    private String cfUuhid;
    private boolean defaults;
    private boolean isSelected;

    public UHIDItems() {

    }


    public UHIDItems(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setName(jsonObject.getString("name"));
            setMobileNumber(jsonObject.getString("mobileNumber"));
            setCfUuhid(jsonObject.getString("cfUuhid"));
            setDefaults(jsonObject.getBoolean("default"));
            setSelected(jsonObject.getBoolean("selected"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getCfUuhid() {
        return cfUuhid;
    }

    public void setCfUuhid(String cfUuhid) {
        this.cfUuhid = cfUuhid;
    }

    public boolean isDefaults() {
        return defaults;
    }

    public void setDefaults(boolean defaults) {
        this.defaults = defaults;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
