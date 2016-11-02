package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import curefull.healthapp.CureFull;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class AppPreference implements MyConstants.PrefrenceKeys {
    private static AppPreference prefrence;
    private SharedPreferences _prefrence;

    public static AppPreference getInstance() {
        if (CureFull.getInstanse().getActivityIsntanse() == null)
            return null;
        if (prefrence == null)
            prefrence = new AppPreference(CureFull.getInstanse().getActivityIsntanse());
        return prefrence;
    }

    private AppPreference(Context context) {
        _prefrence = PreferenceManager.getDefaultSharedPreferences(context);
    }


    public void setFacebookUserName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(USER_NAME, className).commit();
    }

    public String getFacebookUserName() {
        if (_prefrence != null)
            return _prefrence.getString(USER_NAME, null);
        return null;
    }

    public void setFacebookProfileImage(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(PROFILE_PIC, className).commit();
    }

    public String getFacebookProfileImage() {
        if (_prefrence != null)
            return _prefrence.getString(PROFILE_PIC, "");
        return "";
    }


    public void setPassword(String password) {
        if (_prefrence != null)
            _prefrence.edit().putString(PASSWORD, password).commit();
    }

    public String getPassword() {
        if (_prefrence != null)
            return _prefrence.getString(PASSWORD, "");
        return "";
    }

    public void setAt(String password) {
        if (_prefrence != null)
            _prefrence.edit().putString(AT, password).commit();
    }

    public String getAt() {
        if (_prefrence != null)
            return _prefrence.getString(AT, "");
        return "";
    }

    public void setRt(String password) {
        if (_prefrence != null)
            _prefrence.edit().putString(RT, password).commit();
    }

    public String getRt() {
        if (_prefrence != null)
            return _prefrence.getString(RT, "");
        return "";
    }

    public void setIsLogin(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean(PREF_IS_LOGIN, isLogin).commit();
        }
    }

    public boolean isLogin() {
        if (_prefrence != null)
            return _prefrence.getBoolean(PREF_IS_LOGIN, false);
        return false;
    }

    public void setIsDestroy(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("destroy", isLogin).commit();
        }
    }

    public boolean isDestory() {
        if (_prefrence != null)
            return _prefrence.getBoolean("destroy", false);
        return false;
    }


    public void setIsLoginFirst(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean(LOGIN_FIRST, isLogin).commit();
        }
    }

    public boolean isLoginFirst() {
        if (_prefrence != null)
            return _prefrence.getBoolean(LOGIN_FIRST, false);
        return false;
    }


    public void setProfileID(int className) {
        if (_prefrence != null)
            _prefrence.edit().putInt(PROFILE_ID, className).commit();
    }

    public int getProfileID() {
        if (_prefrence != null)
            return _prefrence.getInt(PROFILE_ID, 0);
        return 0;
    }

    public void setUserID(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(USER_ID, className).commit();
    }

    public String getUserID() {
        if (_prefrence != null)
            return _prefrence.getString(USER_ID, "");
        return "";
    }

    public void setUserName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(USER_NAME, className).commit();
    }

    public String getUserName() {
        if (_prefrence != null)
            return _prefrence.getString(USER_NAME, "");
        return "";
    }


    public void setNearLocation(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("location", className).commit();
    }

    public String getNearLocation() {
        if (_prefrence != null)
            return _prefrence.getString("location", "");
        return "";
    }


    public void setDoctorName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorName", className).commit();
    }

    public String getDoctorName() {
        if (_prefrence != null)
            return _prefrence.getString("doctorName", "");
        return "";
    }


    public void setDoctorQualification(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorQualification", className).commit();
    }

    public String getDoctorQualification() {
        if (_prefrence != null)
            return _prefrence.getString("doctorQualification", "");
        return "";
    }

    public void setDoctorSpecifiation(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("DoctorSpecifiation", className).commit();
    }

    public String getDoctorSpecifiation() {
        if (_prefrence != null)
            return _prefrence.getString("DoctorSpecifiation", "");
        return "";
    }

    public void setDoctorLocation(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorLocation", className).commit();
    }

    public String getDoctorLocation() {
        if (_prefrence != null)
            return _prefrence.getString("doctorLocation", "");
        return "";
    }

    public void setDoctorHospitalName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("HospitalName", className).commit();
    }

    public String getHospitalName() {
        if (_prefrence != null)
            return _prefrence.getString("HospitalName", "");
        return "";
    }

    public void setPatientName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("PatientName", className).commit();
    }

    public String getPatientName() {
        if (_prefrence != null)
            return _prefrence.getString("PatientName", "");
        return "";
    }


    public void setPatientDoctorId(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("patientDoctorId", className).commit();
    }

    public String getPatientDoctorId() {
        if (_prefrence != null)
            return _prefrence.getString("patientDoctorId", "");
        return "";
    }

    public void setDoctorDmcNo(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorDmcNo", className).commit();
    }

    public String getDoctorDmcNo() {
        if (_prefrence != null)
            return _prefrence.getString("doctorDmcNo", "");
        return "";
    }

    public void setDoctorSignature(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorSignature", className).commit();
    }

    public String getDoctorSignature() {
        if (_prefrence != null)
            return _prefrence.getString("doctorSignature", "");
        return "";
    }

    public void setDoctorImage(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("DoctorImage", className).commit();
    }

    public String getDoctorImage() {
        if (_prefrence != null)
            return _prefrence.getString("DoctorImage", "");
        return "";
    }


    public void setDoctorId(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("DoctorId", className).commit();
    }

    public String getDoctorId() {
        if (_prefrence != null)
            return _prefrence.getString("DoctorId", "");
        return "";
    }

}



