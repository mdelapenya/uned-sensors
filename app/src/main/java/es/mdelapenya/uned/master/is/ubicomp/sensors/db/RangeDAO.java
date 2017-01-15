package es.mdelapenya.uned.master.is.ubicomp.sensors.db;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

import java.util.ArrayList;
import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.db.model.Range;

/**
 * @author Manuel de la Peña
 */
public class RangeDAO implements Closeable {

    public RangeDAO(Context context) {
        dbHelper = new RangeDBHelper(context);
    }

    @Override
    public void close() {
        dbHelper.close();
    }

    public Range createRange(int max, int min, String name) {
        ContentValues values = new ContentValues();

        values.put(RangeDBHelper.Range.COLUMN_NAME_MAX, max);
        values.put(RangeDBHelper.Range.COLUMN_NAME_MIN, min);
        values.put(RangeDBHelper.Range.COLUMN_NAME_NAME, name);

        long newRangeId = database.insert(RangeDBHelper.Range.TABLE_NAME, null, values);

        return getRange(newRangeId);
    }

    public void deleteRange(Range range) {
        String[] whereArgs = { String.valueOf(range.getId()) };

        database.delete(
            RangeDBHelper.Range.TABLE_NAME, RangeDBHelper.Range._ID + " = ?", whereArgs);
    }

    public List<Range> getAllRanges() {
        List<Range> ranges = new ArrayList<>();

        try (Cursor cursor = database.query(
            RangeDBHelper.Range.TABLE_NAME, allColumns, null, null, null, null, null)) {

            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Range range = cursorToRange(cursor);

                ranges.add(range);

                cursor.moveToNext();
            }
        }

        return ranges;
    }

    public Range getRange(long rangeId) {
        String selection = RangeDBHelper.Range._ID + "=?";

        String[] selectionArgs = { String.valueOf(rangeId) };

        try (Cursor cursor = database.query(
            RangeDBHelper.Range.TABLE_NAME, allColumns, selection, selectionArgs, null, null,
            null)) {

            cursor.moveToFirst();

            if (!cursor.isAfterLast()) {
                return cursorToRange(cursor);
            }
        }

        return null;
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public Range updateRange(long id, int max, int min, String name) {
        ContentValues values = new ContentValues();

        values.put(RangeDBHelper.Range.COLUMN_NAME_MAX, max);
        values.put(RangeDBHelper.Range.COLUMN_NAME_MIN, min);
        values.put(RangeDBHelper.Range.COLUMN_NAME_NAME, name);

        String whereClause = RangeDBHelper.Range._ID + "=?";
        String[] whereArgs = { String.valueOf(id) };

        database.update(RangeDBHelper.Range.TABLE_NAME, values, whereClause, whereArgs);

        return getRange(id);
    }

    private Range cursorToRange(Cursor cursor) {
        long id = cursor.getLong(0);
        int max = cursor.getInt(1);
        int min = cursor.getInt(2);
        String name = cursor.getString(3);

        return new Range(id, max, min, name);
    }

    private String[] allColumns = {
        RangeDBHelper.Range._ID,
        RangeDBHelper.Range.COLUMN_NAME_MAX,
        RangeDBHelper.Range.COLUMN_NAME_MIN,
        RangeDBHelper.Range.COLUMN_NAME_NAME
    };
    private SQLiteDatabase database;
    private RangeDBHelper dbHelper;

}
