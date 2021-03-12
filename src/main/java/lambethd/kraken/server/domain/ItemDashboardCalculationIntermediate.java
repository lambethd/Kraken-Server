package lambethd.kraken.server.domain;

import runescape.Item;

public class ItemDashboardCalculationIntermediate extends Item {
    private float dailyPercentageChange;
    private long dailyValueChange;

    private float monthlyPercentageChange;
    private long monthlyValueChange;

    public float getDailyPercentageChange() {
        return dailyPercentageChange;
    }

    public void setDailyPercentageChange(float dailyPercentageChange) {
        this.dailyPercentageChange = dailyPercentageChange;
    }

    public long getDailyValueChange() {
        return dailyValueChange;
    }

    public void setDailyValueChange(long dailyValueChange) {
        this.dailyValueChange = dailyValueChange;
    }

    public float getMonthlyPercentageChange() {
        return monthlyPercentageChange;
    }

    public void setMonthlyPercentageChange(float monthlyPercentageChange) {
        this.monthlyPercentageChange = monthlyPercentageChange;
    }

    public long getMonthlyValueChange() {
        return monthlyValueChange;
    }

    public void setMonthlyValueChange(long monthlyValueChange) {
        this.monthlyValueChange = monthlyValueChange;
    }
}
