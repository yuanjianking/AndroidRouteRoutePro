package jp.co.nri.route.activity;

import android.content.Intent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.di.component.DaggerLoginComponent;
import jp.co.nri.route.di.module.LoginModule;
import jp.co.nri.route.presenter.LoginPresenter;
import jp.co.nri.route.view.ILoginView;

public class LoginActivity extends BaseActivity<LoginPresenter> implements ILoginView {

    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.etUserID) EditText etUserID;
    @BindView(R.id.etPassword) EditText etPassword;

    @Override
    protected int contentView() {
        return R.layout.activity_login;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        tvToolbarBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        DaggerLoginComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).loginModule(new LoginModule(this)).build().inject(this);
    }

    @OnClick(R.id.btnLogin) void onClick(){
        presenter.login(etUserID.getText().toString(), etPassword.getText().toString());
    }

    /**
     * ジャンプページ
     */
    @Override
    public void startListActivity() {
        Intent intent = new Intent(this, EventListActivity.class);
        startActivity(intent);
        finish();
    }

    @OnClick(R.id.tvToolbarBack) void onClick(View v){
        switch (v.getId()){
            case R.id.tvToolbarBack:
                finish();
                break;
        }
    }
}
