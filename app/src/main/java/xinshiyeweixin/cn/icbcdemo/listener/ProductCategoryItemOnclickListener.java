package xinshiyeweixin.cn.icbcdemo.listener;

import java.util.List;

import xinshiyeweixin.cn.icbcdemo.bean.Product;

public interface ProductCategoryItemOnclickListener {
    /**
     * 商品分类Item的点击事件
     *
     * @param productList
     * @param position
     */
    void onProductCategoryItemOnclick(List<Product> productList, int position);
}
