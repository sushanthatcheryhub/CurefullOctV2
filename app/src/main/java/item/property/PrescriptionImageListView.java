package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionImageListView implements Parcelable, MyConstants.JsonUtils {

    private String imageNumber;
    private String prescriptionImage;
    private String iPrescriptionId;
    private String prescriptionId;

    public PrescriptionImageListView() {

    }

    public PrescriptionImageListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setImageNumber(jsonObject.getString(MyConstants.JsonUtils.IMAGE_NUMBER));
            setPrescriptionImage(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_IMAGE));
            setiPrescriptionId(jsonObject.getString("iPrescriptionId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ContentValues getInsertingValue(JSONObject json, int id) {
        try {
            ContentValues values = new ContentValues();
            values.put(IMAGE_NUMBER, json.getString(IMAGE_NUMBER));
            values.put(PRESCRIPTION_IMAGE, json.getString(PRESCRIPTION_IMAGE));
            values.put("iPrescriptionId", json.getString("iPrescriptionId"));
            values.put(PRESCRIPTION_ID, id);
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public PrescriptionImageListView(Cursor cur) {
        if (cur == null)
            return;
        try {
            setImageNumber(cur.getString(cur.getColumnIndex(IMAGE_NUMBER)));
            setPrescriptionImage(cur.getString(cur.getColumnIndex(PRESCRIPTION_IMAGE)));
            setiPrescriptionId(cur.getString(cur.getColumnIndex("iPrescriptionId")));
            setPrescriptionId(cur.getString(cur.getColumnIndex(PRESCRIPTION_ID)));
        } catch (Exception e) {
            e.printStackTrace();
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

    public String getiPrescriptionId() {
        return iPrescriptionId;
    }

    public void setiPrescriptionId(String iPrescriptionId) {
        this.iPrescriptionId = iPrescriptionId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imageNumber);
        dest.writeString(this.prescriptionImage);
        dest.writeString(this.iPrescriptionId);
    }

    public PrescriptionImageListView(Parcel in) {
        this.imageNumber = in.readString();
        this.prescriptionImage = in.readString();
        this.iPrescriptionId = in.readString();
    }

    public static final Parcelable.Creator<PrescriptionImageListView> CREATOR = new Parcelable.Creator<PrescriptionImageListView>() {
        public PrescriptionImageListView createFromParcel(Parcel in) {
            return new PrescriptionImageListView(in);
        }

        public PrescriptionImageListView[] newArray(int size) {
            return new PrescriptionImageListView[size];
        }
    };

    public String getPrescriptionId() {
        return prescriptionId;
    }

    public void setPrescriptionId(String prescriptionId) {
        this.prescriptionId = prescriptionId;
    }


}
