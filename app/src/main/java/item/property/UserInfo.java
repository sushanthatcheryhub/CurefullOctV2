package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UserInfo implements MyConstants.JsonUtils {

    private String fname;
    private String user_name;
    private String user_id;
    private String mobile_number;
    private String a_t;
    private String r_t;
    private String cf_uuhid;
    private String profileImageUrl;
    private String primaryId;
    private String hintScreen;
    private String user_id_profile;

    public UserInfo() {

    }


    public UserInfo(Cursor cur) {
        if (cur == null)
            return;
        try {
            setUser_id(cur.getString(cur.getColumnIndex(EMAIL)));
            setUser_name(cur.getString(cur.getColumnIndex(NAME)));
            setMobile_number(cur.getString(cur.getColumnIndex(MOBILE_NO)));
            setProfileImageUrl(cur.getString(cur.getColumnIndex(PROFILE_IMAGE_URL)));
            setA_t(cur.getString(cur.getColumnIndex(A_T)));
            setR_t(cur.getString(cur.getColumnIndex(R_T)));
            setCf_uuhid(cur.getString(cur.getColumnIndex(CF_UUHID)));
            setHintScreen(cur.getString(cur.getColumnIndex("hint_screen")));
            setUser_id_profile(cur.getString(cur.getColumnIndex("user_id")));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public UserInfo(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {

            JSONObject jsonResponse1 = jsonObject.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            setUser_id(jsonResponse1.getString(EMAIL));
            setUser_name(jsonResponse1.getString(NAME));
            setMobile_number(jsonResponse1.getString(MOBILE_NO));
            setProfileImageUrl(jsonResponse1.getString(PROFILE_IMAGE_URL));
            JSONObject jsonResponse = jsonObject.getJSONObject(MyConstants.JsonUtils.HEADERS);
//            setFname(jsonResponse.getString(MyConstants.JsonUtils.FNAME));
            setA_t(jsonResponse.getString(MyConstants.JsonUtils.A_T));
            setR_t(jsonResponse.getString(MyConstants.JsonUtils.R_T));
            setCf_uuhid(jsonResponse.getString(MyConstants.JsonUtils.CF_UUHID));
            setUser_id_profile(jsonResponse.getString("user_id"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ContentValues getInsertingValue(JSONObject json) {
        try {
            JSONObject jsonResponse1 = json.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            ContentValues values = new ContentValues();
            values.put(EMAIL, jsonResponse1.getString(EMAIL));
            values.put(NAME, jsonResponse1.getString(NAME));
            values.put(MOBILE_NO, jsonResponse1.getString(MOBILE_NO));
            values.put(PROFILE_IMAGE_URL, jsonResponse1.getString(PROFILE_IMAGE_URL));
            JSONObject jsonResponse = json.getJSONObject(MyConstants.JsonUtils.HEADERS);
            values.put(A_T, jsonResponse.getString(A_T));
            values.put(R_T, jsonResponse.getString(R_T));
            setPrimaryId(jsonResponse.getString(CF_UUHID));
            values.put(CF_UUHID, jsonResponse.getString(CF_UUHID));
            values.put("user_id", jsonResponse.getString("user_id"));
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }


    public String getCf_uuhid() {
        return cf_uuhid;
    }

    public void setCf_uuhid(String cf_uuhid) {
        this.cf_uuhid = cf_uuhid;
    }

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getA_t() {
        return a_t;
    }

    public void setA_t(String a_t) {
        this.a_t = a_t;
    }

    public String getR_t() {
        return r_t;
    }

    public void setR_t(String r_t) {
        this.r_t = r_t;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }


    public String getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(String primaryId) {
        this.primaryId = primaryId;
    }


    public String getHintScreen() {
        return hintScreen;
    }

    public void setHintScreen(String hintScreen) {
        this.hintScreen = hintScreen;
    }

    public String getUser_id_profile() {
        return user_id_profile;
    }

    public void setUser_id_profile(String user_id_profile) {
        this.user_id_profile = user_id_profile;
    }
}
