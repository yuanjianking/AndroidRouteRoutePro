package jp.co.nri.route.presenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.EventListResult;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.IEventListView;

public class EventListPresenter extends BasePresenter<IEventListView> {

    private EventModel eventModel;
    private List<Event> list = new ArrayList<>();

    @Inject
    public EventListPresenter(IEventListView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
    }

    public List<Event> getData(){
        return list;
    }

    /**
     * データリスト
     */
    @Inject
    public void eventList(){
        eventModel.eventList().subscribe(new BaseObserver<EventListResult>(this) {
            @Override
            public void onNext(EventListResult eventListResult) {
                if("200000".equals(eventListResult.getStatus())){
                    list.clear();
                    list.addAll(eventListResult.getEventList());
                    view.updateListView(eventListResult.getSysTime());
                }else if("201004".equals(eventListResult.getStatus())){
                    AppUtil.showToast("イペントなし");
                }else if("202000".equals(eventListResult.getStatus())){
                    AppUtil.showToast("Token期限が切れる");
                }
            }
        });
    }

}
