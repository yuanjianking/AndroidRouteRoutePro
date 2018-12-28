package jp.co.nri.route.activity;

import android.content.Intent;
import android.view.View;

import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.base.BaseActivity;

public class WelcomeActivity extends BaseActivity {

    @Override
    protected int contentView() {
        return R.layout.activity_welcome;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        setStatusBarColor(R.color.white, R.color.colorStatus, R.color.white);
    }

    /**
     * ジャンプページ
     * @param view view
     */
    @OnClick({R.id.btnLogin, R.id.btnSignUp}) void onClick(View view){
        Intent intent;
        switch (view.getId()){
            case R.id.btnLogin:
                intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.btnSignUp:
                intent = new Intent(this, SignUpActivity.class);
                startActivity(intent);
                break;
        }
    }

}
