package jp.co.nri.route.di.module;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.api.ApiService;

@Module
public class AppModule {

    private BaseApplication application;

    public AppModule(BaseApplication application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context providesApplication(){
        return application;
    }

    @Provides
    @Singleton
    BaseApplication providesBaseApplication(){
        return application;
    }

    @Provides
    @Singleton
    ApiService providesApiService(BaseApplication application){
        return application.getApiService();
    }

}
