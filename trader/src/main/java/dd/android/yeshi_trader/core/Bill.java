package dd.android.yeshi_trader.core;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class Bill implements Serializable {
    private static final long serialVersionUID = 210221891457L;

    public String _id;
    String plus= "", user_id, trader_id, commodity_id = null;
    Float price, total;
    Integer quantity;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }

    public String getPlus() {
        return plus;
    }

    public void setPlus(String plus) {
        this.plus = plus;
    }

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }

    public String getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(String trader_id) {
        this.trader_id = trader_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
