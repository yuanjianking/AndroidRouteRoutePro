package jp.co.nri.route.model;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.EventListResult;
import jp.co.nri.route.bean.Result;

public class EventModel {

    private ApiService apiService;

    public EventModel(ApiService apiService) {
        this.apiService = apiService;
    }

    // event list
    public Observable<EventListResult> eventList(){
        return apiService.eventList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    // make event
    public Observable<Result> makeEvent(Event event){
        return apiService.makeEvent(event)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
