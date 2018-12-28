package jp.co.nri.route.api;

import io.reactivex.Observable;
import jp.co.nri.route.bean.BaseBean;
import jp.co.nri.route.bean.EventListResult;
import jp.co.nri.route.bean.User;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Http請求インタフェース
 */
public interface ApiService {

    @POST("login")
    Observable<BaseBean> login(@Body User user);

    @POST("signup")
    Observable<BaseBean> signUp(@Body User user);

    @GET("eventList")
    Observable<EventListResult> eventList();

}
