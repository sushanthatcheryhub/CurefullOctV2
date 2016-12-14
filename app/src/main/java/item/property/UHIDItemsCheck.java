package item.property;

import android.os.Parcel;
import android.os.Parcelable;
import org.json.JSONObject;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UHIDItemsCheck implements Parcelable {

    private String name;
    private String createdAt;
    private String cfUuhid;
    private boolean isChecked=false;

    public UHIDItemsCheck() {

    }


    public UHIDItemsCheck(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setName(jsonObject.getString("name"));
            setCfUuhid(jsonObject.getString("cfUuhid"));
            setCreatedAt(jsonObject.getString("createdAt"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getCfUuhid() {
        return cfUuhid;
    }

    public void setCfUuhid(String cfUuhid) {
        this.cfUuhid = cfUuhid;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }


    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.cfUuhid);
        dest.writeString(this.createdAt);
    }

    public UHIDItemsCheck(Parcel in) {
        this.name = in.readString();
        this.cfUuhid = in.readString();
        this.createdAt = in.readString();
    }

    public static final Parcelable.Creator<UHIDItemsCheck> CREATOR = new Parcelable.Creator<UHIDItemsCheck>() {
        public UHIDItemsCheck createFromParcel(Parcel in) {
            return new UHIDItemsCheck(in);
        }

        public UHIDItemsCheck[] newArray(int size) {
            return new UHIDItemsCheck[size];
        }
    };
}
