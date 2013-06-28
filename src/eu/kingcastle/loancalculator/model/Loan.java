package eu.kingcastle.loancalculator.model;

import java.text.DecimalFormat;

import com.google.common.base.Strings;

import android.os.Parcel;
import android.os.Parcelable;

public class Loan implements Parcelable {
    private final double amountLoan;
    private final double interestRatePerAnnum;
    private final int lifeTime;
    private final RepaymentMode mode;
    private final RepaymentPeriod time;

    public Loan(double amountLoan, double interestRate, int lifeTime, RepaymentMode mode,
	    RepaymentPeriod time) {
	this.amountLoan = amountLoan;
	this.interestRatePerAnnum = interestRate;
	this.lifeTime = lifeTime;
	this.mode = mode;
	this.time = time;
    }

    public double getAnnuity() {

	double periodInterestRate = 0;
	int periodLifeTime = 0;

	switch (time) {
	case ANNUAL:
	    periodInterestRate = interestRatePerAnnum;
	    periodLifeTime = lifeTime;
	    break;
	case QUARTERLY:
	    periodInterestRate = interestRatePerAnnum / 4;
	    periodLifeTime = lifeTime * 4;
	    break;
	case MONTHLY:
	    periodInterestRate = interestRatePerAnnum / 12;
	    periodLifeTime = lifeTime * 12;
	    break;
	default:
	    return 0;
	}

	double A = amountLoan
		* ((periodInterestRate * Math.pow(periodInterestRate + 1, periodLifeTime)) / (Math
			.pow(periodInterestRate + 1, periodLifeTime) - 1));
	return A;
    }

    public String[][] getSchedule() {

	double periodInterestRate = 0;
	int periodLifeTime = 0;

	switch (time) {
	case ANNUAL:
	    periodInterestRate = interestRatePerAnnum;
	    periodLifeTime = lifeTime;
	    break;
	case QUARTERLY:
	    periodInterestRate = interestRatePerAnnum / 4;
	    periodLifeTime = lifeTime * 4;
	    break;
	case MONTHLY:
	    periodInterestRate = interestRatePerAnnum / 12;
	    periodLifeTime = lifeTime * 12;
	    break;
	default:
	    return null;
	}

	String[][] schedule = new String[periodLifeTime][5];
	final double A = getAnnuity();
	double interestToPay = 0;
	double repaymentOfLoan = 0;
	double restOfLoan = amountLoan;

	// DecimalFormat df = new DecimalFormat("#.##");
	DecimalFormat df = new DecimalFormat("#");

	for (int period = 0; period < periodLifeTime; period++) {
	    String[] row = new String[5];

	    interestToPay = restOfLoan * periodInterestRate;
	    repaymentOfLoan = A - interestToPay;
	    restOfLoan -= repaymentOfLoan;

	    row[0] = getPeriodReadable(period);
	    row[1] = df.format(repaymentOfLoan); // repayment of loan
	    row[2] = df.format(interestToPay); // interest to pay
	    row[3] = df.format(A); // payment
	    row[4] = df.format(Math.abs(restOfLoan));

	    schedule[period] = row;
	}

	return schedule;
    }

    private String getPeriodReadable(int periodLifetime) {
	switch (time) {
	case ANNUAL:
	    return (periodLifetime + 1) + "";
	case QUARTERLY:
	    return ((periodLifetime % 4) + 1) + "/" + ((periodLifetime / 4) + 1);
	case MONTHLY:
	    return Strings.padStart(((periodLifetime % 12) + 1) + "", 2, '0') + "/"
		    + ((periodLifetime / 12) + 1);
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
    }
}
