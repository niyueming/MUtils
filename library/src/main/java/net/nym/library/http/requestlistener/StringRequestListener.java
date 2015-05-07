package net.nym.library.http.requestlistener;


import net.nym.library.util.Log;

/**
 * @author nym
 * @date 2015/5/7 0007.
 * @since 1.0
 */
public class StringRequestListener extends DefaultRequestListener<String> {

    @Override
    public void onResponse(String object) {
        Log.i("onResponse=%s", object + "");
    }

}