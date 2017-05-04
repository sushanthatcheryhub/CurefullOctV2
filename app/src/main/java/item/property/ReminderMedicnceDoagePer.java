package item.property;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import curefull.healthapp.CureFull;
import operations.DatabaseHelper;
import operations.DbOperations;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;

import static utils.MyConstants.IDataBaseTableKeys.TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE;

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
    private String common_id;
    //private String sr_id;
    private String dosagePerDayDetailsId;

    public String getDosagePerDayDetailsId() {
        return dosagePerDayDetailsId;
    }

    public void setDosagePerDayDetailsId(String dosagePerDayDetailsId) {

        this.dosagePerDayDetailsId = dosagePerDayDetailsId;
    }

   /* public String getSr_id() {
        return sr_id;
    }

    public void setSr_id(String sr_id) {

        this.sr_id = sr_id;
    }*/

    public String getCommon_id() {
        return common_id;
    }

    public void setCommon_id(String common_id) {

        this.common_id = common_id;
    }
    public ReminderMedicnceDoagePer(){}

    public ReminderMedicnceDoagePer(JSONObject jsonObject) {
        try {
            setStatus(jsonObject.getString("status"));
            setDate(jsonObject.getString("date"));
            setDosagePerDayDetailsId(jsonObject.getString("dosagePerDayDetailsId"));
            setReminderMedicnceTimes(jsonObject.getJSONArray("medicineReminderAlarmDetailsResponse"));
        } catch (JSONException e) {
            e.printStackTrace();

        }

    }

    public ReminderMedicnceDoagePer(Cursor cursorprivate2) {
        if(cursorprivate2==null){
            return;
        }

        try{
            setStatus(cursorprivate2.getString(cursorprivate2.getColumnIndex("status")));
            setDate(cursorprivate2.getString(cursorprivate2.getColumnIndex("date")));
            setCommon_id(cursorprivate2.getString(cursorprivate2.getColumnIndex("common_id")));
            setDosagePerDayDetailsId(cursorprivate2.getString(cursorprivate2.getColumnIndex("dosagePerDayDetailsId")));
           //setSr_id(cursorprivate2.getString(cursorprivate2.getColumnIndex("sr_id")));
            reminderMedicnceTimes = DbOperations.setReminderMedicinealarmdetailres(CureFull.getInstanse().getActivityIsntanse(), getCommon_id(),cursorprivate2.getString(cursorprivate2.getColumnIndex("dosagePerDayDetailsId")));//, cursorprivate2.getString(cursorprivate2.getColumnIndex("sr_id")

        }catch (Exception e){

        }

    }
   /* public ReminderMedicnceDoagePer(Cursor cursorprivate2,String nothing) {
        if(cursorprivate2==null){
            return;
        }

        try{

            setCommon_id(cursorprivate2.getString(cursorprivate2.getColumnIndex("common_id")));

        }catch (Exception e){

        }

    }*/

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

    public void setReminderMedicnceTimesLocal(JSONArray symptomslistArray,String commonID) {
        if (symptomslistArray == null)
            return;
        ReminderMedicnceTime card = null;
        this.reminderMedicnceTimes = new ArrayList<ReminderMedicnceTime>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new ReminderMedicnceTime(symptomslistArray.getJSONObject(i));
                this.reminderMedicnceTimes.add(card);
                card.getInsertingValue(symptomslistArray.getJSONObject(i),commonID,i);
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

    public void getInsertingValue(JSONObject jsonObject, String commonID, int i) {
    try{
        ContentValues values=new ContentValues();
        values.put("status",jsonObject.getString("status"));
        values.put("date",jsonObject.getString("date"));
        values.put("common_id",commonID);
        values.put("data_id",i);
        DbOperations.insertMedicineRemiderPerDosageDateResponse(CureFull.getInstanse().getActivityIsntanse(), values, commonID,i,jsonObject.getString("date"));

        setReminderMedicnceTimesLocal(jsonObject.getJSONArray("medicineReminderAlarmDetailsResponse"),commonID);
    }catch (Exception e){
        e.getMessage();
    }

    }

    public void setInsertingValueLab(String newTime, String commonid,int data_id,String selectedDate,String noOfdays,String dosagePerDayDetailsId) {
//2


       // int days=Integer.parseInt(noOfdays);
        //for (int i=0;i<days;i++) {

            ContentValues values = new ContentValues();

            values.put("status", "pending");
            values.put("date", selectedDate);//Utils.getTodayDate()
            values.put("common_id", commonid);
            values.put("data_id", data_id);
            values.put("dosagePerDayDetailsId", dosagePerDayDetailsId);
            DbOperations.insertMedicineRemiderDosagePerLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid, selectedDate,dosagePerDayDetailsId);


          /*  String[] newTimesplit=newTime.split(",");
        for (int i=0;i<newTimesplit.length;i++) {
            String[] time=newTimesplit[i].split(":");
            String hour=time[0];
            String minute=time[1];
            ReminderMedicnceTime reminder_medicine_time = new ReminderMedicnceTime();
            reminder_medicine_time.setInsertingValueLab(newTime, commonid, sr_id,i,hour,minute);
        }*/
        //}
    }

//sr_id means 2nd table id
    public void setInsertingValueNotificationDoneSkip(Context context,String newTime, String commonid, String selectedDate,String status,String dosagePerDayDetailsId) throws IOException {
        String stat="complete";
        String stat1="skip";
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        Cursor cursor;
        Cursor cursor1;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;

        ReminderMedicnceTime reminder_medicine_time=new ReminderMedicnceTime();
        reminder_medicine_time.setInsertingValueNotification(newTime,commonid,status,context,dosagePerDayDetailsId);

        dbhelperShopCart.createDataBase();
        database = DatabaseHelper.openDataBase();
        String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE + " where common_id='" + commonid + "' and (status='" + stat + "' or status='" + stat1 + "') and dosagePerDayDetailsId='"+dosagePerDayDetailsId+"'";
        cursor = database.rawQuery(query, null);
        if(cursor.getCount()>0) {
            String query1 = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE + " where common_id='" + commonid + "' and dosagePerDayDetailsId='"+dosagePerDayDetailsId+"'";
            cursor1=database.rawQuery(query1,null);
            if(cursor1.getCount()==cursor.getCount()){
                ContentValues values = new ContentValues();
                values.put("status", "complete");//all done
                values.put("date", selectedDate);
                values.put("common_id", commonid);
                values.put("dosagePerDayDetailsId", dosagePerDayDetailsId);
                DbOperations.insertMedicineRemiderDosagePerLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid, selectedDate,dosagePerDayDetailsId);
            }else {
                ContentValues values = new ContentValues();
                values.put("status", "progress");//single row ya double row not for all row
                values.put("date", selectedDate);
                values.put("common_id", commonid);
                values.put("dosagePerDayDetailsId", dosagePerDayDetailsId);
                DbOperations.insertMedicineRemiderDosagePerLocal(CureFull.getInstanse().getActivityIsntanse(), values, commonid, selectedDate,dosagePerDayDetailsId);
            }

        }

    }

}
