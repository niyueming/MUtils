/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.mutils.ui.test;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.Window;
import android.widget.TextView;


import net.nym.library.common.BaseApplication;
import net.nym.library.widget.lockpattern.LockPatternUtils;
import net.nym.library.widget.lockpattern.LockPatternView;
import net.nym.mutils.R;

import java.util.List;

/**
 * @author nym
 * @date 2014/12/31 0031.
 * @since 1.0
 */
public class TestLockPatternViewActivity extends ActionBarActivity implements LockPatternView.OnPatternListener {

    LockPatternView mLockPatternView;
    LockPatternUtils mLockPatternUtils;
    TextView hint;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_PROGRESS);
        setContentView(R.layout.activity_test_lock_pattern);
        hint = (TextView) findViewById(R.id.hint);
        hint.setText("");

        mLockPatternUtils = new LockPatternUtils(BaseApplication.getAppContext());
        mLockPatternView = (LockPatternView) findViewById(R.id.LockPatternView);
        mLockPatternView.setOnPatternListener(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onBackPressed() {
        mLockPatternUtils.clearLock();
        super.onBackPressed();
    }

    @Override
    public void onPatternStart() {

    }

    @Override
    public void onPatternCleared() {

    }

    @Override
    public void onPatternCellAdded(List<LockPatternView.Cell> pattern) {

    }

    @Override
    public void onPatternDetected(List<LockPatternView.Cell> pattern) {
        switch (mLockPatternUtils.checkPattern(pattern))
        {
            case 1:
                mLockPatternUtils.saveLockPattern(pattern);
                onBackPressed();
                break;
            case 0:
                mLockPatternUtils.clearLock();
                mLockPatternView.clearPattern();
                hint.setText("两次手势不一致，请重绘");
                break;
            case -1:
                mLockPatternUtils.saveLockPattern(pattern);
                mLockPatternView.clearPattern();
                hint.setText("再次绘制图案进行确认");
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id)
        {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
