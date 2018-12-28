package jp.co.nri.route.activity;

import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;

public class RouteEventActivity extends BaseActivity {

    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;

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
    }

    @OnClick({R.id.tvToolbarBack}) void onClick(View v){
        switch (v.getId()){
            case R.id.tvToolbarBack:
                finish();
                break;
        }
    }
}
