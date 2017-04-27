package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Simple container object for contact data
 * <p/>
 * Created by mgod on 9/12/13.
 *
 * @author mgod
 */
public class LabDoctorName implements MyConstants.JsonUtils {
    String doctorName;

    public LabDoctorName(String s) {
        setDoctorName("" + s);
    }

    public LabDoctorName(Cursor cursor) {
        if(cursor==null){
            return;
        }
        try{
            setDoctorName(cursor.getColumnName(cursor.getColumnIndex(DOCTOR_NAME)));

        }catch (Exception e){

        }
    }


    public String getDoctorName() {
        return doctorName;
    }

    public void setDoctorName(String doctorName) {
        this.doctorName = doctorName;
    }

    // insert through online
    public void getInsertingValue(String json) throws JSONException {
        try {
            String common_id= String.valueOf(System.currentTimeMillis());
            ContentValues values = new ContentValues();
            values.put(DOCTOR_NAME, json);
            values.put("isUploaded", "0");
            values.put("cfuuhid", AppPreference.getInstance().getcf_uuhid());
            values.put("common_id",common_id);

            DbOperations.insertLabReminderDoctorName(CureFull.getInstanse().getActivityIsntanse(), values,common_id, AppPreference.getInstance().getcf_uuhid());

        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
