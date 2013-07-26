package eu.kingcastle.loancalculator.model;

import java.text.DecimalFormat;

import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class Loan implements Parcelable {
	private final double amountLoan;
	private final double interestRatePerAnnum;
	private final int lifeTime;
	private final RepaymentMode mode;
	private final RepaymentPeriod time;
	private double processingFee;
	private int activityCharge;

	public Loan(double amountLoan, double interestRate, int lifeTime,
			RepaymentMode mode, RepaymentPeriod time) {
		this.amountLoan = amountLoan;
		this.interestRatePerAnnum = interestRate;
		this.lifeTime = lifeTime;
		this.mode = mode;
		this.time = time;
	}

	/**
	 * if the repayment period is higher (i.e monthly) than the interest period
	 * (p.a.), then interest will be payed on each repayment period
	 * 
	 * @return
	 */
	public double getAnnuity() {
		double periodInterestRate = interestRatePerAnnum / time.getPeriod();
		int periodLifeTime = lifeTime * time.getPeriod();
		double amountLoanWithFee = amountLoan / (1 - processingFee);

		double A = amountLoanWithFee
				* ((periodInterestRate * Math.pow(periodInterestRate + 1,
						periodLifeTime)) / (Math.pow(periodInterestRate + 1,
						periodLifeTime) - 1));
		return A;
	}

	public String[][] getSchedule() {
		double periodInterestRate = interestRatePerAnnum / time.getPeriod();
		int periodLifeTime = lifeTime * time.getPeriod();

		String[][] schedule = new String[periodLifeTime][6];
		final double A = getAnnuity();
		double interestToPay = 0;
		double repaymentOfLoan = 0;
		double amountLoanWithFee = amountLoan / (1 - processingFee);
		double restOfLoan = amountLoanWithFee;

		// DecimalFormat df = new DecimalFormat("#.##");
		DecimalFormat df = new DecimalFormat("###,###");

		for (int period = 0; period < periodLifeTime; period++) {
			String[] row = new String[6];

			interestToPay = restOfLoan * periodInterestRate;
			repaymentOfLoan = A - interestToPay;
			restOfLoan -= repaymentOfLoan;
			int currentActivityCharge = (period % time.getPeriod() == 0) ? activityCharge
					: 0;

			row[0] = getPeriodReadable(period);
			row[1] = df.format(repaymentOfLoan); // repayment of loan
			row[2] = df.format(interestToPay); // interest to pay
			row[3] = df.format(A + currentActivityCharge); // payment
			row[4] = df.format(Math.abs(restOfLoan));
			row[5] = currentActivityCharge + "";

			schedule[period] = row;
		}

		return schedule;
	}

	/**
	 * returns formatted cells with 6 columns.
	 * 
	 * @return
	 */
	public String getScheduleAsAsciiTable() {
		String[][] scheduleRaw = getSchedule();
		StringBuilder schedule = new StringBuilder();

		// loan amount will be the largest number in the table
		// so this gives a good size for the table
		int cellWidth = (amountLoan + "").length();
		if (cellWidth < 5) {
			cellWidth = 5;
		}
		schedule.append(Strings.padStart("1*", cellWidth, ' '));
		schedule.append(Strings.padStart("2*", cellWidth, ' '));
		schedule.append(Strings.padStart("3*", cellWidth, ' '));
		schedule.append(Strings.padStart("4*", cellWidth, ' '));
		schedule.append(Strings.padStart("5*", cellWidth, ' '));
		schedule.append(Strings.padStart("6*", cellWidth, ' '));
		schedule.append("\n");
		schedule.append(Strings.padStart("", cellWidth * 6, '-'));
		schedule.append("\n");

		for (int i = 0; i < scheduleRaw.length; i++) {
			for (int j = 0; j < scheduleRaw[i].length; j++) {
				schedule.append(Strings.padStart(scheduleRaw[i][j], cellWidth,
						' '));
			}
			schedule.append("\n");
		}
		Log.d("", schedule.toString());

		return schedule.toString();
	}

	private String getPeriodReadable(int periodLifetime) {
		switch (time) {
		case ANNUAL:
			return (periodLifetime + 1) + "";
		case QUARTERLY:
			return ((periodLifetime % 4) + 1) + "/"
					+ ((periodLifetime / 4) + 1);
		case MONTHLY:
			return Strings.padStart(((periodLifetime % 12) + 1) + "", 2, '0')
					+ "/" + ((periodLifetime / 12) + 1);
		default:
			return null;
		}
	}

	public double getAmountLoan() {
		return amountLoan;
	}

	public double getInterestRate() {
		return interestRatePerAnnum;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public RepaymentMode getMode() {
		return mode;
	}

	public RepaymentPeriod getTime() {
		return time;
	}

	public double getProcessingFee() {
		return processingFee;
	}

	public void setProcessingFee(double processingFee) {
		this.processingFee = processingFee;
	}

	public int getActivityCharge() {
		return activityCharge;
	}

	public void setActivityCharg(int activityCharge) {
		this.activityCharge = activityCharge;
	}

	public static final Parcelable.Creator<Loan> CREATOR = new Parcelable.Creator<Loan>() {
		public Loan createFromParcel(Parcel in) {
			return new Loan(in);
		}

		public Loan[] newArray(int size) {
			return new Loan[size];
		}
	};

	private Loan(Parcel in) {
		this.amountLoan = in.readDouble();
		this.interestRatePerAnnum = in.readDouble();
		this.lifeTime = in.readInt();
		this.mode = (RepaymentMode) in.readSerializable();
		this.time = (RepaymentPeriod) in.readSerializable();
		this.processingFee = in.readDouble();
		this.activityCharge = in.readInt();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(amountLoan);
		dest.writeDouble(interestRatePerAnnum);
		dest.writeInt(lifeTime);
		dest.writeSerializable(mode);
		dest.writeSerializable(time);
		dest.writeDouble(processingFee);
		dest.writeInt(activityCharge);
	}
}
