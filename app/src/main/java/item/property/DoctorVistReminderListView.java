package item.property;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class DoctorVistReminderListView implements MyConstants.JsonUtils {

    private ArrayList<Doctor_Visit_Reminder_SelfListView> reminder_selfListViews;
    private ArrayList<DoctorVisitReminderDoctorName> reminderDoctorNames;
    private ArrayList<Doctor_Visit_Reminder_DoctorListView> reminderDoctorNamesLocal;

    public DoctorVistReminderListView() {
    }

    public DoctorVistReminderListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setReminder_selfListViews(jsonObject.getJSONArray("listOfFollowupRemiderBySelf"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("listOfFollowupRemiderByDoctor"));
            setReminderDoctorNames(jsonToMap(jsonObject1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public DoctorVistReminderListView(Cursor cursorprivate, String datee) {
        if(cursorprivate==null){
            return;
        }
        try{
            ArrayList<Doctor_Visit_Reminder_SelfListView> labReportImageListViews= DbOperations.getDoctorReportReminder11(CureFull.getInstanse().getActivityIsntanse(),datee);
            setReminder_selfListViews(labReportImageListViews);

        }catch (Exception e){
            e.getMessage();
        }

    }
//for filter
    public DoctorVistReminderListView(Cursor cursorprivate, String status,String NIU,String NIU1) {
        if(cursorprivate==null){
            return;
        }
        try{
            ArrayList<Doctor_Visit_Reminder_SelfListView> labReportImageListViews= DbOperations.getDoctorReportReminder11AfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);
            setReminder_selfListViews(labReportImageListViews);

        }catch (Exception e){
            e.getMessage();
        }

    }
    public DoctorVistReminderListView(Cursor cursorprivate, String doctorName,String NIU,String NIU1,String NIU2,String NIU3) {
        if(cursorprivate==null){
            return;
        }
        try{
            ArrayList<Doctor_Visit_Reminder_SelfListView> labReportImageListViews= DbOperations.getDoctorReportReminder11BasedDoctor(CureFull.getInstanse().getActivityIsntanse(),doctorName);
            setReminder_selfListViews(labReportImageListViews);

        }catch (Exception e){
            e.getMessage();
        }

    }

    public DoctorVistReminderListView(Cursor cur,String datee,String doctor) {
        if (cur == null)
            return;
        try {

            ArrayList<Doctor_Visit_Reminder_DoctorListView> labReportImageListViews= DbOperations.getDoctorReportReminder11Curefull(CureFull.getInstanse().getActivityIsntanse(),datee);
            Log.e("doc_labtest",""+labReportImageListViews);
            setReminderDoctorNamesLocal(labReportImageListViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DoctorVistReminderListView(Cursor cur,String status,String doctor,String NIU,String NIU1) {
        if (cur == null)
            return;
        try {

            ArrayList<Doctor_Visit_Reminder_DoctorListView> labReportImageListViews= DbOperations.getDoctorReportReminder11CurefullAfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);

            setReminderDoctorNamesLocal(labReportImageListViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public DoctorVistReminderListView(Cursor cur,String doctor,String NIUU,String NIU,String NIU1,String NIU2,String NIU3) {
        if (cur == null)
            return;
        try {

            ArrayList<Doctor_Visit_Reminder_DoctorListView> labReportImageListViews= DbOperations.getDoctorReportReminderCurefull11BasedDoctor(CureFull.getInstanse().getActivityIsntanse(),doctor);

            setReminderDoctorNamesLocal(labReportImageListViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public ArrayList<Doctor_Visit_Reminder_SelfListView> getReminder_selfListViews() {
        return reminder_selfListViews;
    }

    public void setReminder_selfListViews(ArrayList<Doctor_Visit_Reminder_SelfListView> reminder_selfListViews) {
        this.reminder_selfListViews = reminder_selfListViews;
    }


    public void setReminder_selfListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        Doctor_Visit_Reminder_SelfListView card = null;
        this.reminder_selfListViews = new ArrayList<Doctor_Visit_Reminder_SelfListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new Doctor_Visit_Reminder_SelfListView(symptomslistArray.getJSONObject(i));
                this.reminder_selfListViews.add(card);
                card.getInsertingValue(symptomslistArray.getJSONObject(i));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void setReminderDoctorNames(Map<String, Object> stringObjectMap) {
        if (stringObjectMap == null)
            return;
        DoctorVisitReminderDoctorName card = null;
        this.reminderDoctorNames = new ArrayList<DoctorVisitReminderDoctorName>();
        for (Object o : stringObjectMap.keySet()) {
            try {
                card = new DoctorVisitReminderDoctorName(o.toString(), stringObjectMap.get(o));
                this.reminderDoctorNames.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
//
//                Log.e("checl", " " + entry.getKey() + " " + entry.getValue().get(i).getName() + " " + entry.getValue().get(i));
        }

    }

    public ArrayList<DoctorVisitReminderDoctorName> getReminderDoctorNames() {
        return reminderDoctorNames;
    }

    public void setReminderDoctorNames(ArrayList<DoctorVisitReminderDoctorName> reminderDoctorNames) {
        this.reminderDoctorNames = reminderDoctorNames;
    }

    public void setReminderDoctorNamesLocal(ArrayList<Doctor_Visit_Reminder_DoctorListView> reminderDoctorNamesLocal) {
        this.reminderDoctorNamesLocal = reminderDoctorNamesLocal;
    }
    public ArrayList<Doctor_Visit_Reminder_DoctorListView> getReminderDoctorNamesLocal() {
        return reminderDoctorNamesLocal;
    }

    public static Map<String, Object> jsonToMap(JSONObject json) throws JSONException {
        Map<String, Object> retMap = new HashMap<String, Object>();

        if (json != JSONObject.NULL) {
            retMap = toMap(json);
        }
        return retMap;
    }

    public static Map<String, Object> toMap(JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();

        Iterator<String> keysItr = object.keys();
        while (keysItr.hasNext()) {
            String key = keysItr.next();
            Object value = object.get(key);

            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            map.put(key, value);
        }
        return map;
    }

    public static List<Object> toList(JSONArray array) throws JSONException {
        List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < array.length(); i++) {
            Object value = array.get(i);
            if (value instanceof JSONArray) {
                value = toList((JSONArray) value);
            } else if (value instanceof JSONObject) {
                value = toMap((JSONObject) value);
            }
            list.add(value);
        }
        return list;
    }


}
