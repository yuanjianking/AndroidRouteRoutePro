package jp.co.nri.route.activity;

import android.location.Location;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.di.component.DaggerUserEventActiveComponent;
import jp.co.nri.route.di.module.UserEventActiveModule;
import jp.co.nri.route.presenter.UserEventActivePresenter;
import jp.co.nri.route.util.DistanceUtil;
import jp.co.nri.route.view.IUserEventActiveView;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PolylineOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;
import jp.co.yahoo.android.maps.ar.ShapeUtil;

public class UserEventActivity extends BaseActivity<UserEventActivePresenter> implements IUserEventActiveView{

    @BindView(R.id.fLMap) FrameLayout fLMap;
    private MapView mapView;
    List<GeoPoint> list = new ArrayList<>();
    private double distance;

    @Override
    protected int contentView() {
        return R.layout.activity_user_event;
    }

    @Override
    protected void initView() {
        DaggerUserEventActiveComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).
                userEventActiveModule(new UserEventActiveModule(this)).build().inject(this);
        initMap();
    }

    private void initMap() {
        mapView = new MapView(this, "dj00aiZpPXhLdmFnUlRhN3VqSCZzPWNvbnN1bWVyc2VjcmV0Jng9ODY-");
        fLMap.addView(mapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
//        MyLocationOverlay overlay = new MyLocationOverlay(getApplicationContext(), mapView);
//        overlay.runOnFirstFix(new Runnable() {
//            public void run() {
//                if (mapView.getMapController() != null) {
//                    //現在位置を取得
//                    GeoPoint p = overlay.getMyLocation();
//                    //地図移動
//                    mapView.getMapController().animateTo(p);
//                    PinOverlay pinOverlay = new PinOverlay(PinOverlay.PIN_RED);
//                    mapView.getOverlays().add(pinOverlay);
//                    pinOverlay.addPoint(p,BaseApplication.getApplication().name);
//                    PopupOverlay popupOverlay = new PopupOverlay();
//                    mapView.getOverlays().add(popupOverlay);
//                    pinOverlay.setOnFocusChangeListener(popupOverlay);
//                }
//            }
//        });
//        overlay.enableMyLocation();


        mapView.getMapController().setZoom(1);
        //位置が更新されると地図の位置も変える。
        MyLocationOverlay overlay = new MyLocationOverlay(this, mapView){
            @Override
            public void onLocationChanged(Location location) {
                super.onLocationChanged(location);
                if (mapView.getMapController() == null) {
                    return;
                }
                GeoPoint p = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
                if(list.size() == 0){
                    System.out.println("===0");
                    updateMap(p);
                }else{
                    GeoPoint lastGeoPoint = list.get(list.size()-1);
                    // 移動距離米を取得
                    double moveDistance = ShapeUtil.calcDistance(lastGeoPoint.getLatitude(), lastGeoPoint.getLongitude(), p.getLatitude(), p.getLongitude());
                    System.out.println("===1 "+moveDistance);
                    if(moveDistance>0){
                        System.out.println("===2");
                        updateMap(p);
                    }
                }
            }
        };
        //現在位置取得開始
        overlay.enableMyLocation();
        //会有额外的导航图标
        //mapView.getOverlays().add(overlay);
        mapView.invalidate();


    }

    private void updateMap(GeoPoint p){
        mapView.getMapController().animateTo(p);
        mapView.getOverlays().clear();

        GeoPoint target = new GeoPoint((int) (35.653621 * 1E6),(int) (139.735206 * 1E6));
        PinOverlay tOverlay = new PinOverlay(PinOverlay.PIN_RED);
        mapView.getOverlays().add(tOverlay);
        tOverlay.addPoint(target,"目的地");

        list.add(p);
        GeoPoint[] gps = new GeoPoint[list.size()];
        PolylineOverlay polylineOverlay = new PolylineOverlay(list.toArray(gps));
        mapView.getOverlays().add(polylineOverlay);

        PinOverlay pinOverlay = new PinOverlay(PinOverlay.PIN_RED);
        mapView.getOverlays().add(pinOverlay);
        pinOverlay.addPoint(p,BaseApplication.getApplication().name);
        PopupOverlay popupOverlay = new PopupOverlay();
        mapView.getOverlays().add(popupOverlay);
        pinOverlay.setOnFocusChangeListener(popupOverlay);
    }

}
