package item.property;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class ReminderMedicnceTime implements MyConstants.JsonUtils, Parcelable {
    String status;
    private int hour;
    private int minute;


    public ReminderMedicnceTime(JSONObject jsonObject) {
        try {
            setStatus(jsonObject.getString("status"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("timeToTakeMedicineInDay"));
            setHour(jsonObject1.getInt("hour"));
            setMinute(jsonObject1.getInt("minute"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeInt(this.hour);
        dest.writeInt(this.minute);
    }

    public ReminderMedicnceTime(Parcel in) {
        this.status = in.readString();
        this.hour = in.readInt();
        this.minute = in.readInt();
    }

    public static final Parcelable.Creator<ReminderMedicnceTime> CREATOR = new Parcelable.Creator<ReminderMedicnceTime>() {
        public ReminderMedicnceTime createFromParcel(Parcel in) {
            return new ReminderMedicnceTime(in);
        }

        public ReminderMedicnceTime[] newArray(int size) {
            return new ReminderMedicnceTime[size];
        }
    };
}
