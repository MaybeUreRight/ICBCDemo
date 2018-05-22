package xinshiyeweixin.cn.icbcdemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.lzy.okgo.model.Progress;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.OkDownload;
import com.lzy.okserver.download.DownloadListener;
import com.lzy.okserver.download.DownloadTask;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.BuildConfig;
import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.MyItemDecoration;
import xinshiyeweixin.cn.icbcdemo.adapter.GoodAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.CategoryAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBean;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBeanDao;
import xinshiyeweixin.cn.icbcdemo.bean.FailBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBeanDao;
import xinshiyeweixin.cn.icbcdemo.bean.TagBean;
import xinshiyeweixin.cn.icbcdemo.bean.UpdateBean;
import xinshiyeweixin.cn.icbcdemo.db.DAOUtil;
import xinshiyeweixin.cn.icbcdemo.http.HttpManager;
import xinshiyeweixin.cn.icbcdemo.http.ReqCallBack;
import xinshiyeweixin.cn.icbcdemo.http.ReqProgressCallBack;
import xinshiyeweixin.cn.icbcdemo.http.RequestManager;
import xinshiyeweixin.cn.icbcdemo.install.AutoInstaller;
import xinshiyeweixin.cn.icbcdemo.listener.CategoryItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.listener.GoodItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.utils.AppUtils2;
import xinshiyeweixin.cn.icbcdemo.utils.FileUtils;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.LogUtils;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;
import xinshiyeweixin.cn.icbcdemo.utils.SPUtils;

public class MainActivity extends AppCompatActivity implements GoodItemOnclickListener, CategoryItemOnclickListener {
    public static final int REQUEST_RUN_PERMISSION = 111;
    private EasyLayoutScroll easylayoutscroll;

    private RecyclerView categoryRecyclerView;
    private RecyclerView goodRecyclerView;


    private ArrayList<CategoryBean> categoryBeanList;
    private ArrayList<BannerBean> bannerBeanArrayList;

    private CategoryAdapter categoryAdapter;

    private GoodAdapter goodAdapter;
    private ArrayList<GoodBean> goodList;

    private MyPresentation myPresentation;

    private ReqCallBack<ArrayList<BannerBean>> bannerReqCallBack;
    private ReqCallBack<ArrayList<CategoryBean>> categoryReqCallBack;
    private ReqCallBack<UpdateBean> updateReqCallBack;
    private ReqCallBack<List<GoodBean>> goodReqCallBack;
    private ReqCallBack<List<TagBean>> tagReqCallBack;
    private ReqCallBack downloadReqCallBack;

    private SparseArray<List<GoodBean>> goodBeanSparseArray;
//    private ArrayList<CategoryBean> result;

    private int currentPosition;
    private String lastVideoPath;

    private CategoryBeanDao categoryDAO;
    private GoodBeanDao goodDAO;

    private Handler handler;

    private DownloadListener listener = new DownloadListener("task") {
        @Override
        public void onStart(Progress progress) {
            LogUtils.i("===================== onStart ============================");
            LogUtils.i("progress = " + progress.toString());
        }

        @Override
        public void onProgress(Progress progress) {
            LogUtils.i("===================== onProgress ============================");
            long currentSize = progress.currentSize;
            long totalSize = progress.totalSize;
            Toast.makeText(MainActivity.this, "进度 = " + currentSize * 100 / totalSize, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(Progress progress) {
            LogUtils.i("===================== onError ============================");
            LogUtils.i("progress = " + progress.toString());
        }

        @Override
        public void onFinish(File file, Progress progress) {
            LogUtils.i("===================== onFinish ============================");
            LogUtils.i("progress = " + progress.toString());
            LogUtils.i("file.getPath() = " + file.getPath());
            LogUtils.i("file.getAbsolutePath() = " + file.getAbsolutePath());
            String path = Environment.getExternalStorageDirectory().getPath() + "/ICBC/" + file.getName();
            //根据下载用到的URL去更新数据库的某条数据
            DAOUtil.updateGood(progress.url, path);
        }

        @Override
        public void onRemove(Progress progress) {
            LogUtils.i("===================== onRemove ============================");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    /**
     * 初始化View
     */
    private void initView() {
        handler = new Handler();
        //TODO 这里测试的时候用
        SPUtils.getInstance().put("UUID", "test1234567890");
        goodBeanSparseArray = new SparseArray<>();
//        result = new ArrayList<>();
        goodList = new ArrayList<>();
        categoryBeanList = new ArrayList<>();
        bannerBeanArrayList = new ArrayList<>();


        currentPosition = 0;

        easylayoutscroll = findViewById(R.id.titlecontainer).findViewById(R.id.easylayoutscroll);


        categoryRecyclerView = findViewById(R.id.product_category);
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


//        myPresentation = ICBCApplication.application.getPresentation();

        //TODO 检查是否有权限，然后下载最新版本apk
//        checkPermissions();


        initReqCallback();
        loadDataFromDataBase();


        //开启更新任务，九分钟更新一次
//        Intent intent = new Intent(this, HorizonService.class);
//        startService(intent);

//        initDownload();

        List<BannerBean> bannerBeans = DAOUtil.queryAllBanner();
        if (bannerBeans != null && bannerBeans.size() > 0) {
            showEasyLayoutScroll(bannerBeans, true);
        } else {
            HttpManager.banner(ICBCApplication.application.uuid, bannerReqCallBack);
        }
    }

//    private void initDownload() {
//        OkDownload okDownload = OkDownload.getInstance();
//        String path = Environment.getExternalStorageDirectory().getPath() + "/ICBC/";
//        okDownload.setFolder(path);
//        okDownload.getThreadPool().setCorePoolSize(3);
//        okDownload.addOnAllTaskEndListener(this);
//
//    }

    /**
     * 从本地数据库加载数据
     */
    private void loadDataFromDataBase() {
        categoryDAO = ICBCApplication.application.categoryDaoSession.getCategoryBeanDao();
        goodDAO = ICBCApplication.application.goodDaoSession.getGoodBeanDao();

        List<CategoryBean> categoryBeans = DAOUtil.queryAllCategory();
        if (categoryBeans != null && categoryBeans.size() > 0) {
            LogUtils.i("从本数据库加载数据");
            //本地数据库有数据，优先展示
            categoryBeanList.clear();
            categoryBeanList.addAll(categoryBeans);

            for (CategoryBean bean : categoryBeans) {
                int cat_id = bean.getCat_id();
                List<GoodBean> goodBeanList = DAOUtil.queryGoodData(cat_id);
                goodBeanSparseArray.append(cat_id, goodBeanList == null ? new ArrayList<GoodBean>() : goodBeanList);
            }
            currentPosition = 0;
            if (goodBeanSparseArray.size() > currentPosition && categoryBeanList.size() > currentPosition) {

                goodList.clear();
                goodList.addAll(goodBeanSparseArray.get(categoryBeanList.get(currentPosition).cat_id));
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
            LogUtils.i("本地数据库没有数据，直接请求网络的新数据");
            //本地数据库没有数据，直接请求网络的新数据
            HttpManager.category(ICBCApplication.application.uuid, categoryReqCallBack);
        }
    }

    /**
     * 设置回调
     */
    private void initReqCallback() {


        bannerReqCallBack = new ReqProgressCallBack<ArrayList<BannerBean>>() {
            @Override
            public void onProgress(long total, long current) {

            }

            @Override
            public void onReqSuccess(ArrayList<BannerBean> result) {
                if (result != null) {
                    bannerBeanArrayList.clear();
                    bannerBeanArrayList.addAll(result);

                    for (BannerBean bannerBean : result) {
                        DAOUtil.insertBanner(bannerBean);
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
                LogUtils.i("message = \r\n" + message);
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
                    for (CategoryBean categoryBean : result) {
                        int cat_id = categoryBean.cat_id;
                        HttpManager.goods(SPUtils.getInstance().getString("UUID"), cat_id, null, goodReqCallBack);
//                        categoryDAO.insert(categoryBean);
                        DAOUtil.insertCategory(categoryBean);
                    }
                } else {
                    //TODO 没有数据的时候应该展示什么样的界面
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
                LogUtils.i("message = \r\n" + message);
            }

        };

        goodReqCallBack = new ReqProgressCallBack<List<GoodBean>>() {
            @Override
            public void onReqSuccess(List<GoodBean> result) {
                int cat_id = result.get(0).cat_id;
                goodBeanSparseArray.put(cat_id, result);
                if (goodBeanSparseArray.size() > currentPosition && categoryBeanList.size() > currentPosition) {

                    goodList.clear();
                    goodList.addAll(goodBeanSparseArray.get(categoryBeanList.get(currentPosition).cat_id));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            goodAdapter.notifyDataSetChanged();
                            goodRecyclerView.scrollToPosition(0);

                            categoryAdapter.notifyDataSetChanged();
                        }
                    });
                }
                if (result.size() > 0) {
                    for (GoodBean goodBean : result) {
                        DAOUtil.insertGood(goodBean);

                        GetRequest<File> request = OkGo.<File>get(goodBean.video_url);//.headers("", "").params("", "");
                        DownloadTask downloadTask = OkDownload.request("task", request)
                                .extra1(goodBean.name)
                                .extra1(goodBean.good_id)
                                .save()
                                .register(listener);
                        downloadTask.start();
                    }
                }
            }

            @Override
            public void onReqFailed(FailBean failBean) {
                String message = failBean.message;
                LogUtils.i("message = \r\n" + message);
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
                LogUtils.i("message = \r\n" + message);

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
                LogUtils.i("message = \r\n" + message);
            }
        };

//        downloadReqCallBack=new ReqProgressCallBack<List<CategoryBean>>() {
//            @Override
//            public void onProgress(long total, long current) {
//
//            }
//
//            @Override
//            public void onReqSuccess(List<CategoryBean> result) {
//
//            }
//
//            @Override
//            public void onReqFailed(String errorMsg) {
//
//            }
//        };


    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.i("onRequestPermissionsResult");

        boolean flag = true;
        for (int result : grantResults) {
            if (result != 0) {
                flag = false;
            }
        }
        if (flag) {
            downloadNewVersion();
        } else {
            LogUtils.i("falg = false");
        }
    }

    private void downloadNewVersion() {
        LogUtils.i("downloadNewVersion");
//        Environment.getExternalStorageDirectory() + File.separator, "ICBC_update.apk"

        RequestManager requestManager = RequestManager.getInstance(this);
        String destFileDir = Environment.getExternalStorageDirectory() + File.separator;
        requestManager.downLoadFile("http://3d.leygoo.cn/apk/app-release.apk", destFileDir, new ReqProgressCallBack<Object>() {
            @Override
            public void onProgress(long total, long current) {
//                    LogUtils.i( "total = " + total + "\r\ncurrent" + current);
                LogUtils.i("下载进度 ：\r\n" + current * 100 / total + " % ");
            }

            @Override
            public void onReqSuccess(Object result) {
                LogUtils.i("result = " + GsonUtils.convertVO2String(result));
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
        LogUtils.i("checkPermissions");
        File temp = new File(Environment.getExternalStorageDirectory() + File.separator, "ICBC_update.apk");
        if (FileUtils.isFileExists(temp)) {
            LogUtils.i("删除已存在的新版本APK");
            FileUtils.deleteFile(temp);
        }
        String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= 23) {
            boolean tempBoolean = true;
            for (String str : permissions) {
                if (this.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    LogUtils.i(str + " -- > 无权限");
                    this.requestPermissions(permissions, REQUEST_RUN_PERMISSION);
                    tempBoolean = false;
                } else {
                    LogUtils.i(str + " -- > 有权限");
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
                LogUtils.i("onStart");
            }

            @Override
            public void onComplete() {
                // 当请求安装完成时回调
                LogUtils.i("onComplete");
            }

            @Override
            public void onNeed2OpenService() {
                // 当需要用户手动打开 `辅助功能服务` 时回调
                // 可以在这里提示用户打开辅助功能
                LogUtils.i("onNeed2OpenService");

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
//            TextView tv_title = moreView.findViewById(R.id.tv_title);
//            tv_title.setText(data.get(i));
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
    public void onGoodItemOnclick(final String videoPath) {
//            myPresentation.startVideo(videoPath);
        if (TextUtils.isEmpty(lastVideoPath) || !videoPath.equals(lastVideoPath)) {
            //使用SurfaceView播放视频
            myPresentation.play(videoPath);
            lastVideoPath = videoPath;
        } else {
            // 点击的条目是正在播放的条目
        }
//        myPresentation.startVideo("android.resource://" + getPackageName() + "/" + R.raw.demo);
    }

    @Override
    public void onCategoryItemOnclick(int cat_id, int position) {

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
        }
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

}
