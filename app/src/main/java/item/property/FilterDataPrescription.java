package item.property;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.AppPreference;

import static utils.MyConstants.JsonUtils.DATELIST;
import static utils.MyConstants.JsonUtils.DOCTORNAMELIST;
import static utils.MyConstants.JsonUtils.DOCTOR_NAME;
import static utils.MyConstants.JsonUtils.UPLOADEDBYLIST;

/**
 * Created by Sushant Hatcheryhub on 06-01-2017.
 */

public class FilterDataPrescription {

    ArrayList<String> dateList;
    ArrayList<String> doctorNameList;
    ArrayList<String> uploadedByList;

    public FilterDataPrescription(Cursor cur, String date) {
        if (cur == null)
            return;
        try {
            if (date.equalsIgnoreCase("pm.prescriptionDate")) {
                setDateList(cur);
            } else if(date.equalsIgnoreCase("pm.doctorName")){
                setDoctorNameList(cur);

            }else if(date.equalsIgnoreCase("doctor")){
                setDoctorNameList(cur);
            }else if(date.equalsIgnoreCase("date")){
                setDateList(cur);
            }
            else if(date.equalsIgnoreCase("pm.uploadedBy")){
                setUploadedByList(cur);
            }


        } catch (Exception e) {
            e.getMessage();
        }

    }

    public FilterDataPrescription() {

    }

    public FilterDataPrescription(Cursor cur) {
        if (cur == null)
            return;
        try {

            setDateList(cur);
            setDoctorNameList(cur);
            setUploadedByList(cur);

        } catch (Exception e) {
            e.getMessage();
        }

    }

    public FilterDataPrescription(JSONObject jord) {
        try {
            setDateList(jord.getJSONArray("dateList"));
            setDoctorNameList(jord.getJSONArray("doctorNameList"));
//            setDiseaseNameList(jord.getJSONArray("diseaseNameList"));
            setUploadedByList(jord.getJSONArray("uploadedByList"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public ArrayList<String> getDateList() {
        return dateList;
    }

    public void setDateList(ArrayList<String> dateList) {
        this.dateList = dateList;
    }

    public ArrayList<String> getDoctorNameList() {
        return doctorNameList;
    }

    public void setDoctorNameList(ArrayList<String> doctorNameList) {
        this.doctorNameList = doctorNameList;
    }


    public ArrayList<String> getUploadedByList() {
        return uploadedByList;
    }

    public void setUploadedByList(ArrayList<String> uploadedByList) {
        this.uploadedByList = uploadedByList;
    }

    public void setDateList(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        this.dateList = new ArrayList<String>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                this.dateList.add(symptomslistArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setDateList(Cursor cur) {
        if (cur == null)
            return;
        this.dateList = new ArrayList<String>();
        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            try {
                this.dateList.add(cur.getString(cur.getColumnIndex("prescriptionDate")));
                cur.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDoctorNameList(Cursor cur) {
        if (cur == null)
            return;
        this.doctorNameList = new ArrayList<String>();
        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            try {
                this.doctorNameList.add(cur.getString(cur.getColumnIndex("doctorName")));
                cur.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setUploadedByList(Cursor cur) {
        if (cur == null)
            return;
        this.uploadedByList = new ArrayList<String>();
        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            try {
                this.uploadedByList.add(cur.getString(cur.getColumnIndex("uploadedBy")));
                cur.moveToNext();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setDoctorNameList(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        this.doctorNameList = new ArrayList<String>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                this.doctorNameList.add(symptomslistArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setUploadedByList(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        this.uploadedByList = new ArrayList<String>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                this.uploadedByList.add(symptomslistArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /*public void getInsertingValue(JSONObject json) {
        ContentValues values = new ContentValues();
        try {
            values.put(DATELIST, json.getString("dateList"));
            values.put(DOCTORNAMELIST, json.getString("doctorNameList"));
            values.put(UPLOADEDBYLIST, json.getString("uploadedByList"));
        }catch (Exception e){
            e.getMessage();
        }
        //DbOperations.insertPrescriptionList(CureFull.getInstanse().getActivityIsntanse(), values, AppPreference.getInstance().getcf_uuhidNeew(),getCommonID());

    }*/
}
