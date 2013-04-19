package dd.android.yeshi.core;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class Group implements Serializable {
    private static final long serialVersionUID = 2102057195843891457L;

    public String _id;
    String name;
    int commodities_count;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCommodities_count() {
        return commodities_count;
    }

    public void setCommodities_count(int commodities_count) {
        this.commodities_count = commodities_count;
    }
}
