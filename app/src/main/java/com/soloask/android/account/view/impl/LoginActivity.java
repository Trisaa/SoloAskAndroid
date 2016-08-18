package com.soloask.android.account.view.impl;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.soloask.android.MainApplication;
import com.soloask.android.R;
import com.soloask.android.account.injection.LoginModule;
import com.soloask.android.account.presenter.LoginPresenter;
import com.soloask.android.account.view.LoginView;
import com.soloask.android.common.base.BaseActivity;
import com.soloask.android.data.model.User;
import com.soloask.android.util.Constant;
import com.soloask.android.util.QQManager;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;

import org.json.JSONObject;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by LeBron on 2016/8/6.
 */
public class LoginActivity extends BaseActivity implements LoginView {
    @BindView(R.id.tv_facebook_login)
    TextView mLoginView;
    @BindView(R.id.tv_qq_login)
    TextView mQQLoginView;
    @BindView(R.id.progressbar_loading_layout)
    RelativeLayout mLoadingLayout;

    @Inject
    LoginPresenter mPresenter;
    private Tencent mTencent;
    private CallbackManager mCallbackManager;
    private IUiListener mBaseUiListener = new IUiListener() {
        @Override
        public void onComplete(Object o) {
            JSONObject jsonObject = (JSONObject) o;
            Log.i("LoginActivity", jsonObject.toString());
            mPresenter.doQQLogin(mTencent, jsonObject);
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(LoginActivity.this, uiError.errorMessage, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onCancel() {

        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MainApplication.get(this).getAppComponent()
                .plus(new LoginModule(this))
                .inject(this);
        //mCallbackManager = CallbackManager.Factory.create();
        mTencent = QQManager.getTencentInstance(LoginActivity.this);
        mTencent.logout(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //mCallbackManager.onActivityResult(requestCode, resultCode, data);
        Tencent.onActivityResultData(requestCode, resultCode, data, mBaseUiListener);
    }

    @OnClick(R.id.tv_facebook_login)
    public void login() {
        if (mPresenter != null) {
            mPresenter.doLogin(mCallbackManager);
        }
    }

    @OnClick(R.id.tv_qq_login)
    public void qqLogin() {
        if (!mTencent.isSessionValid()) {
            mTencent.login(LoginActivity.this, "all", mBaseUiListener);
        }
    }

    @Override
    protected int getContentViewID() {
        return R.layout.activity_login;
    }

    @Override
    protected void initViewsAndData() {

    }

    @Override
    public void loginSuccess(User user) {
        Intent intent = new Intent();
        intent.putExtra("user_name", user.getUserName());
        intent.putExtra("user_icon_url", user.getUserIcon());
        intent.putExtra("user_object_id", user.getObjectId());
        LoginActivity.this.setResult(Constant.CODE_RESULT_LOGIN, intent);
        LoginActivity.this.finish();
    }

    @Override
    public void showLoadingProgress(boolean show) {
        mLoadingLayout.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    @Override
    public void showToast(int stringId) {
        Toast.makeText(getViewContext(), stringId, Toast.LENGTH_SHORT).show();
    }

    @Override
    public Context getViewContext() {
        return this;
    }
}
