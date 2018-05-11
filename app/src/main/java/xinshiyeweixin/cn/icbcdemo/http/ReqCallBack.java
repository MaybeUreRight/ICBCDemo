package xinshiyeweixin.cn.icbcdemo.http;

import xinshiyeweixin.cn.icbcdemo.bean.FailBean;

public interface ReqCallBack<T> {
    /**
     * 响应成功
     */
    void onReqSuccess(T result);

    /**
     * 响应失败
     */
    void onReqFailed(FailBean failBean);
}
