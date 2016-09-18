package com.deltabit.bazaar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.deltabit.bazaar.fragments.DealFragment;
import com.deltabit.bazaar.fragments.SellFragment;

/**
 * Created by rigel on 17-Sep-16.
 */

//Gerencia fragments da tabbed view
public class TabsPagerAdapter extends FragmentPagerAdapter {

    public TabsPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:

                return new DealFragment();
            case 1:

                return new SellFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }

}
