package item.property;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class ReminderMedicnceDoagePer implements MyConstants.JsonUtils, Parcelable {
    private String status;
    private String date;
    private ArrayList<ReminderMedicnceTime> reminderMedicnceTimes;

    public ReminderMedicnceDoagePer(JSONObject jsonObject) {
        try {
            setStatus(jsonObject.getString("status"));
            setDate(jsonObject.getString("date"));
            setReminderMedicnceTimes(jsonObject.getJSONArray("medicineReminderAlarmDetailsResponse"));
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.status);
        dest.writeString(this.date);
    }

    public ReminderMedicnceDoagePer(Parcel in) {
        this.status = in.readString();
        this.date = in.readString();
    }

    public ArrayList<ReminderMedicnceTime> getReminderMedicnceTimes() {
        return reminderMedicnceTimes;
    }

    public void setReminderMedicnceTimes(ArrayList<ReminderMedicnceTime> reminderMedicnceTimes) {
        this.reminderMedicnceTimes = reminderMedicnceTimes;
    }


    public void setReminderMedicnceTimes(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        ReminderMedicnceTime card = null;
        this.reminderMedicnceTimes = new ArrayList<ReminderMedicnceTime>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new ReminderMedicnceTime(symptomslistArray.getJSONObject(i));
                this.reminderMedicnceTimes.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static final Creator<ReminderMedicnceDoagePer> CREATOR = new Creator<ReminderMedicnceDoagePer>() {
        public ReminderMedicnceDoagePer createFromParcel(Parcel in) {
            return new ReminderMedicnceDoagePer(in);
        }

        public ReminderMedicnceDoagePer[] newArray(int size) {
            return new ReminderMedicnceDoagePer[size];
        }
    };
}
