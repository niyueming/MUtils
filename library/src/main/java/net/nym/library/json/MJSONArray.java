package net.nym.library.json;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.util.Collection;

/**
 * @author nym
 * @date 2015/5/7 0007.
 * @since 1.0
 */
public class MJSONArray extends JSONArray implements Parcelable {

    public MJSONArray() {
        super();
    }

    public MJSONArray(Collection copyFrom) {
        super(copyFrom);
    }

    public MJSONArray(JSONTokener readFrom) throws JSONException {
        super(readFrom);
    }

    public MJSONArray(String json) throws JSONException {
        super(json);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public MJSONArray(Object array) throws JSONException {
        super(array);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.toString());
    }

    public static final Creator<MJSONArray> CREATOR = new Creator<MJSONArray>() {
        @Override
        public MJSONArray createFromParcel(Parcel source) {
            MJSONArray mMember = null;
            try {
                mMember = new MJSONArray(source.readString());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return mMember;
        }

        @Override
        public MJSONArray[] newArray(int size) {
            return new MJSONArray[size];
        }
    };
}
