package dd.android.yeshi.core;

import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: dd
 * Date: 13-3-20
 * Time: 上午10:18
 * To change this template use File | Settings | File Templates.
 */
public class FayeNotificationChatMessage implements Serializable {
    private static final long serialVersionUID = 2102057195843431457L;

    String chat_message_id, name;
    int count;

    public String getChat_message_id() {
        return chat_message_id;
    }

    public void setChat_message_id(String chat_message_id) {
        this.chat_message_id = chat_message_id;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
