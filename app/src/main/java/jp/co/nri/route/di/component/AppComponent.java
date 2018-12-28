package jp.co.nri.route.di.component;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import jp.co.nri.route.di.module.AppModule;
import jp.co.nri.route.api.ApiService;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {
    Context context();
    ApiService getApiService();
}
