package com.deltabit.bazaar.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.deltabit.bazaar.DealAdapter;
import com.deltabit.bazaar.DetailActivity;
import com.deltabit.bazaar.R;
import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarProvider;

/**
 * A simple {@link Fragment} subclass.
 */
public class DealFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, DealAdapter.RecyclerViewClickListener {

    private static final int DEAL_LOADER = 0;

    protected static final String[] DEAL_COLUMNS = {
            BazaarContract.DealEntry.TABLE_NAME + "." + BazaarContract.DealEntry._ID,
            BazaarContract.DealEntry.COLUMN_USER_KEY,
            BazaarContract.DealEntry.COLUMN_IMAGE_ID,
            BazaarContract.DealEntry.COLUMN_CATEGORY_KEY,
            BazaarContract.DealEntry.COLUMN_ITEM_NAME,
            BazaarContract.DealEntry.COLUMN_ITEM_PRICE,
            BazaarContract.DealEntry.COLUMN_ITEM_QUANTITY,
            BazaarContract.DealEntry.COLUMN_STATE
    };

    // These indices are tied to DEAL_COLUMNS_COLUMNS.  If DEAL_COLUMNS changes, these must change
    public static final int COL_DEAL_ID = 0;
    public static final int COL_USER_KEY = 1;
    public static final int COL_IMAGE_ID = 2;
    public static final int COL_CATEGORY_KEY = 3;
    public static final int COL_ITEM_NAME = 4;
    public static final int COL_ITEM_PRICE = 5;
    public static final int COL_ITEM_QUANTITY = 6;
    public static final int COL_STATE = 7;


    protected RecyclerView mRecyclerView;
    protected DealAdapter mDealAdapter;

    public DealFragment() {
    }

    @Override
    public void onResume() {
        super.onResume();
//        getLoaderManager().restartLoader(DEAL_LOADER,null,this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        Uri uri = BazaarContract.DealEntry.buildDealUriWithState(0);

        return new CursorLoader(getActivity(),
                uri,
                null,
                BazaarProvider.sDealByStateSelection,
                new String[]{String.valueOf(0)},
                null);
    }



    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        getLoaderManager().initLoader(DEAL_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mDealAdapter.swapCursor(data);
//          DatabaseUtils.dumpCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mDealAdapter.swapCursor(null);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_deal_base, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_deal);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View emptyView = rootView.findViewById(R.id.recyclerview_deal_empty);


        mDealAdapter = new DealAdapter(getContext(), this, emptyView);
        mRecyclerView.setAdapter(mDealAdapter);
        return rootView;
    }

    @Override
    public void recyclerViewListClicked(DealAdapter.DealAdapterViewHolder vh, int position) {
//        Toast.makeText(getContext(), "Recycler Item clicked", Toast.LENGTH_SHORT).show();
//        Toast.makeText(getContext(), "DealFragment: id:"+ vh.mItem_Id+", position: "+position, Toast.LENGTH_SHORT).show();


        Intent i = new Intent(getContext(),DetailActivity.class);
        i.putExtra(DetailActivity.TYPE_KEY,DetailActivity.TYPE_VIEW_ITEM);
        i.putExtra(DealAdapter.ITEM_ID_EXTRA_KEY,vh.mItem_Id);
        startActivity(i);
    }
}
