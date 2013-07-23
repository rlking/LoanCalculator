package eu.kingcastle.loancalculator;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

public class DFragmentHelp extends DialogFragment {
	private TextView textHelp;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_help_dialog, container,
				false);
		getDialog().setTitle(R.string.help);
		getDialog().setCanceledOnTouchOutside(true);
		textHelp = (TextView) view.findViewById(R.id.textHelp);

		Bundle data = getArguments();
		if (data != null) {
			int helpStringId = data.getInt("helpStringId");
			if (helpStringId != 0) {
				textHelp.setText(getString(helpStringId));
			}
		}
		return view;
	}

	/**
	 * add click listener which shows help dialog with given string
	 * 
	 * @param view
	 * @param activity
	 * @param resId
	 */
	public static void addClickListener(final View view,
			final FragmentActivity activity, final int resId) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				FragmentManager fm = activity.getSupportFragmentManager();
				DFragmentHelp help = new DFragmentHelp();
				Bundle data = new Bundle();
				data.putInt("helpStringId", resId);
				help.setArguments(data);
				help.show(fm, "fragment_help");
			}
		});
	}
}
