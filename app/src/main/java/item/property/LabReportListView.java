package item.property;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

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
    private ArrayList<LabReportImageListView> labReportImageListViews;

    public LabReportListView() {
    }

    public LabReportListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCfUuhid(jsonObject.getString(MyConstants.JsonUtils.CFUUHIDs));
            setReportId(jsonObject.getString(MyConstants.JsonUtils.REPORT_ID));
            setReportDate(jsonObject.getString(MyConstants.JsonUtils.REPORT_DATE));
            setDoctorName(jsonObject.getString(MyConstants.JsonUtils.DOCTOR_NAME));
            setTestName(jsonObject.getString(MyConstants.JsonUtils.TEST_NAME));
            setCountOfFiles(jsonObject.getString(MyConstants.JsonUtils.COUNT_OF_FILES));
            setUploadedBy(jsonObject.getString(MyConstants.JsonUtils.UPLOAD_BY));
            setDateOfUpload(jsonObject.getString(MyConstants.JsonUtils.DATE_OF_UPLOAD));
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
}
