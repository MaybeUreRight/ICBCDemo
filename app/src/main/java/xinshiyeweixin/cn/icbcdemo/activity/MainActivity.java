package xinshiyeweixin.cn.icbcdemo.activity;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutListener;
import com.layoutscroll.layoutscrollcontrols.view.EasyLayoutScroll;

import java.util.ArrayList;
import java.util.List;

import jp.wasabeef.recyclerview.animators.FlipInLeftYAnimator;
import jp.wasabeef.recyclerview.animators.LandingAnimator;
import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.GridSpacingItemDecoration;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductInfoAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.listener.ProductCategoryItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.listener.ProductItemOnclickListener;
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
        product_list.addItemDecoration(new MyItemDecoration(2,20,true));

        products.addAll(productInfos.get(0).productList);
        productAdapter = new ProductAdapter(this, products);
        product_list.setAdapter(productAdapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(product_list);


//        icbcApplication = (ICBCApplication) getApplication();
//        myPresentation = icbcApplication.getPresentation();
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
        for (int i = 0; i < 10; i++) {
            ProductInfo info = new ProductInfo();
            info.cagetory = getString(R.string.item_product_category) + " -> " + i;
            info.productList = new ArrayList<>();
            for (int j = 0; j < 13; j++) {
                Product product = new Product();
                product.name = getString(R.string.item_product_name) + " -> " + j;
                product.picUrl = "https://i03piccdn.sogoucdn.com/66766b011ffe1eac";
                product.introduction = "秋田犬（拉丁学名：Japanese Akita），别名日本秋田犬、日系秋田犬，原产地日本。其祖先被称呼为山地狩猎犬，是大型的熊猎犬。除了协助猎熊外，它还被利用来捕...";
                if (5 - i > 0) {
                    product.recommend=true;
                }else{
                    product.recommend=false;
                }
                info.productList.add(product);
            }
            list.add(info);
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
    public void onProductCategoryItemOnclick(ArrayList<Product> productList, int position) {
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
