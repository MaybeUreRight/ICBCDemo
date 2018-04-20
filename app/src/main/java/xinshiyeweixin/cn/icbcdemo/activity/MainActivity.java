package xinshiyeweixin.cn.icbcdemo.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.gcssloop.widget.PagerGridLayoutManager;
import com.gcssloop.widget.PagerGridSnapHelper;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.R;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductAdapter;
import xinshiyeweixin.cn.icbcdemo.adapter.ProductInfoAdapter;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.listener.ProductCategoryItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.listener.ProductItemOnclickListener;
import xinshiyeweixin.cn.icbcdemo.utils.MyPresentation;

public class MainActivity extends AppCompatActivity implements ProductItemOnclickListener, ProductCategoryItemOnclickListener {

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
        productInfos = new ArrayList<>();
        products = new ArrayList<>();

        initData(productInfos);
        product_cagetory = findViewById(R.id.product_category);
        product_list = findViewById(R.id.product_list);

        productCategoryAdapter = new ProductInfoAdapter(this, productInfos);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        product_cagetory.setLayoutManager(linearLayoutManager);
        product_cagetory.setAdapter(productCategoryAdapter);

        // 1.水平分页布局管理器
        PagerGridLayoutManager layoutManager = new PagerGridLayoutManager(2, 5, PagerGridLayoutManager.HORIZONTAL);
        product_list.setLayoutManager(layoutManager);

        products.addAll(productInfos.get(0).productList);
        productAdapter = new ProductAdapter(this, products);
        product_list.setAdapter(productAdapter);

        // 2.设置滚动辅助工具
        PagerGridSnapHelper pageSnapHelper = new PagerGridSnapHelper();
        pageSnapHelper.attachToRecyclerView(product_list);


//        icbcApplication = (ICBCApplication) getApplication();
//        myPresentation = icbcApplication.getPresentation();
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
    }
}
