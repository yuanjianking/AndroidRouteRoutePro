package jp.co.nri.route.presenter;

import android.graphics.Color;
import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.inject.Inject;

import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.bean.Location;
import jp.co.nri.route.bean.LocationRequest;
import jp.co.nri.route.bean.LocationResult;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.IOwnerEventActiveView;
import jp.co.yahoo.android.maps.CircleOverlay;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;

public class OwnerEventActivePresenter extends BasePresenter<IOwnerEventActiveView> {

    private EventModel eventModel;
    private Drawable drawable;
    private Drawable targetDrawable;
    private boolean isZoom;
    private GeoPoint targetGeoPoint;
    private Event event;
    private List<GeoPoint> gps = new ArrayList<>();

    @Inject
    public OwnerEventActivePresenter(IOwnerEventActiveView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
        Drawable curr = BaseApplication.getApplication().getDrawable(R.drawable.position_point);
        int size = AppUtil.dp2px(BaseApplication.getApplication(), 60);
        int targetSize = AppUtil.dp2px(BaseApplication.getApplication(), 60);
        assert curr != null;
        drawable = AppUtil.zoomDrawable(curr, size, size);
        curr = BaseApplication.getApplication().getDrawable(R.drawable.user_position_point);
        assert curr != null;
        targetDrawable = AppUtil.zoomDrawable(curr, targetSize, targetSize);

        BaseApplication.getApplication().getHandler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(view == null || event == null){
                    return;
                }
                guestLocations();
                BaseApplication.getApplication().getHandler().postDelayed(this, 10000);
            }
        }, 10 * 1000);
    }

    /**
     * イベント情報を設定
     */
    public void setEvent(Event event){
        this.event = event;
        view.setTitle(event.getName());
    }

    /**
     * ユーザーの経緯度を取得する
     */
    public void guestLocations(){
        LocationRequest request = new LocationRequest();
        request.setEventid(event.get_id());
        eventModel.guestLocations(request).subscribe(new BaseObserver<LocationResult>(this) {
            @Override
            public void onNext(LocationResult result) {
                if("200000".equals(result.getStatus())) {
                    List<Location> list = result.getLocations();
                    showMapForUser(list);
                }
            }
        });
        setTargetLocation(event);
    }

    /**
     * 目標地点を設定する
     */
    private void setTargetLocation(Event event){
        int size = targetDrawable.getIntrinsicHeight()/2;
        targetGeoPoint = new GeoPoint((int)(Double.valueOf(event.getLatitude()) * 1E6), (int)(Double.valueOf(event.getLongitude()) * 1E6));
        PinOverlay po = new PinOverlay(targetDrawable, size, size);
        view.getMapView().getOverlays().add(po);
        po.addPoint(targetGeoPoint,"目的地");
        PopupOverlay pop = new PopupOverlay();
        view.getMapView().getOverlays().add(pop);
        po.setOnFocusChangeListener(pop);
        CircleOverlay circleOverlay = new CircleOverlay(targetGeoPoint, 50, 50);
        circleOverlay.setStrokeColor(Color.parseColor("#ff2b2b"));
        circleOverlay.setStrokeWidth(AppUtil.dp2px(BaseApplication.getApplication(), 2));
        view.getMapView().getOverlays().add(circleOverlay);
    }


    /**
     * ユーザーの経緯度を表示する
     */
    private void showMapForUser(List<Location> list){
        view.getMapView().getOverlays().clear();
        gps.clear();
        setTargetLocation(event);
        for(Location loc : list){
            GeoPoint gp = new GeoPoint((int)(Double.valueOf(loc.getLatitude()) * 1E6), (int)(Double.valueOf(loc.getLongitude()) * 1E6));
            PinOverlay po = new PinOverlay(drawable);
            view.getMapView().getOverlays().add(po);
            po.addPoint(gp,loc.getName());
            PopupOverlay pop = new PopupOverlay();
            view.getMapView().getOverlays().add(pop);
            po.setOnFocusChangeListener(pop);
            gps.add(gp);
        }
        if(!isZoom) {
            isZoom = true;

            gps.add(targetGeoPoint);
            Collections.sort(gps, new Comparator<GeoPoint>() {
                @Override
                public int compare(GeoPoint o1, GeoPoint o2) {
                    return o1.getLatitudeE6() - o2.getLatitudeE6();
                }
            });
            int i = gps.get(gps.size()-1).getLatitudeE6() - gps.get(0).getLatitudeE6() ;
            int lat = gps.get(0).getLatitudeE6() + i/2;
            Collections.sort(gps, new Comparator<GeoPoint>() {
                @Override
                public int compare(GeoPoint o1, GeoPoint o2) {
                    return o1.getLongitudeE6() - o2.getLongitudeE6();
                }
            });
            int j = gps.get(gps.size()-1).getLongitudeE6() - gps.get(0).getLongitudeE6() ;
            int lon = gps.get(0).getLongitudeE6() + j/2;

            GeoPoint center = new GeoPoint(lat, lon);

            view.getMapView().getMapController().zoomToSpan(i / 2, j / 2);
            BaseApplication.getApplication().getHandler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    view.getMapView().getMapController().animateTo(center);
                }
            }, 1000);
        }
    }


}
