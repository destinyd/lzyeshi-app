package dd.android.yeshi_trader.core;


public class Settings {
	public boolean isNotifi = true
            ,isSoundNotifi = true
            ,isShockNotifi = true
            ,isLightNotifi = true
            ,isNotDisturb = false
            ,isOpen = false;

    public String authToken = null;

    public User user = null;

    public boolean isLogin(){
        if(authToken != null && authToken.length() > 0)
            return true;
        return false;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getName() {
        if(user != null)
            return user.getName();
        return null;
    }

    public User getUser() {
        return user;
    }

    public boolean isOpen() {
        return isOpen;
    }

    public void setOpen(boolean open) {
        isOpen = open;
    }

    public void setUser(User user) {
        this.user = user;
    }

    protected static Settings _factory = null;

	public static Settings getFactory() {
		if (_factory == null)
			_factory = new Settings();
		return _factory;
	}

	public static void setFactory(Settings s) {
		_factory = s;
	}

}
