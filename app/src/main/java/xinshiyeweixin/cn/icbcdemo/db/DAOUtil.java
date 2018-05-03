package xinshiyeweixin.cn.icbcdemo.db;


import java.util.ArrayList;
import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.bean.Product;
import xinshiyeweixin.cn.icbcdemo.bean.ProductDao;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfo;
import xinshiyeweixin.cn.icbcdemo.bean.ProductInfoDao;

public class DAOUtil {
    private static final ProductDao productDao = ICBCApplication.application.productDaoSession.getProductDao();
    private static final ProductInfoDao productInfoDao = ICBCApplication.application.productInfoDaoSession.getProductInfoDao();

    /**
     * 插入一条数据
     *
     * @param product
     */
    public static void insertProduct(Product product) {
        productDao.insert(product);
    }

    /**
     * 删除某条数据
     *
     * @param id
     */
    public static void deleteProduct(long id) {
        productDao.deleteByKey(id);
    }

    /**
     * 更新某条数据
     *
     * @param id
     * @param name
     */
    public static void updateProduct(long id, String name) {
        Product product = productDao.queryBuilder()
                .where(ProductInfoDao.Properties.Id.eq(id))
                .build()
                .unique();
        product.setName(name);
        productDao.update(product);
    }

    public static List<ProductInfo> queryProductInfoData(String category, int limitCount) {
        return productInfoDao.queryBuilder()
                .where(ProductInfoDao.Properties.Cagetory.eq(category))
                .limit(limitCount)
                .orderAsc(ProductInfoDao.Properties.Cagetory)
                .list();
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<Product> queryAllProduct() {
        return productDao.queryBuilder()
                .orderAsc(ProductDao.Properties.Id)
                .build()
                .list();
    }


    /**
     * 插入一条数据
     *
     * @param productInfo
     */
    public static void insertProductInfo(ProductInfo productInfo) {
        productInfoDao.insert(productInfo);
    }

    /**
     * 删除某条数据
     *
     * @param id
     */
    public static void deleteProductInfo(long id) {
        productInfoDao.deleteByKey(id);
    }

    /**
     * 更新某条数据
     *
     * @param id
     * @param name
     */
    public static void updateProductInfo(long id, String name) {
        ProductInfo productInfo = productInfoDao.queryBuilder()
                .where(ProductDao.Properties.Id.eq(id))
                .build()
                .unique();
        productInfo.setCagetory(name);
        productInfoDao.update(productInfo);

    }


    public static List<Product> queryProductData(Long productInfoId, int limitCount) {
        return productDao.queryBuilder()
                .where(ProductDao.Properties.ProductInfoId.eq(productInfoId))
                .limit(limitCount)
                .orderDesc(ProductDao.Properties.Recommend)
                .list();
    }

    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<ProductInfo> queryAllProductInfo() {
        return productInfoDao.queryBuilder()
                .orderAsc(ProductInfoDao.Properties.Id)
                .build()
                .list();
    }
}
