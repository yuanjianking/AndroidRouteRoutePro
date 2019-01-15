package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.UserEventActivity;
import jp.co.nri.route.di.module.UserEventActiveModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {UserEventActiveModule.class}, dependencies = {AppComponent.class})
public interface UserEventActiveComponent {

    void inject(UserEventActivity activity);

}
