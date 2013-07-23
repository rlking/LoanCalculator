package eu.kingcastle.loancalculator;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;

public class DFragmentHelp extends DialogFragment {

	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		Bundle data = getArguments();
		int helpStringId = 0;
		if (data != null) {
			helpStringId = data.getInt("helpStringId");
		}

		return new AlertDialog.Builder(getActivity())
				// .setIcon(R.drawable.info)
				.setTitle(R.string.help)
				.setMessage(helpStringId)
				.setPositiveButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int whichButton) {
								;
							}
						}).create();
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
