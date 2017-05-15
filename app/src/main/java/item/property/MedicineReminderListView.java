package item.property;

import android.database.Cursor;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import okhttp3.RequestBody;
import operations.DbOperations;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class MedicineReminderListView implements MyConstants.JsonUtils {

    private ArrayList<Reminder_SelfListView> reminder_selfListViews;

    private ArrayList<ReminderDoctorName> reminderDoctorNames;
    private ArrayList<Reminder_DoctorListView> reminderDoctorNamesLocal;
  //  private ArrayList<Reminder_SelfListView> medicineReminderBySelf;
  //  Reminder_SelfListView  reminder_selfListViews1;
    public MedicineReminderListView() {
    }

    public MedicineReminderListView(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setReminder_selfListViews(jsonObject.getJSONArray("medicineReminderBySelf"));
            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("listOfMedicineUnderDoctor"));
            setReminderDoctorNames(jsonToMap(jsonObject1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public MedicineReminderListView(Cursor cursorprivate,String datee) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminder_selfListViews= DbOperations.getMedicineReportReminder11(CureFull.getInstanse().getActivityIsntanse(),datee);//,datee,commonid_from_innerquery
            Log.e("test",reminder_selfListViews.toString());

        }catch (Exception e){
            e.getMessage();
        }

    }


    public MedicineReminderListView(String currentdatee,String previousdate,Cursor cursorprivate) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminder_selfListViews= DbOperations.getMedicineReportHistoryReminder11(CureFull.getInstanse().getActivityIsntanse(),currentdatee,previousdate);//,datee,commonid_from_innerquery
            Log.e("test",reminder_selfListViews.toString());

        }catch (Exception e){
            e.getMessage();
        }

    }

    public MedicineReminderListView(Cursor cursorprivate,String status,String NIU,String NIU1) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminder_selfListViews= DbOperations.getMedicineReportReminder11AfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);//,datee,commonid_from_innerquery
            Log.e("test",reminder_selfListViews.toString());

        }catch (Exception e){
            e.getMessage();
        }

    }

    public MedicineReminderListView(Cursor cursorprivate,String doctorName,String NIU,String NIU1,String NIU2,String NIU3) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminder_selfListViews= DbOperations.getMedicineReportReminder11BasedDoctor(CureFull.getInstanse().getActivityIsntanse(),doctorName);
            Log.e("test",reminder_selfListViews.toString());

        }catch (Exception e){
            e.getMessage();
        }

    }
   /* public void setReminderDoctorNamesLocal(ArrayList<Reminder_DoctorListView> reminderDoctorNamesLocal) {
        this.reminderDoctorNamesLocal = reminderDoctorNamesLocal;
    }*/

    public ArrayList<Reminder_DoctorListView> getReminderDoctorNamesLocal() {
        return reminderDoctorNamesLocal;
    }

    public MedicineReminderListView(Cursor cursorprivate, String datee, String NIU) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminderDoctorNamesLocal= DbOperations.getMedicineReportReminder11Doctor(CureFull.getInstanse().getActivityIsntanse(),datee);//,datee,commonid_from_innerquery
            Log.e("testDoctor",reminderDoctorNames.toString());
           // setReminderDoctorNamesLocal(reminderDoctorNamesLocal);

        }catch (Exception e){
            e.getMessage();
        }

    }

    public MedicineReminderListView(String currentdate, String previousdate,Cursor cursorprivate,String NIU1,String NIU2,String NIU3,String NIU4,String NIU5) {
        //, String datee,String commonid_from_innerquery
        if(cursorprivate==null){
            return;
        }
        try{
            reminderDoctorNamesLocal= DbOperations.getMedicineReportHistoryReminder11ByDoctor(CureFull.getInstanse().getActivityIsntanse(),currentdate,previousdate);//,datee,commonid_from_innerquery
            Log.e("testDoctor",reminderDoctorNames.toString());
            // setReminderDoctorNamesLocal(reminderDoctorNamesLocal);

        }catch (Exception e){
            e.getMessage();
        }

    }
    public MedicineReminderListView(Cursor cursorprivate, String status, String NIU,String NIU1,String NIU2) {

        if(cursorprivate==null){
            return;
        }
        try{
            reminderDoctorNamesLocal= DbOperations.getMedicineReportReminder11DoctorAfterSelection(CureFull.getInstanse().getActivityIsntanse(),status);

        }catch (Exception e){
            e.getMessage();
        }

    }


    public MedicineReminderListView(Cursor cursorprivate, String doctorName, String NIU,String NIU1,String NIU2,String NIU3,String NIU4) {

        if(cursorprivate==null){
            return;
        }
        try{
            reminderDoctorNamesLocal= DbOperations.getMedicineReportReminder11DoctorBasedDoctorName(CureFull.getInstanse().getActivityIsntanse(),doctorName);

        }catch (Exception e){
            e.getMessage();
        }

    }


    public ArrayList<Reminder_SelfListView> getReminder_selfListViews() {
        return reminder_selfListViews;
    }


    public void setReminder_selfListViews(JSONArray symptomslistArray) {
        if (symptomslistArray == null)
            return;
        Reminder_SelfListView card = null;
        this.reminder_selfListViews = new ArrayList<Reminder_SelfListView>();
        for (int i = 0; i < symptomslistArray.length(); i++) {
            try {
                card = new Reminder_SelfListView(symptomslistArray.getJSONObject(i));
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
        ReminderDoctorName card = null;
        this.reminderDoctorNames = new ArrayList<ReminderDoctorName>();
        for (Object o : stringObjectMap.keySet()) {
            try {
                card = new ReminderDoctorName(o.toString(), stringObjectMap.get(o));
                this.reminderDoctorNames.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
//
//                Log.e("checl", " " + entry.getKey() + " " + entry.getValue().get(i).getName() + " " + entry.getValue().get(i));
        }

    }

    public ArrayList<ReminderDoctorName> getReminderDoctorNames() {
        return reminderDoctorNames;
    }

    public void setReminderDoctorNames(ArrayList<ReminderDoctorName> reminderDoctorNames) {
        this.reminderDoctorNames = reminderDoctorNames;
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
