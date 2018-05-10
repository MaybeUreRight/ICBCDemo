package xinshiyeweixin.cn.icbcdemo.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.TagBean;
import xinshiyeweixin.cn.icbcdemo.bean.UpdateBean;
import xinshiyeweixin.cn.icbcdemo.local.ConstantValue;
import xinshiyeweixin.cn.icbcdemo.local.StatusCode;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.LogUtils;

public class HttpManager {
    /**
     * 分类列表
     *
     * @param sn 机器SN码(test1234567890)
     *           <p>
     *           [{"id":8,"name":"分类1"},{"id":9,"name":"分类2"}]
     *           </p>
     * @description: 获取商品分类
     */
    public static void category(String sn) {
        final List<CategoryBean> categoryBeanList = new ArrayList<>();
        OkGo.<String>post(ConstantValue.CATEGORY)
                .tag("MainActivity")
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        int code = response.code();
                        LogUtils.i("code = " + code);
                        logErrorCode(code);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        ArrayList<String> strList = GsonUtils.convertJson2Array(body);
                        for (String str : strList) {
                            CategoryBean bean = GsonUtils.convertString2Object(str, CategoryBean.class);
                            categoryBeanList.add(bean);
                        }
                        LogUtils.i("categoryBeanList.size() = " + categoryBeanList.size());
                    }
                });

    }


    /**
     * 更新机器状态
     *
     * @param sn 机器SN码(test1234567890)
     *
     * @description: 传入机器的SN码，服务器自动更新机器的最后在线时间（最后在线时间超过10分钟未更新，判定设备离线）
     * <p>
     * {"id":10000,"area_id":33,"sn":"test1234567890","remark":"测试第一台机器","created_at":1495777723,"updated_at":1525936563,"last_online_time":1525936563}
     * </p>
     */
    public static void update(String sn) {
        OkGo.<String>post(ConstantValue.UPDATE)
                .tag("MainActivity")
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        int code = response.code();
                        LogUtils.i("code = " + code);
                        logErrorCode(code);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i(body);

                        UpdateBean updateBean = GsonUtils.convertString2Object(body, UpdateBean.class);
                    }
                });
    }

    /**
     * 商品列表
     *
     * @param sn     机器的SN码
     * @param cat_id 分类id,传入大于0的分类id，系统将会根据分类id的值对商品进行筛选
     * @param tag_id 标签id,传入大于0的标签id，系统将会根据标签id的值对商品进行筛选
     *               <p>
     *               [
     *               {"id":9,"name":"蓝宝石戒指","cat_id":8,"cat_name":"分类1","unit":"枚","market_price":6000,"our_price":5000,"content":"自古以来蓝宝石就有“帝王之石”之称。蓝宝石让几乎每一个时代的皇室被其吸引，并将之视为保佑圣物和典藏珍品。传说蓝宝石可让佩戴者免于遭人妒忌，并可蒙受神灵垂爱，于是古代国王就在颈间配戴蓝宝石，作为避免受伤的强力防御物。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a736b3ec3.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59b8a7599ca58.avi","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3100557295c.jpg","sale_url":"http://mall.icbc.com.cn/"}
     *               ]
     *               </p>
     * @description: 传入机器的SN码，获取相应的商品列表
     */
    public static void goods(String sn, Integer cat_id, Integer tag_id) {
        OkGo.<String>post(ConstantValue.GOODS)
                .tag("MainActivity")
                .params("sn", sn)
                .params("cat_id", cat_id)
                .params("tag_id", tag_id)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        int code = response.code();
                        LogUtils.i("code = " + code);
                        logErrorCode(code);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i(body);
                        ArrayList<String> strings = GsonUtils.convertJson2Array(body);
                        List<GoodBean> goodBeanList = new ArrayList<>();
                        for (String str : strings) {
                            GoodBean goodBean = GsonUtils.convertString2Object(str, GoodBean.class);
                            goodBeanList.add(goodBean);
                        }

                        LogUtils.i("goodBeanList.size() = " + goodBeanList.size());

                    }
                });
    }

    /**
     * 标签列表
     *
     * @param sn 机器的SN码
     *           <p>
     *           [{"id":1,"name":"热卖商品"},{"id":2,"name":"人气飙升"}]
     *           <p>
     * @description: 获取商品标签
     */
    public static void tag(String sn) {
        OkGo.<String>post(ConstantValue.TAG)
                .tag("MainActivity")
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        int code = response.code();
                        LogUtils.i("code = " + code);
                        logErrorCode(code);
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i(body);
                        List<TagBean> tagBeanList = new ArrayList<>();
                        ArrayList<String> strings = GsonUtils.convertJson2Array(body);
                        for (String str : strings) {
                            TagBean tagBean = GsonUtils.convertString2Object(str, TagBean.class);
                            tagBeanList.add(tagBean);
                        }

                        LogUtils.i("tagBeanList.size() = " + tagBeanList.size());
                    }
                });
    }


    private static void logErrorCode(int code) {
        String str = "";
        switch (code) {
            case StatusCode.STATUS_400:
                str += "Bad request";
                break;
            case StatusCode.STATUS_401:
                str += "authorized";
                break;
            case StatusCode.STATUS_404:
                str += "设备不存在";
                break;
            case StatusCode.STATUS_405:
                str += "/Method not allowed";
                break;
            case StatusCode.STATUS_426:
                str += "Upgrade required";
                break;
            case StatusCode.STATUS_429:
                str += "Rate limit exceeded";
                break;
            case StatusCode.STATUS_499:
                str += "";
                break;
            case StatusCode.STATUS_500:
                str += "Customized business errors";
                break;
            default:
                str += "未知错误";
                break;

        }
        LogUtils.i(str);

    }
}
