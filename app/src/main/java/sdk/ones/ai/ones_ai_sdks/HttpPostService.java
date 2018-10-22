package sdk.ones.ai.ones_ai_sdks;


import java.util.List;

import ai.ones.network.wrapresult.ResponseEntity;
import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * 测试接口service-post相关
 * Created by WZG on 2016/12/19.
 */

public interface HttpPostService {

    @FormUrlEncoded
    @POST("AppFiftyToneGraph/videoLink")
    Observable<ResponseEntity<List<SubjectResulte>>> getAllVedioBys(@Field("once") boolean once_no);

}
