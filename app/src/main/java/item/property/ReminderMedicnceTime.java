package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class ReminderMedicnceTime implements MyConstants.JsonUtils, Parcelable {
    private String status;
    private int hour;
    private int minute;
    private String common_id;

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

    public ReminderMedicnceTime() {

    }

    public String getCommon_id() {
        return common_id;
    }

    public void setCommon_id(String common_id) {

        this.common_id = common_id;
    }

    public ReminderMedicnceTime(Cursor cursorprivate2) {
        if(cursorprivate2==null){
        return;
        }
        try{
            setStatus(cursorprivate2.getString(cursorprivate2.getColumnIndex("status")));
            setHour(cursorprivate2.getInt(cursorprivate2.getColumnIndex("hour")));
            setMinute(cursorprivate2.getInt(cursorprivate2.getColumnIndex("minute")));
            setCommon_id(cursorprivate2.getString(cursorprivate2.getColumnIndex("common_id")));
        }catch (Exception e){

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

    public void getInsertingValue(JSONObject jsonObject, String commonID, int i) {
        try {

            ContentValues values=new ContentValues();
            values.put("status",jsonObject.getString("status"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("timeToTakeMedicineInDay"));
            values.put("hour",jsonObject1.getInt("hour"));
            values.put("minute",jsonObject1.getInt("minute"));
            values.put("common_id",commonID);
            values.put("data_id",i);
            DbOperations.insertMedicineRemiderAlarmDetailResponse(CureFull.getInstanse().getActivityIsntanse(), values, commonID,i);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void setInsertingValueLab(String newTime, String commonid) {

        String[] newTimesplit=newTime.split(",");


        for (int i=0;i<newTimesplit.length;i++) {
            String[] time=newTimesplit[i].split(":");
            String hour=time[0];
            String minute=time[1];
            ContentValues values = new ContentValues();
            values.put("status", "pending");
            values.put("hour",hour);
            values.put("minute",minute);
            values.put("common_id",commonid);
            values.put("data_id",i);
            DbOperations.insertMedicineRemiderAlaramDetailResponseLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid,i);

        }
    }
}
