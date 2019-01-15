package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.RouteEventActivity;
import jp.co.nri.route.di.module.RouteEventModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {RouteEventModule.class}, dependencies = {AppComponent.class})
public interface RouteEventComponent {
    void inject(RouteEventActivity activity);
}
