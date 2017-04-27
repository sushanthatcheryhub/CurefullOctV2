package fragment.healthapp;


import android.animation.Animator;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.text.InputType;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amazonaws.auth.BasicSessionCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.JsonObjectRequest;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.sandrios.sandriosCamera.internal.SandriosCamera;
import com.sandrios.sandriosCamera.internal.configuration.CameraConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import ElasticVIews.ElasticAction;
import asyns.JsonUtilsObject;
import curefull.healthapp.BuildConfig;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogProfileFullView;
import interfaces.IOnEmailUpdate;
import interfaces.IOnOtpDonePath;
import interfaces.SmsListener;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.CircularImageView;
import utils.HandlePermission;
import utils.IncomingSms;
import utils.MyConstants;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentProfile extends Fragment implements View.OnClickListener, IOnOtpDonePath, IOnEmailUpdate {
    private CircularImageView profile_image_view;
    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT = 1778;
    private static final int CAPTURE_MEDIA = 368;
    public static final int SELECT_PHOTO = 12345;
    public static final int PICK_FROM_CROP = 203;
    private RelativeLayout realtive_notes;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera, liner_remove, liner_password;
    private float pixelDensity;
    boolean flag = true;
    private Animation alphaAnimation;
    private ImageView img_upload, img_gallery, img_camera, img_upload_animation, img_password_icon;
    private EditText input_name, edt_phone, input_email, input_old_pass, input_new_pass, edt_otp;
    private TextView btn_click_change, btn_save_changes;
    private boolean isclick = false;
    private static Bitmap bitmap_image;
    private AlertDialog dialog;
    private String imge = "";
    private File dir;
    private File file;
    private String fileName = "my_image.jpg";
    private int otp_number;
    private TextInputLayout input_layout_otp;
    private boolean isOtp = false;
    private boolean showPwd = false;
    private LinearLayout linearView;
    private String imageName = "" + System.currentTimeMillis();
    private ArrayList<Image> images = null;
    private int REQUEST_CODE_PICKER = 2000;
    private ProgressBar progressBar;
//    @Override
//    public boolean onBackPressed() {
//        CureFull.getInstanse().cancel();
//        CureFull.getInstanse().getFlowInstanse()
//                .replace(new FragmentLandingPage(), false);
//        return super.onBackPressed();
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_profile,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(1);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(true, "");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        CureFull.getInstanse().setiOnOtpDonePath(this);

        linearView = (LinearLayout) rootView.findViewById(R.id.linearView);
        progressBar = (ProgressBar) rootView.findViewById(R.id.progress_bar_one);
        input_layout_otp = (TextInputLayout) rootView.findViewById(R.id.input_layout_otp);
        btn_save_changes = (TextView) rootView.findViewById(R.id.btn_save_changes);
        btn_click_change = (TextView) rootView.findViewById(R.id.btn_click_change);
        img_password_icon = (ImageView) rootView.findViewById(R.id.img_password_icon);
        input_old_pass = (EditText) rootView.findViewById(R.id.input_old_pass);
        input_new_pass = (EditText) rootView.findViewById(R.id.input_new_pass);
        edt_otp = (EditText) rootView.findViewById(R.id.edt_otp);
        input_name = (EditText) rootView.findViewById(R.id.input_name);
        edt_phone = (EditText) rootView.findViewById(R.id.edt_phone);
        input_email = (EditText) rootView.findViewById(R.id.input_email);
        img_upload_animation = (ImageView) rootView.findViewById(R.id.img_upload_animation);
        img_gallery = (ImageView) rootView.findViewById(R.id.img_gallery);
        img_camera = (ImageView) rootView.findViewById(R.id.img_camera);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        profile_image_view = (CircularImageView) rootView.findViewById(R.id.profile_image_view);
        pixelDensity = getResources().getDisplayMetrics().density;
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        liner_password = (LinearLayout) rootView.findViewById(R.id.liner_password);
        liner_animation_upload = (LinearLayout) rootView.findViewById(R.id.liner_animation_upload);
        liner_upload_new = (LinearLayout) rootView.findViewById(R.id.liner_upload_new);
        liner_gallery = (LinearLayout) rootView.findViewById(R.id.liner_gallery);
        liner_camera = (LinearLayout) rootView.findViewById(R.id.liner_camera);
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        liner_remove = (LinearLayout) rootView.findViewById(R.id.liner_remove);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
//        img_upload_pre = (ImageView) rootView.findViewById(R.id.img_upload_pre);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
//        img_upload_pre.setOnClickListener(this);
        CureFull.getInstanse().setiOnEmailUpdate(this);
        liner_animation_upload.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        liner_remove.setOnClickListener(this);
        btn_click_change.setOnClickListener(this);
        btn_save_changes.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.alpha_anim);
        input_name.setText("" + AppPreference.getInstance().getUserName());
        edt_phone.setText("" + AppPreference.getInstance().getMobileNumber());
        input_email.setText("" + AppPreference.getInstance().getUserID());


        linearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_upload_animation, 400, 0.9f, 0.9f);
                profile_image_view.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
            }
        });

        if (AppPreference.getInstance().getProfileImage().equalsIgnoreCase("") || AppPreference.getInstance().getProfileImage().equalsIgnoreCase("null") || AppPreference.getInstance().getProfileImage().equalsIgnoreCase(null)) {
            progressBar.setVisibility(View.GONE);
        } else {
            Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(AppPreference.getInstance().getProfileImage())
                    .priority(Priority.HIGH)
                    .dontAnimate()
                    .placeholder(R.drawable.profile_avatar)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(profile_image_view);
        }


        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!AppPreference.getInstance().getProfileImage().equalsIgnoreCase("")) {
                    DialogProfileFullView dialogProfileFullView = new DialogProfileFullView(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getProfileImage());
                    dialogProfileFullView.show();
                }
            }
        });
        btn_click_change.setPaintFlags(btn_click_change.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);


        input_old_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (input_old_pass.getRight() - input_old_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (input_old_pass.getText().toString().length() > 0) {
                            if (!showPwd) {
                                showPwd = true;
                                input_old_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                                input_old_pass.setSelection(input_old_pass.getText().length());
                                input_old_pass.setTextSize(14f);
                                input_old_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_visible, 0);
                            } else {
                                showPwd = false;
                                input_old_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                input_old_pass.setSelection(input_old_pass.getText().length());
                                input_old_pass.setTextSize(14f);
                                input_old_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_hide, 0);

                                //confirmPassImage.setImageResource(R.drawable.username);//change Image here
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });

        input_new_pass.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;

                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (event.getRawX() >= (input_new_pass.getRight() - input_new_pass.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        // your action here
                        if (input_new_pass.getText().toString().length() > 0) {
                            if (!showPwd) {
                                showPwd = true;
                                input_new_pass.setInputType(InputType.TYPE_CLASS_TEXT);
                                input_new_pass.setSelection(input_new_pass.getText().length());
                                input_new_pass.setTextSize(14f);
                                input_new_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_visible, 0);
                            } else {
                                showPwd = false;
                                input_new_pass.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                input_new_pass.setSelection(input_new_pass.getText().length());
                                input_new_pass.setTextSize(14f);
                                input_new_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_icon, 0, R.drawable.password_hide, 0);

                                //confirmPassImage.setImageResource(R.drawable.username);//change Image here
                            }
                        }
                        return true;
                    }
                }
                return false;
            }
        });
//
//        edt_phone.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                if (s.length() > 9) {
//
//
//
//                }
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//
//            }
//        });

        IncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                edt_otp.setText("");
                String mgs = messageText.replace("Dear User ,\n" + "Your verification code is ", "");
                String again = mgs.replace("\nThanx for using Curefull. Stay Relief.", "");
                edt_otp.setText("" + again);
            }
        });
        input_new_pass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                        jsonUpdateProfile();
                    } else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

                    }

                }
                return false;
            }
        });


        return rootView;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_changes:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    jsonUpdateProfile();
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }

                break;
            case R.id.btn_click_change:
                if (isclick) {
                    img_password_icon.setImageResource(R.drawable.password_icon);
                    btn_click_change.setText("Change password");
                    isclick = false;
                    liner_password.setVisibility(View.GONE);
                } else {
                    isclick = true;
                    img_password_icon.setImageResource(R.drawable.remove_red);
                    btn_click_change.setText("Don't want to change my password");
                    liner_password.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.liner_animation_upload:

                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_upload_animation, 100, 0.9f, 0.9f);
                    profile_image_view.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                }

                break;
            case R.id.liner_upload_new:
                img_password_icon.setImageResource(R.drawable.password_icon);
                btn_click_change.setText("Change password");
                isclick = false;
                liner_password.setVisibility(View.GONE);
                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_upload, 400, 0.9f, 0.9f);
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                }
                break;
            case R.id.liner_camera:


                if (HandlePermission.checkPermissionCamera(CureFull.getInstanse().getActivityIsntanse())) {

                    new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                            .setShowPicker(false)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(true)
                            .launchCamera();
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
//                        ElasticAction.doAction(img_camera, 400, 0.9f, 0.9f);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//
//                    } else {
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                    }


                }

                break;
            case R.id.liner_gallery:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_gallery, 400, 0.9f, 0.9f);
                images = new ArrayList<>();
                start();
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                    photoPickerIntent.setType("image/*");
//                    startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                } else {
//                    pickFromGallery();
//                }

                break;

            case R.id.liner_remove:
                profile_image_view.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                profile_image_view.setImageBitmap(null);
                profile_image_view.setBackground(getResources().getDrawable(R.drawable.transarent_gray));
                break;
//            case R.id.img_upload_pre:
//                showDialogOK("Choose picture from Camera or Gallery !",
//                        new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog,
//                                                int which) {
//                                switch (which) {
//                                    case DialogInterface.BUTTON_POSITIVE:
//
//                                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
//                                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                                        break;
//
//                                    case DialogInterface.BUTTON_NEGATIVE:
//                                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                                        photoPickerIntent.setType("image/*");
//                                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//
//                                        break;
//
//                                }
//                            }
//                        });
//
//
//                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != 0) {

//            if (requestCode == CAPTURE_MEDIA && resultCode == RESULT_OK) {
//                String path = data.getStringExtra(CameraConfiguration.Arguments.FILE_PATH);
//                Log.e("File", " " + data.getStringExtra(CameraConfiguration.Arguments.FILE_PATH));
//                CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
//                getSaveUploadPrescriptionMetadata(new File(path));
//            }

            if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {

                images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    File file1 = new File(images.get(0).getPath());
//                Utils.uploadFile("", "", "", file1);
                    Uri pickedImage = Uri.fromFile(file1);
                    // Let's read picked image path using content resolver
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    // indicate image type and Uri
                    cropIntent.setDataAndType(pickedImage, "image/*");
                    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    // set crop properties
                    cropIntent.putExtra("crop", "true");
                    // indicate aspect of desired crop
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 300);
                    cropIntent.putExtra("outputY", 300);
                    cropIntent.putExtra("scale", true);

                    // retrieve data on return
                    cropIntent.putExtra("return-data", false);
                    File f = createNewFile();
//                    Log.e("f", " " + f.getPath());

                    Uri mCropImagedUri = Uri.fromFile(f);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                    // start the activity - we handle returning in onActivityResult
                    startActivityForResult(cropIntent, PICK_FROM_CROP);
                } else {
                    File file1 = new File(images.get(0).getPath());
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    getSaveUploadPrescriptionMetadata(new File(file1.getPath()));
//                Utils.uploadFile("", "", "", file1);
//                    Uri pickedImage = Uri.fromFile(file1);
                    // Let's read picked image path using content resolver
//                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                    // indicate image type and Uri
//                    Uri photoURI1 = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            file1);
//                    cropIntent.setDataAndType(photoURI1, "image/*");
//                    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    // set crop properties
//                    cropIntent.putExtra("crop", "true");
//                    // indicate aspect of desired crop
//                    cropIntent.putExtra("aspectX", 1);
//                    cropIntent.putExtra("aspectY", 1);
//                    cropIntent.putExtra("outputX", 300);
//                    cropIntent.putExtra("outputY", 300);
//                    cropIntent.putExtra("scale", true);
//
//                    // retrieve data on return
//                    cropIntent.putExtra("return-data", false);
//                    File f = createNewFile();
//                    Log.e("f", " " + f.getPath());
//                    Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            f);
////                    Uri mCropImagedUri = Uri.fromFile(f);
//                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    // start the activity - we handle returning in onActivityResult
//                    startActivityForResult(cropIntent, PICK_FROM_CROP);
                }

            }

            if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                    File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                    Uri pickedImage = Uri.fromFile(file1);
                    // Let's read picked image path using content resolver
                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
                    // indicate image type and Uri
                    cropIntent.setDataAndType(pickedImage, "image/*");
                    // set crop properties

                    cropIntent.putExtra("crop", "true");
                    // indicate aspect of desired crop
                    cropIntent.putExtra("aspectX", 1);
                    cropIntent.putExtra("aspectY", 1);
                    cropIntent.putExtra("outputX", 300);
                    cropIntent.putExtra("outputY", 300);
                    cropIntent.putExtra("scale", true);

                    // retrieve data on return
                    cropIntent.putExtra("return-data", false);
                    File f = createNewFile();
//                    Log.e("f", " " + f.getPath());

                    Uri mCropImagedUri = Uri.fromFile(f);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    // start the activity - we handle returning in onActivityResult
                    startActivityForResult(cropIntent, PICK_FROM_CROP);
                } else {
                    File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    getSaveUploadPrescriptionMetadata(new File(file1.getPath()));
//                imageUpload(file.getPath());
//                sentSaveTestingServer(file.getPath());

//                Utils.uploadFile("", "", "", file1);
//                    Uri pickedImage = Uri.fromFile(file1);
//                    // Let's read picked image path using content resolver
//                    Intent cropIntent = new Intent("com.android.camera.action.CROP");
//                    // indicate image type and Uri
//
//                    Uri photoURI1 = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            file1);
//                    cropIntent.setDataAndType(photoURI1, "image/*");
//                    // set crop properties
//                    cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//                    cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                    cropIntent.putExtra("crop", "true");
//                    // indicate aspect of desired crop
//                    cropIntent.putExtra("aspectX", 1);
//                    cropIntent.putExtra("aspectY", 1);
//                    cropIntent.putExtra("outputX", 300);
//                    cropIntent.putExtra("outputY", 300);
//                    cropIntent.putExtra("scale", true);
//
//                    // retrieve data on return
//                    cropIntent.putExtra("return-data", false);
//                    File f = createNewFile();
//                    Log.e("f", " " + f.getPath());
//
////                Uri mCropImagedUri = Uri.fromFile(f);
//                    Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            f);
//                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//
//                    // start the activity - we handle returning in onActivityResult
//                    startActivityForResult(cropIntent, PICK_FROM_CROP);
                }
                //Get our saved file into a bitmap object:


//            sentSaveTestingServer(file.getAbsolutePath());
            } else if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT) {
                //Get our saved file into a bitmap object:
                File file1 = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                Utils.uploadFile("", "", "", file1);
                Uri pickedImage = Uri.fromFile(file1);
                // Let's read picked image path using content resolver
                Intent cropIntent = new Intent("com.android.camera.action.CROP");
                // indicate image type and Uri
                cropIntent.setDataAndType(pickedImage, "image/*");
                // set crop properties
                cropIntent.putExtra("crop", "true");
                // indicate aspect of desired crop
                cropIntent.putExtra("aspectX", 1);
                cropIntent.putExtra("aspectY", 1);
                cropIntent.putExtra("outputX", 300);
                cropIntent.putExtra("outputY", 300);
                cropIntent.putExtra("scale", true);
                // retrieve data on return
                cropIntent.putExtra("return-data", false);
                File f = createNewFile();
//                Log.e("f", " " + f.getPath());

                Uri mCropImagedUri = Uri.fromFile(f);
                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                // start the activity - we handle returning in onActivityResult
                startActivityForResult(cropIntent, PICK_FROM_CROP);

//            sentSaveTestingServer(file.getAbsolutePath());
            } else if (requestCode == SELECT_PHOTO) {
                if (data != null) {
                    if (requestCode == SELECT_PHOTO) {
                        // Let's read picked image data - its URI
                        Uri pickedImage = data.getData();

                        // Let's read picked image path using content resolver
                        Intent cropIntent = new Intent("com.android.camera.action.CROP");
                        // indicate image type and Uri
                        cropIntent.setDataAndType(pickedImage, "image/*");
                        //you must setup two line below
                        cropIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        cropIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                        // set crop properties
                        cropIntent.putExtra("crop", "true");
                        // indicate aspect of desired crop
                        cropIntent.putExtra("aspectX", 1);
                        cropIntent.putExtra("aspectY", 1);
                        cropIntent.putExtra("outputX", 300);
                        cropIntent.putExtra("outputY", 300);
                        cropIntent.putExtra("scale", true);

                        // retrieve data on return
                        cropIntent.putExtra("return-data", false);
                        File f = createNewFile();
//                        Log.e("f", " " + f.getPath());
                        Uri mCropImagedUri = Uri.fromFile(f);
                        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                        // start the activity - we handle returning in onActivityResult
                        startActivityForResult(cropIntent, PICK_FROM_CROP);

                    }
                }

            } else if (requestCode == PICK_FROM_CROP) {
//                    CureFull.getInstanse().getActivityIsntanse().grantUriPermission(CureFull.getInstanse().getActivityIsntanse().revokeUriPermissionfileUri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);

//            Uri selectedImageUri= data.getData();
//            String[] filePath = {MediaStore.Images.Media.DATA};
//            Cursor cursor = CureFull.getInstanse().getActivityIsntanse().getContentResolver().query(selectedImageUri, filePath, null, null, null);
//            cursor.moveToFirst();
//            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//            cursor.close();
//                Log.e("file.exists ", " " + file.exists() + " " + file.getPath());
                if (file.exists()) {
//                    Log.e("file", " " + file.getPath());
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    getSaveUploadPrescriptionMetadata(new File(file.getPath()));
//                imageUpload(file.getPath());
//                sentSaveTestingServer(file.getPath());
                }
            }

        }


    }

    public File createNewFile() {
        dir = new File(Environment.getExternalStorageDirectory().getPath(), "profilepics");
        if (!dir.exists())
            dir.mkdir();
        if (file == null)
            file = new File(dir, fileName);
        if (file.exists()) {
            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }


    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        int x = realtive_notes.getRight();
        int y = realtive_notes.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());

//        Log.e("flag", " " + flag);

        if (flag) {

//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
            if (Build.VERSION.SDK_INT > 19) {
                FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                        revealView.getLayoutParams();
                parameters.height = realtive_notes.getHeight();
                revealView.setLayoutParams(parameters);

                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
                anim.setDuration(100);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        layoutButtons.setVisibility(View.VISIBLE);
                        layoutButtons.startAnimation(alphaAnimation);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                revealView.setVisibility(View.VISIBLE);
                anim.start();

            } else {
                revealView.setVisibility(View.VISIBLE);
                layoutButtons.setVisibility(View.VISIBLE);
            }

            flag = false;
        } else {

//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
            if (Build.VERSION.SDK_INT > 19) {
                Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, hypotenuse, 0);
                anim.setDuration(400);

                anim.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animator) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animator) {
                        revealView.setVisibility(View.GONE);
                        layoutButtons.setVisibility(View.GONE);
                    }

                    @Override
                    public void onAnimationCancel(Animator animator) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animator) {

                    }
                });

                anim.start();
            } else {
                revealView.setVisibility(View.GONE);
                layoutButtons.setVisibility(View.GONE);
            }

            flag = true;
        }
    }


    public static HashMap<String, File> getFileParam(String image) {
        HashMap<String, File> param = new HashMap<>();
        param.put("profileImage", new File(image));
        return param;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_camera, 400, 0.9f, 0.9f);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
                                BuildConfig.APPLICATION_ID + ".provider",
                                file);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                    } else {
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
                    }

                }
                break;
            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_upload, 400, 0.9f, 0.9f);
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                }
                break;
        }
    }


    public void jsonUpdateProfile() {

        if (!validateName()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Name cannot be left blank.");
            return;
        }
        if (!validateEmail()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Email Id cannot be left blank.");
            return;
        }
        if (!validateMobileNo()) {
            return;
        }

        if (isOtp) {
            if (!validateOtp()) {
                return;
            }
        }


        final String name;
        final String mobNo;
        final String emailId;

        if (input_name.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getUserName().trim())) {
            name = "";
        } else {
            name = input_name.getText().toString().trim();
        }

        if (edt_phone.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getMobileNumber().trim())) {
            mobNo = "";
        } else {
            mobNo = edt_phone.getText().toString().trim();
        }

        if (input_email.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getUserID().trim())) {
            emailId = "";
        } else {
            emailId = input_email.getText().toString().trim();
        }


        String oldPassowrd = "";
        String newPasswrod = "";
        if (isclick) {
            if (!validateOldPassword()) {
                return;
            }
            if (!validateNewPassword()) {
                return;
            }


            oldPassowrd = input_old_pass.getText().toString().trim();
            if (!oldPassowrd.equalsIgnoreCase(AppPreference.getInstance().getPassword())) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Old Password not Match");
                return;
            }
            newPasswrod = input_new_pass.getText().toString().trim();
        }

        if (name.equalsIgnoreCase("") && mobNo.equalsIgnoreCase("") && emailId.equalsIgnoreCase("") && oldPassowrd.equalsIgnoreCase("") && newPasswrod.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "No Change in profile");
            return;
        }


        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        JSONObject data = JsonUtilsObject.getProfileDeatils(name, mobNo, emailId, oldPassowrd, newPasswrod);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.PROFILE_UPDATE, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (json.getString("payload").equals(null) || json.getString("payload").equalsIgnoreCase("No Data")) {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "No changed in profile");
                                } else {

                                    if (!isOtp) {
                                        if (!AppPreference.getInstance().getMobileNumber().equalsIgnoreCase(edt_phone.getText().toString().trim())) {
                                            isOtp = true;
                                            sendOTPService();
                                            CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                                            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Otp Sent Please check your mobile");
                                            input_layout_otp.setVisibility(View.VISIBLE);
                                            return;
                                        } else {
                                            isOtp = false;
                                            input_layout_otp.setVisibility(View.GONE);
                                        }

                                    }
                                    isOtp = false;
                                    img_password_icon.setImageResource(R.drawable.password_icon);
                                    btn_click_change.setText("Change password");
                                    liner_password.setVisibility(View.GONE);
                                    input_layout_otp.setVisibility(View.GONE);
                                    edt_otp.setText("");
                                    if (input_name.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getUserName().trim()) && edt_phone.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getMobileNumber().trim()) && input_email.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getUserID().trim())) {
                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Your Password Successfully Changed");
                                    } else {
                                        if (isclick) {
                                            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Your Password Successfully Changed");
                                        } else {
                                            if (!input_email.getText().toString().trim().equalsIgnoreCase(AppPreference.getInstance().getUserID().trim())) {
                                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Please check your email id for verification");
                                            } else {
                                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Your Profile is up to date now.");
                                            }


                                        }
                                    }

                                    AppPreference.getInstance().setUserName(input_name.getText().toString().trim());
                                    AppPreference.getInstance().setMobileNumber(edt_phone.getText().toString().trim());


                                    if (isclick) {
                                        AppPreference.getInstance().setPassword("" + input_new_pass.getText().toString().trim());
                                        input_old_pass.setText("");
                                        input_new_pass.setText("");
                                        isclick = false;
                                    }

                                    CureFull.getInstanse().getActivityIsntanse().setActionDrawerHeading(AppPreference.getInstance().getUserName(), AppPreference.getInstance().getcf_uuhid());
                                }

                            } else {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
//                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }


    private boolean validateName() {
        String email = input_name.getText().toString().trim();
        if (email.isEmpty()) {
            return false;
        } else {
        }
        return true;
    }

    private boolean validateOldPassword() {
        String email = input_old_pass.getText().toString().trim();
        if (email.isEmpty()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter Old Password");
            return false;
        } else {
        }
        return true;
    }

    private boolean validateNewPassword() {
        String email = input_new_pass.getText().toString().trim();
        if (email.isEmpty()) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Please Enter New Password");
            return false;
        } else {
        }
        return true;
    }


    private boolean validateMobileNo() {
        String email = edt_phone.getText().toString().trim();

        if (email.isEmpty() || email.length() != 10) {
            if (email.length() < 10 && email.length() > 1) {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be less than 10 numbers.");
            } else {
                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "Mobile Number cannot be left blank.");
            }
            return false;

        }
        return true;
    }

    private boolean validateEmail() {
        String email = input_email.getText().toString().trim();
        if (email.isEmpty() || !isValidEmail(email)) {
            return false;
        }
        return true;
    }

    private boolean validateOtp() {
        String email = edt_otp.getText().toString().trim();
        if (email.equalsIgnoreCase("")) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "OTP can not be left blank.");
            return false;
        } else if (!email.equalsIgnoreCase("" + otp_number)) {
            CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "OTP not matched.");
            return false;
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void sendOTPService() {
        Random rnd = new Random();
        otp_number = 100000 + rnd.nextInt(900000);

        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + edt_phone.getText().toString().trim() + MyConstants.WebUrls.OTP_MESSAGE + "Dear%20User%20,%0AYour%20verification%20code%20is%20" + String.valueOf(otp_number) + "%0AThanx%20for%20using%20Curefull.%20Stay%20Relief." + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private void getSaveUploadPrescriptionMetadata(final File file) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.TEMPORY_CREDENTIALS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (!json.getString("payload").equals(null)) {
                                    JSONObject json1 = new JSONObject(json.getString("payload"));
                                    String accessKeyID = json1.getString("accessKeyID");
                                    String secretAccessKey = json1.getString("secretAccessKey");
                                    String sessionToken = json1.getString("sessionToken");
                                    uploadFile(accessKeyID, secretAccessKey, sessionToken, MyConstants.AWSType.BUCKET_PROFILE_NAME + MyConstants.AWSType.FOLDER_PROFILE_NAME, file);
                                }

                            } else {
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    public void uploadFile(String accessKeyID, String secretAccessKey, String sessionToken, String bucketName, File fileUpload) {

        String imageUploadUrl = null;

        BasicSessionCredentials credentials =
                new BasicSessionCredentials(accessKeyID,
                        secretAccessKey,
                        sessionToken);
        final AmazonS3 s3client = new AmazonS3Client(credentials);
        s3client.setRegion(Region.getRegion(Regions.AP_SOUTH_1));
        try {

            if (!(s3client.doesBucketExist(bucketName))) {
                // Note that CreateBucketRequest does not specify region. So bucket is
                // created in the region specified in the client.
                s3client.createBucket(new CreateBucketRequest(
                        bucketName));
            }


        } catch (Exception e) {

        }

        TransferUtility transferUtility = new TransferUtility(s3client, CureFull.getInstanse().getActivityIsntanse());
        // Request server-side encryption.
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
        try {
            String[] spiltName = fileUpload.getName().split("\\.");
            String getName = spiltName[1];
            final String name = "profile-img-" + AppPreference.getInstance().getUserIDProfile() + "." + getName;
            final TransferObserver observer = transferUtility.upload(
                    bucketName,
                    name,
                    fileUpload, CannedAccessControlList.PublicRead
            );
            observer.setTransferListener(new TransferListener() {
                @Override
                public void onStateChanged(int id, TransferState state) {
                    switch (state.name()) {
                        case "COMPLETED":
                            sendImgProfileToServer("https://s3.ap-south-1.amazonaws.com/" + MyConstants.AWSType.BUCKET_PROFILE_NAME + "/" + "profileImages/" + name);
                            break;

                    }
                }

                @Override
                public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
                }

                @Override
                public void onError(int id, Exception ex) {
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }


//        TransferObserver observer = transferUtility.download(
//                "curefull.storage.test/cure.ehr",
//                "",
//                imageFile
//        );

//        for (Bucket bucket : s3client.listBuckets()) {
//            Log.e("Bucket list - ", bucket.getName());
//        }
//            observer.refresh();

    }


    private void sendImgProfileToServer(final String url) {
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.UPLOADED_PROFILE + url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        getFilenameDelete();
                        AppPreference.getInstance().setProfileImage(url);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        progressBar.setVisibility(View.VISIBLE);
                        Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(url)
                                .priority(Priority.HIGH)
                                .dontAnimate()
                                .placeholder(R.drawable.profile_avatar)
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)
                                .listener(new RequestListener<String, GlideDrawable>() {
                                    @Override
                                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                                        progressBar.setVisibility(View.GONE);
                                        return false;
                                    }
                                })
                                .into(profile_image_view);
//                        CureFull.getInstanse().getSmallImageLoader().clearCache();
//                        CureFull.getInstanse().getSmallImageLoader().startLazyLoading(url, profile_image_view);
//                                        Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(jsonObject.getString("payload"))
//                                                .thumbnail(0.5f)
//                                                .diskCacheStrategy(DiskCacheStrategy.NONE)
//                                                .skipMemoryCache(true)
//                                                .into(profile_image_view);


                        CureFull.getInstanse().getActivityIsntanse().setActionDrawerProfilePic(url);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        error.printStackTrace();
                    }
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("a_t", AppPreference.getInstance().getAt());
                headers.put("r_t", AppPreference.getInstance().getRt());
                headers.put("user_name", AppPreference.getInstance().getUserName());
                headers.put("email_id", AppPreference.getInstance().getUserID());
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
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

        float maxHeight = 300.0f;
        float maxWidth = 300.0f;
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
//            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
//                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
//                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
//                Log.d("EXIF", "Exif: " + orientation);
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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "profile");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }


    public void getFilenameDelete() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "profile");
        if (file.exists()) {
            file.delete();
        }

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = CureFull.getInstanse().getActivityIsntanse().getContentResolver().query(contentUri, null, null, null, null);
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


    // Recomended builder
    public void start() {
        CureFull.getInstanse().setPostionGet(2001);
        ImagePicker.create(this)
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("CureFull") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .limit(1) // max images can be selected (999 by default)
                .showCamera(false) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }


    @Override
    public void optDonePath(ArrayList<Image> path, String pathCAmera, int id) {
        profile_image_view.post(new Runnable() {
            @Override
            public void run() {
                launchTwitter(rootView);
            }
        });
        if (id == CAPTURE_MEDIA) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            getSaveUploadPrescriptionMetadata(new File(pathCAmera));
        } else {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            getSaveUploadPrescriptionMetadata(new File(path.get(0).getPath()));
        }

    }

    @Override
    public void optEmailUpdate() {
        input_email.setText("" + AppPreference.getInstance().getUserID());
        input_email.clearFocus();
    }
}