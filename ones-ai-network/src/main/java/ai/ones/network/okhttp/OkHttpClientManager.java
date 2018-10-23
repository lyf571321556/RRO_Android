package ai.ones.network.okhttp;


import android.content.Context;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import ai.ones.network.debug.StethoManager;
import okhttp3.Callback;
import okhttp3.Interceptor;


public class OkHttpClientManager {

    private static OkHttpClientManager OkHttpClientManager;
    private IHttpClient mHttpClient;


    public static OkHttpClientManager getInstance(Context context) {
        return getInstance(new Builder(context));
    }

    public static OkHttpClientManager getInstance(Builder builder) {
        if (OkHttpClientManager == null) {
            synchronized (OkHttpClientManager.class) {
                if (OkHttpClientManager == null) {
                    OkHttpClientManager = builder.build();
                }
            }
        }
        return OkHttpClientManager;
    }


    private OkHttpClientManager(Builder okHttpClientManager) {
        OkHttpClientImpl.Builder okHttpClientBuilder = new OkHttpClientImpl.Builder();
        List<Interceptor> interceptors = new ArrayList<>();
        List<Interceptor> networkInterceptors = new ArrayList<>();
        /**
         * 添加httpdns
         */
        OkHttpDnsImpl.Builder okHttpDnsBuilder = null;
        if (okHttpClientManager != null) {
            okHttpDnsBuilder = new OkHttpDnsImpl.Builder();
            //添加自定义拦截器
            interceptors.addAll(okHttpClientManager.getOtherInterceptors());
            if (okHttpClientManager.getCacheDnsDir() != null) {
                okHttpDnsBuilder.setCacheDnsDir(okHttpClientManager.getCacheDnsDir());
                okHttpDnsBuilder.setHttpDnsUrl(okHttpClientManager.getHttpDnsUrl());
                okHttpDnsBuilder.getInterceptors().addAll(okHttpClientManager
                        .getOtherInterceptors());
            }

            if (StethoManager.getStethoInterceptor() != null) {
                okHttpDnsBuilder.addNetworkInterceptor(StethoManager.getStethoInterceptor());
            }

            if (StethoManager.getHttpLoggingInterceptor() != null) {
                okHttpDnsBuilder.addInterceptor(StethoManager.getHttpLoggingInterceptor());
            }
            //okHttpClientBuilder.setIHttpDns(okHttpDnsBuilder.build());
        }


        StethoManager.initStetho(okHttpClientManager.mContext);
        if (StethoManager.getStethoInterceptor() != null) {
            networkInterceptors.add(StethoManager.getStethoInterceptor());
        }

        if (StethoManager.getHttpLoggingInterceptor() != null) {
            interceptors.add(StethoManager.getHttpLoggingInterceptor());
        }
        okHttpClientBuilder.getInterceptors().addAll(interceptors);
        okHttpClientBuilder.getNetworkInterceptors().addAll(networkInterceptors);
        mHttpClient = okHttpClientBuilder.build();
    }


    /**
     * 执行同步请求
     */
    public okhttp3.Response executeRequest(okhttp3.Request request) throws IOException {
        return mHttpClient.executeRequest(request);
    }

    /**
     * 执行异步请求
     */
    public void asyExecuteRequest(okhttp3.Request request, Callback callback) {
        mHttpClient.asyExecuteRequest(request, callback);
    }


    public okhttp3.OkHttpClient getOkHttpClient() {
        return mHttpClient.getOkHttpClient();
    }


    public static final class Builder {

        private List<Interceptor> otherInterceptors = new ArrayList<>();
        private String httpDnsUrl;
        private File cacheDnsDir;
        private Context mContext;
        private boolean enableDebug;

        public Builder(Context context) {
            if (context == null)
                throw new RuntimeException("context must not null");
            this.mContext = context;
        }

        /**
         * 是否开启Stetho调试
         */
        public Builder enableDebug(boolean enableDebug) {
            this.enableDebug = enableDebug;
            return this;
        }

        public boolean isEnableDebug() {
            return enableDebug;
        }

        /**
         * 不为空开启
         */
        public Builder setHttpDnsUrl(String httpDnsUrl) {
            this.httpDnsUrl = httpDnsUrl;
            if (httpDnsUrl != null) {//need review code
                cacheDnsDir = new File(mContext.getExternalCacheDir(), "ai_ones_httpdns");
            }
            return this;
        }

        public File getCacheDnsDir() {
            return cacheDnsDir;
        }

        public String getHttpDnsUrl() {
            return httpDnsUrl;
        }

        /**
         * 添加自定义拦截器
         */
        public Builder addInterceptor(Interceptor interceptor) {
            otherInterceptors.add(interceptor);
            return this;
        }

        public List<Interceptor> getOtherInterceptors() {
            return otherInterceptors;
        }

        public OkHttpClientManager build() {
            return new OkHttpClientManager(this);
        }
    }
}
