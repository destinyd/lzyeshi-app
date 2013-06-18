package dd.android.yeshi_trader.core;

import java.io.Serializable;

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
    String name, category_list, humanize_price;
    int commodities_count, reserve;

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

    public String getCategory_list() {
        return category_list;
    }

    public void setCategory_list(String category_list) {
        this.category_list = category_list;
    }

    public int getCommodities_count() {
        return commodities_count;
    }

    public void setCommodities_count(int commodities_count) {
        this.commodities_count = commodities_count;
    }

    public String getHumanize_price() {
        return humanize_price;
    }

    public void setHumanize_price(String humanize_price) {
        this.humanize_price = humanize_price;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }
}
