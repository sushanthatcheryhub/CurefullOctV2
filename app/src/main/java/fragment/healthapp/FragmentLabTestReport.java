package fragment.healthapp;


import android.animation.Animator;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
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

import adpter.UploadLabTestReportAdpter;
import adpter.UploadPrescriptionAdpter;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import item.property.LabReportListView;
import item.property.PrescriptionDiseaseName;
import item.property.PrescriptionDoctorName;
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
public class FragmentLabTestReport extends BaseBackHandlerFragment implements View.OnClickListener, IOnAddMoreImage, IOnDoneMoreImage, PopupWindow.OnDismissListener {


    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int SELECT_PHOTO = 12345;
    private RelativeLayout realtive_notes;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera;
    private float pixelDensity;
    boolean flag = true;
    private Animation alphaAnimation;
    private ImageView img_upload, img_gallery, img_camera, img_upload_animation, btn_reset;
    private RecyclerView labReportItemView;
    private GridLayoutManager lLayout;
    private UploadLabTestReportAdpter uploadLabTestReportAdpter;
    private List<LabReportListView> labReportListViews;
    // private List<LabReportImageList> labReportImageLists;
    private List<PrescriptionImageList> prescriptionImageLists;
    private String selectUploadPrescription = "";
    private int value = 0;
    private RequestQueue requestQueue;
    private ListPopupWindow listPopupWindow;
    private ListPopupWindow listPopupWindow1;
    private ListPopupWindow listPopupWindow2;
    private ListPopupWindow listPopupWindow3;
    private ListPopupWindow listPopupWindow4;
    private List<PrescriptionDoctorName> prescriptionDoctorNames;
    private List<PrescriptionDiseaseName> prescriptionDiseaseNames;
    private TextView txt_no_prescr, txt_Doctor_name, txt_tst_name, txt_dates;
    private LinearLayout txt_heath_note, txt_heath_app, txt_prescription;
    private List<UHIDItems> uhidItemses;
    private TextView txt_sort_user_name;
    private int imageName = 0;
    private String fileName = "";
    private LinearLayout liner_layout_recyler;
    private String newMessage = "";
    private String clickDoctorName = "", clickDiseaseName = "", clickDates = "";
    private ImageView img_doctor_name, img_test_name, img_date, img_user_name;
    private String checkDialog = "";
    private boolean isUploadClick = false;
    private String doctorName, dieaseName, prescriptionDate;

    @Override
    public boolean onBackPressed() {

        if (isUploadClick) {
            isUploadClick = false;
            CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload_animation);
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
        rootView = inflater.inflate(R.layout.fragment_health_lab_report,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

        img_doctor_name = (ImageView) rootView.findViewById(R.id.img_doctor_name);
        img_test_name = (ImageView) rootView.findViewById(R.id.img_test_name);
        img_date = (ImageView) rootView.findViewById(R.id.img_date);
        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);

        btn_reset = (ImageView) rootView.findViewById(R.id.btn_reset);
        liner_layout_recyler = (LinearLayout) rootView.findViewById(R.id.liner_layout_recyler);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_heath_app);
        txt_prescription = (LinearLayout) rootView.findViewById(R.id.txt_prescription);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
        txt_Doctor_name = (TextView) rootView.findViewById(R.id.txt_Doctor_name);
        txt_tst_name = (TextView) rootView.findViewById(R.id.txt_tst_name);
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
        liner_animation_upload.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.alpha_anim);
        labReportItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        labReportItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        labReportItemView.setLayoutManager(lLayout);
        labReportItemView.setHasFixedSize(true);
        txt_heath_note.setOnClickListener(this);
        txt_heath_app.setOnClickListener(this);
        txt_prescription.setOnClickListener(this);
        btn_reset.setOnClickListener(this);

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        getAllHealthUserList();
        getLabReportList();

        (rootView.findViewById(R.id.img_doctor_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labReportListViews != null && labReportListViews.size() > 0) {
                    checkDialog = "img_doctor_name";
                    rotatePhoneClockwise(img_doctor_name);
                    listPopupWindow = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getDoctorNameAsStringList(labReportListViews)));
                    listPopupWindow.setAnchorView(rootView.findViewById(R.id.txt_Doctor_name));
                    listPopupWindow.setWidth((int) getResources().getDimension(R.dimen._100dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow.setModal(true);
                    listPopupWindow.setOnDismissListener(FragmentLabTestReport.this);
                    listPopupWindow.setOnItemClickListener(popUpItemClickDoctor);
                    listPopupWindow.show();
                }

            }
        });


        (rootView.findViewById(R.id.img_test_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labReportListViews != null && labReportListViews.size() > 0) {
                    checkDialog = "img_test_name";
                    rotatePhoneClockwise(img_test_name);
                    listPopupWindow1 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow1.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getDiseaseListAsStringList(labReportListViews)));
                    listPopupWindow1.setAnchorView(rootView.findViewById(R.id.txt_tst_name));
                    listPopupWindow1.setWidth((int) getResources().getDimension(R.dimen._100dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow1.setModal(true);
                    listPopupWindow1.setOnDismissListener(FragmentLabTestReport.this);
                    listPopupWindow1.setOnItemClickListener(popUpItemClickdisease);
                    listPopupWindow1.show();

                }

            }
        });


        (rootView.findViewById(R.id.img_date)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (labReportListViews != null && labReportListViews.size() > 0) {
                    checkDialog = "img_date";
                    rotatePhoneClockwise(img_date);
                    listPopupWindow3 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow3.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getDateAsStringList(labReportListViews)));
                    listPopupWindow3.setAnchorView(rootView.findViewById(R.id.txt_dates));
                    listPopupWindow3.setWidth((int) getResources().getDimension(R.dimen._100dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow3.setModal(true);
                    listPopupWindow3.setOnDismissListener(FragmentLabTestReport.this);
                    listPopupWindow3.setOnItemClickListener(popUpItemClickDate);
                    listPopupWindow3.show();
                }

            }
        });


        (rootView.findViewById(R.id.img_user_name)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (uhidItemses != null && uhidItemses.size() > 0) {
                    checkDialog = "img_user_name";
                    rotatePhoneClockwise(img_user_name);
                    listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());
                    listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                            R.layout.adapter_list_doctor_data, getUserAsStringList(uhidItemses)));
                    listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_sort_user_name));
                    listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                    listPopupWindow4.setModal(true);
                    listPopupWindow4.setOnDismissListener(FragmentLabTestReport.this);
                    listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                    listPopupWindow4.show();
                }

            }
        });

        txt_sort_user_name.setSelected(true);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }

    AdapterView.OnItemClickListener popUpItemClickDoctor = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_doctor_name);
            listPopupWindow.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(labReportListViews).get(position));
            if (labReportListViews != null && labReportListViews.size() > 0) {
                txt_Doctor_name.setText("" + getDoctorNameAsStringList(labReportListViews).get(position));


                if (getFilterListDoctor(getDoctorNameAsStringList(labReportListViews).get(position)).size() > 0) {
                    txt_no_prescr.setVisibility(View.GONE);
                    labReportItemView.setVisibility(View.VISIBLE);
                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                            getFilterListDoctor(getDoctorNameAsStringList(labReportListViews).get(position)));
                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                    uploadLabTestReportAdpter.notifyDataSetChanged();
                } else {
                    txt_no_prescr.setText("No Prescription Found For " + getFilterListDoctor(getDoctorNameAsStringList(labReportListViews).get(position)));
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    labReportItemView.setVisibility(View.GONE);
                }


            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickdisease = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_test_name);
            listPopupWindow1.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(labReportListViews).get(position));
            if (labReportListViews != null && labReportListViews.size() > 0) {
                txt_tst_name.setText("" + getDiseaseListAsStringList(labReportListViews).get(position));
                if (getFilterListDisease(getDiseaseListAsStringList(labReportListViews).get(position)).size() > 0) {
                    txt_no_prescr.setVisibility(View.GONE);
                    labReportItemView.setVisibility(View.VISIBLE);
                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                            getFilterListDisease(getDiseaseListAsStringList(labReportListViews).get(position)));
                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                    uploadLabTestReportAdpter.notifyDataSetChanged();
                } else {
                    txt_no_prescr.setText("No Prescription Found For " + getFilterListDisease(getDiseaseListAsStringList(labReportListViews).get(position)));
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    labReportItemView.setVisibility(View.GONE);
                }

            }
        }
    };


    AdapterView.OnItemClickListener popUpItemClickDate = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_date);
            listPopupWindow3.dismiss();
            Log.e("Doctor Name ", ":- " + getDoctorNameAsStringList(labReportListViews).get(position));
            if (labReportListViews != null && labReportListViews.size() > 0) {
                txt_dates.setText("" + getDateAsStringList(labReportListViews).get(position));
                if (getFilterListDate(getDateAsStringList(labReportListViews).get(position)).size() > 0) {
                    txt_no_prescr.setVisibility(View.GONE);
                    labReportItemView.setVisibility(View.VISIBLE);
                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                            getFilterListDate(getDateAsStringList(labReportListViews).get(position)));
                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                    uploadLabTestReportAdpter.notifyDataSetChanged();
                } else {
                    txt_no_prescr.setText("No Prescription Found For " + getFilterListDate(getDateAsStringList(labReportListViews).get(position)));
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    labReportItemView.setVisibility(View.GONE);
                }

            }
        }
    };

    AdapterView.OnItemClickListener popUpItemClickUserList = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            rotatePhoneAntiClockwise(img_user_name);
            listPopupWindow4.dismiss();
            if (uhidItemses != null && uhidItemses.size() > 0) {
                getSelectedUserList(getUserAsStringListUFHID(uhidItemses).get(position));
                txt_sort_user_name.setText("" + getUserAsStringList(uhidItemses).get(position));
                AppPreference.getInstance().setcf_uuhidNeew(getUserAsStringListUFHID(uhidItemses).get(position));
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                txt_Doctor_name.setText("Doctor Name");
                txt_tst_name.setText("Test Name");
                txt_dates.setText("Date");
                getLabReportList();
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
                txt_Doctor_name.setText("Doctor Name");
                txt_tst_name.setText("Test Name");
                txt_dates.setText("Date");
                getLabReportList();
                break;
            case R.id.txt_heath_note:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthNote(), true);
                break;
            case R.id.txt_heath_app:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentHealthAppNew(), true);
                break;
            case R.id.txt_prescription:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionCheck(), true);
                break;

            case R.id.liner_animation_upload:
                isUploadClick = false;
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_upload_animation);
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
                prescriptionImageLists = new ArrayList<>();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
            //Get our saved file into a bitmap object:
            fileName = Environment.getExternalStorageDirectory() + File.separator;
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
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                dialogFullViewClickImage.setiOnDoneMoreImage(this);
                dialogFullViewClickImage.show();
            } else {
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), bitmap, selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
            }
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
                    PrescriptionImageList labReportImageList = new PrescriptionImageList();
                    labReportImageList.setImageNumber(value + 1);
                    value = value + 1;
                    labReportImageList.setPrescriptionImage(imagePath);
                    labReportImageList.setChecked(false);
                    prescriptionImageLists.add(labReportImageList);
                    if (newMessage.equalsIgnoreCase("yes")) {
                        PrescriptionImageList prescriptionImageList1 = new PrescriptionImageList();
                        prescriptionImageList1.setImageNumber(000);
                        prescriptionImageList1.setPrescriptionImage(null);
                        prescriptionImageList1.setChecked(false);
                        prescriptionImageLists.add(prescriptionImageList1);
                        DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
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
            }else{
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
            }else{
                revealView.setVisibility(View.GONE);
                layoutButtons.setVisibility(View.GONE);
            }

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
            DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
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


//    private void sentSaveTestingServer(String doctorName, String dieaseName, String prescriptionDate, List<PrescriptionImageList> prescriptionImage) {
//        String response = "";
//        RequestBuilderOkHttp builderOkHttp = new RequestBuilderOkHttp();
//        String removeSyptoms = "";
//        try {
//            for (int i = 0; i < prescriptionImage.size(); i++) {
//                if (prescriptionImage.get(i).getImageNumber() != 000) {
//                    if (selectUploadPrescription.equalsIgnoreCase("camera")) {
//                        String imageName = prescriptionImage.get(i).getPrescriptionImage().replace(fileName, "");
//                        removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + imageName + ",";
//                        Log.e("check", "" + removeSyptoms);
//                    } else {
//                        int file = prescriptionImage.get(i).getPrescriptionImage().lastIndexOf("/");
//                        String hello = prescriptionImage.get(i).getPrescriptionImage().substring(file + 1);
//                        Log.e("fileName", " " + hello);
//                        removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + hello + ",";
//                        Log.e("check", "" + removeSyptoms);
//                    }
//
//                }
//            }
//            if (removeSyptoms.endsWith(",")) {
//                removeSyptoms = removeSyptoms.substring(0, removeSyptoms.length() - 1);
//            }
////            Log.e("request", " " + MyConstants.WebUrls.FILE_UPLOAD + "First:- "  + "Second:- " + getFileParam(myPath.getPath()));
//            response = builderOkHttp.postR(MyConstants.WebUrls.UPLOAD_LAB_TEST_REPORT, null, getFileParam(prescriptionImage), doctorName, dieaseName, prescriptionDate, removeSyptoms);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        Log.e("response :- ", "" + response);
//
//    }


    public static HashMap<String, List<File>> getFileParam(List<PrescriptionImageList> image) {
        HashMap<String, List<File>> param = new HashMap<>();
        List<File> files = new ArrayList<>();
        for (int i = 0; i < image.size(); i++) {
            if (image.get(i).getImageNumber() != 000) {
                Log.e("run", " " + i);
                files.add(new File(image.get(i).getPrescriptionImage()));
            }
            param.put("reportFile", files);
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
                response = builderOkHttp.postR(MyConstants.WebUrls.UPLOAD_LAB_TEST_REPORT, null, getFileParam(prescriptionImage), doctorName, dieaseName, prescriptionDate, removeSyptoms);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Log.e("response :- ", "" + response);

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            getLabReportList();
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


    public void jsonUploadPrescription(String doctorName, String dieaseName, String prescriptionDate, List<PrescriptionImageList> prescriptionImageListss) {
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        JSONObject data = JsonUtilsObject.LabReportUpload(prescriptionDate, doctorName, dieaseName, prescriptionImageListss);
        Log.e("jsonUploadPrescription", ":- " + "size :- " + prescriptionImageListss.size());
        Log.e("jsonUploadPrescription", ":- " + data.toString() + " size :- " + prescriptionImageListss.size());

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UPLOAD_LAB_TEST_REPORT, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("upload, URL 3.", response.toString());
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            getLabReportList();
//                            UserInfo userInfo = ParseJsonData.getInstance().getLoginData(response.toString());
//                            if (ParseJsonData.getInstance().getHttp_code().equalsIgnoreCase(MyConstants.JsonUtils.OK)) {
//                            }
                        } else {
                            try {
                                JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
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


    private void getLabReportList() {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LAB_TEST_REPORT_list,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

                        Log.e("Lab Report List, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            labReportListViews = ParseJsonData.getInstance().getLabTestReportList(response);
                            Log.e("labReportListViews", ":- " + labReportListViews.size());
                            if (labReportListViews.size() > 0) {
                                txt_no_prescr.setVisibility(View.GONE);
                                labReportItemView.setVisibility(View.VISIBLE);
                                uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                                        labReportListViews);
                                labReportItemView.setAdapter(uploadLabTestReportAdpter);
                                uploadLabTestReportAdpter.notifyDataSetChanged();
                            } else {

                                labReportItemView.setVisibility(View.GONE);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                            }

                        } else {
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


    private List<String> getDiseaseListAsStringList(List<LabReportListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (LabReportListView logy : result) {
                hsDoctorList.add(logy.getTestName());
            }
        }
        return new ArrayList<String>(hsDoctorList);
    }

    private List<String> getDoctorNameAsStringList(List<LabReportListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (LabReportListView logy : result) {
                hsDoctorList.add(logy.getDoctorName());
            }
        }
        return new ArrayList<String>(hsDoctorList);
    }

    private List<String> getDateAsStringList(List<LabReportListView> result) {
        Set<String> hsDoctorList = new HashSet<>();
        if (result != null) {
            for (LabReportListView logy : result) {
                hsDoctorList.add(logy.getReportDate());
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

    private List<String> getUserAsStringListUFHID(List<UHIDItems> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (UHIDItems logy : result)
                list.add(logy.getCfUuhid());
        return list;
    }

    private List<String> getUploadByAsStringList(List<LabReportListView> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (LabReportListView logy : result)
                list.add(logy.getUploadedBy());
        return list;
    }


    public ArrayList<LabReportListView> getFilterListDoctor(String charSequence) {
        ArrayList<LabReportListView> searched = new ArrayList<LabReportListView>();
        for (LabReportListView str : labReportListViews) {
            if (!clickDiseaseName.equalsIgnoreCase("")) {
                if (str.getDoctorName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDates.equalsIgnoreCase("")) {
                        if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                            if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else if (!clickDates.equalsIgnoreCase("")) {
                if (str.getDoctorName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDiseaseName.equalsIgnoreCase("")) {
                        if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                            if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else {
                if (str.getDoctorName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    searched.add(str);
                }
            }

        }
        return searched;
    }

    public ArrayList<LabReportListView> getFilterListDisease(String charSequence) {
        ArrayList<LabReportListView> searched = new ArrayList<LabReportListView>();
        for (LabReportListView str : labReportListViews) {
            if (!clickDoctorName.equalsIgnoreCase("")) {
                if (str.getTestName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDates.equalsIgnoreCase("")) {
                        if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                            if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else if (!clickDates.equalsIgnoreCase("")) {
                if (str.getTestName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDoctorName.equalsIgnoreCase("")) {
                        if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                            if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getReportDate().toLowerCase().contains(clickDates.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else {
                if (str.getTestName().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    searched.add(str);
                }
            }
        }
        return searched;
    }

    public ArrayList<LabReportListView> getFilterListUploadBy(String charSequence) {
        ArrayList<LabReportListView> searched = new ArrayList<LabReportListView>();
        for (LabReportListView str : labReportListViews) {
            if (str.getUploadedBy().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                searched.add(str);
            }
        }
        return searched;
    }

    public ArrayList<LabReportListView> getFilterListDate(String charSequence) {
        ArrayList<LabReportListView> searched = new ArrayList<LabReportListView>();
        for (LabReportListView str : labReportListViews) {
            if (!clickDoctorName.equalsIgnoreCase("")) {
                if (str.getReportDate().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDiseaseName.equalsIgnoreCase("")) {
                        if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                            if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else if (!clickDiseaseName.equalsIgnoreCase("")) {
                if (str.getReportDate().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    if (!clickDoctorName.equalsIgnoreCase("")) {
                        if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                            if (str.getDoctorName().toLowerCase().contains(clickDoctorName.toString().toLowerCase())) {
                                searched.add(str);
                            }
                        }
                    } else {
                        if (str.getTestName().toLowerCase().contains(clickDiseaseName.toString().toLowerCase())) {
                            searched.add(str);
                        }
                    }

                }
            } else {
                if (str.getReportDate().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                    searched.add(str);
                }
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
                        Log.e("getCfUuhidList, URL 1.", response);
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
                            txt_sort_user_name.setText("User Name");
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
        getLabReportList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    value = 0;
                    imageName = 0;
                    prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                    isUploadClick = true;
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
                    isUploadClick = true;
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
        } else if (checkDialog.equalsIgnoreCase("img_test_name")) {
            rotatePhoneAntiClockwise(img_test_name);
        } else if (checkDialog.equalsIgnoreCase("img_date")) {
            rotatePhoneAntiClockwise(img_date);
        } else if (checkDialog.equalsIgnoreCase("img_user_name")) {
            rotatePhoneAntiClockwise(img_user_name);
        }
    }
}