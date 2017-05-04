package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.provider.Settings;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            setDoctorName(cursor.getString(cursor.getColumnIndex(DOCTOR_NAME)));

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
    public void getInsertingValue(ArrayList<LabDoctorName> json) throws JSONException {
        try {
            for (int i1=0;i1<json.size();i1++) {
                String common_id = String.valueOf(System.currentTimeMillis());
                ContentValues values = new ContentValues();
                values.put(DOCTOR_NAME, json.get(i1).getDoctorName());//.getString("doctorName")
                values.put("isUploaded", "0");
                values.put("cfuuhid", AppPreference.getInstance().getcf_uuhid());
                values.put("common_id", common_id);
                values.put("case_id", "3");//not in use   //1-medicine reminder doctor name   2-doctor reminder doctor name  3-lab reminder doctor name

                DbOperations.insertDoctorName(CureFull.getInstanse().getActivityIsntanse(), values, common_id, AppPreference.getInstance().getcf_uuhid());
                //DbOperations.insertLabReminderDoctorName(CureFull.getInstanse().getActivityIsntanse(), values,common_id, AppPreference.getInstance().getcf_uuhid());
            }
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

}
