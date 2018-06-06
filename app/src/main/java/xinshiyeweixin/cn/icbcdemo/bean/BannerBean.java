package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author: liubo
 * @date: 2018/5/22/022
 * @description: $description$
 */
@Entity
public class BannerBean {
    @Id(autoincrement = true)
    public Long id;
    public int banner_id;
    public String title;
    public String image_url;
    public String image_url_local;
    @Generated(hash = 1840734079)
    public BannerBean(Long id, int banner_id, String title, String image_url,
            String image_url_local) {
        this.id = id;
        this.banner_id = banner_id;
        this.title = title;
        this.image_url = image_url;
        this.image_url_local = image_url_local;
    }
    @Generated(hash = 2832759)
    public BannerBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getBanner_id() {
        return this.banner_id;
    }
    public void setBanner_id(int banner_id) {
        this.banner_id = banner_id;
    }
    public String getTitle() {
        return this.title;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getImage_url() {
        return this.image_url;
    }
    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
    public String getImage_url_local() {
        return this.image_url_local;
    }
    public void setImage_url_local(String image_url_local) {
        this.image_url_local = image_url_local;
    }
}
