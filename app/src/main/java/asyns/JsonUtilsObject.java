package asyns;

import android.graphics.Bitmap;
import android.provider.ContactsContract;
import android.util.Base64;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import item.property.EduationDetails;
import item.property.LabReportImageList;
import item.property.PrescriptionImageList;
import utils.MyConstants;

import static android.R.attr.bitmap;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class JsonUtilsObject implements MyConstants.JsonUtils {


    public static JSONObject toLogin(String userName, String password) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(USER_NAME, userName.trim());
            jsonParent.put(PASSWORD, password.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toUHID(String name, String mobile, String emailId) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("name", name);
            jsonParent.put("mobileNumber", mobile.trim());
            jsonParent.put("emailId", emailId.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toForgotPassword(String mobileNumber, String password) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("mobileNumber", mobileNumber.trim());
            jsonParent.put(PASSWORD, password.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toForgotPasswordEmail(String mobileNumber, String password) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("userId", mobileNumber.trim());
            jsonParent.put(PASSWORD, password.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toUHIDADD(String name, String mobileNumber) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("name", name);
            jsonParent.put("mobileNumber", mobileNumber.trim());


        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSignUp(String userName, String email, String password, String mobileNo, String uhid) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put(NAME, userName);
            jsonParent.put(EMAIL, email.trim());
            jsonParent.put(PASSWORD, password.trim());
            jsonParent.put(MOBILE_NO, mobileNo.trim());
            Log.e("cf", "" + uhid);
            if ("null".equalsIgnoreCase(uhid)) {
                jsonParent.put("cfUuhid", "");
            } else {
                jsonParent.put("cfUuhid", uhid);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toAddHealthNote(String subject, String details, String date, String fromTime, String toTime) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("subject", subject.trim());
            jsonParent.put("details", details.trim());
            jsonParent.put("date", date.trim());
            jsonParent.put("fromTime", fromTime.trim());
            jsonParent.put("toTime", toTime.trim());


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
            if (gender.equalsIgnoreCase("male")) {
                gender = "MALE";
            } else if (gender.equalsIgnoreCase("female")) {
                gender = "FEMALE";
            } else {
                gender = "OTHER";
            }
            jsonParent.put("gender", gender);
            if (relationshipStatus.equalsIgnoreCase("single")) {
                relationshipStatus = "SINGLE";
            } else {
                relationshipStatus = "MARRIED";
            }
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
            jsonParent.put("educationDetails", obj1);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSetGoals(String targetSteps, String targetCaloriesToBurn, String targetWaterInTake) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("targetSteps", targetSteps.trim());
            jsonParent.put("targetCaloriesToBurn", targetCaloriesToBurn.trim());
            jsonParent.put("targetWaterInTake", targetWaterInTake.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSetGoalsDetails(String height, String weight, String dateOfBirth, String gender) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("height", height.trim());
            jsonParent.put("weight", weight.trim());
            jsonParent.put("dateOfBirth", dateOfBirth.trim());
            jsonParent.put("gender", gender.trim());
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSaveHealthAppDetails(String steps, String running, String cycling, String waterIntake, String date, String time) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("steps", steps.trim());
            jsonParent.put("running", running.trim());
            jsonParent.put("cycling", cycling.trim());
            jsonParent.put("waterIntake", waterIntake.trim());
            jsonParent.put("date", date.trim());
            jsonParent.put("time", time.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject prescriptionUpload(String date, String doctorName, String disease, List<PrescriptionImageList> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("date", date);
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("disease", disease);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
                    Log.e("number:- ", "" + prescriptionImageList.get(i).getImageNumber());
//                    list1.put("prescriptionImage", changeTOBase64(prescriptionImageList.get(i).getPrescriptionImage()));
                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("prescriptionImageList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject getGraphDeatils(String fromDate, String date, String frequency, String type) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("fromDate", fromDate.trim());
            jsonParent.put("toDate", date.trim());
            jsonParent.put("frequency", frequency.trim());
            jsonParent.put("type", type.trim());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonParent;
    }


    public static JSONObject getProfileDeatils(String name, String mobileNumber, String emailId, String oldPassword, String newPassword) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("name", name);
            jsonParent.put("mobileNumber", mobileNumber.trim());
            jsonParent.put("emailId", emailId.trim());
            jsonParent.put("oldPassword", oldPassword.trim());
            jsonParent.put("newPassword", newPassword.trim());

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject LabReportUpload(String date, String doctorName, String testName, List<PrescriptionImageList> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("date", date);
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("testName", testName);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
//                    list1.put("reportImage", changeTOBase64(prescriptionImageList.get(i).getPrescriptionImage()));
                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("reportImageList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }



    public static String changeTOBase64(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 10, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
        return encoded;
    }


}
