package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Id;

public class GoodBean {
    //[
    // {"id":9,"name":"蓝宝石戒指","cat_id":8,"cat_name":"分类1","unit":"枚","market_price":6000,"our_price":5000,"content":"自古以来蓝宝石就有“帝王之石”之称。蓝宝石让几乎每一个时代的皇室被其吸引，并将之视为保佑圣物和典藏珍品。传说蓝宝石可让佩戴者免于遭人妒忌，并可蒙受神灵垂爱，于是古代国王就在颈间配戴蓝宝石，作为避免受伤的强力防御物。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a736b3ec3.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59b8a7599ca58.avi","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3100557295c.jpg","sale_url":"http://mall.icbc.com.cn/"}
    // ,{"id":10,"name":"钻石戒指","cat_id":8,"cat_name":"分类1","unit":"枚","market_price":8000,"our_price":7000,"content":"戒指源自古代太阳崇拜。古代戒指以玉石制成环状，象征太阳神日轮，认为它象太阳神一样，给人以温暖，庇护着人类的幸福和平安，同时也象征着美德与永恒，真理与信念。婚礼时，新郎戴金戒指，象征着火红的太阳；新娘戴银戒指，象征着皎洁的月亮。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a7cc50f37.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd698ec711.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a31010f32ffe.jpg","sale_url":"http://mall.icbc.com.cn/"}
    // ,{"id":11,"name":"金戒指","cat_id":9,"cat_name":"分类2","unit":"枚","market_price":5000,"our_price":4000,"content":"金戒指从古代到今朝是受到大众的欢迎的饰品。坚韧珍贵的金戒指纯洁华美，是恒久真情的绝佳载体，仅以这一对纯净的指环，把两个恋人连在一起，从今以后两个人的世界成为一种永恒！是爱情坚贞的一种信物！忠贞爱情的完美见证！","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a81f77669.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd645d5405.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a310117d0ace.jpg","sale_url":"http://mall.icbc.com.cn/"}
    // ,{"id":12,"name":"大钻戒","cat_id":9,"cat_name":"分类2","unit":"枚","market_price":10000,"our_price":8000,"content":"“南非之星”的英文名称为Star of South Africa，重47.55carats，无色，梨形琢刻形状，原产于南非，是一颗极优质的净水钻，原钻石重83.5carats。","image_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-09/goods_59b8a8700e62a.png","video_url":"http://3d.leygoo.cn/statics/data/upload/goods_video/2017-09/goods_59bcd6aabb7e8.mp4","ercode_img_url":"http://3d.leygoo.cn/statics/data/upload/goods_image/2017-12/goods_5a3101265d2db.jpg","sale_url":"http://mall.icbc.com.cn/"}]
    @Id(autoincrement = true)
    public Long id;
    public int good_id;
    public String name;
    public Integer cat_id;
    public String cat_name;
    public String unit;
    public int market_price;
    public int our_price;
    public String content;
    public String image_url;
    public String video_url;
    public String ercode_img_url;
    public String sale_url;

}
