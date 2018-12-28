package jp.co.nri.route.model;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import jp.co.nri.route.api.ApiService;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.bean.BaseBean;
import jp.co.nri.route.bean.User;

public class UserModel {

    private ApiService apiService;

    public UserModel(ApiService apiService) {
        this.apiService = apiService;
    }

    public Observable<BaseBean> login(User user){
        return apiService.login(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<BaseBean> signUp(User user){
        return apiService.signUp(user)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
