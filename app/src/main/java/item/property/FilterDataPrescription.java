package item.property;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sushant Hatcheryhub on 06-01-2017.
 */

public class FilterDataPrescription {

    ArrayList<String> dateList;
    ArrayList<String> doctorNameList;
    ArrayList<String> uploadedByList;

    public FilterDataPrescription() {


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
}
