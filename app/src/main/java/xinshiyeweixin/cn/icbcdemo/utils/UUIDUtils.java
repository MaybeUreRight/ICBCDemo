package xinshiyeweixin.cn.icbcdemo.utils;

import android.text.TextUtils;

import java.util.UUID;

/**
 * @author: liubo
 * @date: 2018/5/11/011
 * @description: $description$
 */
public class UUIDUtils {
    public static String getMyUUID() {
        String uniqueId = SPUtils.getInstance().getString("UUID");
        if (TextUtils.isEmpty(uniqueId)) {
            uniqueId = UUID.randomUUID().toString();
            SPUtils.getInstance().put("UUID", uniqueId);
        }
        LogUtils.i("uuid=" + uniqueId);
        return uniqueId;
    }
}
