package appewtc.masterung.ungrestaurant;

import android.content.ContentValues;
import android.content.Context;
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

    //Add New User
    public long addUser(String strUser, String strPass, String strName) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_USER, strUser);
        objContentValues.put(COLUMN_PASSWORD, strPass);
        objContentValues.put(COLUMN_NAME, strName);

        return writeSqLiteDatabase.insert(USER_TABLE, null, objContentValues);
    }

}   // Main Class
