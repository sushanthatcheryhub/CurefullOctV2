package item.property;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class LabReportImageListView implements Parcelable {

    private String imageNumber;
    private String reportImage;
    private String reportImageId;

    public LabReportImageListView() {
    }

    public LabReportImageListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setImageNumber(jsonObject.getString(MyConstants.JsonUtils.IMAGE_NUMBER));
            setReportImage(jsonObject.getString(MyConstants.JsonUtils.REPORT_IMAGE));
            setReportImageId(jsonObject.getString("reportImageId"));
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
}
