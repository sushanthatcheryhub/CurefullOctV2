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

import item.property.Doctor_Visit_Reminder_DoctorListView;
import item.property.Doctor_Visit_Reminder_SelfListView;
import item.property.EduationDetails;
import item.property.HealthNoteItems;
import item.property.LabReportImageList;
import item.property.LabReportImageListView;
import item.property.Lab_Test_Reminder_DoctorListView;
import item.property.Lab_Test_Reminder_SelfListView;
import item.property.MedicineReminderItem;
import item.property.PrescriptionImageList;
import item.property.PrescriptionImageListView;
import item.property.ReminderMedicnceDoagePer;
import item.property.Reminder_DoctorListView;
import item.property.Reminder_SelfListView;
import utils.AppPreference;
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


    public static JSONObject toAddOfflineHealthNote(List<HealthNoteItems> healthNoteItemsesDummy) {
        JSONObject jsonParent = new JSONObject();
        JSONArray obj1 = new JSONArray();
        try {
            for (int i = 0; i < healthNoteItemsesDummy.size(); i++) {
                JSONObject list1 = new JSONObject();
                list1.put("subject", healthNoteItemsesDummy.get(i).getNote_heading());
                list1.put("details", healthNoteItemsesDummy.get(i).getDeatils());
                list1.put("date", healthNoteItemsesDummy.get(i).getNote_date());
                list1.put("fromTime", healthNoteItemsesDummy.get(i).getNote_time());
                list1.put("toTime", healthNoteItemsesDummy.get(i).getNote_to_time());
                obj1.put(list1);
            }
            jsonParent.put("healthNoteList", obj1);
        } catch (JSONException e1) {
            e1.printStackTrace();
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


    public static JSONObject toSetGoals(String targetSteps, String targetCaloriesToBurn, double targetWaterInTake) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("targetSteps", targetSteps.trim());
            jsonParent.put("targetCaloriesToBurn", targetCaloriesToBurn.trim());
            jsonParent.put("targetWaterInTake", targetWaterInTake);

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


    public static JSONObject toSaveHealthAppDetails(String steps, String running, String cycling, String waterIntake, String caloriesBurnt, String date, String time) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("steps", steps.trim());
            jsonParent.put("running", running.trim());
            jsonParent.put("cycling", cycling.trim());
            jsonParent.put("waterIntake", waterIntake.trim());
            jsonParent.put("caloriesBurnt", caloriesBurnt);
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


    public static JSONObject setRemMedAdd(String startFrom, String duration, String doages, String noOfDayInweek, ArrayList<MedicineReminderItem> listCurrent, String alarmTime, double interval) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
            JSONObject jsonParent1 = new JSONObject();
            jsonParent1.put("startDate", startFrom);
            jsonParent1.put("noOfDays", duration);
            jsonParent1.put("unitOfInterval", "hours");
            jsonParent1.put("interval", interval);
            jsonParent1.put("alramTime", alarmTime);
            jsonParent1.put("noOfDosage", doages);
            jsonParent1.put("noOfDayInWeek", noOfDayInweek);

            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < listCurrent.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("medicineType", listCurrent.get(i).getType());
                    list1.put("medicineName", listCurrent.get(i).getMedicineName());
                    list1.put("doctorName", listCurrent.get(i).getDoctorName());
                    list1.put("quantityType", "Mg");
                    list1.put("medicinePotency", "600 mg");
                    list1.put("medicineQuantity", listCurrent.get(i).getInterval());
                    list1.put("isAtferMeal", listCurrent.get(i).isBaMealAfter());
                    list1.put("isBeforeMeal", listCurrent.get(i).isBaMealBefore());
                    list1.put("isSelf", true);
                    list1.put("status", "pending");///doubt
                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent1.put("medicineReminderDetailsRequest", obj1);
            jsonParent.put("medicineScheduleRequest", jsonParent1);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject setRemMedAddLocal(List<Reminder_SelfListView> medicinereminder,String chk_self_or_digi) {
//String startFrom, String duration, String doages, String noOfDayInweek, ArrayList<MedicineReminderItem> listCurrent, String alarmTime, double interval, String cfuuhid, String status, String medicineReminderId, ArrayList<ReminderMedicnceDoagePer> reminderMedicnceDoagePer,
        boolean isAndroidPrimaryKey = true;

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("cfuuhId", medicinereminder.get(0).getCfuuhId());
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < medicinereminder.size(); i++) {
                    if (medicinereminder.get(i).getMedicineReminderId().length() > 7) {
                        isAndroidPrimaryKey = true;
                    } else {
                        isAndroidPrimaryKey = false;
                    }


                    String time = "";
                    String hour = "";
                    String minute = "";
                    String[] alarm_time = medicinereminder.get(i).getAlarmTime().split(",");

                    int sizee = alarm_time.length;
                    for (int y = 0; y < sizee; y++) {
                        String[] timee = alarm_time[y].split(":");
                        String hourr = timee[0];
                        String minutee = timee[1];
                        if (Integer.parseInt(hourr) < 10) {

                            hour = "0" + Integer.parseInt(hourr);
                        } else {
                            hour = "" + Integer.parseInt(hourr);
                        }

                        if (Integer.parseInt(minutee) < 10) {

                            minute = "0" + Integer.parseInt(minutee);
                        } else {
                            minute = "" + Integer.parseInt(minutee);
                        }

                        if (y == (sizee - 1)) {
                            time += hour + ":" + minute;
                        } else {
                            time += hour + ":" + minute + ",";
                        }
                    }
                    String monthh = "";
                    String dayy = "";

                    if (medicinereminder.get(i).getMonth() < 10) {

                        monthh = "0" + medicinereminder.get(i).getMonth();
                    } else {
                        monthh = "" + medicinereminder.get(i).getMonth();
                    }

                    if (medicinereminder.get(i).getDate() < 10) {

                        dayy = "0" + medicinereminder.get(i).getDate();
                    } else {
                        dayy = "" + medicinereminder.get(i).getDate();
                    }


                    JSONObject list1 = new JSONObject();
                    list1.put("medicineReminderId", Long.parseLong(medicinereminder.get(i).getMedicineReminderId()));
                    list1.put("isAndroidPrimaryKey", isAndroidPrimaryKey);
                    list1.put("status", medicinereminder.get(i).getStatus());
                    list1.put("medicineType", medicinereminder.get(i).getType());
                    list1.put("medicineName", medicinereminder.get(i).getRemMedicineName());
                    list1.put("doctorName", medicinereminder.get(i).getDoctorName());
                    //list1.put("quantityType", "Mg");
                    list1.put("medicinePotency", "600 mg");
                    list1.put("medicineQuantity",String.valueOf( medicinereminder.get(i).getInterval()));
                    list1.put("isAtferMeal", medicinereminder.get(i).isAfterMeal());
                    list1.put("isBeforeMeal", medicinereminder.get(i).isBeforeMeal());

                    list1.put("alramTime", medicinereminder.get(i).getAlarmTime());
                    list1.put("noOfDosage", medicinereminder.get(i).getNoOfDosage());
                    list1.put("noOfDayInWeek", medicinereminder.get(i).getNoOfDaysInWeek());
                    list1.put("noOfDays", medicinereminder.get(i).getNoOfDays());
                    list1.put("unitOfInterval", "hours");
                    list1.put("interval", medicinereminder.get(i).getInterval());
                    list1.put("startDate", medicinereminder.get(i).getYear() + "-" + monthh + "-" + dayy);
                    if(chk_self_or_digi.equalsIgnoreCase("1")){
                        list1.put("isSelf", true);
                    }else{
                        list1.put("isSelf", false);
                    }
                    JSONArray obj2 = new JSONArray();
                    JSONObject jsonParent2 = new JSONObject();
                    if ( medicinereminder.get(i).getReminderMedicnceDoagePers().size() == 0) {
                        JSONArray arrnull=new JSONArray();
                        /*JSONObject objnull=null;
                        arrnull.put(objnull);*/
                        list1.put("dosageDetailsRequest", arrnull);


                    } else {

                        for (int sec = 0; sec < medicinereminder.get(i).getReminderMedicnceDoagePers().size(); sec++) {
                            //SONObject list2 = new JSONObject();
                            jsonParent2.put("dosageDate", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getDate());
                            jsonParent2.put("dosageStatus", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getStatus());

                            obj2.put(jsonParent2);

                            JSONArray obj3 = new JSONArray();
                            for (int thir = 0; thir < medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().size(); thir++) {

                                JSONObject list3 = new JSONObject();
                                list3.put("status", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getStatus());
                                list3.put("time", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getHour() + ":" + medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getMinute());


                                obj3.put(list3);
                                jsonParent2.put("perDosageDetailsRequest", obj3);
                            }
                            list1.put("dosageDetailsRequest", obj2);
                        }
                    }

                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            jsonParent.put("medicienReminderRequest", obj1);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject setRemMedAddDigitization(List<Reminder_DoctorListView> medicinereminder, String chk_self_or_digi) {
//String startFrom, String duration, String doages, String noOfDayInweek, ArrayList<MedicineReminderItem> listCurrent, String alarmTime, double interval, String cfuuhid, String status, String medicineReminderId, ArrayList<ReminderMedicnceDoagePer> reminderMedicnceDoagePer,
        boolean isAndroidPrimaryKey = true;

        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("cfuuhId", medicinereminder.get(0).getCfuuhId());
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < medicinereminder.size(); i++) {
                    if (medicinereminder.get(i).getMedicineReminderId().length() > 7) {
                        isAndroidPrimaryKey = true;
                    } else {
                        isAndroidPrimaryKey = false;
                    }


                    String time = "";
                    String hour = "";
                    String minute = "";
                    String[] alarm_time = medicinereminder.get(i).getAlarmTime().split(",");

                    int sizee = alarm_time.length;
                    for (int y = 0; y < sizee; y++) {
                        String[] timee = alarm_time[y].split(":");
                        String hourr = timee[0];
                        String minutee = timee[1];
                        if (Integer.parseInt(hourr) < 10) {

                            hour = "0" + Integer.parseInt(hourr);
                        } else {
                            hour = "" + Integer.parseInt(hourr);
                        }

                        if (Integer.parseInt(minutee) < 10) {

                            minute = "0" + Integer.parseInt(minutee);
                        } else {
                            minute = "" + Integer.parseInt(minutee);
                        }

                        if (y == (sizee - 1)) {
                            time += hour + ":" + minute;
                        } else {
                            time += hour + ":" + minute + ",";
                        }
                    }
                    String monthh = "";
                    String dayy = "";

                    if (medicinereminder.get(i).getMonth() < 10) {

                        monthh = "0" + medicinereminder.get(i).getMonth();
                    } else {
                        monthh = "" + medicinereminder.get(i).getMonth();
                    }

                    if (medicinereminder.get(i).getDate() < 10) {

                        dayy = "0" + medicinereminder.get(i).getDate();
                    } else {
                        dayy = "" + medicinereminder.get(i).getDate();
                    }


                    JSONObject list1 = new JSONObject();
                    list1.put("medicineReminderId",  Long.parseLong(medicinereminder.get(i).getMedicineReminderId()));
                    list1.put("isAndroidPrimaryKey", isAndroidPrimaryKey);
                    list1.put("status", medicinereminder.get(i).getStatus());
                    list1.put("medicineType", medicinereminder.get(i).getType());
                    list1.put("medicineName", medicinereminder.get(i).getRemMedicineName());
                    list1.put("doctorName", medicinereminder.get(i).getDoctorName());
                    //list1.put("quantityType", "Mg");
                    list1.put("medicinePotency", "600 mg");
                    list1.put("medicineQuantity",String.valueOf( medicinereminder.get(i).getInterval()));
                    list1.put("isAtferMeal", medicinereminder.get(i).isAfterMeal());
                    list1.put("isBeforeMeal", medicinereminder.get(i).isBeforeMeal());

                    list1.put("alramTime", medicinereminder.get(i).getAlarmTime());
                    list1.put("noOfDosage", medicinereminder.get(i).getNoOfDosage());
                    list1.put("noOfDayInWeek", medicinereminder.get(i).getNoOfDaysInWeek());
                    list1.put("noOfDays", medicinereminder.get(i).getNoOfDays());
                    list1.put("unitOfInterval", "hours");
                    list1.put("interval", medicinereminder.get(i).getInterval());
                    list1.put("startDate", medicinereminder.get(i).getYear() + "-" + monthh + "-" + dayy);
                    if(chk_self_or_digi.equalsIgnoreCase("1")){
                        list1.put("isSelf", true);
                    }else{
                        list1.put("isSelf", false);
                    }
                    JSONArray obj2 = new JSONArray();
                    JSONObject jsonParent2 = new JSONObject();
                    if ( medicinereminder.get(i).getReminderMedicnceDoagePers().size() == 0) {

                        JSONArray arrnull=new JSONArray();

                        list1.put("dosageDetailsRequest", arrnull);


                    } else {

                        for (int sec = 0; sec < medicinereminder.get(i).getReminderMedicnceDoagePers().size(); sec++) {
                            //SONObject list2 = new JSONObject();
                            jsonParent2.put("dosageDate", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getDate());
                            jsonParent2.put("dosageStatus", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getStatus());

                            obj2.put(jsonParent2);

                            JSONArray obj3 = new JSONArray();
                            for (int thir = 0; thir < medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().size(); thir++) {

                                JSONObject list3 = new JSONObject();
                                list3.put("status", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getStatus());
                                list3.put("time", medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getHour() + ":" + medicinereminder.get(i).getReminderMedicnceDoagePers().get(sec).getReminderMedicnceTimes().get(thir).getMinute());


                                obj3.put(list3);
                                jsonParent2.put("perDosageDetailsRequest", obj3);
                            }
                            list1.put("dosageDetailsRequest", obj2);
                        }
                    }

                    obj1.put(list1);
                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }

            jsonParent.put("medicienReminderRequest", obj1);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject setRemMedEdit(String medicineReminderId, String startFrom, String duration, String doages, String noOfDayInweek, ArrayList<MedicineReminderItem> listCurrent, String alarmTime, double interval, String status) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("medicineReminderId", medicineReminderId);
            jsonParent.put("startDate", startFrom);
            jsonParent.put("noOfDays", duration);
            jsonParent.put("unitOfInterval", "hours");
            jsonParent.put("interval", interval);
            jsonParent.put("alramTime", alarmTime);
            jsonParent.put("noOfDosage", doages);
            jsonParent.put("noOfDayInWeek", noOfDayInweek);
            jsonParent.put("medicineType", listCurrent.get(0).getType());
            jsonParent.put("medicineName", listCurrent.get(0).getMedicineName());
            jsonParent.put("doctorName", listCurrent.get(0).getDoctorName());
            jsonParent.put("medicinePotency", "600 mg");
            jsonParent.put("medicineQuantity", listCurrent.get(0).getInterval());
            jsonParent.put("isAtferMeal", listCurrent.get(0).isBaMealAfter());
            jsonParent.put("isBeforeMeal", listCurrent.get(0).isBaMealBefore());
            jsonParent.put("status", status);
            jsonParent.put("isSelf", true);
        } catch (JSONException e1) {
            e1.printStackTrace();
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


    public static JSONObject toSetLabTestReminder(String doctorName, String testName, String labName, String labTestDate, String labTestTime, String labTestReminderId, boolean isNewLabTest, boolean isAtterMeal) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("testName", testName);
            jsonParent.put("labName", labName);
            jsonParent.put("labTestDate", labTestDate);
            jsonParent.put("labTestTime", labTestTime);
            jsonParent.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
            jsonParent.put("labTestReminderId", labTestReminderId);
            jsonParent.put("isNewLabTest", isNewLabTest);
            jsonParent.put("isAfterMeal", isAtterMeal);
            jsonParent.put("isSelf", true);
            jsonParent.put("labTestStatus", "pending");
//
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSetLabTestReminderfromLocal(String chk_self_or_digi, List<Lab_Test_Reminder_SelfListView> labreminder) {
        //String doctorName, String testName, String labName, String labTestDate, String labTestTime, String labTestReminderId, boolean isNewLabTest, boolean isAtterMeal, String cfhuuid, String status,
        JSONObject jsonParent = new JSONObject();
        JSONArray arr = new JSONArray();
        try {

            jsonParent.put("cfuuhId", labreminder.get(0).getCfuuhId());
            for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {
                boolean isNewReminder = false;
                String monthh = "";
                String dayy = "";
                String hour = "";
                String minute = "";

                if (labreminder.get(ilrs).getLabTestReminderId().length() > 7) {
                    isNewReminder = true;
                } else {
                    isNewReminder = false;
                }

                if (labreminder.get(ilrs).getMonth() < 10) {

                    monthh = "0" + labreminder.get(ilrs).getMonth();
                } else {
                    monthh = "" + labreminder.get(ilrs).getMonth();
                }

                if (labreminder.get(ilrs).getDate() < 10) {

                    dayy = "0" + labreminder.get(ilrs).getDate();
                } else {
                    dayy = "" + labreminder.get(ilrs).getDate();
                }

                if (labreminder.get(ilrs).getHour() < 10) {

                    hour = "0" + labreminder.get(ilrs).getHour();
                } else {
                    hour = "" + labreminder.get(ilrs).getHour();
                }

                if (labreminder.get(ilrs).getMintue() < 10) {

                    minute = "0" + labreminder.get(ilrs).getMintue();
                } else {
                    minute = "" + labreminder.get(ilrs).getMintue();
                }

//labreminder.get(ilrs).getDoctorName().toString().trim(), labreminder.get(ilrs).getRemMedicineName().toString().trim(), labreminder.get(ilrs).getLabName().toString().trim(), labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, labreminder.get(ilrs).getLabTestReminderId(), isNewReminder, labreminder.get(ilrs).isAfterMeal(), labreminder.get(ilrs).getCfuuhId(), labreminder.get(ilrs).getStatus()
                JSONObject jsonobjint = new JSONObject();
                jsonobjint.put("doctorName", labreminder.get(ilrs).getDoctorName().toString().trim());
                jsonobjint.put("testName", labreminder.get(ilrs).getRemMedicineName().toString().trim());
                jsonobjint.put("labName", labreminder.get(ilrs).getLabName().toString().trim());
                jsonobjint.put("labTestDate", labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy);
                jsonobjint.put("labTestTime", hour + ":" + minute);
                jsonobjint.put("cfuuhId", labreminder.get(ilrs).getCfuuhId());
                jsonobjint.put("labTestReminderId", labreminder.get(ilrs).getLabTestReminderId());
                jsonobjint.put("labTestStatus", labreminder.get(ilrs).getStatus());
                jsonobjint.put("isNewLabTest", isNewReminder);//edit and medicine id from server side - false and edit and medicine id generated from my side -true
                jsonobjint.put("isAfterMeal", labreminder.get(ilrs).isAfterMeal());
                if (chk_self_or_digi.equalsIgnoreCase("1")) {
                    jsonobjint.put("isSelf", true);//self ya digitization
                } else {
                    jsonobjint.put("isSelf", false);
                }
                arr.put(jsonobjint);
            }
            jsonParent.put("addLabTestReminderRequest", arr);
        } catch (Exception e) {

        }
        return jsonParent;
    }

    public static JSONObject toSetLabTestReminderfromDigitization(String chk_self_or_digi, List<Lab_Test_Reminder_DoctorListView> labreminder) {
        //String doctorName, String testName, String labName, String labTestDate, String labTestTime, String labTestReminderId, boolean isNewLabTest, boolean isAtterMeal, String cfhuuid, String status,
        JSONObject jsonParent = new JSONObject();
        JSONArray arr = new JSONArray();
        try {

            jsonParent.put("cfuuhId", labreminder.get(0).getCfuuhId());
            for (int ilrs = 0; ilrs < labreminder.size(); ilrs++) {
                boolean isNewReminder = false;
                String monthh = "";
                String dayy = "";
                String hour = "";
                String minute = "";

                if (labreminder.get(ilrs).getLabTestReminderId().length() > 7) {
                    isNewReminder = true;
                } else {
                    isNewReminder = false;
                }

                if (labreminder.get(ilrs).getMonth() < 10) {

                    monthh = "0" + labreminder.get(ilrs).getMonth();
                } else {
                    monthh = "" + labreminder.get(ilrs).getMonth();
                }

                if (labreminder.get(ilrs).getDate() < 10) {

                    dayy = "0" + labreminder.get(ilrs).getDate();
                } else {
                    dayy = "" + labreminder.get(ilrs).getDate();
                }

                if (labreminder.get(ilrs).getHour() < 10) {

                    hour = "0" + labreminder.get(ilrs).getHour();
                } else {
                    hour = "" + labreminder.get(ilrs).getHour();
                }

                if (labreminder.get(ilrs).getMintue() < 10) {

                    minute = "0" + labreminder.get(ilrs).getMintue();
                } else {
                    minute = "" + labreminder.get(ilrs).getMintue();
                }

//labreminder.get(ilrs).getDoctorName().toString().trim(), labreminder.get(ilrs).getRemMedicineName().toString().trim(), labreminder.get(ilrs).getLabName().toString().trim(), labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, labreminder.get(ilrs).getLabTestReminderId(), isNewReminder, labreminder.get(ilrs).isAfterMeal(), labreminder.get(ilrs).getCfuuhId(), labreminder.get(ilrs).getStatus()
                JSONObject jsonobjint = new JSONObject();
                jsonobjint.put("doctorName", labreminder.get(ilrs).getDoctorName().toString().trim());
                jsonobjint.put("testName", labreminder.get(ilrs).getRemMedicineName().toString().trim());
                jsonobjint.put("labName", labreminder.get(ilrs).getLabName().toString().trim());
                jsonobjint.put("labTestDate", labreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy);
                jsonobjint.put("labTestTime", hour + ":" + minute);
                jsonobjint.put("cfuuhId", labreminder.get(ilrs).getCfuuhId());
                jsonobjint.put("labTestReminderId", labreminder.get(ilrs).getLabTestReminderId());
                jsonobjint.put("labTestStatus", labreminder.get(ilrs).getStatus());
                jsonobjint.put("isNewLabTest", isNewReminder);//edit and medicine id from server side - false and edit and medicine id generated from my side -true
                jsonobjint.put("isAfterMeal", labreminder.get(ilrs).isAfterMeal());
                if (chk_self_or_digi.equalsIgnoreCase("1")) {
                    jsonobjint.put("isSelf", true);//self ya digitization
                } else {
                    jsonobjint.put("isSelf", false);
                }
                arr.put(jsonobjint);
            }
            jsonParent.put("addLabTestReminderRequest", arr);
        } catch (Exception e) {

        }
        return jsonParent;
    }

    public static JSONObject toSetDoctorVisitReminder(String doctorName, String hospitalName, String follwupDate, String followupTime, String doctorFollowupReminderId, boolean isNewReminder) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("hospitalName", hospitalName);
            jsonParent.put("follwupDate", follwupDate);
            jsonParent.put("followupTime", followupTime);
            jsonParent.put("cfuuhId", AppPreference.getInstance().getcf_uuhid());
            jsonParent.put("doctorFollowupReminderId", doctorFollowupReminderId);
            jsonParent.put("isNewReminder", isNewReminder);
            jsonParent.put("isSelf", true);
            jsonParent.put("status", "pending");
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSetDoctorVisitReminderLocal(String chk_self_or_digit, List<Doctor_Visit_Reminder_SelfListView> doctorreminder) {
        //String doctorName, String hospitalName, String follwupDate, String followupTime, String doctorFollowupReminderId, boolean isNewReminder, String cfuuhid, String status,
        // doctorreminder.get(ilrs).getDoctorName().toString().trim(), doctorreminder.get(ilrs).getRemMedicineName().toString().trim(), doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), isNewReminder, doctorreminder.get(ilrs).getCfuuhId(), doctorreminder.get(ilrs).getStatus(),
        JSONObject jsonParent = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            jsonParent.put("cfuuhId", doctorreminder.get(0).getCfuuhId());
            for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
                boolean isNewReminder = false;
                String monthh = "";
                String dayy = "";
                String hour = "";
                String minute = "";

                if (doctorreminder.get(ilrs).getDoctorFollowupReminderId().length() > 7) {
                    isNewReminder = true;
                } else {
                    isNewReminder = false;
                }

                if (doctorreminder.get(ilrs).getMonth() < 10) {

                    monthh = "0" + doctorreminder.get(ilrs).getMonth();
                } else {
                    monthh = "" + doctorreminder.get(ilrs).getMonth();
                }

                if (doctorreminder.get(ilrs).getDate() < 10) {

                    dayy = "0" + doctorreminder.get(ilrs).getDate();
                } else {
                    dayy = "" + doctorreminder.get(ilrs).getDate();
                }

                if (doctorreminder.get(ilrs).getHour() < 10) {

                    hour = "0" + doctorreminder.get(ilrs).getHour();
                } else {
                    hour = "" + doctorreminder.get(ilrs).getHour();
                }

                if (doctorreminder.get(ilrs).getMintue() < 10) {

                    minute = "0" + doctorreminder.get(ilrs).getMintue();
                } else {
                    minute = "" + doctorreminder.get(ilrs).getMintue();
                }


                JSONObject jsonobjint = new JSONObject();

                jsonobjint.put("doctorName", doctorreminder.get(ilrs).getDoctorName().toString().trim());
                jsonobjint.put("hospitalName", doctorreminder.get(ilrs).getRemMedicineName().toString().trim());
                jsonobjint.put("follwupDate", doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy);
                jsonobjint.put("followupTime", hour + ":" + minute);
                jsonobjint.put("cfuuhId", doctorreminder.get(ilrs).getCfuuhId());
                jsonobjint.put("doctorFollowupReminderId", doctorreminder.get(ilrs).getDoctorFollowupReminderId());
                jsonobjint.put("isNewReminder", isNewReminder);
                if (chk_self_or_digit.equalsIgnoreCase("1")) {
                    jsonobjint.put("isSelf", true);
                } else {
                    jsonobjint.put("isSelf", false);
                }
                jsonobjint.put("status", doctorreminder.get(ilrs).getStatus());

                arr.put(jsonobjint);
            }
            jsonParent.put("addDoctorFollowupReminderRequest", arr);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return jsonParent;
    }


    //for doctor reminder digitization
    public static JSONObject toSetDoctorVisitReminderLocalDigitization(String chk_self_or_digit, List<Doctor_Visit_Reminder_DoctorListView> doctorreminder) {
        //String doctorName, String hospitalName, String follwupDate, String followupTime, String doctorFollowupReminderId, boolean isNewReminder, String cfuuhid, String status,
        // doctorreminder.get(ilrs).getDoctorName().toString().trim(), doctorreminder.get(ilrs).getRemMedicineName().toString().trim(), doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy, hour + ":" + minute, doctorreminder.get(ilrs).getDoctorFollowupReminderId(), isNewReminder, doctorreminder.get(ilrs).getCfuuhId(), doctorreminder.get(ilrs).getStatus(),
        JSONObject jsonParent = new JSONObject();
        JSONArray arr = new JSONArray();
        try {
            jsonParent.put("cfuuhId", doctorreminder.get(0).getCfuuhId());
            for (int ilrs = 0; ilrs < doctorreminder.size(); ilrs++) {
                boolean isNewReminder = false;
                String monthh = "";
                String dayy = "";
                String hour = "";
                String minute = "";

                if (doctorreminder.get(ilrs).getDoctorFollowupReminderId().length() > 7) {
                    isNewReminder = true;
                } else {
                    isNewReminder = false;
                }

                if (doctorreminder.get(ilrs).getMonth() < 10) {

                    monthh = "0" + doctorreminder.get(ilrs).getMonth();
                } else {
                    monthh = "" + doctorreminder.get(ilrs).getMonth();
                }

                if (doctorreminder.get(ilrs).getDate() < 10) {

                    dayy = "0" + doctorreminder.get(ilrs).getDate();
                } else {
                    dayy = "" + doctorreminder.get(ilrs).getDate();
                }

                if (doctorreminder.get(ilrs).getHour() < 10) {

                    hour = "0" + doctorreminder.get(ilrs).getHour();
                } else {
                    hour = "" + doctorreminder.get(ilrs).getHour();
                }

                if (doctorreminder.get(ilrs).getMintue() < 10) {

                    minute = "0" + doctorreminder.get(ilrs).getMintue();
                } else {
                    minute = "" + doctorreminder.get(ilrs).getMintue();
                }


                JSONObject jsonobjint = new JSONObject();

                jsonobjint.put("doctorName", doctorreminder.get(ilrs).getDoctorName().toString().trim());
                jsonobjint.put("hospitalName", doctorreminder.get(ilrs).getRemMedicineName().toString().trim());
                jsonobjint.put("follwupDate", doctorreminder.get(ilrs).getYear() + "-" + monthh + "-" + dayy);
                jsonobjint.put("followupTime", hour + ":" + minute);
                jsonobjint.put("cfuuhId", doctorreminder.get(ilrs).getCfuuhId());
                jsonobjint.put("doctorFollowupReminderId", doctorreminder.get(ilrs).getDoctorFollowupReminderId());
                jsonobjint.put("isNewReminder", isNewReminder);
                if (chk_self_or_digit.equalsIgnoreCase("1")) {
                    jsonobjint.put("isSelf", true);
                } else {
                    jsonobjint.put("isSelf", false);
                }
                jsonobjint.put("status", doctorreminder.get(ilrs).getStatus());

                arr.put(jsonobjint);
            }
            jsonParent.put("addDoctorFollowupReminderRequest", arr);
        } catch (Exception e) {

            e.printStackTrace();
        }
        return jsonParent;
    }

    public static JSONObject toSaveUploadPrescriptionMetadata(String prescriptionDate, String doctorName, String disease) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("prescriptionDate", prescriptionDate);
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("disease", disease);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSaveUploadedPrescriptionData(String prescriptionId, String cfuuhidId, List<PrescriptionImageList> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("prescriptionId", prescriptionId);
            jsonParent.put("cfuuhidId", cfuuhidId);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
                    list1.put("imageUrl", prescriptionImageList.get(i).getPrescriptionImage());
                    //add status in this position
                    obj1.put(list1);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("imageUploadList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSaveUploadedPrescriptionDataToserver(String prescriptionId, String cfuuhidId, ArrayList<PrescriptionImageListView> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("prescriptionId", prescriptionId);
            jsonParent.put("cfuuhidId", cfuuhidId);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
                    list1.put("imageUrl", prescriptionImageList.get(i).getPrescriptionImage());
                    obj1.put(list1);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("imageUploadList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toSaveUploadedLabData(String prescriptionId, String cfuuhidId, ArrayList<LabReportImageListView> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("labReportId", prescriptionId);
            jsonParent.put("cfuuhidId", cfuuhidId);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
                    list1.put("imageUrl", prescriptionImageList.get(i).getReportImage());
                    obj1.put(list1);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("imageUploadList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }
    //Lab Report Upload new

    public static JSONObject toSaveUploadLabReposrtMetadata(String prescriptionDate, String doctorName, String disease) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("labReportDate", prescriptionDate);
            jsonParent.put("doctorName", doctorName);
            jsonParent.put("testName", disease);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toSaveUploadedLabReportData(String prescriptionId, String cfuuhidId, List<PrescriptionImageList> prescriptionImageList) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("labReportId", prescriptionId);
            jsonParent.put("cfuuhidId", cfuuhidId);
            JSONArray obj1 = new JSONArray();
            try {
                for (int i = 0; i < prescriptionImageList.size(); i++) {
                    JSONObject list1 = new JSONObject();
                    list1.put("imageNumber", prescriptionImageList.get(i).getImageNumber());
                    list1.put("imageUrl", prescriptionImageList.get(i).getPrescriptionImage());
                    obj1.put(list1);

                }
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
            jsonParent.put("imageUploadList", obj1);

        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toRegisterUserForNotification(String registrationToken, String deviceId) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("registrationToken", registrationToken);
            jsonParent.put("deviceId", deviceId);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toNotificationMEdincie(String perDayDosageDetailsId, String status) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("perDayDosageDetailsId", perDayDosageDetailsId);
            jsonParent.put("status", status);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toNotificationDoctor(String perDayDosageDetailsId, String status) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("doctorFollowupReminderId", perDayDosageDetailsId);
            jsonParent.put("status", status);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }


    public static JSONObject toNotificationLabTest(String perDayDosageDetailsId, String status) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("labTestReminderId", perDayDosageDetailsId);
            jsonParent.put("status", status);
        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

    public static JSONObject toCotact(String title, String details) {
        JSONObject jsonParent = new JSONObject();
        try {
            jsonParent.put("title", title.trim());
            jsonParent.put("details", details.trim());


        } catch (Exception e) {

            e.printStackTrace();
        }

        return jsonParent;
    }

}
