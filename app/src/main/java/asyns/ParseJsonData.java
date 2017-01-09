package asyns;

import android.content.ContentValues;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import item.property.FilterDataPrescription;
import item.property.FilterDataReports;
import item.property.GoalInfo;
import item.property.GraphYearMonthDeatils;
import item.property.HealthNoteItems;
import item.property.LabDoctorName;
import item.property.LabReportListView;
import item.property.LabTestName;
import item.property.PrescriptionDiseaseName;
import item.property.PrescriptionDoctorName;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import item.property.UHIDItemsCheck;
import item.property.UserInfo;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class ParseJsonData implements MyConstants.JsonUtils {

    private static ParseJsonData data;

    public String getHttp_code() {
        return http_code;
    }

    public void setHttp_code(String http_code) {
        this.http_code = http_code;
    }

    private String http_code;

    private ParseJsonData() {

    }

    public static ParseJsonData getInstance() {
        if (data == null)
            data = new ParseJsonData();
        return data;
    }

    public UserInfo getLoginData(String response) {
        UserInfo user = null;
        try {
            JSONObject json = new JSONObject(response);
            setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
            user = new UserInfo(json);
            ContentValues cv = user.getInsertingValue(json);
            String primaryId = user.getPrimaryId();
            DbOperations operations = new DbOperations();
            operations.insertLoginList(CureFull.getInstanse().getActivityIsntanse(), cv, primaryId);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

//    public SignUpInfo getSignUpData(String response) {
//        SignUpInfo user = null;
//        try {
//            JSONObject json = new JSONObject(response);
//            setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
//            user = new SignUpInfo(json);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return user;
//    }


    public List<HealthNoteItems> getHealthNoteListItem(String response) {
        HealthNoteItems details = null;
        ArrayList<HealthNoteItems> detailListing = null;
        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null")) {
                    JSONArray jsonPayload = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                    detailListing = new ArrayList<HealthNoteItems>();
                    for (int i = 0; i < jsonPayload.length(); i++) {
                        JSONObject jsonObject = jsonPayload.getJSONObject(i);
                        details = new HealthNoteItems(jsonObject);
                        detailListing.add(details);
                        details = new HealthNoteItems();
                        ContentValues cv = details.getInsertingValue(jsonObject);
                        int primaryId = details.getPrimaryId();
                        DbOperations operations = new DbOperations();
                        operations.insertNoteList(CureFull.getInstanse().getActivityIsntanse(), cv, primaryId);
                    }
                }

            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<UHIDItems> getUHID(String response) {
        UHIDItems details = null;
        ArrayList<UHIDItems> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null")) {
                    JSONArray jsonPayload = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                    detailListing = new ArrayList<UHIDItems>();
                    for (int i = 0; i < jsonPayload.length(); i++) {
                        JSONObject jsonObject = jsonPayload.getJSONObject(i);
                        details = new UHIDItems(jsonObject);
                        detailListing.add(details);
                    }
                } else {
                    detailListing = new ArrayList<UHIDItems>();
                }

            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public ArrayList<UHIDItemsCheck> getUHIDCheck(String response) {
        UHIDItemsCheck details = null;
        ArrayList<UHIDItemsCheck> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null")) {
                    JSONArray jsonPayload = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                    detailListing = new ArrayList<UHIDItemsCheck>();
                    for (int i = 0; i < jsonPayload.length(); i++) {
                        JSONObject jsonObject = jsonPayload.getJSONObject(i);
                        details = new UHIDItemsCheck(jsonObject);
                        detailListing.add(details);
                    }
                } else {
                    detailListing = new ArrayList<UHIDItemsCheck>();
                }

            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<PrescriptionListView> getPrescriptionList(String response) {
        PrescriptionListView details = null;
        ArrayList<PrescriptionListView> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null")) {
                    JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                    detailListing = new ArrayList<PrescriptionListView>();
                    for (int i = 0; i < jord.length(); i++) {
                        JSONObject jsonObject = jord.getJSONObject(i);
                        details = new PrescriptionListView(jsonObject);
                        detailListing.add(details);
                    }
                }


            } catch (Exception e) {

            }
        }
        return detailListing;
    }

    public List<LabReportListView> getLabTestReportList(String response) {
        LabReportListView details = null;
        ArrayList<LabReportListView> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null")) {
                    JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                    detailListing = new ArrayList<LabReportListView>();
                    for (int i = 0; i < jord.length(); i++) {
                        JSONObject jsonObject = jord.getJSONObject(i);
                        details = new LabReportListView(jsonObject);
                        detailListing.add(details);
                    }
                } else {
                    detailListing = new ArrayList<LabReportListView>();
                }

            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<GraphYearMonthDeatils> getGraphViewList(String response) {
        GraphYearMonthDeatils details = null;
        ArrayList<GraphYearMonthDeatils> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<GraphYearMonthDeatils>();
                for (int i = 0; i < jord.length(); i++) {
                    JSONObject jsonObject = jord.getJSONObject(i);
                    details = new GraphYearMonthDeatils(jsonObject);
                    detailListing.add(details);
                }
            } catch (Exception e) {

            }
        }
        return detailListing;
    }

    public List<PrescriptionDoctorName> getDoctorName(String response) {
        PrescriptionDoctorName details = null;
        ArrayList<PrescriptionDoctorName> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<PrescriptionDoctorName>();
                for (int i = 0; i < jord.length(); i++) {
                    details = new PrescriptionDoctorName(jord.get(i).toString());
                    detailListing.add(details);
                }
            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<PrescriptionDiseaseName> getDiseaseName(String response) {
        PrescriptionDiseaseName details = null;
        ArrayList<PrescriptionDiseaseName> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<PrescriptionDiseaseName>();
                for (int i = 0; i < jord.length(); i++) {
                    details = new PrescriptionDiseaseName(jord.get(i).toString());
                    detailListing.add(details);
                }
            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<LabDoctorName> getLabDoctorName(String response) {
        LabDoctorName details = null;
        ArrayList<LabDoctorName> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<LabDoctorName>();
                for (int i = 0; i < jord.length(); i++) {
                    details = new LabDoctorName(jord.get(i).toString());
                    detailListing.add(details);
                }
            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<LabTestName> getLabTestName(String response) {
        LabTestName details = null;
        ArrayList<LabTestName> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<LabTestName>();
                for (int i = 0; i < jord.length(); i++) {
                    details = new LabTestName(jord.get(i).toString());
                    detailListing.add(details);
                }
            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public GoalInfo getGoalDeatils(String response) {
        GoalInfo user = null;
        try {
            JSONObject json = new JSONObject(response);
            setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
            user = new GoalInfo();
            ContentValues cv = user.getInsertingValue(json);
            String primaryId = user.getPrimaryId();
            DbOperations operations = new DbOperations();
            operations.insertEditGoalList(CureFull.getInstanse().getActivityIsntanse(), cv, primaryId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }


    public FilterDataPrescription getFilterDataPre(String response) {
        FilterDataPrescription details = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONObject jord = new JSONObject(json.getString(JSON_KEY_PAYLOAD));
                details = new FilterDataPrescription(jord);
            } catch (Exception e) {

            }
        }
        return details;
    }

    public FilterDataReports getFilterDataReports(String response) {
        FilterDataReports details = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONObject jord = new JSONObject(json.getString(JSON_KEY_PAYLOAD));
                details = new FilterDataReports(jord);
            } catch (Exception e) {

            }
        }
        return details;
    }
}
