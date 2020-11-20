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
    public static String TOTAl_TIME_MINUTE_SAVE_KEY = "total_time_minute";
    public static String TOTAl_TIME_SECOND_SAVE_KEY = "total_time_second";
    public static String OFFENSE_TIME_SAVE_KEY = "offense_time";
    public static String AFTER_REBOUNDING_TIME_SAVE_KEY = "after_rebounding_time";
    public static String TIME_OUT_MINUTE_SAVE_KEY = "time_out_minute";
    public static String TIME_OUT_SECOND_SAVE_KEY = "time_out_second";
    public static String SHORT_BREAK_MINUTE_SAVE_KEY = "short_break_minute";
    public static String SHORT_BREAK_SEC_SAVE_KEY = "short_break_second";
    public static String LONG_BREAK_MINUTE_SAVE_KEY = "long_break_minute";
    public static String LONG_BREAK_SEC_SAVE_KEY = "long_break_second";

    public static final int DEFAULT_TOTAL_TIME_MIN = 12 * 60 * 1000;
    public static final int DEFAULT_TOTAL_TIME_SEC = 0;
    public static final int DEFAULT_SHORT_SHOT_CLOCK_TIME = 14 * 1000;
    public static final int DEFAULT_LONG_SHOT_CLOCK_TIME = 24 * 1000;
    public static final int DEFAULT_TIME_OUT_MIN = 1 * 60 * 1000;
    public static final int DEFAULT_TIME_OUT_SEC= 0;

    public static final int DEFAULT_SHORT_BREAK_MIN = 2 * 60 * 1000;
    public static final int DEFAULT_SHORT_BREAK_SEC = 0;
    public static final int DEFAULT_LONG_BREAK_MIN = 5 * 60 * 1000;
    public static final int DEFAULT_LONG_BREAK_SEC = 0;

    public static int totalTimeMinute;
    public static int totalTimeSecond;
    public static int afterReboundingTimeInSecond;
    public static int offenseTimeInSecond;
    public static int timeOutMinute;
    public static int timeOutSecond;
    public static int shortBreakMinute;
    public static int shortBreakSecond;
    public static int longBreakMinute;
    public static int longBreakSecond;

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
        scoreboardFragment.stopAndResetTimers();
    }

    public void getConfigurations() {
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(this);

        totalTimeMinute = minuteFromMillis(pref.getLong(TOTAl_TIME_MINUTE_SAVE_KEY, DEFAULT_TOTAL_TIME_MIN));
        totalTimeSecond = secondFromMillis(pref.getLong(TOTAl_TIME_SECOND_SAVE_KEY, DEFAULT_TOTAL_TIME_SEC));

        afterReboundingTimeInSecond = secondFromMillis(pref.getLong(AFTER_REBOUNDING_TIME_SAVE_KEY,
                DEFAULT_SHORT_SHOT_CLOCK_TIME));
        offenseTimeInSecond = secondFromMillis(pref.getLong(OFFENSE_TIME_SAVE_KEY,
                DEFAULT_LONG_SHOT_CLOCK_TIME));

        timeOutMinute = minuteFromMillis(pref.getLong(TIME_OUT_MINUTE_SAVE_KEY,
                DEFAULT_TIME_OUT_MIN));
        timeOutSecond = secondFromMillis(pref.getLong(TIME_OUT_SECOND_SAVE_KEY,
                DEFAULT_TIME_OUT_SEC));

        shortBreakMinute = minuteFromMillis(pref.getLong(SHORT_BREAK_MINUTE_SAVE_KEY,
                DEFAULT_SHORT_BREAK_MIN));
        shortBreakSecond = secondFromMillis(pref.getLong(SHORT_BREAK_SEC_SAVE_KEY,
                DEFAULT_SHORT_BREAK_SEC));

        longBreakMinute = minuteFromMillis(pref.getLong(LONG_BREAK_MINUTE_SAVE_KEY,
                DEFAULT_LONG_BREAK_MIN));
        longBreakSecond = secondFromMillis(pref.getLong(LONG_BREAK_SEC_SAVE_KEY,
                DEFAULT_LONG_BREAK_SEC));
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

    public void changeTab(int pos) {
        if (mCurTabPos == pos) {
            return;
        }
        mCurTabPos = pos;
        for (View tab : mTabs) {
            tab.setSelected(false);
        }
        mTabs[mCurTabPos].setSelected(true);
        viewPager.setCurrentItem(mCurTabPos, false);

        if (mCurTabPos == CONFIGURATIONS_TAB_INDEX) {
            configurationsFragment.initAndShowData();
        }
    }

    public static int minuteFromMillis(long timeInMillis) {
        return (int)(timeInMillis / 1000 / 60);
    }

    public static int secondFromMillis(long timeInMillis) {
        return (int)(timeInMillis / 1000);
    }

    public static long millisFromMinute(int timeInMinute) {
        return (long)timeInMinute * 1000 * 60;
    }

    public static long millisFromSecond(int timeInSeconds) {
        return (long)timeInSeconds * 1000;
    }

    @Override
    protected void onPause() {
        scoreboardFragment.pauseAllTimers();
        super.onPause();
    }
}