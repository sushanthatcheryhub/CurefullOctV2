package fragment.healthapp;


import android.animation.Animator;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import asyns.JsonUtilsObject;
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
    private ImageView img_upload, img_gallery, img_camera, img_upload_animation, img_password_icon;
    private EditText input_name, edt_phone, input_email, input_old_pass, input_new_pass;
    private TextView btn_click_change, btn_save_changes;
    private boolean isclick = false;
    private static Bitmap bitmap_image;
    private AlertDialog dialog;
    private String imge = "";
    private File dir;
    private File file;
    private String fileName = "my_image.jpg";
    private RequestQueue requestQueue;

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
        btn_save_changes = (TextView) rootView.findViewById(R.id.btn_save_changes);
        btn_click_change = (TextView) rootView.findViewById(R.id.btn_click_change);
        img_password_icon = (ImageView) rootView.findViewById(R.id.img_password_icon);
        input_old_pass = (EditText) rootView.findViewById(R.id.input_old_pass);
        input_new_pass = (EditText) rootView.findViewById(R.id.input_new_pass);

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
        btn_save_changes.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
        input_name.setText("" + AppPreference.getInstance().getUserName());
        edt_phone.setText("" + AppPreference.getInstance().getMobileNumber());
        input_email.setText("" + AppPreference.getInstance().getUserID());

        if (!AppPreference.getInstance().getProfileImage().equalsIgnoreCase("")) {
            try {
                CureFull.getInstanse().getSmallImageLoader().clearCache();
                CureFull.getInstanse().getSmallImageLoader().startLazyLoading(AppPreference.getInstance().getProfileImage(), profile_image_view);
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
        btn_click_change.setPaintFlags(btn_click_change.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_save_changes:
                jsonUpdateProfile();

                break;
            case R.id.btn_click_change:
                if (isclick) {
                    img_password_icon.setImageResource(R.drawable.password_icon);
                    btn_click_change.setText("Change password");
                    isclick = false;
                    liner_password.setVisibility(View.GONE);
                } else {
                    isclick = true;
                    img_password_icon.setImageResource(R.drawable.cancel_red);
                    btn_click_change.setText("Don't want to change my password");
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
                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload);
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
            if (Build.VERSION.SDK_INT > 19) {
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
                        CureFull.getInstanse().getFullImageLoader().startLazyLoading(jsonObject.getString("payload"), profile_image_view);
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
            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload);
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
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());

        final String name = input_name.getText().toString().trim();
        final String mobNo = edt_phone.getText().toString().trim();
        final String emailId = input_email.getText().toString().trim();
        String oldPassowrd = "";
        String newPasswrod = "";
        if (isclick) {
            oldPassowrd = input_old_pass.getText().toString().trim();
            newPasswrod = input_new_pass.getText().toString().trim();
        }


        JSONObject data = JsonUtilsObject.getProfileDeatils(name, mobNo, emailId, oldPassowrd, newPasswrod);
        Log.e("jsonUpdateProfile", ": " + data.toString());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.PROFILE_UPDATE, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("Target, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                if (json.getString("payload").equals(null)) {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "No Changed");
                                } else {
                                    img_password_icon.setImageResource(R.drawable.password_icon);
                                    btn_click_change.setText("Change password");
                                    isclick = false;
                                    liner_password.setVisibility(View.GONE);
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + "Profile Changed");
                                    AppPreference.getInstance().setUserName(name);
                                    AppPreference.getInstance().setMobileNumber(mobNo);
                                    AppPreference.getInstance().setUserID(emailId);
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
                VolleyLog.e("FragmentLogin, URL 3.", "Error: " + error.getMessage());
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


}