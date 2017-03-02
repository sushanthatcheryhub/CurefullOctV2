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
}
