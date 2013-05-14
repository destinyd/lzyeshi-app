package dd.android.yeshi.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.actionbarsherlock.view.MenuItem;
import dd.android.yeshi.R;
import dd.android.yeshi.core.Commodity;
import dd.android.yeshi.core.Trader;
import roboguice.inject.InjectView;

import static dd.android.yeshi.core.Constants.Extra.COMMODITY;
import static dd.android.yeshi.core.Constants.Extra.TRADER;


public class ActivityContact extends ActivityYS {

    private LinearLayout.LayoutParams LP_FF = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT);
    private LinearLayout.LayoutParams LP_FW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    private LinearLayout.LayoutParams LP_WW = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

    private RelativeLayout.LayoutParams RLP_FF = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.FILL_PARENT);
    private RelativeLayout.LayoutParams RLP_FW = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    private RelativeLayout.LayoutParams RLP_WW = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);

    Trader trader = null;

    @InjectView(R.id.layout_base)
    LinearLayout layout_base;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.act_contact);

        get_extra();
        init_view();
    }

    private void init_view() {
        LinearLayout ll = new LinearLayout(this);
        ll.setLayoutParams(LP_FW);
        ll.setOrientation(LinearLayout.VERTICAL);
        RelativeLayout rl = new RelativeLayout(this);
        rl.setLayoutParams(RLP_FW);
        TextView tv_label = new TextView(this);
        tv_label.setText("label");
        TextView tv_value = new TextView(this);
        tv_value.setText("value");
        rl.addView(tv_label);
        rl.addView(tv_value);
        ll.addView(rl);
        layout_base.addView(ll);


    }

    public void handleLocation(View view){
        ActivityLauncher.getFactory().changeTab(2);
    }


    private void get_extra() {
        Intent intent = getIntent();
        trader = (Trader) intent.getSerializableExtra(TRADER);
    }
}
