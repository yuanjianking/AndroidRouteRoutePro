package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IEventListView;

@Module
public class EventListModule {

    private final IEventListView view;

    public EventListModule(IEventListView view) {
        this.view = view;
    }

    // p层需要使用activity，所以在build时需要主动new model
    @PerActivity
    @Provides
    IEventListView provideIEventListView() {
        return view;
    }

    @PerActivity
    @Provides
    EventModel providesLoginModel(ApiService apiService) {
        return new EventModel(apiService);
    }

}
