package com.jaydenxiao.androidfire.ui.main.contract;

import com.jaydenxiao.androidfire.bean.NewsChannelTable;
import com.jaydenxiao.common.base.BaseModel;
import com.jaydenxiao.common.base.BasePresenter;
import com.jaydenxiao.common.base.BaseView;

import java.util.List;

import rx.Observable;

/**
 * des:
 * Created by xsf
 * on 2016.09.11:53
 */
public interface NewsMainContract {

    //在NewsMainModel中具体实现
    interface Model extends BaseModel {
        Observable<List<NewsChannelTable>> lodeMineNewsChannels();
    }

    //在NewsMainFragment具体实现
    interface View extends BaseView {
        void returnMineNewsChannels(List<NewsChannelTable> newsChannelsMine);
    }

    //抽象类，既保证子类具有BasePresenter的所有属性，还添加了lodeMineChannelsRequest方法，在NewsMainPresenter具体实现。
    //NewsMainPresenter还可以继承BasePresenter，并通过接口实现此方法¬，效果一致
    abstract class Presenter extends BasePresenter<View, Model> {
        public abstract void lodeMineChannelsRequest();
    }
}
