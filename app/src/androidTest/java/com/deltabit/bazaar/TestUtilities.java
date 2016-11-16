package com.deltabit.bazaar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by rigel on 16-Sep-16.
 */
public class TestUtilities extends AndroidTestCase {

    static final String TEST_USERNAME = "joão";
    static final String TEST_PASSWORD = "spock";
    static final String TEST_CATEGORY_NAME = "vestimentas";
    private static final int TEST_IMAGE_ID = 1;
    private static final String TEST_ITEM_NAME = "camiseta";
    private static final int TEST_ITEM_QUANTITY = 1;
    private static final float TEST_ITEM_PRICE = 30.5f;
    private static final int TEST_ITEM_STATE = 0;

    public static ContentValues createUserValues() {
        ContentValues userValues = new ContentValues();
        userValues.put(BazaarContract.UserEntry.COLUMN_USERNAME, TEST_USERNAME);
        userValues.put(BazaarContract.UserEntry.COLUMN_PASSWORD, TEST_PASSWORD);


        return userValues;
    }

    public static long insertUserValues(Context context) {

        BazaarDbHelper dbHelper = new BazaarDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createUserValues();

        long rowId;
        rowId = db.insert(BazaarContract.UserEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert user values", rowId != -1);

        return rowId;
    }

    public static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues) {
        assertTrue("Empty cursor returned. " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }

    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }
    }

    public static long insertCategoryValues(Context mContext) {
        BazaarDbHelper dbHelper = new BazaarDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createCategoryValues();

        long rowId;
        rowId = db.insert(BazaarContract.CategoryEntry.TABLE_NAME, null, testValues);

        // Verify we got a row back.
        assertTrue("Error: Failure to insert user values", rowId != -1);

        return rowId;
    }

    public static ContentValues createCategoryValues() {
        ContentValues categoryValues = new ContentValues();
        categoryValues.put(BazaarContract.CategoryEntry.COLUMN_CATEGORY_NAME, TEST_CATEGORY_NAME);

        return categoryValues;
    }

    public static ContentValues createDealValues(long userRowId, long categoryRowId) {

        ContentValues dealValues = new ContentValues();
        dealValues.put(BazaarContract.DealEntry.COLUMN_USER_KEY, userRowId);
        dealValues.put(BazaarContract.DealEntry.COLUMN_CATEGORY_KEY, categoryRowId);
        dealValues.put(BazaarContract.DealEntry.COLUMN_IMAGE_ID, TEST_IMAGE_ID);
        dealValues.put(BazaarContract.DealEntry.COLUMN_ITEM_NAME, TEST_ITEM_NAME);
        dealValues.put(BazaarContract.DealEntry.COLUMN_ITEM_QUANTITY, TEST_ITEM_QUANTITY);
        dealValues.put(BazaarContract.DealEntry.COLUMN_ITEM_PRICE, TEST_ITEM_PRICE);
        dealValues.put(BazaarContract.DealEntry.COLUMN_STATE, TEST_ITEM_STATE);

        return dealValues;
    }
}
