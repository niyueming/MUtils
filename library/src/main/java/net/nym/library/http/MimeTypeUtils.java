package net.nym.library.http;

import android.webkit.MimeTypeMap;

/**
 * @author nym
 * @date 2014/9/25 0025.
 */
public class MimeTypeUtils {

    public static String getMimeType(final String fileName) {
        String result = "application/octet-stream";
        int extPos = fileName.lastIndexOf(".");
        if (extPos != -1) {
            String ext = fileName.substring(extPos + 1);
            result = MimeTypeMap.getSingleton().getMimeTypeFromExtension(ext);
        }
        return result;
    }
}
