package com.deltabit.bazaar.fragments;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_deal_sell, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview_deal);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        View emptyView = rootView.findViewById(R.id.recyclerview_deal_empty);


        mDealAdapter = new DealAdapter(getContext(), this, emptyView);
        mRecyclerView.setAdapter(mDealAdapter);

        rootView.findViewById(R.id.add_item_fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), DetailActivity.class);
                i.putExtra(DetailActivity.TYPE_KEY, DetailActivity.TYPE_REGISTER_NEW_ITEM);
                startActivity(i);
            }
        });

        return rootView;
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
