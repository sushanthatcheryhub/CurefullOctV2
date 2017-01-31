package item.property;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionImageFollowUpListView implements Parcelable, MyConstants.JsonUtils {

    private String prescriptionImage;
    private String prescriptonImageFollowupId;
    private String prescriptionDate;
    private String countOfFiles;
    private ArrayList<PrescriptionImageListView> prescriptionImageListViews;

    public PrescriptionImageFollowUpListView() {

    }

    public PrescriptionImageFollowUpListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setCountOfFiles(jsonObject.getString("countOfFiles"));
            setPrescriptionDate(jsonObject.getString("prescriptionDate"));
//            setPrescriptionImage(jsonObject.getString(MyConstants.JsonUtils.PRESCRIPTION_IMAGE));
            setPrescriptonImageFollowupId(jsonObject.getString("prescriptonImageFollowupId"));
            setPrescriptionImageListViews(jsonObject.getJSONArray(MyConstants.JsonUtils.PRESCRIPTION_RESPONSE_LIST));
        } catch (Exception e) {
            e.printStackTrace();
        }
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
        dest.writeString(this.prescriptionDate);
        dest.writeString(this.prescriptionImage);
        dest.writeString(this.prescriptonImageFollowupId);
        dest.writeString(this.countOfFiles);
    }

    public PrescriptionImageFollowUpListView(Parcel in) {
        this.prescriptionDate = in.readString();
        this.prescriptionImage = in.readString();
        this.prescriptonImageFollowupId = in.readString();
        this.countOfFiles = in.readString();
    }

    public static final Creator<PrescriptionImageFollowUpListView> CREATOR = new Creator<PrescriptionImageFollowUpListView>() {
        public PrescriptionImageFollowUpListView createFromParcel(Parcel in) {
            return new PrescriptionImageFollowUpListView(in);
        }

        public PrescriptionImageFollowUpListView[] newArray(int size) {
            return new PrescriptionImageFollowUpListView[size];
        }
    };

    public String getPrescriptonImageFollowupId() {
        return prescriptonImageFollowupId;
    }

    public void setPrescriptonImageFollowupId(String prescriptonImageFollowupId) {
        this.prescriptonImageFollowupId = prescriptonImageFollowupId;
    }

    public String getPrescriptionDate() {
        return prescriptionDate;
    }

    public void setPrescriptionDate(String prescriptionDate) {
        this.prescriptionDate = prescriptionDate;
    }

    public String getCountOfFiles() {
        return countOfFiles;
    }

    public void setCountOfFiles(String countOfFiles) {
        this.countOfFiles = countOfFiles;
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
