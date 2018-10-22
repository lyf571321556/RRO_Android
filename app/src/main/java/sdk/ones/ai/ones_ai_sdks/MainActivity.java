package sdk.ones.ai.ones_ai_sdks;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.List;

import ai.ones.network.NetManager;
import ai.ones.network.htttp.listener.HttpOnNextListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        NetManager.init(NetManager.newInitializerBuilder(this).setBaseUrl("https://www.izaodao.com/Api/"));
        HttpOnNextListener simpleOnNextListener = new HttpOnNextListener<List<SubjectResulte>>() {
            @Override
            public void onNext(List<SubjectResulte> subjects) {
                System.out.println(subjects.toString());
            }

            @Override
            public void onCacheNext(String cache) {
                System.out.println(cache);
//                /*缓存回调*/
//                Gson gson = new Gson();
//                java.lang.reflect.Type type = new TypeToken<BaseResultEntity<List<SubjectResulte>>>() {
//                }.getType();
//                BaseResultEntity resultEntity = gson.fromJson(cache, type);
//                tvMsg.setText("缓存返回：\n" + resultEntity.getData().toString());
            }

            /*用户主动调用，默认是不需要覆写该方法*/
            @Override
            public void onError(Throwable e) {
                super.onError(e);
                System.out.println(e.toString());
            }

            /*用户主动调用，默认是不需要覆写该方法*/
            @Override
            public void onCancel() {
                super.onCancel();
                System.out.println("onCancel");
            }
        };

        SubjectPostApi postEntity = new SubjectPostApi(simpleOnNextListener);
        postEntity.setAll(true);
        NetManager.getInstance(this).asyExecuteRequest(postEntity);
    }
}
