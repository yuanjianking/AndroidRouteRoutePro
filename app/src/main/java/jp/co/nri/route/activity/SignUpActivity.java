package jp.co.nri.route.activity;

import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.di.component.DaggerSignUpComponent;
import jp.co.nri.route.di.module.SignUpModule;
import jp.co.nri.route.presenter.SignUpPresenter;
import jp.co.nri.route.view.ISignUpView;

public class SignUpActivity extends BaseActivity<SignUpPresenter> implements ISignUpView {

    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    @BindView(R.id.tvToolbarTitle) TextView tvToolbarTitle;
    @BindView(R.id.etName) EditText etName;
    @BindView(R.id.etUserID) EditText etUserID;
    @BindView(R.id.etPassword) EditText etPassword;

    @Override
    protected int contentView() {
        return R.layout.activity_sign_up;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        tvToolbarBack.setVisibility(View.VISIBLE);
        tvToolbarTitle.setVisibility(View.VISIBLE);
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        DaggerSignUpComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).signUpModule(new SignUpModule(this)).build().inject(this);
    }

    @OnClick({R.id.tvToolbarBack, R.id.btnSignUp}) void onClick(View v){
        switch (v.getId()){
            case R.id.tvToolbarBack:
                finish();
                break;
            case R.id.btnSignUp:
                presenter.signUp(etName.getText().toString(), etUserID.getText().toString(), etPassword.getText().toString());
                break;
        }
    }


    @Override
    public void closeView() {
        finish();
    }
}
