package xinshiyeweixin.cn.icbcdemo.http;

/**
 * @author: liubo
 * @date: 2018/5/11/011
 * @description: $description$
 */
public interface MyCallBack<T> {

    /**
     * 响应成功
     */
    void onReqSuccess(T result, String tag);

    /**
     * 响应失败
     */
    void onReqFailed(String errorMsg, String tag);
}
