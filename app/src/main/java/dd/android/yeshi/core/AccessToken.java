package dd.android.yeshi.core;

import java.sql.Time;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-1-9
 * Time: 下午8:52
 * To change this template use File | Settings | File Templates.
 */
public class AccessToken {


    public String access_token;
    public String token_type;
    public String refresh_token;
    public String scope;
    public Time expires_in;


    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Time getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Time expires_in) {
        this.expires_in = expires_in;
    }
}
