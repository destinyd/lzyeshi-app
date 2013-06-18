package dd.android.yeshi_trader.core;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class Accounts implements Serializable {
    private static final long serialVersionUID = 2321891457L;

    Float income_yesterday,
            income_today,
            income_month,
            income_year
    ;

    public Float getIncome_month() {
        return income_month;
    }

    public void setIncome_month(Float income_month) {
        this.income_month = income_month;
    }

    public Float getIncome_today() {
        return income_today;
    }

    public void setIncome_today(Float income_today) {
        this.income_today = income_today;
    }

    public Float getIncome_year() {
        return income_year;
    }

    public void setIncome_year(Float income_year) {
        this.income_year = income_year;
    }

    public Float getIncome_yesterday() {
        return income_yesterday;
    }

    public void setIncome_yesterday(Float income_yesterday) {
        this.income_yesterday = income_yesterday;
    }
}
