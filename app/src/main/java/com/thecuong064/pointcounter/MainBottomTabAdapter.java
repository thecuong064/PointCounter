package com.thecuong064.pointcounter;

import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.lang.ref.WeakReference;

public class MainBottomTabAdapter extends FragmentPagerAdapter {

    private SparseArray<WeakReference<Fragment>> fragmentList;

    public MainBottomTabAdapter(@NonNull FragmentManager fm, int behavior, SparseArray<WeakReference<Fragment>> fragmentList) {
        super(fm, behavior);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        if (fragmentList.get(position) != null && fragmentList.get(position).get() != null) {
            return fragmentList.get(position).get();
        } else {
            Fragment fragment;
            switch (position) {
                case MainActivity.SCOREBOARD_TAB_INDEX:
                    fragment = new ScoreboardFragment();
                    break;
                case MainActivity.SHOT_CLOCK_TAB_INDEX:
                    fragment = new ShotClockFragment();
                    break;
                case MainActivity.CONFIGURATIONS_TAB_INDEX:
                    fragment = new ConfigurationsFragment();
                    break;
                default:
                    fragment = new ScoreboardFragment();
            }
            fragmentList.put(position, new WeakReference<>(fragment));
            return fragment;
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
