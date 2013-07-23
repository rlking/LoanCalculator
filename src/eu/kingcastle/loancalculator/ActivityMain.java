package eu.kingcastle.loancalculator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class ActivityMain extends FragmentActivity {
	public static final int ITEM_ID_SETTINGS_ADV = 0;
	public static final int ITEM_ID_SETTINGS = 1;
	public static final int ITEM_ID_SCHEDULE = 2;

	FragmentSettings settings;
	FragmentSettingsAdv settingsAdv;
	FragmentSchedule schedule;
	MyPagerAdapter myPagerAdapter;
	ViewPager mViewPager;
	Menu menu;

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
		mViewPager.setCurrentItem(ITEM_ID_SETTINGS);
	}

	@Override
	public boolean onCreateOptionsMenu(final Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		this.menu = menu;
		menu.getItem(0).setVisible(false);
		menu.getItem(1).setVisible(false);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mViewPager.getCurrentItem() == ITEM_ID_SCHEDULE) {
			menu.getItem(0).setVisible(true);
			menu.getItem(1).setVisible(true);
		} else {
			menu.getItem(0).setVisible(false);
			menu.getItem(1).setVisible(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	@SuppressWarnings("deprecation")
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_send_via_email:
			Intent i = new Intent(Intent.ACTION_SEND);
			i.setType("message/rfc822");
			i.putExtra(Intent.EXTRA_EMAIL,
					new String[] { "recipient@example.com" });
			i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
			i.putExtra(Intent.EXTRA_TEXT, settings.getLoan()
					.getScheduleAsAsciiTable());
			try {
				startActivity(Intent.createChooser(i, "Send mail..."));
			} catch (android.content.ActivityNotFoundException ex) {
				Toast.makeText(this, "There are no email clients installed.",
						Toast.LENGTH_SHORT).show();
			}

			break;
		case R.id.menu_copy_to_clipboard:
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(settings.getLoan().getScheduleAsAsciiTable());
			Toast.makeText(this, getString(R.string.copied_to_clipboard),
					Toast.LENGTH_SHORT).show();
			break;
		}

		return super.onOptionsItemSelected(item);
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
