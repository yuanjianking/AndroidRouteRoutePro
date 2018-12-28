package jp.co.nri.route.activity;

import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.OnClick;
import jp.co.nri.route.R;
import jp.co.nri.route.adapter.MyAdapter;
import jp.co.nri.route.base.BaseActivity;
import jp.co.nri.route.base.BaseApplication;
import jp.co.nri.route.di.component.DaggerEventListComponent;
import jp.co.nri.route.di.module.EventListModule;
import jp.co.nri.route.presenter.EventListPresenter;
import jp.co.nri.route.view.IEventListView;

/**
 *
 */
public class EventListActivity extends BaseActivity<EventListPresenter> implements IEventListView {

    @BindView(R.id.recyclerView) RecyclerView recyclerView;
    @BindView(R.id.tvMakeEvent) TextView tvMakeEvent;
    @BindView(R.id.tvToolbarBack) TextView tvToolbarBack;
    private MyAdapter mAdapter;

    @Override
    protected int contentView() {
        return R.layout.activity_event_list;
    }

    /**
     * View初期化
     */
    @Override
    protected void initView() {
        setStatusBarColor(R.color.colorAccent, R.color.colorAccent, R.color.colorAccent);
        DaggerEventListComponent.builder().appComponent(BaseApplication.getApplication().getAppComponent()).eventListModule(new EventListModule(this)).build().inject(this);

        tvMakeEvent.setVisibility(View.VISIBLE);
        tvToolbarBack.setVisibility(View.VISIBLE);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mAdapter = new MyAdapter(presenter.getData());
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    protected void initData() {

    }


    /**
     * 更新する RecycleView
     * @param sysTime long
     */
    @Override
    public void updateListView(long sysTime) {
        mAdapter.setSysTime(sysTime);
        mAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.tvMakeEvent, R.id.tvToolbarBack}) void onClick(View v){
        switch (v.getId()){
            case R.id.tvToolbarBack:
                finish();
                break;
            case R.id.tvMakeEvent:
                Intent intent = new Intent(this, RouteEventActivity.class);
                startActivity(intent);
                break;
        }
    }
}


