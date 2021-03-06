package xinshiyeweixin.cn.icbcdemo.local;

import android.os.Environment;

public class ConstantValue {

    public static final String DATABASE_CATEGORY = "category.db";
    public static final String DATABASE_GOOD = "good.db";
    public static final String DATABASE_BANNER = "banner.db";

    public static final String BASE_URL = "http://3d.leygoo.cn/api.php?r=v1";
    public static final String APP = BASE_URL + "/app";
    public static final String BANNER = BASE_URL + "/banner";
    public static final String CATEGORY = BASE_URL + "/category";
    public static final String GOODS = BASE_URL + "/goods";
    public static final String TAG = BASE_URL + "/tag";
    public static final String UPDATE = BASE_URL + "/equipment/update";
    public static final String DOWNLOAD_APK_URL = "http://3d.leygoo.cn/apk/app-release.apk";

    public static final String TAG_APP = "TAG_APP";
    public static final String TAG_BANNER = "TAG_BANNER";
    public static final String TAG_CATEGORY = "TAG_CATEGORY";
    public static final String TAG_GOODS = "TAG_GOODS";
    public static final String TAG_TAG = "TAG_TAG";
    public static final String TAG_UPDATE = "TAG_UPDATE";
    public static final String TAG_DOWNLOAD_APK = "TAG_DOWNLOAD_APK";
//    public static final String CURRENT_VIDEO_PATH = "CURRENT_VIDEO_PATH";

    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().getPath() + "/ICBC/";
    public static final String NEW_APK = ROOT_PATH + "app/";
    public static final String GOOD_VIDEO_PATH = ROOT_PATH + "good/video/";
    public static final String GOOD_IMAGE_PATH = ROOT_PATH + "good/image/";
    public static final String BANNER_IMAGE_PATH = ROOT_PATH + "banner/image/";

    //    public static final int TURN2GOODDETAIL = 105;
    public static final int REQUEST_RUN_PERMISSION = 111;

    public static final int DOWNLOAD_VIDEO_DELAY = 60 * 1000;
    public static final int DOWNLOAD_BANNER_IMAGE_DELAY = 30 * 1000;
    public static final int DOWNLOAD_NEW_APK = 60 * 1000;
    public static final int UPDATE_DELAY = 15 * 1000;
}
