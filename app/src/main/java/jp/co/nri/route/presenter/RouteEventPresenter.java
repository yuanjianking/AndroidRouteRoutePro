package jp.co.nri.route.presenter;

import javax.inject.Inject;

import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.Result;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.IRouteEventView;
import jp.co.yahoo.android.maps.GeoPoint;

public class RouteEventPresenter extends BasePresenter<IRouteEventView> {

    private EventModel eventModel;
    private GeoPoint geoPoint;

    @Inject
    public RouteEventPresenter(IRouteEventView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
    }

    /**
     * イベントを発表する
     */
    public void routeEvent(String eventName, String eventDetail, String startDate, String endDate, String startTime, String endTime){
        if(geoPoint == null){
            AppUtil.showToast("ターゲット値を設定していません");
            return;
        }
        if("".equals(eventName)){
            AppUtil.showToast("EventNameヌル");
            return;
        }
        if("".equals(eventDetail)){
            AppUtil.showToast("EventDetailヌル");
            return;
        }
        Event event = new Event(eventName, BaseApplication.getApplication().userId, eventDetail, String.valueOf(geoPoint.getLatitude()), String.valueOf(geoPoint.getLongitude()), startDate, endDate, startTime, endTime);
        eventModel.makeEvent(event).subscribe(new BaseObserver<Result>(this) {

            @Override
            public void onNext(Result result) {
                if("200000".equals(result.getStatus())){
                    view.routeEventViewClose();
                }else if("201002".equals(result.getStatus())){
                    AppUtil.showToast("メールアドレス検索結果なし");
                }else if("201005".equals(result.getStatus())){
                    AppUtil.showToast("イベント日が過去日付");
                }else if("202000".equals(result.getStatus())){
                    AppUtil.showToast("Token期限が切れる");
                }
            }

        });
    }

    // 経緯度
    public void setLatLon(GeoPoint geoPoint){
        this.geoPoint = geoPoint;
    }

}
