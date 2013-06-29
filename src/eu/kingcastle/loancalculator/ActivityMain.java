package eu.kingcastle.loancalculator;

import eu.kingcastle.loancalculator.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ActivityMain extends FragmentActivity {
    public static final int ITEM_ID_SETTINGS_ADV = 0;
    public static final int ITEM_ID_SETTINGS = 1;
    public static final int ITEM_ID_SCHEDULE = 2;

    FragmentSettings settings;
    FragmentSettingsAdv settingsAdv;
    FragmentSchedule schedule;
    MyPagerAdapter myPagerAdapter;
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setContentView(R.layout.activity_main);

	myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
	mViewPager = (ViewPager) findViewById(R.id.pager);
	mViewPager.setAdapter(myPagerAdapter);

	// get or create settings fragment
	// so we are always using the correct reference
	Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		"android:switcher:" + mViewPager.getId() + ":"
			+ myPagerAdapter.getItemId(ITEM_ID_SETTINGS));
	if (fragment == null) {
	    settings = new FragmentSettings();
	} else {
	    settings = (FragmentSettings) fragment;
	}

	// get or create advanced settings fragment
	fragment = getSupportFragmentManager().findFragmentByTag(
		"android:switcher:" + mViewPager.getId() + ":"
			+ myPagerAdapter.getItemId(ITEM_ID_SETTINGS_ADV));
	if (fragment == null) {
	    settingsAdv = new FragmentSettingsAdv();
	} else {
	    settingsAdv = (FragmentSettingsAdv) fragment;
	}

	// get or create schedule fragment
	fragment = getSupportFragmentManager().findFragmentByTag(
		"android:switcher:" + mViewPager.getId() + ":"
			+ myPagerAdapter.getItemId(ITEM_ID_SCHEDULE));
	if (fragment == null) {
	    schedule = new FragmentSchedule();
	} else {
	    schedule = (FragmentSchedule) fragment;
	}

	// set current to settings
	mViewPager.setCurrentItem(1);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

	public MyPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int i) {
	    Fragment fragment = null;
	    switch (i) {
	    case ITEM_ID_SETTINGS_ADV:
		fragment = settingsAdv;
		break;
	    case ITEM_ID_SETTINGS:
		fragment = settings;
		break;
	    case ITEM_ID_SCHEDULE:
		fragment = schedule;
		break;
	    default:
		throw new IllegalArgumentException("pager item not implemented");
	    }

	    return fragment;
	}

	@Override
	public int getCount() {
	    return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    switch (position) {
	    case ITEM_ID_SETTINGS_ADV:
		return getString(R.string.settings_advanced);
	    case ITEM_ID_SETTINGS:
		return getString(R.string.settings);
	    case ITEM_ID_SCHEDULE:
		return getString(R.string.amortisationSchedule);
	    default:
		throw new IllegalArgumentException("pager item not implemented");
	    }
	}
    }
}
