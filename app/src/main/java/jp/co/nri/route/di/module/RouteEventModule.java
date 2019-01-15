package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IRouteEventView;

@Module
public class RouteEventModule {
    private final IRouteEventView view;

    public RouteEventModule(IRouteEventView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    IRouteEventView provideIRouteEventView() {
        return view;
    }

    @PerActivity
    @Provides
    EventModel providesEventModel(ApiService apiService) {
        return new EventModel(apiService);
    }

}
