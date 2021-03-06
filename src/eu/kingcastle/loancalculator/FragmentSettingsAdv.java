package eu.kingcastle.loancalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.common.base.Strings;

import eu.kingcastle.loancalculator.model.Loan;

public class FragmentSettingsAdv extends Fragment {
	private TextView textActivityCharge;
	private EditText editActivityCharge;
	private SeekBar seekBarProcessingFee;
	private TextView textProcessingFee;
	private TextView textProcessingFeeCurrent;
	private double processingFee;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings_adv, null);

		textProcessingFee = ((TextView) view
				.findViewById(R.id.textProcessingFee));
		textProcessingFeeCurrent = ((TextView) view
				.findViewById(R.id.textProcessingFeeCurrent));
		textActivityCharge = ((TextView) view
				.findViewById(R.id.textActivityCharge));
		editActivityCharge = ((EditText) view
				.findViewById(R.id.editActivityCharge));
		seekBarProcessingFee = (SeekBar) view
				.findViewById(R.id.seekProcessingFee);

		editActivityCharge
				.setFilters(new InputFilter[] { new InputFilterMinMax(0, 100000) });

		DFragmentHelp.addClickListener(textProcessingFee, getActivity(),
				R.string.processing_fee_help);
		DFragmentHelp.addClickListener(textActivityCharge, getActivity(),
				R.string.activity_charge_help);

		seekBarProcessingFee
				.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

					@Override
					public void onStopTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onStartTrackingTouch(SeekBar seekBar) {
					}

					@Override
					public void onProgressChanged(SeekBar arg0, int progress,
							boolean arg2) {
						textProcessingFeeCurrent.setText((float) progress / 10
								+ "");
						processingFee = (double) progress / 1000;
					}
				});

		return view;
	}

	public void addAdvancedValues(Loan loan) {
		if (isValidateInput()) {
			loan.setProcessingFee(processingFee);
			loan.setActivityCharg(Integer.parseInt(editActivityCharge.getText()
					.toString()));
		}
	}

	private boolean isValidateInput() {
		boolean inputIsValid = true;

		if (Strings.isNullOrEmpty(editActivityCharge.getText().toString())) {
			editActivityCharge.setError(getString(R.string.invalidInputLife));
			inputIsValid = false;
		}

		return inputIsValid;
	}

}
