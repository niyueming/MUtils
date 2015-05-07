package net.nym.library.http.requestlistener;


import net.nym.library.util.Log;

import org.json.JSONObject;

/**
 * @author nym
 * @date 2015/5/7 0007.
 * @since 1.0
 */
public class JSONObjectRequestListener extends DefaultRequestListener<JSONObject> {



    @Override
    public void onResponse(JSONObject object) {
        Log.i("onResponse=%s", object + "");
    }


}
