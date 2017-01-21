package fragment.healthapp;


import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ProgressBar;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentTermCondition extends Fragment {


    private View rootView;
    WebView webView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_term_condtion,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(8);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        webView = (WebView) rootView.findViewById(R.id.webView1);
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
        webView.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
        webView.setBackgroundColor(Color.TRANSPARENT);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("file:///android_asset/term_condtion.html");
        return rootView;
    }

    private class WebViewClient extends android.webkit.WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
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