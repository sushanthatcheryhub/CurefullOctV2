package operations;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import curefull.healthapp.CureFull;
import item.property.HealthNoteItems;
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
                    + " P ORDER BY P.healthNoteId DESC ";
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


    public static void clearUserDB() {
        SQLiteDatabase database = null;
        try {
            database = DatabaseHelper.openDataBase();
            database.delete(TABLE_USER_INFO, null, null);
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