package eu.kingcastle.loancalculator;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.google.common.base.Strings;

import eu.kingcastle.loancalculator.model.Loan;
import eu.kingcastle.loancalculator.model.RepaymentMode;
import eu.kingcastle.loancalculator.model.RepaymentPeriod;

public class FragmentSettings extends Fragment {
	private Button buttonCalculate;
	private Button buttonRepaymentMode;
	private Button buttonRepaymentPeriod;
	private SeekBar seekBarInterest;
	private TextView textInterestRate;
	private TextView textRuntime;
	private TextView textInterestCurrent;
	private TextView textLoanAmount;
	private EditText editAmountLoan;
	private EditText editLife;
	// private TextView textResult;
	private double interestRate = 0.05f;
	private RepaymentMode repaymentMode = RepaymentMode.ANNUITY;
	private RepaymentPeriod repaymentPeriod = RepaymentPeriod.MONTHLY;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, null);

		buttonCalculate = ((Button) view.findViewById(R.id.buttonCalculate));
		buttonRepaymentMode = ((Button) view
				.findViewById(R.id.button_repayment_mode));
		buttonRepaymentPeriod = ((Button) view
				.findViewById(R.id.button_repayment_period));
		seekBarInterest = ((SeekBar) view.findViewById(R.id.seekBarInterest));
		textInterestCurrent = ((TextView) view
				.findViewById(R.id.textInterestCurrent));
		textLoanAmount = ((TextView) view.findViewById(R.id.textLoanAmount));
		textInterestRate = ((TextView) view.findViewById(R.id.textInterest));
		textRuntime = ((TextView) view.findViewById(R.id.textRuntime));
		// textResult = ((TextView) view.findViewById(R.id.textReslt));
		editAmountLoan = ((EditText) view.findViewById(R.id.editAmountLoan));
		editLife = ((EditText) view.findViewById(R.id.editLife));

		editLife.setFilters(new InputFilter[] { new InputFilterMinMax(1, 100) });
		editAmountLoan.setFilters(new InputFilter[] { new InputFilterMinMax(1,
				1000000) });

		buttonCalculate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				if (!validateInput()) {
					return;
				}

				final Loan loan = ((ActivityMain) getActivity()).getLoan();
				((ActivityMain) getActivity()).schedule.updateTable(loan, true);
			}
		});

		seekBarInterest
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
						textInterestCurrent.setText(progress + "% p.a.");
						interestRate = (double) progress / 100;
					}
				});

		buttonRepaymentMode.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				final String[] items = new String[3];
				items[0] = getString(R.string.repayment_mode_annuity);
				items[1] = getString(R.string.repayment_mode_bullet);
				items[2] = getString(R.string.repayment_mode_schedule);

				builder.setTitle(R.string.repayment_mode).setItems(items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								buttonRepaymentMode.setText(items[which]);
								if (which == 0) {
									repaymentMode = RepaymentMode.ANNUITY;
								} else if (which == 1) {
									repaymentMode = RepaymentMode.BULLET;
								} else if (which == 2) {
									repaymentMode = RepaymentMode.SCHEDULED;
								}
							}
						});
				builder.create().show();
			}
		});

		buttonRepaymentPeriod.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder = new AlertDialog.Builder(
						getActivity());

				final String[] items = new String[3];
				items[0] = getString(R.string.repayment_period_annual);
				items[1] = getString(R.string.repayment_period_quarterly);
				items[2] = getString(R.string.repayment_period_monthly);

				builder.setTitle(R.string.repayment_period).setItems(items,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								buttonRepaymentPeriod.setText(items[which]);
								if (which == 0) {
									repaymentPeriod = RepaymentPeriod.ANNUAL;
								} else if (which == 1) {
									repaymentPeriod = RepaymentPeriod.QUARTERLY;
								} else if (which == 2) {
									repaymentPeriod = RepaymentPeriod.MONTHLY;
								}
							}
						});
				builder.create().show();
			}
		});

		DFragmentHelp.addClickListener(textLoanAmount, getActivity(),
				R.string.loan_amount_help);
		DFragmentHelp.addClickListener(textInterestRate, getActivity(),
				R.string.interest_rate_help);
		DFragmentHelp.addClickListener(textRuntime, getActivity(),
				R.string.runtime_help);

		return view;
	}

	private boolean validateInput() {
		boolean inputIsValid = true;

		if (Strings.isNullOrEmpty(editAmountLoan.getText().toString())) {
			editAmountLoan.setError(getString(R.string.invalidInputAmountLoan));
			inputIsValid = false;
		}

		if (Strings.isNullOrEmpty(editLife.getText().toString())) {
			editLife.setError(getString(R.string.invalidInputLife));
			inputIsValid = false;
		}

		return inputIsValid;
	}

	public Loan getLoan() {
		int amountLoan = Integer.parseInt(editAmountLoan.getText().toString());
		int life = Integer.parseInt(editLife.getText().toString());

		final Loan loan = new Loan(amountLoan, interestRate, life,
				repaymentMode, repaymentPeriod);

		return loan;
	}
}
