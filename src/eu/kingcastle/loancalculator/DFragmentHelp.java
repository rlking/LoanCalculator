package eu.kingcastle.loancalculator;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DFragmentHelp extends DialogFragment {
	private TextView textHelp;

	public DFragmentHelp() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_helpdialog, container,
				false);
		getDialog().setTitle(R.string.help);
		textHelp = (TextView) view.findViewById(R.id.textHelp);
		textHelp.setEnabled(false);
		textHelp.setText("fsfasfasd\nfasdfsadfsadfasd\nfasdfasfsdafasd");
		
		return view;
	}
}
