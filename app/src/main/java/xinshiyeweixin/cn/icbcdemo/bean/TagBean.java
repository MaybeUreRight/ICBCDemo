package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TagBean {
    //[{"id":1,"name":"热卖商品"},{"id":2,"name":"人气飙升"}]
    public int id;
    public String name;
    @Generated(hash = 852672054)
    public TagBean(int id, String name) {
        this.id = id;
        this.name = name;
    }
    @Generated(hash = 814153312)
    public TagBean() {
    }
    public int getId() {
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
