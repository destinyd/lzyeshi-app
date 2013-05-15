
package dd.android.yeshi.ui;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import com.costum.android.widget.LoadMoreListView;
import com.github.kevinsawicki.wishlist.Toaster;
import com.google.inject.Inject;
import dd.android.yeshi.R;
import dd.android.yeshi.core.ChatMessage;
import dd.android.yeshi.core.PictureImageLoader;
import dd.android.yeshi.core.ServiceYS;
import roboguice.inject.InjectView;
import roboguice.util.RoboAsyncTask;

import java.util.ArrayList;
import java.util.List;

import static dd.android.yeshi.core.Constants.Extra.*;
import static dd.android.yeshi.core.Constants.Other.*;

/**
 * Activity to authenticate the ABUser against an API (example API on Parse.com)
 */
public class ActivityChatMessages extends
        ActivityUserYS {

    List<ChatMessage> chat_messages = new ArrayList<ChatMessage>();

    @InjectView(R.id.lv_list)
    private LoadMoreListView lv_list;
    @Inject
    private PictureImageLoader avatars;
    AdapterChatMessage adapter = null;
    int page = 1,pass_page = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);    //To change body of overridden methods use File | Settings | File Templates.
        setContentView(R.layout.act_chat_messages);

        lv_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                onListItemClick((LoadMoreListView) parent, view, position, id);
            }
        });
        lv_list.setOnLoadMoreListener(
                new LoadMoreListView.OnLoadMoreListener() {
                    @Override
                    public void onLoadMore() {
                        get_next_page();
                    }
                }
        );

        if (!isLogin()) {
            start_login();
        }
        hide_notification();
    }

    private void hide_notification() {
        NotificationManager messageNotificatioManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        messageNotificatioManager.cancel(R.string.app_name);
    }

    private void init_chat_messages() {
        if (chat_messages == null)
            chat_messages = new ArrayList<ChatMessage>();
    }

    private void get_next_page() {
        page++;
        get_chat_messages();
    }

    private void get_chat_messages() {
        progressDialogShow(this);
        new RoboAsyncTask<Boolean>(this) {
            public Boolean call() throws Exception {
                List<ChatMessage> getChatMessages = ServiceYS.GotChatMessages(page);
                if (page > 1 && getChatMessages != null && getChatMessages.size() == 0) {
                    page--;
                    lv_list.setOnLoadMoreListener(null);
                    Toaster.showLong(ActivityChatMessages.this, "没有数据了。");
                } else
                    addCommodities(getChatMessages);
                return true;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toaster.showLong(ActivityChatMessages.this, "获取商品信息失败");
                progressDialogDismiss();
            }

            @Override
            public void onSuccess(Boolean relationship) {
                commodities_to_list();
            }

            @Override
            protected void onFinally() throws RuntimeException {
                progressDialogDismiss();
                lv_list.onLoadMoreComplete();
            }
        }.execute();
    }


    private void addCommodities(List<ChatMessage> getChatMessages) {
        init_chat_messages();
        chat_messages.addAll(getChatMessages);
    }

    public void onListItemClick(LoadMoreListView l, View v, int position, long id) {
        ChatMessage chatMessage = ((ChatMessage) l.getItemAtPosition(position));
        startActivity(new Intent(this, ActivityChatMessage.class).putExtra(CHAT_MESSAGE,chatMessage));
    }

    private void commodities_to_list() {
        if(adapter == null)
        {
            adapter = new AdapterChatMessage(
                    getLayoutInflater(), chat_messages);
            lv_list.setAdapter(adapter);
        }
        else
        {
            adapter.setItems(chat_messages);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();    //To change body of overridden methods use File | Settings | File Templates.
        if (isLogin() && (chat_messages == null || chat_messages.size() == 0)) {
            get_chat_messages();
        }
    }
}
