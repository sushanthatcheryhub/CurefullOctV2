package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

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
import static utils.MyConstants.JsonUtils.IMAGE_NUMBER;
import static utils.MyConstants.JsonUtils.REPORT_DATE;
import static utils.MyConstants.JsonUtils.REPORT_ID;
import static utils.MyConstants.JsonUtils.REPORT_IMAGE;
import static utils.MyConstants.JsonUtils.REPORT_IMAGELIST;
import static utils.MyConstants.JsonUtils.TEST_NAME;
import static utils.MyConstants.JsonUtils.UPLOAD_BY;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class LabReportImageListView implements Parcelable, Comparable<LabReportImageListView> {

    private String imageNumber;
    private String reportImage;
    private String reportImageId;
    private String commonID;
    private String status;
    private String isUploaded;
    private String doctorName;

    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    public String getCommonID() {
        return commonID;
    }

    public void setCommonID(String commonID) {

        this.commonID = commonID;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getIsUploaded() {
        return isUploaded;
    }

    public void setIsUploaded(String isUploaded) {
        this.isUploaded = isUploaded;
    }

    public LabReportImageListView() {

    }

    public LabReportImageListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setImageNumber(jsonObject.getString(IMAGE_NUMBER));
            setReportImage(jsonObject.getString(REPORT_IMAGE));
            setReportImageId(jsonObject.getString("reportImageId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void getInsertingValue(JSONObject jsonobject1, String commonID) throws JSONException {
        try {
            ContentValues values = new ContentValues();
            values.put(IMAGE_NUMBER, jsonobject1.getString(IMAGE_NUMBER));
            values.put(REPORT_IMAGE, jsonobject1.getString(REPORT_IMAGE));
            values.put("reportImageId", jsonobject1.getString("reportImageId"));
            values.put(COMMON_ID, commonID);
            values.put("isUploaded", "0");
            values.put("status", "pending");

            DbOperations.insertLabTestReportResponseList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew(), jsonobject1.getString("reportImageId"));

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void setInsertingValueLab(List<PrescriptionImageList> imageFile, String commonid) {
        try {

            for (int ii = 0; ii < imageFile.size(); ii++) {
                String imagenum = String.valueOf(imageFile.get(ii).getImageNumber());
                ContentValues values2 = new ContentValues();
                values2.put(IMAGE_NUMBER, imagenum);
                values2.put("reportImage", imageFile.get(ii).getPrescriptionImage());
                values2.put("reportImageId", commonid);
                values2.put(COMMON_ID, commonid);
                values2.put("isUploaded", "0");
                values2.put("status", "pending");

                DbOperations.insertLabReportResponseListLocal(CureFull.getInstanse().getActivityIsntanse(), values2, commonid, imagenum);

            }
        } catch (Exception e) {

        }
    }

    public LabReportImageListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setReportImage(cur.getString(cur.getColumnIndex(REPORT_IMAGE)));
            setReportImageId(cur.getString(cur.getColumnIndex("reportImageId")));
            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            setStatus(cur.getString(cur.getColumnIndex("status")));
            setCommonID(cur.getString(cur.getColumnIndex("common_id")));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public LabReportImageListView(Cursor cur,String NIU) {
        if (cur == null)
            return;
        try {
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setReportImage(cur.getString(cur.getColumnIndex(REPORT_IMAGE)));
            setReportImageId(cur.getString(cur.getColumnIndex("reportImageId")));
            setIsUploaded(cur.getString(cur.getColumnIndex("isUploaded")));
            setStatus(cur.getString(cur.getColumnIndex("status")));
            setCommonID(cur.getString(cur.getColumnIndex("common_id")));
            setDoctorName(cur.getString(cur.getColumnIndex("doctorName")));
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String getImageNumber() {
        return imageNumber;
    }

    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }

    public String getReportImage() {
        return reportImage;
    }

    public void setReportImage(String reportImage) {
        this.reportImage = reportImage;
    }

    public String getReportImageId() {
        return reportImageId;
    }

    public void setReportImageId(String reportImageId) {
        this.reportImageId = reportImageId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageNumber);
        dest.writeString(this.reportImage);
        dest.writeString(this.reportImageId);
    }

    public LabReportImageListView(Parcel in) {
        this.imageNumber = in.readString();
        this.reportImage = in.readString();
        this.reportImageId = in.readString();
    }

    public static final Parcelable.Creator<LabReportImageListView> CREATOR = new Parcelable.Creator<LabReportImageListView>() {
        public LabReportImageListView createFromParcel(Parcel in) {
            return new LabReportImageListView(in);
        }

        public LabReportImageListView[] newArray(int size) {
            return new LabReportImageListView[size];
        }
    };

    @Override
    public int compareTo(LabReportImageListView o) {
        return imageNumber.compareTo(o.getImageNumber());
    }
}
