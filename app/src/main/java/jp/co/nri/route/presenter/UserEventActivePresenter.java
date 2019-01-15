package jp.co.nri.route.presenter;

import javax.inject.Inject;

import jp.co.nri.route.base.BasePresenter;
import jp.co.nri.route.model.EventModel;
import jp.co.nri.route.view.IUserEventActiveView;

public class UserEventActivePresenter extends BasePresenter<IUserEventActiveView> {

    private EventModel eventModel;

    @Inject
    public UserEventActivePresenter(IUserEventActiveView view, EventModel eventModel) {
        this.view = view;
        this.eventModel = eventModel;
    }
}
