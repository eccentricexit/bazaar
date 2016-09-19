package com.deltabit.bazaar.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.deltabit.bazaar.TestUtilities;

import java.util.HashSet;

/**
 * Created by rigel on 16-Sep-16.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase() {
        mContext.deleteDatabase(BazaarDbHelper.DATABASE_NAME);
    }

    //Executada antes de cada teste para garantir testes limpos
    public void setUp() {
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable {

        //Constroi um hashset de todas as tabelas (incluindo a de metadados)

        final HashSet<String> tableNameHashSet = new HashSet<>();
        tableNameHashSet.add(BazaarContract.UserEntry.TABLE_NAME);
        tableNameHashSet.add(BazaarContract.CategoryEntry.TABLE_NAME);
        tableNameHashSet.add(BazaarContract.DealEntry.TABLE_NAME);

        mContext.deleteDatabase(BazaarDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new BazaarDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true, db.isOpen());

        // Verificar se temos as tabelas na base de dados
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);

        assertTrue("Error: A base foi criada de forma incorreta",
                c.moveToFirst());

        // verificar que as tabelas foram criadas
        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        //Se isto falhar é porque não temos as tabelas na base.
        assertTrue("Error: a base foi criada sem as tabelas.",
                tableNameHashSet.isEmpty());

        // verificar se a tabela user possui as colunas corretas
        c = db.rawQuery("PRAGMA table_info(" + BazaarContract.UserEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: Não foi possível obter informação sobre a tabela",
                c.moveToFirst());

        // Build a HashSet of all of the column names we want to look for
        final HashSet<String> locationColumnHashSet = new HashSet<>();
        locationColumnHashSet.add(BazaarContract.UserEntry._ID);
        locationColumnHashSet.add(BazaarContract.UserEntry.COLUMN_USERNAME);
        locationColumnHashSet.add(BazaarContract.UserEntry.COLUMN_PASSWORD);


        int columnNameIndex = c.getColumnIndex("name");
        do {
            String columnName = c.getString(columnNameIndex);
            locationColumnHashSet.remove(columnName);
        } while (c.moveToNext());

        // Se isto falhar é porque não a tabela usuário não possui todas as colunas
        assertTrue("Error",
                locationColumnHashSet.isEmpty());
        db.close();
        c.close();
    }

    public void testUserTable() {
        insertUser();
    }

    public long insertUser() {
        BazaarDbHelper dbHelper = new BazaarDbHelper(mContext);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        String TEST_USERNAME = "João";
        String TEST_PASSWORD = "Spock";
        ContentValues testValues = TestUtilities.createUserValues();


        long locationRowId;
        locationRowId = db.insert(BazaarContract.UserEntry.TABLE_NAME, null, testValues);

        //verificar se recebemos a linha
        assertTrue(locationRowId != -1);

        Cursor cursor = db.query(
                BazaarContract.UserEntry.TABLE_NAME,  // Table to Query
                null,
                null,
                null,
                null,
                null,
                null
        );

        assertTrue("Error: Nenhum registro retornado :(", cursor.moveToFirst());

        //Verificar se apenas um registro existia
        assertFalse("Error: Mais de um registro retornado do teste query. PS.:Comente a linha insertDummyValues() da classe BazaarDbHelper.",
                cursor.moveToNext());


        cursor.close();
        db.close();
        return locationRowId;
    }

}