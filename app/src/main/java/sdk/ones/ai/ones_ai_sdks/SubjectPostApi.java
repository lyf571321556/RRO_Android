package sdk.ones.ai.ones_ai_sdks;

import ai.ones.network.htttp.listener.HttpOnNextListener;
import ai.ones.network.request.BaseRequest;
import io.reactivex.Observable;
import retrofit2.Retrofit;

/**
 * 测试数据
 * Created by WZG on 2016/7/16.
 */
public class SubjectPostApi extends BaseRequest {
    //    接口需要传入的参数 可自定义不同类型
    private boolean all;
    /*任何你先要传递的参数*/
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;
//    String xxxxx;


    /**
     * 默认初始化需要给定回调和rx周期类
     * 可以额外设置请求设置加载框显示，回调等（可扩展）
     *
     * @param listener
     */
    public SubjectPostApi(HttpOnNextListener listener) {
        super(listener);
        setShowProgress(true);
        setCancel(true);
        setCache(false);
        setMethod("AppFiftyToneGraph/videoLink11");
        setCookieNetWorkTime(60);
        setCookieNoNetWorkTime(24 * 60 * 60);
    }


    public boolean isAll() {
        return all;
    }

    public void setAll(boolean all) {
        this.all = all;
    }

    @Override
    public Observable getObservable(Retrofit retrofit) {
        HttpPostService service = retrofit.create(HttpPostService.class);
        return service.getAllVedioBys(isAll());
    }
}
