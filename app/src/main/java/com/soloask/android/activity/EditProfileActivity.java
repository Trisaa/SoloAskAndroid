package com.soloask.android.activity;

import android.app.Dialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.soloask.android.R;
import com.soloask.android.adapter.PriceAdapter;
import com.soloask.android.util.Constant;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lebron on 2016/6/24.
 */
public class EditProfileActivity extends BaseActivity implements View.OnClickListener {
    private TextView mPriceView, mTextCountView, mTitleCountView;
    private EditText mDescribeView, mTitleView;
    private RelativeLayout mSavingLayout;
    private PriceAdapter mPriceAdapter;
    private TextView mPriceView1, mPriceView5, mPriceView10, mPriceViewMore;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar_save, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_save) {
            if (TextUtils.isEmpty(mTitleView.getText())) {
                mTitleView.setError(EditProfileActivity.this.getString(R.string.notice_cannot_null));
            } else if (TextUtils.isEmpty(mDescribeView.getText())) {
                mDescribeView.setError(EditProfileActivity.this.getString(R.string.notice_cannot_null));
            } else {
                mSavingLayout.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mSavingLayout.setVisibility(View.GONE);
                        EditProfileActivity.this.setResult(Constant.CODE_RESULT_EDIT, null);
                        finish();
                    }
                }, 2000L);
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mPriceView = (TextView) findViewById(R.id.tv_choose_price);
        mSavingLayout = (RelativeLayout) findViewById(R.id.rl_progressbar);
        mPriceView1 = (TextView) findViewById(R.id.tv_price_1);
        mPriceView5 = (TextView) findViewById(R.id.tv_price_5);
        mPriceView10 = (TextView) findViewById(R.id.tv_price_10);
        mPriceViewMore = (TextView) findViewById(R.id.tv_price_more);
        mTextCountView = (TextView) findViewById(R.id.text_count);
        mTitleCountView = (TextView) findViewById(R.id.tv_edit_count);
        mDescribeView = (EditText) findViewById(R.id.edit_user_describe);
        mTitleView = (EditText) findViewById(R.id.edit_user_title);

        addListener();
    }

    private void addListener() {
        mPriceView1.setOnClickListener(this);
        mPriceView5.setOnClickListener(this);
        mPriceView10.setOnClickListener(this);
        mPriceViewMore.setOnClickListener(this);
        mDescribeView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTextCountView.setText(String.format(getString(R.string.format_write_describe), mDescribeView.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mTitleView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mTitleCountView.setText(String.format(getString(R.string.format_write_title), mTitleView.getText().length()));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_price_1:
                mPriceView.setText("$1");
                break;
            case R.id.tv_price_5:
                mPriceView.setText("$5");
                break;
            case R.id.tv_price_10:
                mPriceView.setText("$10");
                break;
            case R.id.tv_price_more:
                showChoosePriceDialog();
                break;
        }
    }

    private void showChoosePriceDialog() {
        View view = getLayoutInflater().inflate(R.layout.dialog_price_table, null);
        TextView okView = (TextView) view.findViewById(R.id.tv_choose_price_ok);
        final RecyclerView priceView = (RecyclerView) view.findViewById(R.id.grid_price_view);
        List list = new ArrayList();
        for (int i = 0; i < 20; i++) {
            list.add("$" + i);
        }
        priceView.setLayoutManager(new GridLayoutManager(this, 5));
        mPriceAdapter = new PriceAdapter(this, list);
        priceView.setAdapter(mPriceAdapter);


        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(view);
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);
        mBottomSheetDialog.show();
        okView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(mPriceAdapter.getChoosedPrice())) {
                    mPriceView.setText(mPriceAdapter.getChoosedPrice());
                }
                mBottomSheetDialog.dismiss();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
