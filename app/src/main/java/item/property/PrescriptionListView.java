package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionListView implements MyConstants.JsonUtils {

    private String cfUuhid;
    private String prescriptionId;
    private String prescriptionDate;
    private String doctorName;
    private String diseaseName;
    private String countOfFiles;
    private String uploadedBy;
    private String dateOfUpload;
    private String common_id;
    private ArrayList<PrescriptionImageFollowUpListView> prescriptionImageFollowUpListViews;

    public PrescriptionListView() {
    }

    public PrescriptionListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCfUuhid(jsonObject.getString(MyConstants.JsonUtils.CFUUHIDs));
            setPrescriptionId(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_ID));
            setPrescriptionDate(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_DATE));
            setDoctorName(jsonObject.getString(MyConstants.JsonUtils.DOCTOR_NAME));
//            setDiseaseName(jsonObject.getString(MyConstants.JsonUtils.DISEASE_NAME));
            setCountOfFiles(jsonObject.getString(MyConstants.JsonUtils.COUNT_OF_FILES));
            setUploadedBy(jsonObject.getString(MyConstants.JsonUtils.UPLOAD_BY));
//            setDateOfUpload(jsonObject.getString(MyConstants.JsonUtils.DATE_OF_UPLOAD));
            setPrescriptionImageFollowUpListViews(jsonObject.getJSONArray("prescriptionFollowupList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void getInsertingValue(JSONObject json) {
        try {
            JSONObject jsonResponse1 = json.getJSONObject(MyConstants.JsonUtils.JSON_KEY_PAYLOAD);
            ContentValues values = new ContentValues();
            values.put(CFUUHIDs, jsonResponse1.getString(CFUUHIDs));
            values.put(PRESCRIPTION_ID, jsonResponse1.getString(PRESCRIPTION_ID));
            values.put(PRESCRIPTION_DATE, jsonResponse1.getString(PRESCRIPTION_DATE));
            values.put(DOCTOR_NAME, jsonResponse1.getString(DOCTOR_NAME));
            values.put(COUNT_OF_FILES, jsonResponse1.getString(COUNT_OF_FILES));
            values.put(UPLOAD_BY, jsonResponse1.getString(UPLOAD_BY));
            values.put(COMMON_ID, jsonResponse1.getString(COMMON_ID));
            setCommonID(jsonResponse1.getString(PRESCRIPTION_ID));
            DbOperations.insertPrescriptionList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew());

            ContentValues values1 = new ContentValues();
            JSONObject jsonResponse2 = json.getJSONObject(MyConstants.JsonUtils.PRESCRIPTION_FOLLOWUPLIST);
            values1.put(COUNT_OF_FILES, jsonResponse2.getString(COUNT_OF_FILES));
            values1.put(PRESCRIPTION_DATE, jsonResponse2.getString(PRESCRIPTION_DATE));
            values1.put(PRESCRIPTION_IMAGEFOLLOWUP_ID, jsonResponse2.getString(PRESCRIPTION_IMAGEFOLLOWUP_ID));
            values1.put(COMMON_ID, getCommonID());
            DbOperations.insertPrescriptionFollowUPList(CureFull.getInstanse().getActivityIsntanse(), values1, getCommonID());

            ContentValues values2 = new ContentValues();
            JSONObject jsonResponse3 = json.getJSONObject(MyConstants.JsonUtils.PRESCRIPTION_RESPONSE_LIST);
            values2.put(IMAGE_NUMBER, jsonResponse3.getString(IMAGE_NUMBER));
            values2.put(PRESCRIPTION_IMAGE, jsonResponse3.getString(PRESCRIPTION_IMAGE));
            values2.put(PRESCRIPTION_IMAGEPARTID, jsonResponse3.getString(PRESCRIPTION_IMAGEPARTID));
            values2.put(COMMON_ID, getCommonID());
            DbOperations.insertPrescriptionResponseList(CureFull.getInstanse().getActivityIsntanse(), values2, getCommonID());



        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public String getCfUuhid() {
        return cfUuhid;
    }

    public void setCfUuhid(String cfUuhid) {
        this.cfUuhid = cfUuhid;
    }

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }
    public void setCommonID(String common_id) {
        this.common_id = common_id;
    }
    public String getCommonID() {
        return common_id;
    }
    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public void setDiseaseName(String diseaseName) {
        this.diseaseName = diseaseName;
    }

    public String getCountOfFiles() {
        return countOfFiles;
    }

    public void setCountOfFiles(String countOfFiles) {
        this.countOfFiles = countOfFiles;
    }

    public String getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(String uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public String getDateOfUpload() {
        return dateOfUpload;
    }

    public void setDateOfUpload(String dateOfUpload) {
        this.dateOfUpload = dateOfUpload;
    }

    public ArrayList<PrescriptionImageFollowUpListView> getPrescriptionImageFollowUpListViews() {
        return prescriptionImageFollowUpListViews;
    }

    public void setPrescriptionImageFollowUpListViews(ArrayList<PrescriptionImageFollowUpListView> prescriptionImageListViews) {
        this.prescriptionImageFollowUpListViews = prescriptionImageListViews;
    }

    public void setPrescriptionImageFollowUpListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageFollowUpListView card = null;
        this.prescriptionImageFollowUpListViews = new ArrayList<PrescriptionImageFollowUpListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageFollowUpListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageFollowUpListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
