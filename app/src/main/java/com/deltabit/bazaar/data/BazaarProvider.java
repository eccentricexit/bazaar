package com.deltabit.bazaar.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;

import com.deltabit.bazaar.data.BazaarContract.DealEntry;
import com.deltabit.bazaar.data.BazaarContract.UserEntry;
import com.deltabit.bazaar.data.BazaarContract.CategoryEntry;

/**
 * Created by rigel on 16-Sep-16.
 */
public class BazaarProvider extends ContentProvider {

    // The URI Matcher used by this content provider.
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = BazaarProvider.class.getClass().getSimpleName();


    private BazaarDbHelper mOpenHelper;

    static final int USER = 100;
    static final int CATEGORY = 101;
    static final int DEAL = 102;

    static final int USER_WITH_LOGIN_AND_PASSWORD = 103;
    static final int CATEGORY_WITH_NAME = 104;
    static final int DEAL_WITH_CATEGORY = 105;
    static final int DEAL_WITH_USER_ID = 106;
    static final int DEAL_WITH_ID = 107;


    //user.username = ? AND user.password = ?
    public static final String sUserSelection =
            UserEntry.TABLE_NAME+"."+ UserEntry.COLUMN_USERNAME + " = ? AND " +
                    UserEntry.COLUMN_PASSWORD+" = ?"
            ;

    //category.name = ?
    public static final String sCategorySelection =
            CategoryEntry.TABLE_NAME+"."+ CategoryEntry.COLUMN_CATEGORY_NAME + " = ?";

    //deal.user_key = ?
    public static final String sDealByUserSelection=
            DealEntry.TABLE_NAME+"."+DealEntry.COLUMN_USER_KEY + " = ?";

    //deal.category_key = ?
    public static final String sDealByCategorySelection =
            DealEntry.TABLE_NAME+"."+DealEntry.COLUMN_CATEGORY_KEY+" = ?";

    //deal._id = ?
    public static final String sDealByIdSelection =
            DealEntry.TABLE_NAME+"._id = ?";

    //deal.state = ?
    public static final String sDealByStateSelection =
            DealEntry.TABLE_NAME+"."+DealEntry.COLUMN_STATE+" = ?";



    private Cursor getUserByLoginAndPassword(Uri uri, String[] projection, String sortOrder) {

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(UserEntry.TABLE_NAME);

        String user = UserEntry.getUserFromUri(uri);
        String password = UserEntry.getPasswordFromUri(uri);

        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sUserSelection,
                new String[]{user  ,password},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDealByUser(Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(CategoryEntry.TABLE_NAME);
        //Para filtrar por usuário
        //deal INNER JOIN user ON deal.user_id = user._id
        queryBuilder.setTables(
                DealEntry.TABLE_NAME + " INNER JOIN " +
                        UserEntry.TABLE_NAME +
                        " ON " + DealEntry.TABLE_NAME +"." + DealEntry.COLUMN_USER_KEY +
                        " = " + UserEntry.TABLE_NAME +
                        "." + UserEntry._ID);

        String user_id = uri.getQueryParameter(DealEntry.COLUMN_USER_KEY);

        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sDealByUserSelection,
                new String[]{user_id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDealByCategory(Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(CategoryEntry.TABLE_NAME);

        //Para filtrar por tipo de negócio
        //deal INNER JOIN category ON deal.category_id = category._id
        queryBuilder.setTables(
                DealEntry.TABLE_NAME + " INNER JOIN " +
                        CategoryEntry.TABLE_NAME +
                        " ON " + DealEntry.TABLE_NAME +"." + DealEntry.COLUMN_CATEGORY_KEY +
                        " = " + CategoryEntry.TABLE_NAME +
                        "." + CategoryEntry._ID);


        String category_id = "1";

        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sDealByCategorySelection,
                new String[]{category_id},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getCategoryByName(Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(CategoryEntry.TABLE_NAME);

        String categoryName = CategoryEntry.getCategoryNameFromUri(uri);

        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sCategorySelection,
                new String[]{categoryName},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getDealByID(Uri uri, String[] projection, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(DealEntry.TABLE_NAME);

        String dealId = DealEntry.getIdFromDealUri(uri);

        return queryBuilder.query(mOpenHelper.getReadableDatabase(),
                projection,
                sDealByIdSelection,
                new String[]{dealId},
                null,
                null,
                sortOrder
        );
    }

    static UriMatcher buildUriMatcher() {

        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = BazaarContract.CONTENT_AUTHORITY;

        // For each type of URI you want to add, create a corresponding code.
        matcher.addURI(authority, BazaarContract.PATH_USER, USER);
        matcher.addURI(authority, BazaarContract.PATH_USER +"/*/*", USER_WITH_LOGIN_AND_PASSWORD);

        matcher.addURI(authority, BazaarContract.PATH_CATEGORY, CATEGORY);
        matcher.addURI(authority, BazaarContract.PATH_CATEGORY + "/*",CATEGORY_WITH_NAME );

        matcher.addURI(authority, BazaarContract.PATH_DEAL, DEAL);
        matcher.addURI(authority, BazaarContract.PATH_DEAL + "/#", DEAL_WITH_ID);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new BazaarDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DEAL:
                return DealEntry.CONTENT_TYPE;
            case USER:
                return UserEntry.CONTENT_TYPE;
            case CATEGORY:
                return CategoryEntry.CONTENT_TYPE;
            case DEAL_WITH_ID:
                return DealEntry.CONTENT_ITEM_TYPE;

            case USER_WITH_LOGIN_AND_PASSWORD:
                return UserEntry.CONTENT_ITEM_TYPE;
            case CATEGORY_WITH_NAME:
                return CategoryEntry.CONTENT_TYPE;
            case DEAL_WITH_CATEGORY:
                return DealEntry.CONTENT_TYPE;
            case DEAL_WITH_USER_ID:
                return DealEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        Cursor retCursor;

        //O switch vai definir qual o tipo de requisição
        int matched = sUriMatcher.match(uri);
        Log.v(LOG_TAG,String.valueOf(matched));
        switch (matched) {

            case CATEGORY:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        CategoryEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case USER:{
                retCursor = mOpenHelper.getReadableDatabase().query(
                        UserEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            //Todos os negócios
            case DEAL: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        DealEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            //User
            case USER_WITH_LOGIN_AND_PASSWORD:
            {
                retCursor = getUserByLoginAndPassword(uri, projection, sortOrder);
                break;
            }

            //Categoria
            case CATEGORY_WITH_NAME: {
                retCursor = getCategoryByName(uri, projection, sortOrder);
                break;
            }

            //Negócio por id
            case DEAL_WITH_ID:{
                retCursor = getDealByID(uri,projection,sortOrder);
                break;
            }

            //Negócio por categoria
            case DEAL_WITH_CATEGORY: {
                retCursor = getDealByCategory(uri, projection, sortOrder);
                break;
            }

            //Negócio por usuário
            case DEAL_WITH_USER_ID: {
                retCursor = getDealByUser(uri, projection, sortOrder);
                break;
            }

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case USER: {
                long _id = db.insert(UserEntry.TABLE_NAME,null,values);
                if ( _id > 0 )
                    returnUri = UserEntry.buildUserUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case CATEGORY: {
                long _id = db.insert(CategoryEntry.TABLE_NAME,null,values);
                if ( _id > 0 )
                    returnUri = CategoryEntry.buildCategoryUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case DEAL: {
                long _id = db.insert(DealEntry.TABLE_NAME,null,values);
                if ( _id > 0 )
                    returnUri = DealEntry.buildDealUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        //Usado para avisar possíveis listeners de mudança no conteúdo da base de dados
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsDeleted;

        //Isto faz deletar todas as linhas e retornar o número de linhas deletadas
        if ( selection == null) selection = "1";

        switch (match) {
            case USER:
                rowsDeleted = db.delete(
                        UserEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case CATEGORY:
                rowsDeleted = db.delete(
                        CategoryEntry.TABLE_NAME, selection, selectionArgs);
                break;

            case DEAL:
                rowsDeleted = db.delete(
                        DealEntry.TABLE_NAME, selection, selectionArgs);
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // Notificar caso algo tenha sido deletado
        if (rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowsDeleted;
    }

    @Override
    public int update(
            Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int rowsUpdated;

        switch (match) {
            case USER:
                rowsUpdated = db.update(UserEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case CATEGORY:
                rowsUpdated = db.update(CategoryEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            case DEAL:
                rowsUpdated = db.update(DealEntry.TABLE_NAME, values, selection,
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }


        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }

}
