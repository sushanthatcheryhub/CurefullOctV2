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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.PrescriptionImageViewAdpter;
import adpter.UploadPrescriptionAdpter;
import asyns.ParseJsonData;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogUploadNewPrescription;
import interfaces.IOnAddMoreImage;
import interfaces.IOnDoneMoreImage;
import item.property.PrescriptionImageList;
import item.property.PrescriptionImageListView;
import item.property.PrescriptionListView;
import item.property.UHIDItems;
import utils.AppPreference;
import utils.MyConstants;
import utils.RequestBuilderOkHttp;
import utils.SpacesItemDecoration;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionImageView extends Fragment {


    private View rootView;
    private RecyclerView prescriptionItemView;
    private GridLayoutManager lLayout;
    private PrescriptionImageViewAdpter prescriptionImageViewAdpter;
    private List<PrescriptionImageListView> prescriptionImageListViews;
    private TextView txt_doctor_name, txt_diease_name, txt_date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_presciption_image_view,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        txt_doctor_name = (TextView) rootView.findViewById(R.id.txt_doctor_name);
        txt_diease_name = (TextView) rootView.findViewById(R.id.txt_diease_name);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        prescriptionItemView = (RecyclerView) rootView.findViewById(R.id.grid_list_symptom);
        int spacingInPixels = 10;
        prescriptionItemView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        lLayout = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        prescriptionItemView.setLayoutManager(lLayout);
        prescriptionItemView.setHasFixedSize(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            String completeDate = "";
            String date = bundle.getString("date");
            if (!date.equalsIgnoreCase("")) {
                String[] dateFormat = date.split("-");
                int mYear = Integer.parseInt(dateFormat[0]);
                int mMonth = Integer.parseInt(dateFormat[1]);
                int mDay = Integer.parseInt(dateFormat[2]);
                try {
                    completeDate = mDay + " " + Utils.formatMonth(String.valueOf(mMonth)) + "," + mYear;
                    txt_date.setText("" + completeDate);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            txt_doctor_name.setText("" + bundle.getString("doctorName"));
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionImageListViews = bundle.getParcelableArrayList("imageList");
            Log.e("size", ":- " + prescriptionImageListViews.size());

            if (prescriptionImageListViews.size() > 0 && prescriptionImageListViews != null) {
                prescriptionImageViewAdpter = new PrescriptionImageViewAdpter(CureFull.getInstanse().getActivityIsntanse(),
                        prescriptionImageListViews, bundle.getString("doctorName"), bundle.getString("id"), bundle.getString("dieaseName"), completeDate);
                prescriptionItemView.setAdapter(prescriptionImageViewAdpter);
//                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }



}