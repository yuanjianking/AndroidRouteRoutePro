package jp.co.nri.route.presenter;

import java.util.regex.Pattern;

import javax.inject.Inject;

import jp.co.nri.route.base.BaseObserver;
import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.bean.Result;
import jp.co.nri.route.bean.User;
import jp.co.nri.route.model.UserModel;
import jp.co.nri.route.util.AppUtil;
import jp.co.nri.route.util.MD5Utils;
import jp.co.nri.route.view.ISignUpView;

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
        user.setPassword(MD5Utils.MD5Encode(password, "utf8"));
        userModel.signUp(user).subscribe(new BaseObserver<Result>(this) {

            @Override
            public void onNext(Result baseBean) {
                if("200000".equals(baseBean.getStatus())){
                    AppUtil.showToast("Signup Success");
                    view.closeView();
                }else if("201001".equals(baseBean.getStatus())){
                    AppUtil.showToast("ID重複");
                }
            }
        });
    }
}
