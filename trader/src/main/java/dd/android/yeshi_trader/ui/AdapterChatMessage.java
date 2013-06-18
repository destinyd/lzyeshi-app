package dd.android.yeshi_trader.ui;

import android.view.LayoutInflater;
import dd.android.common.PrettyDateFormat;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.ChatMessage;

import java.util.List;

public class AdapterChatMessage extends AdapterAlternatingColorList<ChatMessage> {

    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public AdapterChatMessage(LayoutInflater inflater, List<ChatMessage> items,
                              boolean selectable) {
        super(R.layout.item_chat_message, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public AdapterChatMessage(LayoutInflater inflater, List<ChatMessage> items) {
        super(R.layout.item_chat_message, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_name, R.id.tv_content, R.id.tv_created_at};
    }

    @Override
    protected void update(int position, ChatMessage item) {
        super.update(position, item);

        setText(0, item.getUser_name());
        setText(1, item.getContent());
        setText(2, PrettyDateFormat.defaultFormat(item.getCreated_at()));
    }
}