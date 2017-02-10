package dialog;

import android.app.Dialog;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import adpter.AddImageAdpter;
import curefull.healthapp.CureFull;
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


        btn_add_more_image.setOnClickListener(this);
        btn_retry.setOnClickListener(this);
        btn_done.setOnClickListener(this);
        selectUploadPrescriptions = selectUploadPrescription;
        prescriptionImageListss = prescriptionImageLists;
        if (prescriptionImageListss.size() > 0) {
            Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(Uri.fromFile(new File(compressImage(prescriptionImageListss.get(prescriptionImageListss.size() - 1).getPrescriptionImage()))))
                    .into(img_vew);
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
                            Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(Uri.fromFile(new File(compressImage(prescriptionImageListss.get(position).getPrescriptionImage()))))
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

    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        String filename = getFilename();
        try {
            out = new FileOutputStream(filename);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 50, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }
}