package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.LoginActivity;
import jp.co.nri.route.di.module.LoginModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {LoginModule.class}, dependencies = {AppComponent.class})
public interface LoginComponent {
    void inject(LoginActivity activity);
}
