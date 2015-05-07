package net.nym.library.json;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.util.Map;

/**
 * @author nym
 * @date 2015/5/7 0007.
 * @since 1.0
 */
public class MJSONObject extends JSONObject implements Parcelable {

    public MJSONObject() {
        super();
    }

    public MJSONObject(Map copyFrom) {
        super(copyFrom);
    }

    public MJSONObject(JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public MJSONObject(String json) throws JSONException {
        super(json);
    }

    public MJSONObject(JSONObject copyFrom, String[] names) throws JSONException {
        super(copyFrom, names);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toString());
    }

    public static final Creator<MJSONObject> CREATOR = new Creator<MJSONObject>() {
        @Override
        public MJSONObject createFromParcel(Parcel source) {
            MJSONObject mMember = null;
            try {
                mMember = new MJSONObject(source.readString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mMember;
        }

        @Override
        public MJSONObject[] newArray(int size) {
            return new MJSONObject[size];
        }
    };
}
