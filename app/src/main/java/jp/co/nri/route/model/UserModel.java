package jp.co.nri.route.model;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.bean.LoginResult;
import jp.co.nri.route.bean.Result;
import jp.co.nri.route.bean.User;

public class UserModel {

    private ApiService apiService;

    public UserModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<LoginResult> login(User user){
        return apiService.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<Result> signUp(User user){
        return apiService.signUp(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
