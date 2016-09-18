package com.deltabit.bazaar.fragments;


import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.deltabit.bazaar.R;
import com.deltabit.bazaar.Utility;
import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class SellFragment extends DealFragment {


    public SellFragment() {

    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        int userId = Utility.userId;
        Uri uri = BazaarContract.DealEntry.buildDealUriWithUserId(userId);

        return new CursorLoader(getActivity(),
                uri,
                DealFragment.DEAL_COLUMNS,
                BazaarProvider.sDealByUserSelection,
                new String[]{String.valueOf(userId)},
                null);
    }


}
