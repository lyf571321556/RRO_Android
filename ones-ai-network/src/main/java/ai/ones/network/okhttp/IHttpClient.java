package ai.ones.network.okhttp;


import java.io.IOException;

import okhttp3.Callback;

/**
 * Created by administrator on 2016/10/22.
 */
public interface IHttpClient {

    /**
     *执行同步请求
     * @param request
     * @return
     * @throws IOException
     */
    okhttp3.Response executeRequest(okhttp3.Request request) throws IOException;

    /**
     * 执行异同步请求
     * @param request
     * @param callback
     */
    void asyExecuteRequest(okhttp3.Request request, Callback callback);


    okhttp3.OkHttpClient getOkHttpClient();

}
