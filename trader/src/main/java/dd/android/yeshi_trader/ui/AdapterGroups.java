package dd.android.yeshi_trader.ui;

import android.text.TextUtils;
import android.view.LayoutInflater;
import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Group;
import dd.android.yeshi_trader.core.PictureImageLoader;

import java.util.List;

public class AdapterGroups extends AdapterAlternatingColorList<Group> {

    public AdapterGroups(LayoutInflater inflater, List<Group> items) {
        super(R.layout.item_group,inflater,items);
    }

    @Override
    public long getItemId(final int position) {
        final String id = getItem(position)._id;
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_name, R.id.tv_count };
    }

    @Override
    protected void update(int position, Group group) {
        super.update(position, group);

        setText(0, group.getName());
        setText(1, String.valueOf(group.getCommodities_count()));

    }
}