package xinshiyeweixin.cn.icbcdemo.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutListener;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutScroll;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductInfoAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.db.DAOUtil;
import xinshiyeweixin.cn.icbcdemo.http.ReqProgressCallBack;
import xinshiyeweixin.cn.icbcdemo.http.RequestManager;
import xinshiyeweixin.cn.icbcdemo.install.AutoInstaller;
import xinshiyeweixin.cn.icbcdemo.listener.ProductCategoryItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.listener.ProductItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.utils.AppUtils;
import xinshiyeweixin.cn.icbcdemo.utils.FileUtils;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.LogUtils;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;

public class MainActivity extends AppCompatActivity implements ProductItemOnclickListener, ProductCategoryItemOnclickListener {

    private EasyLayoutScroll easylayoutscroll;

    private RecyclerView product_cagetory;
    private RecyclerView product_list;
    private ArrayList<ProductInfo> productInfos;

    private ProductInfoAdapter productCategoryAdapter;

    private ProductAdapter productAdapter;
    private ArrayList<Product> products;

    private MyPresentation myPresentation;
    private ICBCApplication icbcApplication;

    public static final int REQUEST_RUN_PERMISSION = 111;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        easylayoutscroll = findViewById(R.id.titlecontainer).findViewById(R.id.easylayoutscroll);
        initEasyLayoutScroll();

        productInfos = new ArrayList<>();
        products = new ArrayList<>();

        initData(productInfos);
        product_cagetory = findViewById(R.id.product_category);
        product_list = findViewById(R.id.product_list);

        productCategoryAdapter = new ProductInfoAdapter(this, productInfos);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        product_cagetory.setLayoutManager(linearLayoutManager);
        product_cagetory.setAdapter(productCategoryAdapter);

        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 2, PagerGridLayoutManager.HORIZONTAL);
        product_list.setLayoutManager(layoutManager);
        product_list.addItemDecoration(new MyItemDecoration(2, 20, true));

        products.addAll(productInfos.get(0).getProductList());
        productAdapter = new ProductAdapter(this, products);
        product_list.setAdapter(productAdapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(product_list);


//        icbcApplication = (ICBCApplication) getApplication();
//        myPresentation = icbcApplication.getPresentation();

        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        LogUtils.i( "onRequestPermissionsResult");
//        LogUtils.i( "requestCode = " + requestCode);
//        LogUtils.i( "permissions = " + GsonUtils.convertVO2String(permissions));
//        LogUtils.i( "grantResults = " + GsonUtils.convertVO2String(grantResults));

        boolean flag = true;
        for (int result : grantResults) {
            if (result != 0) {
                flag = false;
            }
        }
        if (flag) {
            downloadNewVersion();
        } else {
            LogUtils.i( "falg = false");
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
                LogUtils.i( "result = " + GsonUtils.convertVO2String(result));
//                autoInstall((File) result);
//                AppUtils.installAppSilent((File) result);
//                AppUtils.installApp((File) result);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator, "ICBC_update.apk");
//                AppUtils.installApp(file);
            }

            @Override
            public void onReqFailed(String errorMsg) {
                LogUtils.i( "errorMsg = " + errorMsg);

            }
        });
    }

    /**
     * 检车是否具有运行时权限（读.写）
     */
    private void checkPermissions() {
        LogUtils.i( "checkPermissions");
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
                    LogUtils.i( str + " -- > 无权限");
                    this.requestPermissions(permissions, REQUEST_RUN_PERMISSION);
                    tempBoolean = false;
                } else {
                    LogUtils.i( str + " -- > 有权限");
                }
            }
            if (tempBoolean) {
//                downloadNewVersion();
                autoInstall();
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
                LogUtils.i( "onStart");
            }

            @Override
            public void onComplete() {
                // 当请求安装完成时回调
                LogUtils.i( "onComplete");
            }

            @Override
            public void onNeed2OpenService() {
                // 当需要用户手动打开 `辅助功能服务` 时回调
                // 可以在这里提示用户打开辅助功能
                LogUtils.i( "onNeed2OpenService");

            }
        });

        AutoInstaller installer = builder.build();
        installer.installFromUrl("http://3d.leygoo.cn/apk/app-release.apk");
//        installer.install2(file);
    }

    private void initEasyLayoutScroll() {
        //
        ArrayList<String> data = new ArrayList<>();
        data.add("测试条目1");
        data.add("测试条目2");
        data.add("测试条目3");
        data.add("测试条目4");

        List<View> views = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_view_single, null);
            TextView tv_title = moreView.findViewById(R.id.tv_title);
            tv_title.setText(data.get(i));
            views.add(moreView);
        }
        //设置数据集
        easylayoutscroll.setEasyViews(views);
        //开始滚动
        easylayoutscroll.startScroll();

        easylayoutscroll.setOnItemClickListener(new EasyLayoutListener.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Toast.makeText(MainActivity.this, "您点击了第" + pos + "条索引", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 初始化数据（假数据）
     *
     * @param list
     */
    private void initData(ArrayList<ProductInfo> list) {
        List<ProductInfo> productInfos = DAOUtil.queryAllProductInfo();
        if (productInfos != null && productInfos.size() > 0) {
            //TODO 本地数据库已经有数据，先加载本地数据库的数据
            for (ProductInfo info : productInfos) {
                Long id = info.getId();
                List<Product> products = DAOUtil.queryProductData(id, 150);

                List<Product> productList = info.getProductList();
                if (productList == null) {
                    productList = new ArrayList<>();
                } else {
                    productList.clear();
                }
                productList.addAll(products);
            }
            list.addAll(productInfos);
        } else {
            //TODO 本地数据库没有数据，请求新数据(没有接口，所以这里用假数据)
            for (int i = 0; i < 10; i++) {
                ProductInfo info = new ProductInfo();
                info.setCagetory(getString(R.string.item_product_category) + i);
                DAOUtil.insertProductInfo(info);
                for (int j = 0; j < 13; j++) {
                    Product product = new Product();
                    product.setId(null);
                    product.setName(getString(R.string.item_product_name) + " -> " + j);
                    product.setPicUrl("https://i03piccdn.sogoucdn.com/66766b011ffe1eac");
                    product.setIntroduction("秋田犬（拉丁学名：Japanese Akita），别名日本秋田犬、日系秋田犬，原产地日本。其祖先被称呼为山地狩猎犬，是大型的熊猎犬。除了协助猎熊外，它还被利用来捕...");
                    product.setProductInfoId(info.getId());
                    if (5 - j > 0) {
                        product.setRecommend(true);
                    } else {
                        product.setRecommend(false);
                    }
                    DAOUtil.insertProduct(product);
                }
                list.add(info);
            }
        }
    }

    @Override
    public void onProductItemOnclick(String videoPath) {
        //暂时用假数据
        String uri = "android.resource://" + getPackageName() + "/" + R.raw.demo;
        this.myPresentation.startVideo(uri);

//        this.myPresentation.startVideo(videoPath);
    }

    @Override
    public void onProductCategoryItemOnclick(List<Product> productList, int position) {
        products.clear();
        products.addAll(productList);
        productAdapter.notifyDataSetChanged();
        product_list.scrollToPosition(0);

        productCategoryAdapter.notifyDataSetChanged();
    }


    public class MyItemDecoration extends RecyclerView.ItemDecoration {

        private int spanCount;
        private int spacing;
        private boolean includeEdge;

        public MyItemDecoration(int spanCount, int spacing, boolean includeEdge) {
            this.spanCount = spanCount;
            this.spacing = spacing;
            this.includeEdge = includeEdge;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            int position = parent.getChildAdapterPosition(view); // item position
            int column = position % spanCount; // item column

            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount; // spacing - column * ((1f / spanCount) * spacing)
                outRect.right = (column + 1) * spacing / spanCount; // (column + 1) * ((1f / spanCount) * spacing)

                if (position < spanCount) { // top edge
                    outRect.top = spacing;
                }
                outRect.bottom = spacing; // item bottom
            } else {
                outRect.left = column * spacing / spanCount; // column * ((1f / spanCount) * spacing)
                outRect.right = spacing - (column + 1) * spacing / spanCount; // spacing - (column + 1) * ((1f /    spanCount) * spacing)
                if (position >= spanCount) {
                    outRect.top = spacing; // item top
                }
            }
        }
    }
}
