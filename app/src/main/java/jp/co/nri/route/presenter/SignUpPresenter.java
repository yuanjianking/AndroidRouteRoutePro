package jp.co.nri.route.presenter;

import java.util.regex.Pattern;

import javax.inject.Inject;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.BaseBean;
import jp.co.nri.route.bean.User;
import jp.co.nri.route.model.UserModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.view.ISignUpView;
import retrofit2.HttpException;

public class SignUpPresenter extends BasePresenter<ISignUpView> {

    private UserModel userModel;

    @Inject
    public SignUpPresenter(ISignUpView view, UserModel userModel) {
        this.view = view;
        this.userModel = userModel;
    }

    /**
     * 登録する
     * @param name String
     * @param userid String
     * @param password String
     */
    public void signUp(String name, String userid, String password){
        if("".equals(name)){
            AppUtil.showToast("ユーザー名が空いている");
            return;
        }
        if("".equals(userid)){
            AppUtil.showToast("ユーザーid空");
            return;
        }
        if("".equals(password)){
            AppUtil.showToast("パスワードは空");
            return;
        }
        String pattern = "^[a-zA-Z0-9_]{2,16}$";
        if(!Pattern.matches(pattern, userid)){
            AppUtil.showToast("ユーザーIDアルファベット、数字、下線を許す");
            return;
        }
        User user = new User();
        user.setName(name);
        user.setUserid(userid);
        user.setPassword(password);
        userModel.signUp(user).subscribe(new Observer<BaseBean>() {
            @Override
            public void onSubscribe(Disposable d) {
                disposable = d;
                subscribe();
            }

            @Override
            public void onNext(BaseBean baseBean) {
                if("200000".equals(baseBean.getStatus())){
                    AppUtil.showToast("Signup Success");
                    view.closeView();
                }else if("201001".equals(baseBean.getStatus())){
                    AppUtil.showToast("ID重複");
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
