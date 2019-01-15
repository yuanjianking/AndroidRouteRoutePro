package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IUserEventActiveView;

@Module
public class UserEventActiveModule {

    private final IUserEventActiveView view;

    public UserEventActiveModule(IUserEventActiveView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    IUserEventActiveView provideIUserEventActiveView(){
        return view;
    }

    @PerActivity
    @Provides
    EventModel provideEventModel(ApiService apiService){
        return new EventModel(apiService);
    }
}
