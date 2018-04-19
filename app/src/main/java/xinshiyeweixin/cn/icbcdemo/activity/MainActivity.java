package xinshiyeweixin.cn.icbcdemo.activity;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductInfoAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.view.HorizontalPageLayoutManager;
import xinshiyeweixin.cn.icbcdemo.view.PagingItemDecoration;
import xinshiyeweixin.cn.icbcdemo.view.PagingScrollHelper;

public class MainActivity extends FragmentActivity implements ProductInfoAdapter.OnRecycleViewItemClickListener, PagingScrollHelper.onPageChangeListener {

    //    private IndicatorViewPager indicatorViewPager;
    private RecyclerView product_cagetory;
    private RecyclerView product_list;

    private PagingScrollHelper scrollHelper;
    private HorizontalPageLayoutManager horizontalPageLayoutManager;
    private PagingItemDecoration pagingItemDecoration;
    private ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
//        initViewPager();


        ArrayList<ProductInfo> list = new ArrayList<>();
        initData(list);

        product_cagetory = findViewById(R.id.product_category);

        product_cagetory.setHasFixedSize(true);//设置固定大小
        product_cagetory.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        LinearLayoutManager mLayoutManage = new LinearLayoutManager(this);
        mLayoutManage.setOrientation(OrientationHelper.HORIZONTAL);//设置滚动方向，横向滚动
        product_cagetory.setLayoutManager(mLayoutManage);
        ProductInfoAdapter adapter = new ProductInfoAdapter(this, R.layout.item_category, list, this);
        product_cagetory.setAdapter(adapter);

        product_list = findViewById(R.id.product_list);
         productAdapter = new ProductAdapter(this, R.layout.item_product, list.get(0).productList);
        product_list.setAdapter(productAdapter);

         scrollHelper = new PagingScrollHelper();

        scrollHelper.setUpRecycleView(product_list);
        scrollHelper.setOnPageChangeListener(this);

         horizontalPageLayoutManager = new HorizontalPageLayoutManager(2, 5);
         pagingItemDecoration = new PagingItemDecoration(this, horizontalPageLayoutManager);

        product_list.setLayoutManager(horizontalPageLayoutManager);
        product_list.addItemDecoration(pagingItemDecoration);
        scrollHelper.updateLayoutManger();
        scrollHelper.scrollToPosition(0);
    }

    private void initData(ArrayList<ProductInfo> list) {
        for (int i = 0; i < 10; i++) {
            ProductInfo info = new ProductInfo();
            info.cagetory = getString(R.string.item_product_category) + " -> " + i;
            info.productList = new ArrayList<>();
            for (int j = 0; j < 23; j++) {
                Product product = new Product();
                product.name = getString(R.string.item_product_name) + " -> " + j;
                product.picUrl = "https://i03piccdn.sogoucdn.com/66766b011ffe1eac";
                product.introduction = "秋田犬（拉丁学名：Japanese Akita），别名日本秋田犬、日系秋田犬，原产地日本。其祖先被称呼为山地狩猎犬，是大型的熊猎犬。除了协助猎熊外，它还被利用来捕...";
                info.productList.add(product);
            }
            list.add(info);
        }
    }

    @Override
    public void onItemClick(ArrayList<Product> list, int position) {
        product_list.setHasFixedSize(true);//设置固定大小
        product_list.setItemAnimator(new DefaultItemAnimator());//设置默认动画
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this, 2);
        gridLayoutManager.setOrientation(OrientationHelper.HORIZONTAL);
        product_list.setLayoutManager(gridLayoutManager);
        ProductAdapter adapter = new ProductAdapter(MainActivity.this, R.layout.item_product, list);
        product_list.setAdapter(adapter);
    }

    @Override
    public void onPageChange(int index) {

    }
}
