package dd.android.yeshi_trader.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class BusinessHour implements Serializable {
    private static final long serialVersionUID = 12057195843891457L;

    public String _id;
    Date created_at, opened_at, closed_at;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Date getClosed_at() {
        return closed_at;
    }

    public void setClosed_at(Date closed_at) {
        this.closed_at = closed_at;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getOpened_at() {
        return opened_at;
    }

    public void setOpened_at(Date opened_at) {
        this.opened_at = opened_at;
    }
}
