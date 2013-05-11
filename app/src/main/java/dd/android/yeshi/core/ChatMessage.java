package dd.android.yeshi.core;

import java.io.Serializable;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class ChatMessage implements Serializable {
    private static final long serialVersionUID = 210205795843891457L;

    public String _id;
    String content, chatable_id, chatable_type, user_id, name;
    String commodity_id = null;
    Date created_at, read_at;

    public ChatMessage() {
    }

    public ChatMessage(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public ChatMessage(String name, String content, String commodity_id) {
        this.name = name;
        this.content = content;
        this.commodity_id = commodity_id;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getChatable_id() {
        return chatable_id;
    }

    public void setChatable_id(String chatable_id) {
        this.chatable_id = chatable_id;
    }

    public String getChatable_type() {
        return chatable_type;
    }

    public void setChatable_type(String chatable_type) {
        this.chatable_type = chatable_type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Date created_at) {
        this.created_at = created_at;
    }

    public Date getRead_at() {
        return read_at;
    }

    public void setRead_at(Date read_at) {
        this.read_at = read_at;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCommodity_id() {
        return commodity_id;
    }

    public void setCommodity_id(String commodity_id) {
        this.commodity_id = commodity_id;
    }
}
