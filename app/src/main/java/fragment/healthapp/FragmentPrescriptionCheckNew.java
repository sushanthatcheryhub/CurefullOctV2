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
import adpter.Filter_prescription_ListAdpterNew;
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
import item.property.PrescriptionImageList;
import item.property.PrescriptionImageListView;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.HandlePermission;
import utils.MyConstants;
import utils.SpacesItemDecoration;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionCheckNew extends BaseBackHandlerFragment implements View.OnClickListener, IOnAddMoreImage, IOnDoneMoreImage, PopupWindow.OnDismissListener, IOnOtpDonePath {
    private int localoffset = 0;
    private List<UHIDItems> uhiditemslocal;
    private View rootView;
    private int REQUEST_CODE_PICKER = 2002;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE = 1777;
    public static final int CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT = 1778;
    public static final int SELECT_PHOTO = 12345;
    private static final int CAPTURE_MEDIA = 368;
    private RelativeLayout realtive_notes;
    private RelativeLayout realtive_notesShort, liner_short_by, txt_filter_by, liner_upload_new;
    private RelativeLayout realtive_notesFilter;
    private LinearLayout revealView, layoutButtons, liner_animation_upload;
    private LinearLayout liner_gallery, liner_camera, liner_btn_done, liner_filter_btn_reset;
    private LinearLayout revealViewShort, layoutButtonsShort;
    private LinearLayout revealViewFilter, layoutButtonsFilter;
    private float pixelDensity;
    boolean flag = true;
    boolean flagShort = true;
    boolean flagFilter = true;
    boolean sortby_apply_flag = false;
    boolean isApplyFilter = false;
    private ImageView img_user_name, img_upload, img_gallery, img_camera, img_upload_animation;
    private RecyclerView prescriptionItemView;
    private GridLayoutManager lLayout;
    private UploadPrescriptionAdpterNew uploadPrescriptionAdpter;
    private List<PrescriptionListView> prescriptionListViewsDummy;
    private List<PrescriptionListView> prescriptionListViews = new ArrayList<>();
    private ArrayList<PrescriptionListView> prescripdata = new ArrayList<>();
    private List<PrescriptionImageList> prescriptionImageLists;
    private String selectUploadPrescription = "";
    private int value = 0;
    private int valueUpload = 0;
    private ListPopupWindow listPopupWindow4;
    private TextView txt_no_prescr;
    private ImageView img_btn_refresh;
    private TextView txt_sort_user_name, txt_total_prescription;
    private List<UHIDItems> uhidItemses;
    private String path;
    private String imageName = "" + System.currentTimeMillis();
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1, attectPosittion;
    private ImageView btn_reset, img_doctor_name, img_disease_name, img_upload_by, img_date;
    private String newMessage = "";
    private String clickDoctorName = "", clickDiseaseName = "", clickUploadBy = "", clickDates = "", clickShortBy = "DESC";
    private String checkDialog = "";
    private boolean isButtonRest = false, isUploadClick = false, isloadMore = false, isOpenShortBy = false, isOpenUploadNew = false, isOpenFilter = false;
    private String doctorName, dieaseName, prescriptionDate;
    private int offset = 0;
    private FilterDataPrescription filterDataPrescription;
    private LinearLayout liner_filter_date, liner_filter_doctor, liner_filter_disease, liner_filter_uploadby;
    private TextView txt_date, txt_doctor, txt_diease, txt_uploadby, txt_pre_total;
    private RecyclerView recyclerView_filter;
    private Filter_prescription_ListAdpterNew filter_prescription_listAdpter;
    private RadioGroup radioShort;
    private RadioButton radioNewtest, radioOldest;
    private LinearLayout txt_short_cancel, txt_short_apply;
    private boolean isList = false, isRest = true, isChecked = true;
    private DialogLoader dialogLoader;
    private ArrayList<Image> images = null;
    static SharedPreferences preferences;
    private FilterDataPrescription responsefilter = null;
    int countofFileslocal = 1;

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
        rootView = inflater.inflate(R.layout.fragment_health_presciption,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        CureFull.getInstanse().getActivityIsntanse().isbackButtonVisible(false, "Prescription");
        CureFull.getInstanse().getActivityIsntanse().isTobBarButtonVisible(true, "");
        dialogLoader = new DialogLoader(CureFull.getInstanse().getActivityIsntanse());
        dialogLoader.setCancelable(false);
        dialogLoader.setCanceledOnTouchOutside(false);
        dialogLoader.hide();
        preferences = PreferenceManager.getDefaultSharedPreferences(CureFull.getInstanse().getActivityIsntanse());
        CureFull.getInstanse().setiOnOtpDonePath(this);
        AppPreference.getInstance().setIsEditGoalPage(false);
        AppPreference.getInstance().setFragmentHealthApp(false);
        AppPreference.getInstance().setFragmentHealthNote(false);
        AppPreference.getInstance().setFragmentHealthpre(true);
        AppPreference.getInstance().setFragmentHealthReprts(false);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(0);
        txt_short_cancel = (LinearLayout) rootView.findViewById(R.id.txt_short_cancel);
        txt_short_apply = (LinearLayout) rootView.findViewById(R.id.txt_short_apply);
        radioShort = (RadioGroup) rootView.findViewById(R.id.radioShort);
        radioNewtest = (RadioButton) rootView.findViewById(R.id.radioNewtest);
        radioOldest = (RadioButton) rootView.findViewById(R.id.radioOldest);
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
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(CureFull.getInstanse().getActivityIsntanse());
        recyclerView_filter.setLayoutManager(mLayoutManager);


        txt_total_prescription = (TextView) rootView.findViewById(R.id.txt_total_prescription);
        img_btn_refresh = (ImageView) rootView.findViewById(R.id.img_btn_refresh);
        img_user_name = (ImageView) rootView.findViewById(R.id.img_user_name);
        txt_sort_user_name = (TextView) rootView.findViewById(R.id.txt_sort_user_name);
        txt_no_prescr = (TextView) rootView.findViewById(R.id.txt_no_prescr);
        img_upload_animation = (ImageView) rootView.findViewById(R.id.img_upload_animation);
        img_gallery = (ImageView) rootView.findViewById(R.id.img_gallery);
        img_camera = (ImageView) rootView.findViewById(R.id.img_camera);
        img_upload = (ImageView) rootView.findViewById(R.id.img_upload);
        pixelDensity = getResources().getDisplayMetrics().density;
        realtive_notesShort = (RelativeLayout) rootView.findViewById(R.id.realtive_notesShort);
        realtive_notes = (RelativeLayout) rootView.findViewById(R.id.realtive_notes);
        realtive_notesFilter = (RelativeLayout) rootView.findViewById(R.id.realtive_notesFilter);
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
//        liner_animation_upload.setOnClickListener(this);
        txt_short_apply.setOnClickListener(this);
        txt_short_cancel.setOnClickListener(this);
        liner_upload_new.setOnClickListener(this);
        liner_camera.setOnClickListener(this);
        liner_gallery.setOnClickListener(this);
        liner_short_by.setOnClickListener(this);
        txt_filter_by.setOnClickListener(this);
        liner_filter_date.setOnClickListener(this);
        liner_filter_doctor.setOnClickListener(this);
        liner_filter_disease.setOnClickListener(this);
        liner_filter_uploadby.setOnClickListener(this);
        liner_btn_done.setOnClickListener(this);
        liner_filter_btn_reset.setOnClickListener(this);

        img_btn_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                getAllHealthUserList();
                getAllFilterData();
            }
        });
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
//        btn_reset.setOnClickListener(this);

        prescriptionItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        prescriptionItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        prescriptionItemView.setLayoutManager(lLayout);
        prescriptionItemView.setHasFixedSize(true);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        AppPreference.getInstance().setFilterDate("");
        AppPreference.getInstance().setFilterDoctor("");
        AppPreference.getInstance().setFilterDiese("");
        AppPreference.getInstance().setFilterUploadBy("");
        getAllHealthUserList();
//        getPrescriptionList();

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
                    listPopupWindow4.setOnDismissListener(FragmentPrescriptionCheckNew.this);
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
                        listPopupWindow4.setOnDismissListener(FragmentPrescriptionCheckNew.this);
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
            CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
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
                AppPreference.getInstance().setFilterDate("");
                AppPreference.getInstance().setFilterDoctor("");
                AppPreference.getInstance().setFilterDiese("");
                AppPreference.getInstance().setFilterUploadBy("");
                getAllFilterData();
                getPrescriptionList();
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
                //for update
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
                getPrescriptionList();
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
                getPrescriptionList();

                break;

            case R.id.txt_short_cancel:
                if (isRest) {
                    isChecked = false;
                    isRest = false;
                    isButtonRest = true;
                    radioNewtest.setChecked(false);
                    radioOldest.setChecked(false);
                    clickDoctorName = "";
                    clickDiseaseName = "";
                    clickDates = "";
                    clickUploadBy = "";
                    AppPreference.getInstance().setFilterDate("");
                    AppPreference.getInstance().setFilterDoctor("");
                    AppPreference.getInstance().setFilterDiese("");
                    AppPreference.getInstance().setFilterUploadBy("");
                    getAllFilterData();
                    getPrescriptionList();
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

            case R.id.liner_btn_done:
                liner_upload_new.post(new Runnable() {
                    @Override
                    public void run() {
                        launchTwitterFilterBy(rootView);
                    }
                });
                clickDoctorName = AppPreference.getInstance().getFilterDoctor();
                clickDiseaseName = AppPreference.getInstance().getFilterDiese();
                clickDates = AppPreference.getInstance().getFilterDate();
                clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
                isApplyFilter = true;
                getPrescriptionList();
                getAllFilterData();
                break;
            case R.id.txt_filter_date:
                liner_filter_date.setBackgroundResource(R.color.health_yellow);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_date.setTextColor(getResources().getColor(R.color.white));
                txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataPrescription.getDateList() != null && filterDataPrescription.getDateList().size() > 0) {
                        showAdpter(filterDataPrescription.getDateList(), "date");
                    }
                } else {
                    filterbyDate();
                }
                break;
            case R.id.txt_filter_doctor:
                liner_filter_date.setBackgroundResource(R.color.transprent_new);
                liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                liner_filter_doctor.setBackgroundResource(R.color.health_yellow);
                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_date.setTextColor(getResources().getColor(R.color.health_dark_gray));
                txt_doctor.setTextColor(getResources().getColor(R.color.white));
                txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));


                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (filterDataPrescription.getDoctorNameList() != null && filterDataPrescription.getDoctorNameList().size() > 0) {
                        showAdpter(filterDataPrescription.getDoctorNameList(), "doctor");
                    }
                } else {
                    filterbyDoctor();
                }
                break;
//            case R.id.txt_filter_disease:
//                liner_filter_date.setBackgroundResource(R.color.transprent_new);
//                liner_filter_disease.setBackgroundResource(R.color.health_yellow);
//                liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
//                liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
//                txt_date.setTextColor(getResources().getColor(R.color.health_yellow));
//                txt_doctor.setTextColor(getResources().getColor(R.color.health_yellow));
//                txt_diease.setTextColor(getResources().getColor(R.color.health_red_drawer));
//                txt_uploadby.setTextColor(getResources().getColor(R.color.health_yellow));
//                if (filterDataPrescription.getDiseaseNameList() != null && filterDataPrescription.getDiseaseNameList().size() > 0) {
//                    showAdpter(filterDataPrescription.getDiseaseNameList(), "disease");
//                }
//                break;
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
                    if (filterDataPrescription.getUploadedByList() != null && filterDataPrescription.getUploadedByList().size() > 0) {
                        showAdpter(filterDataPrescription.getUploadedByList(), "uploadBy");
                    }
                } else {
                    filterbyUploadBy();
                }
                break;
            case R.id.liner_filter_btn_reset:
                if (isRest) {
                    isChecked = false;
                    isRest = false;
                    isButtonRest = true;
                    radioNewtest.setChecked(false);
                    radioOldest.setChecked(false);
                    clickDoctorName = "";
                    clickDiseaseName = "";
                    clickDates = "";
                    clickUploadBy = "";
                    AppPreference.getInstance().setFilterDate("");
                    AppPreference.getInstance().setFilterDoctor("");
                    AppPreference.getInstance().setFilterDiese("");
                    AppPreference.getInstance().setFilterUploadBy("");
                    getAllFilterData();
                    getPrescriptionList();
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


            case R.id.liner_animation_upload:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_upload_animation, 400, 0.9f, 0.9f);
                isUploadClick = false;
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
                //if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                if (HandlePermission.checkPermissionWriteExternalStorage(CureFull.getInstanse().getActivityIsntanse())) {
                    isUploadClick = true;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                }

                /*} else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                }*/

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
                    // if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
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


            case R.id.liner_camera:
                //if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                if (HandlePermission.checkPermissionCamera(CureFull.getInstanse().getActivityIsntanse())) {
                    newMessage = "";
                    value = 0;
                    imageName = "" + System.currentTimeMillis();
                    prescriptionImageLists = new ArrayList<>();
                    isUploadClick = false;
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_camera, 400, 0.9f, 0.9f);
//                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
////                            Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
////                            File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
////                            Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
////                                    BuildConfig.APPLICATION_ID + ".provider",
////                                    file);
////                            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
////                            startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
////                            CureFull.getInstanse().setPostionGet(1777);
//
//                            new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
//                                    .setShowPicker(false)
//                                    .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
//                                    .enableImageCropping(true)
//                                    .launchCamera();
//                        } else {

                    new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                            .setShowPicker(false)
                            .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                            .enableImageCropping(true)
                            .launchCamera();
//                            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                            intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
//                            startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT);
                }

//                    }
                /*} else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                }*/


                break;
            case R.id.liner_gallery:


                //if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                value = 0;
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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_gallery, 400, 0.9f, 0.9f);
                images = new ArrayList<>();
                start();
//                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
//                        Intent intent = new Intent(Intent.ACTION_PICK);
////                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
////                        intent.setAction(Intent.ACTION_GET_CONTENT);
//                        intent.setType("image/*");
//                        startActivityForResult(Intent.createChooser(intent, "Select Picture"), SELECT_PHOTO);
//                    } else {
//                        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
//                        photoPickerIntent.setType("image/*");
//                        startActivityForResult(photoPickerIntent, SELECT_PHOTO);
//                    }

                /*} else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);

                }*/
                break;
        }
    }

    private void filterbyUploadBy() {
        responsefilter = DbOperations.getFilterDataPrescriptionDate(CureFull.getInstanse().getActivityIsntanse(), "pm.uploadedBy", AppPreference.getInstance().getcf_uuhidNeew());

        if (responsefilter != null) {

            showAdpter(responsefilter.getUploadedByList(), "uploadBy");//"doctor""uploadBy"
        }


    }

    private void filterbyDoctor() {
        responsefilter = DbOperations.getFilterDataPrescriptionDate(CureFull.getInstanse().getActivityIsntanse(), "pm.doctorName", AppPreference.getInstance().getcf_uuhidNeew());

        if (responsefilter != null) {

            showAdpter(responsefilter.getDoctorNameList(), "doctor");
        }
    }


//    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode != 0) {
//
//            if (requestCode == REQUEST_CODE_PICKER && resultCode == RESULT_OK && data != null) {
//                images = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
//                for (int i = 0, l = images.size(); i < l; i++) {
//                    PrescriptionImageList labReportImageList = new PrescriptionImageList();
//                    labReportImageList.setImageNumber(value + 1);
//                    value = value + 1;
//                    labReportImageList.setPrescriptionImage(images.get(i).getPath());
//                    labReportImageList.setChecked(false);
//                    prescriptionImageLists.add(labReportImageList);
//                }
//                if (newMessage.equalsIgnoreCase("yes")) {
//                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
//                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
//                    dialogFullViewClickImage.show();
//                } else {
//                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
//                    dialogUploadNewPrescription.setiOnAddMoreImage(this);
//                    dialogUploadNewPrescription.show();
//                }
//
//
//            }
//            if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
//                //Get our saved file into a bitmap object:
//                File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
//                prescriptionImageList.setImageNumber(value + 1);
//                value = value + 1;
//                imageName = "" + System.currentTimeMillis();
//                prescriptionImageList.setPrescriptionImage(file.getAbsolutePath());
//                prescriptionImageList.setChecked(false);
//                prescriptionImageLists.add(prescriptionImageList);
//                if (newMessage.equalsIgnoreCase("yes")) {
//                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
//                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
//                    dialogFullViewClickImage.show();
//                } else {
//                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), file.getAbsolutePath(), selectUploadPrescription, prescriptionImageLists);
//                    dialogUploadNewPrescription.setiOnAddMoreImage(this);
//                    dialogUploadNewPrescription.show();
//                }
//
////            img_vew.setImageBitmap(bitmap);
//            } else if (requestCode == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE_KIT) {
//                //Get our saved file into a bitmap object:
//                File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + File.separator + imageName + ".jpg");
//                PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
//                prescriptionImageList.setImageNumber(value + 1);
//                value = value + 1;
//                imageName = "" + System.currentTimeMillis();
//                prescriptionImageList.setPrescriptionImage(file.getAbsolutePath());
//
//                prescriptionImageList.setChecked(false);
//                prescriptionImageLists.add(prescriptionImageList);
//                if (newMessage.equalsIgnoreCase("yes")) {
//                    DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
//                    dialogFullViewClickImage.setiOnDoneMoreImage(this);
//                    dialogFullViewClickImage.show();
//                } else {
//                    DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), file.getAbsolutePath(), selectUploadPrescription, prescriptionImageLists);
//                    dialogUploadNewPrescription.setiOnAddMoreImage(this);
//                    dialogUploadNewPrescription.show();
//                }
//
////            img_vew.setImageBitmap(bitmap);
//            } else {
//                if (data != null) {
//                    if (requestCode == SELECT_PHOTO) {
//                        if (data != null) {
//                            Uri pickedImage = data.getData();
//                            String[] filePath = {MediaStore.Images.Media.DATA};
//                            Cursor cursor = CureFull.getInstanse().getActivityIsntanse().getContentResolver().query(pickedImage, filePath, null, null, null);
//                            cursor.moveToFirst();
//                            String imagePath = cursor.getString(cursor.getColumnIndex(filePath[0]));
//                            PrescriptionImageList labReportImageList = new PrescriptionImageList();
//                            labReportImageList.setImageNumber(value + 1);
//                            value = value + 1;
//                            labReportImageList.setPrescriptionImage(imagePath);
//                            labReportImageList.setChecked(false);
//                            prescriptionImageLists.add(labReportImageList);
//
//
//                            if (newMessage.equalsIgnoreCase("yes")) {
//                                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
//                                dialogFullViewClickImage.setiOnDoneMoreImage(this);
//                                dialogFullViewClickImage.show();
//                            } else {
//                                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
//                                dialogUploadNewPrescription.setiOnAddMoreImage(this);
//                                dialogUploadNewPrescription.show();
//                            }
//                        }
//
//                    }
//                }
//
//            }
//
//        }
//
//    }

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
        /*
         MARGIN_RIGHT = 16;
         FAB_BUTTON_RADIUS = 28;
         */

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
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    if (prescriptionListViews != null) {
                        txt_total_prescription.setText("Prescription (" + prescriptionListViews.size() + ")");

                    }
                } else {
                    if (prescripdata != null) {
                        txt_total_prescription.setText("Prescription (" + prescripdata.size() + ")");
                    }
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
                    if (prescriptionListViews != null & prescriptionListViews.size() > 0) {
                        txt_pre_total.setText("Prescription (" + prescriptionListViews.size() + ")");
                    }

                    if (filterDataPrescription != null) {
                        liner_filter_date.setBackgroundResource(R.color.health_yellow);
                        liner_filter_disease.setBackgroundResource(R.color.transprent_new);
                        liner_filter_doctor.setBackgroundResource(R.color.transprent_new);
                        liner_filter_uploadby.setBackgroundResource(R.color.transprent_new);
                        txt_date.setTextColor(getResources().getColor(R.color.white));
                        txt_doctor.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_diease.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        txt_uploadby.setTextColor(getResources().getColor(R.color.health_dark_gray));
                        showAdpter(filterDataPrescription.getDateList(), "date");
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
                    if (prescripdata != null & prescripdata.size() > 0) {
                        txt_pre_total.setText("Prescription (" + prescripdata.size() + ")");
                    }
                    filterbyDate();
                    if (responsefilter != null) {
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

            }


        }
    }

    @Override
    public void optAddMoreImage(String messsage) {
        if (messsage.equalsIgnoreCase("done")) {
            DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
            dialogFullViewClickImage.setiOnDoneMoreImage(this);
            dialogFullViewClickImage.show();
        } else if (messsage.equalsIgnoreCase("retry")) {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                prescriptionImageLists.remove(prescriptionImageLists.size() - 1);
                value = prescriptionImageLists.size() - 1;
                imageName = "" + System.currentTimeMillis();
                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();

            } else {
                prescriptionImageLists.remove(prescriptionImageLists.size() - 1);
                value = prescriptionImageLists.size() - 1;
                images = new ArrayList<>();
                start();
            }
        } else {
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {

                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();
            } else {
                images = new ArrayList<>();
                start();
            }
        }
    }

    @Override
    public void optDoneMoreImage(final String doctorNames, final String dieaseNames, final String prescriptionDates, final List<PrescriptionImageList> prescriptionImageListss, String mesaage) {
        if (mesaage.equalsIgnoreCase("yes")) {
            prescriptionImageListss.remove(prescriptionImageListss.size() - 1);
            newMessage = mesaage;
            if (selectUploadPrescription.equalsIgnoreCase("camera")) {
                new SandriosCamera(CureFull.getInstanse().getActivityIsntanse(), CAPTURE_MEDIA)
                        .setShowPicker(false)
                        .setMediaAction(CameraConfiguration.MEDIA_ACTION_PHOTO)
                        .enableImageCropping(true)
                        .launchCamera();
            } else {
                images = new ArrayList<>();
                start();
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

                        PrescriptionListView imagelist = new PrescriptionListView();
                        imagelist.uploadFilelocal(prescriptionDate, doctorName, dieaseName, AppPreference.getInstance().getcf_uuhidNeew(), prescriptionImageListss, prescriptionImageListss.size(), "self");

                        dialogLoader.hide();
                        getPrescriptionList();
                    }
                }
            });
        }
    }


    private void getPrescriptionList() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            isloadMore = false;
            offset = 0;
            prescriptionListViews = null;
            prescriptionListViews = new ArrayList<>();

            StringBuilder s = new StringBuilder();
            if (!clickDoctorName.equalsIgnoreCase("")) {
                s.append("&doctorName=" + clickDoctorName.replace(" ", "%20"));
            }
//            if (!clickDiseaseName.equalsIgnoreCase("")) {
//                s.append("&diseaseName=" + clickDiseaseName.replace(" ", "%20"));
//            }
            if (!clickDates.equalsIgnoreCase("")) {
                s.append("&date=" + clickDates);
            }
            if (!clickUploadBy.equalsIgnoreCase("")) {
                s.append("&uploadedBy=" + clickUploadBy);
            }

            if (clickDoctorName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                s.append("");
            }

            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_PRESCRIPTION_LIST + "?limit=10&offset=" + offset + "&sortBy=" + clickShortBy + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
//                            Log.e("jgjg", "response" + response);
//                            Log.e("jgjg", "jj" + AppPreference.getInstance().getcf_uuhidNeew());
                            isRest = true;
                            dialogLoader.hide();
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
                                prescriptionListViewsDummy = ParseJsonData.getInstance().getPrescriptionList(response);
                                if (prescriptionListViewsDummy != null && prescriptionListViewsDummy.size() > 0) {

                                    isList = true;
                                    prescriptionListViews.addAll(prescriptionListViewsDummy);
                                    if (prescriptionListViewsDummy.size() < 10) {
                                        isloadMore = true;
                                    }
                                    AppPreference.getInstance().setPrescriptionSize(1);
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);
                                    prescriptionItemView.setVisibility(View.VISIBLE);
                                    uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                                            prescriptionListViews, rootView);
                                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadPrescriptionAdpter.notifyDataSetChanged();
                                } else {
                                    isList = false;
                                    if (prescriptionListViewsDummy == null) {
                                        isloadMore = true;
                                    }
                                    AppPreference.getInstance().setPrescriptionSize(0);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    img_btn_refresh.setVisibility(View.GONE);
                                    txt_no_prescr.setText("No Prescriptions Yet.");
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                    prescriptionItemView.setVisibility(View.GONE);
                                }
                            } else {
                                img_btn_refresh.setVisibility(View.GONE);
                                txt_no_prescr.setText("No Prescriptions Yet.");
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
//                            Log.e("error", "error");
                            isRest = true;
                            dialogLoader.hide();

                            if (sortby_apply_flag) {
                                List<PrescriptionListView> res = DbOperations.getPrescriptionListALLSort(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickShortBy, offset);
                                if (res.size() > 0) {
                                    isList = true;
                                    prescriptionItemView.setVisibility(View.VISIBLE);
                                    prescripdata = null;
                                    prescripdata = new ArrayList<>();
                                    prescripdata.addAll(res);

                                    uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                                            prescripdata, rootView);
                                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadPrescriptionAdpter.notifyDataSetChanged();
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);

                                } else {
                                    isList = false;
                                    AppPreference.getInstance().setPrescriptionSize(0);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    img_btn_refresh.setVisibility(View.GONE);
                                    prescriptionItemView.setVisibility(View.GONE);
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_PRESCRIPTION);
                                }
                            } else {
                                List<PrescriptionListView> response = DbOperations.getPrescriptionListALL(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew());
                                if (response.size() > 0) {
                                    //prescriptionListViewsDummy = ParseJsonData.getInstance().getPrescriptionList(response);
                                    isList = true;
                                    //prescriptionListViews = new ArrayList<>();
                                    //  prescriptionListViews.addAll(prescriptionListViewsDummy);
                                    prescriptionItemView.setVisibility(View.VISIBLE);
                                    prescripdata = null;
                                    prescripdata = new ArrayList<>();
                                    prescripdata.addAll(response);

                                    uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                                            prescripdata, rootView);
                                    //prescriptionItemView.removeAllViews();//setAdapter(null);
                                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadPrescriptionAdpter.notifyDataSetChanged();
                                    txt_no_prescr.setVisibility(View.GONE);
                                    img_btn_refresh.setVisibility(View.GONE);
                                } else {
                                    isList = false;
                                    AppPreference.getInstance().setPrescriptionSize(0);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    img_btn_refresh.setVisibility(View.GONE);
                                    prescriptionItemView.setVisibility(View.GONE);
                                    txt_no_prescr.setVisibility(View.VISIBLE);
                                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_PRESCRIPTION);
                                }
                            }
                            sortby_apply_flag = false;
                           /* prescriptionItemView.setVisibility(View.GONE);
                            txt_no_prescr.setText(MyConstants.CustomMessages.ISSUES_WITH_SERVER);
                            txt_no_prescr.setVisibility(View.VISIBLE);
                            img_btn_refresh.setVisibility(View.VISIBLE);*/
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
            //localoffset=0;
            if (sortby_apply_flag) {
                List<PrescriptionListView> res = DbOperations.getPrescriptionListALLSort(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickShortBy, offset);
                if (res.size() > 0) {
                    prescriptionItemView.setVisibility(View.VISIBLE);
                    isList = true;
                    prescripdata = null;
                    prescripdata = new ArrayList<>();
                    prescripdata.addAll(res);

                    uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                            prescripdata, rootView);
                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    uploadPrescriptionAdpter.notifyDataSetChanged();
                    txt_no_prescr.setVisibility(View.GONE);
                    img_btn_refresh.setVisibility(View.GONE);

                } else {
                    isList = false;
                    AppPreference.getInstance().setPrescriptionSize(0);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    img_btn_refresh.setVisibility(View.GONE);
                    prescriptionItemView.setVisibility(View.GONE);
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_PRESCRIPTION);
                }
            } else {
                List<PrescriptionListView> response = DbOperations.getPrescriptionListALL(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew());
                if (response.size() > 0) {
                    isList = true;
                    prescripdata = null;
                    prescripdata = new ArrayList<>();
                    prescripdata.addAll(response);
                    prescriptionItemView.setVisibility(View.VISIBLE);
                    uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                            prescripdata, rootView);
                    prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    uploadPrescriptionAdpter.notifyDataSetChanged();
                    txt_no_prescr.setVisibility(View.GONE);
                    img_btn_refresh.setVisibility(View.GONE);
                } else {
                    isList = false;
                    AppPreference.getInstance().setPrescriptionSize(0);
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                    img_btn_refresh.setVisibility(View.GONE);
                    prescriptionItemView.setVisibility(View.GONE);
                    txt_no_prescr.setVisibility(View.VISIBLE);
                    txt_no_prescr.setText(MyConstants.CustomMessages.NO_PRESCRIPTION);
                }
            }
            isRest = true;
            sortby_apply_flag = false;

            //prescriptionItemView.setVisibility(View.GONE);
            //txt_no_prescr.setText(MyConstants.CustomMessages.No_INTERNET_USAGE);
            //txt_no_prescr.setVisibility(View.VISIBLE);
            //img_btn_refresh.setVisibility(View.VISIBLE);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        }

    }


    public void callWebServiceAgain(int offsets) {
        if (isloadMore) {

        } else {
            StringBuilder s = new StringBuilder();


            if (clickDoctorName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                s.append("");
            } else {
                s.append("&doctorName=" + clickDoctorName.replace(" ", "%20"));
//            s.append("&diseaseName=" + clickDiseaseName);
                s.append("&date=" + clickDates);
                s.append("&uploadedBy=" + clickUploadBy);
            }
//            Log.e("offsect", "" + offset);
            offset = +offsets;
//            Log.e("off", "" + offsets);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.GET_PRESCRIPTION_LIST + "?limit=10&offset=" + offset + "&sortBy=" + clickShortBy + s,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                            Log.e("prescriptionlist", "" + response);

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
                                if (prescriptionListViewsDummy != null && prescriptionListViewsDummy.size() > 0) {
                                    prescriptionListViews.addAll(prescriptionListViewsDummy);
                                    if (prescriptionListViewsDummy.size() < 10) {
                                        isloadMore = true;
                                    }
                                    AppPreference.getInstance().setPrescriptionSize(1);
                                    txt_no_prescr.setVisibility(View.GONE);
                                    prescriptionItemView.setVisibility(View.VISIBLE);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                    uploadPrescriptionAdpter.notifyDataSetChanged();
                                } else {
//                                    Log.e("what ", "" + prescriptionListViewsDummy);

                                    if (prescriptionListViewsDummy == null) {
                                        isloadMore = true;
                                    }
//                                    AppPreference.getInstance().setPrescriptionSize(0);
                                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                                    txt_no_prescr.setVisibility(View.VISIBLE);
//                                    prescriptionItemView.setVisibility(View.GONE);
                                }
                            } else {
                                try {
                                    if (prescriptionListViewsDummy == null) {
                                        isloadMore = true;
                                    }
                                } catch (Exception e) {

                                }
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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
                    headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                    headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
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


    private List<String> getUserAsStringList(List<UHIDItems> result) {
        List<String> list = new ArrayList<>();

        if (result != null)
            for (UHIDItems logy : result)
                list.add(logy.getName());
        return list;
    }


    private void getAllHealthUserList() {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.CfUuhidList,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

//                            Log.e("lof", "lof " + response);
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
                                img_btn_refresh.setVisibility(View.GONE);
                                getPrescriptionList();
                                getAllFilterData();

                            } else {
                                txt_no_prescr.setText(MyConstants.CustomMessages.No_DATA);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                img_btn_refresh.setVisibility(View.VISIBLE);
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                                try {
                                    JSONObject json1 = new JSONObject(json.getString("errorInfo"));
                                    JSONObject json12 = new JSONObject(json1.getString("errorDetails"));
                                    if (json12.getInt("errorCode") == 110001) {
                                        preferences.edit().putBoolean("logout", true).commit();
                                        AppPreference.getInstance().setIsFirstTimeSteps(false);
                                        CureFull.getInstanse().getFlowInstanse().clearBackStack();
                                        CureFull.getInstanse().getActivityIsntanse().startActivity(new Intent(getActivity(), FragmentLogin.class));
                                        /*CureFull.getInstanse().getFlowInstanse()
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
        } else {
            uhiditemslocal = DbOperations.getUHIDListLocal(CureFull.getInstanse().getActivityIsntanse());
            //  uhidItemses = ParseJsonData.getInstance().getUHID(response);

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
            getPrescriptionList();
            getAllFilterData();


            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            //by sourav
           /* txt_no_prescr.setText(MyConstants.CustomMessages.No_INTERNET_USAGE);
            txt_no_prescr.setVisibility(View.VISIBLE);
            img_btn_refresh.setVisibility(View.VISIBLE);*/
        }

    }


    private void getAllFilterData() {
        isChecked = true;
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            clickDoctorName = AppPreference.getInstance().getFilterDoctor();
            clickDiseaseName = AppPreference.getInstance().getFilterDiese();
            clickDates = AppPreference.getInstance().getFilterDate();
            clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
            StringBuilder s = new StringBuilder();

            if (clickDoctorName.equalsIgnoreCase("") && clickDates.equalsIgnoreCase("") && clickUploadBy.equalsIgnoreCase("")) {
                s.append("");
            } else {
                s.append("doctorName=" + clickDoctorName.replace(" ", "%20"));
                s.append("&date=" + clickDates);
                s.append("&uploadedBy=" + clickUploadBy);
            }
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);

            StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.PRESCRIPTION_FILTER_DATA + s,
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
                                filterDataPrescription = ParseJsonData.getInstance().getFilterDataPre(response);
                                if (isButtonRest) {
                                    isButtonRest = false;
                                    if (filterDataPrescription.getDateList() != null && filterDataPrescription.getDateList().size() > 0) {
                                        showAdpter(filterDataPrescription.getDateList(), "date");
                                    }
                                }

                            } else {
                                txt_no_prescr.setText(MyConstants.CustomMessages.No_DATA);
                                txt_no_prescr.setVisibility(View.VISIBLE);
                                img_btn_refresh.setVisibility(View.VISIBLE);
                                CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
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

        } else {
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            clickDoctorName = AppPreference.getInstance().getFilterDoctor();
            clickDiseaseName = AppPreference.getInstance().getFilterDiese();
            clickDates = AppPreference.getInstance().getFilterDate();
            clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
            if (!clickDates.equalsIgnoreCase("")) {
                responsefilter = DbOperations.getFilDataPrescDateClick(CureFull.getInstanse().getActivityIsntanse(), "doctor", clickDates, clickDoctorName, clickUploadBy);
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
            if (!clickDoctorName.equalsIgnoreCase("")) {
                responsefilter = DbOperations.getFilDataPrescDateClick(CureFull.getInstanse().getActivityIsntanse(), "date", clickDates, clickDoctorName, clickUploadBy);
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
            /*
            if(!clickUploadBy.equalsIgnoreCase("")){
                responsefilter = DbOperations.getFilDataPrescDateClick(CureFull.getInstanse().getActivityIsntanse(), "pm.prescriptionDate",clickDates,clickDoctorName,clickUploadBy);
            }
*/

            if (isButtonRest) {
                isButtonRest = false;
                filterbyDate();

            }

        }
    }

    private void filterbyDate() {
        responsefilter = DbOperations.getFilterDataPrescriptionDate(CureFull.getInstanse().getActivityIsntanse(), "pm.prescriptionDate", AppPreference.getInstance().getcf_uuhidNeew());

        if (responsefilter != null) {
            showAdpter(responsefilter.getDateList(), "date");

        }
    }

    private void allfilter() {
       /* responsefilter = DbOperations.getFilterDataPrescriptionDate(CureFull.getInstanse().getActivityIsntanse(), "pm.prescriptionDate");

        if (responsefilter != null) {
            showAdpter(responsefilter.getDateList(), "date");*/
        clickDoctorName = AppPreference.getInstance().getFilterDoctor();
        clickDiseaseName = AppPreference.getInstance().getFilterDiese();
        clickDates = AppPreference.getInstance().getFilterDate();
        clickUploadBy = AppPreference.getInstance().getFilterUploadBy();
        //DbOperations.getFilterDataPrescriptionAfterSelection(CureFull.getInstanse().getActivityIsntanse(),clickDates,clickDoctorName,clickUploadBy);

        List<PrescriptionListView> res = DbOperations.getFilterDataPrescriptionAfterSelection(CureFull.getInstanse().getActivityIsntanse(), clickDates, clickDoctorName, clickUploadBy);//DbOperations.getPrescriptionListALLSort(CureFull.getInstanse().getActivityIsntanse(), AppPreference.getInstance().getcf_uuhidNeew(), clickShortBy, offset);
        if (res != null) {
            prescriptionItemView.setVisibility(View.VISIBLE);
            isList = true;
            prescripdata = null;
            prescripdata = new ArrayList<>();
            prescripdata.addAll(res);

            uploadPrescriptionAdpter = new UploadPrescriptionAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(),
                    prescripdata, rootView);
            prescriptionItemView.setAdapter(uploadPrescriptionAdpter);
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
            uploadPrescriptionAdpter.notifyDataSetChanged();
            txt_no_prescr.setVisibility(View.GONE);
            img_btn_refresh.setVisibility(View.GONE);

        } else {
            prescriptionItemView.setVisibility(View.GONE);
            txt_no_prescr.setVisibility(View.VISIBLE);
            txt_no_prescr.setText(MyConstants.CustomMessages.NO_PRESCRIPTION);
        }


        //}
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
        getPrescriptionList();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case HandlePermission.MY_PERMISSIONS_REQUEST_CAMERA:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    value = 0;
                    imageName = "" + System.currentTimeMillis();
                    isUploadClick = true;
                    prescriptionImageLists = new ArrayList<>();
                    liner_upload_new.post(new Runnable() {
                        @Override
                        public void run() {
                            launchTwitter(rootView);
                        }
                    });
                    selectUploadPrescription = "camera";
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_camera, 400, 0.9f, 0.9f);
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
//                        File file = new File(Environment.getExternalStorageDirectory() + File.separator + imageName + ".jpg");
//                        Uri photoURI = FileProvider.getUriForFile(CureFull.getInstanse().getActivityIsntanse(),
//                                BuildConfig.APPLICATION_ID + ".provider",
//                                file);
//                        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                        startActivityForResult(intent, CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE);
//                        CureFull.getInstanse().setPostionGet(1777);
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
            case HandlePermission.MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                        ElasticAction.doAction(img_upload, 400, 0.9f, 0.9f);
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
        if (checkDialog.equalsIgnoreCase("img_user_name")) {
            rotatePhoneAntiClockwise(img_user_name);
        }
    }

    public void showAdpter(ArrayList<String> strings, String filterName) {
        if (strings != null && strings.size() > 0) {
            filter_prescription_listAdpter = null;
            filter_prescription_listAdpter = new Filter_prescription_ListAdpterNew(FragmentPrescriptionCheckNew.this, CureFull.getInstanse().getActivityIsntanse(), strings, filterName);
            recyclerView_filter.setAdapter(filter_prescription_listAdpter);
            filter_prescription_listAdpter.notifyDataSetChanged();
        }
    }

    public void callFilterAgain() {
        getAllFilterData();
        if (filter_prescription_listAdpter != null)
            filter_prescription_listAdpter.notifyDataSetChanged();
    }


    public void jsonSaveUploadPrescriptionMetadata(String prescriptionDate, String doctorName, String disease, final List<PrescriptionImageList> file) {
        JSONObject data = JsonUtilsObject.toSaveUploadPrescriptionMetadata(prescriptionDate, doctorName, disease);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_UPLOAD_PRESCRIPTION_METADATA, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            //get prescriptionid and cfUhidId
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String prescriptionId = json1.getString("prescriptionId");
                                String cfUuhidId = json1.getString("cfUuhidId");
                                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                                    getSaveUploadPrescriptionMetadata(prescriptionId, cfUuhidId, file);
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                    dialogLoader.hide();
                                }
                            } else {
                                dialogLoader.hide();
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

    public void jsonSaveUploadPrescriptionMetadataFromLocal(String prescriptionDate, String doctorName, String disease, final List<PrescriptionImageList> file, final String chfUuid) {
        JSONObject data = JsonUtilsObject.toSaveUploadPrescriptionMetadata(prescriptionDate, doctorName, disease);

        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.SAVE_UPLOAD_PRESCRIPTION_METADATA, data,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                            //get prescriptionid and cfUhidId
                            if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                                JSONObject json1 = new JSONObject(json.getString("payload"));
                                String prescriptionId = json1.getString("prescriptionId");
                                String cfUuhidId = json1.getString("cfUuhidId");
                                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                                    getSaveUploadPrescriptionMetadata(prescriptionId, cfUuhidId, file);
                                } else {
                                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                    dialogLoader.hide();
                                }
                            } else {
                                dialogLoader.hide();
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
                headers.put("cf_uuhid", chfUuid);
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
                                        uploadFile(prescriptionId, cfUuhidId, accessKeyID, secretAccessKey, sessionToken, MyConstants.AWSType.BUCKET_NAME + "/" + AppPreference.getInstance().getcf_uuhidNeew() + MyConstants.AWSType.FOLDER_PRECREPTION_NAME, file);
                                    } else {
                                        CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.No_INTERNET_USAGE);
                                        dialogLoader.hide();
                                    }
                                }

                            } else {
                                dialogLoader.hide();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
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
    //prescriptionDate, doctorName, dieaseName, AppPreference.getInstance().getcf_uuhidNeew(),prescriptionImageListss

   /* private void uploadFilelocal(String prescriptionDate, String doctorName, String dieaseName, String cfUuhidId, List<PrescriptionImageList> imageFile,int countOfFiles,String uploadedBy) {

        String countoffilis=String.valueOf(countOfFiles);
        for (int i = 0; i < imageFile.size(); i++) {

            try {
                File fileUpload = new File(compressImage(imageFile.get(i).getPrescriptionImage()));
                String[] spiltName = new File(imageFile.get(i).getPrescriptionImage()).getName().split("\\.");
                String getName = spiltName[1];
                String name = cfUuhidId + "-" + imageFile.get(i).getImageNumber() + "." + getName;

                ContentValues values = new ContentValues();

                values.put("prescriptionDate", prescriptionDate);
                values.put("dieaseName", doctorName);
                values.put("doctorName", dieaseName);
                values.put("image", fileUpload.toString());
                values.put("cfUuhidId", AppPreference.getInstance().getcf_uuhidNeew());
                values.put("status", "1");
                values.put("countOfFiles",countoffilis);
                values.put("uploadedBy", "self");

                DbOperations.insertPrescriptionImage(CureFull.getInstanse().getActivityIsntanse(), values);

                //
              *//*  File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/PrescriptionLocal");
                File imagefile[] =	file.listFiles();
                for(int icount=0;icount<imagefile.length;icount++)
                {
                    Bitmap photo= BitmapFactory.decodeFile(imagefile[icount].getAbsolutePath());
                    if(photo!=null){

                    }
                }*//*

                } catch (Exception e) {
                e.printStackTrace();
            }

        }


    }*/

    public void uploadFile(final String prescriptionId, final String cfUuhidId, String accessKeyID, String secretAccessKey, String sessionToken, String bucketName, final List<PrescriptionImageList> imageFile) {
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
//                                Log.e("check", " - " + finalI + " size " + imageFile.size() + "value " + valueUpload);
                                imageFile.get(finalI).setPrescriptionImage("https://s3.ap-south-1.amazonaws.com/" + MyConstants.AWSType.BUCKET_NAME + "/" + AppPreference.getInstance().getcf_uuhidNeew() + "/prescription/" + observer.getKey());
                                if (valueUpload == (imageFile.size())) {
//                                    Log.e("call ", "call ");
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

    }


    public void jsonSaveUploadedPrescriptionData(String prescriptionId, String cfuuhidId, final List<PrescriptionImageList> file) {
        JSONObject data = JsonUtilsObject.toSaveUploadedPrescriptionData(prescriptionId, cfuuhidId, file);
        final JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, MyConstants.WebUrls.UPLOADED_PRESCRETION_DATA, data,
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
                                getPrescriptionList();
                            } else {
                                dialogLoader.hide();
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
        String filename = "";
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
            filename = getFilename();
        } else {
            filename = getFilenameLocal();
        }
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
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Prescription");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public String getFilenameLocal() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/PrescriptionLocal");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    public void getFilenameDelete() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "CureFull/Prescription");
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
        PrescriptionImageList prescriptionImageList = null;
        if (id == CAPTURE_MEDIA) {
//            Log.e("value", "" + value);
            prescriptionImageList = new PrescriptionImageList();
            prescriptionImageList.setImageNumber(value + 1);
            value = value + 1;
//            imageName = "" + System.currentTimeMillis();
            prescriptionImageList.setPrescriptionImage(pathCamera);
            prescriptionImageList.setChecked(false);
            prescriptionImageLists.add(prescriptionImageList);
//            Log.e("one", " " + prescriptionImageLists.size());
            if (newMessage.equalsIgnoreCase("yes")) {
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
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
                prescriptionImageList = new PrescriptionImageList();
                prescriptionImageList.setImageNumber(value + 1);
                value = value + 1;
                prescriptionImageList.setPrescriptionImage(images.get(i).getPath());
                prescriptionImageList.setChecked(false);
                prescriptionImageLists.add(prescriptionImageList);
            }

            if (newMessage.equalsIgnoreCase("yes")) {
                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
                dialogFullViewClickImage.setiOnDoneMoreImage(this);
                dialogFullViewClickImage.show();
            } else {
                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), "", selectUploadPrescription, prescriptionImageLists);
                dialogUploadNewPrescription.setiOnAddMoreImage(this);
                dialogUploadNewPrescription.show();
            }

        }
//        else if (id == CAPTURE_IMAGE_FULLSIZE_ACTIVITY_REQUEST_CODE) {
//
//            PrescriptionImageList prescriptionImageList = new PrescriptionImageList();
//            prescriptionImageList.setImageNumber(value + 1);
//            value = value + 1;
//            imageName = "" + System.currentTimeMillis();
//            prescriptionImageList.setPrescriptionImage(pathCamera);
//            prescriptionImageList.setChecked(false);
//            prescriptionImageLists.add(prescriptionImageList);
//            if (newMessage.equalsIgnoreCase("yes")) {
//                DialogFullViewClickImage dialogFullViewClickImage = new DialogFullViewClickImage(CureFull.getInstanse().getActivityIsntanse(), prescriptionImageLists, "Prescription");
//                dialogFullViewClickImage.setiOnDoneMoreImage(this);
//                dialogFullViewClickImage.show();
//            } else {
//                DialogUploadNewPrescription dialogUploadNewPrescription = new DialogUploadNewPrescription(CureFull.getInstanse().getActivityIsntanse(), pathCamera, selectUploadPrescription, prescriptionImageLists);
//                dialogUploadNewPrescription.setiOnAddMoreImage(this);
//                dialogUploadNewPrescription.show();
//            }
//
//        }


    }


}