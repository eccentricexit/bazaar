package com.deltabit.bazaar.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by rigel on 16-Sep-16.
 */

/*
Define nomes de tabela e colunas para a base de dados.
*/
public class BazaarContract {
    public static final String CONTENT_AUTHORITY = "com.deltabit.bazaar";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    //Caminhos do content provider
    public static final String PATH_DEAL = "deal";
    public static final String PATH_USER = "user";
    public static final String PATH_CATEGORY = "category";

    //Classe interna para definir a tabela usuario
    public static final class UserEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_USER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_USER;


        public static final String TABLE_NAME = "user";
        public static final String COLUMN_USERNAME = "username";
        public static final String COLUMN_PASSWORD = "password";

        public static Uri buildUserUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getUserFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_USERNAME);
        }

        public static String getPasswordFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_PASSWORD);
        }

    }

    //Tabela Categoria
    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_CATEGORY).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_CATEGORY;

        public static final String TABLE_NAME = "category";
        public static final String COLUMN_CATEGORY_NAME = "category_name";

        public static Uri buildCategoryUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getCategoryNameFromUri(Uri uri) {
            return uri.getQueryParameter(COLUMN_CATEGORY_NAME);
        }

        public static Uri buildCategory(String category) {
            return CONTENT_URI.buildUpon().appendQueryParameter(COLUMN_CATEGORY_NAME, category).build();
        }

    }

    //Tabela Negócios (ou Deal)
    public static final class DealEntry implements BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_DEAL).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEAL;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_DEAL;

        public static final String TABLE_NAME = "deal";
        public static final String COLUMN_ID_KEY = "_ID";
        public static final String COLUMN_USER_KEY = "user_id";
        public static final String COLUMN_IMAGE_ID = "image_id";
        public static final String COLUMN_CATEGORY_KEY = "category_id";
        public static final String COLUMN_ITEM_NAME = "item_name";
        public static final String COLUMN_ITEM_PRICE = "item_price";
        public static final String COLUMN_ITEM_QUANTITY = "item_quantity";
        public static final String COLUMN_STATE = "state";


        public static Uri buildDealUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static Uri buildDealUriWithState(int state) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_STATE, String.valueOf(state)).build();
        }

        public static Uri buildDealUriWithDealId(int dealId) {
            return ContentUris.withAppendedId(CONTENT_URI, dealId);
        }

        public static Uri buildDealUriWithUserId(int userId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_USER_KEY, String.valueOf(userId)).build();
        }

        public static String getIdFromDealUri(Uri uri) {
            return uri.getLastPathSegment();
        }

        public static Uri buildDealUriByCategory(int categoryId) {
            return CONTENT_URI.buildUpon()
                    .appendQueryParameter(COLUMN_CATEGORY_KEY, String.valueOf(categoryId)).build();
        }
    }


}
