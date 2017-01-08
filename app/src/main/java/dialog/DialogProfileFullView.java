package dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import image.zoom.GestureImageView;
import utils.AppPreference;
import utils.MyConstants;


public class DialogProfileFullView extends Dialog {

    private View v = null;
    Context context;
    private GestureImageView img_full_view;

    public DialogProfileFullView(Context _activiyt, String bitmap) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_full_view);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        img_full_view = (GestureImageView) findViewById(R.id.img_full_view);
        CureFull.getInstanse().getFullImageLoader().startLazyLoading( bitmap, img_full_view);


    }


}