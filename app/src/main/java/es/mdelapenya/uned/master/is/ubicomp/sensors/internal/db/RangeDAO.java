/**
 *    Copyright 2017-today Manuel de la Peña
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package es.mdelapenya.uned.master.is.ubicomp.sensors.internal.db;

import android.content.ContentValues;
import android.content.Context;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;

import java.util.ArrayList;
import java.util.List;

import es.mdelapenya.uned.master.is.ubicomp.sensors.model.Range;

/**
 * @author Manuel de la Peña
 */
public class RangeDAO implements Closeable {

    public RangeDAO(Context context) {
        dbHelper = new RangeDBHelper(context);

        open();

        if (!initialized && getAllRanges().isEmpty()) {
            initData();
        }
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

    public Range findBy(Criterion criterion) {
        String selection = criterion.getField() + "=?";

        String[] selectionArgs = { String.valueOf(criterion.getValue()) };

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

    public Range getRange(long rangeId) {
        return findBy(new CriterionImpl(RangeDBHelper.Range._ID, new Long(rangeId)));
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

    private void initData() {
        createRange(1, 0, "status_stopped");
        createRange(4, 1, "status_walking");
        createRange(6, 4, "status_marching");
        createRange(12, 6, "status_running");
        createRange(25, 12, "status_sprinting");
        createRange(170, 25, "status_terrain_motor_vehicle");
        createRange(Integer.MAX_VALUE, 170, "status_aerial_motor_vehicle");

        initialized = true;
    }

    private static boolean initialized = false;

    private String[] allColumns = {
        RangeDBHelper.Range._ID,
        RangeDBHelper.Range.COLUMN_NAME_MAX,
        RangeDBHelper.Range.COLUMN_NAME_MIN,
        RangeDBHelper.Range.COLUMN_NAME_NAME
    };
    private SQLiteDatabase database;
    private RangeDBHelper dbHelper;

}
