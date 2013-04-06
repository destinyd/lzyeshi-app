package dd.android.yeshi.ui;

import android.text.TextUtils;
import com.github.kevinsawicki.wishlist.SingleTypeAdapter;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Commodity;
import android.view.LayoutInflater;
import dd.android.yeshi.core.Picture;
import dd.android.yeshi.core.PictureImageLoader;

import java.util.List;

public class AdapterCommodities extends AdapterAlternatingColorList<Commodity> {
    private final PictureImageLoader avatars;

    public AdapterCommodities(LayoutInflater inflater, List<Commodity> items, PictureImageLoader avatars) {
        super(R.layout.item_commodity,inflater,items);
        this.avatars = avatars;
    }

    /**
     * @param inflater
     * @param items
     */
    public AdapterCommodities(LayoutInflater inflater, List<Commodity> items) {
        this(inflater, items, null);

    }
    @Override
    public long getItemId(final int position) {
        final String id = getItem(position)._id;
        return !TextUtils.isEmpty(id) ? id.hashCode() : super
                .getItemId(position);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.iv_image, R.id.tv_name, R.id.tv_reserve, R.id.tv_price };
    }

    @Override
    protected void update(int position, Commodity commodity) {
        super.update(position, commodity);

        avatars.bind(imageView(0), commodity.getPicture(),"android");

        setText(1, commodity.getName());
        setText(2, String.valueOf(commodity.getReserve()));
        setText(3, commodity.getHumanize_price());

    }
}