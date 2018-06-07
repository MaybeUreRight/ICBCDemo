package xinshiyeweixin.cn.icbcdemo.db;


import java.util.List;

import xinshiyeweixin.cn.icbcdemo.ICBCApplication;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBean;
import xinshiyeweixin.cn.icbcdemo.bean.BannerBeanDao;

public class BannerDAOUtil {
    private static final BannerBeanDao bannerDAO = ICBCApplication.application.bannerDaoSession.getBannerBeanDao();

    public static void insertBanner(BannerBean bannerBean) {
        bannerDAO.insertOrReplace(bannerBean);
    }

    public static List<BannerBean> queryAllBanner() {
        return bannerDAO.queryBuilder()
                .orderAsc(BannerBeanDao.Properties.Banner_id)
                .build()
                .list();
    }

    public static void updateBannerImageLocalUrl(String image_url, String path) {
        BannerBean bannerBean = bannerDAO.queryBuilder()
                .where(BannerBeanDao.Properties.Image_url.eq(image_url))
                .build()
                .unique();
        bannerBean.setImage_url_local(path);
        bannerDAO.update(bannerBean);
    }
}
