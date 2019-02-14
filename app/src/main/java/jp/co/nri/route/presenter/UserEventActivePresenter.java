package jp.co.nri.route.presenter;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.inject.Inject;

import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.LocationRequest;
import jp.co.nri.route.bean.LocationResult;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.IUserEventActiveView;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.Overlay;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PolylineOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;
import jp.co.yahoo.android.maps.ar.ShapeUtil;
import jp.co.yahoo.android.maps.navi.NaviController;
import jp.co.yahoo.android.maps.routing.GPoint;
import jp.co.yahoo.android.maps.routing.RouteControl;
import jp.co.yahoo.android.maps.routing.RouteOverlay;

public class UserEventActivePresenter extends BasePresenter<IUserEventActiveView> {

    private EventModel eventModel;
    private Event event;
    private List<GeoPoint> list = new ArrayList<>();
    private double currentDistance;
    private double historyDistance;
    private Drawable drawable;
    private Drawable targetDrawable;
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm");
    private long eventEndTime;
    // ユーザーの開始とシステム時間
    private long sysTime;
    private long startTime;
    private GeoPoint targetPoint;
    private RouteOverlay routeOverlay;
    private RouteControl routeControl;

    @Inject
    public UserEventActivePresenter(IUserEventActiveView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
        Drawable curr = BaseApplication.getApplication().getDrawable(R.drawable.position_point);
        int size = AppUtil.dp2px(BaseApplication.getApplication(), 60);
        int targetSize = AppUtil.dp2px(BaseApplication.getApplication(), 55);
        assert curr != null;
        drawable = AppUtil.zoomDrawable(curr, size, size);
        curr = BaseApplication.getApplication().getDrawable(R.drawable.user_position_point);
        assert curr != null;
        targetDrawable = AppUtil.zoomDrawable(curr, targetSize, targetSize);
    }

    /**
     * 初期化Map
     */
    public void initMap(Event event){
        this.event = event;
        targetPoint = new GeoPoint((int) (Double.valueOf(event.getLatitude()) * 1E6),(int) (Double.valueOf(event.getLongitude()) * 1E6));
        view.setTitle(event.getName());
        try {
            long eventStartTime = sdf.parse(event.getStartDate() + " " + event.getStartTime()).getTime();
            this.eventEndTime = sdf.parse(event.getEndDate() + " " + event.getEndTime()).getTime();
            long ms = eventEndTime - eventStartTime;
            int hour = (int)(ms / 1000 / 60 / 60);
            int minute = (int)(ms / 1000 / 60 % 60);
            hour = hour > 0 ? hour : 0;
            minute = minute > 0 ? minute : 0;
            view.updateRemainingTime(hour, minute);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        view.getMapView().getMapController().setZoom(1);
        //位置が更新されると地図の位置も変える。
        MyLocationOverlay overlay = new MyLocationOverlay(BaseApplication.getApplication(), view.getMapView()){
            @Override
            public void onLocationChanged(Location location) {
                super.onLocationChanged(location);
                if (view == null || view.getMapView().getMapController() == null) {
                    return;
                }
                GeoPoint p = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
                if(list.size() == 0){
                    updateMap(p);
                    if(startTime > 0) {
                        //addRouteOverlay(p);
                    }
                }else{
                    GeoPoint lastGeoPoint = list.get(list.size()-1);
                    // 移動距離米を取得
                    double moveDistance = ShapeUtil.calcDistance(lastGeoPoint.getLatitude(), lastGeoPoint.getLongitude(), p.getLatitude(), p.getLongitude());
                    if(moveDistance > 1){
                        if(startTime > 0) {
                            moveDistance = (double)Math.round(moveDistance/1000.0*10.0)/10.0;
                            currentDistance += moveDistance;

                            /*
                            GPoint gp = new GPoint(p.getLongitude(), p.getLatitude());
                            routeControl.cmpLineAndPoint(gp);
                            double lineDist = routeControl.getMDistanceToNearPoint(gp);
                            System.out.println("------> lineDist " + lineDist);
                            */
                        }
                        updateMap(p);
                    }
                }
            }
        };
        //現在位置取得開始
        overlay.enableMyLocation();
        //ナビゲーションアイコン
        //mapView.getOverlays().add(overlay);
        view.getMapView().invalidate();
        BaseApplication.getApplication().getHandler().post(new Runnable() {
            @Override
            public void run() {
                if (view == null) {
                    return;
                }
                historyLocation();
                BaseApplication.getApplication().getHandler().postDelayed(this, 1000*60);
            }
        });
    }

    /**
     * 地図を描き更新
     */
    private void updateMap(GeoPoint p){
        view.getMapView().getMapController().animateTo(p);
        Iterator<Overlay> iterable = view.getMapView().getOverlays().iterator();
        while(iterable.hasNext()){
            Overlay overlay = iterable.next();
            if(!routeOverlay.equals(overlay)){
                iterable.remove();
            }
        }

        // 目的地
        PinOverlay tOverlay = new PinOverlay(targetDrawable);
        view.getMapView().getOverlays().add(tOverlay);
        tOverlay.addPoint(targetPoint,"目的地");
        PopupOverlay pop = new PopupOverlay();
        view.getMapView().getOverlays().add(pop);
        tOverlay.setOnFocusChangeListener(pop);

        // line
        list.add(p);
        GeoPoint[] gps = new GeoPoint[list.size()];
        PolylineOverlay polylineOverlay = new PolylineOverlay(list.toArray(gps));
        polylineOverlay.setColor(Color.parseColor("#FF2B2B"));
        polylineOverlay.setWidth(AppUtil.dp2px(BaseApplication.getApplication(), 2));
        view.getMapView().getOverlays().add(polylineOverlay);

        // 現在の位置とPopup
        PinOverlay pinOverlay = new PinOverlay(drawable);
        view.getMapView().getOverlays().add(pinOverlay);
        pinOverlay.addPoint(p,BaseApplication.getApplication().name);
        PopupOverlay popupOverlay = new PopupOverlay();
        view.getMapView().getOverlays().add(popupOverlay);
        pinOverlay.setOnFocusChangeListener(popupOverlay);

        updateLocation(p);
    }

    /**
     * ユーザーのアドレスを更新する
     */
    private void updateLocation(GeoPoint p){
        LocationRequest request = new LocationRequest();
        request.setEventid(event.get_id());
        request.setUserid(BaseApplication.getApplication().userId);
        request.setName(BaseApplication.getApplication().name);
        request.setLatitude(String.valueOf(p.getLatitude()));
        request.setLongitude(String.valueOf(p.getLongitude()));
        request.setEventStartDate(event.getStartDate());
        request.setEventStartTime(event.getStartTime());
        request.setEventEndDate(event.getEndDate());
        request.setEventEndTime(event.getEndTime());
        request.setCurrentDistance(currentDistance);
        request.setHistoryDistance(historyDistance);
        eventModel.updateGuestLocation(request).subscribe(new BaseObserver<LocationResult>(this) {
            @Override
            public void onNext(LocationResult result) {
                if("200000".equals(result.getStatus())){
                    sysTime = result.getSysTime();
                    startTime = result.getStartTime();
                    setRemainingTime(false);
                }
            }
        });
    }

    /**
     * 歴史の位置を得る
     */
    private void historyLocation(){
        if(startTime == 0) {
            LocationRequest request = new LocationRequest();
            request.setEventid(event.get_id());
            request.setUserid(BaseApplication.getApplication().userId);
            request.setName(BaseApplication.getApplication().name);
            request.setEventStartDate(event.getStartDate());
            request.setEventStartTime(event.getStartTime());
            request.setEventEndDate(event.getEndDate());
            request.setEventEndTime(event.getEndTime());
            eventModel.guestHistoryLocation(request).subscribe(new BaseObserver<LocationResult>(this) {
                @Override
                public void onNext(LocationResult locationResult) {
                    if ("200000".equals(locationResult.getStatus())) {
                        sysTime = locationResult.getSysTime();
                        startTime = locationResult.getLocations().get(0).getStartTime();
                        historyDistance = locationResult.getLocations().get(0).getHistoryDistance();
                        historyDistance = Math.round(historyDistance*10.0)/10.0;
                        setRemainingTime(false);
                    }
                }
            });
        }else{
            setRemainingTime(true);
        }
    }

    private void setDistance(){
        double distance = Math.round((currentDistance + historyDistance) * 10.0) / 10.0;
        view.updateDistanceView(distance);
    }

    /**
     * 残り時間を設定する
     */
    private void setRemainingTime(boolean timeAdd){
        if(sysTime > 0 && startTime > 0){
            if(timeAdd) {
                sysTime += 1000 * 60;
            }
            long ms = eventEndTime - sysTime;
            int hour = (int)(ms / 1000 / 60 / 60);
            int minute = (int)(ms / 1000 / 60 % 60);
            hour = hour > 0 ? hour : 0;
            minute = minute > 0 ? minute : 0;
            view.updateRemainingTime(hour, minute);

            setSpeed();
            setDistance();
        }
    }

    /**
     * 速度を計算する
     */
    private void setSpeed(){
        double distance = currentDistance + historyDistance;
        if(distance > 0){
            double speed = distance / ((sysTime - startTime) / 1000.0 / 60.0 / 60.0);
            speed = Math.round(speed * 10.0) / 10.0;
            speed = speed > 0 ? speed : 0;
            view.updateSpeed(speed);
        }
    }

    public void showTarget(){
        if(targetPoint != null){
            view.getMapView().getMapController().animateTo(targetPoint);
        }
    }

    public void showCurrent(){
        if(list.size() > 0){
            view.getMapView().getMapController().animateTo(list.get(list.size()-1));
        }
    }

    private void addRouteOverlay(GeoPoint p){
        // route
        routeOverlay = new RouteOverlay(BaseApplication.getApplication(), BaseApplication.MAP_ID);
        routeOverlay.setRoutePos(p, targetPoint, RouteOverlay.TRAFFIC_WALK);
        //出発地ピンを非表示
        routeOverlay.setStartPinVisible(false);
        //目的地ピンを非表示
        routeOverlay.setGoalPinVisible(false);
        //経由点ピンを非表示
        routeOverlay.setRoutePinVisible(false);
        routeOverlay.setRouteOverlayListener(new RouteOverlay.RouteOverlayListener() {
            @Override
            public boolean finishRouteSearch(RouteOverlay routeOverlay) {
                routeOverlay.getAllRouteNodeInfo();
                routeControl = (RouteControl) routeOverlay.getObject();

                NaviController naviController = new NaviController(BaseApplication.getApplication(), routeOverlay);
                naviController.setMapView(view.getMapView());
                naviController.setNaviControlListener(new NaviController.NaviControllerListener() {
                    @Override
                    public boolean onLocationChanged(NaviController naviController) {
                        return false;
                    }

                    @Override
                    public boolean onLocationTimeOver(NaviController naviController) {
                        return false;
                    }

                    @Override
                    public boolean onLocationAccuracyBad(NaviController naviController) {
                        return false;
                    }

                    @Override
                    public boolean onRouteOut(NaviController naviController) {
                        System.out.println("------> " + "onRouteOut");
                        return false;
                    }

                    @Override
                    public boolean onGoal(NaviController naviController) {
                        return false;
                    }
                });
                naviController.start();


                return false;
            }

            @Override
            public boolean errorRouteSearch(RouteOverlay routeOverlay, int i) {
                return false;
            }
        });
        routeOverlay.search();
        view.getMapView().getOverlays().add(routeOverlay);
    }
}
