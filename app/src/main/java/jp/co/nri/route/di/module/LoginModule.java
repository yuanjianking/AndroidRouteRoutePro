package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.UserModel;
import jp.co.nri.route.view.ILoginView;

@Module
public class LoginModule {

    private final ILoginView view;

    public LoginModule(ILoginView view) {
        this.view = view;
    }

    // p层需要使用activity，所以在build时需要主动new model
    @PerActivity
    @Provides
    ILoginView provideILoginView(){
        return view;
    }

    @PerActivity
    @Provides
    UserModel providesLoginModel(ApiService apiService){
        return new UserModel(apiService);
    }

}
