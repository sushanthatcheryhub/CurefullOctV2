package fragment.healthapp;


import android.animation.Animator;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import adpter.UploadPrescriptionAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import item.property.FilterDataPrescription;
import item.property.HealthNoteItems;
import item.property.PrescriptionImageList;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import utils.AppPreference;
import utils.HandlePermission;
import utils.MyConstants;
import utils.RequestBuilderOkHttp;
import utils.SpacesItemDecoration;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionCheck extends BaseBackHandlerFragment implements View.OnClickListener, IOnAddMoreImage, IOnDoneMoreImage, PopupWindow.OnDismissListener {


    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int SELECT_PHOTO = 12345;
    private RelativeLayout realtive_notes;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera;
    private float pixelDensity;
    boolean flag = true;
    private ImageView img_user_name, img_upload, img_gallery, img_camera, img_upload_animation;
    private RecyclerView prescriptionItemView;
    private GridLayoutManager lLayout;
    private UploadPrescriptionAdpter uploadPrescriptionAdpter;
    private List<PrescriptionListView> prescriptionListViewsDummy;
    private List<PrescriptionListView> prescriptionListViews = new ArrayList<>();
    private List<PrescriptionImageList> prescriptionImageLists;
    private String selectUploadPrescription = "";
    private int value = 0;
    private RequestQueue requestQueue;
    private ListPopupWindow listPopupWindow;
    private ListPopupWindow listPopupWindow1;
    private ListPopupWindow listPopupWindow2;
    private ListPopupWindow listPopupWindow3;
    private ListPopupWindow listPopupWindow4;
    private TextView txt_no_prescr, txt_sort_doctor_name, txt_disease_names, txt_upload_by, txt_dates;
    private LinearLayout txt_heath_note, txt_heath_app, txt_lab_reports;
    private TextView txt_sort_user_name;
    private List<UHIDItems> uhidItemses;
    private String path;
    private int imageName = 0;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, attectPosittion;
    private String fileName = "";
    private ImageView btn_reset, img_doctor_name, img_disease_name, img_upload_by, img_date;
    private String newMessage = "";
    private String clickDoctorName = "", clickDiseaseName = "", clickUploadBy = "", clickDates = "";
    private String checkDialog = "";
    private boolean isUploadClick = false;
    private String doctorName, dieaseName, prescriptionDate;
    private int offset = 0;
    private boolean isloadMore = false;
    private FilterDataPrescription filterDataPrescription;

    @Override
    public boolean onBackPressed() {

        if (isUploadClick) {
            isUploadClick = false;
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    launchTwitter(rootView);
                }
            });
            return false;
        } else {
            return true;
        }


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_presciption,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

        img_doctor_name = (ImageView) rootView.findViewById(R.id.img_doctor_name);
        img_date = (ImageView) rootView.findViewById(R.id.img_date);


        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);
        btn_reset = (ImageView) rootView.findViewById(R.id.btn_reset);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_heath_app);
        txt_lab_reports = (LinearLayout) rootView.findViewById(R.id.txt_lab_reports);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
        txt_dates = (TextView) rootView.findViewById(R.id.txt_dates);
        txt_no_prescr = (TextView) rootView.findViewById(R.id.txt_no_prescr);
        img_upload_animation = (ImageView) rootView.findViewById(R.id.img_upload_animation);
        img_gallery = (ImageView) rootView.findViewById(R.id.img_gallery);
        img_camera = (ImageView) rootView.findViewById(R.id.img_camera);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        pixelDensity = getResources().getDisplayMetrics().density;
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        liner_animation_upload = (LinearLayout) rootView.findViewById(R.id.liner_animation_upload);
        liner_upload_new = (LinearLayout) rootView.findViewById(R.id.liner_upload_new);
        liner_gallery = (LinearLayout) rootView.findViewById(R.id.liner_gallery);
        liner_camera = (LinearLayout) rootView.findViewById(R.id.liner_camera);
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);
//        img_upload_pre = (ImageView) rootView.findViewById(R.id.img_upload_pre);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
//        img_upload_pre.setOnClickListener(this);
//        liner_animation_upload.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        txt_heath_note.setOnClickListener(this);
        txt_heath_app.setOnClickListener(this);
        txt_lab_reports.setOnClickListener(this);
//        btn_reset.setOnClickListener(this);

        prescriptionItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        prescriptionItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        prescriptionItemView.setLayoutManager(lLayout);
        prescriptionItemView.setHasFixedSize(true);


        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        getAllHealthUserList();
        getPrescriptionList();
        getAllFilterData();


        (rootView.findViewById(R.id.img_user_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uhidItemses != null && uhidItemses.size() > 0) {
                    Log.e("no user", "no user");
                    checkDialog = "img_user_name";
                    rotatePhoneClockwise(img_user_name);
                    listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getUserAsStringList(uhidItemses)));
                    listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_sort_user_name));
                    listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow4.setModal(true);
                    listPopupWindow4.setOnDismissListener(FragmentPrescriptionCheck.this);
                    listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                    listPopupWindow4.show();
                }

            }
        });

        txt_sort_user_name.setSelected(true);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        Log.e("a_t", AppPreference.getInstance().getAt() + " r_t " + AppPreference.getInstance().getRt());
        return rootView;
    }


    AdapterView.OnItemClickListener popUpItemClickDoctor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_doctor_name);
            listPopupWindow.dismiss();
//            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(prescriptionListViews).get(position));
            if (filterDataPrescription.getDoctorNameList() != null & filterDataPrescription.getDoctorNameList().size() > 0) {
//                txt_disease_names.setText("Disease Name");
//                txt_upload_by.setText("Uploaded By");
//                txt_dates.setText("Date");
                clickDoctorName = filterDataPrescription.getDoctorNameList().get(position);
                txt_sort_doctor_name.setText("" + filterDataPrescription.getDoctorNameList().get(position));
                getAllFilterData();
                getPrescriptionList();
            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickdisease = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_disease_name);
            listPopupWindow1.dismiss();
            if (filterDataPrescription.getDiseaseNameList() != null & filterDataPrescription.getDiseaseNameList().size() > 0) {
                clickDiseaseName = "" + filterDataPrescription.getDiseaseNameList().get(position);
                txt_disease_names.setText("" + filterDataPrescription.getDiseaseNameList().get(position));
                getPrescriptionList();
            }
        }
    };


    AdapterView.OnItemClickListener popUpItemClickupload_by = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_upload_by);
            listPopupWindow2.dismiss();

            if (filterDataPrescription.getUploadedByList() != null & filterDataPrescription.getUploadedByList().size() > 0) {
                clickUploadBy = filterDataPrescription.getUploadedByList().get(position);
                txt_upload_by.setText("" + filterDataPrescription.getUploadedByList().get(position));
                getPrescriptionList();

            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickDate = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_date);
            listPopupWindow3.dismiss();
            if (filterDataPrescription.getDateList() != null & filterDataPrescription.getDateList().size() > 0) {
                clickDates = filterDataPrescription.getDateList().get(position);
                txt_dates.setText("" + filterDataPrescription.getDateList().get(position));
                getPrescriptionList();

            }
        }
    };


    AdapterView.OnItemClickListener popUpItemClickUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.e("yes user", "yes user");
            rotatePhoneAntiClockwise(img_user_name);
            listPopupWindow4.dismiss();
            if (uhidItemses != null && uhidItemses.size() > 0) {
                getSelectedUserList(getUserAsStringListUFHID(uhidItemses).get(position));
                txt_sort_user_name.setText("" + getUserAsStringList(uhidItemses).get(position));
                AppPreference.getInstance().setcf_uuhidNeew(getUserAsStringListUFHID(uhidItemses).get(position));
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                clickUploadBy = "";
                txt_sort_doctor_name.setText("Doctor Name");
                txt_disease_names.setText("Disease Name");
                txt_upload_by.setText("Uploaded By");
                txt_dates.setText("Date");
                getPrescriptionList();
//                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
//                        getFilterListDate(getDateAsStringList(prescriptionListViews).get(position)));
//                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
//                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
    };


    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_reset:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(btn_reset);
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                clickUploadBy = "";
                txt_sort_doctor_name.setText("Doctor Name");
                txt_disease_names.setText("Disease Name");
                txt_upload_by.setText("Uploaded By");
                txt_dates.setText("Date");
                getAllFilterData();
                getPrescriptionList();
                break;

            case R.id.txt_heath_note:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthNote(), true);
                break;
            case R.id.txt_heath_app:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthAppNew(), true);
                break;
            case R.id.txt_lab_reports:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLabTestReport(), true);
                break;


            case R.id.liner_animation_upload:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload_animation);
                isUploadClick = false;
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_upload_new:
                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    isUploadClick = true;
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
                    value = 0;
                    imageName = 0;
                    prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                    isUploadClick = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_camera);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                }

                break;
            case R.id.liner_gallery:
                value = 0;
                prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                isUploadClick = false;
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                selectUploadPrescription = "gallery";
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_gallery);
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
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

    Uri imageUri;

    private void cameraIntent() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = CureFull.getInstanse().getActivityIsntanse().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("requestCode", ":- " + requestCode);
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:
            fileName = Environment.getExternalStorageDirectory() + File.separator;
            Log.e("fileName", " " + fileName);
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.ARGB_8888;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
            prescriptionImageList.setImageNumber(value + 1);
            value = value + 1;
            imageName = imageName + 1;
            Log.e("parent", " " + Environment.getExternalStorageDirectory());
            prescriptionImageList.setPrescriptionImage(file.getAbsolutePath());
            prescriptionImageList.setChecked(false);
            prescriptionImageLists.add(prescriptionImageList);
            if (newMessage.equalsIgnoreCase("yes")) {
                PrescriptionImageList prescriptionImageList1 = new PrescriptionImageList();
                prescriptionImageList1.setImageNumber(000);
                prescriptionImageList1.setPrescriptionImage(null);
                prescriptionImageList1.setChecked(false);
                prescriptionImageLists.add(prescriptionImageList1);
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
                dialogFullViewClickImage.setiOnDoneMoreImage(this);
                dialogFullViewClickImage.show();
            } else {
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), bitmap, selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
            }

//            img_vew.setImageBitmap(bitmap);
        } else {
            if (data != null) {
                if (requestCode == SELECT_PHOTO) {
                    // Let's read picked image data - its URI
                    Uri pickedImage = data.getData();

                    // Let's read picked image path using content resolver
                    String[] filePath = {MediaStore.Images.Media.DATA};
                    Cursor cursor = getActivity().getContentResolver().query(pickedImage, filePath, null, null, null);
                    cursor.moveToFirst();
                    String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options);
                    PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
                    prescriptionImageList.setImageNumber(value + 1);
                    value = value + 1;

                    prescriptionImageList.setPrescriptionImage(imagePath);
                    prescriptionImageList.setChecked(false);
                    prescriptionImageLists.add(prescriptionImageList);

                    if (newMessage.equalsIgnoreCase("yes")) {
                        PrescriptionImageList prescriptionImageList1 = new PrescriptionImageList();
                        prescriptionImageList1.setImageNumber(000);
                        prescriptionImageList1.setPrescriptionImage(null);
                        prescriptionImageList1.setChecked(false);
                        prescriptionImageLists.add(prescriptionImageList1);
                        DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
                        dialogFullViewClickImage.setiOnDoneMoreImage(this);
                        dialogFullViewClickImage.show();
                    } else {
                        DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), bitmap, selectUploadPrescription, prescriptionImageLists);
                        dialogUploadNewPrescription.setiOnAddMoreImage(this);
                        dialogUploadNewPrescription.show();
                    }

//                img_vew.setImageBitmap(bitmap);
                    // Do something with the bitmap
                    // At the end remember to close the cursor or you will end with the RuntimeException!
                    cursor.close();
                }
            }

        }

    }

    public static Bitmap decodeSampledBitmapFromFile(String path, int reqWidth, int reqHeight) { // BEST QUALITY MATCH

        //First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        // Calculate inSampleSize, Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        int inSampleSize = 1;

        if (height > reqHeight) {
            inSampleSize = Math.round((float) height / (float) reqHeight);
        }
        int expectedWidth = width / inSampleSize;

        if (expectedWidth > reqWidth) {
            //if(Math.round((float)width / (float)reqWidth) > inSampleSize) // If bigger SampSize..
            inSampleSize = Math.round((float) width / (float) reqWidth);
        }

        options.inSampleSize = inSampleSize;

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(path, options);
    }

    private void showDialogOK(String message,
                              DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(CureFull.getInstanse().getActivityIsntanse()).setMessage(message).setCancelable(false)
                .setPositiveButton("Camera", okListener).setNegativeButton("Gallery", okListener).show();
    }


    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        int x = realtive_notes.getLeft()/2;
        int y = realtive_notes.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());
        try {
            if (flag) {
//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                            revealView.getLayoutParams();
                    parameters.height = realtive_notes.getHeight();
                    revealView.setLayoutParams(parameters);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealView, x, y, 0, hypotenuse);
                    anim.setDuration(700);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            layoutButtons.setVisibility(View.VISIBLE);
                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
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
        } catch (Exception e) {

        }


    }

    @Override
    public void optAddMoreImage(String messsage) {
        if (messsage.equalsIgnoreCase("done")) {
            PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
            prescriptionImageList.setImageNumber(000);
            prescriptionImageList.setPrescriptionImage(null);
            prescriptionImageList.setChecked(false);
            prescriptionImageLists.add(prescriptionImageList);
            DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
            dialogFullViewClickImage.setiOnDoneMoreImage(this);
            dialogFullViewClickImage.show();
        } else if (messsage.equalsIgnoreCase("retry")) {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

            } else {
                prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        } else {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        }
    }

    @Override
    public void optDoneMoreImage(final String doctorNames, final String dieaseNames, final String prescriptionDates, final List<PrescriptionImageList> prescriptionImageListss, String mesaage) {

        if (mesaage.equalsIgnoreCase("yes")) {
            prescriptionImageListss.remove(prescriptionImageListss.size() - 1);
            newMessage = mesaage;
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
            } else {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        } else {
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    isUploadClick = false;
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    doctorName = doctorNames;
                    dieaseName = dieaseNames;
                    prescriptionDate = prescriptionDates;
                    new LongOperation().execute(prescriptionImageListss);
//                    sentSaveTestingServer(doctorName, dieaseName, prescriptionDate, prescriptionImageListss);
                }
            });
        }


    }


    private void sentSaveTestingServer(String doctorName, String dieaseName, String prescriptionDate, List<PrescriptionImageList> prescriptionImage) {

    }


    public static HashMap<String, List<File>> getFileParam(List<PrescriptionImageList> image) {
        HashMap<String, List<File>> param = new HashMap<>();
        List<File> files = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            if (image.get(i).getImageNumber() != 000) {
                Log.e("run", " " + i);
                files.add(new File(image.get(i).getPrescriptionImage()));
            }
            param.put("prescriptionFile", files);
        }
        return param;
    }


    private class LongOperation extends AsyncTask<List<PrescriptionImageList>, Void, String> {


        @Override
        protected String doInBackground(List<PrescriptionImageList>... params) {

            List<PrescriptionImageList> prescriptionImage = params[0];
            String response = "";
            RequestBuilderOkHttp builderOkHttp = new RequestBuilderOkHttp();
            String removeSyptoms = "";
            try {
                for (int i = 0; i < prescriptionImage.size(); i++) {
                    if (prescriptionImage.get(i).getImageNumber() != 000) {

                        if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                            String imageName = prescriptionImage.get(i).getPrescriptionImage().replace(fileName, "");
                            removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + imageName + ",";
                            Log.e("check", "" + removeSyptoms);
                        } else {
                            int file = prescriptionImage.get(i).getPrescriptionImage().lastIndexOf("/");
                            String hello = prescriptionImage.get(i).getPrescriptionImage().substring(file + 1);
                            Log.e("fileName", " " + hello);
                            removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + hello + ",";
                            Log.e("check", "" + removeSyptoms);
                        }


                    }
                }
                if (removeSyptoms.endsWith(",")) {
                    removeSyptoms = removeSyptoms.substring(0, removeSyptoms.length() - 1);
                }
//            Log.e("request", " " + MyConstants.WebUrls.FILE_UPLOAD + "First:- "  + "Second:- " + getFileParam(myPath.getPath()));
                response = builderOkHttp.post(MyConstants.WebUrls.UPLOAD_PRESCRIPTION, null, getFileParam(prescriptionImage), doctorName, dieaseName, prescriptionDate, removeSyptoms);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("response :- ", "" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            getPrescriptionList();
            // might want to change "executed" for the returned string passed
            // into onPostExecute() but that is upto you
        }

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected void onProgressUpdate(Void... values) {
        }
    }


    private void getPrescriptionList() {
        isloadMore = false;
        offset = 0;
        prescriptionListViews = null;
        prescriptionListViews = new ArrayList<>();

        StringBuilder s = new StringBuilder();
        if (!clickDoctorName.equalsIgnoreCase("")) {
            s.append("&doctorName=" + clickDoctorName);
        }
        if (!clickDiseaseName.equalsIgnoreCase("")) {
            s.append("&diseaseName=" + clickDiseaseName);
        }
        if (!clickDates.equalsIgnoreCase("")) {
            s.append("&date=" + clickDates);
        }
        if (!clickUploadBy.equalsIgnoreCase("")) {
            s.append("&uploadedBy=" + clickUploadBy);
        }

        if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
            s.append("");
        }

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_PRESCRIPTION_LIST + "?limit=6&offset=" + offset + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("prescriptionlist", "" + response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            prescriptionListViewsDummy = ParseJsonData.getInstance().getPrescriptionList(response);
                            if (prescriptionListViewsDummy.size() > 0 && prescriptionListViewsDummy != null) {
                                prescriptionListViews.addAll(prescriptionListViewsDummy);
                                if (prescriptionListViewsDummy.size() < 6) {
                                    isloadMore = true;
                                }
                                AppPreference.getInstance().setPrescriptionSize(1);
                                txt_no_prescr.setVisibility(View.GONE);
                                prescriptionItemView.setVisibility(View.VISIBLE);
                                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(FragmentPrescriptionCheck.this, CureFull.getInstanse().getActivityIsntanse(),
                                        prescriptionListViews);
                                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                uploadPrescriptionAdpter.notifyDataSetChanged();
                            } else {
                                if (prescriptionListViewsDummy == null) {
                                    isloadMore = true;
                                }
                                AppPreference.getInstance().setPrescriptionSize(0);
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                prescriptionItemView.setVisibility(View.GONE);
                            }
                        } else {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            txt_no_prescr.setVisibility(View.VISIBLE);
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        txt_no_prescr.setVisibility(View.VISIBLE);
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    public void callWebServiceAgain(int offsets) {
        if (isloadMore) {

        } else {
            StringBuilder s = new StringBuilder();
            if (!clickDoctorName.equalsIgnoreCase("")) {
                s.append("&doctorName=" + clickDoctorName);
            }
            if (!clickDiseaseName.equalsIgnoreCase("")) {
                s.append("&diseaseName=" + clickDiseaseName);
            }
            if (!clickDates.equalsIgnoreCase("")) {
                s.append("&date=" + clickDates);
            }
            if (!clickUploadBy.equalsIgnoreCase("")) {
                s.append("&uploadedBy=" + clickUploadBy);
            }

            if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                s.append("");
            }
//            Log.e("offsect", "" + offset);
            offset = +offsets;
//            Log.e("off", "" + offsets);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_PRESCRIPTION_LIST + "?limit=6&offset=" + offset + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.e("prescriptionlist", "" + response);

                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                prescriptionListViewsDummy = ParseJsonData.getInstance().getPrescriptionList(response);
                                if (prescriptionListViewsDummy.size() > 0 && prescriptionListViewsDummy != null) {
                                    prescriptionListViews.addAll(prescriptionListViewsDummy);
                                    if (prescriptionListViewsDummy.size() < 6) {
                                        isloadMore = true;
                                    }
                                    AppPreference.getInstance().setPrescriptionSize(1);
                                    txt_no_prescr.setVisibility(View.GONE);
                                    prescriptionItemView.setVisibility(View.VISIBLE);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadPrescriptionAdpter.notifyDataSetChanged();
                                } else {
                                    if (prescriptionListViewsDummy == null) {
                                        isloadMore = true;
                                    }
//                                    AppPreference.getInstance().setPrescriptionSize(0);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                                    txt_no_prescr.setVisibility(View.VISIBLE);
//                                    prescriptionItemView.setVisibility(View.GONE);
                                }
                            } else {
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            txt_no_prescr.setVisibility(View.VISIBLE);
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
                    headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                    return headers;
                }
            };

            CureFull.getInstanse().getRequestQueue().add(postRequest);
        }

    }

    private List<String> getUserAsStringListUFHID(List<UHIDItems> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (UHIDItems logy : result)
                list.add(logy.getCfUuhid());
        return list;
    }

    private List<String> getDiseaseListAsStringList(List<PrescriptionListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (PrescriptionListView logy : result) {
                hsDoctorList.add(logy.getDiseaseName());
            }
        }
        return new ArrayList<String>(hsDoctorList);
    }

    private List<String> getDoctorNameAsStringList(List<PrescriptionListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (PrescriptionListView logy : result) {
                hsDoctorList.add(logy.getDoctorName());
            }
        }
        return new ArrayList<String>(hsDoctorList);
    }


    private List<String> getDateAsStringList(List<PrescriptionListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (PrescriptionListView logy : result) {
                hsDoctorList.add(logy.getPrescriptionDate());
            }
        }
        return new ArrayList<String>(hsDoctorList);

    }

    private List<String> getUserAsStringList(List<UHIDItems> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (UHIDItems logy : result)
                list.add(logy.getName());
        return list;
    }


    private List<String> getUploadByAsStringList(List<PrescriptionListView> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (PrescriptionListView logy : result)
                list.add(logy.getUploadedBy());
        return list;
    }


    public ArrayList<PrescriptionListView> getFilterListDoctor(String charSequence) {
        ArrayList<PrescriptionListView> searched = new ArrayList<PrescriptionListView>();
        for (PrescriptionListView str : prescriptionListViews) {
            if (str.getDoctorName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                searched.add(str);
            }

        }
        return searched;
    }


    public ArrayList<PrescriptionListView> getFilterListDisease(String charSequence) {
        ArrayList<PrescriptionListView> searched = new ArrayList<PrescriptionListView>();
        for (PrescriptionListView str : prescriptionListViews) {
            if (str.getDiseaseName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                searched.add(str);
            }

        }
        return searched;
    }

    public ArrayList<PrescriptionListView> getFilterListUploadBy(String charSequence) {
        ArrayList<PrescriptionListView> searched = new ArrayList<PrescriptionListView>();
        for (PrescriptionListView str : prescriptionListViews) {
            if (str.getUploadedBy().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                searched.add(str);
            }
        }
        return searched;
    }

    public ArrayList<PrescriptionListView> getFilterListDate(String charSequence) {
        ArrayList<PrescriptionListView> searched = new ArrayList<PrescriptionListView>();
        for (PrescriptionListView str : prescriptionListViews) {
            if (str.getPrescriptionDate().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                searched.add(str);
            }
        }
        return searched;
    }


    private void getAllHealthUserList() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CfUuhidList,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getUserList, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            uhidItemses = ParseJsonData.getInstance().getUHID(response);
                            if (uhidItemses != null && uhidItemses.size() > 0) {
                                for (int i = 0; i < uhidItemses.size(); i++) {
                                    if (uhidItemses.get(i).isSelected()) {
                                        txt_sort_user_name.setText("" + uhidItemses.get(i).getName());
                                    } else {
                                        if (uhidItemses.get(i).isDefaults())
                                            txt_sort_user_name.setText("" + uhidItemses.get(i).getName());
                                    }
                                }
                            } else {
                                txt_sort_user_name.setText("User Name");
                            }


                        } else {

                        }
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
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private void getAllFilterData() {

        StringBuilder s = new StringBuilder();
        if (!clickDoctorName.equalsIgnoreCase("")) {
            s.append("doctorName=" + clickDoctorName);
        }
        if (!clickDiseaseName.equalsIgnoreCase("")) {
            s.append("&diseaseName=" + clickDiseaseName);
        }
        if (!clickDates.equalsIgnoreCase("")) {
            s.append("&date=" + clickDates);
        }
        if (!clickUploadBy.equalsIgnoreCase("")) {
            s.append("&uploadedBy=" + clickUploadBy);
        }

        if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
            s.append("");
        }


        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.PRESCRIPTION_FILTER_DATA + s,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getFilterData, URL 1. ", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            filterDataPrescription = ParseJsonData.getInstance().getFilterDataPre(response);
                        } else {

                        }
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
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


//    class RetrieveFeedTask extends AsyncTask<String, Void, String> {
//
//        private Exception exception;
//
//        protected String doInBackground(String... urls) {
//            try {
//                String response = "";
//                RequestBuilderOkHttp builderOkHttp = new RequestBuilderOkHttp();
//                try {
////            Log.e("request", " " + MyConstants.WebUrls.FILE_UPLOAD + "First:- "  + "Second:- " + getFileParam(myPath.getPath()));
//
////                    Log.e("path new "," "+path);
//
//
//                    response = builderOkHttp.post(MyConstants.WebUrls.UPLOAD_PRESCRIPTION, null, getFileParam(path), "rahul", "cancer", "2016-12-07");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                return response;
//            } catch (Exception e) {
//                this.exception = e;
//
//                return null;
//            }
//        }
//
//        protected void onPostExecute(String feed) {
//            Log.e("yes",""+feed);
//            // TODO: check this.exception
//            // TODO: do something with the feed
//        }
//    }


    private void getSelectedUserList(String cfUuhid) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.SELECTED_USER_LIST + cfUuhid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("getUserList, URL 1.", response);
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                        } else {

                        }
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
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    public void checkList() {
        getPrescriptionList();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    value = 0;
                    imageName = 0;
                    isUploadClick = true;
                    prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_camera);
                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
                }
                break;
            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload);
                    isUploadClick = true;
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


    private void rotatePhoneClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_anti_rotate_anim);
        imageView.startAnimation(rotate);
    }

    private void rotatePhoneAntiClockwise(ImageView imageView) {
        Animation rotate = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.semi_rotate_anim);
        imageView.startAnimation(rotate);
    }

    @Override
    public void onDismiss() {
        if (checkDialog.equalsIgnoreCase("img_doctor_name")) {
            rotatePhoneAntiClockwise(img_doctor_name);
        } else if (checkDialog.equalsIgnoreCase("img_disease_name")) {
            rotatePhoneAntiClockwise(img_disease_name);
        } else if (checkDialog.equalsIgnoreCase("img_upload_by")) {
            rotatePhoneAntiClockwise(img_upload_by);
        } else if (checkDialog.equalsIgnoreCase("img_date")) {
            rotatePhoneAntiClockwise(img_date);
        } else if (checkDialog.equalsIgnoreCase("img_user_name")) {
            rotatePhoneAntiClockwise(img_user_name);
        }

    }
}