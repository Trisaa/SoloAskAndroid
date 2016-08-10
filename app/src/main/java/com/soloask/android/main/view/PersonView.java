package com.soloask.android.main.view;


import com.soloask.android.common.base.BaseView;
import com.soloask.android.data.model.User;

import java.util.List;

/**
 * Created by lebron on 16-8-5.
 */
public interface PersonView extends BaseView {

    /**
     * 是否显示无网络界面
     *
     * @param show
     */
    void showNetworkError(boolean show);

    /**
     * 是否显示进度条
     *
     * @param show
     */
    void showProgress(boolean show);

    /**
     * 显示PopularLives数据列表
     *
     * @param personList
     */
    void showPopularPersons(List<User> personList);

    /**
     * 获取数据个数
     */
    int getDataSize();
}