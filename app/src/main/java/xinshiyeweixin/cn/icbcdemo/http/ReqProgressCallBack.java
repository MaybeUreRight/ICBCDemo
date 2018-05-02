package xinshiyeweixin.cn.icbcdemo.http;

public interface ReqProgressCallBack<T> extends ReqCallBack<T> {
    /**
     * 响应进度更新
     */
    void onProgress(long total, long current);
}
