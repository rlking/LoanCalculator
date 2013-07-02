package eu.kingcastle.loancalculator.model;

public enum RepaymentPeriod {
	ANNUAL(1), QUARTERLY(4), MONTHLY(12);

	private int period;

	RepaymentPeriod(int period) {
		this.period = period;
	}

	public int getPeriod() {
		return period;
	}
}
