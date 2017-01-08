package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

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
    private ArrayList<PrescriptionImageListView> prescriptionImageListViews;

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
            setDiseaseName(jsonObject.getString(MyConstants.JsonUtils.DISEASE_NAME));
            setCountOfFiles(jsonObject.getString(MyConstants.JsonUtils.COUNT_OF_FILES));
            setUploadedBy(jsonObject.getString(MyConstants.JsonUtils.UPLOAD_BY));
            setDateOfUpload(jsonObject.getString(MyConstants.JsonUtils.DATE_OF_UPLOAD));
            setPrescriptionImageListViews(jsonObject.getJSONArray(MyConstants.JsonUtils.PRESCRIPTION_RESPONSE_LIST));
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

    public ArrayList<PrescriptionImageListView> getPrescriptionImageListViews() {
        return prescriptionImageListViews;
    }

    public void setPrescriptionImageListViews(ArrayList<PrescriptionImageListView> prescriptionImageListViews) {
        this.prescriptionImageListViews = prescriptionImageListViews;
    }

    public void setPrescriptionImageListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageListView card = null;
        this.prescriptionImageListViews = new ArrayList<PrescriptionImageListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
