package appewtc.masterung.ungrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 8/8/15 AD.
 */
public class UserTABLE {

    //Explicit
    private MySQLiteOpenHelper objMySQLiteOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String USER_TABLE = "userTABLE";
    public static final String COLUMN_ID_USER = "_id";
    public static final String COLUMN_USER = "User";
    public static final String COLUMN_PASSWORD = "Password";
    public static final String COLUMN_NAME = "Name";


    public UserTABLE(Context context) {

        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();
    }   // Constructor

    //Search User
    public String[] searchUser(String strUser) {

        try {

            String[] strResult = null;
            Cursor objCursor = readSqLiteDatabase.query(USER_TABLE,
                    new String[]{COLUMN_ID_USER, COLUMN_USER, COLUMN_PASSWORD, COLUMN_NAME},
                    COLUMN_USER + "=?",
                    new String[]{String.valueOf(strUser)},
                    null, null, null, null);
            if (objCursor != null) {
                if (objCursor.moveToFirst()) {

                    strResult = new String[4];
                    strResult[0] = objCursor.getString(0);
                    strResult[1] = objCursor.getString(1);
                    strResult[2] = objCursor.getString(2);
                    strResult[3] = objCursor.getString(3);

                }   // if
            }   // if
            objCursor.close();
            return strResult;

        } catch (Exception e) {
            return null;
        }

        //return new String[0];
    }



    //Add New User
    public long addUser(String strUser, String strPass, String strName) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_USER, strUser);
        objContentValues.put(COLUMN_PASSWORD, strPass);
        objContentValues.put(COLUMN_NAME, strName);

        return writeSqLiteDatabase.insert(USER_TABLE, null, objContentValues);
    }

}   // Main Class
