package eu.kingcastle.loancalculator;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import eu.kingcastle.loancalculator.R;
import eu.kingcastle.loancalculator.model.Loan;

public class FragmentSchedule extends Fragment {
    private TableLayout tableSchedule;
    private Loan loan;

    public FragmentSchedule() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
	View view = inflater.inflate(R.layout.fragment_schedule, container, false);
	tableSchedule = (TableLayout) view.findViewById(R.id.tableLayoutSchedule);
	// fragment gets recreated from pager class during paging, so don't show
	// up the loading dialog
	if (loan != null) {
	    updateTable(loan, false);
	} else if (savedInstanceState != null) {
	    updateTable((Loan) savedInstanceState.getParcelable("loan"), false);
	}
	return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
	super.onSaveInstanceState(outState);
	outState.putParcelable("loan", loan);
    }

    // TODO: split loading dialog/calculation apart
    public void updateTable(Loan loan, final boolean showLoading) {
	if (loan == null) {
	    return;
	}
	this.loan = loan;

	// might not be set (fragment is not created immediately on orientation
	// change)
	if (tableSchedule == null) {
	    return;
	}

	new AsyncTask<Loan, Void, String[][]>() {
	    ProgressDialog progress;

	    @Override
	    protected void onPreExecute() {
		progress = new ProgressDialog(getActivity());
		progress.setMessage(getString(R.string.loading));
		if (showLoading) {
		    progress.show();
		}
	    }

	    @Override
	    protected String[][] doInBackground(Loan... params) {
		return params[0].getSchedule();
	    }

	    @Override
	    protected void onPostExecute(String[][] result) {
		tableSchedule.removeAllViews();

		LayoutInflater inflater = getActivity().getLayoutInflater();

		TableRow headerRow = new TableRow(getActivity());
		headerRow.addView(getTextView(getString(R.string.period),
			R.layout.schedule_header_text_view, inflater));
		headerRow.addView(getTextView(getString(R.string.repayment),
			R.layout.schedule_header_text_view, inflater));
		headerRow.addView(getTextView(getString(R.string.interest),
			R.layout.schedule_header_text_view, inflater));
		headerRow.addView(getTextView(getString(R.string.payment),
			R.layout.schedule_header_text_view, inflater));
		headerRow.addView(getTextView(getString(R.string.loan_left),
			R.layout.schedule_header_text_view, inflater));
		tableSchedule.addView(headerRow);

		for (int i = 0; i < result.length; i++) {
		    TableRow row = new TableRow(getActivity());
		    for (int j = 0; j < result[i].length; j++) {
			TextView col = (TextView) inflater.inflate(R.layout.schedule_row_text_view,
				null);
			col.setText(result[i][j]);
			row.addView(col);
		    }
		    tableSchedule.addView(row);
		}

		if (showLoading) {
		    progress.cancel();
		    ((ActivityMain) getActivity()).mViewPager.setCurrentItem(2);
		}
	    }
	}.execute(loan);

    }

    private TextView getTextView(String text, int R, LayoutInflater inflater) {
	TextView textView = (TextView) inflater.inflate(R, null);
	textView.setText(text);
	return textView;
    }
}
