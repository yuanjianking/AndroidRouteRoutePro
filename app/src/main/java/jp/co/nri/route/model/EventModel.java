package jp.co.nri.route.model;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.bean.BaseBean;
import jp.co.nri.route.bean.EventListResult;
import jp.co.nri.route.bean.User;

public class EventModel {

    private ApiService apiService;

    public EventModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<EventListResult> eventList(){
        return apiService.eventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
