package jp.co.nri.route.activity;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;

import java.util.ArrayList;
import java.util.List;

import jp.co.nri.route.R;
import jp.co.yahoo.android.maps.CircleOverlay;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PolylineOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;
import jp.co.yahoo.android.maps.routing.RouteOverlay;

/**
 * 仮テストActivity、後ろは削除します
 */
@Deprecated
public class TestActivity extends AppCompatActivity implements MapView.MapTouchListener {

    List<GeoPoint> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_test);
        MapView mapView = new MapView(this, "dj00aiZpPXhLdmFnUlRhN3VqSCZzPWNvbnN1bWVyc2VjcmV0Jng9ODY-");
        MapController c = mapView.getMapController();
//        c.setCenter(new GeoPoint(35665721,139731006)); //指定初始显示的地图
//        c.setZoom(1); //指定初始显示的比例



        setContentView(mapView);

        GeoPoint g1 = new GeoPoint(35665721,139731006);
        GeoPoint g2 = new GeoPoint(35670087,139734117);
        GeoPoint g3 = new GeoPoint(35685187,139734217);
        PinOverlay p1 = new PinOverlay(PinOverlay.PIN_RED);
        mapView.getOverlays().add(p1);
        p1.addPoint(g1,"11111111");

        PinOverlay p2 = new PinOverlay(PinOverlay.PIN_GREEN);
        mapView.getOverlays().add(p2);
        p2.addPoint(g2,"22222222");

        PinOverlay p3 = new PinOverlay(PinOverlay.PIN_GREEN);
        mapView.getOverlays().add(p3);
        p3.addPoint(g3,"22222222");

        mapView.setMapTouchListener(this);
        mapView.setLongPress(true);

        PopupOverlay popupOverlay = new PopupOverlay();
        mapView.getOverlays().add(popupOverlay);
        p1.setOnFocusChangeListener(popupOverlay);

        c.zoomToSpan(35670087 - 35665721, 139734117 - 139731006);

//        MyLocationOverlay overlay = new MyLocationOverlay(this, mapView){
//            @Override
//            public void onLocationChanged(Location location) {
//                super.onLocationChanged(location);
//                if (mapView.getMapController() != null) {
//                    //位置が更新されると地図の位置も変える。
//                    GeoPoint p = new GeoPoint((int) (location.getLatitude() * 1E6),(int) (location.getLongitude() * 1E6));
//                    mapView.getMapController().animateTo(p);
//                    mapView.invalidate();
//                }
//            }
//        };
//
//        //現在位置取得開始
//        overlay.enableMyLocation();
//
//        //MapViewにMyLocationOverlayを追加。
//        mapView.getOverlays().add(overlay);
//        mapView.invalidate();


        RouteOverlay ro = new RouteOverlay(this, "dj00aiZpPXhLdmFnUlRhN3VqSCZzPWNvbnN1bWVyc2VjcmV0Jng9ODY-");

    }

    @Override
    public boolean onTouch(MapView mapView, MotionEvent motionEvent) {
        System.out.println("======");
        return false;
    }

    boolean flag = false;
    @Override
    public boolean onLongPress(MapView mapView, Object o, PinOverlay pinOverlay, GeoPoint geoPoint) {
        mapView.getOverlays().add(pinOverlay);
        pinOverlay.addPoint(geoPoint,null);
        list.add(geoPoint);
        GeoPoint[] gps = new GeoPoint[list.size()];
        PolylineOverlay polylineOverlay = new PolylineOverlay(list.toArray(gps));
        mapView.getOverlays().add(polylineOverlay);
        System.out.println(geoPoint.getLatitude() + "   " + geoPoint.getLongitude());
        if(!flag) {
            flag = true;
            CircleOverlay circleOverlay = new CircleOverlay(geoPoint, 50, 50);
            mapView.getOverlays().add(circleOverlay);
        }
        return false;
    }

    @Override
    public boolean onPinchIn(MapView mapView) {
        return false;
    }

    @Override
    public boolean onPinchOut(MapView mapView) {
        return false;
    }
}
