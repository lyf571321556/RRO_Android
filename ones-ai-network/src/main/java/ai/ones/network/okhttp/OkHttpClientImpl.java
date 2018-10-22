package ai.ones.network.okhttp;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by administrator on 2018/10/22.
 */
public class OkHttpClientImpl implements IHttpClient {
    public final static int CONNECT_TIMEOUT =60;
    public final static int READ_TIMEOUT=100;
    public final static int WRITE_TIMEOUT=60;
    private static final String TAG = OkHttpClientImpl.class.getSimpleName();

    private okhttp3.OkHttpClient mHttpClient;

    private OkHttpClientImpl(final Builder okhttpDnsBuilder) {

        okhttp3.OkHttpClient.Builder builder = new okhttp3.OkHttpClient.Builder();
        /*
        builder.readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(WRITE_TIMEOUT,TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(CONNECT_TIMEOUT,TimeUnit.SECONDS);//设置连接超时时间
                */
        // add application interceptor
        if(okhttpDnsBuilder!=null) {
            for (Interceptor interceptor : okhttpDnsBuilder.getInterceptors()) {
                builder.addInterceptor(interceptor);
            }
            // add network interceptor
            for (Interceptor interceptor : okhttpDnsBuilder.getNetworkInterceptors()) {
                builder.addNetworkInterceptor(interceptor);
            }

            if (okhttpDnsBuilder.getIHttpDns() != null) {
                builder.dns(new Dns() {
                    @Override
                    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
                        return okhttpDnsBuilder.getIHttpDns().lookup(hostname);
                    }
                });
            }
        }
        mHttpClient = builder.build();
    }

    @Override
    public Response executeRequest(Request request) throws IOException {
        return  mHttpClient.newCall(request).execute();
    }

    @Override
    public void asyExecuteRequest(Request request, Callback callback) {
        mHttpClient.newCall(request).enqueue(callback);
    }

    @Override
    public OkHttpClient getOkHttpClient() {
        return mHttpClient;
    }


    public static final class Builder {
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<Interceptor> networkInterceptors = new ArrayList<>();
        private IHttpDns IHttpDns;
        public Builder() {
        }

        public void setIHttpDns(IHttpDns IHttpDns) {
            this.IHttpDns = IHttpDns;
        }

        public IHttpDns getIHttpDns() {
            return IHttpDns;
        }

        public List<Interceptor> getInterceptors() {
            return interceptors;
        }

        public List<Interceptor> getNetworkInterceptors() {
            return networkInterceptors;
        }
        public OkHttpClientImpl build() {
            return  new OkHttpClientImpl(this);
        }
    }
}
