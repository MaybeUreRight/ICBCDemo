package xinshiyeweixin.cn.icbcdemo.listener;

import xinshiyeweixin.cn.icbcdemo.bean.GoodBean;

public interface GoodItemOnclickListener {
    /**
     * 播放商品Item的视频
     *
     * @param videoPath
     */
    void playItemVideo(String videoPath);

    void onGoodItemClick(GoodBean goodBean);
}
