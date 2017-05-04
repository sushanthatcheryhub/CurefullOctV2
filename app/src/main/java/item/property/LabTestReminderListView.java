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
public class LabTestReminderListView implements MyConstants.JsonUtils {

    private ArrayList<Lab_Test_Reminder_SelfListView> reminder_selfListViews;

    private ArrayList<LabTestReminderDoctorName> reminderDoctorNames;
    private ArrayList<Lab_Test_Reminder_DoctorListView> reminderDoctorNamesLocal;

    public LabTestReminderListView() {
    }

    public LabTestReminderListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setReminder_selfListViews(jsonObject.getJSONArray("labTestReminderBySelf"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("labTestReminderByDoctor"));
            setReminderDoctorNames(jsonToMap(jsonObject1));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public LabTestReminderListView(Cursor cur,String date) {
        if (cur == null)
            return;
        try {

            ArrayList<Lab_Test_Reminder_SelfListView> labReportImageListViews= DbOperations.getLabTestReportReminder11(CureFull.getInstanse().getActivityIsntanse(),date);
            setReminder_selfListViews(labReportImageListViews);
            //JSONObject jsonObject1 = new JSONObject(jsonObject.getString("labTestReminderByDoctor"));
            //setReminderDoctorNames(jsonToMap(jsonObject1));


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabTestReminderListView(Cursor cur,String status,String NIU,String NIU1) {
        if (cur == null)
            return;
        try {

            ArrayList<Lab_Test_Reminder_SelfListView> labReportImageListViews= DbOperations.getLabTestReportReminder11AfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);
            setReminder_selfListViews(labReportImageListViews);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabTestReminderListView(Cursor cur,String date,String doctor) {
        if (cur == null)
            return;
        try {

            ArrayList<Lab_Test_Reminder_DoctorListView> labReportImageListViews= DbOperations.getLabTestReportReminderDoctor11(CureFull.getInstanse().getActivityIsntanse(),date);
            Log.e("doc_labtest",""+labReportImageListViews);
            setReminderDoctorNamesLocal(labReportImageListViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LabTestReminderListView(Cursor cur,String status,String doctor,String NIU,String NIU1) {
        if (cur == null)
            return;
        try {

            ArrayList<Lab_Test_Reminder_DoctorListView> labReportImageListViews= DbOperations.getLabTestReportReminderDoctor11AfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);
            Log.e("doc_labtest",""+labReportImageListViews);
            setReminderDoctorNamesLocal(labReportImageListViews);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Lab_Test_Reminder_SelfListView> getReminder_selfListViews() {
        return reminder_selfListViews;
    }

    public void setReminder_selfListViews(ArrayList<Lab_Test_Reminder_SelfListView> reminder_selfListViews) {
        this.reminder_selfListViews = reminder_selfListViews;
    }


    public void setReminder_selfListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        Lab_Test_Reminder_SelfListView card = null;
        this.reminder_selfListViews = new ArrayList<Lab_Test_Reminder_SelfListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new Lab_Test_Reminder_SelfListView(symptomslistArray.getJSONObject(i));
                this.reminder_selfListViews.add(card);
                card.getInsertingValue(symptomslistArray.getJSONObject(i));

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
   /* public void setReminder_selfListViews(Cursor symptomslistArray) {
        if (symptomslistArray == null)
            return;
        Lab_Test_Reminder_SelfListView card = null;
        this.reminder_selfListViews = new ArrayList<Lab_Test_Reminder_SelfListView>();
        for (int i = 0; i < symptomslistArray.getCount(); i++) {
            try {
                card = new Lab_Test_Reminder_SelfListView(symptomslistArray);
                this.reminder_selfListViews.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    public void setReminderDoctorNames(Map<String, Object> stringObjectMap) {
        if (stringObjectMap == null)
            return;
        LabTestReminderDoctorName card = null;
        this.reminderDoctorNames = new ArrayList<LabTestReminderDoctorName>();
        for (Object o : stringObjectMap.keySet()) {
            try {
                card = new LabTestReminderDoctorName(o.toString(), stringObjectMap.get(o));
                this.reminderDoctorNames.add(card);

            } catch (Exception e) {
                e.printStackTrace();
            }
//
//                Log.e("checl", " " + entry.getKey() + " " + entry.getValue().get(i).getName() + " " + entry.getValue().get(i));
        }

    }

    public ArrayList<LabTestReminderDoctorName> getReminderDoctorNames() {
        return reminderDoctorNames;
    }

    public void setReminderDoctorNames(ArrayList<LabTestReminderDoctorName> reminderDoctorNames) {
        this.reminderDoctorNames = reminderDoctorNames;
    }

    public ArrayList<Lab_Test_Reminder_DoctorListView> getReminderDoctorNamesLocal() {
        return reminderDoctorNamesLocal;
    }

    public void setReminderDoctorNamesLocal(ArrayList<Lab_Test_Reminder_DoctorListView> reminderDoctorNamesLocal) {
        this.reminderDoctorNamesLocal = reminderDoctorNamesLocal;
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
