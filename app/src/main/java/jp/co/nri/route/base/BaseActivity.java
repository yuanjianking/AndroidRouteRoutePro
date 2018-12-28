package jp.co.nri.route.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.WindowManager;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.co.nri.route.R;

/**
 * Activity 基類
 * @param <T>
 */
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity {

    @Inject
    protected T presenter;
    @BindView(R.id.toolbar) Toolbar toolbar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(contentView());
        ButterKnife.bind(this);
        initView();
        getWindow().getDecorView().post(new Runnable() {
            @Override
            public void run() {
                initData();
            }
        });
    }

    protected abstract int contentView();

    protected abstract void initView();

    protected void initData(){

    }

    @Override
    protected void onDestroy() {
        if(presenter != null) {
            presenter.unsubscribe();
            presenter = null;
        }
        super.onDestroy();
    }

    protected void setStatusBarColor(int colorM, int colorL, int colorT){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                getWindow().setStatusBarColor(getResources().getColor(colorM));
            }else{
                getWindow().setStatusBarColor(getResources().getColor(colorL));
            }
            BaseApplication.getApplication().setAndroidNativeLightStatusBar(this, true);
        }
        toolbar.setBackgroundColor(getResources().getColor(colorT));
    }
}
