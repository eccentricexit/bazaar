package com.deltabit.bazaar.data;

import android.content.UriMatcher;
import android.net.Uri;
import android.test.AndroidTestCase;

/**
 * Created by rigel on 16-Sep-16.
 */
public class TestMatcher extends AndroidTestCase {

    // content://com.deltabit.bazaar/category
    private static final Uri TEST_CATEGORY_DIR = BazaarContract.CategoryEntry.CONTENT_URI;

    // content://com.deltabit.bazaar/user
    private static final Uri TEST_USER_DIR = BazaarContract.UserEntry.CONTENT_URI;

    // content://com.deltabit.bazaar/deal
    private static final Uri TEST_DEAL_DIR = BazaarContract.DealEntry.CONTENT_URI;

    public void testUriMatcher() {
        UriMatcher testMatcher = BazaarProvider.buildUriMatcher();

        assertEquals("Error: The USER URI was matched incorrectly.",
                testMatcher.match(TEST_USER_DIR), BazaarProvider.USER);
        assertEquals("Error: CATEGORY URI was matched incorrectly.",
                testMatcher.match(TEST_CATEGORY_DIR), BazaarProvider.CATEGORY);
        assertEquals("Error: The CATEGORY URI was matched incorrectly.",
                testMatcher.match(TEST_DEAL_DIR), BazaarProvider.DEAL);
    }
}
