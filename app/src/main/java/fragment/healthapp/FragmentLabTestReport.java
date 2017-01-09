package fragment.healthapp;


import android.animation.Animator;
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
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.Filter_Reports_ListAdpter;
import adpter.UploadLabTestReportAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import item.property.FilterDataReports;
import item.property.LabReportListView;
import item.property.PrescriptionDiseaseName;
import item.property.PrescriptionDoctorName;
import item.property.PrescriptionImageList;
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
    private RelativeLayout realtive_notesShort;
    private RelativeLayout realtive_notesFilter;
    private LinearLayout revealView, layoutButtons, liner_upload_new, liner_animation_upload;
    private LinearLayout revealViewShort, layoutButtonsShort, liner_short_by;
    private LinearLayout revealViewFilter, layoutButtonsFilter, txt_filter_by;
    private LinearLayout liner_gallery, liner_camera, liner_btn_done, liner_filter_btn_reset;
    private float pixelDensity;
    boolean flag = true;
    boolean flagShort = true;
    boolean flagFilter = true;
    private Animation alphaAnimation;
    private ImageView img_upload, img_gallery, img_camera, img_upload_animation;
    private RecyclerView labReportItemView;
    private GridLayoutManager lLayout;
    private UploadLabTestReportAdpter uploadLabTestReportAdpter;
    private List<LabReportListView> labReportListViews = new ArrayList<>();
    private List<LabReportListView> labReportListViewsDummy;
    // private List<LabReportImageList> labReportImageLists;
    private List<PrescriptionImageList> prescriptionImageLists;
    private String selectUploadPrescription = "";
    private int value = 0;
    private RequestQueue requestQueue;
    private ListPopupWindow listPopupWindow4;
    private List<PrescriptionDoctorName> prescriptionDoctorNames;
    private List<PrescriptionDiseaseName> prescriptionDiseaseNames;
    private TextView txt_no_prescr;
    private LinearLayout txt_heath_note, txt_heath_app, txt_prescription;
    private List<UHIDItems> uhidItemses;
    private TextView txt_sort_user_name, txt_total_prescription;
    ;
    private int imageName = 0;
    private String fileName = "";
    private LinearLayout liner_layout_recyler;
    private String newMessage = "";
    private String clickDoctorName = "", clickDiseaseName = "", clickDates = "";
    private ImageView img_doctor_name, img_test_name, img_date, img_user_name;
    private String checkDialog = "";
    private boolean isButtonRest = false, isUploadClick = false, isloadMore = false, isOpenShortBy = false, isOpenUploadNew = false, isOpenFilter = false;
    private String doctorName, dieaseName, prescriptionDate;
    private int offset = 0;
    private FilterDataReports filterDataReports;
    private LinearLayout liner_filter_date, liner_filter_doctor, liner_filter_disease;
    private TextView txt_date, txt_doctor, txt_diease, txt_pre_total;

    private RecyclerView recyclerView_filter;
    private Filter_Reports_ListAdpter filter_prescription_listAdpter;

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
        rootView = inflater.inflate(R.layout.fragment_health_lab_report,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

        liner_filter_btn_reset = (LinearLayout) rootView.findViewById(R.id.liner_filter_btn_reset);
        liner_btn_done = (LinearLayout) rootView.findViewById(R.id.liner_btn_done);
        txt_pre_total = (TextView) rootView.findViewById(R.id.txt_pre_total);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_doctor = (TextView) rootView.findViewById(R.id.txt_doctor);
        txt_diease = (TextView) rootView.findViewById(R.id.txt_diease);

        liner_filter_date = (LinearLayout) rootView.findViewById(R.id.txt_filter_date);
        liner_filter_doctor = (LinearLayout) rootView.findViewById(R.id.txt_filter_doctor);
        liner_filter_disease = (LinearLayout) rootView.findViewById(R.id.txt_filter_disease);


        recyclerView_filter = (RecyclerView) rootView.findViewById(R.id.recyclerView_filter);
        realtive_notesShort = (RelativeLayout) rootView.findViewById(R.id.realtive_notesShort);
        realtive_notesFilter = (RelativeLayout) rootView.findViewById(R.id.realtive_notesFilter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView_filter.setLayoutManager(mLayoutManager);
        txt_total_prescription = (TextView) rootView.findViewById(R.id.txt_total_prescription);


        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);

        liner_layout_recyler = (LinearLayout) rootView.findViewById(R.id.liner_layout_recyler);
        txt_heath_note = (LinearLayout) rootView.findViewById(R.id.txt_heath_note);
        txt_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_heath_app);
        txt_prescription = (LinearLayout) rootView.findViewById(R.id.txt_prescription);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
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

        revealViewShort = (LinearLayout) rootView.findViewById(R.id.linearViewShort);
        layoutButtonsShort = (LinearLayout) rootView.findViewById(R.id.layoutButtonsShort);
        liner_short_by = (LinearLayout) rootView.findViewById(R.id.liner_short_by);


        revealViewFilter = (LinearLayout) rootView.findViewById(R.id.linearViewFilter);
        layoutButtonsFilter = (LinearLayout) rootView.findViewById(R.id.layoutButtonsFilter);
        txt_filter_by = (LinearLayout) rootView.findViewById(R.id.txt_filter_by);

//        img_upload_pre = (ImageView) rootView.findViewById(R.id.img_upload_pre);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
//        img_upload_pre.setOnClickListener(this);
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

        liner_short_by.setOnClickListener(this);
        txt_filter_by.setOnClickListener(this);
        liner_filter_date.setOnClickListener(this);
        liner_filter_doctor.setOnClickListener(this);
        liner_filter_disease.setOnClickListener(this);
        liner_btn_done.setOnClickListener(this);
        liner_filter_btn_reset.setOnClickListener(this);
        revealViewFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                launchTwitterFilterBy(rootView);

            }
        });
        revealViewShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTwitterShort(rootView);
            }
        });

        revealView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchTwitter(rootView);
            }
        });

        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);

        AppPreference.getInstance().setFilterDateReports("");
        AppPreference.getInstance().setFilterDoctorReports("");
        AppPreference.getInstance().setFilterDieseReports("");
        getAllHealthUserList();
        getLabReportList();
        getAllFilterData();


        (rootView.findViewById(R.id.liner_user_name_click)).setOnClickListener(new View.OnClickListener() {
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
                getLabReportList();
                getAllFilterData();
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

            case R.id.liner_btn_done:
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitterFilterBy(rootView);
                    }
                });
                clickDoctorName = AppPreference.getInstance().getFilterDoctorReports();
                clickDiseaseName = AppPreference.getInstance().getFilterDieseReports();
                clickDates = AppPreference.getInstance().getFilterDateReports();
                getLabReportList();
                getAllFilterData();
                break;
            case R.id.txt_filter_date:
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.health_red_drawer));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_diease.setTextColor(getResources().getColor(R.color.health_yellow));
                if (filterDataReports.getDateList() != null && filterDataReports.getDateList().size() > 0) {
                    showAdpter(filterDataReports.getDateList(), "date");
                }
                break;
            case R.id.txt_filter_doctor:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                txt_date.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_red_drawer));
                txt_diease.setTextColor(getResources().getColor(R.color.health_yellow));
                if (filterDataReports.getDoctorNameList() != null && filterDataReports.getDoctorNameList().size() > 0) {
                    showAdpter(filterDataReports.getDoctorNameList(), "doctor");
                }
                break;
            case R.id.txt_filter_disease:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.health_yellow);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_diease.setTextColor(getResources().getColor(R.color.health_red_drawer));
                if (filterDataReports.getDiseaseNameList() != null && filterDataReports.getDiseaseNameList().size() > 0) {
                    showAdpter(filterDataReports.getDiseaseNameList(), "disease");
                }
                break;

            case R.id.liner_filter_btn_reset:
                isButtonRest = true;
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                AppPreference.getInstance().setFilterDateReports("");
                AppPreference.getInstance().setFilterDoctorReports("");
                AppPreference.getInstance().setFilterDieseReports("");
                getAllFilterData();
                getLabReportList();
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.health_red_drawer));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_yellow));
                txt_diease.setTextColor(getResources().getColor(R.color.health_yellow));
                if (filterDataReports.getDateList() != null && filterDataReports.getDateList().size() > 0) {
                    showAdpter(filterDataReports.getDateList(), "date");
                }
                break;
            case R.id.liner_short_by:
                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterShort(rootView);
                        }
                    });
                }
                break;

            case R.id.txt_filter_by:

                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterFilterBy(rootView);
                        }
                    });
                }

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


    public void launchTwitter(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */

        if (isOpenShortBy) {
            launchTwitterShort(view);
        }
        if (isOpenFilter) {
            launchTwitterFilterBy(view);
        }


        int x = realtive_notes.getRight();
        int y = realtive_notes.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notes.getWidth(), realtive_notes.getHeight());
        try {
            if (flag) {
                isOpenUploadNew = true;
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
                isOpenUploadNew = false;
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

    public void launchTwitterShort(View view) {
        if (isOpenUploadNew) {
            launchTwitter(view);
        }
        if (isOpenFilter) {
            launchTwitterFilterBy(view);
        }
        int x = realtive_notesShort.getLeft();
        int y = realtive_notesShort.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notesShort.getWidth(), realtive_notesShort.getHeight());
        try {
            if (flagShort) {
                isOpenShortBy = true;
                if (labReportListViews != null) {
                    txt_total_prescription.setText("Prescription (" + labReportListViews.size() + ")");

                }
//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                            revealViewShort.getLayoutParams();
                    parameters.height = realtive_notesShort.getHeight();
                    revealViewShort.setLayoutParams(parameters);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewShort, x, y, 0, hypotenuse);
                    anim.setDuration(700);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            layoutButtonsShort.setVisibility(View.VISIBLE);
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

                    revealViewShort.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    revealViewShort.setVisibility(View.VISIBLE);
                    layoutButtonsShort.setVisibility(View.VISIBLE);
                }


                flagShort = false;
            } else {
                isOpenShortBy = false;
//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewShort, x, y, hypotenuse, 0);
                    anim.setDuration(400);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealViewShort.setVisibility(View.GONE);
                            layoutButtonsShort.setVisibility(View.GONE);
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
                    revealViewShort.setVisibility(View.GONE);
                    layoutButtonsShort.setVisibility(View.GONE);
                }

                flagShort = true;
            }
        } catch (Exception e) {

        }


    }

    public void launchTwitterFilterBy(View view) {
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */
        if (isOpenUploadNew) {
            launchTwitter(view);
        }
        if (isOpenShortBy) {
            launchTwitterShort(view);
        }

        int x = realtive_notesFilter.getLeft();
        int y = realtive_notesFilter.getTop();
        x -= ((28 * pixelDensity) + (16 * pixelDensity));
        int hypotenuse = (int) Math.hypot(realtive_notesFilter.getWidth(), realtive_notesFilter.getHeight());
        try {
            if (flagFilter) {
                isOpenFilter = true;
                if (labReportListViews != null && labReportListViews.size() > 0) {
                    txt_pre_total.setText("Prescription (" + labReportListViews.size() + ")");
                }

                if (filterDataReports.getDateList() != null) {
                    liner_filter_date.setBackgroundResource(R.color.health_yellow);
                    liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                    liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                    txt_date.setTextColor(getResources().getColor(R.color.health_red_drawer));
                    txt_doctor.setTextColor(getResources().getColor(R.color.health_yellow));
                    txt_diease.setTextColor(getResources().getColor(R.color.health_yellow));
                    showAdpter(filterDataReports.getDateList(), "date");
                }

//            imageButton.setBackgroundResource(R.drawable.rounded_cancel_button);
//            imageButton.setImageResource(R.drawable.image_cancel);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    FrameLayout.LayoutParams parameters = (FrameLayout.LayoutParams)
                            revealViewFilter.getLayoutParams();
                    parameters.height = realtive_notesFilter.getHeight();
                    revealViewFilter.setLayoutParams(parameters);

                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewFilter, x, y, 0, hypotenuse);
                    anim.setDuration(700);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {
                            layoutButtonsFilter.setVisibility(View.VISIBLE);
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

                    revealViewFilter.setVisibility(View.VISIBLE);
                    anim.start();
                } else {
                    revealViewFilter.setVisibility(View.VISIBLE);
                    layoutButtonsFilter.setVisibility(View.VISIBLE);
                }


                flagFilter = false;
            } else {
                isOpenFilter = false;
//            imageButton.setBackgroundResource(R.drawable.rounded_button);
//            imageButton.setImageResource(R.drawable.twitter_logo);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    Animator anim = ViewAnimationUtils.createCircularReveal(revealViewFilter, x, y, hypotenuse, 0);
                    anim.setDuration(400);

                    anim.addListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animator) {

                        }

                        @Override
                        public void onAnimationEnd(Animator animator) {
                            revealViewFilter.setVisibility(View.GONE);
                            layoutButtonsFilter.setVisibility(View.GONE);
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
                    revealViewFilter.setVisibility(View.GONE);
                    layoutButtonsFilter.setVisibility(View.GONE);
                }

                flagFilter = true;
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


    private void getLabReportList() {
        isloadMore = false;
        offset = 0;
        labReportListViews = null;
        labReportListViews = new ArrayList<>();

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

        if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("")) {
            s.append("");
        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LAB_TEST_REPORT_list + "?limit=10&offset=" + offset + s,
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
                            labReportListViewsDummy = ParseJsonData.getInstance().getLabTestReportList(response);
                            if (labReportListViewsDummy != null && labReportListViewsDummy.size() > 0) {
                                if (labReportListViewsDummy.size() < 10) {
                                    isloadMore = true;
                                }
                                labReportListViews.addAll(labReportListViewsDummy);
                                txt_no_prescr.setVisibility(View.GONE);
                                labReportItemView.setVisibility(View.VISIBLE);
                                uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                                        labReportListViews);
                                labReportItemView.setAdapter(uploadLabTestReportAdpter);
                                uploadLabTestReportAdpter.notifyDataSetChanged();
                            } else {
                                if (labReportListViewsDummy == null) {
                                    isloadMore = true;
                                }
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

            if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("")) {
                s.append("");
            }
            offset = +offsets;
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LAB_TEST_REPORT_list + "?limit=10&offset=" + offset + s,
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
                                labReportListViewsDummy = ParseJsonData.getInstance().getLabTestReportList(response);
                                if (labReportListViewsDummy != null && labReportListViewsDummy.size() > 0) {
                                    if (labReportListViewsDummy.size() < 10) {
                                        isloadMore = true;
                                    }
                                    labReportListViews.addAll(labReportListViewsDummy);
                                    txt_no_prescr.setVisibility(View.GONE);
                                    labReportItemView.setVisibility(View.VISIBLE);
                                    uploadLabTestReportAdpter.notifyDataSetChanged();
                                } else {
                                    if (labReportListViewsDummy == null) {
                                        isloadMore = true;
                                    }
//                                    labReportItemView.setVisibility(View.GONE);
//                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                }

                            } else {
//                                txt_no_prescr.setVisibility(View.VISIBLE);
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
        if (checkDialog.equalsIgnoreCase("img_user_name")) {
            rotatePhoneAntiClockwise(img_user_name);
        }
    }

    private void getAllFilterData() {
        clickDoctorName = AppPreference.getInstance().getFilterDoctorReports();
        clickDiseaseName = AppPreference.getInstance().getFilterDieseReports();
        clickDates = AppPreference.getInstance().getFilterDateReports();
        StringBuilder s = new StringBuilder();
        s.append("doctorName=" + clickDoctorName);
        s.append("&diseaseName=" + clickDiseaseName);
        s.append("&date=" + clickDates);
        if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("")) {
            s.append("");
        }
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.REPORTS_FILTER_DATA + s,
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
                            filterDataReports = ParseJsonData.getInstance().getFilterDataReports(response);
                            if (isButtonRest) {
                                isButtonRest = false;
                                if (filterDataReports.getDateList() != null && filterDataReports.getDateList().size() > 0) {
                                    showAdpter(filterDataReports.getDateList(), "date");
                                }
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

    public void showAdpter(ArrayList<String> strings, String filterName) {
        if (strings != null && strings.size() > 0) {
            filter_prescription_listAdpter = null;
            filter_prescription_listAdpter = new Filter_Reports_ListAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(), strings, filterName);
            recyclerView_filter.setAdapter(filter_prescription_listAdpter);
            filter_prescription_listAdpter.notifyDataSetChanged();
        }
    }


    public void callFilterAgain() {
        getAllFilterData();
        if (filter_prescription_listAdpter != null)
            filter_prescription_listAdpter.notifyDataSetChanged();
    }
}