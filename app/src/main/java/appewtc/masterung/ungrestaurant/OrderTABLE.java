package appewtc.masterung.ungrestaurant;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by masterUNG on 8/8/15 AD.
 */
public class OrderTABLE {

    private MySQLiteOpenHelper objMySQLiteOpenHelper;
    private SQLiteDatabase writeSqLiteDatabase, readSqLiteDatabase;

    public static final String ORDER_TABLE = "orderTABLE";
    public static final String COLUMN_ID_ORDER = "_id";
    public static final String COLUMN_OFFICER = "Officer";
    public static final String COLUMN_DESK = "Desk";
    public static final String COLUMN_FOOD_ORDER = "Food";
    public static final String COLUMN_ITEM = "Item";

    public OrderTABLE(Context context) {
        objMySQLiteOpenHelper = new MySQLiteOpenHelper(context);
        writeSqLiteDatabase = objMySQLiteOpenHelper.getWritableDatabase();
        readSqLiteDatabase = objMySQLiteOpenHelper.getReadableDatabase();
    }

    public long addOrder(String strOfficer, String strDesk, String strFood, String strItem) {

        ContentValues objContentValues = new ContentValues();
        objContentValues.put(COLUMN_OFFICER, strOfficer);
        objContentValues.put(COLUMN_DESK, strDesk);
        objContentValues.put(COLUMN_FOOD_ORDER, strFood);
        objContentValues.put(COLUMN_ITEM, strItem);

        return writeSqLiteDatabase.insert(ORDER_TABLE, null, objContentValues);
    }

}   // Main Class
