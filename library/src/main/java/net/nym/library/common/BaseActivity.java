package net.nym.library.common;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.nym.library.R;
import net.nym.library.util.ContextUtils;


/**
 * @author nym
 * @date 2015/5/6 0006.
 * @since 1.0
 */
public class BaseActivity extends AppCompatActivity {
    protected String TAG = getClass().getSimpleName();
    public static final int DIALOG_LOADING = 0;//访问网络对话框标签

    private FrameLayout layoutContent;
    private RelativeLayout layoutBack,layoutRight;
    private LinearLayout layoutTop;
    private TextView mTxtBack;
    private TextView mTxtTitle;
    private TextView mTxtRight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (ContextUtils.isKitkatOrLater()){
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        super.setContentView(R.layout.root);
        getSupportActionBar().hide();

        if (ContextUtils.isKitkatOrLater()){
            View statusBar = findViewById(R.id.statusBar);
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) statusBar.getLayoutParams();
            params.height = ContextUtils.getStatusBarHeight(this);
        }

        layoutTop = (LinearLayout) findViewById(R.id.layout_root_top);
        layoutBack = (RelativeLayout) findViewById(R.id.layout_top_back);
        layoutRight = (RelativeLayout) findViewById(R.id.layout_top_right);
        layoutContent = (FrameLayout) findViewById(R.id.layout_root_content);

        mTxtBack = (TextView) layoutTop.findViewById(R.id.top_id_back);
        layoutBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        mTxtTitle = (TextView) layoutTop.findViewById(R.id.top_id_sub_title);
        mTxtRight = (TextView) layoutTop.findViewById(R.id.top_id_right);

    }

    /**
     * 改写 setContentView()
     */
    @Override
    public void setContentView(int layoutResId) {
        View view = LayoutInflater.from(this).inflate(layoutResId, null);
        layoutContent.addView(view);
    }

    /**
     * 改写setContentView
     */
    @Override
    public void setContentView(View view) {
        layoutContent.addView(view);
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id)
        {
            case DIALOG_LOADING:
                return new ProgressDialog(this);
//                return new LoadDialog(this,R.style.dialog);


        }
        return super.onCreateDialog(id);
    }


    @Override
    public void setTitle(CharSequence title) {
        mTxtTitle.setText(title);
    }

    @Override
    public void setTitle(int titleId) {
        setTitle(getText(titleId));
    }

    @Override
    public void setTitleColor(int color) {
        mTxtTitle.setTextColor(color);
    }

    public void setTitleOnClickListener(View.OnClickListener clickListener)
    {
        mTxtTitle.setOnClickListener(clickListener);
    }

    public void showTitle(boolean isShow)
    {
        if (isShow)
        {
            layoutTop.setVisibility(View.VISIBLE);
        }
        else {
            layoutTop.setVisibility(View.GONE);
        }
    }

    public void setTitleBackgroundColor(int color)
    {
        layoutTop.setBackgroundColor(color);
    }

    public void showBack(boolean isShow)
    {
        if (isShow)
        {
            layoutBack.setVisibility(View.VISIBLE);
        }
        else {
            layoutBack.setVisibility(View.GONE);
        }
    }

    public void showRight(boolean isShow)
    {
        if (isShow)
        {
            mTxtRight.setVisibility(View.VISIBLE);
        }
        else {
            mTxtRight.setVisibility(View.GONE);
        }
    }

    public void setBackTextColor(int color)
    {
        mTxtBack.setTextColor(color);
    }

    public void setBackText(String text)
    {
        mTxtBack.setText(text);
    }

    public void setBackText(int res)
    {
        mTxtBack.setText(res);
    }

    public void setRightBackgroundResource(int res)
    {
        mTxtRight.setBackgroundResource(res);
    }

    public void setRightBackgroundColor(int res)
    {
        mTxtRight.setBackgroundColor(res);
    }

    public void setTitleTextColor(int res)
    {
        mTxtTitle.setTextColor(res);
    }

    public void setTitleTextResource(int res){mTxtTitle.setTextColor(res);}

    public void setRightOnClickListener(View.OnClickListener clickListener)
    {
        layoutRight.setOnClickListener(clickListener);
    }

    public void setRightText(String text)
    {
        mTxtRight.setText(text);
    }

    public void setRightText(int res)
    {
        mTxtRight.setText(res);
    }

    public void setRightTextColor(int color)
    {
        mTxtRight.setTextColor(color);
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}
