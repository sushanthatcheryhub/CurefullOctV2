package item.property;

import android.database.Cursor;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sushant Hatcheryhub on 06-01-2017.
 */

public class FilterDataReports {

    ArrayList<String> dateList;
    ArrayList<String> doctorNameList;
    ArrayList<String> diseaseNameList;
    ArrayList<String> uploadedByList;

    public FilterDataReports() {


    }
    public FilterDataReports(Cursor cur, String date) {
        if (cur == null)
            return;
        try {
            if (date.equalsIgnoreCase("pm.reportDate")) {
                setDateList(cur);
            } else if(date.equalsIgnoreCase("pm.doctorName")){
                setDoctorNameList(cur);

            }else if(date.equalsIgnoreCase("pm.testName")) {
                setDiseaseNameList(cur);
            }
            else if(date.equalsIgnoreCase("doctor")) {
                setDoctorNameList(cur);
            }else if(date.equalsIgnoreCase("date")) {
                setDateList(cur);
            }
            else if(date.equalsIgnoreCase("disease")) {
                setDateList(cur);
            }else if(date.equalsIgnoreCase("datedoctor")){
                setDiseaseNameList(cur);
            }else if(date.equalsIgnoreCase("datetest")) {
                setDoctorNameList(cur);
            }else if(date.equalsIgnoreCase("doctortest")){
                setDateList(cur);
            }
            else if(date.equalsIgnoreCase("pm.uploadedBy"))
            {
                setUploadedByList(cur);
            }


        } catch (Exception e) {
            e.getMessage();
        }

    }
    public FilterDataReports(JSONObject jord) {
        try {
            setDateList(jord.getJSONArray("dateList"));
            setDoctorNameList(jord.getJSONArray("doctorNameList"));
            setDiseaseNameList(jord.getJSONArray("testNameList"));
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

    public ArrayList<String> getDiseaseNameList() {
        return diseaseNameList;
    }

    public void setDiseaseNameList(ArrayList<String> diseaseNameList) {
        this.diseaseNameList = diseaseNameList;
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

    public void setDiseaseNameList(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        this.diseaseNameList = new ArrayList<String>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                this.diseaseNameList.add(symptomslistArray.get(i).toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

//cursor
    public void setDateList(Cursor cur) {
        if (cur == null)
            return;
        this.dateList = new ArrayList<String>();
        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            try {
                this.dateList.add(cur.getString(cur.getColumnIndex("reportDate")));
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
    public void setDiseaseNameList(Cursor cur) {
        if (cur == null)
            return;
        this.diseaseNameList = new ArrayList<String>();
        cur.moveToFirst();
        for (int i = 0; i < cur.getCount(); i++) {
            try {
                this.diseaseNameList.add(cur.getString(cur.getColumnIndex("testName")));
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

    public ArrayList<String> getUploadedByList() {
        return uploadedByList;
    }

    public void setUploadedByList(ArrayList<String> uploadedByList) {
        this.uploadedByList = uploadedByList;
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
}
