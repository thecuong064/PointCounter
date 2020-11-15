package com.thecuong064.pointcounter;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.SparseArray;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.thecuong064.pointcounter.base.BaseActivity;

import java.lang.ref.WeakReference;

import butterknife.BindView;
import butterknife.BindViews;

public class MainActivity extends BaseActivity {

    @BindViews({R.id.tab_scoreboard, R.id.tab_configurations})
    public View[] mTabs;
    @BindView(R.id.viewPager)
    public ViewPager viewPager;

    // ------------- CONFIGURATIONS ------------------
    public static String TOTAl_TIME_SAVE_KEY = "total_time";
    public static String SHORT_SHOT_CLOCK_TIME_SAVE_KEY = "short_shot_clock_time";
    public static String LONG_SHOT_CLOCK_TIME_SAVE_KEY = "long_shot_clock_time";

    public static final long DEFAULT_TOTAL_TIME = 12;
    public static final long DEFAULT_SHORT_SHOT_CLOCK_TIME = 14;
    public static final long DEFAULT_LONG_SHOT_CLOCK_TIME = 24;

    public static long totalTimeInMinute;
    public static long shortShotClockTimeInSecond;
    public static long longShotClockTimeInSecond;

    public static final int SCOREBOARD_TAB_INDEX = 0;
    public static final int CONFIGURATIONS_TAB_INDEX = 1;

    private int mCurTabPos = -1;
    private ScoreboardFragment scoreboardFragment;
    private ConfigurationsFragment configurationsFragment;
    private SparseArray<WeakReference<Fragment>> fragmentList;

    @Override
    protected int getContentViewId() {
        return R.layout.activity_main;
    }

    @Override
    protected void afterCreate() {
        super.afterCreate();

        setTitleBarVisible(false);
        initBottomTab();
        getConfigurations();
    }

    public void setConfiguration(long newValue, String save_key) {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = pref.edit();
        editor.putLong(save_key, newValue);
        editor.apply();
        getConfigurations();
        scoreboardFragment.initTimersView();
    }

    public void getConfigurations() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);
        totalTimeInMinute = pref.getLong(TOTAl_TIME_SAVE_KEY, DEFAULT_TOTAL_TIME);
        shortShotClockTimeInSecond = pref.getLong(SHORT_SHOT_CLOCK_TIME_SAVE_KEY,
                DEFAULT_SHORT_SHOT_CLOCK_TIME);
        longShotClockTimeInSecond = pref.getLong(LONG_SHOT_CLOCK_TIME_SAVE_KEY,
                DEFAULT_LONG_SHOT_CLOCK_TIME);
    }

    private void initBottomTab() {
        fragmentList = new SparseArray<>();
        scoreboardFragment = new ScoreboardFragment();
        configurationsFragment = new ConfigurationsFragment();
        fragmentList.put(SCOREBOARD_TAB_INDEX, new WeakReference<>(scoreboardFragment));
        fragmentList.put(CONFIGURATIONS_TAB_INDEX, new WeakReference<>(configurationsFragment));

        viewPager.setAdapter(new MainAdapter(getSupportFragmentManager(),
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragmentList));

        viewPager.setOffscreenPageLimit(2);
        for (int i = 0; i < 2; i++) {
            final int index = i;
            mTabs[i].setOnClickListener(v -> {
                changeTab(index);
            });
        }

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                changeTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        changeTab(SCOREBOARD_TAB_INDEX);
    }

    private void changeTab(int pos) {
        if (mCurTabPos == pos) {
            return;
        }
        mCurTabPos = pos;
        for (View tab : mTabs) {
            tab.setSelected(false);
        }
        mTabs[mCurTabPos].setSelected(true);
        viewPager.setCurrentItem(mCurTabPos, false);
    }

    @Override
    protected void onPause() {
        scoreboardFragment.pauseAllTimers();
        super.onPause();
    }
}