package dd.android.yeshi_trader.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import com.baidu.mapapi.map.*;
import com.baidu.mapapi.search.MKPoiInfo;
import com.baidu.platform.comapi.basestruct.GeoPoint;
import com.github.kevinsawicki.wishlist.Toaster;
import dd.android.yeshi_trader.core.Location;
import dd.android.yeshi_trader.core.PropertiesController;
import dd.android.yeshi_trader.core.ServiceYS;
import dd.android.yeshi_trader.core.Settings;
import roboguice.util.RoboAsyncTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OverlayItemSelectLocation extends ItemizedOverlay<OverlayItem> {
    public List<OverlayItem> mGeoList = new ArrayList<OverlayItem>();
    private Context mContext = null;
//    static PopupOverlay pop = null;

//    Toast mToast = null;
    ProgressDialog pd = null;
    OverlayItem select = null;

    public OverlayItemSelectLocation(Drawable marker, Context context, MapView mapView) {
        super(marker, mapView);
        this.mContext = context;
//        pop = new PopupOverlay(mMapView, new PopupClickListener() {
//
//            @Override
//            public void onClickedPopup(int index) {
//                if (null == mToast)
//                    mToast = Toast.makeText(mContext, "popup item :" + index + " is clicked.", Toast.LENGTH_SHORT);
//                else mToast.setText("popup item :" + index + " is clicked.");
//                mToast.show();
//            }
//        });
        // 自2.1.1 开始，使用 add/remove 管理overlay , 无需调用以下接口.
        // populate();

    }

    protected boolean onTap(int index) {
//        System.out.println("item onTap: " + index);
//
//        Bitmap[] bmps = new Bitmap[3];
//        if (index % 2 == 0) {
//            try {
//                bmps[0] = BitmapFactory.decodeStream(mContext.getAssets().open("marker1.png"));
//                bmps[1] = BitmapFactory.decodeStream(mContext.getAssets().open("marker2.png"));
//                bmps[2] = BitmapFactory.decodeStream(mContext.getAssets().open("marker3.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        } else {
//            try {
//                bmps[2] = BitmapFactory.decodeStream(mContext.getAssets().open("marker1.png"));
//                bmps[1] = BitmapFactory.decodeStream(mContext.getAssets().open("marker2.png"));
//                bmps[0] = BitmapFactory.decodeStream(mContext.getAssets().open("marker3.png"));
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

//        pop.showPopup(bmps, getItem(index).getPoint(), 32);
//        if (null == mToast)
//            mToast = Toast.makeText(mContext, getItem(index).getTitle(), Toast.LENGTH_SHORT);
//        else mToast.setText(getItem(index).getTitle());
//        mToast.show();
        select  = getItem(index);

        new AlertDialog.Builder(mContext).setTitle("确定选择此地点吗？")
//                .setMessage("你确定要登出吗？")
                .setNegativeButton("取消", null).setPositiveButton("确定",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        postLocation();
                    }
                }).show();

        return true;
    }

    private void postLocation() {
        pd = ProgressDialog.show(mContext, "", "努力的发送中...", true, false);
        new RoboAsyncTask<Boolean>(mContext) {
            public Boolean call() throws Exception {
                GeoPoint gp = select.getPoint();
                Location location = new Location(gp.getLatitudeE6(),gp.getLongitudeE6());
                Location p_location = ServiceYS.postLocation(location);
                if (p_location != null) {
                    return true;
                }
                return false;
            }

            @Override
            protected void onException(Exception e) throws RuntimeException {
                e.printStackTrace();
                Toast.makeText(mContext, "地点提交失败。",Toast.LENGTH_SHORT).show();
                pd.dismiss();
//                setProgressBarVisibility(false);
            }

            @Override
            public void onSuccess(Boolean isSuccess) {
                if(isSuccess)
                {
                    FragmentDashboard.factroy.post_business_hours(true);
                    Settings.getFactory().setOpen(isSuccess);
                    PropertiesController.writeConfiguration();
                    ((Activity) mContext).finish();
                }
                else{

                    Toast.makeText(mContext, "地点提交失败。",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            protected void onFinally() throws RuntimeException {
                pd.dismiss();
            }
        }.execute();
    }

//    public boolean onTap(GeoPoint pt, MapView mapView) {
//        if (pop != null) {
//            pop.hidePop();
//        }
//        super.onTap(pt, mapView);
//        return false;
//    }

    // 自2.1.1 开始，使用 add/remove 管理overlay , 无需重写以下接口
    /*
	@Override
	protected OverlayItem createItem(int i) {
		return mGeoList.get(i);
	}

	@Override
	public int size() {
		return mGeoList.size();
	}
	*/
}
