package xinshiyeweixin.cn.icbcdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
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

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.DetailAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.DetailBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.utils.GsonUtils;
import xinshiyeweixin.cn.icbcdemo.view.JustifyTextView;
import xinshiyeweixin.cn.icbcdemo.view.QRCodeDialog;

public class GoodDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private EasyLayoutScroll easyLayoutScroll;
    private ImageView detail_img;
    private TextView productDetailNameCh;
    private TextView productDetailNameEn;
    private TextView productDetailMarketPrice;
    private TextView productDetailICBCPrice;
    private TextView productDetailOriginal;
    private TextView productDetailDesc;

    private TextView buy;

    private TextView textViewPrice;
    private JustifyTextView justifyTextViewName;
    private JustifyTextView justifyTextViewOriginal;
    private JustifyTextView justifyTextViewIntro;

    private RecyclerView productDetail;
    private ArrayList<DetailBean> detailBeans;
    private DetailAdapter detailAdapter;

    private QRCodeDialog qrCodeDialog;

    private GoodBean goodBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        initView();

        initRecyclerView();
    }

    private void initRecyclerView() {
        detailBeans = new ArrayList<>();
        createSomeData();
        detailAdapter = new DetailAdapter(this, detailBeans);
        productDetail.setAdapter(detailAdapter);

        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(1, 4, PagerGridLayoutManager.HORIZONTAL);
        productDetail.setLayoutManager(layoutManager);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(productDetail);
    }

    private void createSomeData() {
        for (int i = 0; i < 20; i++) {
            DetailBean detailBean = new DetailBean();
            detailBean.shortcut = getString(R.string.item_detail_shortcut) + " - > " + i;
            detailBean.description = getString(R.string.item_detail_desc) + " - > " + i;
            detailBean.picUrl = "";
            detailBeans.add(detailBean);
        }
    }

    private void initView() {
        goodBean = GsonUtils.convertString2Object(getIntent().getStringExtra("GOOD"),GoodBean.class);
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

        justifyTextViewName.setTitleWidth(textViewPrice);
        justifyTextViewOriginal.setTitleWidth(textViewPrice);
        justifyTextViewIntro.setTitleWidth(textViewPrice);

        initEasyLayoutScroll();

        Glide.with(this).asBitmap().load(goodBean.image_url).into(detail_img);
        productDetailNameCh.setText("" + goodBean.name);
        productDetailNameEn.setText("" + goodBean.name);
        productDetailMarketPrice.setText("" + goodBean.market_price);
        productDetailICBCPrice.setText("" + goodBean.our_price);
        productDetailOriginal.setText("" + goodBean.name);
        productDetailDesc.setText("" + goodBean.content);


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
        easyLayoutScroll.setEasyViews(views);
        //开始滚动
        easyLayoutScroll.startScroll();

        easyLayoutScroll.setOnItemClickListener(new EasyLayoutListener.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {
                Toast.makeText(GoodDetailActivity.this, "您点击了第" + pos + "条索引", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy:
                qrCodeDialog = new QRCodeDialog(this, goodBean.ercode_img_url);
                qrCodeDialog.show();
                break;
            case R.id.back_container:
                onBackPressed();
                break;
            default:
                break;
        }
    }
}