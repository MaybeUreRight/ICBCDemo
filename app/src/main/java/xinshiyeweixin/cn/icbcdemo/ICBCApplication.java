package xinshiyeweixin.cn.icbcdemo;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import xinshiyeweixin.cn.icbcdemo.bean.DaoMaster;
import xinshiyeweixin.cn.icbcdemo.bean.DaoSession;
import xinshiyeweixin.cn.icbcdemo.local.ConstantValue;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;

public class ICBCApplication extends Application {
    public static ICBCApplication application;

    private MediaRouter mediaRouter = null;
    private MyPresentation myPresentation = null;

    private MediaRouter.SimpleCallback simpleCallback = new MediaRouter.SimpleCallback() {
        @Override
        public void onRouteSelected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            super.onRouteSelected(router, type, info);
            ICBCApplication.this.UpdatePresent();
        }

        @Override
        public void onRouteUnselected(MediaRouter router, int type, MediaRouter.RouteInfo info) {
            super.onRouteUnselected(router, type, info);
            ICBCApplication.this.UpdatePresent();
        }

        @Override
        public void onRouteChanged(MediaRouter router, MediaRouter.RouteInfo info) {
            super.onRouteChanged(router, info);
            ICBCApplication.this.UpdatePresent();
        }
    };

    public DaoSession productInfoDaoSession;
    public DaoSession productDaoSession;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        this.mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
        this.mediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, simpleCallback);
//        UpdatePresent();

        productDaoSession = createDaoSession(ConstantValue.DATABASE_PRODUCT);
        productInfoDaoSession = createDaoSession(ConstantValue.DATABASE_PRODUCTINFO);
    }

    private void UpdatePresent() {
        DisplayManager mDisplayManager;// 屏幕管理类
        mDisplayManager = (DisplayManager) this.getSystemService(Context.DISPLAY_SERVICE);
        Display[] displays = mDisplayManager.getDisplays();

        if (myPresentation != null && myPresentation.getDisplay() != displays[displays.length - 1]) {
            myPresentation.dismiss();
            myPresentation = null;
        }

        // Show a new presentation if the previous one has been dismissed and a route has been selected.
        Log.i("lt", "获取屏幕是否为空：" + (displays[displays.length - 1] != null));
        if (myPresentation == null && displays[displays.length - 1] != null) {
            // Initialize a new Presentation for the Display
            myPresentation = new MyPresentation(this, displays[displays.length - 1]);
            myPresentation.setOnDismissListener(
                    new DialogInterface.OnDismissListener() {
                        // Listen for presentation dismissal and then remove it
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            if (dialog == myPresentation) {
                                myPresentation = null;
                            }
                        }
                    });

            // Try to show the presentation, this might fail if the display has
            // gone away in the meantime
            try {
                this.myPresentation.getWindow().getAttributes().type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
                myPresentation.show();
            } catch (WindowManager.InvalidDisplayException ex) {
                // Couldn't show presentation - display was already removed
                myPresentation = null;
            }
        }
    }

    public MyPresentation getPresentation() {
        return this.myPresentation;
    }

    /**
     * 创建DaoSession实例
     *
     * @param databaseName 数据库名称
     * @return DaoSession实例
     * @since 2018年4月29日
     */
    private DaoSession createDaoSession(String databaseName) {
        DaoMaster.OpenHelper openHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), databaseName, null);
        DaoMaster daoMaster = new DaoMaster(openHelper.getWritableDatabase());
        return daoMaster.newSession();
    }
}
