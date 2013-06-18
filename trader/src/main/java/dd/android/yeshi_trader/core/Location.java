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
public class Location implements Serializable {

    public String _id;
    Double lat,lng;
    Date created_at;

    public Location() {
    }

    public Location(Double lat, Double lng) {
        this.lat = lat;
        this.lng = lng;
    }

    public Location(int lat, int lng) {
        this.lat = lat / 1E6;
        this.lng = lng / 1E6;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }
}
