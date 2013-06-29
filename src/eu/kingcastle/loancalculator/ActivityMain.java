package eu.kingcastle.loancalculator;

import eu.kingcastle.loancalculator.R;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;

public class ActivityMain extends FragmentActivity {
    public static final int RESULT_LOAN = 0;
    public static final String RESULT_DATA_LOAN = "loan";
    FragmentMain main;
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

	// get or create main fragment
	// so we are always using the correct reference
	Fragment fragment = getSupportFragmentManager().findFragmentByTag(
		"android:switcher:" + mViewPager.getId() + ":" + myPagerAdapter.getItemId(0));
	if (fragment == null) {
	    main = new FragmentMain();
	} else {
	    main = (FragmentMain) fragment;
	}

	// get or create schedule fragment
	fragment = getSupportFragmentManager().findFragmentByTag(
		"android:switcher:" + mViewPager.getId() + ":" + myPagerAdapter.getItemId(1));
	if (fragment == null) {
	    schedule = new FragmentSchedule();
	} else {
	    schedule = (FragmentSchedule) fragment;
	}
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

	public MyPagerAdapter(FragmentManager fm) {
	    super(fm);
	}

	@Override
	public Fragment getItem(int i) {
	    Fragment fragment = null;
	    switch (i) {
	    case 0:
		fragment = main;
		break;
	    case 1:
		fragment = schedule;
		break;
	    default:
		throw new IllegalArgumentException("pager item not implemented");
	    }

	    return fragment;
	}

	@Override
	public int getCount() {
	    return 2;
	}

	@Override
	public CharSequence getPageTitle(int position) {
	    switch (position) {
	    case 0:
		return getString(R.string.action_settings);
	    case 1:
		return getString(R.string.amortisationSchedule);
	    default:
		throw new IllegalArgumentException("pager item not implemented");
	    }
	}
    }
}
