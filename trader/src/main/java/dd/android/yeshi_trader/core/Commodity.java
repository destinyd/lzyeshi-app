package dd.android.yeshi_trader.core;

import java.io.File;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class Commodity implements Serializable {
    private static final long serialVersionUID = 2102057155433891457L;

    public String _id, group_id, trader_id, humanize_price;
    String name,desc, category_list;
    int reserve;
    Picture picture;
    File filePicture = null;
    String strPicture = null;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getHumanize_price() {
        return humanize_price;
    }

    public void setHumanize_price(String humanize_price) {
        this.humanize_price = humanize_price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Picture getPicture() {
        return picture;
    }

    public void setPicture(Picture picture) {
        this.picture = picture;
    }

    public int getReserve() {
        return reserve;
    }

    public void setReserve(int reserve) {
        this.reserve = reserve;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTrader_id() {
        return trader_id;
    }

    public void setTrader_id(String trader_id) {
        this.trader_id = trader_id;
    }

    public String getCategory_list() {
        return category_list;
    }

    public void setCategory_list(String category_list) {
        this.category_list = category_list;
    }

    public File getFilePicture() {
        return filePicture;
    }

    public void setFilePicture(File filePicture) {
        this.filePicture = filePicture;
    }

    public String getStrPicture() {
        return strPicture;
    }

    public void setStrPicture(String strPicture) {
        this.strPicture = strPicture;
    }
}
