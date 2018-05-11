package xinshiyeweixin.cn.icbcdemo.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Unique;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class CategoryBean {
    @Id(autoincrement = true)
    public Long id;
    public int cat_id;
    public String name;
    @Generated(hash = 1880569117)
    public CategoryBean(Long id, int cat_id, String name) {
        this.id = id;
        this.cat_id = cat_id;
        this.name = name;
    }
    @Generated(hash = 1870435730)
    public CategoryBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public int getCat_id() {
        return this.cat_id;
    }
    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
}
