package Adapter;

import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import Fragment.CubeCaptureFragment2;
import Fragment.HighlightsFragment;
import Fragment.TrendsFragment;
import Fragment.TrendsFragment2;

/**
 * Created by AASHI on 02-04-2018.
 */

public class TabAdapter extends FragmentPagerAdapter {

    int page_count;
    TabLayout tabLayout;


    public TabAdapter(FragmentManager fm, int page_count) {
        super(fm);
        this.page_count = page_count;

      //  Log.d("tab--", String.valueOf(page_count));

    }


    @Override
    public Fragment getItem(int position) {


        switch (position) {

            case 0:
                return new HighlightsFragment();

            case 1:
                return new CubeCaptureFragment2();

            case 2:
                return new TrendsFragment2();

            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return page_count;
    }
}
