package jp.co.nri.route.presenter;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.model.UserModel;
import jp.co.nri.route.bean.BaseBean;
import jp.co.nri.route.bean.User;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.util.MD5Utils;
import jp.co.nri.route.view.ILoginView;
import retrofit2.HttpException;

public class LoginPresenter extends BasePresenter<ILoginView> {

    private UserModel loginModel;

    @Inject
    public LoginPresenter(ILoginView view, UserModel loginModel){
        this.view = view;
        this.loginModel = loginModel;
    }

    /**
     * 登録する
     * @param userid String
     * @param password String
     */
    public void login(String userid, String password){
        User user = new User();
        user.setUserid(userid);
        user.setPassword(MD5Utils.MD5Encode(password, "utf8"));
        loginModel.login(user).subscribe(new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                subscribe();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if("200000".equals(baseBean.getStatus())){
                    view.startListActivity();
                }else if("201002".equals(baseBean.getStatus())){
                    AppUtil.showToast("Userd 検索結果なし");
                }else if("201003".equals(baseBean.getStatus())){
                    AppUtil.showToast("パスワード間違い");
                }
            }

            @Override
            public void onError(Throwable e) {
                if(e instanceof HttpException){
                    HttpException httpException = (HttpException) e;
                    int code = httpException.code();
                    if (code >= 400 && code < 500) {
                        AppUtil.showToast("リクエスト不正");
                    }
                    if (code == 500) {
                        AppUtil.showToast("システムエラー");
                    }
                }
            }

            @Override
            public void onComplete() {

            }
        });
    }
}
