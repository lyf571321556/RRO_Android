package ai.ones.network.httpserver;

import android.content.Context;

import ai.ones.network.okhttp.OkHttpClientManager;
import ai.ones.network.request.BaseRequest;
import ai.ones.network.response.ProgressSubscriber;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Request;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpServiceImpl implements IHttpService {
    private Retrofit retrofit;

    public HttpServiceImpl(Context context, String baseUrl) {
        retrofit = new Retrofit.Builder()
                .client(OkHttpClientManager.getInstance(context).getOkHttpClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public void executeRequest(Request request) {

    }

    @Override
    public void asyExecuteRequest(BaseRequest request) {

        ProgressSubscriber subscriber = new ProgressSubscriber(request);
        Observable observable = request.getObservable(retrofit)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(request);
                /*失败后的retry配置*/
//                .retryWhen(new RetryWhenNetworkException(request.getRetryCount(),
//                        request.getRetryDelay(), request.getRetryIncreaseDelay()));
//        if (request.getLifecycleProvider() != null) {
//            observable.compose(request.getLifecycleProvider().bindUntilEvent(Lifecycle.Event.ON_DESTROY));
//        }
        /*生命周期管理*/
//                .compose(basePar.getRxAppCompatActivity().bindToLifecycle())
//                .compose(request.getRxAppCompatActivity().bindUntilEvent(ActivityEvent.PAUSE))
        /*http请求线程*/
//        observable.subscribeOn(Schedulers.io());
//        observable.unsubscribeOn(Schedulers.io());
//        /*回调线程*/
//        observable.observeOn(AndroidSchedulers.mainThread()).map(request);
        /*结果判断*/
//        observable.map(request);


//        /*链接式对象返回*/
//        SoftReference<HttpOnNextListener> httpOnNextListener = request.getListener();
//        if (httpOnNextListener != null && httpOnNextListener.get() != null) {
//            httpOnNextListener.get().onNext(observable);
//        }

        /*数据回调*/
        observable.subscribe(subscriber);
    }


}
