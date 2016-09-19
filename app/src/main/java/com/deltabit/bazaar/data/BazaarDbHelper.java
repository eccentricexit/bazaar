package com.deltabit.bazaar.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.deltabit.bazaar.data.BazaarContract.CategoryEntry;
import com.deltabit.bazaar.data.BazaarContract.DealEntry;
import com.deltabit.bazaar.data.BazaarContract.UserEntry;

/**
 * Created by rigel on 16-Sep-16.
 */
public class BazaarDbHelper extends SQLiteOpenHelper {

    static final String DATABASE_NAME = "bazaar.db";
    private static final int DATABASE_VERSION = 1;

    public BazaarDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        //Comandos de criação da base de dados

        //Tabela usuário
        final String SQL_CREATE_USER_TABLE = "CREATE TABLE " + UserEntry.TABLE_NAME + " (" +
                UserEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                UserEntry.COLUMN_USERNAME + " TEXT UNIQUE NOT NULL, " +
                UserEntry.COLUMN_PASSWORD + " TEXT NOT NULL " +
                " );";

        //Tabela categorias
        final String SQL_CREATE_CATEGORY_TABLE = "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                CategoryEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                CategoryEntry.COLUMN_CATEGORY_NAME + " TEXT UNIQUE NOT NULL " +
                " );";

        //Tabela negócio
        final String SQL_CREATE_DEAL_TABLE = "CREATE TABLE " + DealEntry.TABLE_NAME + " (" +
                DealEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                DealEntry.COLUMN_USER_KEY + " INTEGER NOT NULL, " +
                DealEntry.COLUMN_IMAGE_ID + " INTEGER NOT NULL, " +
                DealEntry.COLUMN_CATEGORY_KEY + " INTEGER NOT NULL, " +

                DealEntry.COLUMN_ITEM_NAME + " TEXT NOT NULL, " +
                DealEntry.COLUMN_ITEM_PRICE + " REAL NOT NULL, " +
                DealEntry.COLUMN_ITEM_QUANTITY + " INTEGER NOT NULL, " +

                DealEntry.COLUMN_STATE + " INTEGER NOT NULL, " +

                //foreign key do usuário
                " FOREIGN KEY (" + DealEntry.COLUMN_USER_KEY + ") REFERENCES " +
                UserEntry.TABLE_NAME + " (" + UserEntry._ID + "), " +

                //foreign key de categoria
                " FOREIGN KEY (" + DealEntry.COLUMN_CATEGORY_KEY + ") REFERENCES " +
                CategoryEntry.TABLE_NAME + " (" + CategoryEntry._ID + ") " +

                " );";

        sqLiteDatabase.execSQL(SQL_CREATE_CATEGORY_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_USER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_DEAL_TABLE);

        insertDummyValues(sqLiteDatabase);
    }

    private void insertDummyValues(SQLiteDatabase sqLiteDatabase) {
        insertCategory("roupas", sqLiteDatabase);
        insertCategory("acessórios", sqLiteDatabase);
        insertCategory("eletrônicos", sqLiteDatabase);

        insertUser("joao", "spock", sqLiteDatabase);
        insertUser("pedro", "yoda", sqLiteDatabase);

        insertDeal(sqLiteDatabase, 0, 0, 0, "Vestido Vermelho", "49.00", "1", 0);
        insertDeal(sqLiteDatabase, 1, 1, 0, "Sapato Bonito", "39.00", "1", 0);
        insertDeal(sqLiteDatabase, 0, 2, 0, "Jaqueta Preta", "29.00", "1", 0);
        insertDeal(sqLiteDatabase, 1, 3, 1, "Óculos Wayfarer", "1.00", "1", 0);
        insertDeal(sqLiteDatabase, 0, 4, 0, "Shorts Xadrez", "4.00", "1", 0);
    }

    private void insertDeal(SQLiteDatabase sqLiteDatabase,
                            int userKey,
                            int imageID,
                            int categoryKey,
                            String name,
                            String price,
                            String quantity,
                            int state) {

        String SQL = "INSERT INTO deal VALUES (null," + userKey + ","
                + imageID + ","
                + categoryKey + ",'"
                + name + "',"
                + price + ","
                + quantity + ","
                + state + ")";

        sqLiteDatabase.execSQL(SQL);
    }


    private void insertUser(String username, String password, SQLiteDatabase sqLiteDatabase) {
        String SQL = "INSERT INTO user VALUES (null,'" + username + "','" + password + "')";
        sqLiteDatabase.execSQL(SQL);
    }

    private void insertCategory(String name, SQLiteDatabase sqLiteDatabase) {
        String SQL = "INSERT INTO category VALUES (null,'" + name + "')";
        sqLiteDatabase.execSQL(SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + DealEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + UserEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
