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
import android.view.View;
import android.widget.EditText;

import net.nym.library.widget.RoundProgressBar;
import net.nym.mutils.R;


public class TestRoundProgressBarActivity extends ActionBarActivity implements View.OnClickListener {

    EditText edtTxt_text;
    RoundProgressBar mRoundProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_round_progress_bar);
        edtTxt_text = (EditText) findViewById(R.id.editText);
        mRoundProgressBar = (RoundProgressBar) findViewById(R.id.roundProgressBar);
        mRoundProgressBar.setProgress(50);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        String progress = edtTxt_text.getText().toString().trim();
        if (!progress.equals(""))
            mRoundProgressBar.setProgress(Integer.parseInt(progress));
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
