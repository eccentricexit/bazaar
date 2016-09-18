package com.deltabit.bazaar;

import android.database.Cursor;

/**
 * Created by rigel on 17-Sep-16.
 */
public class Utility {
    public static int userId;

    public static int getArtResourceForDeal(int imageId) {
        int id;

        switch(imageId){
            case 1:
                id = R.drawable.ic_shoe;
                break;
            default:
                id = R.drawable.ic_shirt;
                break;
        }

        return id;
    }
}
