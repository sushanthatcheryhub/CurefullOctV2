package adpter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import fragment.healthapp.Fragment_Health_Todays;
import fragment.healthapp.Fragment_Health_Trends;

/**
 * Created by Curefull on 26-05-2017.
 */

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Fragment_Health_Todays tab1 = new Fragment_Health_Todays();
                return tab1;
            case 1:
                Fragment_Health_Trends tab2 = new Fragment_Health_Trends();
                return tab2;

            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
