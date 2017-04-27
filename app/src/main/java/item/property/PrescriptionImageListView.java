package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import java.util.List;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionImageListView implements Parcelable, MyConstants.JsonUtils, Comparable<PrescriptionImageListView> {

    private String imageNumber;
    private String prescriptionImage;
    private String prescriptionImagePartId;

    public PrescriptionImageListView() {

    }

    public PrescriptionImageListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setImageNumber(jsonObject.getString(MyConstants.JsonUtils.IMAGE_NUMBER));
            setPrescriptionImage(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_IMAGE));
            setPrescriptionImagePartId(jsonObject.getString("prescriptionImagePartId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public PrescriptionImageListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setPrescriptionImage(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGE)));
            setPrescriptionImagePartId(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGEPARTID)));
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

    public String getPrescriptionImage() {
        return prescriptionImage;
    }

    public void setPrescriptionImage(String prescriptionImage) {
        this.prescriptionImage = prescriptionImage;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageNumber);
        dest.writeString(this.prescriptionImage);
        dest.writeString(this.prescriptionImagePartId);
    }

    public PrescriptionImageListView(Parcel in) {
        this.imageNumber = in.readString();
        this.prescriptionImage = in.readString();
        this.prescriptionImagePartId = in.readString();
    }

    public static final Parcelable.Creator<PrescriptionImageListView> CREATOR = new Parcelable.Creator<PrescriptionImageListView>() {
        public PrescriptionImageListView createFromParcel(Parcel in) {
            return new PrescriptionImageListView(in);
        }

        public PrescriptionImageListView[] newArray(int size) {
            return new PrescriptionImageListView[size];
        }
    };


    public String getPrescriptionImagePartId() {
        return prescriptionImagePartId;
    }

    public void setPrescriptionImagePartId(String prescriptionImagePartId) {
        this.prescriptionImagePartId = prescriptionImagePartId;
    }


    @Override
    public int compareTo(PrescriptionImageListView o) {
        return imageNumber.compareTo(o.getImageNumber());
    }

    public void getInsertingValue(JSONObject jsonobject1, String commonID) {
        try {
            ContentValues values2 = new ContentValues();
            values2.put(IMAGE_NUMBER, jsonobject1.getString(IMAGE_NUMBER));
            values2.put(PRESCRIPTION_IMAGE, jsonobject1.getString(PRESCRIPTION_IMAGE));
            values2.put(PRESCRIPTION_IMAGEPARTID, jsonobject1.getString(PRESCRIPTION_IMAGEPARTID));
            values2.put(COMMON_ID, commonID);
            DbOperations.insertPrescriptionResponseList(CureFull.getInstanse().getActivityIsntanse(), values2, commonID, jsonobject1.getString(PRESCRIPTION_IMAGEPARTID));
        } catch (Exception e) {

        }
    }

//data from local
    public void setInsertingValue(List<PrescriptionImageList> imageFile,String commonid) {
        try {

            for (int ii=0;ii<imageFile.size();ii++) {
                String imagenum = String.valueOf(imageFile.get(ii).getImageNumber());
                ContentValues values2 = new ContentValues();
                values2.put(IMAGE_NUMBER, imagenum);
                values2.put(PRESCRIPTION_IMAGE, imageFile.get(ii).getPrescriptionImage());
                values2.put(PRESCRIPTION_IMAGEPARTID, commonid);
                values2.put(COMMON_ID, commonid);
                DbOperations.insertPrescriptionResponseListLocal(CureFull.getInstanse().getActivityIsntanse(), values2, commonid,imagenum);

            }
        } catch (Exception e) {

        }
    }
   /* public void setInsertingValueLab(List<PrescriptionImageList> imageFile,String commonid) {
        try {

            for (int ii=0;ii<imageFile.size();ii++) {
                String imagenum = String.valueOf(imageFile.get(ii).getImageNumber());
                ContentValues values2 = new ContentValues();
                values2.put(IMAGE_NUMBER, imagenum);
                values2.put("reportImage", imageFile.get(ii).getPrescriptionImage());
                values2.put("reportImageId", commonid);
                values2.put(COMMON_ID, commonid);
                DbOperations.insertLabReportResponseListLocal(CureFull.getInstanse().getActivityIsntanse(), values2, commonid,imagenum);

            }
        } catch (Exception e) {

        }
    }*/


}
