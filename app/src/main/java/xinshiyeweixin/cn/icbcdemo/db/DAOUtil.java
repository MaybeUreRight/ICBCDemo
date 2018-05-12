package xinshiyeweixin.cn.icbcdemo.db;


import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBean;
import xinshiyeweixin.cn.icbcdemo.bean.CategoryBeanDao;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBeanDao;

public class DAOUtil {
    private static final CategoryBeanDao categoryDAO = ICBCApplication.application.categoryDaoSession.getCategoryBeanDao();
    private static final GoodBeanDao goodDAO = ICBCApplication.application.goodDaoSession.getGoodBeanDao();


    /**
     * 插入一条数据
     *
     * @param categoryBean
     */
    public static void insertCategory(CategoryBean categoryBean) {
        categoryDAO.insert(categoryBean);
    }

    /**
     * 插入一条数据
     *
     * @param goodBean
     */
    public static void insertGood(GoodBean goodBean) {
        goodDAO.insert(goodBean);
    }


    /**
     * 更新某条数据
     *
     * @param cat_id
     * @param name
     */
    public static void updateCategory(int cat_id, String name) {
        CategoryBean categoryBean = categoryDAO.queryBuilder()
                .where(CategoryBeanDao.Properties.Cat_id.eq(cat_id))
                .build()
                .unique();
        categoryBean.setName(name);
        categoryDAO.update(categoryBean);
    }

    /**
     * 查询
     *
     * @param cat_id
     * @param limitCount
     * @return
     */
    public static List<CategoryBean> queryCategoryData(int cat_id, int limitCount) {
        return categoryDAO.queryBuilder()
                .where(CategoryBeanDao.Properties.Cat_id.eq(cat_id))
                .limit(limitCount)
                .orderAsc(CategoryBeanDao.Properties.Id)
                .list();
    }


    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<CategoryBean> queryAllCategory() {
        return categoryDAO.queryBuilder()
                .orderAsc(CategoryBeanDao.Properties.Id)
                .build()
                .list();
    }


    /**
     * 查询所有数据
     *
     * @return
     */
    public static List<GoodBean> queryAllGood() {
        return goodDAO.queryBuilder()
                .orderAsc(GoodBeanDao.Properties.Id)
                .build()
                .list();
    }


    /**
     * 更新某条数据
     *
     * @param cat_id
     * @param name
     */
    public static void updateGood(int cat_id, String name) {
        GoodBean goodBean = goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Cat_id.eq(cat_id))
                .build()
                .unique();
        goodBean.setName(name);
        goodDAO.update(goodBean);
    }

    /**
     * 查询
     *
     * @param cat_id //     * @param limitCount
     * @return
     */
    public static List<GoodBean> queryGoodData(int cat_id) {
        return goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Cat_id.eq(cat_id))
//                .limit(limitCount)
                .orderAsc(GoodBeanDao.Properties.Id)
                .list();
    }


    /**
     * 删除某条数据
     *
     * @param cat_id
     */
    public static void deleteCategory(long cat_id) {
        categoryDAO.deleteByKey(cat_id);
    }


    /**
     * 删除某条数据
     *
     * @param good_id
     */
    public static void deleteGoodBean(long good_id) {
        goodDAO.deleteByKey(good_id);
    }

}
