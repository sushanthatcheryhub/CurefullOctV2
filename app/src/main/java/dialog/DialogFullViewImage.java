package dialog;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import adpter.AddImageDoneAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import image.zoom.GestureImageView;
import interfaces.IOnDoneMoreImage;
import item.property.PrescriptionImageList;
import utils.SpacesItemDecoration;


public class DialogFullViewImage extends Dialog {

    private View v = null;
    Context context;
    private GestureImageView img_full_view;

    public DialogFullViewImage(Context _activiyt, String bitmap) {
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

        Glide.with(context).load(Uri.fromFile(new File(bitmap)))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_full_view);
    }


}