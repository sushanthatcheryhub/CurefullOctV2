package item.property;

import android.content.ContentValues;
import android.database.Cursor;

import org.json.JSONObject;

import utils.AppPreference;
import utils.MyConstants;

import static utils.MyConstants.JsonUtils.ID;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class HealthNoteItems implements MyConstants.JsonUtils {

    private int note_id;
    private String note_date;
    private String note_time;
    private String note_heading;
    private String deatils;
    private String note_to_time;
    private String note_year;
    private int primaryId;

    public HealthNoteItems() {
    }


    public HealthNoteItems(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setNote_id(jsonObject.getInt(ID));
            setNote_date(jsonObject.getString(MyConstants.JsonUtils.NOTE_DATE));
            setNote_heading(jsonObject.getString(MyConstants.JsonUtils.NOTE_HEADING));
            setDeatils(jsonObject.getString(MyConstants.JsonUtils.NOTE_DEATILS));
            setNote_time(jsonObject.getString(MyConstants.JsonUtils.NOTE_TIME));
            setNote_to_time(jsonObject.getString(MyConstants.JsonUtils.NOTE_TIME_TO));
            setNote_year(jsonObject.getString(MyConstants.JsonUtils.YEAR));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ContentValues getInsertingValue(JSONObject json) {
        try {
            ContentValues values = new ContentValues();
            setPrimaryId(json.getInt(ID));
            values.put(ID, json.getInt(ID));
            values.put(NOTE_DATE, json.getString(NOTE_DATE));
            values.put(NOTE_HEADING, json.getString(NOTE_HEADING));
            values.put(NOTE_DEATILS, json.getString(NOTE_DEATILS));
            values.put(NOTE_TIME, json.getString(NOTE_TIME));
            values.put(NOTE_TIME_TO, json.getString(NOTE_TIME_TO));
            values.put(YEAR, json.getString(YEAR));
            values.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
            return values;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public HealthNoteItems(Cursor cur) {
        if (cur == null)
            return;
        try {
            setNote_id(cur.getInt(cur.getColumnIndex(ID)));
            setNote_date(cur.getString(cur.getColumnIndex(NOTE_DATE)));
            setNote_heading(cur.getString(cur.getColumnIndex(NOTE_HEADING)));
            setDeatils(cur.getString(cur.getColumnIndex(NOTE_DEATILS)));
            setNote_time(cur.getString(cur.getColumnIndex(NOTE_TIME)));
            setNote_to_time(cur.getString(cur.getColumnIndex(NOTE_TIME_TO)));
            setNote_year(cur.getString(cur.getColumnIndex(YEAR)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getNote_id() {
        return note_id;
    }

    public void setNote_id(int note_id) {
        this.note_id = note_id;
    }

    public String getNote_date() {
        return note_date;
    }

    public void setNote_date(String note_date) {
        this.note_date = note_date;
    }

    public String getNote_time() {
        return note_time;
    }

    public void setNote_time(String note_time) {
        this.note_time = note_time;
    }

    public String getNote_heading() {
        return note_heading;
    }

    public void setNote_heading(String note_heading) {
        this.note_heading = note_heading;
    }

    public String getDeatils() {
        return deatils;
    }

    public void setDeatils(String deatils) {
        this.deatils = deatils;
    }

    public String getNote_to_time() {
        return note_to_time;
    }

    public void setNote_to_time(String note_to_time) {
        this.note_to_time = note_to_time;
    }

    public String getNote_year() {
        return note_year;
    }

    public void setNote_year(String note_year) {
        this.note_year = note_year;
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }
}
