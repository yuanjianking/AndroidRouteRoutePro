package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IOwnerEventActiveView;

@Module
public class OwnerEventActiveModule {

    private final IOwnerEventActiveView view;

    public OwnerEventActiveModule(IOwnerEventActiveView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    IOwnerEventActiveView provideIOwnerEventActiveView(){
        return view;
    }

    @PerActivity
    @Provides
    EventModel provideEventModel(ApiService apiService){
        return new EventModel(apiService);
    }

}
