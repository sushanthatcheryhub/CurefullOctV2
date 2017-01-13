package dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;
import java.util.List;

import adpter.AddImageAdpter;
import adpter.UploadPrescriptionAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.MainActivity;
import curefull.healthapp.R;
import interfaces.IOnAddMoreImage;
import item.property.PrescriptionImageList;
import utils.RecyclerItemClickListener;
import utils.SpacesItemDecoration;


public class DialogUploadNewPrescription extends Dialog implements View.OnClickListener {

    private View v = null;
    Context context;
    private ImageView img_vew;
    private TextView btn_done, btn_add_more_image, btn_retry;
    private LinearLayout liner_mid;
    private String selectUploadPrescriptions;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int SELECT_PHOTO = 12345;
    private IOnAddMoreImage iOnAddMoreImage;
    private List<PrescriptionImageList> prescriptionImageListss;
    private RecyclerView recyclerViewAddImage;
    private LinearLayoutManager lLayout;
    private AddImageAdpter addImageAdpter;

    public DialogUploadNewPrescription(Context _activiyt, String bitmap, String selectUploadPrescription, List<PrescriptionImageList> prescriptionImageLists) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_camra_click_presciption);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        liner_mid = (LinearLayout) findViewById(R.id.liner_mid);
        img_vew = (ImageView) findViewById(R.id.img_vew);
        btn_done = (TextView) findViewById(R.id.btn_done);
        btn_add_more_image = (TextView) findViewById(R.id.btn_add_more_image);
        btn_retry = (TextView) findViewById(R.id.btn_retry);

        Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(Uri.fromFile(new File(bitmap)))
                .thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(img_vew);
        btn_add_more_image.setOnClickListener(this);
        btn_retry.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        selectUploadPrescriptions = selectUploadPrescription;
        prescriptionImageListss = prescriptionImageLists;
        if (prescriptionImageListss.size() > 0) {
            liner_mid.setVisibility(View.VISIBLE);
            recyclerViewAddImage = (RecyclerView) findViewById(R.id.grid_list_symptom);
            int spacingInPixels = 10;
            recyclerViewAddImage.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
            lLayout = new LinearLayoutManager(CureFull.getInstanse().getActivityIsntanse(), LinearLayoutManager.HORIZONTAL, false);
            recyclerViewAddImage.setLayoutManager(lLayout);
            recyclerViewAddImage.setHasFixedSize(true);
            addImageAdpter = new AddImageAdpter(CureFull.getInstanse().getActivityIsntanse(),
                    prescriptionImageListss);
            recyclerViewAddImage.setAdapter(addImageAdpter);
            addImageAdpter.notifyDataSetChanged();
            recyclerViewAddImage.addOnItemTouchListener(
                    new RecyclerItemClickListener(context, new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(Uri.fromFile(new File(prescriptionImageListss.get(position).getPrescriptionImage())))
                                    .thumbnail(0.5f)
                                    .crossFade()
                                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                                    .into(img_vew);
//                            BitmapFactory.Options options = new BitmapFactory.Options();
//                            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
//                            Bitmap bitmap = BitmapFactory.decodeFile(prescriptionImageListss.get(position).getPrescriptionImage(), options);
//                            img_vew.setImageBitmap(bitmap);
                        }
                    })
            );
        }
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_done:
                if (iOnAddMoreImage != null) {
                    iOnAddMoreImage.optAddMoreImage("done");
                    dismiss();
                }
                break;
            case R.id.btn_add_more_image:
                if (iOnAddMoreImage != null) {
                    iOnAddMoreImage.optAddMoreImage("More");
                    dismiss();
                }
                break;
            case R.id.btn_retry:
                if (iOnAddMoreImage != null) {
                    iOnAddMoreImage.optAddMoreImage("retry");
                    dismiss();
                }
                break;
        }
    }

    public IOnAddMoreImage getiOnAddMoreImage() {
        return iOnAddMoreImage;
    }

    public void setiOnAddMoreImage(IOnAddMoreImage iOnAddMoreImage) {
        this.iOnAddMoreImage = iOnAddMoreImage;
    }

}