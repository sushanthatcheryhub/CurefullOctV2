package operations;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class DbOperations implements MyConstants.IDataBaseTableNames, MyConstants.IDataBaseTableKeys, MyConstants.JsonUtils {
    static Cursor cursor = null;


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


}