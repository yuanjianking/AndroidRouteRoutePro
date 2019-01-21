package jp.co.nri.route.activity;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import butterknife.BindView;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.bean.Event;
import jp.co.nri.route.di.component.DaggerOwnerEventActiveComponent;
import jp.co.nri.route.di.module.OwnerEventActiveModule;
import jp.co.nri.route.presenter.OwnerEventActivePresenter;
import jp.co.nri.route.view.IOwnerEventActiveView;
import jp.co.yahoo.android.maps.MapView;

public class OwnerEventActivity extends BaseActivity<OwnerEventActivePresenter> implements IOwnerEventActiveView {

    @BindView(R.id.fLMap) FrameLayout fLMap;
    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    private MapView mapView;


    @Override
    protected int contentView() {
        return R.layout.activity_owner_event;
    }

    @Override
    protected void initView() {
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        tvToolbarBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        tvToolbarBack.setText("quite event");
        DaggerOwnerEventActiveComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).ownerEventActiveModule(new OwnerEventActiveModule(this)).build().inject(this);
        mapView = new MapView(this, BaseApplication.MAP_ID);
        fLMap.addView(mapView, FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void initData() {
        mapView.setScalebar(true);
        presenter.setEvent((Event) getIntent().getSerializableExtra("event"));
        presenter.guestLocations();
    }

    @Override
    public MapView getMapView() {
        return mapView;
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
