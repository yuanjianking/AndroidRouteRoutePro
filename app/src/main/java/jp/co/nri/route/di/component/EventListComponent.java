package jp.co.nri.route.di.component;

import dagger.Component;
import jp.co.nri.route.activity.EventListActivity;
import jp.co.nri.route.di.module.EventListModule;
import jp.co.nri.route.di.scope.PerActivity;

@PerActivity
@Component(modules = {EventListModule.class}, dependencies = {AppComponent.class})
public interface EventListComponent {
    void inject(EventListActivity activity);
}
