package com.deltabit.bazaar;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.deltabit.bazaar.data.BazaarContract;
import com.deltabit.bazaar.data.BazaarProvider;
import com.deltabit.bazaar.fragments.DealFragment;

public class DetailActivity extends AppCompatActivity {

    public static final int TYPE_REGISTER_NEW_ITEM = 0;
    public static final int TYPE_VIEW_ITEM = 1;

    public static final String TYPE_KEY = "type";

    int mItemId;
    int activityType;
    private Cursor mItemCursor;
    private Cursor mCategories;
    private ImageView mItemImage;
    private EditText mTxtItemName;
    private EditText mTxtItemPrice;
    private EditText mTxtQuantity;
    private Spinner mSpnCategory;
    private Button mBtnDelete;
    private Button mBtnUpdate;
    private Button mBtnBuy;
    private Button mBtnSell;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        setup();

        activityType = getIntent().getIntExtra(TYPE_KEY, -1);

        if (activityType == TYPE_VIEW_ITEM) {
            mItemId = getIntent().getIntExtra(DealAdapter.ITEM_ID_EXTRA_KEY, -1);
            setupViewItem();
        } else {
            mBtnSell.setVisibility(View.VISIBLE);
            mItemImage.setImageResource(Utility.getArtResourceForDeal(-1));
        }
    }

    private void setup() {
        mItemImage = (ImageView) findViewById(R.id.list_item_icon);
        mTxtItemName = (EditText) findViewById(R.id.list_item_name_textview);
        mTxtItemPrice = (EditText) findViewById(R.id.list_item_price_textview);
        mTxtQuantity = (EditText) findViewById(R.id.list_item_quantity_textview);

        mSpnCategory = (Spinner) findViewById(R.id.list_item_category_spinner);

        mBtnDelete = (Button) findViewById(R.id.btnDelete);
        mBtnUpdate = (Button) findViewById(R.id.btnUpdate);
        mBtnBuy = (Button) findViewById(R.id.btnBuy);
        mBtnSell = (Button) findViewById(R.id.btnSell);

        loadSpinner();
    }


    private void setupViewItem() {

        Uri uri = BazaarContract.DealEntry.buildDealUriWithDealId(mItemId);
        mItemCursor = getContentResolver()
                .query(uri,
                        null,
                        BazaarProvider.sDealByIdSelection,
                        new String[]{String.valueOf(mItemId)},
                        null
                );

        mItemCursor.moveToFirst();


        //Check if user is author of the item and if so, enable edit.
        boolean isAuthor = Utility.userId == mItemCursor.getInt(DealFragment.COL_USER_KEY);
        mTxtItemName.setFocusable(isAuthor);
        mTxtItemPrice.setFocusable(isAuthor);
        mTxtQuantity.setFocusable(isAuthor);

        mSpnCategory.setEnabled(isAuthor);
        if (isAuthor) {
            mBtnDelete.setVisibility(View.VISIBLE);
            mBtnUpdate.setVisibility(View.VISIBLE);
            mBtnBuy.setVisibility(View.GONE);
        } else {
            mBtnDelete.setVisibility(View.GONE);
            mBtnUpdate.setVisibility(View.GONE);
            mBtnBuy.setVisibility(View.VISIBLE);
        }

        setValues();
//        Toast.makeText(DetailActivity.this, mItemCursor.getString(DealFragment.COL_ITEM_NAME), Toast.LENGTH_SHORT).show();
    }

    private void loadSpinner() {
        String[] adapterCols = new String[]{BazaarContract.CategoryEntry.COLUMN_CATEGORY_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        Uri uri = BazaarContract.CategoryEntry.CONTENT_URI;

        mCategories = getContentResolver()
                .query(uri,
                        null,
                        null,
                        null,
                        null
                );

        SimpleCursorAdapter sca = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, mCategories, adapterCols, adapterRowViews, 0);
        sca.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mSpnCategory.setAdapter(sca);
    }

    private void setValues() {
        int categoryId = mItemCursor.getInt(DealFragment.COL_CATEGORY_KEY);
        mSpnCategory.setSelection(categoryId);

        mTxtItemName.setText(mItemCursor.getString(DealFragment.COL_ITEM_NAME));
        mTxtItemPrice.setText(mItemCursor.getString(DealFragment.COL_ITEM_PRICE));
        mTxtQuantity.setText(mItemCursor.getString(DealFragment.COL_ITEM_QUANTITY));

        mItemImage.setImageResource(Utility.getArtResourceForDeal(mItemCursor.getInt(DealFragment.COL_IMAGE_ID)));
    }


    public void btnDelete_onClick(View view) {
        getContentResolver()
                .delete(BazaarContract.DealEntry.CONTENT_URI,
                        BazaarProvider.sDealByIdSelection,
                        new String[]{String.valueOf(mItemId)}
                );

        finish();
    }

    public void btnUpdate_onClick(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_NAME, mTxtItemName.getText().toString());
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_QUANTITY, mTxtQuantity.getText().toString());
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_PRICE, mTxtItemPrice.getText().toString());

        long spinnerSelectedItem = mSpnCategory.getSelectedItemPosition();
        contentValues.put(BazaarContract.DealEntry.COLUMN_CATEGORY_KEY, spinnerSelectedItem);

        getContentResolver()
                .update(BazaarContract.DealEntry.CONTENT_URI,
                        contentValues,
                        BazaarProvider.sDealByIdSelection,
                        new String[]{String.valueOf(mItemId)}
                );

        Toast.makeText(this, R.string.row_updated_label, Toast.LENGTH_SHORT).show();
    }

    public void btnBuy_onClick(View view) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(BazaarContract.DealEntry.COLUMN_STATE, 1);

        getContentResolver()
                .update(BazaarContract.DealEntry.CONTENT_URI,
                        contentValues,
                        BazaarProvider.sDealByIdSelection,
                        new String[]{String.valueOf(mItemId)}
                );

        Toast.makeText(this, R.string.item_bought_message, Toast.LENGTH_SHORT).show();
        finish();
    }

    public void btnSell_onClick(View view) {
        ContentValues contentValues = new ContentValues();

        contentValues.put(BazaarContract.DealEntry.COLUMN_USER_KEY, Utility.userId);
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_NAME, mTxtItemName.getText().toString());
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_QUANTITY, mTxtQuantity.getText().toString());
        contentValues.put(BazaarContract.DealEntry.COLUMN_ITEM_PRICE, mTxtItemPrice.getText().toString());
        contentValues.put(BazaarContract.DealEntry.COLUMN_STATE, 0);

        long spinnerSelectedItem = mSpnCategory.getSelectedItemPosition();
        contentValues.put(BazaarContract.DealEntry.COLUMN_CATEGORY_KEY, spinnerSelectedItem);

        contentValues.put(BazaarContract.DealEntry.COLUMN_IMAGE_ID, spinnerSelectedItem);

        getContentResolver()
                .insert(BazaarContract.DealEntry.CONTENT_URI,
                        contentValues
                );

        Toast.makeText(this, "Item Cadastrado!", Toast.LENGTH_SHORT).show();

        finish();
    }
}
