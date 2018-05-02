package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.ToOne;

@Entity
public class Product {
    @Id(autoincrement = true)
    private Long id;
    private String name;
    private String picUrl;
    private String videoPath;
    private String introduction;
    private boolean recommend;
    private Long productInfoId;
    @Generated(hash = 258961792)
    public Product(Long id, String name, String picUrl, String videoPath,
            String introduction, boolean recommend, Long productInfoId) {
        this.id = id;
        this.name = name;
        this.picUrl = picUrl;
        this.videoPath = videoPath;
        this.introduction = introduction;
        this.recommend = recommend;
        this.productInfoId = productInfoId;
    }
    @Generated(hash = 1890278724)
    public Product() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getPicUrl() {
        return this.picUrl;
    }
    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
    public String getVideoPath() {
        return this.videoPath;
    }
    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }
    public String getIntroduction() {
        return this.introduction;
    }
    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }
    public boolean getRecommend() {
        return this.recommend;
    }
    public void setRecommend(boolean recommend) {
        this.recommend = recommend;
    }
    public Long getProductInfoId() {
        return this.productInfoId;
    }
    public void setProductInfoId(Long productInfoId) {
        this.productInfoId = productInfoId;
    }

//    @Override
//    public boolean equals(Object obj) {
//        Product product = (Product) obj;
//        return this.name.equals(product.name)
//                && this.picUrl.equals(product.picUrl)
//                && this.videoPath.equals(product.videoPath)
//                && this.introduction.equals(product.introduction)
//                && this.recommend == product.recommend
//                && this.productInfoId.equals(product.productInfoId)
//                ;
//    }


}
