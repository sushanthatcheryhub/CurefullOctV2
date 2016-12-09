package item.property;

import org.json.JSONObject;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionUploadItems {

    private int note_id;
    private String note_date;
    private String note_time;
    private String note_heading;
    private String deatils;
    private String note_to_time;
    private String note_year;

    public PrescriptionUploadItems() {

    }


    public PrescriptionUploadItems(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setNote_id(jsonObject.getInt(MyConstants.JsonUtils.ID));
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
}
