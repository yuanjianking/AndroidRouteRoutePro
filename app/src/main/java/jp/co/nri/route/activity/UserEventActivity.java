package jp.co.nri.route.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.di.component.DaggerUserEventActiveComponent;
import jp.co.nri.route.di.module.UserEventActiveModule;
import jp.co.nri.route.presenter.UserEventActivePresenter;
import jp.co.nri.route.view.IUserEventActiveView;
import jp.co.yahoo.android.maps.MapView;

public class UserEventActivity extends BaseActivity<UserEventActivePresenter> implements IUserEventActiveView{

    @BindView(R.id.fLMap) FrameLayout fLMap;
    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.tvDistance) TextView tvDistance;
    @BindView(R.id.tvHour) TextView tvHour;
    @BindView(R.id.tvMinute) TextView tvMinute;
    @BindView(R.id.tvSpeed) TextView tvSpeed;
    private MapView mapView;

    @Override
    protected int contentView() {
        return R.layout.activity_user_event;
    }

    @Override
    protected void initView() {
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        tvToolbarBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarBack.setText("quite event");
        DaggerUserEventActiveComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).
                userEventActiveModule(new UserEventActiveModule(this)).build().inject(this);
        mapView = new MapView(this, BaseApplication.MAP_ID);
        fLMap.addView(mapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void initData() {
        presenter.initMap((Event) getIntent().getSerializableExtra("event"));
    }

    @OnClick({R.id.tvToolbarBack, R.id.btnTarget, R.id.btnCurrent})public void onClick(View v){
        switch (v.getId()){
            case R.id.tvToolbarBack:
                finish();
                break;
            case R.id.btnTarget:
                presenter.showTarget();
                break;
            case R.id.btnCurrent:
                presenter.showCurrent();
                break;
        }
    }

    /**
     * 地図View
     */
    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void updateDistanceView(double distance) {
        tvDistance.setText(String.valueOf(distance));
    }

    @Override
    public void updateRemainingTime(int hour, int minute) {
        tvHour.setText(String.valueOf(hour));
        tvMinute.setText(String.valueOf(minute));
    }

    @Override
    public void updateSpeed(double speed) {
        tvSpeed.setText(String.valueOf(speed));
    }

    @Override
    public void setTitle(String title) {
        tvToolbarTitle.setText(title);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }
}
