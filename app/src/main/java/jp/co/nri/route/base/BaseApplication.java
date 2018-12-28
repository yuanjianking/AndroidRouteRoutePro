package jp.co.nri.route.base;

import android.app.Activity;
import android.app.Application;
import android.view.View;

import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.component.AppComponent;
import jp.co.nri.route.di.component.DaggerAppComponent;
import jp.co.nri.route.di.module.AppModule;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * 全体のApplication
 */
public class BaseApplication extends Application {

    private static final String HOST = "http://192.168.61.159:8880/";
    private Retrofit retrofit;
    private static BaseApplication baseApplication;
    private volatile ApiService apiService;
    private AppComponent appComponent;

    @Override
    public void onCreate() {
        super.onCreate();

        baseApplication = this;
        createRetrofit();

        appComponent = DaggerAppComponent.builder().appModule(new AppModule(this)).build();
    }

    public AppComponent getAppComponent() {
        return appComponent;
    }

    public static BaseApplication getApplication(){
        return baseApplication;
    }

    private void createRetrofit() {
        retrofit = new Retrofit.Builder().
                baseUrl(HOST).
                addConverterFactory(GsonConverterFactory.create()).
                addCallAdapterFactory(RxJava2CallAdapterFactory.create()).
                build();
    }

    public ApiService getApiService(){
        if(apiService == null){
            synchronized (BaseApplication.class) {
                if(apiService == null) {
                    apiService = retrofit.create(ApiService.class);
                }
            }
        }
        return apiService;
    }

    public void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        // android:fitsSystemWindows=”true” android:clipToPadding=”false”
        View decor = activity.getWindow().getDecorView();
        if (dark) {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        } else {
            decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }
    }
}
