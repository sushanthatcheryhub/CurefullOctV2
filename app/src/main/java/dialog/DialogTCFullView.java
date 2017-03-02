package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentTermCondition;
import image.zoom.GestureImageView;
import utils.AppPreference;


public class DialogTCFullView extends Dialog {

    private View v = null;
    Context context;
    WebView webView;

    public DialogTCFullView(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_t_c_full_view);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        webView = (WebView) findViewById(R.id.webView1);
        WebSettings webSetting = webView.getSettings();
        webSetting.setSupportZoom(true);
        webSetting.setJavaScriptEnabled(true);

        webView.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // chromium, enable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else {
            // older android version, disable hardware acceleration
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        webView.setLongClickable(false);
        webView.setHapticFeedbackEnabled(false);
        webView.setFocusableInTouchMode(false);
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setWebViewClient(new DialogTCFullView.WebViewClient());
        webView.loadUrl("file:///android_asset/term_condtion.html");
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("http:") || url.startsWith("https:")) {
//                webView.loadUrl(url);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
            } else if (url.startsWith("mail")) {
                /* Create the Intent */
                final Intent emailIntent = new Intent(Intent.ACTION_SEND);
/* Fill it with Data */
                emailIntent.setType("plain/text");
                emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{url.replace("mailto:", "")});
                emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "");
                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");
/* Send it off to the Activity-Chooser */
                context.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            }

            return true;
        }


        public WebViewClient() {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        }
    }
}