package es.mdelapenya.uned.master.is.ubicomp.sensors.db;

import android.content.Context;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import android.provider.BaseColumns;

/**
 * @author Manuel de la Peña
 */
public class RangeDBHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "UNEDSensors.db";

    public RangeDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_RANGES);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_RANGES);

        onCreate(db);
    }

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_RANGES =
        "CREATE TABLE " + Range.TABLE_NAME + " (" +
            Range._ID + " INTEGER PRIMARY KEY" + COMMA_SEP + " " +
            Range.COLUMN_NAME_NAME + " " + TEXT_TYPE + COMMA_SEP + " " +
            Range.COLUMN_NAME_MAX + " " + INTEGER_TYPE + COMMA_SEP + " " +
            Range.COLUMN_NAME_MIN + " " + INTEGER_TYPE +
            " )";

    private static final String SQL_DELETE_RANGES =
        "DROP TABLE IF EXISTS " + Range.TABLE_NAME;

    public static abstract class Range implements BaseColumns {

        public static final String TABLE_NAME = "range";
        public static final String COLUMN_NAME_NAME = "name";
        public static final String COLUMN_NAME_MAX = "max";
        public static final String COLUMN_NAME_MIN = "min";

    }

}
