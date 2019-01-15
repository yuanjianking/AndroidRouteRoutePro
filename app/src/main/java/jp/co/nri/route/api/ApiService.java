package jp.co.nri.route.api;

import io.reactivex.Observable;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.LoginResult;
import jp.co.nri.route.bean.Result;
import jp.co.nri.route.bean.EventListResult;
import jp.co.nri.route.bean.UpdateGuestLocationResult;
import jp.co.nri.route.bean.User;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Http請求インタフェース
 */
public interface ApiService {

    @POST("login")
    Observable<LoginResult> login(@Body User user);

    @POST("signup")
    Observable<Result> signUp(@Body User user);

    @GET("eventList")
    Observable<EventListResult> eventList();

    @POST("makeEvent")
    Observable<Result> makeEvent(@Body Event event);

    @POST("updateGuestLocation")
    Observable<UpdateGuestLocationResult> updateGuestLocation();

}
