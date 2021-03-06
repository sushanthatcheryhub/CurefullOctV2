package asyns;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import item.property.DoctorVistReminderListView;
import item.property.FilterDataPrescription;
import item.property.FilterDataReports;
import item.property.GoalInfo;
import item.property.GraphYearMonthDeatils;
import item.property.GraphYearMonthDeatilsList;
import item.property.HealthNoteItems;
import item.property.LabDoctorName;
import item.property.LabReportListView;
import item.property.LabTestName;
import item.property.LabTestReminderListView;
import item.property.MedicineReminderListView;
import item.property.PrescriptionDiseaseName;
import item.property.PrescriptionDoctorName;
import item.property.PrescriptionImageList;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import item.property.UHIDItemsCheck;
import item.property.UserInfo;
import operations.DbOperations;
import utils.AppPreference;
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
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null") || !json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("No Data")) {
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

                        ContentValues cv = details.getInsertingUHIDValue(jsonObject);
                        String primaryId = details.getPrimaryId();
                        DbOperations operations = new DbOperations();
                        operations.insertUHIDListLocal(CureFull.getInstanse().getActivityIsntanse(), cv, primaryId);

                    }
                } else {
                    detailListing = new ArrayList<UHIDItems>();
                }

            } catch (Exception e) {

            }
        }


        return detailListing;
    }

    public void getUHIDUpdate(String cfUuhid) {

        if (cfUuhid != null) {
            try {
                ContentValues values = new ContentValues();
                ContentValues values0 = new ContentValues();
                values.put(SELECTED, 1);
                DbOperations operations = new DbOperations();
                //operations.insertUHIDListLocal(CureFull.getInstanse().getActivityIsntanse(), values, cfUuhid);
                values0.put(SELECTED, 0);
                operations.insertUHIDListLocalUpdateSelected(CureFull.getInstanse().getActivityIsntanse(), values, cfUuhid, values0);

            } catch (Exception e) {

            }
        }

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

                        //by sourav
                        /*ContentValues cv = new ContentValues();
                        cv.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                        cv.put("prescription_data", response.toString());
                        DbOperations.insertPrescriptionList(CureFull.getInstanse().getActivityIsntanse(), cv, AppPreference.getInstance().getcf_uuhidNeew());
                        */
                        details.getInsertingValue(jsonObject);//for local db


                    }
                }


            } catch (Exception e) {
            e.getMessage();
                Log.e("eeexception",e.getMessage());
            }
        }
        return detailListing;
    }


    public List<PrescriptionListView> getPrescriptionListImage(String prescriptionDate, String doctorName, String dieaseName, List<PrescriptionImageList> prescriptionImageListss, int countofFileslocal, String uploadedBy) {
        ArrayList<PrescriptionListView> arr_preslist=new ArrayList<>();
        PrescriptionListView imagelist=new PrescriptionListView();
        imagelist.uploadFilelocal(prescriptionDate, doctorName, dieaseName, AppPreference.getInstance().getcf_uuhidNeew(),prescriptionImageListss,countofFileslocal,"self");
        arr_preslist.add(imagelist);
        return arr_preslist;
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
                        details.getInsertingValue(jsonObject);
                    }
                } else {
                    detailListing = new ArrayList<LabReportListView>();
                }

            } catch (Exception e) {

            }
        }
        return detailListing;
    }


    public List<GraphYearMonthDeatilsList> getGraphViewList(String response) {
        GraphYearMonthDeatilsList details = null;
        ArrayList<GraphYearMonthDeatilsList> detailListing = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                JSONArray jord = new JSONArray(json.getString(JSON_KEY_PAYLOAD));
                detailListing = new ArrayList<GraphYearMonthDeatilsList>();
                for (int i = 0; i < jord.length(); i++) {
                    details = new GraphYearMonthDeatilsList(jord.getJSONObject(i));
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


    public ArrayList<LabDoctorName> getLabDoctorName(String response) {
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
                    //insert local

                }
                details.getInsertingValue(detailListing,"3");
            } catch (Exception e) {

            }
        }
        return detailListing;
    }
       public ArrayList<LabDoctorName> getDoc_DoctorName(String response) {
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
                    //insert local

                }
                details.getInsertingValue(detailListing,"2");
            } catch (Exception e) {

            }
        }
        return detailListing;
    }

    public ArrayList<LabDoctorName> getMed_DoctorName(String response) {
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
                    //insert local

                }
                details.getInsertingValue(detailListing,"1");
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

                //details.getInsertingValue(jord);
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


    public MedicineReminderListView  getReminderMedicineList(String response) {
        MedicineReminderListView details = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null") || !json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("")) {
                    JSONObject jord = new JSONObject(json.getString(JSON_KEY_PAYLOAD));
                    details = new MedicineReminderListView(jord);
                }


            } catch (Exception e) {

            }
        }
        return details;
    }


    public LabTestReminderListView getReminderLabTestList(String response) {
        LabTestReminderListView details = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null") || !json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("")) {
                    JSONObject jord = new JSONObject(json.getString(JSON_KEY_PAYLOAD));
                    details = new LabTestReminderListView(jord);


                }

            } catch (Exception e) {

            }
        }
        return details;
    }

    public DoctorVistReminderListView getReminderDoctorList(String response) {
        DoctorVistReminderListView details = null;

        if (response != null) {
            try {
                JSONObject json = new JSONObject(response);
                setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
                if (!json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("null") || !json.getString(JSON_KEY_PAYLOAD).equalsIgnoreCase("")) {
                    JSONObject jord = new JSONObject(json.getString(JSON_KEY_PAYLOAD));
                    details = new DoctorVistReminderListView(jord);
                }

            } catch (Exception e) {

            }
        }
        return details;
    }


    private class MyAsyncTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


}
