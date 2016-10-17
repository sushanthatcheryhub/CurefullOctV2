package asyns;

import android.provider.ContactsContract;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

import item.property.EduationDetails;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class JsonUtilsObject implements MyConstants.PrefrenceKeys {


    public static JSONObject toLogin(String userName, String password) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(USER_NAME, userName);
            jsonParent.put(PASSWORD, password);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSignUp(String userName, String email, String password) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(NAME, userName);
            jsonParent.put(EMAIL, email);
            jsonParent.put(PASSWORD, password);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSignUpFacebook(String facebookId, String name, String emailId, String dateOfBirth, String gender, String relationshipStatus, String profileImageUrl, String devices, ArrayList<EduationDetails> eduationDeatilsResults) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("facebookId", facebookId);
            jsonParent.put("name", name);
            jsonParent.put("emailId", emailId);
            jsonParent.put("dateOfBirth", dateOfBirth);
            jsonParent.put("gender", gender);
            jsonParent.put("relationshipStatus", relationshipStatus);
            jsonParent.put("profileImageUrl", profileImageUrl);
            jsonParent.put("devices", devices);

            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < eduationDeatilsResults.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("instituteName", eduationDeatilsResults.get(i).getInstituteName());
                    list1.put("instituteType", eduationDeatilsResults.get(i).getInstituteType());
                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toForgotPassword(String resendPasswordId, String resendPasswordType) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(RESEND_ID, resendPasswordId);
            jsonParent.put(RESEND_TYPE, resendPasswordType);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toAllAutoCompleteText(String userId, String userRole) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("userId", userId);
            jsonParent.put("userRole", userRole);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toAddSymptoms(String newSymptoms, String doctorId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("newSymptoms", newSymptoms);
            jsonParent.put("doctorId", doctorId);
            jsonParent.put("isMostUsed", true);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toRemoveSymptoms(String newSymptoms, String doctorId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("symptoms", newSymptoms);
            jsonParent.put("doctorId", doctorId);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toRemoveSigns(String newSymptoms, String doctorId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("signs", newSymptoms);
            jsonParent.put("doctorId", doctorId);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toAddSigns(String newSymptoms, String doctorId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("sign", newSymptoms);
            jsonParent.put("doctorId", doctorId);
            jsonParent.put("isMostUsed", true);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toAddDiagonis(String newSymptoms, String doctorId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("diagnosis", newSymptoms);
            jsonParent.put("doctorId", doctorId);
            jsonParent.put("isMostUsed", true);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toGetPatientList(int userId, String user_Role) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(USER_ID, userId);
            jsonParent.put(USER_ROLE, user_Role);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toGetAddPatientListDone(String firstName, String gender, String emailId, String state, String city, String pincode, String country, String mobileNo, String age, int heightFeet, int heightInch, int weightKg, int weightGm, String bodyTemperature, String pulseRate, String prescriptionType, String dateOfBirth, String street, String appointmentDate, int appointmentId, int doctorServiceProviderId, int bloodPressure) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("firstName", firstName);
            jsonParent.put("gender", gender);
            jsonParent.put("emailId", emailId);
            jsonParent.put("state", state);
            jsonParent.put("city", city);
            jsonParent.put("pincode", pincode);
            jsonParent.put("country", country);
            jsonParent.put("mobileNo", mobileNo);
            jsonParent.put("age", age);
            jsonParent.put("heightFeet", heightFeet);
            jsonParent.put("heightInch", heightInch);
            jsonParent.put("weightGm", weightGm);
            jsonParent.put("weightGm", weightGm);
            jsonParent.put("bodyTemperature", bodyTemperature);
            jsonParent.put("pulseRate", pulseRate);
            jsonParent.put("prescriptionType", prescriptionType);
            jsonParent.put("dateOfBirth", dateOfBirth);
            jsonParent.put("street", street);
            jsonParent.put("appointmentDate", appointmentDate);
            jsonParent.put("appointmentId", appointmentId);
            jsonParent.put("doctorServiceProviderId", doctorServiceProviderId);
            jsonParent.put("bloodPressure", bloodPressure);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toGetAddPatientListSave(String firstName, String gender, String emailId, String state, String city, String pincode, String country, String mobileNo, String dateOfBirth, String street, String appointmentDate, int doctorServiceProviderId) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("firstName", firstName);
            jsonParent.put("gender", gender);
            jsonParent.put("emailId", emailId);
            jsonParent.put("state", state);
            jsonParent.put("city", city);
            jsonParent.put("pincode", pincode);
            jsonParent.put("country", country);
            jsonParent.put("mobileNo", mobileNo);
            jsonParent.put("dateOfBirth", dateOfBirth);
            jsonParent.put("street", street);
            jsonParent.put("appointmentDate", appointmentDate);
            jsonParent.put("doctorServiceProviderId", doctorServiceProviderId);
            jsonParent.put("cfuhhid", "");

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toUpdatePendingPhyicalData(int heightFeet, int heightInch, int weightKg, int weightGm, String bloodPressureKgs, String bloodPressureGms, String bodyTemperature, int patientDoctorId) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("heightFeet", heightFeet);
            jsonParent.put("heightInch", heightInch);
            jsonParent.put("weightKg", weightKg);
            jsonParent.put("weightGm", weightGm);
            jsonParent.put("bodyTemperature", bodyTemperature);
            jsonParent.put("bloodPressureKgs", bloodPressureKgs);
            jsonParent.put("bloodPressureGms", bloodPressureGms);
            jsonParent.put("patientDoctorId", patientDoctorId);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toPatientListDashBorad(String doctorHealhcareProviderId, String pageNo, String noOfRecord, String date) {


        Log.e("date", ":- " + date);
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("doctorHealhcareProviderId", doctorHealhcareProviderId);
            jsonParent.put("pageNo", pageNo);
            jsonParent.put("noOfRecord", noOfRecord);
            jsonParent.put("appointmentDate", date);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toDoctorCommonDeatils(String userId, String userRole) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("userId", userId);
            jsonParent.put("userRole", userRole);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSendReferToDoctor(int fromDoctorId, String hospitalName, String specilaization, String DoctorName, String note, String referDoctorEmailId, boolean SharePrescription, String ePrescriptionId, boolean CurefullDoctor, int toDoctorId, int refererPatientId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("fromDoctorId", fromDoctorId);
            jsonParent.put("hospitalName", hospitalName);
            jsonParent.put("specilaization", specilaization);
            jsonParent.put("DoctorName", DoctorName);
            jsonParent.put("note", note);
            jsonParent.put("referDoctorEmailId", referDoctorEmailId);
            jsonParent.put("SharePrescription", SharePrescription);
            jsonParent.put("ePrescriptionId", ePrescriptionId);
            jsonParent.put("CurefullDoctor", CurefullDoctor);
            jsonParent.put("toDoctorId", toDoctorId);
            jsonParent.put("refererPatientId", refererPatientId);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toPrescriptionHistoryFullImage(String imageId) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("patientDoctorId", "");
            jsonParent.put("prescriptionId", "");
            jsonParent.put("ePrescriptionId", "");
            jsonParent.put("imageId", imageId);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toPrescriptionHistoryByDate(String id, String shortBy) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("patientDoctorId", id);
            jsonParent.put("sortBy", shortBy);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toUploadSimgleFile(File file) {

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("file", file);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

}
