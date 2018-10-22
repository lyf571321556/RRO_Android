package ai.ones.network.debug;

import android.content.Context;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.uphyca.stetho_realm.RealmInspectorModulesProvider;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import okhttp3.logging.HttpLoggingInterceptor;

/**
 * @author lyf
 * debug模式下才会开始stetho调试模式
 */
public class StethoManager {

    private static StethoInterceptor stethoInterceptor = null;
    private static HttpLoggingInterceptor httpLoggingInterceptor = null;

    public static void initStetho(Context context) {
        Stetho.initialize(
                Stetho.newInitializerBuilder(context)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(context))
                        .enableWebKitInspector(RealmInspectorModulesProvider.builder(context).build())
                        .build());
    }

    /**
     * 日志输出到chrome
     *
     * @return
     */
    public static StethoInterceptor getStethoInterceptor() {
        if (stethoInterceptor == null) {
            stethoInterceptor = new StethoInterceptor();
        }
        return new StethoInterceptor();
    }


    /**
     * 日志输出到console
     *
     * @return
     */
    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        if (httpLoggingInterceptor == null) {
            httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                @Override
                public void log(String message) {
                    try {
                        String text = URLDecoder.decode(message, "utf-8");
                        Log.e("OKHttp-----", text);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        Log.e("OKHttp-----", message);
                    }
                }
            });
        }
        return httpLoggingInterceptor;
    }
}
