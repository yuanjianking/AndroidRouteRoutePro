package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.SignUpActivity;
import jp.co.nri.route.di.module.SignUpModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {SignUpModule.class}, dependencies = {AppComponent.class})
public interface SignUpComponent {
    void inject(SignUpActivity activity);
}
