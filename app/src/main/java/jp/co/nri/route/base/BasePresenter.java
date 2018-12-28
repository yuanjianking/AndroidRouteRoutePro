package jp.co.nri.route.base;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * 展示層の基類
 * @param <T>
 */
public abstract class BasePresenter<T> {

    protected T view;
    protected CompositeDisposable mDisposables;
    protected Disposable disposable;

    protected BasePresenter() {
        mDisposables = new CompositeDisposable();
    }

    public void subscribe() {
        if(disposable!=null) {
            mDisposables.add(disposable);
        }
    }


    public void unsubscribe() {
        if (mDisposables != null && mDisposables.size()>0) {
            mDisposables.dispose();
        }
        this.view = null;
    }
}
