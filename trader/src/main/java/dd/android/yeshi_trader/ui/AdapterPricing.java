package dd.android.yeshi_trader.ui;

import dd.android.yeshi_trader.R;
import dd.android.yeshi_trader.core.Price;
import android.view.LayoutInflater;

import java.util.List;

public class AdapterPricing extends AdapterAlternatingColorList<Price> {

    /**
     * @param inflater
     * @param items
     * @param selectable
     */
    public AdapterPricing(LayoutInflater inflater, List<Price> items,
                          boolean selectable) {
        super(R.layout.item_pricing, inflater, items, selectable);
    }

    /**
     * @param inflater
     * @param items
     */
    public AdapterPricing(LayoutInflater inflater, List<Price> items) {
        super(R.layout.item_pricing, inflater, items);
    }

    @Override
    protected int[] getChildViewIds() {
        return new int[] { R.id.tv_name, R.id.tv_price};
    }

    @Override
    protected void update(int position, Price item) {
        super.update(position, item);

        setText(0, item.getName());
        setText(1, item.getPriceStr());
    }
}