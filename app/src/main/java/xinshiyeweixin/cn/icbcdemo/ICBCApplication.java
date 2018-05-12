package xinshiyeweixin.cn.icbcdemo;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.hardware.display.DisplayManager;
import android.media.MediaRouter;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.DBCookieStore;
import com.lzy.okgo.cookie.store.MemoryCookieStore;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

import org.greenrobot.greendao.database.Database;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;
import xinshiyeweixin.cn.icbcdemo.bean.DaoMaster;
import xinshiyeweixin.cn.icbcdemo.bean.DaoSession;
import xinshiyeweixin.cn.icbcdemo.local.ConstantValue;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;
import xinshiyeweixin.cn.icbcdemo.utils.SPUtils;
import xinshiyeweixin.cn.icbcdemo.utils.UUIDUtils;
import xinshiyeweixin.cn.icbcdemo.utils.Utils;

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

    public DaoSession categoryDaoSession;
    public DaoSession goodDaoSession;


    public String uuid;


    @Override
    public void onCreate() {
        super.onCreate();
        application = this;
        Utils.init(application);
        initOkGo();
        uuid = "test1234567890";
        SPUtils.getInstance().put("UUID", uuid);
//        uuid = UUIDUtils.getMyUUID();

        this.mediaRouter = (MediaRouter) getSystemService(Context.MEDIA_ROUTER_SERVICE);
        this.mediaRouter.addCallback(MediaRouter.ROUTE_TYPE_LIVE_VIDEO, simpleCallback);
//        UpdatePresent();

        categoryDaoSession = createDaoSession(ConstantValue.DATABASE_CATEGORY);
        goodDaoSession = createDaoSession(ConstantValue.DATABASE_GOOD);
    }

    private void initOkGo() {
        //1.构建
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        //2.配置LOG
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor("OkGo");
        //log打印级别，决定了log显示的详细程度
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        //3.配置超时时间
        //全局的读取超时时间
        builder.readTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(OkGo.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //4. 配置Cookie，以下几种任选其一就行
        //使用sp保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new SPCookieStore(this)));
        //使用数据库保持cookie，如果cookie不过期，则一直有效
//        builder.cookieJar(new CookieJarImpl(new DBCookieStore(this)));
        //使用内存保持cookie，app退出后，cookie消失
        builder.cookieJar(new CookieJarImpl(new MemoryCookieStore()));

        //6.Https配置，以下几种方案根据需要自己设置
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        //配置https的域名匹配规则，详细看demo的初始化介绍，不需要就不要加入，使用不当会导致https握手失败
//        builder.hostnameVerifier(new SafeHostnameVerifier());
        OkGo.getInstance().init(this);


        //7.配置OkGo
        //---------这里给出的是示例代码,告诉你可以这么传,实际使用的时候,根据需要传,不需要就不传-------------//
        //        HttpHeaders headers = new HttpHeaders();
        //        headers.put("commonHeaderKey1", "commonHeaderValue1");    //header不支持中文，不允许有特殊字符
        //        headers.put("commonHeaderKey2", "commonHeaderValue2");
        //        HttpParams params = new HttpParams();
        //        params.put("commonParamsKey1", "commonParamsValue1");     //param支持中文,直接传,不要自己编码
        //        params.put("commonParamsKey2", "这里支持中文参数");
        //-------------------------------------------------------------------------------------//

        OkGo.getInstance().init(this)                       //必须调用初始化
                .setOkHttpClient(builder.build())               //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)               //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)   //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3);                         //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                      //全局公共头
//                .addCommonParams(params);                       //全局公共参数
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
        SQLiteDatabase database = openHelper.getWritableDatabase();
//        org.greenrobot.greendao.database.Database database = openHelper.getEncryptedReadableDb("123");
        DaoMaster daoMaster = new DaoMaster(database);
        return daoMaster.newSession();
    }
}
