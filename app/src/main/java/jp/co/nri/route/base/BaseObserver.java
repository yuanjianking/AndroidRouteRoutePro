package jp.co.nri.route.base;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import jp.co.nri.route.util.AppUtil;
import retrofit2.HttpException;

public abstract class BaseObserver<T> implements Observer<T> {

    private BasePresenter presenter;

    public BaseObserver(BasePresenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onSubscribe(Disposable d) {
        presenter.subscribe(d);
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
        e.printStackTrace();
    }

    @Override
    public void onComplete() {

    }
}
