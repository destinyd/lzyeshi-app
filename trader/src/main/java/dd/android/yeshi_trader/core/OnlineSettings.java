package dd.android.yeshi_trader.core;


public class OnlineSettings {
	public boolean isOpen = false;
    float
//            income_yesterday= 0,
            income_today = 0, income_month = 0;


    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public float getIncome_month() {
        return income_month;
    }

    public void setIncome_month(float income_month) {
        this.income_month = income_month;
    }

    public float getIncome_today() {
        return income_today;
    }

    public void setIncome_today(float income_today) {
        this.income_today = income_today;
    }

//    public float getIncome_yesterday() {
//        return income_yesterday;
//    }
//
//    public void setIncome_yesterday(float income_yesterday) {
//        this.income_yesterday = income_yesterday;
//    }

    protected static OnlineSettings _factory = null;

	public static OnlineSettings getFactory() {
		if (_factory == null)
			_factory = new OnlineSettings();
		return _factory;
	}

	public static void setFactory(OnlineSettings s) {
		_factory = s;
	}

}
