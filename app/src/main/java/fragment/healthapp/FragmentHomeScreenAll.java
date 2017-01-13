package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import interfaces.IGlobalIsbackButtonVisible;
import utils.AppPreference;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHomeScreenAll extends Fragment implements IGlobalIsbackButtonVisible, View.OnClickListener {


    private View rootView;
    private RelativeLayout liner_bottom_view;
    private LinearLayout txt_bottom_heath_app, txt_bottom_health_note, txt_bottom_home, txt_bottom_prescription, txt_bottom_reports;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.home_screen,
                container, false);

        liner_bottom_view = (RelativeLayout) rootView.findViewById(R.id.liner_bottom_view);
        AppPreference.getInstance().setIsLogin(true);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showRelativeActionBar(true);
        CureFull.getInstanse().getActivityIsntanse().showLogo(false);
        txt_bottom_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_bottom_heath_app);
        txt_bottom_health_note = (LinearLayout) rootView.findViewById(R.id.txt_bottom_health_note);
        txt_bottom_home = (LinearLayout) rootView.findViewById(R.id.txt_bottom_home);
        txt_bottom_prescription = (LinearLayout) rootView.findViewById(R.id.txt_bottom_prescription);
        txt_bottom_reports = (LinearLayout) rootView.findViewById(R.id.txt_bottom_reports);
        txt_bottom_heath_app.setOnClickListener(this);
        txt_bottom_health_note.setOnClickListener(this);
        txt_bottom_home.setOnClickListener(this);
        txt_bottom_prescription.setOnClickListener(this);
        txt_bottom_reports.setOnClickListener(this);
        CureFull.getInstanse().setiGlobalIsbackButtonVisible(FragmentHomeScreenAll.this);
        CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
        CureFull.getInstanse().getFlowInstanseAll()
                .replace(new FragmentLandingPage(), false);


        return rootView;
    }


    @Override
    public void isbackButtonVisible(boolean isback) {
        if (isback) {
            liner_bottom_view.setVisibility(View.GONE);
        } else {
            liner_bottom_view.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {
            case R.id.txt_bottom_heath_app:
                if (AppPreference.getInstance().isEditGoal()) {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentHealthAppNew(), false);
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentEditGoal(), false);

                }
                break;
            case R.id.txt_bottom_health_note:
                if (AppPreference.getInstance().isEditGoalPage()) {
                    if (AppPreference.getInstance().isEditGoal()) {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentHealthNote(), false);
                    }
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentHealthNote(), false);
                }

                break;
            case R.id.txt_bottom_home:

                CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLandingPage(), false);
                break;
            case R.id.txt_bottom_prescription:
                if (AppPreference.getInstance().isEditGoalPage()) {
                    if (AppPreference.getInstance().isEditGoal()) {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentPrescriptionCheck(), false);
                    }
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentPrescriptionCheck(), false);
                }

                break;
            case R.id.txt_bottom_reports:
                if (AppPreference.getInstance().isEditGoalPage()) {
                    if (AppPreference.getInstance().isEditGoal()) {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentLabTestReport(), false);
                    }
                } else {
                    CureFull.getInstanse().getFlowInstanseAll()
                            .replace(new FragmentLabTestReport(), false);
                }
                break;

        }

    }
}