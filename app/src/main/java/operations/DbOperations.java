package operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import item.property.GoalInfo;
import item.property.HealthNoteItems;
import item.property.UserInfo;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class DbOperations implements MyConstants.IDataBaseTableNames, MyConstants.IDataBaseTableKeys, MyConstants.JsonUtils {
    static Cursor cursor = null;


    public static List<HealthNoteItems> getNoteList(Context context) {
        DatabaseHelper dbhelperShopCart = CureFull.getInstanse().getDatabaseHelperInstance(context);
        if (dbhelperShopCart == null)
            return new ArrayList<HealthNoteItems>();
        List<HealthNoteItems> listApps = new ArrayList<HealthNoteItems>();
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();

            String query = "SELECT P.* FROM " + TABLE_NOTE
                    + " P WHERE P.cf_uuhid = '" + AppPreference.getInstance().getcf_uuhid() + "' ORDER BY P.healthNoteId DESC ";
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
//                Log.e("update", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_NOTE, null, cv);
//                Log.e("", "Qurery Enty number-" + id);
            }

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
                cv.put("hint_screen", 1);
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
                Log.e("updateLoginList", "Qurery Enty number-");
            } else {
                long id = database.insert(TABLE_EDIT_GOAL, null, cv);
                Log.e("insertLoginList", "Qurery Enty number-" + id);
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


}