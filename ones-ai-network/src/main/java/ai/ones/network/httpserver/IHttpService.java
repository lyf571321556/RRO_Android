package ai.ones.network.httpserver;


import ai.ones.network.request.BaseRequest;
import okhttp3.Request;
import retrofit2.Call;

public interface IHttpService {


    /**
     * 执行同步请求
     *
     * @param request
     * @return
     */
    void executeRequest(okhttp3.Request request);

    /**
     * 执行异同步请求
     *
     * @param request
     */
    void asyExecuteRequest(BaseRequest request);
}
