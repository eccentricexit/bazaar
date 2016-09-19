package com.deltabit.bazaar;

/**
 * Created by rigel on 17-Sep-16.
 */
public class Utility {
    public static int userId;

    public static int getArtResourceForDeal(int imageId) {
        int id = 0;

        switch (imageId) {
            case -1:
                id = R.drawable.ic_logo_icon;
                break;
            case 0:
                id = R.drawable.ic_dress;
                break;
            case 1:
                id = R.drawable.ic_shoe;
                break;
            case 2:
                id = R.drawable.ic_jacket;
                break;
            case 3:
                id = R.drawable.ic_sunglasses;
                break;
            case 4:
                id = R.drawable.ic_short;
                break;
        }

        return id;
    }
}
