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

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import net.nym.library.common.BaseAdapter;
import net.nym.library.entity.Entities;
import net.nym.library.entity.ProductInfo;
import net.nym.mutils.R;


public class TestParcelableActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_parcelable);
        ListView listView = (ListView) findViewById(R.id.listView);
        Entities<ProductInfo> data = new Entities<ProductInfo>();
        for (int i = 0 ; i < 10;i ++)
        {
            ProductInfo info = new ProductInfo();
            info.setName("name" + i);
            data.add(info);
        }
        listView.setAdapter(new MBaseAdapter(this,data));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private class MBaseAdapter extends BaseAdapter<ProductInfo> {
        public MBaseAdapter(Context context, Entities<ProductInfo> data) {
            super(context, data);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = new TextView(mContext);
            TextView text = (TextView) convertView;
            text.setPadding(10,10,10,10);
            text.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
            text.setText(mData.get(position).toString());
            return convertView;
        }
    };

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
