package com.deltabit.bazaar.data;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.deltabit.bazaar.TestUtilities;
import com.deltabit.bazaar.data.BazaarContract.CategoryEntry;
import com.deltabit.bazaar.data.BazaarContract.DealEntry;
import com.deltabit.bazaar.data.BazaarContract.UserEntry;

/**
 * Created by rigel on 16-Sep-16.
 */
public class TestProvider extends AndroidTestCase {
    public static final String LOG_TAG = TestProvider.class.getSimpleName();

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        deleteAllRecords();
    }


    public void deleteAllRecordsFromProvider() {

        mContext.getContentResolver().delete(
                DealEntry.CONTENT_URI,
                null,
                null
        );

        mContext.getContentResolver().delete(
                UserEntry.CONTENT_URI,
                null,
                null
        );
        mContext.getContentResolver().delete(
                CategoryEntry.CONTENT_URI,
                null,
                null
        );

        Cursor cursor;
        cursor = mContext.getContentResolver().query(
                DealEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: registros não foram deletados da tabela " + DealEntry.TABLE_NAME, 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: registros não foram deletados da tabela " + UserEntry.TABLE_NAME, 0, cursor.getCount());
        cursor.close();

        cursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        assertEquals("Error: registros não foram deletados da tabela " + CategoryEntry.TABLE_NAME, 0, cursor.getCount());
        cursor.close();


    }

    public void deleteAllRecords() {
        deleteAllRecordsFromProvider();
    }

    //Verificar se o provider está registrado corretamente.
    public void testProviderRegistry() {
        PackageManager pm = mContext.getPackageManager();


        ComponentName componentName = new ComponentName(mContext.getPackageName(),
                BazaarProvider.class.getName());
        try {
            ProviderInfo providerInfo = pm.getProviderInfo(componentName, 0);
            assertEquals("Error: BazaarProvider registered with authority: " + providerInfo.authority +
                            " instead of authority: " + BazaarContract.CONTENT_AUTHORITY,
                    providerInfo.authority, BazaarContract.CONTENT_AUTHORITY);
        } catch (PackageManager.NameNotFoundException e) {
            assertTrue("Error: BazaarProvider not registered at " + mContext.getPackageName(),
                    false);
        }
    }


    public void testGetType() {
        // content://com.deltabit.bazaar/category/
        String type = mContext.getContentResolver().getType(CategoryEntry.CONTENT_URI);
        // vnd.android.cursor.dir/com.deltabit.bazaar/category/
        assertEquals("Error: the CategoryEntry CONTENT_URI should return CategoryEntry.CONTENT_TYPE",
                CategoryEntry.CONTENT_TYPE, type);

        String testCategory = "vestimentas";

        // content://com.deltabit.bazaar/category/vestimentas
        type = mContext.getContentResolver().getType(
                CategoryEntry.buildCategory(testCategory));
        // vnd.android.cursor.dir/com.deltabit.bazaar/category/
        assertEquals("Error: the CategoryEntry CONTENT_URI with name should return WeatherEntry.CONTENT_TYPE",
                CategoryEntry.CONTENT_TYPE, type);
    }


    public void testBasicUserQueries() {
        // inserir valores na base de dados
        ContentValues testValues = TestUtilities.createUserValues();
        long rowId = TestUtilities.insertUserValues(mContext);

        assertTrue("Error: inserção falhou.", rowId != 0);

        // Verificar o conteúdo query
        Cursor userCursor = mContext.getContentResolver().query(
                UserEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        // Verificar se temos o cursor correto
        TestUtilities.validateCursor("testBasicUserQueries, user query", userCursor, testValues);

        userCursor.close();
    }

    public void testBasicCategoryQuery() {
        // inserir valores na base de dados
        ContentValues testValues = TestUtilities.createCategoryValues();
        long rowId = TestUtilities.insertCategoryValues(mContext);

        assertTrue("Error: inserção falhou.", rowId != 0);

        // Verificar o conteúdo query
        Cursor categoryCursor = mContext.getContentResolver().query(
                CategoryEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );
        // Verificar se temos o cursor correto
        TestUtilities.validateCursor("testBasicCategoryQuery, category query", categoryCursor, testValues);
    }


    public void testBasicDealQuery() {
        // inserir valores na base de dados
        BazaarDbHelper dbHelper = new BazaarDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues categoryValues = TestUtilities.createCategoryValues();
        long categoryRowId = TestUtilities.insertCategoryValues(mContext);
        assertTrue("Error: inserção falhou.", categoryRowId != -1);

        ContentValues userValues = TestUtilities.createUserValues();
        long userRowId = TestUtilities.insertUserValues(mContext);
        assertTrue("Error: inserção falhou.", userRowId != -1);

        ContentValues dealValues = TestUtilities.createDealValues(userRowId, categoryRowId);
        long dealRowId = db.insert(DealEntry.TABLE_NAME, null, dealValues);
        assertTrue("Error: inserção falhou.", dealRowId != -1);

        db.close();

        // Verificar o conteúdo query
        Cursor dealCursor = mContext.getContentResolver().query(
                DealEntry.CONTENT_URI,
                null,
                null,
                null,
                null
        );

        TestUtilities.validateCursor("testBasicDealQuery", dealCursor, dealValues);
    }


}
