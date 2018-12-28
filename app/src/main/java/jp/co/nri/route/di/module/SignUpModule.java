package jp.co.nri.route.di.module;

import dagger.Module;
import dagger.Provides;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.di.scope.PerActivity;
import jp.co.nri.route.model.UserModel;
import jp.co.nri.route.view.ISignUpView;

@Module
public class SignUpModule {

    private final ISignUpView view;

    public SignUpModule(ISignUpView view) {
        this.view = view;
    }

    @PerActivity
    @Provides
    ISignUpView provideISignUpView(){
        return view;
    }

    @PerActivity
    @Provides
    UserModel providesLoginModel(ApiService apiService){
        return new UserModel(apiService);
    }


}
