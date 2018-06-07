package xinshiyeweixin.cn.icbcdemo.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutScroll;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.db.DownloadManager;
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;
import com.tencent.bugly.crashreport.CrashReport;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.BuildConfig;
import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.CategoryAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.GoodAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.MyItemDecoration;
import xinshiyeweixin.cn.icbcdemo.bean.AppBean;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBean;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.bean.FailBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.TagBean;
import xinshiyeweixin.cn.icbcdemo.bean.UpdateBean;
import xinshiyeweixin.cn.icbcdemo.db.BannerDAOUtil;
import xinshiyeweixin.cn.icbcdemo.db.CategoryDAOUtil;
import xinshiyeweixin.cn.icbcdemo.db.GoodDAOUtil;
import xinshiyeweixin.cn.icbcdemo.http.HttpManager;
import xinshiyeweixin.cn.icbcdemo.http.ReqCallBack;
import xinshiyeweixin.cn.icbcdemo.http.ReqProgressCallBack;
import xinshiyeweixin.cn.icbcdemo.http.RequestManager;
import xinshiyeweixin.cn.icbcdemo.install.AutoInstaller;
import xinshiyeweixin.cn.icbcdemo.listener.CategoryItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.listener.CompleteListener;
import xinshiyeweixin.cn.icbcdemo.listener.GoodItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.local.ConstantValue;
import xinshiyeweixin.cn.icbcdemo.service.HorizonService;
import xinshiyeweixin.cn.icbcdemo.utils.AppUtils2;
import xinshiyeweixin.cn.icbcdemo.utils.FileUtils;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.LogUtils;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;
import xinshiyeweixin.cn.icbcdemo.utils.SPUtils;

public class MainActivity2 extends BaseActivity implements GoodItemOnclickListener, CategoryItemOnclickListener, CompleteListener {
    private ArrayList<String> tagList;
    private EasyLayoutScroll easylayoutscroll;
    private RecyclerView goodRecyclerView;
    private CategoryAdapter categoryAdapter;

    private ArrayList<CategoryBean> categoryBeanList;
    private ArrayList<BannerBean> bannerBeanArrayList;


    private GoodAdapter goodAdapter;
    private ArrayList<GoodBean> goodList;
//    private ArrayList<GoodBean> goodDownloadList;

    private ICBCApplication application;
    protected MyPresentation myPresentation;

    private ReqCallBack<AppBean> appReqCallBack;
    private ReqCallBack<ArrayList<BannerBean>> bannerReqCallBack;
    private ReqCallBack<ArrayList<CategoryBean>> categoryReqCallBack;
    private ReqCallBack<List<GoodBean>> goodReqCallBack;
    private ReqCallBack<UpdateBean> updateReqCallBack;
    private ReqCallBack<List<TagBean>> tagReqCallBack;

    private SparseArray<List<GoodBean>> goodBeanSparseArray;

    private long lastClickTime, lastCategoryItemOnclickTime;

    private int currentPosition;
    private String lastVideoPath;
    private ArrayList<String> videoPathList;

    private OkDownload okDownload;


    private Handler handler;

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            HttpManager.app(ICBCApplication.application.uuid, appReqCallBack);
        }
    };

    private DownloadListener downloadAPKListener = new DownloadListener("download") {
        @Override
        public void onStart(Progress progress) {
            Log.i("Demo", "开始下载新版本APK");
        }

        @Override
        public void onProgress(Progress progress) {
            long currentSize = progress.currentSize;
            long totalSize = progress.totalSize;
            Log.i("Demo", "进度 = " + currentSize * 100 / totalSize);
        }

        @Override
        public void onError(Progress progress) {
            LogUtils.i("progress = " + progress.toString());
            DownloadTask download = okDownload.getTask("download");
            download.restart();
        }

        @Override
        public void onFinish(File file, Progress progress) {
            String path = Environment.getExternalStorageDirectory().getPath() + "/ICBC/" + file.getName();
            AppUtils2.installApp(path, BuildConfig.APPLICATION_ID + ".fileprovider");
//
//            Log.i("Demo", "待下载视频列表 \r\ngoodDownloadList = \r\n" + GsonUtils.convertVO2String(goodDownloadList));
//            if (goodDownloadList != null && goodDownloadList.size() > 0) {
//                downloadGoodItemVideo(goodDownloadList.get(0), 0);
//            }
        }

        @Override
        public void onRemove(Progress progress) {
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (myPresentation == null) {
            application.UpdatePresent();
        }
        myPresentation.setCompleteListener(this);
        if (!TextUtils.isEmpty(lastVideoPath)) {
            myPresentation.play(lastVideoPath);
        } else {
            if (goodList != null && goodList.size() > 0) {
                GoodBean bean = goodList.get(0);
                String path = bean.video_url_local;
                if (TextUtils.isEmpty(path)) {
                    path = bean.video_url;
                }
                myPresentation.play(path);
                lastVideoPath = path;
            }
        }
        //不是初次打开，开始下载任务
        if (tagList != null && tagList.size() > 0) {
            for (String tag : tagList) {
                DownloadTask task = okDownload.getTask(tag);
                if (task != null) {
                    task.start();
                }
            }
        }
//        DownloadTask task = okDownload.getTask("task");
//        if (task != null) {
//            task.start();
//        }
//
//        DownloadTask download = okDownload.getTask("download");
//        if (download != null) {
//            download.start();
//        }

        //如果是初次打开，从本地数据库恢复下载任务
        List<Progress> downloading = DownloadManager.getInstance().getDownloading();
        if (downloading != null && downloading.size() > 0) {
            OkDownload.restore(downloading);
        }

    }

    @Override
    protected void onPause() {
        super.onPause();

        //暂停下载任务
        okDownload.pauseAll();
//        DownloadTask task = okDownload.getTask("task");
//        if (task != null) {
//            task.pause();
//        }
//
//        DownloadTask download = okDownload.getTask("download");
//        if (download != null) {
//            download.pause();
//        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //TODO 暂定取消所有的网络请求
        OkGo.getInstance().cancelAll();
//        OkGo.getInstance().cancelTag(ConstantValue.TAG_TAG);
//        OkGo.getInstance().cancelTag(ConstantValue.TAG_GOODS);
//        OkGo.getInstance().cancelTag(ConstantValue.TAG_UPDATE);
//        OkGo.getInstance().cancelTag(ConstantValue.TAG_CATEGORY);
//        OkGo.getInstance().cancelTag(ConstantValue.TAG_DOWNLOAD_APK);
    }

    /**
     * 初始化View
     */
    private void initView() {
        lastCategoryItemOnclickTime = lastClickTime = 0;
        handler = new Handler();

        application = ICBCApplication.application;
        myPresentation = application.getPresentation();

        //TODO 这里测试的时候用
        SPUtils.getInstance().put("UUID", "test1234567890");
        goodBeanSparseArray = new SparseArray<>();
        goodList = new ArrayList<>();
//        goodDownloadList = new ArrayList<>();
        categoryBeanList = new ArrayList<>();
        bannerBeanArrayList = new ArrayList<>();
        videoPathList = new ArrayList<>();
        tagList = new ArrayList<>();

        currentPosition = 0;

        easylayoutscroll = findViewById(R.id.titlecontainer).findViewById(R.id.easylayoutscroll);

        RecyclerView categoryRecyclerView = findViewById(R.id.product_category);
        goodRecyclerView = findViewById(R.id.product_list);

        categoryAdapter = new CategoryAdapter(this, categoryBeanList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        categoryRecyclerView.setLayoutManager(linearLayoutManager);
        categoryRecyclerView.setHasFixedSize(true);
        categoryRecyclerView.setAdapter(categoryAdapter);

        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 4, PagerGridLayoutManager.HORIZONTAL);
        goodRecyclerView.setLayoutManager(layoutManager);
        goodRecyclerView.addItemDecoration(new MyItemDecoration(2, 20, true));

        goodAdapter = new GoodAdapter(this, goodList);
        goodRecyclerView.setAdapter(goodAdapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(goodRecyclerView);


        initReqCallback();
        loadDataFromDataBase();

        initDownload();

        List<BannerBean> bannerBeans = BannerDAOUtil.queryAllBanner();
        if (bannerBeans != null && bannerBeans.size() > 0) {
            showEasyLayoutScroll(bannerBeans, true);
        } else {
            HttpManager.banner(ICBCApplication.application.uuid, bannerReqCallBack);
        }

        //延后检查版本更新
        handler.postDelayed(updateRunnable, ConstantValue.UPDATE_DELAY);

        //九分钟后开启更新任务
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity2.this, HorizonService.class);
                startService(intent);
            }
        }, 9 * 60 * 1000);

    }


    private void initDownload() {
        okDownload = OkDownload.getInstance();
        okDownload.setFolder(ConstantValue.ROOT_PATH);
        okDownload.getThreadPool().setCorePoolSize(3);
    }

    /**
     * 从本地数据库加载数据
     */
    private void loadDataFromDataBase() {
        List<CategoryBean> categoryBeans = CategoryDAOUtil.queryAllCategory();
        if (categoryBeans != null && categoryBeans.size() > 0) {
            //本地数据库有数据，优先展示
            categoryBeanList.clear();
            categoryBeanList.addAll(categoryBeans);

            for (CategoryBean bean : categoryBeans) {
                int cat_id = bean.getCat_id();
                List<GoodBean> goodBeanList = GoodDAOUtil.queryGoodData(cat_id);
                goodBeanSparseArray.append(cat_id, goodBeanList == null ? new ArrayList<GoodBean>() : goodBeanList);
            }
            currentPosition = 0;
            if (goodBeanSparseArray.size() > currentPosition && categoryBeanList.size() > currentPosition) {

                goodList.clear();
                goodList.addAll(goodBeanSparseArray.get(categoryBeanList.get(currentPosition).cat_id));

//                goodDownloadList.clear();
//                goodDownloadList.addAll(goodList);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        goodAdapter.notifyDataSetChanged();
                        goodRecyclerView.scrollToPosition(0);

                        categoryAdapter.notifyDataSetChanged();
                    }
                });
            }

            //延后2s请求网络的新数据
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HttpManager.category(ICBCApplication.application.uuid, categoryReqCallBack);
                }
            }, 2 * 1000);
        } else {
//            LogUtils.i("本地数据库没有数据，直接请求网络的新数据");
            //本地数据库没有数据，直接请求网络的新数据
            HttpManager.category(ICBCApplication.application.uuid, categoryReqCallBack);
        }
    }

    /**
     * 设置回调
     */
    private void initReqCallback() {
        appReqCallBack = new ReqProgressCallBack<AppBean>() {

            @Override
            public void onReqSuccess(final AppBean result) {
                int lastVersionCode = result.version_code;
                int currentVersionCode = BuildConfig.VERSION_CODE;
                if (lastVersionCode > currentVersionCode) {
                    Log.i("Demo", "有新版本,开始下载");
                    //有新版本,开始下载
//                    checkPermissions();

//                    if (okDownload.hasTask("task")) {
//                        okDownload.removeTask("task");
//                    }
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            GetRequest<File> request = OkGo.<File>get(result.file_url);//.headers("", "").params("", "");
                            DownloadTask downloadTask = okDownload.request("download", request)
                                    .priority(100)
                                    .save()
                                    .folder(ConstantValue.NEW_APK)
                                    .register(downloadAPKListener);
                            downloadTask.start();
                        }
                    }, ConstantValue.DOWNLOAD_NEW_APK);
                }
//                else {
//                    //没有新版本APK，直接开始下载item中的视频
//                    Log.i("Demo", "待下载视频列表 \r\ngoodDownloadList = \r\n" + GsonUtils.convertVO2String(goodDownloadList));
//                    if (goodDownloadList != null && goodDownloadList.size() > 0) {
//                        downloadGoodItemVideo(goodDownloadList.get(0), 0);
//                    }
//                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
//                LogUtils.i("message = \r\n" + message);
            }

            @Override
            public void onProgress(long total, long current) {

            }
        };


        bannerReqCallBack = new ReqProgressCallBack<ArrayList<BannerBean>>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onReqSuccess(ArrayList<BannerBean> result) {
                if (result != null && result.size() > 0) {

                    bannerBeanArrayList.clear();
                    bannerBeanArrayList.addAll(result);

                    for (BannerBean bannerBean : result) {
                        BannerDAOUtil.insertBanner(bannerBean);
                        //下载banner图片
                        downloadBannerItemImage(bannerBean, ConstantValue.DOWNLOAD_BANNER_IMAGE_DELAY);
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showEasyLayoutScroll(bannerBeanArrayList, false);
                        }
                    });
                } else {
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
//                LogUtils.i("message = \r\n" + message);
            }

        };

        categoryReqCallBack = new ReqProgressCallBack<ArrayList<CategoryBean>>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onReqSuccess(ArrayList<CategoryBean> result) {
                if (result != null) {
                    categoryBeanList.clear();
                    categoryBeanList.addAll(result);

                    //轮询去查询每种商品分类下的产品
                    for (int i = 0; i < result.size(); i++) {
                        CategoryBean categoryBean = result.get(i);
                        int cat_id = categoryBean.cat_id;
                        HttpManager.goods(SPUtils.getInstance().getString("UUID"), cat_id, null, goodReqCallBack);
                        CategoryDAOUtil.insertCategory(categoryBean);
                    }
                } else {
                    //TODO 没有数据的时候应该展示什么样的界面
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
//                LogUtils.i("message = \r\n" + message);
            }

        };

        goodReqCallBack = new ReqProgressCallBack<List<GoodBean>>() {
            @Override
            public void onReqSuccess(List<GoodBean> result) {
                int cat_id = result.get(0).cat_id;
                List<GoodBean> beanList = GoodDAOUtil.queryGoodData(cat_id);
                for (GoodBean bean : result) {
                    String video_url = bean.video_url;
                    for (GoodBean goodBean : beanList) {
                        String video_url1 = goodBean.video_url;
                        if (video_url.equals(video_url1) && !TextUtils.isEmpty(goodBean.video_url_local)) {
                            File file = new File(goodBean.video_url_local);
                            if (file.exists()) {
                                bean.setVideo_url_local(goodBean.video_url_local);
                            } else {
                                bean.setVideo_url_local(null);
                            }
                        }
                    }
                    if (TextUtils.isEmpty(bean.video_url_local)) {
                        Log.i("Demo", cat_id + "_" + bean.good_id + " >>> 需要下载");
                        downloadGoodItemVideo(bean, ConstantValue.DOWNLOAD_VIDEO_DELAY);
                    } else {
                        Log.i("Demo", cat_id + "_" + bean.good_id + " >>> 不需要下载");
                    }

                    if (TextUtils.isEmpty(bean.image_url_local)) {
                        downloadGoodItemImage(bean, ConstantValue.DOWNLOAD_VIDEO_DELAY);
                    }

                    GoodDAOUtil.insertGood(bean);
                }
                goodBeanSparseArray.put(cat_id, result);
                if (goodBeanSparseArray != null && goodBeanSparseArray.size() > currentPosition && categoryBeanList.size() > currentPosition) {
                    try {
                        List<GoodBean> localList = GoodDAOUtil.queryGoodData(cat_id);

                        goodList.clear();
                        goodList.addAll(goodBeanSparseArray.get(categoryBeanList.get(currentPosition).cat_id));

//                        goodDownloadList.addAll(goodList);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                goodAdapter.notifyDataSetChanged();
                                goodRecyclerView.scrollToPosition(0);

                                categoryAdapter.notifyDataSetChanged();
                            }
                        });
                    } catch (Exception e) {
                        e.printStackTrace();
                        CrashReport.postCatchedException(e);
                    }
                }
                if (goodList.size() > 0) {
                    for (GoodBean goodBean : goodList) {
                        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                            videoPathList.add(goodBean.video_url_local);
                        } else {
                            videoPathList.add(goodBean.video_url);
                        }
                    }
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
            }

            @Override
            public void onProgress(long total, long current) {

            }
        };

        updateReqCallBack = new ReqProgressCallBack<UpdateBean>() {

            @Override
            public void onReqSuccess(UpdateBean result) {

            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;

            }

            @Override
            public void onProgress(long total, long current) {

            }
        };

        tagReqCallBack = new ReqProgressCallBack<List<TagBean>>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onReqSuccess(List<TagBean> result) {

            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
            }
        };
    }

    private void downloadGoodItemVideo(final GoodBean goodBean, int delayTime) {
        final String tag = goodBean.cat_id + "_" + goodBean.good_id;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetRequest<File> request = OkGo.get(goodBean.video_url);//.headers("", "").params("", "");
//                DownloadTask downloadTask = okDownload.request(tag, request)
                DownloadTask downloadTask = okDownload.request(tag, request)
                        .extra1(goodBean.name)
                        .extra1(goodBean.good_id)
                        .save()
                        .folder(ConstantValue.GOOD_VIDEO_PATH)
                        .register(new DownloadListener(tag) {
                            @Override
                            public void onStart(Progress progress) {
                                Log.i("Demo", "开始下载视频 = " + tag + "\r\n progress = " + progress.fileName);
                            }

                            @Override
                            public void onProgress(Progress progress) {
                                final long currentSize = progress.currentSize;
                                final long totalSize = progress.totalSize;
                                Log.i("Demo", tag + " >>> 进度 = " + currentSize * 100 / totalSize);
                            }

                            @Override
                            public void onError(Progress progress) {
                                Log.i("Demo", "onError \r\n progress = \r\n" + progress.fileName);
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {

                                Log.i("Demo", "视频下载结束");
                                String path = Environment.getExternalStorageDirectory().getPath() + "/ICBC/" + file.getName();
                                String video_url = progress.url;

                                Log.i("Demo", "============================================================");
                                Log.i("Demo", "video_url = \r\n" + video_url);

                                //根据下载用到的URL去更新数据库的某条数据
                                GoodDAOUtil.updateGood(video_url, path);

                                for (GoodBean goodBean : goodList) {
                                    if (video_url.equals(goodBean.video_url)) {
                                        goodBean.setVideo_url_local(path);
//                                        goodDownloadList.remove(goodBean);
                                    }
                                }
                                Log.i("Demo", "============================================================");
                            }

                            @Override
                            public void onRemove(Progress progress) {
                            }
                        });
                downloadTask.start();
                tagList.add(tag);
            }
        }, delayTime);
    }

    private void downloadGoodItemImage(final GoodBean goodBean, int delayTime) {
        final String tag = goodBean.cat_id + "_" + goodBean.good_id + "_image";
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetRequest<File> request = OkGo.get(goodBean.image_url);//.headers("", "").params("", "");
//                DownloadTask downloadTask = okDownload.request(tag, request)
                DownloadTask downloadTask = okDownload.request(tag, request)
                        .extra1(goodBean.name)
                        .extra1(goodBean.good_id)
                        .save()
                        .folder(ConstantValue.GOOD_IMAGE_PATH)
                        .register(new DownloadListener(tag) {
                            @Override
                            public void onStart(Progress progress) {
                            }

                            @Override
                            public void onProgress(Progress progress) {
                            }

                            @Override
                            public void onError(Progress progress) {
                                Log.i("Demo", "onError \r\n progress = \r\n" + progress.fileName);
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                Log.i("Demo", "图片下载结束");

                                String path = ConstantValue.GOOD_IMAGE_PATH + file.getName();
                                String image_url = progress.url;

                                Log.i("Demo", "============================================================");
                                Log.i("Demo", "image_url = \r\n" + image_url);

                                //根据下载用到的URL去更新数据库的某条数据
                                GoodDAOUtil.updateGoodImageLocalUrl(image_url, path);

                                for (GoodBean goodBean : goodList) {
                                    if (image_url.equals(goodBean.video_url)) {
                                        goodBean.setVideo_url_local(path);
//                                        goodDownloadList.remove(goodBean);
                                    }
                                }
                                Log.i("Demo", "============================================================");
                            }

                            @Override
                            public void onRemove(Progress progress) {
                            }
                        });
                downloadTask.start();
                tagList.add(tag);
            }
        }, delayTime);
    }

    private void downloadBannerItemImage(final BannerBean bannerBean, int delayTime) {
        final String tag = "" + bannerBean.banner_id;
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                GetRequest<File> request = OkGo.get(bannerBean.image_url);//.headers("", "").params("", "");
//                DownloadTask downloadTask = okDownload.request(tag, request)
                DownloadTask downloadTask = okDownload.request(tag, request)
                        .extra1(bannerBean.title)
                        .extra1(bannerBean.banner_id)
                        .save()
                        .folder(ConstantValue.BANNER_IMAGE_PATH)
                        .register(new DownloadListener(tag) {
                            @Override
                            public void onStart(Progress progress) {
                            }

                            @Override
                            public void onProgress(Progress progress) {
                            }

                            @Override
                            public void onError(Progress progress) {
                                Log.i("Demo", "onError \r\n progress = \r\n" + progress.fileName);
                            }

                            @Override
                            public void onFinish(File file, Progress progress) {
                                String path = ConstantValue.GOOD_IMAGE_PATH + file.getName();
                                String image_url = progress.url;

                                Log.i("Demo", "下载banner图片完毕 >>> " + image_url);

                                //根据下载用到的URL去更新数据库的某条数据
                                try {
                                    BannerDAOUtil.updateBannerImageLocalUrl(image_url, path);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                for (BannerBean bean : bannerBeanArrayList) {
                                    if (image_url.equals(bean.image_url)) {
                                        bean.setImage_url_local(path);
                                    }
                                }
                            }

                            @Override
                            public void onRemove(Progress progress) {
                            }
                        });
                downloadTask.start();
                tagList.add(tag);
            }
        }, delayTime);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        LogUtils.i("onRequestPermissionsResult");

        boolean flag = true;
        for (int result : grantResults) {
            if (result != 0) {
                flag = false;
            }
        }
        if (flag) {
            downloadNewVersion();
        } else {
//            LogUtils.i("flag = false");
        }
    }

    private void downloadNewVersion() {
        RequestManager requestManager = RequestManager.getInstance(this);
        String destFileDir = Environment.getExternalStorageDirectory() + File.separator;
        requestManager.downLoadFile("http://3d.leygoo.cn/apk/app-release.apk", destFileDir, new ReqProgressCallBack<Object>() {
            @Override
            public void onProgress(long total, long current) {
//                LogUtils.i("下载进度 ：\r\n" + current * 100 / total + " % ");
            }

            @Override
            public void onReqSuccess(Object result) {
//                LogUtils.i("result = " + GsonUtils.convertVO2String(result));
                File file = new File(Environment.getExternalStorageDirectory() + File.separator, "ICBC_update.apk");
                AppUtils2.installApp(file, BuildConfig.APPLICATION_ID + ".fileprovider");
            }

            @Override
            public void onReqFailed(FailBean failBean) {

            }
        });
    }

    /**
     * 检查是否具有运行时权限（读.写）
     */
    private void checkPermissions() {
//        LogUtils.i("checkPermissions");
        File temp = new File(Environment.getExternalStorageDirectory() + File.separator, "ICBC_update.apk");
        if (FileUtils.isFileExists(temp)) {
//            LogUtils.i("删除已存在的新版本APK");
            FileUtils.deleteFile(temp);
        }
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            boolean tempBoolean = true;
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
//                    LogUtils.i(str + " -- > 无权限");
                    this.requestPermissions(permissions, ConstantValue.REQUEST_RUN_PERMISSION);
                    tempBoolean = false;
                } else {
//                    LogUtils.i(str + " -- > 有权限");
                }
            }
            if (tempBoolean) {
                downloadNewVersion();
//                autoInstall();
            }
        }
    }

    private void autoInstall() {
        AutoInstaller.Builder builder = new AutoInstaller.Builder(this);
//        builder.setMode(AutoInstaller.MODE.ROOT_ONLY);
        builder.setMode(AutoInstaller.MODE.BOTH);
        builder.setOnStateChangedListener(new AutoInstaller.OnStateChangedListener() {
            @Override
            public void onStart() {
                // 当后台安装线程开始时回调
//                LogUtils.i("onStart");
            }

            @Override
            public void onComplete() {
                // 当请求安装完成时回调
//                LogUtils.i("onComplete");
            }

            @Override
            public void onNeed2OpenService() {
                // 当需要用户手动打开 `辅助功能服务` 时回调
                // 可以在这里提示用户打开辅助功能
//                LogUtils.i("onNeed2OpenService");

            }
        });

        AutoInstaller installer = builder.build();
        installer.installFromUrl("http://3d.leygoo.cn/apk/app-release.apk");
//        installer.install2(file);
    }

    private void showEasyLayoutScroll(List<BannerBean> bannerBeanArrayList, boolean fromDatabase) {
        List<View> views = new ArrayList<>();
        for (BannerBean bannerBean : bannerBeanArrayList) {
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_view_single, null);
            ImageView tv_img = moreView.findViewById(R.id.tv_img);
            Glide.with(this).asBitmap().load(bannerBean.image_url).into(tv_img);
            views.add(moreView);
        }
        //设置数据集
        easylayoutscroll.setEasyViews(views);
        //开始滚动
        easylayoutscroll.startScroll();

//        easylayoutscroll.setOnItemClickListener(new EasyLayoutListener.OnItemClickListener() {
//            @Override
//            public void onItemClick(int pos, View view) {
//            }
//        });
        if (fromDatabase) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    HttpManager.banner(ICBCApplication.application.uuid, bannerReqCallBack);
                }
            }, 2 * 1000);
        }
    }


    @Override
    public void playItemVideo(String videoPath) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastClickTime < 1000) {
            LogUtils.i("频繁点击");
        } else {
            lastClickTime = currentTimeMillis;
            if (myPresentation == null) {
                Toast.makeText(this, "playItemVideo  --> myPresentation == null", Toast.LENGTH_SHORT).show();
                application.UpdatePresent();
            }
            if (!TextUtils.isEmpty(videoPath)) {
                myPresentation.play(videoPath);
                lastVideoPath = videoPath;
            } else {
                // 点击的条目是正在播放的条目
                Toast.makeText(this, "获取视频播放地址失败", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onGoodItemClick(GoodBean goodBean) {
        startActivity(
                new Intent(MainActivity2.this, GoodDetailActivity.class)
                        .putExtra("GOOD", GsonUtils.convertVO2String(goodBean)));
    }


    @Override
    public void onCategoryItemOnclick(int cat_id, int position) {
        long currentTimeMillis = System.currentTimeMillis();
        if (currentTimeMillis - lastCategoryItemOnclickTime < 500) {
            return;
        }
        lastCategoryItemOnclickTime = currentTimeMillis;
        currentPosition = position;
        if (goodBeanSparseArray.size() > position) {
            goodList.clear();
            goodList.addAll(goodBeanSparseArray.get(cat_id));
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    goodAdapter.notifyDataSetChanged();
                    goodRecyclerView.scrollToPosition(0);

                    categoryAdapter.notifyDataSetChanged();
                }
            });

            if (myPresentation != null) {
                GoodBean goodBean = goodList.get(0);
                String path;
                if (!TextUtils.isEmpty(goodBean.video_url_local)) {
                    path = goodBean.video_url_local;
                } else {
                    path = goodBean.video_url;
                }
                myPresentation.play(path);
            }
        }
    }


    @Override
    public String onComplete(String videoPath) {
        for (int i = 0; i < goodList.size(); i++) {
            GoodBean bean = goodList.get(i);
            if (videoPath.startsWith("http")) {
                //网络地址
                if (bean.video_url.equals(videoPath)) {
                    if (i == goodList.size() - 1) {
                        return getVideoPath(0);
                    } else {
                        return getVideoPath(i + 1);
                    }
                }
            } else {
                //本地地址
                if (bean.video_url_local.equals(videoPath)) {
                    if (i == goodList.size() - 1) {
                        return getVideoPath(0);
                    } else {
                        return getVideoPath(i + 1);
                    }
                }
            }
        }

        return videoPath;
    }

    private String getVideoPath(int position) {
        GoodBean goodBean = goodList.get(position);
        if (!TextUtils.isEmpty(goodBean.video_url_local)) {
            return goodBean.video_url_local;
        } else {
            return goodBean.video_url;
        }
    }
}
