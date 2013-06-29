package eu.kingcastle.loancalculator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.common.base.Strings;

import eu.kingcastle.loancalculator.model.Loan;

public class FragmentSettingsAdv extends Fragment {
    private TextView textChargesLoan;
    private EditText editChargeCredit;
    private TextView textActivityCharge;
    private EditText editActivityCharge;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_settings_adv, null);

	textChargesLoan = ((TextView) view.findViewById(R.id.textChargesLoan));
	editChargeCredit = ((EditText) view.findViewById(R.id.editChargesLoan));
	textActivityCharge = ((TextView) view.findViewById(R.id.textActivityCharge));
	editActivityCharge = ((EditText) view.findViewById(R.id.editActivityCharge));

	editActivityCharge.setFilters(new InputFilter[] { new InputFilterMinMax(0, 100) });
	editChargeCredit.setFilters(new InputFilter[] { new InputFilterMinMax(0, 10000) });

	textChargesLoan.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		DFragmentHelp help = new DFragmentHelp();
		help.show(fm, "fragment_help");
	    }
	});
	textActivityCharge.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {
		FragmentManager fm = getActivity().getSupportFragmentManager();
		DFragmentHelp help = new DFragmentHelp();
		help.show(fm, "fragment_help");
	    }
	});

	return view;
    }
    
    public void addAdvancedValues(Loan loan) {
	if(isValidateInput()) {
	    
	}
    }
    
    private boolean isValidateInput() {
	boolean inputIsValid = true;

	if (Strings.isNullOrEmpty(editChargeCredit.getText().toString())) {
	    editChargeCredit.setError(getString(R.string.invalidInputAmountLoan));
	    inputIsValid = false;
	}

	if (Strings.isNullOrEmpty(editActivityCharge.getText().toString())) {
	    editActivityCharge.setError(getString(R.string.invalidInputLife));
	    inputIsValid = false;
	}

	return inputIsValid;
    }

}
