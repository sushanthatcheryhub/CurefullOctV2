package utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import curefull.healthapp.CureFull;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class AppPreference implements MyConstants.JsonUtils {
    private static AppPreference prefrence;
    private SharedPreferences _prefrence;

    public static AppPreference getInstance() {
        if (CureFull.getInstanse().getActivityIsntanse() == null)
            return null;
        if (prefrence == null)
            prefrence = new AppPreference(CureFull.getInstanse().getActivityIsntanse());
        return prefrence;
    }


    public void clearAllData() {
        _prefrence.edit().clear().commit();
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


    public void setIsEditGoal(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("EditGoal", isLogin).commit();
        }
    }

    public boolean isEditGoal() {
        if (_prefrence != null)
            return _prefrence.getBoolean("EditGoal", false);
        return false;
    }


    public void setIsEditGoalPage(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("EditGoalPage", isLogin).commit();
        }
    }

    public boolean isEditGoalPage() {
        if (_prefrence != null)
            return _prefrence.getBoolean("EditGoalPage", false);
        return false;
    }


    public void setIsFirstTimeScreen1(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("screen1", isLogin).commit();
        }
    }

    public boolean isFirstTimeScreen1() {
        if (_prefrence != null)
            return _prefrence.getBoolean("screen1", false);
        return false;
    }

    public void setIsFirstTimeSteps(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("screen1Steps", isLogin).commit();
        }
    }

    public boolean isFirstTimeSteps() {
        if (_prefrence != null)
            return _prefrence.getBoolean("screen1Steps", false);
        return false;
    }

    public void setcf_uuhid(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("cf_uuhid", className).commit();
    }

    public String getcf_uuhid() {
        if (_prefrence != null)
            return _prefrence.getString("cf_uuhid", "");
        return "";
    }
    public String getcf_uuhidLocal() {
        if (_prefrence != null)
            return _prefrence.getString("cfUuhid", "");
        return "";
    }

    public void setHintScreen(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("hint_screen", className).commit();
    }

    public String getHintScreen() {
        if (_prefrence != null)
            return _prefrence.getString("hint_screen", "");
        return "";
    }


    public void setcf_uuhidNeew(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("cf_uuhidNeew", className).commit();
    }

    public String getcf_uuhidNeew() {
        if (_prefrence != null)
            return _prefrence.getString("cf_uuhidNeew", "");
        return "";
    }


    public void setcf_uuhidSignUp(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("cf_uuhidSingUp", className).commit();
    }

    public String getcf_uuhidSignUp() {
        if (_prefrence != null)
            return _prefrence.getString("cf_uuhidSingUp", "");
        return "";
    }

    public void setMobileNumber(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("mobileNmuber", className).commit();
    }

    public String getMobileNumber() {
        if (_prefrence != null)
            return _prefrence.getString("mobileNmuber", "");
        return "";
    }


    public void setProfileImage(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("profileImageUrl", className).commit();
    }

    public String getProfileImage() {
        if (_prefrence != null)
            return _prefrence.getString("profileImageUrl", "");
        return "";
    }


    public void setGlass(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("glass", className).commit();
    }

    public String getGlass() {
        if (_prefrence != null)
            return _prefrence.getString("glass", "0");
        return "";
    }

    public void setUserID(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("email_id", className).commit();
    }

    public String getUserID() {
        if (_prefrence != null)
            return _prefrence.getString("email_id", "");
        return "";
    }


    public void setUserIDProfile(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("UserIDProfile", className).commit();
    }

    public String getUserIDProfile() {
        if (_prefrence != null)
            return _prefrence.getString("UserIDProfile", "");
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


    public void setFtIN(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("ftin", isMale).commit();
    }

    public boolean getFtIN() {
        if (_prefrence != null)
            return _prefrence.getBoolean("ftin", true);
        return false;
    }

    public void setCM(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("cm", isMale).commit();
    }

    public boolean getCM() {
        if (_prefrence != null)
            return _prefrence.getBoolean("cm", false);
        return false;
    }


    public void setStepStarts(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("Steps", isMale).commit();
    }

    public boolean getStepStarts() {
        if (_prefrence != null)
            return _prefrence.getBoolean("Steps", false);
        return false;
    }


    public void setKgs(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("kgs", isMale).commit();
    }

    public boolean getKgs() {
        if (_prefrence != null)
            return _prefrence.getBoolean("kgs", true);
        return false;
    }

    public void setpound(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("pound", isMale).commit();
    }

    public boolean getPound() {
        if (_prefrence != null)
            return _prefrence.getBoolean("pound", false);
        return false;
    }

    public void setMale(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("Male", isMale).commit();
    }

    public boolean getMale() {
        if (_prefrence != null)
            return _prefrence.getBoolean("Male", false);
        return false;
    }


    public void setFeMale(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("Female", isMale).commit();
    }

    public boolean getFeMale() {
        if (_prefrence != null)
            return _prefrence.getBoolean("Female", false);
        return false;
    }

    public void setGoalAge(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("Age", className).commit();
    }

    public String getGoalAge() {
        if (_prefrence != null)
            return _prefrence.getString("Age", "0");
        return "";
    }


    public void setGoalAgeNew(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("AgeN", className).commit();
    }

    public String getGoalAgeNew() {
        if (_prefrence != null)
            return _prefrence.getString("AgeN", "0");
        return "";
    }


    public void setGoalHeightFeet(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("HeightFeet", className).commit();
    }

    public String getGoalHeightFeet() {
        if (_prefrence != null)
            return _prefrence.getString("HeightFeet", "");
        return "";
    }

//    public void setStepsCount(String className) {
//        if (_prefrence != null)
//            _prefrence.edit().putString("stepsCount", className).commit();
//    }
//
//    public String getStepsCount() {
//        if (_prefrence != null)
//            return _prefrence.getString("stepsCount", "0");
//        return "";
//    }


//    public void setCaloriesCount(String className) {
//        if (_prefrence != null)
//            _prefrence.edit().putString("CaloriesCount", className).commit();
//    }

//    public String getCaloriesCount() {
//        if (_prefrence != null)
//            return _prefrence.getString("CaloriesCount", "0");
//        return "";
//    }


    public void setStepsCountTarget(int className) {
        if (_prefrence != null)
            _prefrence.edit().putInt("stepsCountTarget", className).commit();
    }

    public int getStepsCountTarget() {
        if (_prefrence != null)
            return _prefrence.getInt("stepsCountTarget", 10000);
        return 0;
    }

//    public void setWaterInTake(String className) {
//        if (_prefrence != null)
//            _prefrence.edit().putString("waterTake", className).commit();
//    }

//    public String getWaterInTake() {
//        if (_prefrence != null)
//            return _prefrence.getString("waterTake", "0");
//        return "";
//    }


    public void setWaterInTakeLeft(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("waterTakeLeft", className).commit();
    }

    public String getWaterInTakeLeft() {
        if (_prefrence != null)
            return _prefrence.getString("waterTakeLeft", "0");
        return "";
    }


    public void setWaterInTakeTarget(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("waterTakeTarget", className).commit();
    }

    public String getWaterInTakeTarget() {
        if (_prefrence != null)
            return _prefrence.getString("waterTakeTarget", "0");
        return "";
    }

    public void setGoalHeightInch(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("HeightInch", className).commit();
    }

    public String getGoalHeightInch() {
        if (_prefrence != null)
            return _prefrence.getString("HeightInch", "");
        return "";
    }

    public void setGoalHeightCm(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("HeightCm", className).commit();
    }

    public String getGoalHeightCm() {
        if (_prefrence != null)
            return _prefrence.getString("HeightCm", "");
        return "";
    }


    public void setGoalWeightKg(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("WeightKg", className).commit();
    }

    public String getGoalWeightKg() {
        if (_prefrence != null)
            return _prefrence.getString("WeightKg", "");
        return "";
    }


    public void setGoalWeightGrams(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("WeightGrams", className).commit();
    }

    public String getGoalWeightGrams() {
        if (_prefrence != null)
            return _prefrence.getString("WeightGrams", "");
        return "";
    }

    public void setGoalWeightPound(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("WeightPound", className).commit();
    }

    public String getGoalWeightPound() {
        if (_prefrence != null)
            return _prefrence.getString("WeightPound", "");
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


    public void set_ip(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("ip", className).commit();
    }

    public String get_ip() {
        if (_prefrence != null)
            return _prefrence.getString("ip", "");
        return "";
    }

    public void setPrescriptionSize(int password) {
        if (_prefrence != null)
            _prefrence.edit().putInt("presciption", password).commit();
    }

    public int getPrescriptionSize() {
        if (_prefrence != null)
            return _prefrence.getInt("presciption", 0);
        return 0;
    }


    public void setDelete(boolean isMale) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("delete", isMale).commit();
    }

    public boolean getDelete() {
        if (_prefrence != null)
            return _prefrence.getBoolean("delete", false);
        return false;
    }

   /* public void setDateClicked(boolean isDateClicked) {
        if (_prefrence != null)
            _prefrence.edit().putBoolean("delete", isDateClicked).commit();
    }

    public boolean getDateClicked() {
        if (_prefrence != null)
            return _prefrence.getBoolean("delete", true);
        return false;
    }*/

    public void setFilterDoctor(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDoctor", className).commit();
    }

    public String getFilterDoctor() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDoctor", "");
        return "";
    }

    public void setFilterDate(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDate", className).commit();
    }

    public String getFilterDate() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDate", "");
        return "";
    }

    public void setFilterDiese(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDiese", className).commit();
    }

    public String getFilterDiese() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDiese", "");
        return "";
    }


    public void setFilterUploadBy(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("UploadBy", className).commit();
    }

    public String getFilterUploadBy() {
        if (_prefrence != null)
            return _prefrence.getString("UploadBy", "");
        return "";
    }


    public void setFilterDoctorReports(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDoctorReports", className).commit();
    }

    public String getFilterDoctorReports() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDoctorReports", "");
        return "";
    }

    public void setFilterDateReports(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDateReports", className).commit();
    }

    public String getFilterDateReports() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDateReports", "");
        return "";
    }

    public void setFilterDieseReports(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("FilterDieseReports", className).commit();
    }

    public String getFilterDieseReports() {
        if (_prefrence != null)
            return _prefrence.getString("FilterDieseReports", "");
        return "";
    }


    public void setFilterUploadByReports(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("UploadByReports", className).commit();
    }

    public String getFilterUploadByReports() {
        if (_prefrence != null)
            return _prefrence.getString("UploadByReports", "");
        return "";
    }


    public void setGraphDate(String className) {
        if (_prefrence != null)
            _prefrence.edit().putString("GraphDate", className).commit();
    }

    public String getGraphDate() {
        if (_prefrence != null)
            return _prefrence.getString("GraphDate", "");
        return "";
    }


    public void setFragmentHealthApp(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("HealthApp", isLogin).commit();
        }
    }

    public boolean isFragmentHealthApp() {
        if (_prefrence != null)
            return _prefrence.getBoolean("HealthApp", false);
        return false;
    }

    public void setFragmentHealthNote(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("HealthNote", isLogin).commit();
        }
    }

    public boolean isFragmentHealthNote() {
        if (_prefrence != null)
            return _prefrence.getBoolean("HealthNote", false);
        return false;
    }


    public void setFragmentHealthpre(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("HealthPre", isLogin).commit();
        }
    }

    public boolean isFragmentHealtpre() {
        if (_prefrence != null)
            return _prefrence.getBoolean("HealthPre", false);
        return false;
    }

    public void setFragmentHealthReprts(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("HealthReprts", isLogin).commit();
        }
    }

    public boolean isFragmentHealtReprts() {
        if (_prefrence != null)
            return _prefrence.getBoolean("HealthReprts", false);
        return false;
    }


    public void setFragmentMedicine(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("Medicine", isLogin).commit();
        }
    }

    public boolean isFragmentMedicine() {
        if (_prefrence != null)
            return _prefrence.getBoolean("Medicine", false);
        return false;
    }


    public void setFragmentDoctorVisit(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("DoctorVisit", isLogin).commit();
        }
    }

    public boolean isFragmentDoctorVisit() {
        if (_prefrence != null)
            return _prefrence.getBoolean("DoctorVisit", false);
        return false;
    }

    public void setFragmentLabTst(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("LabTs", isLogin).commit();
        }
    }

    public boolean isFragmentLabTs() {
        if (_prefrence != null)
            return _prefrence.getBoolean("LabTs", false);
        return false;
    }


    public void setIsFirstTimeNotifictaion(boolean isLogin) {
        if (_prefrence != null) {
            _prefrence.edit().putBoolean("Notifictaion", isLogin).commit();
        }
    }

    public boolean isFirstTimeStepsNotifictaion() {
        if (_prefrence != null)
            return _prefrence.getBoolean("Notifictaion", true);
        return false;
    }

}



