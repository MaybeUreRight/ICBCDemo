package xinshiyeweixin.cn.icbcdemo.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.lang.reflect.Type;

public class GsonUtils {
    static volatile Gson gson = GsonHolder.gson;

    /**
     * 对象转换为json
     *
     * @param object
     * @return
     */
    public static String convertToString(Object object) {
        return gson.toJson(object);
    }

    /**
     * JSON转换为对象1--普通类型
     *
     * @param json
     * @param classOfT
     * @param <T>
     * @return
     */
    public static <T> T convertJsonToObject(String json, Class<T> classOfT) {
        return gson.fromJson(json, classOfT);
    }

    /**
     * JSON转换为对象-针对泛型的类型
     *
     * @param json
     * @param typeOfT
     * @param <T>
     * @return
     */
    public static <T> T fromJson(String json, Type typeOfT) {
        return gson.fromJson(json, typeOfT);
    }

    private static class GsonHolder {
        private static final Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()//打开Export注解，但打开了这个注解,副作用，要转换和不转换都要加注解
//              .serializeNulls()  //是否序列化空值
                .setDateFormat("yyyy-MM-dd HH:mm:ss")//序列化日期格式  "yyyy-MM-dd"
//              .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)//会把字段首字母大写
                .setPrettyPrinting() //自动格式化换行
//              .setVersion(1.0)  //需要结合注解使用，有的字段在1。0的版本的时候解析，但0。1版本不解析
                .create();
    }

}
