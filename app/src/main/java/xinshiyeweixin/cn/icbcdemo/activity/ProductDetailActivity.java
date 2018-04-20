package xinshiyeweixin.cn.icbcdemo.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.DetailAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductInfoAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.DetailBean;

public class ProductDetailActivity extends AppCompatActivity {
    private TextView viewpager;
    private TextView productDetailNameCh;
    private TextView productDetailNameEn;
    private TextView productDetailPriceHigh;
    private TextView productDetailPriceNormal;
    private TextView productDetailOriginal;
    private TextView productDetailDesc;

    private RecyclerView productDetail;
    private ArrayList<DetailBean> detailBeans;
    private DetailAdapter detailAdapter;

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
        productDetail = (RecyclerView) findViewById(R.id.product_detail);
        viewpager = (TextView) findViewById(R.id.viewpager);
        productDetailNameCh = (TextView) findViewById(R.id.product_detail_name_ch);
        productDetailNameEn = (TextView) findViewById(R.id.product_detail_name_en);
        productDetailPriceHigh = (TextView) findViewById(R.id.product_detail_price_high);
        productDetailPriceNormal = (TextView) findViewById(R.id.product_detail_price_normal);
        productDetailOriginal = (TextView) findViewById(R.id.product_detail_original);
        productDetailDesc = (TextView) findViewById(R.id.product_detail_desc);

    }
}
