package operations;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import fragment.healthapp.FragmentPrescriptionCheckNew;
import item.property.DoctorVistReminderListView;
import item.property.Doctor_Visit_Reminder_SelfListView;
import item.property.FilterDataPrescription;
import item.property.FilterDataReports;
import item.property.GoalInfo;
import item.property.HealthNoteItems;
import item.property.LabDoctorName;
import item.property.LabReportImageListView;
import item.property.LabReportListView;
import item.property.LabTestReminderListView;
import item.property.Lab_Test_Reminder_SelfListView;
import item.property.MedicineReminderListView;
import item.property.PrescriptionImageFollowUpListView;
import item.property.PrescriptionImageListView;
import item.property.PrescriptionListView;
import item.property.ReminderMedicnceDoagePer;
import item.property.ReminderMedicnceTime;
import item.property.Reminder_SelfListView;
import item.property.StepsCountsItems;
import item.property.StepsCountsStatus;
import item.property.UHIDItems;
import item.property.UserInfo;
import utils.AppPreference;
import utils.MyConstants;
import utils.NotificationUtils;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class DbOperations implements MyConstants.IDataBaseTableNames, MyConstants.IDataBaseTableKeys, MyConstants.JsonUtils {
    static Cursor cursor = null;
    static Cursor cursor_reset = null;

    public static List<HealthNoteItems> getNoteList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<HealthNoteItems>();
        List<HealthNoteItems> listApps = new ArrayList<HealthNoteItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_NOTE
                    + " P WHERE P.cf_uuhid = '" + AppPreference.getInstance().getcf_uuhid() + "' ORDER BY P.dateOfNote DESC";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    HealthNoteItems content = new HealthNoteItems(cursor);
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static List<UHIDItems> getUHIDListLocal(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<UHIDItems>();
        List<UHIDItems> listApps = new ArrayList<UHIDItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_UHID
                    + " P WHERE P.cfhid_user = '" + AppPreference.getInstance().getcf_uuhid() + "'";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    UHIDItems content = new UHIDItems(cursor);
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //List<LabDoctorName>
    public static List<LabDoctorName> getLabDoctorReminderListLocal(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<LabDoctorName>();
        List<LabDoctorName> listApps = new ArrayList<LabDoctorName>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_LAB_REMINDER_DOCTOR_NAME
                    + " P WHERE P.cfuuhid = '" + AppPreference.getInstance().getcf_uuhid() + "'";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    LabDoctorName content = new LabDoctorName(cursor);
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static List<StepsCountsItems> getOfflineSteps(Context context, String cfhuid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<StepsCountsItems>();
        List<StepsCountsItems> listApps = new ArrayList<StepsCountsItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_STEPS
                    + " P WHERE P.cf_uuhid = '" + cfhuid + "'";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    StepsCountsItems content = new StepsCountsItems(cursor);
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static List<HealthNoteItems> getOfflineNoteList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<HealthNoteItems>();
        List<HealthNoteItems> listApps = new ArrayList<HealthNoteItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_OFFLINE_NOTE
                    + " P WHERE P.cf_uuhid = '" + AppPreference.getInstance().getcf_uuhid() + "' AND P.isSent='0'";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    HealthNoteItems content = new HealthNoteItems(cursor, "");
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static List<HealthNoteItems> getNoteListLanding(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<HealthNoteItems>();
        List<HealthNoteItems> listApps = new ArrayList<HealthNoteItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
// LIMIT 0,10
            String query = "SELECT P.* FROM " + TABLE_NOTE
                    + " P WHERE P.cf_uuhid = '" + AppPreference.getInstance().getcf_uuhid() + "' ORDER BY P.year DESC LIMIT 4";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    HealthNoteItems content = new HealthNoteItems(cursor);
                    listApps.add(content);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static List<String> getEmailList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<String>();
        List<String> listApps = new ArrayList<String>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();

            String query = "SELECT P.* FROM " + TABLE_EMAIL
                    + " P ORDER BY P.id DESC ";
            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    listApps.add(cursor.getString(cursor.getColumnIndex("email_id")));
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static UserInfo getLoginList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new UserInfo();
        UserInfo listApps = null;
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT L.* FROM " + TABLE_LOGIN
                    + " L WHERE L.cf_uuhid = '" + AppPreference.getInstance().getcf_uuhid() + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    listApps = new UserInfo(cursor);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static String getGraphList(Context context, String cf_uuhid, String graph_type, String graph_frequecy) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return "";
        String response = "";
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT P.* FROM " + TABLE_GRAPH
                    + " P WHERE P.cf_uuhid = '" + cf_uuhid + "' AND graph_type = '" + graph_type + "' AND graph_frequecy = '" + graph_frequecy + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                response = cursor.getString(cursor.getColumnIndex("graph_data"));
//                cursor.moveToNext();
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return response;
    }

    /*public static String getPrescriptionList(Context context, String cf_uuhid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return "";
        String response = "";
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT P.* FROM " + TABLE_PRESCRIPTION
                    + " P WHERE P.cf_uuhid = '" + cf_uuhid + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                response = cursor.getString(cursor.getColumnIndex("prescription_data"));

            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return response;
    }
*/
    public static List<PrescriptionListView> getPrescriptionListALL(Context context, String cf_uuhid) {
        Cursor cursorprivate = null;

        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<PrescriptionListView>();

        List<PrescriptionListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.cfUuhid='" + cf_uuhid + "'";

            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    PrescriptionListView content = new PrescriptionListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //for labtestreport main
    public static List<LabReportListView> getLabTestReportListALL(Context context, String cf_uuhid) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<LabReportListView>();

        List<LabReportListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String isupload = "0";
            database = DatabaseHelper.openDataBase();
            String query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.cfUuhid='" + cf_uuhid + "' ";
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    LabReportListView content = new LabReportListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static LabTestReminderListView getLabTestReportReminder(Context context, String cf_uuhid, String datee) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new LabTestReminderListView();

        //List<LabReportListView> listApps = new ArrayList<>();
        LabTestReminderListView content = null;
        SQLiteDatabase database = null;
        try {
            String[] date = datee.split("-");
            String year = date[0];
            String month = date[1];
            String dayy = date[2];

            database = DatabaseHelper.openDataBase();
            //"SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " Where labTestReminderId='" + labTestReminderId + "'
            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " where cfuuhId='" + cf_uuhid + "' and dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'";
            cursorprivate = database.rawQuery(query, null);
            int aa = cursorprivate.getCount();
            if (cursorprivate.getCount() > 0) {

                content = new LabTestReminderListView(cursorprivate, datee);

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }

    public static DoctorVistReminderListView getDoctorReportReminder(Context context, String cf_uuhid, String datee) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new DoctorVistReminderListView();

        //List<LabReportListView> listApps = new ArrayList<>();
        DoctorVistReminderListView content = null;
        SQLiteDatabase database = null;
        try {
            String[] date = datee.split("-");
            String year = date[0];
            String month = date[1];
            String dayy = date[2];

            database = DatabaseHelper.openDataBase();
            //"SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " Where labTestReminderId='" + labTestReminderId + "'
            String query = "SELECT * FROM " + TABLE_DOCTOR_REMINDER_SELF + " where cfuuhId='" + cf_uuhid + "' and dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'";
            cursorprivate = database.rawQuery(query, null);
            int aa = cursorprivate.getCount();
            if (cursorprivate.getCount() > 0) {

                content = new DoctorVistReminderListView(cursorprivate, datee);

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }

    public static MedicineReminderListView getMedicineReportReminder(Context context, String cf_uuhid, String datee) {
        Cursor cursorprivate = null;
        Cursor cursornew = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new MedicineReminderListView();

        List<ReminderMedicnceDoagePer> listApps = new ArrayList<>();
        MedicineReminderListView content = null;
        SQLiteDatabase database = null;
        try {
          /*  String[] date = datee.split("-");
            String year = date[0];
            String month = date[1];
            String dayy = date[2];*/


            database = DatabaseHelper.openDataBase();


            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " where cfuuhId='" + cf_uuhid + "' and currentdate='" + datee + "'";//dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'
            cursorprivate = database.rawQuery(query, null);
            int aa = cursorprivate.getCount();
            if (cursorprivate.getCount() > 0) {
                content = new MedicineReminderListView(cursorprivate, datee);

            }
            cursorprivate.close();
            database.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }

    public static MedicineReminderListView getMedicineReportHistoryReminder(Context context, String cf_uuhid, String datee) {
        Cursor cursorprivate = null;
        Cursor cursornew = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new MedicineReminderListView();

        List<ReminderMedicnceDoagePer> listApps = new ArrayList<>();
        MedicineReminderListView content = null;
        SQLiteDatabase database = null;
        try {
          /*  String[] date = datee.split("-");
            String year = date[0];
            String month = date[1];
            String dayy = date[2];*/
            String currentdate=Utils.getTodayDate();
            String previousdate=Utils.getPreviousDate(currentdate);
            database = DatabaseHelper.openDataBase();


            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " where cfuuhId='" + cf_uuhid + "' and currentdate BETWEEN '"+previousdate+"' AND '"+currentdate+"'";//dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'
            cursorprivate = database.rawQuery(query, null);//
            int aa = cursorprivate.getCount();
            if (cursorprivate.getCount() > 0) {
                content = new MedicineReminderListView(cursorprivate, datee);

            }
            cursorprivate.close();
            database.close();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }


    public static ArrayList<Reminder_SelfListView> getMedicineReportReminder11(Context context, String datee) {//, String datee, String commonid_from_innerquery
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Reminder_SelfListView>();

        //List<LabReportListView> listApps = new ArrayList<>();
        ArrayList<Reminder_SelfListView> content = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            /*String[] date = datee.split("-");
            String year = date[0];
            String month = date[1];
            String dayy = date[2];*/

            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " where cfuuhId='" + AppPreference.getInstance().getcf_uuhid() + "' and currentdate='" + datee + "'";// and common_id='" + commonid_from_innerquery + "'    //dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'
            cursorprivate = database.rawQuery(query, null);
            int aa = cursorprivate.getCount();
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    Reminder_SelfListView content1 = new Reminder_SelfListView(cursorprivate);
                    content.add(content1);
                    cursorprivate.moveToNext();

                }
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }

    public static ArrayList<Lab_Test_Reminder_SelfListView> getLabTestReportReminder11(Context context, String date) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Lab_Test_Reminder_SelfListView>();

        ArrayList<Lab_Test_Reminder_SelfListView> listApps = new ArrayList<>();
        Lab_Test_Reminder_SelfListView content = null;
        SQLiteDatabase database = null;

        try {
            String[] date1 = date.split("-");
            String year = date1[0];
            String month = date1[1];
            String dayy = date1[2];

            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " where cfuuhId='" + AppPreference.getInstance().getcf_uuhid() + "' and dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'";
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    content = new Lab_Test_Reminder_SelfListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static ArrayList<Doctor_Visit_Reminder_SelfListView> getDoctorReportReminder11(Context context, String date) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Doctor_Visit_Reminder_SelfListView>();

        ArrayList<Doctor_Visit_Reminder_SelfListView> listApps = new ArrayList<>();
        Doctor_Visit_Reminder_SelfListView content = null;
        SQLiteDatabase database = null;

        try {
            String[] date1 = date.split("-");
            String year = date1[0];
            String month = date1[1];
            String dayy = date1[2];

            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_DOCTOR_REMINDER_SELF + " where cfuuhId='" + AppPreference.getInstance().getcf_uuhid() + "' and dayOfMonth='" + dayy + "' and monthValue='" + month + "' and year='" + year + "'";
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    content = new Doctor_Visit_Reminder_SelfListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static List<LabReportListView> getLabTestReportListALLSort(Context context, String cf_uuhid, String clickShortBy) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<LabReportListView>();
        String query = "";
        List<LabReportListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            String isupload = "0";
            database = DatabaseHelper.openDataBase();
            if (clickShortBy.equalsIgnoreCase("DESC")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.cfUuhid='" + cf_uuhid + "' order by pm.reportDate DESC";//where pm.reportDate='"+ Utils.getTodayDate()+"'and pm.isUploaded='"+isupload+"'
            } else {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.cfUuhid='" + cf_uuhid + "' order by pm.reportDate ASC";//where pm.reportDate='"+ Utils.getTodayDate()+"'
            }
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    LabReportListView content = new LabReportListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //getFilDataPrescDynamicSearch
    public static FilterDataPrescription getFilDataPrescDateClick(Context context, String date, String clickDates, String clickDoctorName, String clickUploadBy) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new FilterDataPrescription();
        FilterDataPrescription content = null;
        SQLiteDatabase database = null;
        String query = "";
        try {
            database = DatabaseHelper.openDataBase();
            //clickDates

            if (date.equalsIgnoreCase("doctor")) {
                if (!clickDates.equalsIgnoreCase("")) {
                    query = "select pm.doctorName from tbl_prescription_main as pm where pm.prescriptionDate='" + clickDates + "' GROUP BY pm.doctorName";
                }
            }

            if (date.equalsIgnoreCase("date")) {
                if (!clickDoctorName.equalsIgnoreCase("")) {
                    query = "select pm.prescriptionDate from tbl_prescription_main as pm where pm.doctorName='" + clickDoctorName + "' GROUP BY pm.prescriptionDate";
                }
            }
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {

                content = new FilterDataPrescription(cursorprivate, date);
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }


    public static FilterDataReports getFilLabTestDateClick(Context context, String date, String clickDates, String clickDoctorName, String clickDiseaseName) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new FilterDataReports();
        FilterDataReports content = null;
        SQLiteDatabase database = null;
        String query = "";
        try {
            database = DatabaseHelper.openDataBase();
            //clickDates
//reportDate
            //clickdate
            if (date.equalsIgnoreCase("doctor")) {
                if (!clickDates.equalsIgnoreCase("")) {
                    query = "select pm.doctorName from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' GROUP BY pm.doctorName";
                }
            }

//
            if (date.equalsIgnoreCase("date")) {
                if (!clickDoctorName.equalsIgnoreCase("")) {
                    query = "select pm.reportDate from tbl_labtestreprort_main as pm where pm.doctorName='" + clickDoctorName + "' GROUP BY pm.reportDate";
                }
            }

            if (date.equalsIgnoreCase("disease")) {
                if (!clickDiseaseName.equalsIgnoreCase("")) {
                    query = "select pm.reportDate from tbl_labtestreprort_main as pm where pm.testName='" + clickDiseaseName + "' GROUP BY pm.reportDate";
                }
            }

            if (date.equalsIgnoreCase("datedoctor")) {
                if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("")) {
                    query = "select pm.testName from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "' GROUP BY pm.testName";
                }
            }

            if (date.equalsIgnoreCase("datetest")) {
                if (!clickDates.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                    query = "select pm.doctorName from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.testName='" + clickDiseaseName + "' GROUP BY pm.doctorName";
                }
            }
            if (date.equalsIgnoreCase("doctortest")) {
                if (!clickDoctorName.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                    query = "select pm.reportDate from tbl_labtestreprort_main as pm where pm.doctorName='" + clickDoctorName + "' and pm.testName='" + clickDiseaseName + "' GROUP BY pm.reportDate";
                }
            }

            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {

                content = new FilterDataReports(cursorprivate, date);
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }


    public static FilterDataPrescription getFilterDataPrescriptionDate(Context context, String date, String cfhuid) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new FilterDataPrescription();
        FilterDataPrescription content = null;
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select " + date + " from tbl_prescription_main as pm where cfUuhid='" + cfhuid + "' GROUP BY " + date;//
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {

                content = new FilterDataPrescription(cursorprivate, date);
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }

    public static FilterDataReports getFilterDataReports(Context context, String date, String cfhuid) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new FilterDataReports();
//pm.reportDate,pm.doctorName,pm.uploadedBy
        FilterDataReports content = null;
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select " + date + " from tbl_labtestreprort_main as pm where cfUuhid='" + cfhuid + "' GROUP BY " + date;
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {

                content = new FilterDataReports(cursorprivate, date);
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return content;
    }


    public static List<PrescriptionListView> getFilterDataPrescriptionAfterSelection(Context context, String clickDates, String clickDoctorName, String clickUploadBy) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<>();
//pm.prescriptionDate,pm.doctorName,pm.uploadedBy
        List<PrescriptionListView> listAlls = new ArrayList<>();
        SQLiteDatabase database = null;
        String query = "";
        try {

            database = DatabaseHelper.openDataBase();
            if (clickDoctorName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm";//GROUP BY " + date;
            }
            if (!clickDates.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.prescriptionDate='" + clickDates + "'";// GROUP BY " + date;
            }
            if (!clickDoctorName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.doctorName='" + clickDoctorName + "'"; //GROUP BY " + date;
            }
            if (!clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.uploadedBy='" + clickUploadBy + "' and pm.cfUuhid='" + AppPreference.getInstance().getcf_uuhidNeew() + "'"; //GROUP BY " + date;
            }
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.prescriptionDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "'";// GROUP BY " + date;
            }
            if (!clickDates.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.uploadedBy='" + clickUploadBy + "' and pm.prescriptionDate='" + clickDates + "'";//  GROUP BY " + date;
            }
            if (!clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "'";//  GROUP BY " + date;
            }
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.prescriptionDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "'";// GROUP BY " + date;
            }

            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    PrescriptionListView content = new PrescriptionListView(cursorprivate);
                    listAlls.add(content);
                    cursorprivate.moveToNext();
                }
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listAlls;
    }

    //clickDates,clickDoctorName,clickDiseaseName,clickUploadBy
    public static List<LabReportListView> getLabTestReportListAfterSelection(Context context, String cfUuhid, String clickDates, String clickDoctorName, String clickDiseaseName, String clickUploadBy) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<>();

        List<LabReportListView> listAlls = new ArrayList<>();
        SQLiteDatabase database = null;
        String query = "";
        try {
            // select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id from tbl_labtestreprort_main as pm
            database = DatabaseHelper.openDataBase();
            //0
            if (clickDoctorName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm";
            }
            //1
            if (!clickDates.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "'";
            }
            if (!clickDoctorName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.doctorName='" + clickDoctorName + "'";
            }
            if (!clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.uploadedBy='" + clickUploadBy + "' and pm.cfUuhid='" + AppPreference.getInstance().getcf_uuhidNeew() + "'";
            }

            if (!clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.testName='" + clickDiseaseName + "'";
            }
            //2
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "'";// GROUP BY " + date;
            }

            if (!clickDates.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.uploadedBy='" + clickUploadBy + "' and pm.reportDate='" + clickDates + "'";//  GROUP BY " + date;
            }

            if (!clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "'";
            }
            if (!clickDoctorName.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.doctorName='" + clickDoctorName + "' and pm.testName='" + clickDiseaseName + "'";
            }
            if (!clickUploadBy.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.uploadedBy='" + clickUploadBy + "' and pm.testName='" + clickDiseaseName + "'";
            }
            if (!clickDates.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.testName='" + clickDiseaseName + "'";
            }
            //3
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "' and pm.testName='" + clickDiseaseName + "'";
            }
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "'";
            }

            if (!clickDates.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.testName='" + clickDiseaseName + "' and pm.uploadedBy='" + clickUploadBy + "'";
            }

            if (!clickDiseaseName.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.testName='" + clickDiseaseName + "' and pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "'";
            }
            //4
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("") && !clickUploadBy.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.reportDate,pm.reportId,pm.testName,pm.uploadedBy,pm.dateOfUpload,pm.common_id,pm.isUploaded from tbl_labtestreprort_main as pm where pm.reportDate='" + clickDates + "' and pm.doctorName='" + clickDoctorName + "' and pm.uploadedBy='" + clickUploadBy + "' and pm.testName='" + clickDiseaseName + "'";
            }

            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    LabReportListView content = new LabReportListView(cursorprivate);
                    listAlls.add(content);
                    cursorprivate.moveToNext();
                }
            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listAlls;
    }


    public static List<PrescriptionListView> getPrescriptionListALLSort(Context context, String cf_uuhid, String clickShortBy, int offset) {
        Cursor cursorprivate = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<PrescriptionListView>();

        List<PrescriptionListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        String query = "";
        try {
            database = DatabaseHelper.openDataBase();
            if (clickShortBy.equalsIgnoreCase("DESC")) {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.cfUuhid='" + cf_uuhid + "' order by pm.prescriptionDate DESC";//where pm.prescriptionDate='"+ Utils.getTodayDate()+"'
            } else {
                query = "select pm.cfUuhid,pm.countOfFiles,pm.doctorName,pm.prescriptionDate,pm.prescriptionId,pm.uploadedBy,pm.common_id,pm.isUploaded from tbl_prescription_main as pm where pm.cfUuhid='" + cf_uuhid + "' order by pm.prescriptionDate ASC";//where pm.prescriptionDate='"+ Utils.getTodayDate()+"'
            }
            //select * from table name where  prescriptionDate order by desc/asc(this is varialble) limit 0,10;
            cursorprivate = database.rawQuery(query, null);
            if (cursorprivate.getCount() > 0) {
                cursorprivate.moveToFirst();
                int ss = cursorprivate.getCount();
                for (int i = 0; i < ss; i++) {
                    PrescriptionListView content = new PrescriptionListView(cursorprivate);
                    listApps.add(content);
                    cursorprivate.moveToNext();
                }

            }
            cursorprivate.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static ArrayList<PrescriptionImageFollowUpListView> setPrescriptionImageFollowUpListViewsLocal(Context context, String common_id) {
        Cursor cursorprivate1 = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<PrescriptionImageFollowUpListView>();

        ArrayList<PrescriptionImageFollowUpListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select pfl.prescriptonImageFollowupId from tbl_prescription_followuplist as pfl where pfl.common_id ='" + common_id + "'";
            cursorprivate1 = database.rawQuery(query, null);
            if (cursorprivate1.getCount() > 0) {
                cursorprivate1.moveToFirst();
                int ss = cursorprivate1.getCount();
                for (int i = 0; i < ss; i++) {
                    PrescriptionImageFollowUpListView content = new PrescriptionImageFollowUpListView(cursorprivate1, common_id);
                    listApps.add(content);
                    cursorprivate1.moveToNext();
                }

            }
            cursorprivate1.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static ArrayList<PrescriptionImageListView> setPrescriptionResponseListViewsLocal(Context context, String common_id) {
        Cursor cursorprivate2 = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<PrescriptionImageListView>();

        ArrayList<PrescriptionImageListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select prl.imageNumber,prl.prescriptionImage,prl.prescriptionImagePartId from tbl_prescription_response_list as prl where prl.common_id ='" + common_id + "'";

            cursorprivate2 = database.rawQuery(query, null);
            if (cursorprivate2.getCount() > 0) {
                cursorprivate2.moveToFirst();
                int ss = cursorprivate2.getCount();
                for (int i = 0; i < ss; i++) {
                    PrescriptionImageListView content = new PrescriptionImageListView(cursorprivate2);
                    listApps.add(content);
                    cursorprivate2.moveToNext();
                }

            }
            cursorprivate2.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static ArrayList<LabReportImageListView> setLabTestReportResponseListViewsLocal(Context context, String common_id) {
        Cursor cursorprivate2 = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<LabReportImageListView>();

        ArrayList<LabReportImageListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select prl.imageNumber,prl.reportImage,prl.reportImageId from tbl_labtestreport_response_list as prl where prl.common_id ='" + common_id + "'";

            cursorprivate2 = database.rawQuery(query, null);
            if (cursorprivate2.getCount() > 0) {
                cursorprivate2.moveToFirst();
                int ss = cursorprivate2.getCount();
                for (int i = 0; i < ss; i++) {
                    LabReportImageListView content = new LabReportImageListView(cursorprivate2);
                    listApps.add(content);
                    cursorprivate2.moveToNext();
                }

            }
            cursorprivate2.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //ReminderMedicnceDoagePer
    public static ArrayList<ReminderMedicnceDoagePer> setReminderMedicineDosageLocal(Context context, String common_id,String date) {
        Cursor cursorprivate2 = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<ReminderMedicnceDoagePer>();

        ArrayList<ReminderMedicnceDoagePer> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select status,date,common_id,data_id from tbl_medicine_reminder_self_dosageperDateResponse where common_id ='" + common_id + "' and date='"+date+"'";

            cursorprivate2 = database.rawQuery(query, null);
            if (cursorprivate2.getCount() > 0) {
                cursorprivate2.moveToFirst();
                int ss = cursorprivate2.getCount();
                for (int i = 0; i < ss; i++) {
                    ReminderMedicnceDoagePer content = new ReminderMedicnceDoagePer(cursorprivate2);
                    listApps.add(content);
                    cursorprivate2.moveToNext();
                }

            }
            cursorprivate2.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static ArrayList<ReminderMedicnceTime> setReminderMedicinealarmdetailres(Context context, String common_id) {
        Cursor cursorprivate2 = null;
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<ReminderMedicnceTime>();

        ArrayList<ReminderMedicnceTime> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "select status,hour,minute,common_id,data_id from tbl_medicine_reminder_self_alarmdetailresponse where common_id ='" + common_id + "'";

            cursorprivate2 = database.rawQuery(query, null);
            if (cursorprivate2.getCount() > 0) {
                cursorprivate2.moveToFirst();
                int ss = cursorprivate2.getCount();
                for (int i = 0; i < ss; i++) {
                    ReminderMedicnceTime content = new ReminderMedicnceTime(cursorprivate2);
                    listApps.add(content);
                    cursorprivate2.moveToNext();
                }

            }
            cursorprivate2.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static String getLabTestReportList(Context context, String cf_uuhid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return "";
        String response = "";
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT P.* FROM " + TABLE_LABTESTREPORT
                    + " P WHERE P.cf_uuhid = '" + cf_uuhid + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                response = cursor.getString(cursor.getColumnIndex("labtest_data"));
//                cursor.moveToNext();
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return response;
    }


    public static void insertEmailList(Context context, ContentValues cv, String emailID) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_EMAIL + " Where email_id ='" + emailID + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                database.update(TABLE_EMAIL, cv, "email_id" + "='" + emailID + "'",
                        null);
//                Log.e("update", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_EMAIL, null, cv);
//                Log.e("", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertNoteList(Context context, ContentValues cv, int primaryId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_NOTE + " Where healthNoteId =" + primaryId;

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                database.update(TABLE_NOTE, cv, ID + "=" + primaryId,
                        null);
            } else {
                long id = database.insert(TABLE_NOTE, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertOfflineNoteList(Context context, ContentValues cv) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            long id = database.insert(TABLE_OFFLINE_NOTE, null, cv);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertLoginList(Context context, ContentValues cv, String primaryId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LOGIN + " Where cf_uuhid ='" + primaryId + "'";

            cursor = database.rawQuery(query, null);
            AppPreference.getInstance().setcf_uuhid(primaryId);
            if (cursor.getCount() > 0) {
                cv.put("hint_screen", 1);
                database.update(TABLE_LOGIN, cv, CF_UUHID + "='" + primaryId + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_LOGIN, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertUHIDListLocal(Context context, ContentValues cv, String primaryId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_UHID + " Where cfUuhid ='" + primaryId + "'";

            cursor = database.rawQuery(query, null);
//            AppPreference.getInstance().setcf_uuhid(primaryId);
            if (cursor.getCount() > 0) {
                database.update(TABLE_UHID, cv, CFUUHID_LOCAL + "='" + primaryId + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_UHID, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    // for selected (update 0 to all and then in case of selected is 1);
    public static void insertUHIDListLocalUpdateSelected(Context context, ContentValues cv, String primaryId, ContentValues cv0) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();
            String query_reset = "SELECT * FROM " + TABLE_UHID + "";
            String query = "SELECT * FROM " + TABLE_UHID + " Where cfUuhid ='" + primaryId + "'";

            cursor_reset = database.rawQuery(query_reset, null);
            cursor = database.rawQuery(query, null);

            //reset all
            if (cursor_reset.getCount() > 0) {
                cursor_reset.moveToFirst();
                for (int i = 0; i < cursor_reset.getCount(); i++) {
                    database.update(TABLE_UHID, cv0, null, null);
                    cursor_reset.moveToNext();
                }
                cursor_reset.close();
            }


            if (cursor.getCount() > 0) {
                database.update(TABLE_UHID, cv, CFUUHID_LOCAL + "='" + primaryId + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertStepsCounts(Context context, ContentValues cv) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

//            String query = "SELECT * FROM " + TABLE_STEPS + " Where cf_uuhid ='" + cf_uuhid + "'";
//
//            cursor = database.rawQuery(query, null);

            long id = database.insert(TABLE_STEPS, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static StepsCountsStatus getStepStatusList(Context context, String primaryId, String date) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new StepsCountsStatus();
        StepsCountsStatus listApps = null;
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_STEPS_STATUS + " Where cf_uuhid ='" + primaryId + "'" + " AND date = '" + date + "'";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    listApps = new StepsCountsStatus(cursor);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static void insertStepStaus(Context context, ContentValues cv, String cf_uuhid, String date) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_STEPS_STATUS + " Where cf_uuhid ='" + cf_uuhid + "'" + " AND date = '" + date + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_STEPS_STATUS, cv, CF_UUHID + "='" + cf_uuhid + "'" + " AND date = '" + date + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_STEPS_STATUS, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertGraphList(Context context, ContentValues cv, String primaryId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_GRAPH + " Where cf_uuhid ='" + primaryId + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_GRAPH, cv, CF_UUHID + "='" + primaryId + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_GRAPH, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertPrescriptionImage(Context context, ContentValues cv, String isUploaded, String commonid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_MAIN + " where common_id='" + commonid + "'"; //Where isUploaded='" + isUploaded + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                String a = "0";
                database.update(TABLE_PRESCRIPTION_MAIN, cv, "isUploaded" + "='" + a + "'",
                        null);//

            } else {
                long id = database.insert(TABLE_PRESCRIPTION_MAIN, null, cv);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertLabReportImage(Context context, ContentValues cv, String isUploaded, String commonid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LABTESTREPORT_MAIN + " where common_id='" + commonid + "'"; //Where isUploaded='" + isUploaded + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                String a = "0";
                database.update(TABLE_LABTESTREPORT_MAIN, cv, "isUploaded" + "='" + a + "'",
                        null);//

            } else {
                long id = database.insert(TABLE_LABTESTREPORT_MAIN, null, cv);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    //assa
    public static ArrayList<PrescriptionListView> getPrescriptionData(Context context, String isUploaded) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<PrescriptionListView>();
        ArrayList<PrescriptionListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_MAIN + " Where isUploaded ='" + isUploaded + "'" + "";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    PrescriptionListView list = new PrescriptionListView(cursor);
                    listApps.add(list);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    public static ArrayList<LabReportListView> getLabData(Context context, String isUploaded) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<LabReportListView>();
        ArrayList<LabReportListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_LABTESTREPORT_MAIN + " Where isUploaded ='" + isUploaded + "'" + "";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    LabReportListView list = new LabReportListView(cursor);
                    listApps.add(list);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //lab reminder
    public static ArrayList<Lab_Test_Reminder_SelfListView> getLabReminderbySelf(Context context, String isUploaded) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Lab_Test_Reminder_SelfListView>();
        ArrayList<Lab_Test_Reminder_SelfListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " Where isUploaded ='" + isUploaded + "'" + "";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    Lab_Test_Reminder_SelfListView list = new Lab_Test_Reminder_SelfListView(cursor);
                    listApps.add(list);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //doctor remider
    public static ArrayList<Doctor_Visit_Reminder_SelfListView> getDoctorReminderbySelf(Context context, String isUploaded) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Doctor_Visit_Reminder_SelfListView>();
        ArrayList<Doctor_Visit_Reminder_SelfListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_DOCTOR_REMINDER_SELF + " Where isUploaded ='" + isUploaded + "'" + "";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    Doctor_Visit_Reminder_SelfListView list = new Doctor_Visit_Reminder_SelfListView(cursor);
                    listApps.add(list);
                    cursor.moveToNext();

                }
            }
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }

    //
    public static ArrayList<Reminder_SelfListView> getMedicineReminderbySelf(Context context, String isUploaded) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<Reminder_SelfListView>();
        ArrayList<Reminder_SelfListView> listApps = new ArrayList<>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " Where isUploaded ='" + isUploaded + "'" + "";


            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    Reminder_SelfListView list = new Reminder_SelfListView(cursor);
                    listApps.add(list);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static void insertPrescriptionList(Context context, ContentValues cv, String cf_uuhid, String common_id) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_MAIN + " Where common_id='" + common_id + "'";//cfUuhid ='" + cf_uuhid + "' and
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_MAIN, cv, "common_id" + "='" + common_id + "'",
                        null);

            } else {
                long id = database.insert(TABLE_PRESCRIPTION_MAIN, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertLabReminderDoctorName(Context context, ContentValues cv, String common_id, String cf_uuhid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_DOCTOR_NAME + " Where common_id='" + common_id + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_LAB_REMINDER_DOCTOR_NAME, cv, "common_id" + "='" + common_id + "'",
                        null);//

            } else {
                long id = database.insert(TABLE_LAB_REMINDER_DOCTOR_NAME, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertLabTestReportList(Context context, ContentValues cv, String cf_uuhid, String common_id) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LABTESTREPORT_MAIN + " Where common_id='" + common_id + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_LABTESTREPORT_MAIN, cv, "common_id" + "='" + common_id + "'",
                        null);
            } else {
                long id = database.insert(TABLE_LABTESTREPORT_MAIN, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertFilterDataPrescription(Context context, ContentValues cv, String cf_uuhid, String common_id) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_FILTER_DATA + "";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_MAIN, cv, "common_id" + "='" + common_id + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_PRESCRIPTION_MAIN, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertPrescriptionFollowUPList(Context context, ContentValues cv, String common_id, String prescriptonImageFollowupId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_FOLLOWUPLIST + " Where common_id ='" + common_id + "' and prescriptonImageFollowupId='" + prescriptonImageFollowupId + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_FOLLOWUPLIST, cv, COMMON_ID + "='" + common_id + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_PRESCRIPTION_FOLLOWUPLIST, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertPrescriptionFollowUPListLocal(Context context, ContentValues cv, String common_id) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_FOLLOWUPLIST + " Where common_id ='" + common_id + "'";// and prescriptonImageFollowupId='" + prescriptonImageFollowupId + "'
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_FOLLOWUPLIST, cv, COMMON_ID + "='" + common_id + "'",
                        null);

            } else {
                long id = database.insert(TABLE_PRESCRIPTION_FOLLOWUPLIST, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertPrescriptionResponseList(Context context, ContentValues cv, String common_id, String PRESCRIPTION_IMAGEPARTID) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_RESPONSELIST + " Where  prescriptionImagePartId='" + PRESCRIPTION_IMAGEPARTID + "'";//common_id ='" + common_id + "' and
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_RESPONSELIST, cv, PRESCRIPTIONIMAGEPARTID + "='" + PRESCRIPTION_IMAGEPARTID + "'",
                        null);
//                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_PRESCRIPTION_RESPONSELIST, null, cv);
//                Log.e("insertLoginList", "Qurery Enty number-" + id);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertPrescriptionResponseListLocal(Context context, ContentValues cv, String common_id, String imagenum) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_PRESCRIPTION_RESPONSELIST + " Where common_id ='" + common_id + "' and imageNumber='" + imagenum + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_PRESCRIPTION_RESPONSELIST, cv, COMMON_ID + "='" + common_id + "' and imageNumber='" + imagenum + "'",
                        null);

            } else {
                long id = database.insert(TABLE_PRESCRIPTION_RESPONSELIST, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertLabReportResponseListLocal(Context context, ContentValues cv, String common_id, String imagenum) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LABTESTREPORT_RESPONSELIST + " Where common_id ='" + common_id + "' and imageNumber='" + imagenum + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_LABTESTREPORT_RESPONSELIST, cv, COMMON_ID + "='" + common_id + "' and imageNumber='" + imagenum + "'",
                        null);

            } else {
                long id = database.insert(TABLE_LABTESTREPORT_RESPONSELIST, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertLabTestReportResponseList(Context context, ContentValues cv, String common_id, String PRESCRIPTION_IMAGEPARTID) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LABTESTREPORT_RESPONSELIST + " Where reportImageId='" + PRESCRIPTION_IMAGEPARTID + "'";//common_id ='" + common_id + "'
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_LABTESTREPORT_RESPONSELIST, cv, "reportImageId" + "='" + PRESCRIPTION_IMAGEPARTID + "'",
                        null);
            } else {
                long id = database.insert(TABLE_LABTESTREPORT_RESPONSELIST, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertLabTestRemiderReport(Context context, ContentValues cv, String labTestReminderId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " Where labTestReminderId='" + labTestReminderId + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_LAB_REMINDER_SELF, cv, "labTestReminderId" + "='" + labTestReminderId + "'",
                        null);
            } else {
                long id = database.insert(TABLE_LAB_REMINDER_SELF, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertDoctorRemiderReport(Context context, ContentValues cv, String doctorFollowupReminderId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_DOCTOR_REMINDER_SELF + " Where doctorFollowupReminderId='" + doctorFollowupReminderId + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_DOCTOR_REMINDER_SELF, cv, "doctorFollowupReminderId" + "='" + doctorFollowupReminderId + "'",
                        null);
            } else {
                long id = database.insert(TABLE_DOCTOR_REMINDER_SELF, null, cv);

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    //medicine reminder
    public static void insertMedicineRemiderReport(Context context, ContentValues cv, String medicineReminderId, String date) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " Where medicineReminderId='" + medicineReminderId + "'  and currentdate='"+ date +"'";//
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_MEDICINE_REMINDER_SELF, cv, "medicineReminderId" + "='" + medicineReminderId + "' and currentdate" + "='" + date + "'",
                        null);
            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertMedicineRemiderPerDosageDateResponse(Context context, ContentValues cv, String commonID, int i, String date) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE + " Where common_id='" + commonID + "' and data_id='" + i + "' and date='"+ date +"'";// and date='"+ date +"'
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE, cv, "common_id" + "='" + commonID + "' and data_id='" + i + "' and date='"+date+"'",
                        null);
            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertMedicineRemiderAlarmDetailResponse(Context context, ContentValues cv, String commonID, int i) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE + " Where common_id='" + commonID + "' and data_id='" + i + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                database.update(TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE, cv, "common_id" + "='" + commonID + "' and data_id='" + i + "'",
                        null);
            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertEditGoalList(Context context, ContentValues cv, String primaryId) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_EDIT_GOAL + " Where edit_id ='" + primaryId + "'";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                database.update(TABLE_EDIT_GOAL, cv, "edit_id" + "='" + primaryId + "'",
                        null);
            } else {
                long id = database.insert(TABLE_EDIT_GOAL, null, cv);
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static GoalInfo getGoalList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new GoalInfo();
        GoalInfo listApps = null;
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            String query = "SELECT L.* FROM " + TABLE_EDIT_GOAL
                    + " L WHERE L.edit_id = '" + AppPreference.getInstance().getcf_uuhid() + "' ORDER BY L.edit_id DESC ";

            cursor = database.rawQuery(query, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                for (int i = 0; i < cursor.getCount(); i++) {
                    listApps = new GoalInfo(cursor);
                    cursor.moveToNext();
                }
            }
            cursor.close();
            database.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        return listApps;
    }


    public static void clearUserDB() {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_LOGIN, null, null);
            database.close();
        } catch (Exception e) {
        }
    }

    public static void clearOfflineNoteDB() {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_OFFLINE_NOTE, null, null);
            database.close();
        } catch (Exception e) {
        }
    }

    public static void clearNoteDB() {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_NOTE, null, null);
            database.close();
        } catch (Exception e) {
        }
    }

    public static void clearDB() {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_USER_INFO, null, null);
            database.delete(TABLE_DOCTOR, null, null);
            database.delete(TABLE_PATIENT_DETAILS, null, null);
            database.close();
        } catch (Exception e) {
        }
    }


    public static int getTotalPatientCount() {
        Cursor c = null;
        SQLiteDatabase database = null;
        try {

            database = DatabaseHelper.openDataBase();
            // db = Dbhelper.getReadableDatabase();
            String query = "SELECT count(patientProfileId) from " + TABLE_PATIENT_DETAILS;
//            Log.e("getTotalDispenseCount", ": " + query);
            c = database.rawQuery(query, null);
            if (c.moveToFirst()) {
                return c.getInt(0);
            }
            return 0;
        } finally {
            if (c != null) {
                c.close();
            }
            if (database != null) {
                database.close();
            }
        }

    }

    public static void deleteNoteListing(int healthNoteId) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_NOTE, "healthNoteId" + "=?",
                    new String[]{String.valueOf(healthNoteId)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public static void deleteSteps(String date) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_STEPS, "dateTime" + "=?",
                    new String[]{String.valueOf(date)});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }


    public static void clearLabData(String commonID, String isUploaded) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_LABTESTREPORT_MAIN, "common_id " + "=? AND " + "isUploaded" + "=?", new String[]{commonID, isUploaded});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }


    }


    public static void clearPrescriptionData(String commonID, String isUploaded) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_PRESCRIPTION_MAIN, "common_id " + "=? AND " + "isUploaded" + "=?", new String[]{commonID, isUploaded});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public static void clearLabReminderbyself(String labTestReminderId, String isUploaded) {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_LAB_REMINDER_SELF, "labTestReminderId  " + "=? AND " + "isUploaded" + "=?", new String[]{labTestReminderId, isUploaded});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }


    public static void clearDoctorReminderbyself(String doctorFollowupReminderId, String isUploaded) {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_DOCTOR_REMINDER_SELF, "doctorFollowupReminderId  " + "=? AND " + "isUploaded" + "=?", new String[]{doctorFollowupReminderId, isUploaded});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public static void clearMedicineReminderbyself(String medicineReminderId, String isUploaded) {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_MEDICINE_REMINDER_SELF, "medicineReminderId  " + "=? AND " + "isUploaded" + "=?", new String[]{medicineReminderId, isUploaded});
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public static void clearLabDataFromLocal(String reportID, String doctorName, String check_text) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            if (check_text.equalsIgnoreCase("pres")) {
                database.delete(TABLE_PRESCRIPTION_MAIN, "prescriptionId " + "=? AND " + "doctorName" + "=?", new String[]{reportID, doctorName});
            } else if (check_text.equalsIgnoreCase("labreport")) {
                database.delete(TABLE_LABTESTREPORT_MAIN, "reportId " + "=? AND " + "doctorName" + "=?", new String[]{reportID, doctorName});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }

    public static void insertLabTestRemiderLocal(Context context, ContentValues cv, String commonid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_LAB_REMINDER_SELF + " where labTestReminderId='" + commonid + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {

                long id1=  database.update(TABLE_LAB_REMINDER_SELF, cv, "labTestReminderId "+"='"+commonid+"' ",
                        null);//isUploaded" + "='" + a + "' and
                Log.e("",""+id1);

            } else {
                long id = database.insert(TABLE_LAB_REMINDER_SELF, null, cv);
                Log.e("",""+id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertDoctorRemiderLocal(Context context, ContentValues cv, String commonid) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_DOCTOR_REMINDER_SELF + " where doctorFollowupReminderId='" + commonid + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
               // String a = "0";
                database.update(TABLE_DOCTOR_REMINDER_SELF, cv, "doctorFollowupReminderId" + "='" + commonid + "'",
                        null);// and isUploaded" + "='" + a + "'

            } else {
                long id = database.insert(TABLE_DOCTOR_REMINDER_SELF, null, cv);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertMedicineRemiderLocal(Context context, ContentValues cv, String commonid,String selectedDate) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF + " where medicineReminderId='" + commonid + "' and currentdate='"+selectedDate+"'" ;//
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                String a = "0";
                database.update(TABLE_MEDICINE_REMINDER_SELF, cv, "medicineReminderId" +"='"+commonid+"'",
                        null);//"isUploaded" + "='" + a + "'"

            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF, null, cv);
                Log.e("ins", " " + id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }

    public static void insertMedicineRemiderDosagePerLocal(Context context, ContentValues cv, String commonid,String selectedDate) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE + " where common_id='" + commonid + "' and date='"+selectedDate+"'";//'
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                //String a = "0";
                database.update(TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE, cv, "common_id" + "='" + commonid + "'",
                        null);

            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF_DOSAGEPERDATERESPONSE, null, cv);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    public static void insertMedicineRemiderAlaramDetailResponseLocal(Context context, ContentValues cv, String commonid, int i) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse()
                .getDatabaseHelperInstance(context);
        if (cv == null)
            return;
        if (dbhelperShopCart == null)
            return;
        SQLiteDatabase database = null;
        try {
            dbhelperShopCart.createDataBase();
            database = DatabaseHelper.openDataBase();

            String query = "SELECT * FROM " + TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE + " where common_id='" + commonid + "' and data_id='" + i + "'";
            cursor = database.rawQuery(query, null);

            if (cursor.getCount() > 0) {
                String a = "0";
                database.update(TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE, cv, "common_id" + "='" + commonid + "'",
                        null);//isUploaded" + "='" + a + "'

            } else {
                long id = database.insert(TABLE_MEDICINE_REMINDER_SELF_ALARAMDETAILRESPONSE, null, cv);

            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DatabaseHelper.closedatabase();
        }
        database.close();
    }


    ////delete doctor reminder from local
    public static void clearDoctorReminderFromLocal(String reportID, String doctorName, String check_text) {
        SQLiteDatabase database = null;

        try {
            database = DatabaseHelper.openDataBase();
            if (check_text.equalsIgnoreCase("doctorreminder")) {
                database.delete(TABLE_DOCTOR_REMINDER_SELF, "doctorFollowupReminderId " + "=? AND " + "doctorName" + "=?", new String[]{reportID, doctorName});
            } else if (check_text.equalsIgnoreCase("labreminder")) {
                database.delete(TABLE_LAB_REMINDER_SELF, "labTestReminderId " + "=? AND " + "doctorName" + "=?", new String[]{reportID, doctorName});
            }else if(check_text.equalsIgnoreCase("medicinereminder")){
                database.delete(TABLE_MEDICINE_REMINDER_SELF, "medicineReminderId " + "=? AND " + "doctorName" + "=?", new String[]{reportID, doctorName});
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            database.close();
        }
    }



}