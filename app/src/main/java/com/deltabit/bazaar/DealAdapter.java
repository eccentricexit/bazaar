package com.deltabit.bazaar;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.deltabit.bazaar.fragments.DealFragment;

import java.text.NumberFormat;

/**
 * Created by rigel on 17-Sep-16.
 */
public class DealAdapter extends RecyclerView.Adapter<DealAdapter.DealAdapterViewHolder> {

    public static final String ITEM_ID_EXTRA_KEY = "item_id";
    private static RecyclerViewClickListener mItemListener;
    private final Context mContext;
    private final View mEmptyView;
    private Cursor mCursor;


    public DealAdapter(Context context, RecyclerViewClickListener itemListener, View emptyView) {
        mContext = context;
        mItemListener = itemListener;
        mEmptyView = emptyView;
    }

    public void swapCursor(Cursor newCursor) {
        mCursor = newCursor;
        notifyDataSetChanged();
        mEmptyView.setVisibility(getItemCount() == 0 ? View.VISIBLE : View.GONE);
    }

    @Override
    public DealAdapterViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        int layoutId = R.layout.list_item_deal;
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);

        view.setFocusable(true);
        return new DealAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DealAdapterViewHolder holder, int position) {
        mCursor.moveToPosition(position);
        int imageId = mCursor.getInt(DealFragment.COL_IMAGE_ID);
        int defaultImage = Utility.getArtResourceForDeal(imageId);
        holder.mItem_Id = mCursor.getInt(DealFragment.COL_DEAL_ID);
        holder.mNameView.setText(mCursor.getString(DealFragment.COL_ITEM_NAME));
        holder.mIconView.setImageResource(defaultImage);

        float price = Float.valueOf(mCursor.getString(DealFragment.COL_ITEM_PRICE));
        NumberFormat currencyInstance = NumberFormat.getCurrencyInstance();

        String strPrice = currencyInstance.format(price);
        holder.mPriceView.setText(strPrice);
    }

    @Override
    public int getItemCount() {
        if (null == mCursor) return 0;
        return mCursor.getCount();
    }

    public interface RecyclerViewClickListener {
        void recyclerViewListClicked(DealAdapterViewHolder vh, int id);
    }

    public class DealAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final ImageView mIconView;
        public final TextView mNameView;
        public final TextView mPriceView;
        public int mItem_Id;

        public DealAdapterViewHolder(View itemView) {
            super(itemView);

            mIconView = (ImageView) itemView.findViewById(R.id.list_item_icon);
            mNameView = (TextView) itemView.findViewById(R.id.list_item_name_textview);
            mPriceView = (TextView) itemView.findViewById(R.id.list_item_price_textview);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mItemListener.recyclerViewListClicked(this, getPosition());
        }
    }

}
