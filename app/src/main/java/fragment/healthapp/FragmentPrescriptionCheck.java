package fragment.healthapp;


import android.animation.Animator;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import java.util.List;
import java.util.Map;

import adpter.UploadPrescriptionAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import item.property.PrescriptionImageList;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import utils.AppPreference;
import utils.MyConstants;
import utils.RequestBuilderOkHttp;
import utils.SpacesItemDecoration;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionCheck extends Fragment implements View.OnClickListener, IOnAddMoreImage, IOnDoneMoreImage {


    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int SELECT_PHOTO = 12345;
    private RelativeLayout realtive_notes;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera;
    private float pixelDensity;
    boolean flag = true;
    private Animation alphaAnimation;
    private ImageView img_user_name, img_upload, img_gallery, img_camera, img_upload_animation;
    private RecyclerView prescriptionItemView;
    private GridLayoutManager lLayout;
    private UploadPrescriptionAdpter uploadPrescriptionAdpter;
    private List<PrescriptionListView> prescriptionListViews;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_presciption,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_heath_app);
        txt_lab_reports = (LinearLayout) rootView.findViewById(R.id.txt_lab_reports);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
        txt_dates = (TextView) rootView.findViewById(R.id.txt_dates);
        txt_upload_by = (TextView) rootView.findViewById(R.id.txt_upload_by);
        txt_disease_names = (TextView) rootView.findViewById(R.id.txt_disease_names);
        txt_sort_doctor_name = (TextView) rootView.findViewById(R.id.txt_sort_doctor_name);
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
        liner_animation_upload.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        txt_heath_note.setOnClickListener(this);
        txt_heath_app.setOnClickListener(this);
        txt_lab_reports.setOnClickListener(this);

        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
        prescriptionItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        prescriptionItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        prescriptionItemView.setLayoutManager(lLayout);
        prescriptionItemView.setHasFixedSize(true);


        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        getAllHealthUserList();
        getPrescriptionList();

        (rootView.findViewById(R.id.img_doctor_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, getDoctorNameAsStringList(prescriptionListViews)));
                listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_sort_doctor_name));
                listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow.setModal(true);
                listPopupWindow.setOnItemClickListener(popUpItemClickDoctor);
                listPopupWindow.show();
            }
        });


        (rootView.findViewById(R.id.img_disease_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow1 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow1.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, getDiseaseListAsStringList(prescriptionListViews)));
                listPopupWindow1.setAnchorView(rootView.findViewById(R.id.txt_disease_names));
                float width = getResources().getDimension(R.dimen._190dp);
                listPopupWindow1.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow1.setModal(true);
                listPopupWindow1.setOnItemClickListener(popUpItemClickdisease);
                listPopupWindow1.show();
            }
        });


        (rootView.findViewById(R.id.img_upload_by)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow2 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow2.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, MyConstants.IArrayData.listUploadBy));
                listPopupWindow2.setAnchorView(rootView.findViewById(R.id.txt_upload_by));
                float width = getResources().getDimension(R.dimen._190dp);
                listPopupWindow2.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow2.setModal(true);
                listPopupWindow2.setOnItemClickListener(popUpItemClickupload_by);
                listPopupWindow2.show();
            }
        });

        (rootView.findViewById(R.id.img_date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow3 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow3.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, getDateAsStringList(prescriptionListViews)));
                listPopupWindow3.setAnchorView(rootView.findViewById(R.id.txt_dates));
                listPopupWindow3.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow3.setModal(true);
                listPopupWindow3.setOnItemClickListener(popUpItemClickDate);
                listPopupWindow3.show();
            }
        });


        (rootView.findViewById(R.id.img_user_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                        R.layout.adapter_list_doctor_data, getUserAsStringList(uhidItemses)));
                listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_sort_user_name));
                listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                listPopupWindow4.setModal(true);
                listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                listPopupWindow4.show();
            }
        });

        txt_sort_user_name.setSelected(true);

        return rootView;
    }

    AdapterView.OnItemClickListener popUpItemClickDoctor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(prescriptionListViews).get(position));
            if (prescriptionListViews != null) {
                txt_disease_names.setText("Disease Name");
                txt_upload_by.setText("Uploaded By");
                txt_dates.setText("Date");
                txt_sort_doctor_name.setText("" + getDoctorNameAsStringList(prescriptionListViews).get(position));
                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                        getFilterListDoctor(getDoctorNameAsStringList(prescriptionListViews).get(position)));
                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickdisease = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow1.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(prescriptionListViews).get(position));
            if (prescriptionListViews != null) {
                txt_sort_doctor_name.setText("Doctor Name");
                txt_upload_by.setText("Uploaded By");
                txt_dates.setText("Date");
                txt_disease_names.setText("" + getDiseaseListAsStringList(prescriptionListViews).get(position));
                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                        getFilterListDisease(getDiseaseListAsStringList(prescriptionListViews).get(position)));
                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
    };


    AdapterView.OnItemClickListener popUpItemClickupload_by = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow2.dismiss();
            if (position == 0) {
                txt_sort_doctor_name.setText("Doctor Name");
                txt_disease_names.setText("Disease Name");
                txt_dates.setText("Date");
                txt_upload_by.setText("" + "Self");
                if (prescriptionListViews != null && prescriptionListViews.size() > 0) {
                    uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                            getFilterListUploadBy("Self"));
                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                    uploadPrescriptionAdpter.notifyDataSetChanged();
                }

            } else {
                txt_sort_doctor_name.setText("Doctor Name");
                txt_disease_names.setText("Disease Name");
                txt_dates.setText("Date");
                txt_upload_by.setText("" + "CureFull");
                if (prescriptionListViews != null && prescriptionListViews.size() > 0) {
                    uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                            getFilterListUploadBy("CureFull"));
                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                    uploadPrescriptionAdpter.notifyDataSetChanged();
                }

            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickDate = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow3.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(prescriptionListViews).get(position));
            if (prescriptionListViews != null) {
                txt_sort_doctor_name.setText("Doctor Name");
                txt_disease_names.setText("Disease Name");
                txt_upload_by.setText("Uploaded By");
                txt_dates.setText("" + getDateAsStringList(prescriptionListViews).get(position));
                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                        getFilterListDate(getDateAsStringList(prescriptionListViews).get(position)));
                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
    };


    AdapterView.OnItemClickListener popUpItemClickUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listPopupWindow4.dismiss();
            if (uhidItemses != null) {
                getSelectedUserList(getUserAsStringListUFHID(uhidItemses).get(position));
                txt_sort_user_name.setText("" + getUserAsStringList(uhidItemses).get(position));
                AppPreference.getInstance().setcf_uuhidNeew(getUserAsStringListUFHID(uhidItemses).get(position));
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
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_upload_new:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload);
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_camera:
                value = 0;
                imageName = 0;
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
                break;
            case R.id.liner_gallery:
                value = 0;
                prescriptionImageLists = new ArrayList<PrescriptionImageList>();
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
            DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), bitmap, selectUploadPrescription, prescriptionImageLists);
            dialogUploadNewPrescription.setiOnAddMoreImage(this);
            dialogUploadNewPrescription.show();
//            img_vew.setImageBitmap(bitmap);
        } else {
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
                Log.e("parent", " " + Environment.getExternalStorageDirectory());
                prescriptionImageList.setPrescriptionImage(imagePath);
                prescriptionImageList.setChecked(false);
                prescriptionImageLists.add(prescriptionImageList);
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), bitmap, selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
//                img_vew.setImageBitmap(bitmap);
                // Do something with the bitmap
                // At the end remember to close the cursor or you will end with the RuntimeException!
                cursor.close();
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
    public void optDoneMoreImage(final String doctorName, final String dieaseName, final String prescriptionDate, final List<PrescriptionImageList> prescriptionImageListss) {
        CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
        liner_upload_new.post(new Runnable() {
            @Override
            public void run() {
                sentSaveTestingServer(doctorName, dieaseName, prescriptionDate, prescriptionImageListss);
            }
        });

    }


    private void sentSaveTestingServer(String doctorName, String dieaseName, String prescriptionDate, List<PrescriptionImageList> prescriptionImage) {
        String response = "";
        RequestBuilderOkHttp builderOkHttp = new RequestBuilderOkHttp();
        String removeSyptoms = "";
        try {
            for (int i = 0; i < prescriptionImage.size(); i++) {
                if (prescriptionImage.get(i).getImageNumber() != 000) {

                    if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                        String imageName = prescriptionImage.get(i).getPrescriptionImage().replace("/storage/emulated/0/", "");
                        removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + imageName + ",";
                        Log.e("check", "" + removeSyptoms);
                    } else {
                        String imageName = prescriptionImage.get(i).getPrescriptionImage().replace("/storage/emulated/0/DCIM/Camera/", "");
                        removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + imageName + ",";
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
        getPrescriptionList();
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


    private void getPrescriptionList() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_PRESCRIPTION_LIST,
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
                            prescriptionListViews = ParseJsonData.getInstance().getPrescriptionList(response);
                            if (prescriptionListViews.size() > 0 && prescriptionListViews != null) {
                                txt_no_prescr.setVisibility(View.GONE);
                                uploadPrescriptionAdpter = new UploadPrescriptionAdpter(CureFull.getInstanse().getActivityIsntanse(),
                                        prescriptionListViews);
                                prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                uploadPrescriptionAdpter.notifyDataSetChanged();
                            } else {
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setVisibility(View.VISIBLE);
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

    private List<String> getUserAsStringListUFHID(List<UHIDItems> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (UHIDItems logy : result)
                list.add(logy.getCfUuhid());
        return list;
    }

    private List<String> getDiseaseListAsStringList(List<PrescriptionListView> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (PrescriptionListView logy : result)
                list.add(logy.getDiseaseName());
        return list;
    }

    private List<String> getDoctorNameAsStringList(List<PrescriptionListView> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (PrescriptionListView logy : result)
                list.add(logy.getDoctorName());
        return list;
    }

    private List<String> getDateAsStringList(List<PrescriptionListView> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (PrescriptionListView logy : result)
                list.add(logy.getPrescriptionDate());
        return list;
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

}