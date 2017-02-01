package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import interfaces.IGlobalIsbackButtonVisible;
import interfaces.IGlobalTopBarButtonVisible;
import utils.AppPreference;
import utils.Utils;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHomeScreenAll extends Fragment implements IGlobalIsbackButtonVisible, View.OnClickListener, IGlobalTopBarButtonVisible {


    private View rootView;
    private RelativeLayout liner_bottom_view;
    private LinearLayout top_view;
    private LinearLayout txt_bottom_heath_app, txt_bottom_health_note, txt_bottom_home, txt_bottom_prescription, txt_bottom_reports;
    private LinearLayout liner_medincine, liner_doctor_visit, liner_lab_test;
    private TextView txt_med, txt_doctor_visit, txt_lab_test;
    private ImageView img_medicine, img_doctor_visit, img_lab_test, img_health_app, img_health_note, img_health_home, img_health_pre, img_health_report;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.home_screen,
                container, false);
        top_view = (LinearLayout) rootView.findViewById(R.id.top_view);
        liner_bottom_view = (RelativeLayout) rootView.findViewById(R.id.liner_bottom_view);
        AppPreference.getInstance().setIsLogin(true);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showRelativeActionBar(true);
        CureFull.getInstanse().getActivityIsntanse().showLogo(false);
        img_health_app = (ImageView) rootView.findViewById(R.id.img_health_app);
        img_health_note = (ImageView) rootView.findViewById(R.id.img_health_note);
        img_health_home = (ImageView) rootView.findViewById(R.id.img_health_home);
        img_health_pre = (ImageView) rootView.findViewById(R.id.img_health_pre);
        img_health_report = (ImageView) rootView.findViewById(R.id.img_health_report);


        txt_bottom_heath_app = (LinearLayout) rootView.findViewById(R.id.txt_bottom_heath_app);
        txt_bottom_health_note = (LinearLayout) rootView.findViewById(R.id.txt_bottom_health_note);
        txt_bottom_home = (LinearLayout) rootView.findViewById(R.id.txt_bottom_home);
        txt_bottom_prescription = (LinearLayout) rootView.findViewById(R.id.txt_bottom_prescription);
        txt_bottom_reports = (LinearLayout) rootView.findViewById(R.id.txt_bottom_reports);

        liner_bottom_view = (RelativeLayout) rootView.findViewById(R.id.liner_bottom_view);
        txt_med = (TextView) rootView.findViewById(R.id.txt_med);
        txt_doctor_visit = (TextView) rootView.findViewById(R.id.txt_doctor_visit);
        txt_lab_test = (TextView) rootView.findViewById(R.id.txt_lab_test);

        img_medicine = (ImageView) rootView.findViewById(R.id.img_medicine);
        img_doctor_visit = (ImageView) rootView.findViewById(R.id.img_doctor_visit);
        img_lab_test = (ImageView) rootView.findViewById(R.id.img_lab_test);

        liner_medincine = (LinearLayout) rootView.findViewById(R.id.liner_medincine);
        liner_doctor_visit = (LinearLayout) rootView.findViewById(R.id.liner_doctor_visit);
        liner_lab_test = (LinearLayout) rootView.findViewById(R.id.liner_lab_test);
        liner_medincine.setOnClickListener(this);
        liner_doctor_visit.setOnClickListener(this);
        liner_lab_test.setOnClickListener(this);

        txt_bottom_heath_app.setOnClickListener(this);
        txt_bottom_health_note.setOnClickListener(this);
        txt_bottom_home.setOnClickListener(this);
        txt_bottom_prescription.setOnClickListener(this);
        txt_bottom_reports.setOnClickListener(this);

        CureFull.getInstanse().setiGlobalTopBarButtonVisible(FragmentHomeScreenAll.this);
        CureFull.getInstanse().setiGlobalIsbackButtonVisible(FragmentHomeScreenAll.this);
        CureFull.getInstanse().getFlowInstanseAll().clearBackStack();
        CureFull.getInstanse().getFlowInstanseAll()
                .replace(new FragmentLandingPage(), false);


        return rootView;
    }

    @Override
    public void isTobBarButtonVisible(boolean isback) {
        if (isback) {
            top_view.setVisibility(View.GONE);
        } else {
            liner_medincine.setBackgroundResource(R.drawable.button_mendinic_click);
            liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
            liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
            img_medicine.setImageResource(R.drawable.medicine_icon_red);
            txt_med.setTextColor(getResources().getColor(R.color.health_red_drawer));
            img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
            txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
            img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
            txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
            top_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void isbackButtonVisible(boolean isback, String check) {
        if (isback) {
            liner_bottom_view.setVisibility(View.GONE);
        } else {
            if (check.equalsIgnoreCase("Prescription")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_red);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            } else if (check.equalsIgnoreCase("Lab Reports")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendinic_click);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_red);
            } else if (check.equalsIgnoreCase("Health App")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_red);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            } else if (check.equalsIgnoreCase("Note")) {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendinic_click);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_red);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            }else {
                txt_bottom_heath_app.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_health_note.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_prescription.setBackgroundResource(R.drawable.button_mendicine_unclick);
                txt_bottom_reports.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_health_app.setImageResource(R.drawable.footer_healthapp_yellow);
                img_health_note.setImageResource(R.drawable.footer_healthnote_yellow);
                img_health_pre.setImageResource(R.drawable.footer_ehr_yellow);
                img_health_report.setImageResource(R.drawable.footer_report_yellow);
            }
            liner_bottom_view.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {


            case R.id.liner_medincine:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_medicine);
                liner_medincine.setBackgroundResource(R.drawable.button_mendinic_click);
                liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
                liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_medicine.setImageResource(R.drawable.medicine_icon_red);
                txt_med.setTextColor(getResources().getColor(R.color.health_red_drawer));
                img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
                txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
                img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
                txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentReminderMedicine(), false);
                break;
            case R.id.liner_doctor_visit:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_doctor_visit);
                liner_medincine.setBackgroundResource(R.drawable.button_mendicine_unclick);
                liner_doctor_visit.setBackgroundResource(R.drawable.button_mendinic_click);
                liner_lab_test.setBackgroundResource(R.drawable.button_mendicine_unclick);
                img_medicine.setImageResource(R.drawable.medicine_icon_yellow);
                txt_med.setTextColor(getResources().getColor(R.color.health_yellow));
                img_doctor_visit.setImageResource(R.drawable.doctor_icon_red);
                txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_red_drawer));
                img_lab_test.setImageResource(R.drawable.lab_icon_yellow);
                txt_lab_test.setTextColor(getResources().getColor(R.color.health_yellow));
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentReminderDoctorVisit(), false);
                break;
            case R.id.liner_lab_test:
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_lab_test);
                liner_medincine.setBackgroundResource(R.drawable.button_mendicine_unclick);
                liner_doctor_visit.setBackgroundResource(R.drawable.button_mendicine_unclick);
                liner_lab_test.setBackgroundResource(R.drawable.button_mendinic_click);
                img_medicine.setImageResource(R.drawable.medicine_icon_yellow);
                txt_med.setTextColor(getResources().getColor(R.color.health_yellow));
                img_doctor_visit.setImageResource(R.drawable.doctor_icon_yellow);
                txt_doctor_visit.setTextColor(getResources().getColor(R.color.health_yellow));
                img_lab_test.setImageResource(R.drawable.lab_icon_red);
                txt_lab_test.setTextColor(getResources().getColor(R.color.health_red_drawer));
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentReminderLabTest(), false);
                break;
            case R.id.txt_bottom_heath_app:
                if (!AppPreference.getInstance().isFragmentHealthApp()) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_app);
                    if (AppPreference.getInstance().isEditGoal()) {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentHealthAppNewProgress(), false);
                    } else {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentEditGoal(), false);

                    }
                }

                break;
            case R.id.txt_bottom_health_note:
                if (!AppPreference.getInstance().isFragmentHealthNote()) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_note);
                    if (AppPreference.getInstance().isEditGoalPage()) {
                        if (AppPreference.getInstance().isEditGoal()) {
                            CureFull.getInstanse().getFlowInstanseAll()
                                    .replace(new FragmentHealthNote(), false);
                        }
                    } else {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentHealthNote(), false);
                    }
                }

                break;
            case R.id.txt_bottom_home:
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLandingPage(), false);
                break;
            case R.id.txt_bottom_prescription:
                if (!AppPreference.getInstance().isFragmentHealtpre()) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_pre);
                    if (AppPreference.getInstance().isEditGoalPage()) {
                        if (AppPreference.getInstance().isEditGoal()) {
                            CureFull.getInstanse().getFlowInstanseAll()
                                    .replace(new FragmentPrescriptionCheckNew(), false);
                        }
                    } else {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentPrescriptionCheckNew(), false);
                    }
                }


                break;
            case R.id.txt_bottom_reports:
                if (!AppPreference.getInstance().isFragmentHealtReprts()) {
                    CureFull.getInstanse().getActivityIsntanse().iconAnim(img_health_report);
                    if (AppPreference.getInstance().isEditGoalPage()) {
                        if (AppPreference.getInstance().isEditGoal()) {
                            CureFull.getInstanse().getFlowInstanseAll()
                                    .replace(new FragmentLabTestReport(), false);
                        }
                    } else {
                        CureFull.getInstanse().getFlowInstanseAll()
                                .replace(new FragmentLabTestReport(), false);
                    }
                }

                break;

        }

    }


}