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

    public void setSubjectTimeStamp(long timeStamp) {
        if (_prefrence != null)
            _prefrence.edit().putLong(PREF_SUBJECTS_TIME_STAMP, timeStamp).commit();
    }

    public long getSubjectTimeStamp() {
        if (_prefrence != null)
            return _prefrence.getLong(PREF_SUBJECTS_TIME_STAMP, 0);
        return 0;
    }

    public void setClassID(int classId) {
        if (_prefrence != null)
            _prefrence.edit().putInt(PREF_CLASS_ID, classId).commit();
    }

    public int getClassId() {
        if (_prefrence != null)
            return _prefrence.getInt(PREF_CLASS_ID, 0);
        return 0;
    }

    public void setClassName(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(PREF_CLASS_ID, className).commit();
    }

    public String getClassName() {
        if (_prefrence != null)
            return _prefrence.getString(PREF_CLASS_ID, null);
        return null;
    }

    public void setProfileImage(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString(PROFILE_PIC, className).commit();
    }

    public String getProfileImage() {
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


    public void setDoctorHealhcareProviderId(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("doctorHealhcareProviderId", className).commit();
    }

    public String getDoctorHealhcareProviderId() {
        if (_prefrence != null)
            return _prefrence.getString("doctorHealhcareProviderId", "");
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



