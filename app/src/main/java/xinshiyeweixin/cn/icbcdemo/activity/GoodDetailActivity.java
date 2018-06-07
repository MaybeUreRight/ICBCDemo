package xinshiyeweixin.cn.icbcdemo.activity;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutListener;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutScroll;

import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.GoodDetailAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.db.BannerDAOUtil;
import xinshiyeweixin.cn.icbcdemo.db.GoodDAOUtil;
import xinshiyeweixin.cn.icbcdemo.listener.CompleteListener;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;
import xinshiyeweixin.cn.icbcdemo.view.JustifyTextView;
import xinshiyeweixin.cn.icbcdemo.view.QRCodeDialog;

public class GoodDetailActivity extends BaseActivity implements View.OnClickListener, CompleteListener {
    private EasyLayoutScroll easyLayoutScroll;
    private ImageView detail_img;
    private TextView productDetailNameCh;
    private TextView productDetailNameEn;
    private TextView productDetailMarketPrice;
    private TextView productDetailICBCPrice;
    private TextView productDetailOriginal;
    //    private TextView productDetailDesc;
    private TextView productDetailDesc;

    private TextView buy;

    private JustifyTextView textViewPrice;
    private JustifyTextView justifyTextViewName;
    private JustifyTextView justifyTextViewOriginal;
    private JustifyTextView justifyTextViewIntro;
    private JustifyTextView product_detail_price_normal1;

    private RecyclerView productDetail;
    private ArrayList<GoodBean> detailBeans;
    private GoodDetailAdapter goodDetailAdapter;

    private String currentPath;

    private GoodBean goodBean;

    private ICBCApplication application;
    protected MyPresentation myPresentation;

    private Handler handler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideBottomUIMenu();
        setContentView(R.layout.activity_good_detail);
        initView();
        initRecyclerView();
    }

    private void initView() {
        application = ICBCApplication.application;
        myPresentation = application.getPresentation();
        myPresentation.setCompleteListener(this);
        handler = new Handler();

        goodBean = GsonUtils.convertString2Object(getIntent().getStringExtra("GOOD"), GoodBean.class);
        findViewById(R.id.back_container).setOnClickListener(this);

        buy = findViewById(R.id.buy);
        buy.setOnClickListener(this);
        productDetail = (RecyclerView) findViewById(R.id.product_detail);
        detail_img = (ImageView) findViewById(R.id.detail_img);
        easyLayoutScroll = findViewById(R.id.product_detail_titlecontainer).findViewById(R.id.easylayoutscroll);
        productDetailNameCh = (TextView) findViewById(R.id.product_detail_name_ch);
        productDetailNameEn = (TextView) findViewById(R.id.product_detail_name_en);
        productDetailMarketPrice = (TextView) findViewById(R.id.product_detail_price_high);

        productDetailICBCPrice = (TextView) findViewById(R.id.product_detail_price_normal);
        productDetailOriginal = (TextView) findViewById(R.id.product_detail_original);
        productDetailDesc = (TextView) findViewById(R.id.product_detail_desc);

        textViewPrice = findViewById(R.id.tv_price);
        justifyTextViewName = findViewById(R.id.jtv_name);
        justifyTextViewOriginal = findViewById(R.id.jtv_original);
        justifyTextViewIntro = findViewById(R.id.jtv_intro);
        product_detail_price_normal1 = findViewById(R.id.product_detail_price_normal1);


        initEasyLayoutScroll();

        showContent();

        Typeface microsoftYaHei = Typeface.createFromAsset(getAssets(), "MicrosoftYaHei.ttc");
        Typeface microsoftYaHeiLight = Typeface.createFromAsset(getAssets(), "MicrosoftYaHeiLight.ttf");
//        Typeface pingFangRegular = Typeface.createFromAsset(getAssets(), "PingFangRegular.ttf");

        productDetailNameCh.setTypeface(microsoftYaHei);
        productDetailNameEn.setTypeface(microsoftYaHeiLight);
        productDetailOriginal.setTypeface(microsoftYaHeiLight);
        productDetailDesc.setTypeface(microsoftYaHeiLight);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                currentPath = playVideo(goodBean);
            }
        }, 2000);
    }

    private void initRecyclerView() {
        detailBeans = new ArrayList<>();
        goodDetailAdapter = new GoodDetailAdapter(this, detailBeans);
        productDetail.setAdapter(goodDetailAdapter);

        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(1, 4, PagerGridLayoutManager.HORIZONTAL);
        productDetail.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(productDetail);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                queryOtherData();
            }
        }, 1000);
    }


    private void queryOtherData() {
        if (detailBeans.size() > 0) {
            detailBeans.clear();
        }
        Integer cat_id = goodBean.getCat_id();
        int good_id = goodBean.getGood_id();
        List<GoodBean> goodBeanList = GoodDAOUtil.queryAllGoodByCategoryId(cat_id);
        for (GoodBean bean : goodBeanList) {
            int good_id1 = bean.getGood_id();
            if (good_id1 != good_id) {
                detailBeans.add(bean);
            }
        }
        if (detailBeans != null && detailBeans.size() > 0) {
            goodDetailAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        goodBean = GsonUtils.convertString2Object(intent.getStringExtra("GOOD"), GoodBean.class);
        queryOtherData();
        showContent();
        currentPath = playVideo(goodBean);
    }

    protected String playVideo(GoodBean goodBean) {
        String currentPath;
        String path = goodBean.video_url;
        GoodBean bean = GoodDAOUtil.queryGoodData(path);
        if (bean != null && !TextUtils.isEmpty(bean.video_url_local)) {
            //使用SurfaceView播放视频
            myPresentation.play(bean.video_url_local);
            currentPath = bean.video_url_local;
        } else {
            //myPresentation为null
            if (myPresentation == null) {
                application.UpdatePresent();
            }
            myPresentation.play(path);
            currentPath = path;
        }
        return currentPath;
    }


    private void showContent() {
        String path;
        if (TextUtils.isEmpty(goodBean.image_url_local)) {
            path = goodBean.image_url;
        } else {
            path = goodBean.image_url_local;
        }
        Glide.with(this).asBitmap().load(path).into(detail_img);
        productDetailNameCh.setText(goodBean.name);
        productDetailNameEn.setText(goodBean.name_en);
        productDetailMarketPrice.setText(goodBean.market_price);
        productDetailICBCPrice.setText(goodBean.our_price);
        productDetailOriginal.setText(goodBean.origin);
        productDetailDesc.setText(goodBean.content);


        textViewPrice.setTitleWidth(textViewPrice);
        product_detail_price_normal1.setTitleWidth(textViewPrice);
        justifyTextViewName.setTitleWidth(textViewPrice);
        justifyTextViewOriginal.setTitleWidth(textViewPrice);
        justifyTextViewIntro.setTitleWidth(textViewPrice);
    }

    private void initEasyLayoutScroll() {

        List<BannerBean> bannerBeans = BannerDAOUtil.queryAllBanner();
        List<View> views = new ArrayList<>();
        for (BannerBean bannerBean : bannerBeans) {
            LinearLayout moreView = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.item_view_single, null);
            ImageView tv_img = moreView.findViewById(R.id.tv_img);
            Glide.with(this).asBitmap().load(bannerBean.image_url).into(tv_img);
            views.add(moreView);
        }
        //设置数据集
        easyLayoutScroll.setEasyViews(views);
        //开始滚动
        easyLayoutScroll.startScroll();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                QRCodeDialog qrCodeDialog = new QRCodeDialog(this, goodBean.ercode_img_url);
                qrCodeDialog.show();
                break;
            case R.id.back_container:
                onBackPressed();
                break;
            default:
                break;
        }
    }

    @Override
    public String onComplete(String videoPath) {
        return currentPath;
    }
}
