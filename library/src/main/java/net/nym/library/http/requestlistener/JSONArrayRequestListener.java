package net.nym.library.http.requestlistener;


import net.nym.library.util.Log;

import org.json.JSONArray;

/**
 * @author nym
 * @date 2015/5/7 0007.
 * @since 1.0
 */
public class JSONArrayRequestListener extends DefaultRequestListener<JSONArray> {


    @Override
    public void onResponse(JSONArray object) {
        Log.i("onResponse=%s", object + "");
    }


}