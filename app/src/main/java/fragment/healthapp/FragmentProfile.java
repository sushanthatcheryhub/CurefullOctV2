package fragment.healthapp;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogProfileFullView;
import item.property.PrescriptionImageList;
import utils.AppPreference;
import utils.CircularImageView;
import utils.HandlePermission;
import utils.MyConstants;
import utils.RequestBuilderOkHttp;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentProfile extends Fragment implements View.OnClickListener {
    private CircularImageView profile_image_view;
    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int SELECT_PHOTO = 12345;
    public static final int PICK_FROM_CROP = 203;
    private RelativeLayout realtive_notes;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera, liner_remove, liner_password;
    private float pixelDensity;
    boolean flag = true;
    private Animation alphaAnimation;
    private ImageView img_upload, img_gallery, img_camera, img_upload_animation;
    private EditText input_name, edt_phone, input_email;
    private TextView btn_click_change;
    private boolean isclick = false;
    private static Bitmap bitmap_image;
    private AlertDialog dialog;
    private String imge = "";
    private File dir;
    private File file;
    private String fileName = "my_image.jpg";

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
        btn_click_change = (TextView) rootView.findViewById(R.id.btn_click_change);
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
        liner_animation_upload.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        liner_remove.setOnClickListener(this);
        btn_click_change.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
        input_name.setText("" + AppPreference.getInstance().getUserName());
        edt_phone.setText("" + AppPreference.getInstance().getMobileNumber());
        input_email.setText("" + AppPreference.getInstance().getUserID());

        if (!AppPreference.getInstance().getProfileImage().equalsIgnoreCase("")) {
            try {
                CureFull.getInstanse().getSmallImageLoader().clearCache();
                CureFull.getInstanse().getSmallImageLoader().startLazyLoading(MyConstants.WebUrls.PROFILE_IMAGE_PATH + AppPreference.getInstance().getProfileImage(), profile_image_view);
            } catch (Exception e) {
            }
        }

        profile_image_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProfileFullView dialogProfileFullView = new DialogProfileFullView(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getProfileImage());
                dialogProfileFullView.show();

            }
        });

        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_click_change:
                if (isclick) {
                    isclick = false;
                    liner_password.setVisibility(View.GONE);
                } else {
                    isclick = true;
                    liner_password.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.liner_animation_upload:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload_animation);
                profile_image_view.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_upload_new:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload);
                profile_image_view.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_camera:
                if (HandlePermission.checkPermissionCamera(CureFull.getInstanse().getActivityIsntanse())) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_camera);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                }

                break;
            case R.id.liner_gallery:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_gallery);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:
            File file1 = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
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

                    Uri mCropImagedUri = Uri.fromFile(f);
                    cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCropImagedUri);
                    // start the activity - we handle returning in onActivityResult
                    startActivityForResult(cropIntent, PICK_FROM_CROP);

                }
            }

        } else if (requestCode == PICK_FROM_CROP) {
//            Uri selectedImageUri= data.getData();
//            String[] filePath = {MediaStore.Images.Media.DATA};
//            Cursor cursor = getActivity().getContentResolver().query(selectedImageUri, filePath, null, null, null);
//            cursor.moveToFirst();
//            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//            cursor.close();
            if (file.exists())
                sentSaveTestingServer(file.getPath());
        }
    }

    public File createNewFile() {
        dir = new File(Environment.getExternalStorageDirectory(), "App Pics");
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


    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        int x = realtive_notes.getRight();
        int y = realtive_notes.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());

        if (flag) {

//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);

            FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                    revealView.getLayoutParams();
            parameters.height = realtive_notes.getHeight();
            revealView.setLayoutParams(parameters);

            Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
            anim.setDuration(700);

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

            flag = false;
        } else {

//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);

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
            flag = true;
        }
    }


    private void sentSaveTestingServer(String fileName) {
        String response = "";
        RequestBuilderOkHttp builderOkHttp = new RequestBuilderOkHttp();
        try {
            response = builderOkHttp.postProfile(MyConstants.WebUrls.UPDATE_PROFILE, null, getFileParam(fileName));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.e("response :- ", "" + response);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        profile_image_view.post(new Runnable() {
            @Override
            public void run() {
                launchTwitter(rootView);
            }
        });

        if (!response.equalsIgnoreCase("null")) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.getString("responseStatus").equalsIgnoreCase("100")) {
                    AppPreference.getInstance().setProfileImage(jsonObject.getString("payload"));
                    try {
                        CureFull.getInstanse().getFullImageLoader().clearCache();
                        CureFull.getInstanse().getFullImageLoader().startLazyLoading(MyConstants.WebUrls.PROFILE_IMAGE_PATH + jsonObject.getString("payload"), profile_image_view);
                        CureFull.getInstanse().getActivityIsntanse().setActionDrawerProfilePic(jsonObject.getString("payload"));
                    } catch (Exception e) {

                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
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
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_camera);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + "image.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                }
                break;
        }
    }


}