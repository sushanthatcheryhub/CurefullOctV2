package item.property;

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

    public FilterDataReports() {


    }

    public FilterDataReports(JSONObject jord) {
        try {
            setDateList(jord.getJSONArray("dateList"));
            setDoctorNameList(jord.getJSONArray("doctorNameList"));
            setDiseaseNameList(jord.getJSONArray("testNameList"));
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

}
