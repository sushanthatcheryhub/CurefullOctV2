package dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.Window;


import java.util.Random;

import curefull.healthapp.R;
import loader.AVLoadingIndicatorView;


public class DialogLoader extends Dialog {

    private View v = null;
    private AVLoadingIndicatorView loader1, loader2, loader3, loader4, loader5, loader6;

    public DialogLoader(Context _activiyt) {
        super(_activiyt, R.style.MyThemeAnimation);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dailog_loading);
        Window window = getWindow();
        v = window.getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        loader1 = (AVLoadingIndicatorView) findViewById(R.id.loader1);
        loader2 = (AVLoadingIndicatorView) findViewById(R.id.loader2);
        loader3 = (AVLoadingIndicatorView) findViewById(R.id.loader3);
        loader4 = (AVLoadingIndicatorView) findViewById(R.id.loader4);
        loader5 = (AVLoadingIndicatorView) findViewById(R.id.loader5);
        loader6 = (AVLoadingIndicatorView) findViewById(R.id.loader6);
        Random rand = new Random();
        int n = rand.nextInt(6);
        switch (n) {
            case 1:
                loader1.setVisibility(View.VISIBLE);
                loader2.setVisibility(View.GONE);
                loader3.setVisibility(View.GONE);
                loader4.setVisibility(View.GONE);
                loader5.setVisibility(View.GONE);
                loader6.setVisibility(View.GONE);

                break;
            case 2:
                loader1.setVisibility(View.GONE);
                loader2.setVisibility(View.VISIBLE);
                loader3.setVisibility(View.GONE);
                loader4.setVisibility(View.GONE);
                loader5.setVisibility(View.GONE);
                loader6.setVisibility(View.GONE);

                break;
            case 3:
                loader1.setVisibility(View.GONE);
                loader2.setVisibility(View.GONE);
                loader3.setVisibility(View.VISIBLE);
                loader4.setVisibility(View.GONE);
                loader5.setVisibility(View.GONE);
                loader6.setVisibility(View.GONE);

                break;
            case 4:
                loader1.setVisibility(View.GONE);
                loader2.setVisibility(View.GONE);
                loader3.setVisibility(View.GONE);
                loader4.setVisibility(View.VISIBLE);
                loader5.setVisibility(View.GONE);
                loader6.setVisibility(View.GONE);

                break;
            case 5:
                loader1.setVisibility(View.GONE);
                loader2.setVisibility(View.GONE);
                loader3.setVisibility(View.GONE);
                loader4.setVisibility(View.GONE);
                loader5.setVisibility(View.VISIBLE);
                loader6.setVisibility(View.GONE);

                break;

            default:
                loader1.setVisibility(View.GONE);
                loader2.setVisibility(View.GONE);
                loader3.setVisibility(View.GONE);
                loader4.setVisibility(View.GONE);
                loader5.setVisibility(View.GONE);
                loader6.setVisibility(View.VISIBLE);
                break;
        }


//        LoaderView loader = (LoaderView) findViewById(R.id.ldr_view_loader);
//        loader.showLoading();
    }

}