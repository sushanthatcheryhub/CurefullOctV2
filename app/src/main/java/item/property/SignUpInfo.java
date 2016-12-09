package item.property;

import android.database.Cursor;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class SignUpInfo {

    private String fname;
    private String user_name;
    private String user_id;
    private String a_t;
    private String r_t;
    private String cf_uuhid;
    private String mobileNumber;

    public SignUpInfo() {

    }


    public SignUpInfo(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            JSONObject jsonResponse1 = jsonObject.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            setUser_id(jsonResponse1.getString(MyConstants.PrefrenceKeys.EMAIL));
            setUser_name(jsonResponse1.getString(MyConstants.PrefrenceKeys.NAME));
            setMobileNumber(jsonResponse1.getString("mobileNumber"));

            JSONObject jsonResponse = jsonObject.getJSONObject(MyConstants.JsonUtils.HEADERS);
//            setFname(jsonResponse.getString(MyConstants.JsonUtils.FNAME));
            setA_t(jsonResponse.getString(MyConstants.JsonUtils.A_T));
            setR_t(jsonResponse.getString(MyConstants.JsonUtils.R_T));
            setCf_uuhid(jsonResponse.getString(MyConstants.JsonUtils.CF_UUHID));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public SignUpInfo(Cursor cur) {
        if (cur == null)
            return;
        try {
            setFname(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.FNAME)));
//            setPhone(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.PHONE)));
//            setGender(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.GENDER)));
//            setAddress(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.ADDRESS)));
//            setCity(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.CITY)));
//            setState(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.STATE)));
//            setPincode(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.PINCODE)));
//            setStatus(cur.getString(cur.getColumnIndex(MyConstants.JsonUtils.STATUS)));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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


}
