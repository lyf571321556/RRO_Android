package ai.ones.network.debug;

public class StethoManager {
package ai.ones.network.debug;

import android.content.Context;

    public class StethoManager {

        public void initStetho(Context context) {
            //do nothing on release version
        }

        public StethoInterceptor getStethoInterceptor() {
            //do nothing on release version
            return null;
        }


        public HttpLoggingInterceptor getHttpLoggingInterceptor() {
            //do nothing on release version
            return null;
        }
    }

}
