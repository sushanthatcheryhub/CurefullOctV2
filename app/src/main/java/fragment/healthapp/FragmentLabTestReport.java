package fragment.healthapp;


import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.ListPopupWindow;
import android.support.v7.widget.RecyclerView;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import adpter.Filter_Reports_ListAdpter;
import adpter.UploadLabTestReportAdpter;
import adpter.UploadPrescriptionAdpterNew;
import asyns.JsonUtilsObject;
import asyns.ParseJsonData;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogLoader;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import interfaces.IOnOtpDonePath;
import item.property.FilterDataPrescription;
import item.property.FilterDataReports;
import item.property.LabReportListView;
import item.property.PrescriptionDiseaseName;
import item.property.PrescriptionDoctorName;
import item.property.PrescriptionImageList;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.HandlePermission;
import utils.MyConstants;
import utils.RequestBuilderOkHttp;
import utils.SpacesItemDecoration;

import static android.app.Activity.RESULT_OK;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLabTestReport extends BaseBackHandlerFragment implements View.OnClickListener, IOnAddMoreImage, IOnDoneMoreImage, PopupWindow.OnDismissListener, IOnOtpDonePath {

    private List<UHIDItems> uhiditemslocal;
    private View rootView;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT = 1778;
    public static final int SELECT_PHOTO = 12345;
    private static final int CAPTURE_MEDIA = 368;
    private RelativeLayout realtive_notes;
    private RelativeLayout realtive_notesShort, liner_short_by, txt_filter_by, liner_upload_new;
    private RelativeLayout realtive_notesFilter;
    private LinearLayout revealView, layoutButtons, liner_animation_upload;
    private LinearLayout revealViewShort, layoutButtonsShort;
    private LinearLayout revealViewFilter, layoutButtonsFilter;
    private LinearLayout liner_gallery, liner_camera, liner_btn_done, liner_filter_btn_reset;
    private float pixelDensity;
    boolean flag = true;
    private int valueUpload = 0;
    boolean flagShort = true;
    boolean flagFilter = true;
    boolean sortby_apply_flag = false;
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
    private ListPopupWindow listPopupWindow4;
    private List<PrescriptionDoctorName> prescriptionDoctorNames;
    private List<PrescriptionDiseaseName> prescriptionDiseaseNames;
    private TextView txt_no_prescr;
    private ImageView img_btn_refresh;
    private List<UHIDItems> uhidItemses;
    private TextView txt_sort_user_name, txt_total_prescription;
    private String imageName = "" + System.currentTimeMillis();
    private String fileName = "";
    private LinearLayout liner_layout_recyler;
    private String newMessage = "";
    private String clickDoctorName = "", clickDiseaseName = "", clickUploadBy = "", clickDates = "", clickShortBy = "DESC";
    private ImageView img_doctor_name, img_test_name, img_date, img_user_name;
    private String checkDialog = "";
    private boolean isButtonRest = false, isUploadClick = false, isloadMore = false, isOpenShortBy = false, isOpenUploadNew = false, isOpenFilter = false;
    private String doctorName, dieaseName, prescriptionDate;
    private int offset = 0;
    private FilterDataReports filterDataReports;
    private LinearLayout liner_filter_date, liner_filter_doctor, liner_filter_disease, liner_filter_uploadby;
    private TextView txt_date, txt_doctor, txt_diease, txt_pre_total, txt_uploadby;

    private RecyclerView recyclerView_filter;
    private Filter_Reports_ListAdpter filter_prescription_listAdpter;
    private RadioGroup radioShort;
    private RadioButton radioNewtest, radioOldest;

    private LinearLayout txt_short_cancel, txt_short_apply;
    private boolean isList = false, isRest = true, isChecked = true;
    private DialogLoader dialogLoader;
    private ArrayList<Image> images = null;
    private int REQUEST_CODE_PICKER = 2002;
    static SharedPreferences preferences;
    private FilterDataReports responsefilter = null;
    boolean isApplyFilter = false;

    @Override
    public boolean onBackPressed() {

        if (isOpenUploadNew) {
            isOpenUploadNew = false;
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    launchTwitter(rootView);
                }
            });
            return false;
        } else if (isOpenShortBy) {
            isOpenShortBy = false;
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    launchTwitterShort(rootView);
                }
            });
            return false;
        } else if (isOpenFilter) {
            isOpenFilter = false;
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    launchTwitterFilterBy(rootView);
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
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Lab Reports");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        dialogLoader = new DialogLoader(CureFull.getInstanse().getActivityIsntanse());
        dialogLoader.setCancelable(false);
        dialogLoader.setCanceledOnTouchOutside(false);
        dialogLoader.hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(false);
        AppPreference.getInstance().setFragmentHealthpre(false);
        AppPreference.getInstance().setFragmentHealthReprts(true);
        AppPreference.getInstance().setIsEditGoalPage(false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);
        txt_short_cancel = (LinearLayout) rootView.findViewById(R.id.txt_short_cancel);
        txt_short_apply = (LinearLayout) rootView.findViewById(R.id.txt_short_apply);
        radioShort = (RadioGroup) rootView.findViewById(R.id.radioShort);
        radioNewtest = (RadioButton) rootView.findViewById(R.id.radioNewtest);
        radioOldest = (RadioButton) rootView.findViewById(R.id.radioOldest);
        img_btn_refresh = (ImageView) rootView.findViewById(R.id.img_btn_refresh);
        liner_filter_btn_reset = (LinearLayout) rootView.findViewById(R.id.liner_filter_btn_reset);
        liner_btn_done = (LinearLayout) rootView.findViewById(R.id.liner_btn_done);
        txt_pre_total = (TextView) rootView.findViewById(R.id.txt_pre_total);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        txt_doctor = (TextView) rootView.findViewById(R.id.txt_doctor);
        txt_diease = (TextView) rootView.findViewById(R.id.txt_diease);
        txt_uploadby = (TextView) rootView.findViewById(R.id.txt_uploadby);
        liner_filter_date = (LinearLayout) rootView.findViewById(R.id.txt_filter_date);
        liner_filter_doctor = (LinearLayout) rootView.findViewById(R.id.txt_filter_doctor);
        liner_filter_disease = (LinearLayout) rootView.findViewById(R.id.txt_filter_disease);
        liner_filter_uploadby = (LinearLayout) rootView.findViewById(R.id.txt_filter_uploadby);

        recyclerView_filter = (RecyclerView) rootView.findViewById(R.id.recyclerView_filter);
        realtive_notesShort = (RelativeLayout) rootView.findViewById(R.id.realtive_notesShort);
        realtive_notesFilter = (RelativeLayout) rootView.findViewById(R.id.realtive_notesFilter);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CureFull.getInstanse().getActivityIsntanse());
        recyclerView_filter.setLayoutManager(mLayoutManager);
        txt_total_prescription = (TextView) rootView.findViewById(R.id.txt_total_prescription);
        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);
        liner_layout_recyler = (LinearLayout) rootView.findViewById(R.id.liner_layout_recyler);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
        txt_no_prescr = (TextView) rootView.findViewById(R.id.txt_no_prescr);
        img_upload_animation = (ImageView) rootView.findViewById(R.id.img_upload_animation);
        img_gallery = (ImageView) rootView.findViewById(R.id.img_gallery);
        img_camera = (ImageView) rootView.findViewById(R.id.img_camera);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        pixelDensity = getResources().getDisplayMetrics().density;
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        liner_animation_upload = (LinearLayout) rootView.findViewById(R.id.liner_animation_upload);
        liner_upload_new = (RelativeLayout) rootView.findViewById(R.id.liner_upload_new);
        liner_gallery = (LinearLayout) rootView.findViewById(R.id.liner_gallery);
        liner_camera = (LinearLayout) rootView.findViewById(R.id.liner_camera);
        revealView = (LinearLayout) rootView.findViewById(R.id.linearView);
        layoutButtons = (LinearLayout) rootView.findViewById(R.id.layoutButtons);

        revealViewShort = (LinearLayout) rootView.findViewById(R.id.linearViewShort);
        layoutButtonsShort = (LinearLayout) rootView.findViewById(R.id.layoutButtonsShort);
        liner_short_by = (RelativeLayout) rootView.findViewById(R.id.liner_short_by);


        revealViewFilter = (LinearLayout) rootView.findViewById(R.id.linearViewFilter);
        layoutButtonsFilter = (LinearLayout) rootView.findViewById(R.id.layoutButtonsFilter);
        txt_filter_by = (RelativeLayout) rootView.findViewById(R.id.txt_filter_by);

//        img_upload_pre = (ImageView) rootView.findViewById(R.id.img_upload_pre);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
//        img_upload_pre.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        alphaAnimation = AnimationUtils.loadAnimation(CureFull.getInstanse().getActivityIsntanse(), R.anim.alpha_anim);
        labReportItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        labReportItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        labReportItemView.setLayoutManager(lLayout);
        labReportItemView.setHasFixedSize(true);
        txt_short_apply.setOnClickListener(this);
        txt_short_cancel.setOnClickListener(this);
        liner_short_by.setOnClickListener(this);
        txt_filter_by.setOnClickListener(this);
        liner_filter_date.setOnClickListener(this);
        liner_filter_doctor.setOnClickListener(this);
        liner_filter_disease.setOnClickListener(this);
        liner_filter_uploadby.setOnClickListener(this);
        liner_btn_done.setOnClickListener(this);
        liner_filter_btn_reset.setOnClickListener(this);
        CureFull.getInstanse().setiOnOtpDonePath(this);
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


        img_btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                getAllHealthUserList();
                getAllFilterData();
            }
        });

        layoutButtons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutButtonsShort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        layoutButtonsFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                } else {
                    if (uhiditemslocal != null && uhiditemslocal.size() > 0) {
                        checkDialog = "img_user_name";
                        rotatePhoneClockwise(img_user_name);
                        listPopupWindow4 = new ListPopupWindow(CureFull.getInstanse().getActivityIsntanse());

                        listPopupWindow4.setAdapter(new ArrayAdapter(CureFull.getInstanse().getActivityIsntanse(),
                                R.layout.adapter_list_doctor_data, getUserAsStringList(uhiditemslocal)));

                        listPopupWindow4.setAnchorView(rootView.findViewById(R.id.txt_sort_user_name));
                        listPopupWindow4.setWidth((int) getResources().getDimension(R.dimen._110dp));
//                listPopupWindow.setHeight(400);
                        listPopupWindow4.setModal(true);
                        listPopupWindow4.setOnDismissListener(FragmentLabTestReport.this);
                        listPopupWindow4.setOnItemClickListener(popUpItemClickUserList);
                        listPopupWindow4.show();
                    }
                }

            }
        });

        txt_sort_user_name.setSelected(true);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);

        radioShort.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isChecked) {
                    if (checkedId == R.id.radioNewtest) {
                        clickShortBy = "DESC";
                        radioNewtest.setChecked(true);
                        radioOldest.setChecked(false);
                    } else if (checkedId == R.id.radioOldest) {
                        clickShortBy = "ASC";
                        radioNewtest.setChecked(false);
                        radioOldest.setChecked(true);
                    }
                }

            }
        });
        txt_pre_total.setSelected(true);
        txt_total_prescription.setSelected(true);
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

                if (isOpenUploadNew) {
                    isOpenUploadNew = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                } else if (isOpenShortBy) {
                    isOpenShortBy = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterShort(rootView);
                        }
                    });
                } else if (isOpenFilter) {
                    isOpenFilter = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterFilterBy(rootView);
                        }
                    });
                }
                isButtonRest = true;
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                clickUploadBy = "";
                AppPreference.getInstance().setFilterDateReports("");
                AppPreference.getInstance().setFilterDoctorReports("");
                AppPreference.getInstance().setFilterDieseReports("");
                AppPreference.getInstance().setFilterUploadBy("");
                getAllFilterData();
                getLabReportList();
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.white));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
            }

            if (uhiditemslocal != null && uhiditemslocal.size() > 0) {
                getAllUserList(getUserAsStringListUFHID(uhiditemslocal).get(position));
                getSelectedUserList(getUserAsStringListUFHID(uhiditemslocal).get(position));
                txt_sort_user_name.setText("" + getUserAsStringList(uhiditemslocal).get(position));
                AppPreference.getInstance().setcf_uuhidNeew(getUserAsStringListUFHID(uhiditemslocal).get(position));
                if (isOpenUploadNew) {
                    isOpenUploadNew = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                } else if (isOpenShortBy) {
                    isOpenShortBy = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterShort(rootView);
                        }
                    });
                } else if (isOpenFilter) {
                    isOpenFilter = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitterFilterBy(rootView);
                        }
                    });
                }
                isButtonRest = true;
                clickDoctorName = "";
                clickDiseaseName = "";
                clickDates = "";
                clickUploadBy = "";
                AppPreference.getInstance().setFilterDate("");
                AppPreference.getInstance().setFilterDoctor("");
                AppPreference.getInstance().setFilterDiese("");
                AppPreference.getInstance().setFilterUploadBy("");
                getAllFilterData();
                getLabReportList();
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.white));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));

            }
        }
    };

    private void getAllUserList(String cfUuhid) {
        ParseJsonData.getInstance().getUHIDUpdate(cfUuhid);//update selected in local db
        DbOperations.getUHIDListLocal(CureFull.getInstanse().getActivityIsntanse());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txt_short_apply:
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitterShort(rootView);
                    }
                });
                sortby_apply_flag = true;
                getLabReportList();
                break;

            case R.id.txt_short_cancel:
                if (isRest) {
                    isChecked = false;
                    isRest = false;
                    isButtonRest = true;
                    clickDoctorName = "";
                    clickDiseaseName = "";
                    clickUploadBy = "";
                    clickDates = "";
                    radioNewtest.setChecked(false);
                    radioOldest.setChecked(false);
                    AppPreference.getInstance().setFilterDateReports("");
                    AppPreference.getInstance().setFilterDoctorReports("");
                    AppPreference.getInstance().setFilterDieseReports("");
                    getAllFilterData();
                    getLabReportList();
                    liner_filter_date.setBackgroundResource(R.color.health_yellow);
                    liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                    liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                    txt_date.setTextColor(getResources().getColor(R.color.white));
                    txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                }
                break;
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
                clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
                isApplyFilter = true;
                getLabReportList();
                getAllFilterData();
                break;
            case R.id.txt_filter_date:
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.white));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataReports.getDateList() != null && filterDataReports.getDateList().size() > 0) {
                        showAdpter(filterDataReports.getDateList(), "date");
                    }
                } else {
                    filterbyDate();
                }
                break;
            case R.id.txt_filter_doctor:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_doctor.setTextColor(getResources().getColor(R.color.white));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataReports.getDoctorNameList() != null && filterDataReports.getDoctorNameList().size() > 0) {
                        showAdpter(filterDataReports.getDoctorNameList(), "doctor");
                    }
                } else {
                    filterbyDoctor();
                }
                break;
            case R.id.txt_filter_disease:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.health_yellow);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.white));
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataReports.getDiseaseNameList() != null && filterDataReports.getDiseaseNameList().size() > 0) {
                        showAdpter(filterDataReports.getDiseaseNameList(), "disease");
                    }
                } else {
                    filterbyTestName();
                }
                break;

            case R.id.txt_filter_uploadby:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                liner_filter_uploadby.setBackgroundResource(R.color.health_yellow);
                txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_uploadby.setTextColor(getResources().getColor(R.color.white));
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataReports.getUploadedByList() != null && filterDataReports.getUploadedByList().size() > 0) {
                        showAdpter(filterDataReports.getUploadedByList(), "uploadBy");
                    }
                } else {
                    filterbyUplodedby();
                }
                break;

            case R.id.liner_filter_btn_reset:
                if (isRest) {

                    isChecked = false;
                    isRest = false;
                    isButtonRest = true;
                    clickDoctorName = "";
                    clickDiseaseName = "";
                    clickUploadBy = "";
                    clickDates = "";
                    radioNewtest.setChecked(false);
                    radioOldest.setChecked(false);
                    AppPreference.getInstance().setFilterDateReports("");
                    AppPreference.getInstance().setFilterDoctorReports("");
                    AppPreference.getInstance().setFilterDieseReports("");
                    getAllFilterData();
                    getLabReportList();
                    liner_filter_date.setBackgroundResource(R.color.health_yellow);
                    liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                    liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                    liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                    txt_date.setTextColor(getResources().getColor(R.color.white));
                    txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));

                }

                break;
            case R.id.liner_short_by:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (isList) {
                    /*if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {*/
                    if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                        liner_upload_new.post(new Runnable() {
                            @Override
                            public void run() {
                                launchTwitterShort(rootView);
                            }
                        });
                    }
                    /*} else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                    }*/
                }


                break;

            case R.id.txt_filter_by:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (isList) {
                    /*if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {*/
                    if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                        liner_upload_new.post(new Runnable() {
                            @Override
                            public void run() {
                                launchTwitterFilterBy(rootView);
                            }
                        });
                    }
                    /*} else {
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                    }*/
                }

                break;

            case R.id.liner_animation_upload:
                isUploadClick = false;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                break;
            case R.id.liner_upload_new:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);

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
                    newMessage = "";
                    value = 0;
                    imageName = "" + System.currentTimeMillis();
                    prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                    isUploadClick = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                    } else {
                    new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                            .setShowPicker(false)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(true)
                            .launchCamera();
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                    }

                }

                break;
            case R.id.liner_gallery:
                newMessage = "";
                prescriptionImageLists = new ArrayList<>();
                isUploadClick = false;
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitter(rootView);
                    }
                });
                selectUploadPrescription = "gallery";
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                }
                images = new ArrayList<>();
                start();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
////                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != 0) {

            if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
                images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
                for (int i = 0, l = images.size(); i < l; i++) {
                    PrescriptionImageList labReportImageList = new PrescriptionImageList();
                    labReportImageList.setImageNumber(value + 1);
                    value = value + 1;
                    labReportImageList.setPrescriptionImage(images.get(i).getPath());
                    labReportImageList.setChecked(false);
                    prescriptionImageLists.add(labReportImageList);
                }

                if (newMessage.equalsIgnoreCase("yes")) {
                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
                    dialogFullViewClickImage.show();
                } else {
                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
                    dialogUploadNewPrescription.setiOnAddMoreImage(this);
                    dialogUploadNewPrescription.show();
                }


            }
            if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
                //Get our saved file into a bitmap object:
                fileName = Environment.getExternalStorageDirectory() + File.separator;
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
                PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
                prescriptionImageList.setImageNumber(value + 1);
                value = value + 1;
                imageName = "" + System.currentTimeMillis();
                prescriptionImageList.setPrescriptionImage(file.getAbsolutePath());
                prescriptionImageList.setChecked(false);
                prescriptionImageLists.add(prescriptionImageList);
                if (newMessage.equalsIgnoreCase("yes")) {
                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
                    dialogFullViewClickImage.show();
                } else {
                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), file.getAbsolutePath(), selectUploadPrescription, prescriptionImageLists);
                    dialogUploadNewPrescription.setiOnAddMoreImage(this);
                    dialogUploadNewPrescription.show();
                }
            } else if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT) {
                //Get our saved file into a bitmap object:
                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
                PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
                prescriptionImageList.setImageNumber(value + 1);
                value = value + 1;
                imageName = "" + System.currentTimeMillis();
                prescriptionImageList.setPrescriptionImage(file.getAbsolutePath());


                prescriptionImageList.setChecked(false);
                prescriptionImageLists.add(prescriptionImageList);
                if (newMessage.equalsIgnoreCase("yes")) {
                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
                    dialogFullViewClickImage.show();
                } else {
                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), file.getAbsolutePath(), selectUploadPrescription, prescriptionImageLists);
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
                        Cursor cursor = CureFull.getInstanse().getActivityIsntanse().getContentResolver().query(pickedImage, filePath, null, null, null);
                        cursor.moveToFirst();
                        String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
                        PrescriptionImageList labReportImageList = new PrescriptionImageList();
                        labReportImageList.setImageNumber(value + 1);
                        value = value + 1;
                        labReportImageList.setPrescriptionImage(imagePath);
                        labReportImageList.setChecked(false);
                        prescriptionImageLists.add(labReportImageList);
                        if (newMessage.equalsIgnoreCase("yes")) {
                            DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                            dialogFullViewClickImage.setiOnDoneMoreImage(this);
                            dialogFullViewClickImage.show();
                        } else {
                            DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
                            dialogUploadNewPrescription.setiOnAddMoreImage(this);
                            dialogUploadNewPrescription.show();
                        }
//                img_vew.setImageBitmap(bitmap);
                        // Do something with the bitmap
                        // At the end remember to close the cursor or you will end with the RuntimeException!
                    }
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
                    txt_total_prescription.setText("Lab Reports (" + labReportListViews.size() + ")");

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
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
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
                        txt_pre_total.setText("Lab Reports (" + labReportListViews.size() + ")");
                    }

                    if (filterDataReports.getDateList() != null) {
                        liner_filter_date.setBackgroundResource(R.color.health_yellow);
                        liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                        liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                        txt_date.setTextColor(getResources().getColor(R.color.white));
                        txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
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
        } else {
            //local

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
                        txt_pre_total.setText("Lab Reports (" + labReportListViews.size() + ")");
                    }
                    clickDoctorName = "";
                    clickDiseaseName = "";
                    clickUploadBy = "";
                    clickDates = "";
                    AppPreference.getInstance().setFilterDateReports("");
                    AppPreference.getInstance().setFilterDoctorReports("");
                    AppPreference.getInstance().setFilterDieseReports("");

                    filterbyDate();

                    if (responsefilter.getDateList() != null) {
                        liner_filter_date.setBackgroundResource(R.color.health_yellow);
                        liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                        liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                        liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                        txt_date.setTextColor(getResources().getColor(R.color.white));
                        txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        //showAdpter(responsefilter.getDateList(), "date");
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
                e.getMessage();
            }


        }

    }


    @Override
    public void optAddMoreImage(String messsage) {
        if (messsage.equalsIgnoreCase("done")) {
            DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
            dialogFullViewClickImage.setiOnDoneMoreImage(this);
            dialogFullViewClickImage.show();
        } else if (messsage.equalsIgnoreCase("retry")) {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                prescriptionImageLists.remove(prescriptionImageLists.size() - 1);
                value = prescriptionImageLists.size() - 1;
                imageName = "" + System.currentTimeMillis();
//
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                    Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            file);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                } else {
                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                }
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

            } else {
                prescriptionImageLists.remove(prescriptionImageLists.size() - 1);
                value = prescriptionImageLists.size() - 1;
                images = new ArrayList<>();
                start();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
////                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        } else {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);

//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                    Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            file);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                } else {
                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                }

            } else {
                images = new ArrayList<>();
                start();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
////                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        }
    }

    @Override
    public void optDoneMoreImage(final String doctorNames, final String dieaseNames, final String prescriptionDates, final List<PrescriptionImageList> prescriptionImageListss, String mesaage) {

        if (mesaage.equalsIgnoreCase("yes")) {
            prescriptionImageListss.remove(prescriptionImageListss.size() - 1);
            newMessage = mesaage;
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
//                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                        BuildConfig.APPLICATION_ID + ".provider",
//                        file);
//                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                    File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                    Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                            BuildConfig.APPLICATION_ID + ".provider",
//                            file);
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                } else {
                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();
//                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                    startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                }
            } else {
                images = new ArrayList<>();
                start();
//                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
////                photoPickerIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
//                photoPickerIntent.setType("image/*");
//                startActivityForResult(photoPickerIntent, SELECT_PHOTO);
            }
        } else {
            CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
            liner_upload_new.post(new Runnable() {
                @Override
                public void run() {
                    isUploadClick = false;
                    doctorName = doctorNames;
                    dieaseName = dieaseNames;
                    prescriptionDate = prescriptionDates;
                    dialogLoader.show();
                    if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                        jsonSaveUploadPrescriptionMetadata(prescriptionDate, doctorName, dieaseName, prescriptionImageListss);
                    } else {
                       //work offline
                        LabReportListView imagelist=new LabReportListView();
                        imagelist.uploadFilelocal(prescriptionDate, doctorName, dieaseName, AppPreference.getInstance().getcf_uuhidNeew(),prescriptionImageListss,prescriptionImageListss.size(),"self");
                        getLabReportList();
                        dialogLoader.hide();
                    }
//                    new LongOperation().execute(prescriptionImageListss);
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
                        } else {
                            int file = prescriptionImage.get(i).getPrescriptionImage().lastIndexOf("/");
                            String hello = prescriptionImage.get(i).getPrescriptionImage().substring(file + 1);
                            removeSyptoms += prescriptionImage.get(i).getImageNumber() + "/" + hello + ",";
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
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            getLabReportList();
            getAllFilterData();
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
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            isloadMore = false;
            offset = 0;
            labReportListViews = null;
            labReportListViewsDummy = null;
            labReportListViews = new ArrayList<>();

            StringBuilder s = new StringBuilder();
            if (!clickDoctorName.equalsIgnoreCase("")) {
                s.append("&doctorName=" + clickDoctorName.replace(" ", "%20"));
            }
            if (!clickDiseaseName.equalsIgnoreCase("")) {
                s.append("&testName=" + clickDiseaseName.replace(" ", "%20"));
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
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LAB_TEST_REPORT_list + "?limit=10&offset=" + offset + "&sortBy=" + clickShortBy + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.e("res ", " " + response);
                            isRest = true;
                            dialogLoader.hide();
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);


                            int responseStatus = 0;
                            JSONObject json = null;
                            try {
                                json = new JSONObject(response.toString());
                                responseStatus = json.getInt("responseStatus");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                getFilenameDelete();
                                getFilenameDeleteDialog();
                                labReportListViewsDummy = ParseJsonData.getInstance().getLabTestReportList(response);
                                if (labReportListViewsDummy != null && labReportListViewsDummy.size() > 0) {
                                    isList = true;
                                    if (labReportListViewsDummy.size() < 10) {
                                        isloadMore = true;
                                    }
                                    labReportListViews.addAll(labReportListViewsDummy);
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);
                                    labReportItemView.setVisibility(View.VISIBLE);
                                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                                            labReportListViews, rootView);
                                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                                    uploadLabTestReportAdpter.notifyDataSetChanged();
                                } else {
                                    isList = false;
                                    if (labReportListViewsDummy == null) {
                                        isloadMore = true;
                                    }
                                    img_btn_refresh.setVisibility(View.GONE);
                                    txt_no_prescr.setText("No Reports Yet.");
                                    labReportItemView.setVisibility(View.GONE);
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                }

                            } else {
                                txt_no_prescr.setText("No Reports Yet.");
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                img_btn_refresh.setVisibility(View.GONE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialogLoader.hide();
                            if (sortby_apply_flag) {

                                List<LabReportListView> response = DbOperations.getLabTestReportListALLSort(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickShortBy);

                                if (response.size() > 0) {
                                    isList = true;
                                    labReportListViews = null;
                                    labReportListViews = new ArrayList<>();
                                    labReportListViews.addAll(response);
                                    labReportItemView.setVisibility(View.VISIBLE);
                                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                                            labReportListViews, rootView);
                                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadLabTestReportAdpter.notifyDataSetChanged();
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);
                                } else {
                                    isList = false;
                                    img_btn_refresh.setVisibility(View.GONE);
                                    labReportItemView.setVisibility(View.GONE);
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_REPORT);
                                }
                            } else {
                                List<LabReportListView> response = DbOperations.getLabTestReportListALL(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew());

                                if (response.size() > 0) {
                                    isList = true;
                                    labReportListViews = null;
                                    labReportListViews = new ArrayList<>();
                                    labReportListViews.addAll(response);
                                    labReportItemView.setVisibility(View.VISIBLE);
                                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                                            labReportListViews, rootView);
                                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadLabTestReportAdpter.notifyDataSetChanged();
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);
                                } else {
                                    isList = false;
                                    img_btn_refresh.setVisibility(View.GONE);
                                    labReportItemView.setVisibility(View.GONE);
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_REPORT);
                                }
                            }
                            sortby_apply_flag = false;
                            isRest = true;

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
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                    return headers;
                }
            };

            CureFull.getInstanse().getRequestQueue().add(postRequest);
        } else if (isApplyFilter) {
            isApplyFilter = false;
            allfilter();

        } else {
            //by sourav
            if (sortby_apply_flag) {

                List<LabReportListView> response = DbOperations.getLabTestReportListALLSort(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickShortBy);

                if (response.size() > 0) {
                    isList = true;
                    labReportListViews = null;
                    labReportListViews = new ArrayList<>();
                    labReportListViews.addAll(response);
                    labReportItemView.setVisibility(View.VISIBLE);
                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                            labReportListViews, rootView);
                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    uploadLabTestReportAdpter.notifyDataSetChanged();
                    txt_no_prescr.setVisibility(View.GONE);
                    img_btn_refresh.setVisibility(View.GONE);
                } else {
                    isList = false;
                    img_btn_refresh.setVisibility(View.GONE);
                    labReportItemView.setVisibility(View.GONE);
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_REPORT);
                }
            } else {
                List<LabReportListView> response = DbOperations.getLabTestReportListALL(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew());

                if (response.size() > 0) {
                    isList = true;
                    labReportListViews = null;
                    labReportListViews = new ArrayList<>();
                    labReportListViews.addAll(response);
                    labReportItemView.setVisibility(View.VISIBLE);
                    uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                            labReportListViews, rootView);
                    labReportItemView.setAdapter(uploadLabTestReportAdpter);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    uploadLabTestReportAdpter.notifyDataSetChanged();
                    txt_no_prescr.setVisibility(View.GONE);
                    img_btn_refresh.setVisibility(View.GONE);
                } else {
                    isList = false;
                    img_btn_refresh.setVisibility(View.GONE);
                    labReportItemView.setVisibility(View.GONE);
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_REPORT);
                }
            }
            sortby_apply_flag = false;
            isRest = true;
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        }

    }

    private void allfilter() {
        clickDoctorName = AppPreference.getInstance().getFilterDoctorReports();
        clickDiseaseName = AppPreference.getInstance().getFilterDieseReports();
        clickDates = AppPreference.getInstance().getFilterDateReports();
        clickUploadBy = AppPreference.getInstance().getFilterUploadBy();

        List<LabReportListView> response = DbOperations.getLabTestReportListAfterSelection(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickDates, clickDoctorName, clickDiseaseName, clickUploadBy);

        if (response != null) {
            isList = true;
            labReportListViews = null;
            labReportListViews = new ArrayList<>();
            labReportListViews.addAll(response);
            labReportItemView.setVisibility(View.VISIBLE);
            uploadLabTestReportAdpter = new UploadLabTestReportAdpter(FragmentLabTestReport.this, CureFull.getInstanse().getActivityIsntanse(),
                    labReportListViews, rootView);
            labReportItemView.setAdapter(uploadLabTestReportAdpter);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            uploadLabTestReportAdpter.notifyDataSetChanged();
            txt_no_prescr.setVisibility(View.GONE);
            img_btn_refresh.setVisibility(View.GONE);
        } else {
            labReportItemView.setVisibility(View.GONE);
            txt_no_prescr.setVisibility(View.VISIBLE);
            txt_no_prescr.setText(MyConstants.CustomMessages.NO_REPORT);
        }


        //}
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
            offset = +offsets;
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_LAB_TEST_REPORT_list + "?limit=10&offset=" + offset + "&sortBy=" + clickShortBy + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

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
                            /*txt_no_prescr.setText("No Reports Yet.");
                            txt_no_prescr.setVisibility(View.VISIBLE);*/
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
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
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
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CfUuhidList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                                            AppPreference.getInstance().setcf_uuhidNeew(uhidItemses.get(i).getCfUuhid());
                                            txt_sort_user_name.setText("" + uhidItemses.get(i).getName());
                                        } else {
                                            if (uhidItemses.get(i).isDefaults())
                                                txt_sort_user_name.setText("" + uhidItemses.get(i).getName());
                                        }
                                    }
                                } else {
                                    txt_sort_user_name.setText("User Name");
                                }

                                getLabReportList();
                                getAllFilterData();
                            } else {
                                txt_sort_user_name.setText("User Name");
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setText(MyConstants.CustomMessages.No_DATA);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                img_btn_refresh.setVisibility(View.VISIBLE);

                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    if (json12.getInt("errorCode") == 110001) {
                                        preferences.edit().putBoolean("logout", true).commit();
                                        AppPreference.getInstance().setIsFirstTimeSteps(false);
                                        CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                        CureFull.getInstanse().getActivityIsntanse().startActivity(new Intent(getActivity(), FragmentLogin.class));
                                       /* CureFull.getInstanse().getFlowInstanse()
                                                .replace(new FragmentLogin(), false);*/
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            txt_no_prescr.setText(MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                            txt_no_prescr.setVisibility(View.VISIBLE);
                            img_btn_refresh.setVisibility(View.VISIBLE);
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
        } else {

            uhiditemslocal = DbOperations.getUHIDListLocal(CureFull.getInstanse().getActivityIsntanse());
            if (uhiditemslocal != null && uhiditemslocal.size() > 0) {
                for (int i = 0; i < uhiditemslocal.size(); i++) {
                    if (uhiditemslocal.get(i).isSelected()) {


                        //AppPreference.getInstance().setcf_uuhidNeew(uhidItemses.get(i).getCfUuhid());
                        txt_sort_user_name.setText("" + uhiditemslocal.get(i).getName());
                    } else {
                        if (uhiditemslocal.get(i).isDefaults())
                            txt_sort_user_name.setText("" + uhiditemslocal.get(i).getName());
                    }
                }
            }
            img_btn_refresh.setVisibility(View.GONE);

            getLabReportList();
            getAllFilterData();

            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            /*txt_no_prescr.setText(MyConstants.CustomMessages.No_INTERNET_USAGE);
            txt_no_prescr.setVisibility(View.VISIBLE);
            img_btn_refresh.setVisibility(View.VISIBLE);*/
        }

    }

    private void getSelectedUserList(String cfUuhid) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.SELECTED_USER_LIST + cfUuhid,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    public void checkList() {
        getAllFilterData();
        getLabReportList();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    value = 0;
                    imageName = "" + System.currentTimeMillis();
                    prescriptionImageLists = new ArrayList<PrescriptionImageList>();
                    isUploadClick = true;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_camera, 400, 0.9f, 0.9f);
//
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                    } else {
                    new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                            .setShowPicker(false)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(true)
                            .launchCamera();
//                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                        File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
//                    }
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                    }

                }
                break;
            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    isUploadClick = true;
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
        isChecked = true;
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            clickDoctorName = AppPreference.getInstance().getFilterDoctorReports();
            clickDiseaseName = AppPreference.getInstance().getFilterDieseReports();
            clickDates = AppPreference.getInstance().getFilterDateReports();
            clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
            StringBuilder s = new StringBuilder();

            if (clickDoctorName.equalsIgnoreCase("") && clickDiseaseName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                s.append("");
            } else {
                s.append("doctorName=" + clickDoctorName.replace(" ", "%20"));
                s.append("&testName=" + clickDiseaseName.replace(" ", "%20"));
                s.append("&date=" + clickDates);
                s.append("&uploadedBy=" + clickUploadBy);
            }
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.REPORTS_FILTER_DATA + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.e("res", " " + response);
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setText(MyConstants.CustomMessages.No_DATA);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                img_btn_refresh.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                            /*txt_no_prescr.setText(MyConstants.CustomMessages.No_INTERNET_USAGE);
                            txt_no_prescr.setVisibility(View.VISIBLE);
                            img_btn_refresh.setVisibility(View.VISIBLE);*/
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
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                    return headers;
                }
            };

            CureFull.getInstanse().getRequestQueue().add(postRequest);
        } else {
            boolean isdatdoc = false;
            boolean isdatetest = false;//date disease
            boolean isdoctortest = false;
            String doublechk = "0";
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            clickDoctorName = AppPreference.getInstance().getFilterDoctorReports();
            clickDiseaseName = AppPreference.getInstance().getFilterDieseReports();
            clickDates = AppPreference.getInstance().getFilterDateReports();
            clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
            if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("")) {
                isdatdoc = true;
            }
            if (!clickDates.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                isdatetest = true;
                doublechk = "1";
            }
            if (!clickDoctorName.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                isdoctortest = true;
                doublechk = "1";
            }

            if (isdoctortest) {
                if (!clickDoctorName.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                    responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "doctortest", clickDates, clickDoctorName, clickDiseaseName);

                    if (responsefilter != null) {
                        showAdpter(responsefilter.getDateList(), "date");//"doctor""uploadBy"

                        liner_filter_date.setBackgroundResource(R.color.transprent_new);
                        liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                        liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                        liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                        txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_doctor.setTextColor(getResources().getColor(R.color.white));
                        txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    }
                }
            }


            if (isdatetest) {
                if (!clickDates.equalsIgnoreCase("") && !clickDiseaseName.equalsIgnoreCase("")) {
                    responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "datetest", clickDates, clickDoctorName, clickDiseaseName);

                    if (responsefilter != null) {
                        showAdpter(responsefilter.getDoctorNameList(), "doctor");//"doctor""uploadBy"

                        liner_filter_date.setBackgroundResource(R.color.transprent_new);
                        liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                        liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                        liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                        txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_doctor.setTextColor(getResources().getColor(R.color.white));
                        txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    }
                }
            }

            if (isdatdoc) {
                if (!clickDates.equalsIgnoreCase("") && !clickDoctorName.equalsIgnoreCase("")) {
                    responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "datedoctor", clickDates, clickDoctorName, clickDiseaseName);

                    if (responsefilter != null) {
                        showAdpter(responsefilter.getDiseaseNameList(), "disease");//"doctor""uploadBy"

                        liner_filter_date.setBackgroundResource(R.color.transprent_new);
                        liner_filter_disease.setBackgroundResource(R.color.health_yellow);
                        liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                        liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                        txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_diease.setTextColor(getResources().getColor(R.color.white));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                    }
                }
            }

            if (doublechk.equalsIgnoreCase("0")) {
                if (!isdatdoc) {
                    if (!clickDates.equalsIgnoreCase("")) {
                        responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "doctor", clickDates, clickDoctorName, clickDiseaseName);
                        if (responsefilter != null) {
                            showAdpter(responsefilter.getDoctorNameList(), "doctor");//"doctor""uploadBy"

                            liner_filter_date.setBackgroundResource(R.color.transprent_new);
                            liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                            liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                            liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                            txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_doctor.setTextColor(getResources().getColor(R.color.white));
                            txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        }
                    }
                    if (!clickDoctorName.equalsIgnoreCase("")) {
                        responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "date", clickDates, clickDoctorName, clickDiseaseName);
                        if (responsefilter != null) {
                            showAdpter(responsefilter.getDateList(), "date");

                            liner_filter_date.setBackgroundResource(R.color.health_yellow);
                            liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                            liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                            liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                            txt_date.setTextColor(getResources().getColor(R.color.white));
                            txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        }
                    }

                    if (!clickDiseaseName.equalsIgnoreCase("")) {
                        responsefilter = DbOperations.getFilLabTestDateClick(CureFull.getInstanse().getActivityIsntanse(), "disease", clickDates, clickDoctorName, clickDiseaseName);
                        if (responsefilter != null) {
                            showAdpter(responsefilter.getDateList(), "date");

                            liner_filter_date.setBackgroundResource(R.color.health_yellow);
                            liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                            liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                            liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);

                            txt_date.setTextColor(getResources().getColor(R.color.white));
                            txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                            txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        }

                    }
                }
            }
            if (isButtonRest) {
                isButtonRest = false;
                filterbyDate();
            }

        }

    }

    private void filterbyDate() {
        responsefilter = DbOperations.getFilterDataReports(CureFull.getInstanse().getActivityIsntanse(), "pm.reportDate", AppPreference.getInstance().getcf_uuhidNeew());
        if (responsefilter != null) {
            showAdpter(responsefilter.getDateList(), "date");

        }
    }

    private void filterbyDoctor() {
        //pm.doctorName,pm.reportDate
        responsefilter = DbOperations.getFilterDataReports(CureFull.getInstanse().getActivityIsntanse(), "pm.doctorName", AppPreference.getInstance().getcf_uuhidNeew());
        if (responsefilter != null) {
            showAdpter(responsefilter.getDoctorNameList(), "doctor");

        }
    }

    private void filterbyUplodedby() {
        responsefilter = DbOperations.getFilterDataReports(CureFull.getInstanse().getActivityIsntanse(), "pm.uploadedBy", AppPreference.getInstance().getcf_uuhidNeew());

        if (responsefilter != null) {
            showAdpter(responsefilter.getUploadedByList(), "uploadBy");

        }
    }

    private void filterbyTestName() {
        responsefilter = DbOperations.getFilterDataReports(CureFull.getInstanse().getActivityIsntanse(), "pm.testName", AppPreference.getInstance().getcf_uuhidNeew());

        if (responsefilter != null) {
            showAdpter(responsefilter.getDiseaseNameList(), "disease");
        }
    }

    //filterbyTestName
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

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null,
                    null, null);
            int column_index = cursor
                    .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }


    public void jsonSaveUploadPrescriptionMetadata(String prescriptionDate, String doctorName, String disease, final List<PrescriptionImageList> file) {
        JSONObject data = JsonUtilsObject.toSaveUploadLabReposrtMetadata(prescriptionDate, doctorName, disease);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_UPLOAD_LAB_REPORTS_METADATA, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");

                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String prescriptionId = json1.getString("labReportId");
                                String cfUuhidId = json1.getString("cfUuhidId");
                                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                                    getSaveUploadPrescriptionMetadata(prescriptionId, cfUuhidId, file);
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                    dialogLoader.hide();
                                }
                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialogLoader.hide();
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
    }

    private void getSaveUploadPrescriptionMetadata(final String prescriptionId, final String cfUuhidId, final List<PrescriptionImageList> file) {
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
                                    if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                                        valueUpload = 0;
                                        uploadFile(prescriptionId, cfUuhidId, accessKeyID, secretAccessKey, sessionToken, MyConstants.AWSType.BUCKET_NAME + "/" + AppPreference.getInstance().getcf_uuhidNeew() + MyConstants.AWSType.FOLDER_LAB_REPORT_NAME, file);
                                    } else {
                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                        dialogLoader.hide();
                                    }
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
                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                        dialogLoader.hide();
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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }

    public void uploadFile(final String prescriptionId, final String cfUuhidId, String accessKeyID, String secretAccessKey, String sessionToken, String bucketName, final List<PrescriptionImageList> imageFile) {

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

        for (int i = 0; i < imageFile.size(); i++) {
            TransferUtility transferUtility = new TransferUtility(s3client, CureFull.getInstanse().getActivityIsntanse());
            // Request server-side encryption.
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setSSEAlgorithm(ObjectMetadata.AES_256_SERVER_SIDE_ENCRYPTION);
            try {
                File fileUpload = new File(compressImage(imageFile.get(i).getPrescriptionImage()));
                String[] spiltName = new File(imageFile.get(i).getPrescriptionImage()).getName().split("\\.");
                String getName = spiltName[1];
                String name = prescriptionId + "-" + cfUuhidId + "-" + imageFile.get(i).getImageNumber() + "." + getName;
                final TransferObserver observer = transferUtility.upload(
                        bucketName,
                        name,
                        fileUpload, CannedAccessControlList.PublicRead
                );
                final int finalI = i;
                observer.setTransferListener(new TransferListener() {
                    @Override
                    public void onStateChanged(int id, TransferState state) {
                        switch (state.name()) {
                            case "COMPLETED":
                                valueUpload += 1;
//                                Log.e("check", " - " + finalI + " size " + imageFile.size());
                                imageFile.get(finalI).setPrescriptionImage("https://s3.ap-south-1.amazonaws.com/" + MyConstants.AWSType.BUCKET_NAME + "/" + AppPreference.getInstance().getcf_uuhidNeew() + "/labReport/" + observer.getKey());
                                if (valueUpload == (imageFile.size())) {
                                    if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                                        jsonSaveUploadedPrescriptionData(prescriptionId, cfUuhidId, imageFile);
                                    } else {
                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                        dialogLoader.hide();
                                    }
                                }
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

        }

    }


    public void jsonSaveUploadedPrescriptionData(String prescriptionId, String cfuuhidId, final List<PrescriptionImageList> file) {
        JSONObject data = JsonUtilsObject.toSaveUploadedLabReportData(prescriptionId, cfuuhidId, file);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UPLOADED_LAB_REPORTS_DATA, data,
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

                                getAllFilterData();
                                getLabReportList();
                            } else {
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, "" + json12.getString("message"));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                dialogLoader.hide();
                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };
        CureFull.getInstanse().getRequestQueue().add(jsonObjectRequest);
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
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
            } else if (orientation == 3) {
                matrix.postRotate(180);
            } else if (orientation == 8) {
                matrix.postRotate(270);
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
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 98, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return filename;

    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public void getFilenameDelete() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Images");
        if (file.exists()) {
            file.delete();
        }

    }

    public void getFilenameDeleteDialog() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
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
        CureFull.getInstanse().setPostionGet(2002);
        ImagePicker.create(this)
                .folderMode(true) // set folder mode (false by default)
                .folderTitle("CureFull") // folder selection title
                .imageTitle("Tap to select") // image selection title
                .single() // single mode
                .multi() // multi mode (default mode)
                .limit(10) // max images can be selected (999 by default)
                .showCamera(false) // show camera or not (true by default)
                .imageDirectory("Camera")   // captured image directory name ("Camera" folder by default)
                .origin(images) // original selected images, used in multi mode
                .start(REQUEST_CODE_PICKER); // start image picker activity with request code
    }

    @Override
    public void optDonePath(ArrayList<Image> path, String pathCamera, int id) {
        if (id == CAPTURE_MEDIA) {
            PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
            prescriptionImageList.setImageNumber(value + 1);
            value = value + 1;
            imageName = "" + System.currentTimeMillis();
            prescriptionImageList.setPrescriptionImage(pathCamera);

            prescriptionImageList.setChecked(false);
            prescriptionImageLists.add(prescriptionImageList);
            if (newMessage.equalsIgnoreCase("yes")) {
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                dialogFullViewClickImage.setiOnDoneMoreImage(this);
                dialogFullViewClickImage.show();
            } else {
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), pathCamera, selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
            }
        } else if (id == REQUEST_CODE_PICKER) {
            images = path;
            for (int i = 0, l = images.size(); i < l; i++) {
                PrescriptionImageList labReportImageList = new PrescriptionImageList();
                labReportImageList.setImageNumber(value + 1);
                value = value + 1;
                labReportImageList.setPrescriptionImage(images.get(i).getPath());
                labReportImageList.setChecked(false);
                prescriptionImageLists.add(labReportImageList);
            }

            if (newMessage.equalsIgnoreCase("yes")) {
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "lab");
                dialogFullViewClickImage.setiOnDoneMoreImage(this);
                dialogFullViewClickImage.show();
            } else {
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
            }

        }

    }

}