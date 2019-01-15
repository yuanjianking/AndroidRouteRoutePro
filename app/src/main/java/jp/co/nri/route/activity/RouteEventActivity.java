package jp.co.nri.route.activity;

import android.content.DialogInterface;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.di.component.DaggerRouteEventComponent;
import jp.co.nri.route.di.module.RouteEventModule;
import jp.co.nri.route.presenter.RouteEventPresenter;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.IRouteEventView;
import jp.co.yahoo.android.maps.GeoPoint;
import jp.co.yahoo.android.maps.MapController;
import jp.co.yahoo.android.maps.MapView;
import jp.co.yahoo.android.maps.MyLocationOverlay;
import jp.co.yahoo.android.maps.PinOverlay;
import jp.co.yahoo.android.maps.PopupOverlay;

public class RouteEventActivity extends BaseActivity<RouteEventPresenter> implements IRouteEventView, MapView.MapTouchListener {

    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.fLMap) FrameLayout fLMap;
    @BindView(R.id.etEventName) EditText etEventName;
    @BindView(R.id.etEventDetail) EditText etEventDetail;
    @BindView(R.id.etStartDate) EditText etStartDate;
    @BindView(R.id.etEndDate) EditText etEndDate;
    @BindView(R.id.etStartTime) EditText etStartTime;
    @BindView(R.id.etEndTime) EditText etEndTime;
    private MapView mapView;

    @Override
    protected int contentView() {
        return R.layout.activity_route_event;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        tvToolbarBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        DaggerRouteEventComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).routeEventModule(new RouteEventModule(this)).build().inject(this);
        initMap();
    }

    // クリックして事件を処理する
    @OnClick({R.id.tvToolbarBack, R.id.tvStartDate, R.id.tvEndDate, R.id.tvStartTime, R.id.tvEndTime, R.id.btnEvent}) void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvToolbarBack:
                finish();
                break;
            case R.id.tvStartDate:
                AppUtil.showDatePickerDialog(this, etStartDate);
                break;
            case R.id.tvEndDate:
                AppUtil.showDatePickerDialog(this, etEndDate);
                break;
            case R.id.tvStartTime:
                AppUtil.showTimePickerDialog(this, etStartTime);
                break;
            case R.id.tvEndTime:
                AppUtil.showTimePickerDialog(this, etEndTime);
                break;
            case R.id.btnEvent:
                presenter.routeEvent(etEventName.getText().toString(), etEventDetail.getText().toString(), etStartDate.getText().toString(),
                    etEndDate.getText().toString(), etStartTime.getText().toString(), etEndTime.getText().toString());
                break;
        }
    }

    /**
     * 初期化MapView
     */
    private void initMap() {
        mapView = new MapView(this, "dj00aiZpPXhLdmFnUlRhN3VqSCZzPWNvbnN1bWVyc2VjcmV0Jng9ODY-");
        fLMap.addView(mapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
        MyLocationOverlay overlay = new MyLocationOverlay(getApplicationContext(), mapView);
        overlay.runOnFirstFix(new Runnable() {
            public void run() {
                if (mapView.getMapController() != null) {
                    //現在位置を取得
                    GeoPoint p = overlay.getMyLocation();
                    //地図移動
                    mapView.getMapController().animateTo(p);
                }
            }

        });
        overlay.enableMyLocation();
        mapView.getMapController().setZoom(1);
        mapView.setMapTouchListener(this);
        mapView.setLongPress(true);
    }

    /**
     * 目的値の設定確認する
     */
    private void alertDialog(MapView mapView, GeoPoint geoPoint) {
        AppUtil.showAlertDialog(this, "目的値の設定", "指定個所を目的地に設定します。よろしいでしょうか？", "Cancel", "Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapView.getOverlays().clear();
                PinOverlay overlay = new PinOverlay(PinOverlay.PIN_RED);
                mapView.getOverlays().add(overlay);
                overlay.addPoint(geoPoint, "目的地");
                PopupOverlay popupOverlay = new PopupOverlay();
                mapView.getOverlays().add(popupOverlay);
                overlay.setOnFocusChangeListener(popupOverlay);
            }
        });
    }

    @Override
    public boolean onTouch(MapView mapView, MotionEvent motionEvent) {
        return false;
    }

    /**
     * 選択アドレス
     */
    @Override
    public boolean onLongPress(MapView mapView, Object o, PinOverlay pinOverlay, GeoPoint geoPoint) {
        mapView.getOverlays().remove(pinOverlay);
        presenter.setLatLon(geoPoint);
        alertDialog(mapView, geoPoint);
        return true;
    }

    @Override
    public boolean onPinchIn(MapView mapView) {
        return false;
    }

    @Override
    public boolean onPinchOut(MapView mapView) {
        return false;
    }

    @Override
    protected void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    /**
     * 帰って前のページ
     */
    @Override
    public void routeEventViewClose() {
        setResult(RESULT_OK);
        finish();
    }
}
