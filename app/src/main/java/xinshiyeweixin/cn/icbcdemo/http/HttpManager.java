package xinshiyeweixin.cn.icbcdemo.http;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.FileCallback;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.PostRequest;
import com.lzy.okgo.request.base.Request;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import xinshiyeweixin.cn.icbcdemo.bean.AppBean;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBean;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.bean.FailBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.TagBean;
import xinshiyeweixin.cn.icbcdemo.bean.UpdateBean;
import xinshiyeweixin.cn.icbcdemo.local.ConstantValue;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.LogUtils;

public class HttpManager {
    /**
     * 分类列表
     *
     * @param sn          机器SN码(test1234567890)
     *                    <p>
     *                    [{"id":8,"name":"分类1"},{"id":9,"name":"分类2"}]
     *                    </p>
     * @param reqCallBack
     * @description: 获取商品分类
     */
    public static void category(String sn, final ReqCallBack<ArrayList<CategoryBean>> reqCallBack) {
        final ArrayList<CategoryBean> categoryBeanList = new ArrayList<>();
        OkGo.<String>post(ConstantValue.CATEGORY)
                .tag(ConstantValue.TAG_CATEGORY)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
//                            LogUtils.i("string = " + body);
                            FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                            if (reqCallBack != null) {
                                reqCallBack.onReqFailed(failBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body().replace("\"id\"", "\"cat_id\"");
                        List<String> strList = GsonUtils.convertJson2Array(body);
                        for (String str : strList) {
                            CategoryBean bean = GsonUtils.convertString2Object(str, CategoryBean.class);
                            categoryBeanList.add(bean);
                        }
//                        LogUtils.i("categoryBeanList.size() = " + categoryBeanList.size());
                        reqCallBack.onReqSuccess(categoryBeanList);
                    }
                });

    }

    /**
     * 获取广告图
     *
     * @param sn          机器码
     * @param reqCallBack 回调
     *                    <p>
     *                    <p>
     *                    [
     *                    {"id":1,"title":"第一个公告","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2018-05/goods_5b03938c02b92.jpg"}
     *                    ,{"id":2,"title":"第二个公告","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2018-05/goods_5b03939a1a386.jpg"}
     *                    ]
     */
    public static void banner(String sn, final ReqCallBack<ArrayList<BannerBean>> reqCallBack) {
        final ArrayList<BannerBean> bannerBeanList = new ArrayList<>();
        OkGo.<String>post(ConstantValue.BANNER)
                .tag(ConstantValue.TAG_BANNER)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
                            FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                            if (reqCallBack != null) {
                                reqCallBack.onReqFailed(failBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body().replace("\"id\"", "\"banner_id\"");
                        LogUtils.i("banner = \r\n" + body);
                        List<String> strList = GsonUtils.convertJson2Array(body);
                        for (String str : strList) {
                            BannerBean bean = GsonUtils.convertString2Object(str, BannerBean.class);
                            bannerBeanList.add(bean);
                        }
                        reqCallBack.onReqSuccess(bannerBeanList);
                    }
                });

    }


    public static void app(String sn, final ReqCallBack<AppBean> reqCallBack) {
        OkGo.<String>post(ConstantValue.APP)
                .tag(ConstantValue.TAG_APP)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
                            FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                            if (reqCallBack != null) {
                                reqCallBack.onReqFailed(failBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
                        LogUtils.i("app = \r\n" + body);
                        AppBean appBean = GsonUtils.convertString2Object(body, AppBean.class);
                        reqCallBack.onReqSuccess(appBean);
                    }
                });

    }


    /**
     * 更新机器状态
     *
     * @param sn 机器SN码(test1234567890)
     * @description: 传入机器的SN码，服务器自动更新机器的最后在线时间（最后在线时间超过10分钟未更新，判定设备离线）
     * <p>
     * {"id":10000,"area_id":33,"sn":"test1234567890","remark":"测试第一台机器","created_at":1495777723,"updated_at":1525936563,"last_online_time":1525936563}
     * </p>
     */
    public static void update(String sn, final ReqCallBack<UpdateBean> reqCallBack) {
        OkGo.<String>post(ConstantValue.UPDATE)
                .tag(ConstantValue.TAG_UPDATE)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            ResponseBody responseBody = response.getRawResponse().body();
                            if (responseBody == null) {
                                return;
                            } else {
                                String body = responseBody.string();
//                            LogUtils.i("string = " + body);
                                FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                                if (reqCallBack != null) {
                                    reqCallBack.onReqFailed(failBean);
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
//                        LogUtils.i(body);

                        UpdateBean updateBean = GsonUtils.convertString2Object(body, UpdateBean.class);
                        if (reqCallBack != null) {
                            reqCallBack.onReqSuccess(updateBean);
                        }
                    }
                });
    }

    /**
     * 商品列表
     *
     * @param sn              机器的SN码
     * @param cat_id          分类id,传入大于0的分类id，系统将会根据分类id的值对商品进行筛选
     * @param tag_id          标签id,传入大于0的标签id，系统将会根据标签id的值对商品进行筛选
     *                        <p>
     *                        [
     *                        {"id":11
     *                        ,"name":"金戒指"
     *                        ,"name_en":"jinjiezhi"
     *                        ,"origin":"中国"
     *                        ,"cat_id":9
     *                        ,"cat_name":"分类2"
     *                        ,"unit":"枚"
     *                        ,"market_price":5000
     *                        ,"our_price":4000
     *                        ,"content":"金戒指从古代到今朝是受到大众的欢迎的饰品。坚韧珍贵的金戒指纯洁华美，是恒久真情的绝佳载体，仅以这一对纯净的指环，把两个恋人连在一起，从今以后两个人的世界成为一种永恒！是爱情坚贞的一种信物！忠贞爱情的完美见证！"
     *                        ,"image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a81f77669.png"
     *                        ,"video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd645d5405.mp4"
     *                        ,"ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a310117d0ace.jpg"
     *                        ,"sale_url":"http://mall.icbc.com.cn/"
     *                        }
     *                        <p>
     *                        ,{"id":12,"name":"大钻戒","name_en":"big-zuanjie","origin":"欧洲","cat_id":9,"cat_name":"分类2","unit":"枚","market_price":10000,"our_price":8000,"content":"“南非之星”的英文名称为Star of South Africa，重47.55carats，无色，梨形琢刻形状，原产于南非，是一颗极优质的净水钻，原钻石重83.5carats。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a8700e62a.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd6aabb7e8.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3101265d2db.jpg","sale_url":"http://mall.icbc.com.cn/"}]
     *                        </p>
     *                        </p>
     * @param goodReqCallBack
     * @description: 传入机器的SN码，获取相应的商品列表
     */
    public static void goods(String sn, Integer cat_id, Integer tag_id, final ReqCallBack<List<GoodBean>> goodReqCallBack) {
        PostRequest<String> postRequest = OkGo.<String>post(ConstantValue.GOODS).tag(ConstantValue.TAG_GOODS);
        postRequest.params("sn", sn);
        if (cat_id != null) {
            postRequest.params("cat_id", cat_id);
        }
        if (tag_id != null) {
            postRequest.params("tag_id", tag_id);
        }
        postRequest.execute(new StringCallback() {
            @Override
            public void onError(Response<String> response) {
                super.onError(response);

                try {
                    String body = response.getRawResponse().body().string();
//                    LogUtils.i("string = " + body);
                    FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                    if (goodReqCallBack != null) {
                        goodReqCallBack.onReqFailed(failBean);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onSuccess(Response<String> response) {
                String body = response.body().replace("\"id\"", "\"good_id\"");
//                LogUtils.i(body);
                ArrayList<String> strings = GsonUtils.convertJson2Array(body);
                List<GoodBean> goodBeanList = new ArrayList<>();
                for (String str : strings) {
                    GoodBean goodBean = GsonUtils.convertString2Object(str, GoodBean.class);
                    goodBeanList.add(goodBean);
                }

//                LogUtils.i("goodBeanList.size() = " + goodBeanList.size());

                goodReqCallBack.onReqSuccess(goodBeanList);

            }
        });
    }

    /**
     * 标签列表
     *
     * @param sn             机器的SN码
     *                       <p>
     *                       [{"id":1,"name":"热卖商品"},{"id":2,"name":"人气飙升"}]
     *                       <p>
     * @param tagReqCallBack
     * @description: 获取商品标签
     */
    public static void tag(String sn, final ReqCallBack<List<TagBean>> tagReqCallBack) {
        OkGo.<String>post(ConstantValue.TAG)
                .tag(ConstantValue.TAG_TAG)
                .params("sn", sn)
                .execute(new StringCallback() {
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                        try {
                            String body = response.getRawResponse().body().string();
//                            LogUtils.i("string = " + body);
                            FailBean failBean = GsonUtils.convertString2Object(body, FailBean.class);
                            if (tagReqCallBack != null) {
                                tagReqCallBack.onReqFailed(failBean);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onSuccess(Response<String> response) {
                        String body = response.body();
//                        LogUtils.i(body);
                        List<TagBean> tagBeanList = new ArrayList<>();
                        ArrayList<String> strings = GsonUtils.convertJson2Array(body);
                        for (String str : strings) {
                            TagBean tagBean = GsonUtils.convertString2Object(str, TagBean.class);
                            tagBeanList.add(tagBean);
                        }

//                        LogUtils.i("tagBeanList.size() = " + tagBeanList.size());
                        tagReqCallBack.onReqSuccess(tagBeanList);
                    }
                });
    }


    /**
     * 下载新版本安装包
     *
     * @author: liubo
     * @date: 2018年5月10日16:33:15
     * @description:
     */
    public static void downloadNewVersion() {
        OkGo.<File>get(ConstantValue.DOWNLOAD_APK_URL)
                .tag(ConstantValue.TAG_DOWNLOAD_APK)
                .execute(new FileCallback() {
                    @Override
                    public void onStart(Request<File, ? extends Request> request) {
                        super.onStart(request);
                    }

                    @Override
                    public void downloadProgress(Progress progress) {
                        super.downloadProgress(progress);
                    }

                    @Override
                    public void onSuccess(Response<File> response) {

                    }
                });
    }
}
