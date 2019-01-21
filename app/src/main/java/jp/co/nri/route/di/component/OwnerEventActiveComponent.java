package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.OwnerEventActivity;
import jp.co.nri.route.di.module.OwnerEventActiveModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {OwnerEventActiveModule.class}, dependencies = {AppComponent.class})
public interface OwnerEventActiveComponent {

    void inject(OwnerEventActivity activity);

}
