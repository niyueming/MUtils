package net.nym.library.javabean;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

/**
 * @author nym
 * @date 2014/11/7 0007.
 * @since 1.0
 */
public class JSONUtils {

    public static JSONObject toJSONObject(Map<String, Object> map) {
        JSONObject jsonObject = new JSONObject();

        Set<String> keys = map.keySet();

        try {
            for (String key : keys) {
                jsonObject.putOpt(key, map.get(key));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return jsonObject;
    }
}
