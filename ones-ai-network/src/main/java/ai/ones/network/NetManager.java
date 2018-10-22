package ai.ones.network;

import android.content.Context;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ai.ones.network.httpserver.HttpServiceImpl;
import ai.ones.network.httpserver.IHttpService;
import ai.ones.network.request.BaseRequest;
import okhttp3.Interceptor;

/**
 * Created by lyf on 2018/10/22.
 */
public final class NetManager {

    private static NetManager sInstance;

    private IHttpService mHttpService;


    public static Builder newInitializerBuilder(Context context) {
        return new NetManager.Builder(context);
    }

    public static void init(Builder builder) {
        getInstance(builder);
    }

    public static NetManager getInstance(Context context) {
        if (sInstance == null) {
            synchronized (NetManager.class) {
                if (sInstance == null) {
                    sInstance = getInstance(new Builder(context));
                }
            }
        }

        return sInstance;
    }

    private static NetManager getInstance(Builder builder) {
        if (sInstance == null) {
            synchronized (NetManager.class) {
                if (sInstance == null) {
                    sInstance = builder.build();
                }
            }
        }

        return sInstance;
    }

    private NetManager(Builder builder) {
        mHttpService = new HttpServiceImpl(builder.mContext, builder.baseUrl);
    }


    public void executeRequest() {

    }

    /**
     * 异步执行
     *
     * @param request
     */
    public void asyExecuteRequest(BaseRequest request) {
        mHttpService.asyExecuteRequest(request);
    }

    public static final class Builder {
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<Interceptor> networkInterceptors = new ArrayList<>();
        private File cacheDnsDir;
        private String httpDnsUrl;
        private Context mContext;
        private String baseUrl;

        public Builder(Context context) {
            if (context == null)
                throw new RuntimeException("context must not null");
            this.mContext = context;
        }

        public void setCacheDnsDir(File cacheDnsDir) {
            this.cacheDnsDir = cacheDnsDir;
        }

        public File getCacheDnsDir() {
            return cacheDnsDir;
        }

        public void setHttpDnsUrl(String httpDnsUrl) {
            this.httpDnsUrl = httpDnsUrl;
        }

        public Builder addInterceptor(Interceptor interceptor) {
            interceptors.add(interceptor);
            return this;
        }

        public List<Interceptor> getInterceptors() {
            return interceptors;
        }

        public Builder addNetworkInterceptor(Interceptor interceptor) {
            networkInterceptors.add(interceptor);
            return this;
        }

        public String getHttpDnsUrl() {
            return httpDnsUrl;
        }

        public Builder setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
            return this;
        }

        public String getBaseUrl() {
            return baseUrl;
        }

        public NetManager build() {
            if (this.mContext == null)
                throw new RuntimeException("context must not null");
            if (this.baseUrl == null)
                throw new RuntimeException("baseUrl must not null");
            return new NetManager(this);
        }
    }
}
