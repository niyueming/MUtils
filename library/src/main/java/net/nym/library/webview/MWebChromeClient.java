/*
 * Copyright (c) 2015  Ni YueMing<niyueming@163.com>
 *  Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the specific language governing permissions and limitations under the License.
 */

package net.nym.library.webview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Message;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.ConsoleMessage;
import android.webkit.GeolocationPermissions;
import android.webkit.JsPromptResult;
import android.webkit.JsResult;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.EditText;

import net.nym.library.R;
import net.nym.library.common.DebugConfig;


/**
 * @author nym
 * @date 2014/9/30 0030.
 */
public class MWebChromeClient extends WebChromeClient {

    /**
     * 需要支持上传文件的话，需要{@link MWebChromeClient#setUploadFileInvoke(UploadFileInvoke)}
     *
     * */
    private UploadFileInvoke mUploadFileInvoke;

    public MWebChromeClient() {
        super();
    }

    /**
     *
     * 支持上传文件
     * */
    public MWebChromeClient(UploadFileInvoke uploadFileInvoke) {
        this();
        setUploadFileInvoke(uploadFileInvoke);
    }

    public void setUploadFileInvoke(UploadFileInvoke uploadFileInvoke) {
        this.mUploadFileInvoke = uploadFileInvoke;
    }

    /**
     * Tell the host application the current progress of loading a page.
     *
     * @param view        The WebView that initiated the callback.
     * @param newProgress Current page loading progress, represented by
     */
    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        Context context = view.getContext();
        if (Activity.class.isInstance(context))
        {
            /**
             * In order for the progress bar to be shown, the feature must be requested
             * via {@link #requestWindowFeature(android.view.Window.FEATURE_PROGRESS)}.
             *
             * The progress for the progress bar. Valid ranges are from
             * 0 to 10000 (both inclusive). If 10000 is given, the progress
             * bar will be completely filled and will fade out.
             * */
            ((Activity)context).setProgress(newProgress * 100);
        }
        super.onProgressChanged(view, newProgress);
    }

    /**
     * Notify the host application of a change in the document title.
     *
     * @param view  The WebView that initiated the callback.
     * @param title A String containing the new title of the document.
     */
    @Override
    public void onReceivedTitle(WebView view, String title) {
        super.onReceivedTitle(view, title);
    }

    /**
     * Notify the host application of a new favicon for the current page.
     *
     * @param view The WebView that initiated the callback.
     * @param icon A Bitmap containing the favicon for the current page.
     */
    @Override
    public void onReceivedIcon(WebView view, Bitmap icon) {
        super.onReceivedIcon(view, icon);
    }

    /**
     * Notify the host application of the url for an apple-touch-icon.
     *
     * @param view        The WebView that initiated the callback.
     * @param url         The icon url.
     * @param precomposed True if the url is for a precomposed touch icon.
     */
    @Override
    public void onReceivedTouchIconUrl(WebView view, String url, boolean precomposed) {
        super.onReceivedTouchIconUrl(view, url, precomposed);
    }

    /**
     * Notify the host application that the current page would
     * like to show a custom View.  This is used for Fullscreen
     * video playback; see "HTML5 Video support" documentation on
     * {@link android.webkit.WebView}.
     *
     * @param view     is the View object to be shown.
     * @param callback is the callback to be invoked if and when the view
     */
    @Override
    public void onShowCustomView(View view, CustomViewCallback callback) {
        super.onShowCustomView(view, callback);
    }

    /**
     * Notify the host application that the current page would
     * like to hide its custom view.
     */
    @Override
    public void onHideCustomView() {
        super.onHideCustomView();
    }

    /**
     * Request the host application to create a new window. If the host
     * application chooses to honor this request, it should return true from
     * this method, create a new WebView to host the window, insert it into the
     * View system and send the supplied resultMsg message to its target with
     * the new WebView as an argument. If the host application chooses not to
     * honor the request, it should return false from this method. The default
     * implementation of this method does nothing and hence returns false.
     *
     * @param view          The WebView from which the request for a new window
     *                      originated.
     * @param isDialog      True if the new window should be a dialog, rather than
     *                      a full-size window.
     * @param isUserGesture True if the request was initiated by a user gesture,
     *                      such as the user clicking a link.
     * @param resultMsg     The message to send when once a new WebView has been
     *                      created. resultMsg.obj is a
     *                      {@link android.webkit.WebView.WebViewTransport} object. This should be
     *                      used to transport the new WebView, by calling
     *                      {@link android.webkit.WebView.WebViewTransport#setWebView(android.webkit.WebView)
     *                      WebView.WebViewTransport.setWebView(WebView)}.
     * @return This method should return true if the host application will
     * create a new window, in which case resultMsg should be sent to
     * its target. Otherwise, this method should return false. Returning
     * false from this method but also sending resultMsg will result in
     * undefined behavior.
     */
    @Override
    public boolean onCreateWindow(WebView view, boolean isDialog, boolean isUserGesture, Message resultMsg) {
        return super.onCreateWindow(view, isDialog, isUserGesture, resultMsg);
    }

    /**
     * Request display and focus for this WebView. This may happen due to
     * another WebView opening a link in this WebView and requesting that this
     * WebView be displayed.
     *
     * @param view The WebView that needs to be focused.
     */
    @Override
    public void onRequestFocus(WebView view) {
        super.onRequestFocus(view);
    }

    /**
     * Notify the host application to close the given WebView and remove it
     * from the view system if necessary. At this point, WebCore has stopped
     * any loading in this window and has removed any cross-scripting ability
     * in javascript.
     *
     * @param window The WebView that needs to be closed.
     */
    @Override
    public void onCloseWindow(WebView window) {
        super.onCloseWindow(window);
    }

    /**
     * Tell the client to display a javascript alert dialog.  If the client
     * returns true, WebView will assume that the client will handle the
     * dialog.  If the client returns false, it will continue execution.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult to confirm that the user hit enter.
     * @return boolean Whether the client will handle the alert dialog.
     * <p/>
     * 覆盖默认的window.alert展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsAlert(WebView view, String url, String message, final JsResult result) {
        if (DebugConfig.isDebug()) {

            Log.d(DebugConfig.TAG_BEDUG, "onJsAlert:" + message);
        }
        //对alert的简单封装
        new AlertDialog.Builder(view.getContext()).
                setTitle(R.string.ec_hint).setMessage(message).setPositiveButton(R.string.ec_conform,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        result.confirm();
                    }
                }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.confirm();
            }
        }).create().show();

        return true;
    }


    /**
     * Tell the client to display a confirm dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * confirm dialog and call the appropriate JsResult method. If the
     * client returns false, a default value of false will be returned to
     * javascript. The default behavior is to return false.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult used to send the user's response to
     *                javascript.
     * @return boolean Whether the client will handle the confirm dialog.
     * <p/>
     * 覆盖默认的window.confirm展示界面，避免title里显示为“：来自file:////”
     */
    @Override
    public boolean onJsConfirm(WebView view, String url, String message, final JsResult result) {
        if (DebugConfig.isDebug()) {

            Log.i(DebugConfig.TAG_BEDUG, "onJsConfirm:" + message);
        }
        //对confirm的简单封装
        new AlertDialog.Builder(view.getContext()).
                setTitle(R.string.ec_hint).setMessage(message).setPositiveButton(R.string.ec_conform,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        result.confirm();
                    }
                }).setNegativeButton(R.string.ec_concel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                result.cancel();
            }
        }).create().show();

        return true;
    }

    /**
     * Tell the client to display a prompt dialog to the user. If the client
     * returns true, WebView will assume that the client will handle the
     * prompt dialog and call the appropriate JsPromptResult method. If the
     * client returns false, a default value of false will be returned to to
     * javascript. The default behavior is to return false.
     *
     * @param view         The WebView that initiated the callback.
     * @param url          The url of the page requesting the dialog.
     * @param message      Message to be displayed in the window.
     * @param defaultValue The default value displayed in the prompt dialog.
     * @param result       A JsPromptResult used to send the user's reponse to
     *                     javascript.
     * @return boolean Whether the client will handle the prompt dialog.
     * 覆盖默认的window.prompt展示界面，避免title里显示为“：来自file:////”
     * window.prompt('请输入您的域名地址', '618119.com');
     */
    @Override
    public boolean onJsPrompt(WebView view, String url, String message, String defaultValue, final JsPromptResult result) {
        if (DebugConfig.isDebug()) {

            Log.d(DebugConfig.TAG_BEDUG, "onJsPrompt:" + message);
        }
        //对prompt的简单封装
        final EditText et = new EditText(view.getContext());
        et.setSingleLine();
        et.setText(defaultValue);
        new AlertDialog.Builder(view.getContext()).
                setTitle(R.string.ec_hint).setMessage(message).setView(et).setPositiveButton(R.string.ec_conform,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        result.confirm(et.getText().toString().trim());
                    }
                }).setNegativeButton(R.string.ec_concel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                result.cancel();
            }
        })
                // 屏蔽keycode等于84之类的按键，避免按键后导致对话框消息而页面无法再弹出对话框的问题
                .setOnKeyListener(new DialogInterface.OnKeyListener() {
                    @Override
                    public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                        if (DebugConfig.isDebug()) {

                            Log.v(DebugConfig.TAG_BEDUG, "onJsPrompt:keyCode==" + keyCode + "event=" + event);
                        }
                        return true;
                    }
                })
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        result.cancel();
                    }
                }).create().show();

        return true;
    }

    /**
     * Tell the client to display a dialog to confirm navigation away from the
     * current page. This is the result of the onbeforeunload javascript event.
     * If the client returns true, WebView will assume that the client will
     * handle the confirm dialog and call the appropriate JsResult method. If
     * the client returns false, a default value of true will be returned to
     * javascript to accept navigation away from the current page. The default
     * behavior is to return false. Setting the JsResult to true will navigate
     * away from the current page, false will cancel the navigation.
     *
     * @param view    The WebView that initiated the callback.
     * @param url     The url of the page requesting the dialog.
     * @param message Message to be displayed in the window.
     * @param result  A JsResult used to send the user's response to
     *                javascript.
     * @return boolean Whether the client will handle the confirm dialog.
     */
    @Override
    public boolean onJsBeforeUnload(WebView view, String url, String message, JsResult result) {
        return super.onJsBeforeUnload(view, url, message, result);
    }

    /**
     * 配置权限(定位)
     * WebView.getSettings().setGeolocationEnabled(true);		//(定位)
     * Notify the host application that web content from the specified origin
     * is attempting to use the Geolocation API, but no permission state is
     * currently set for that origin. The host application should invoke the
     * specified callback with the desired permission state. See
     * {@link android.webkit.GeolocationPermissions} for details.
     *
     * @param origin   The origin of the web content attempting to use the
     *                 Geolocation API.
     * @param callback The callback to use to set the permission state for the
     */
    @Override
    public void onGeolocationPermissionsShowPrompt(String origin, GeolocationPermissions.Callback callback) {
        callback.invoke(origin, true, false);
        super.onGeolocationPermissionsShowPrompt(origin, callback);
    }

    /**
     * Notify the host application that a request for Geolocation permissions,
     * made with a previous call to
     * {@link #onGeolocationPermissionsShowPrompt(String, android.webkit.GeolocationPermissions.Callback) onGeolocationPermissionsShowPrompt()}
     * has been canceled. Any related UI should therefore be hidden.
     */
    @Override
    public void onGeolocationPermissionsHidePrompt() {
        if (DebugConfig.isDebug()) {
            Log.i(DebugConfig.TAG_BEDUG, "网页在请求定位");
        }
        super.onGeolocationPermissionsHidePrompt();
    }

    /**
     * Report a JavaScript console message to the host application. The ChromeClient
     * should override this to process the log message as they see fit.
     *
     * @param consoleMessage Object containing details of the console message.
     * @return true if the message is handled by the client.
     */
    @Override
    public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
        return super.onConsoleMessage(consoleMessage);
    }

    /**
     * When not playing, video elements are represented by a 'poster' image. The
     * image to use can be specified by the poster attribute of the video tag in
     * HTML. If the attribute is absent, then a default poster will be used. This
     * method allows the ChromeClient to provide that default image.
     *
     * @return Bitmap The image to use as a default poster, or null if no such image is
     * available.
     */
    @Override
    public Bitmap getDefaultVideoPoster() {
        return super.getDefaultVideoPoster();
    }

    /**
     * When the user starts to playback a video element, it may take time for enough
     * data to be buffered before the first frames can be rendered. While this buffering
     * is taking place, the ChromeClient can use this function to provide a View to be
     * displayed. For example, the ChromeClient could show a spinner animation.
     *
     * @return View The View to be displayed whilst the video is loading.
     */
    @Override
    public View getVideoLoadingProgressView() {
        return super.getVideoLoadingProgressView();
    }

    /**
     * Obtains a list of all visited history items, used for link coloring
     *
     * @param callback
     */
    @Override
    public void getVisitedHistory(ValueCallback<String[]> callback) {
        super.getVisitedHistory(callback);
    }
    /**
     * Tell the client that the page being viewed has an autofillable
     * form and the user would like to set a profile up.
     * @param msg A Message to send once the user has successfully
     *      set up a profile and to inform the WebTextView it should
     *      now autofill using that new profile.
     */
    public void setupAutoFill(Message msg) {
    }


    // Android > 4.1.1 调用这个方法 webView上传文件
    /**
     * Tell the client to open a file chooser.
     * @param uploadFile A ValueCallback to set the URI of the file to upload.
     *      onReceiveValue must be called to wake up the thread.a
     * @param acceptType The value of the 'accept' attribute of the input tag
     *         associated with this file picker.
     * @param capture The value of the 'capture' attribute of the input tag
     *         associated with this file picker.
     * @hide
     */
    public void openFileChooser(ValueCallback<Uri> uploadFile,
                                String acceptType, String capture) {
//				Log.i("chooser1:acceptType=%s,capture=%s", acceptType + "",
//						capture + "");
        if (mUploadFileInvoke != null)
        {
            mUploadFileInvoke.invoke(uploadFile,acceptType);
        }

    }

    // 3.0 + 调用这个方法webView上传文件
    public void openFileChooser(ValueCallback<Uri> uploadFile,
                                String acceptType) {
//				Log.i("chooser2:acceptType=%s", acceptType + "");
        openFileChooser(uploadFile, acceptType, "");
    }

    // Android < 3.0 调用这个方法webView上传文件
    public void openFileChooser(ValueCallback<Uri> uploadFile) {

        openFileChooser(uploadFile, "", "");

    }

    public static abstract class UploadFileInvoke{
        Activity mActivity;
        int mRequestCode;
        public UploadFileInvoke(Activity activity,int requestCode)
        {
            mActivity = activity;
            mRequestCode = requestCode;
        }

        public void invoke(ValueCallback<Uri> uploadFile,String acceptType)
        {
            onCallBack(uploadFile);
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.addCategory(Intent.CATEGORY_OPENABLE);
            intent.setType(acceptType);
            mActivity.startActivityForResult(
                    Intent.createChooser(intent, "完成操作需要使用"),
                    mRequestCode);
        }

        public abstract void onCallBack(ValueCallback<Uri> uploadFile);
    }
}
