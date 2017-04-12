package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import operations.DatabaseHelper;
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

    /*private String prescriptonImageFollowupId;

    private String imageNumber;

    private String prescriptionImage;
    private String prescriptionImagePartId;*/

   /* public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    public void setPrescriptionImagePartId(String prescriptionImagePartId) {
        this.prescriptionImagePartId = prescriptionImagePartId;
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public String getPrescriptionImagePartId() {
        return prescriptionImagePartId;
    }



    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public String getPrescriptonImageFollowupId() {
        return prescriptonImageFollowupId;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }

    public void setPrescriptonImageFollowupId(String prescriptonImageFollowupId) {
        this.prescriptonImageFollowupId = prescriptonImageFollowupId;
    }*/

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

    public PrescriptionListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setCfUuhid(cur.getString(cur.getColumnIndex(CFUUHIDs)));
            setPrescriptionId(cur.getString(cur.getColumnIndex(PRESCRIPTION_ID)));
            setPrescriptionDate(cur.getString(cur.getColumnIndex(PRESCRIPTION_DATE)));
            setDoctorName(cur.getString(cur.getColumnIndex(DOCTOR_NAME)));
            setCountOfFiles(cur.getString(cur.getColumnIndex(COUNT_OF_FILES)));
            setUploadedBy(cur.getString(cur.getColumnIndex(UPLOAD_BY)));
            setCommonID(cur.getString(cur.getColumnIndex(COMMON_ID)));

            ArrayList<PrescriptionImageFollowUpListView> prescriptionImageFollowUpListViews=DbOperations.setPrescriptionImageFollowUpListViewsLocal(CureFull.getInstanse().getActivityIsntanse(),cur.getString(cur.getColumnIndex(COMMON_ID)));
            setPrescriptionImageFollowUpListViews(prescriptionImageFollowUpListViews);
            //setPrescriptionImageFollowUpListViewsLocal(prescriptionImageFollowUpListViews);
            /*setPrescriptonImageFollowupId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEFOLLOWUP_ID)));

            setPrescriptionImage(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGE)));
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setPrescriptionImagePartId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEPARTID)));*/

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public void getInsertingValue(JSONObject json) throws JSONException {
        try {
            ContentValues values = new ContentValues();
            values.put(CFUUHIDs, json.getString(CFUUHIDs));
            values.put(PRESCRIPTION_ID, json.getString(PRESCRIPTION_ID));
            values.put(PRESCRIPTION_DATE, json.getString(PRESCRIPTION_DATE));
            values.put(DOCTOR_NAME, json.getString(DOCTOR_NAME));
            values.put(COUNT_OF_FILES, json.getString(COUNT_OF_FILES));
            values.put(UPLOAD_BY, json.getString(UPLOAD_BY));
            setCommonID(json.getString(PRESCRIPTION_ID));
            values.put(COMMON_ID, getCommonID());
            DbOperations.insertPrescriptionList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew(),getCommonID());

            setPrescriptionImageFollowUpListViewsLocal(json.getJSONArray(PRESCRIPTION_FOLLOWUPLIST),getCommonID());

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


    public void setPrescriptionImageFollowUpListViewsLocal(JSONArray symptomslistArray, String commonID) {
        if (symptomslistArray == null)
            return;
        PrescriptionImageFollowUpListView card = null;
        this.prescriptionImageFollowUpListViews = new ArrayList<PrescriptionImageFollowUpListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new PrescriptionImageFollowUpListView(symptomslistArray.getJSONObject(i));
                this.prescriptionImageFollowUpListViews.add(card);

                card.getInsertingValue(symptomslistArray.getJSONObject(i),commonID);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
