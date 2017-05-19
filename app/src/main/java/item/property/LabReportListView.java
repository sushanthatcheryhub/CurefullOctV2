package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;
import utils.MyConstants;

import static utils.MyConstants.JsonUtils.CFUUHIDs;
import static utils.MyConstants.JsonUtils.COMMON_ID;
import static utils.MyConstants.JsonUtils.COUNT_OF_FILES;
import static utils.MyConstants.JsonUtils.DATE_OF_UPLOAD;
import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.PRESCRIPTION_FOLLOWUPLIST;
import static utils.MyConstants.JsonUtils.REPORT_DATE;
import static utils.MyConstants.JsonUtils.REPORT_ID;
import static utils.MyConstants.JsonUtils.REPORT_IMAGELIST;
import static utils.MyConstants.JsonUtils.TEST_NAME;
import static utils.MyConstants.JsonUtils.UPLOAD_BY;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class LabReportListView {

    private String cfUuhid;
    private String reportId;
    private String reportDate;
    private String doctorName;
    private String testName;
    private String countOfFiles;
    private String uploadedBy;
    private String dateOfUpload;
    private String commonID;
    private String isUploaded;

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    private String status;

    public String getIsUploaded() {
        return isUploaded;
    }

    private ArrayList<LabReportImageListView> labReportImageListViews;

    public LabReportListView() {
    }


    public LabReportListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setCfUuhid(cur.getString(cur.getColumnIndex(CFUUHIDs)));
            setReportId(cur.getString(cur.getColumnIndex(REPORT_ID)));
            setReportDate(cur.getString(cur.getColumnIndex(REPORT_DATE)));
            setTestName(cur.getString(cur.getColumnIndex(TEST_NAME)));
            setDoctorName(cur.getString(cur.getColumnIndex(DOCTOR_NAME)));
            setCountOfFiles(cur.getString(cur.getColumnIndex(COUNT_OF_FILES)));
            setUploadedBy(cur.getString(cur.getColumnIndex(UPLOAD_BY)));
            setDateOfUpload(cur.getString(cur.getColumnIndex(DATE_OF_UPLOAD)));
            setCommonID(cur.getString(cur.getColumnIndex(COMMON_ID)));
            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            setStatus(cur.getString(cur.getColumnIndex("status")));
            ArrayList<LabReportImageListView> labReportImageListViews= DbOperations.setLabTestReportResponseListViewsLocal(CureFull.getInstanse().getActivityIsntanse(),cur.getString(cur.getColumnIndex(COMMON_ID)));
            setLabReportList(labReportImageListViews);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void setLabReportList(ArrayList<LabReportImageListView> labImageResponseList) {
        this.labReportImageListViews=labImageResponseList;
    }
    public void getInsertingValue(JSONObject json) throws JSONException {
        try {
            ContentValues values = new ContentValues();
            values.put(CFUUHIDs, json.getString(CFUUHIDs));
            values.put(REPORT_ID, json.getString(REPORT_ID));
            values.put(REPORT_DATE, json.getString(REPORT_DATE));
            values.put(DOCTOR_NAME, json.getString(DOCTOR_NAME));
            values.put(TEST_NAME, json.getString(TEST_NAME));
            values.put(COUNT_OF_FILES, json.getString(COUNT_OF_FILES));
            values.put(UPLOAD_BY, json.getString(UPLOAD_BY));
            values.put(DATE_OF_UPLOAD, json.getString(DATE_OF_UPLOAD));
            setCommonID(json.getString(REPORT_ID));
            values.put(COMMON_ID, getCommonID());
            values.put("isUploaded", "0");
            values.put("status", "pending");
            DbOperations.insertLabTestReportList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew(),getCommonID());

            setLabReprtListViewsLocal(json.getJSONArray(REPORT_IMAGELIST),getCommonID());

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void uploadFilelocal(String prescriptionDate, String doctorName, String dieaseName, String cfUuhidId, List<PrescriptionImageList> imageFile, int countOfFiles, String uploadedBy) {

        String countoffilis=String.valueOf(countOfFiles);


        try {
            String commonid=String.valueOf(System.currentTimeMillis());

            ContentValues values = new ContentValues();

            values.put(REPORT_DATE, prescriptionDate);
            values.put(DATE_OF_UPLOAD, prescriptionDate);
            values.put(TEST_NAME,dieaseName);
            values.put(DOCTOR_NAME, doctorName);
            values.put(CFUUHIDs, AppPreference.getInstance().getcf_uuhidNeew());
            values.put(COUNT_OF_FILES,countoffilis);
            values.put(UPLOAD_BY, "self");
            values.put("isUploaded", "1");
            values.put(COMMON_ID,commonid);
            values.put(REPORT_ID,commonid);
            values.put("status","pending");
            DbOperations.insertLabReportImage(CureFull.getInstanse().getActivityIsntanse(), values,"1",commonid);

            LabReportImageListView  imageListView=new LabReportImageListView();
            imageListView.setInsertingValueLab(imageFile,commonid);
            //PrescriptionImageListView imagelist=new PrescriptionImageListView();
          //  imagelist.setInsertingValueLab(imageFile,commonid);

        } catch (Exception e) {
            e.printStackTrace();
        }



    }


    public String getCommonID() {
        return commonID;
    }

    public void setLabReprtListViewsLocal(JSONArray symptomslistArray, String commonID) {
        if (symptomslistArray == null)
            return;
        LabReportImageListView card = null;
        this.labReportImageListViews = new ArrayList<LabReportImageListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new LabReportImageListView(symptomslistArray.getJSONObject(i));
                this.labReportImageListViews.add(card);

                card.getInsertingValue(symptomslistArray.getJSONObject(i),commonID);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
    public LabReportListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCfUuhid(jsonObject.getString(CFUUHIDs));
            setReportId(jsonObject.getString(MyConstants.JsonUtils.REPORT_ID));
            setReportDate(jsonObject.getString(MyConstants.JsonUtils.REPORT_DATE));
            setDoctorName(jsonObject.getString(DOCTOR_NAME));
            setTestName(jsonObject.getString(MyConstants.JsonUtils.TEST_NAME));
            setCountOfFiles(jsonObject.getString(COUNT_OF_FILES));
            setUploadedBy(jsonObject.getString(UPLOAD_BY));
            setDateOfUpload(jsonObject.getString(DATE_OF_UPLOAD));
            setLabReportImageListViews(jsonObject.getJSONArray(MyConstants.JsonUtils.LAB_REPORT_LIST));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTestName() {
        return testName;
    }

    public void setTestName(String testName) {
        this.testName = testName;
    }

    public String getCfUuhid() {
        return cfUuhid;
    }

    public void setCfUuhid(String cfUuhid) {
        this.cfUuhid = cfUuhid;
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
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

    public String getReportId() {
        return reportId;
    }

    public void setReportId(String reportId) {
        this.reportId = reportId;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public ArrayList<LabReportImageListView> getLabReportImageListViews() {
        return labReportImageListViews;
    }

    public void setLabReportImageListViews(ArrayList<LabReportImageListView> labReportImageListViews) {
        this.labReportImageListViews = labReportImageListViews;
    }

    public void setLabReportImageListViews(JSONArray labReportListArray) {
        if (labReportListArray == null)
            return;
        LabReportImageListView card = null;
        this.labReportImageListViews = new ArrayList<>();
        for (int i = 0; i < labReportListArray.length(); i++) {
            try {
                card = new LabReportImageListView(labReportListArray.getJSONObject(i));
                this.labReportImageListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setCommonID(String commonID) {
        this.commonID = commonID;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }
}
