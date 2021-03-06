package fragment.healthapp;


import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.util.List;

import adpter.PrescriptionImageViewAdpter;
import curefull.healthapp.BaseBackHandlerFragment;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.PrescriptionImageListView;
import utils.AppPreference;
import utils.SpacesItemDecoration;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionImageView extends BaseBackHandlerFragment {

//2 level
    private View rootView;
    private RecyclerView prescriptionItemView;
    private GridLayoutManager lLayout;
    private PrescriptionImageViewAdpter prescriptionImageViewAdpter;
    private List<PrescriptionImageListView> prescriptionImageListViews;
    private TextView txt_doctor_name, txt_diease_name, txt_date;
    private String uploadedBy;


    @Override
    public boolean onBackPressed() {

        if (AppPreference.getInstance().getDelete()) {
            AppPreference.getInstance().setDelete(false);
            CureFull.getInstanse().getActivityIsntanse().onBackPressed();
            return true;
        } else {
            return true;
        }
    }

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
            uploadedBy=bundle.getString("uploadedBy");
            txt_doctor_name.setText("" + bundle.getString("doctorName"));
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionImageListViews = bundle.getParcelableArrayList("imageList");

            if (prescriptionImageListViews.size() > 0 && prescriptionImageListViews != null) {
                prescriptionImageViewAdpter = new PrescriptionImageViewAdpter(CureFull.getInstanse().getActivityIsntanse(),
                        prescriptionImageListViews, bundle.getString("doctorName"), bundle.getString("id"), bundle.getString("dieaseName"), bundle.getString("date"), bundle.getString("prescriptionFollowupId"),uploadedBy);
                prescriptionItemView.setAdapter(prescriptionImageViewAdpter);
//                uploadPrescriptionAdpter.notifyDataSetChanged();
            }
        }
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }


}