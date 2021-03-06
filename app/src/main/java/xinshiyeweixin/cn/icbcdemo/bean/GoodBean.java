package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Unique;

@Entity
public class GoodBean {
   // [
   // {"id":11,"name":"金戒指","name_en":"jinjiezhi","origin":"中国","cat_id":9,"cat_name":"分类2","unit":"枚","market_price":5000,"our_price":4000,"content":"金戒指从古代到今朝是受到大众的欢迎的饰品。坚韧珍贵的金戒指纯洁华美，是恒久真情的绝佳载体，仅以这一对纯净的指环，把两个恋人连在一起，从今以后两个人的世界成为一种永恒！是爱情坚贞的一种信物！忠贞爱情的完美见证！","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a81f77669.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd645d5405.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a310117d0ace.jpg","sale_url":"http://mall.icbc.com.cn/"}
   // ,{"id":12,"name":"大钻戒","name_en":"big-zuanjie","origin":"欧洲","cat_id":9,"cat_name":"分类2","unit":"枚","market_price":10000,"our_price":8000,"content":"“南非之星”的英文名称为Star of South Africa，重47.55carats，无色，梨形琢刻形状，原产于南非，是一颗极优质的净水钻，原钻石重83.5carats。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a8700e62a.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd6aabb7e8.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3101265d2db.jpg","sale_url":"http://mall.icbc.com.cn/"}]
    @Id(autoincrement = true)
    public Long id;
    @Unique
    public int good_id;
    public String name;
    public String name_en;
    public String origin;
    public Integer cat_id;
    public String cat_name;
    public String unit;
    public int market_price;
    public int our_price;
    public String content;
    public String image_url;
    public String image_url_local;
    public String video_url;
    public String video_url_local;
    public String ercode_img_url;
    public String sale_url;

    @Generated(hash = 1940294357)
    public GoodBean(Long id, int good_id, String name, String name_en, String origin, Integer cat_id, String cat_name, String unit, int market_price, int our_price, String content, String image_url, String image_url_local, String video_url, String video_url_local, String ercode_img_url, String sale_url) {
        this.id = id;
        this.good_id = good_id;
        this.name = name;
        this.name_en = name_en;
        this.origin = origin;
        this.cat_id = cat_id;
        this.cat_name = cat_name;
        this.unit = unit;
        this.market_price = market_price;
        this.our_price = our_price;
        this.content = content;
        this.image_url = image_url;
        this.image_url_local = image_url_local;
        this.video_url = video_url;
        this.video_url_local = video_url_local;
        this.ercode_img_url = ercode_img_url;
        this.sale_url = sale_url;
    }

    @Generated(hash = 1348485518)
    public GoodBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getGood_id() {
        return this.good_id;
    }

    public void setGood_id(int good_id) {
        this.good_id = good_id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getCat_id() {
        return this.cat_id;
    }

    public void setCat_id(Integer cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_name() {
        return this.cat_name;
    }

    public void setCat_name(String cat_name) {
        this.cat_name = cat_name;
    }

    public String getUnit() {
        return this.unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getMarket_price() {
        return this.market_price;
    }

    public void setMarket_price(int market_price) {
        this.market_price = market_price;
    }

    public int getOur_price() {
        return this.our_price;
    }

    public void setOur_price(int our_price) {
        this.our_price = our_price;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getImage_url() {
        return this.image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getVideo_url() {
        return this.video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getErcode_img_url() {
        return this.ercode_img_url;
    }

    public void setErcode_img_url(String ercode_img_url) {
        this.ercode_img_url = ercode_img_url;
    }

    public String getSale_url() {
        return this.sale_url;
    }

    public void setSale_url(String sale_url) {
        this.sale_url = sale_url;
    }

    public String getVideo_url_local() {
        return this.video_url_local;
    }

    public void setVideo_url_local(String video_url_local) {
        this.video_url_local = video_url_local;
    }

    public String getName_en() {
        return this.name_en;
    }

    public void setName_en(String name_en) {
        this.name_en = name_en;
    }

    public String getOrigin() {
        return this.origin;
    }

    public void setOrigin(String origin) {
        this.origin = origin;
    }

    public String getImage_url_local() {
        return this.image_url_local;
    }

    public void setImage_url_local(String image_url_local) {
        this.image_url_local = image_url_local;
    }

}
