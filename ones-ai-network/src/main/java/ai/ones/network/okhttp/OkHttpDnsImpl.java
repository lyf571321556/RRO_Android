package ai.ones.network.okhttp;



import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Cache;
import okhttp3.Dns;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by administrator on 2018/10/22.
 *
 * 1.OkHttp内部默认使用Dns.SYSTEM做域名解析
 * 2.Android系统对DNS结果有做缓存处理
 * https://developer.android.com/reference/java/net/InetAddress.html
 */
public class OkHttpDnsImpl implements IHttpDns {

    private static final String TAG = OkHttpDnsImpl.class.getSimpleName();

    private OkHttpClient mHttpDnsClient;

    private String httpDnsUrl;
    private OkHttpDnsImpl(Builder builder) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();

        for (Interceptor i : builder.interceptors) {
            clientBuilder.addInterceptor(i);
        }

        for (Interceptor i : builder.networkInterceptors) {
            clientBuilder.addNetworkInterceptor(i);
        }

        clientBuilder.addNetworkInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Response originalResponse = chain.proceed(chain.request());
                if (!originalResponse.isSuccessful()) {
                    return originalResponse;
                }

                return originalResponse.newBuilder()
                        .header("Cache-Control", "max-age=600") // 10 minutes
                        .build();
            }
        });

        clientBuilder.cache(new Cache(builder.getCacheDnsDir(), 1 * 1024 * 1024)); // 1MB cache
        httpDnsUrl=builder.getHttpDnsUrl();
        mHttpDnsClient = clientBuilder.build();
    }

    @Override
    public List<InetAddress> lookup(String hostname) throws UnknownHostException {
        if (hostname == null) {
            throw new UnknownHostException("hostname == null");
        }

        if (checkIpAddress(hostname) || !hostname.endsWith("ones domain")) {
            Log.d(TAG,"Use system dns lookup! (" + hostname + ")");
            return Dns.SYSTEM.lookup(hostname);
        }

        Request.Builder reqBuilder = new Request.Builder();
        reqBuilder.url(httpDnsUrl);
        Response resp = null;
        try {
            resp = mHttpDnsClient.newCall(reqBuilder.build()).execute();
            if (resp == null || !resp.isSuccessful()) {
                Log.e(TAG,"HttpDns request failure, use system dns lookup!");
                return Dns.SYSTEM.lookup(hostname);
            }

            // 1.empty
            // 2.xxx.xxx.xxx.xxx
            // 3.xxx.xxx.xxx.xxx,xxx.xxx.xxx.xxx,...
            String result = resp.body().string();
            Log.d(TAG,"HttpDns request success (" + result + ")");

            if (result == null || result.isEmpty()) {
                Log.i(TAG,"HttpDns result empty, use system dns lookup!");
                return Dns.SYSTEM.lookup(hostname);
            }

            List<InetAddress> addresses = new ArrayList<>();
            String[] ips = result.split(",");
            for (String ip : ips) {
                if (checkIpAddress(ip)) {
                    addresses.add(InetAddress.getByName(ip));
                }
            }

            if (addresses.isEmpty()) {
                Log.e(TAG,"HttpDns handle fail, use system dns lookup!" + result);
                return Dns.SYSTEM.lookup(hostname);
            }

            Log.d(TAG,"HttpDns success! (" + hostname + "->" + result + ")");
            return addresses;
        } catch (Exception e) {
            Log.e(TAG,"HttpDns failure, use system dns lookup!", e);
            return Dns.SYSTEM.lookup(hostname);
        } finally {
            if (resp != null) {
                resp.body().close();
            }
        }
    }

    private boolean checkIpAddress(String data) {
        if (data == null) {
            return false;
        }

        return data.matches("\\b(?:\\d{1,3}\\.){3}\\d{1,3}\\b");
    }

    public static final class Builder {
        private List<Interceptor> interceptors = new ArrayList<>();
        private List<Interceptor> networkInterceptors = new ArrayList<>();
        private File cacheDnsDir;
        private String httpDnsUrl;
        public Builder() {
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

        public OkHttpDnsImpl build() {
            return new OkHttpDnsImpl(this);
        }
    }
}
