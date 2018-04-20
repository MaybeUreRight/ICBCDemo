package xinshiyeweixin.cn.icbcdemo.listener;

import java.util.ArrayList;

import xinshiyeweixin.cn.icbcdemo.bean.Product;

public interface ProductCategoryItemOnclickListener {
    /**
     * 商品分类Item的点击事件
     *
     * @param productList
     * @param position
     */
    void onProductCategoryItemOnclick(ArrayList<Product> productList, int position);
}
