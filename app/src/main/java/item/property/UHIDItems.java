package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UHIDItems implements MyConstants.JsonUtils {

    private String name;
    private String mobileNumber;
    private String cfUuhid;
    private boolean defaults;
    private boolean isSelected;
    private String primaryId;
    public UHIDItems() {

    }


    public UHIDItems(Cursor cur) {
        if (cur == null)
            return;
        try {
            setName(cur.getString(cur.getColumnIndex(NAME)));
            setMobileNumber(cur.getString(cur.getColumnIndex(MOBILE_NO)));
            setCfUuhid(cur.getString(cur.getColumnIndex("cfUuhid")));

            if(cur.getInt(cur.getColumnIndex(DEFAULT_USER))==1) {
                setDefaults(true);
            }else{
                setDefaults(false);
            }
            if(cur.getInt(cur.getColumnIndex(SELECTED))==1) {
                setSelected(true);
            }else{
                setSelected(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

    public ContentValues getInsertingUHIDValue(JSONObject json) {
        try {
            ContentValues values = new ContentValues();
            values.put(NAME, json.getString(NAME));
            values.put(MOBILE_NO, json.getString(MOBILE_NO));
            values.put("cfhid_user", AppPreference.getInstance().getcf_uuhid());
            setPrimaryId(json.getString("cfUuhid"));
            values.put("cfUuhid", json.getString("cfUuhid"));
            if (json.getBoolean(DEFAULT)) {
                values.put(DEFAULT_USER, 1);
            } else {
                values.put(DEFAULT_USER, 0);
            }
            if (json.getBoolean(SELECTED)) {
                values.put(SELECTED, 1);
            } else {
                values.put(SELECTED, 0);
            }
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
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

    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }

}
