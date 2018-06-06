package xinshiyeweixin.cn.icbcdemo.db;

import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;
import xinshiyeweixin.cn.icbcdemo.bean.GoodBeanDao;

/**
 * @author: liubo
 * @date: 2018/6/6/006
 * @description: $description$
 */
public class GoodDAOUtil {
    private static final GoodBeanDao goodDAO = ICBCApplication.application.goodDaoSession.getGoodBeanDao();


    /**
     * 插入一条数据
     *
     * @param goodBean
     */
    public static void insertGood(GoodBean goodBean) {
        goodDAO.insertOrReplace(goodBean);
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

    public static List<GoodBean> queryAllGoodByCategoryId(int categoryId) {
        return goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Cat_id.eq(categoryId))
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

    public static void updateGood(String remoteUrl, String localPath) {
        GoodBean goodBean = goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Video_url.eq(remoteUrl))
                .build()
                .unique();
        goodBean.setVideo_url_local(localPath);
        goodDAO.update(goodBean);
    }

    public static void updateGoodImageLocalUrl(String remoteUrl, String localPath) {
        GoodBean goodBean = goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Image_url.eq(remoteUrl))
                .build()
                .unique();
        goodBean.setImage_url_local(localPath);
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

    public static GoodBean queryGoodData(String video_url) {
        return goodDAO.queryBuilder()
                .where(GoodBeanDao.Properties.Video_url.eq(video_url))
                .build()
                .unique();
    }

    public static boolean contains(GoodBean goodBean) {
        return goodDAO.hasKey(goodBean);
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
