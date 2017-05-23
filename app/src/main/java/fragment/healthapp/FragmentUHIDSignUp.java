package fragment.healthapp;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.List;
import java.util.Random;

import ElasticVIews.ElasticAction;
import adpter.UHID_Sign_ListAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.UHIDItemsCheck;
import utils.AppPreference;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentUHIDSignUp extends AppCompatActivity implements View.OnClickListener {


    //private View rootView;
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView_notes;
    private UHID_Sign_ListAdpter uhid_listAdpter;
    private TextView btn_contiune, btn_skip, txt_we;
    private List<UHIDItemsCheck> uhidItemsChecks;
    private String health_name, health_email, health_mobile;
    private String realUHID = "";
    private String blankName = "";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_sign_uhid);
        CureFull.getInstanse().getActivityIsntanse().selectedNav(2);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);
        btn_contiune = (TextView)findViewById(R.id.btn_contiune);
        btn_skip = (TextView)findViewById(R.id.btn_skip);
        txt_we = (TextView)findViewById(R.id.txt_we);
        recyclerView_notes = (RecyclerView)findViewById(R.id.recyclerView_notes);
        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.coordinatorLayout);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView_notes.setLayoutManager(mLayoutManager);

        btn_contiune.setOnClickListener(this);
        btn_skip.setOnClickListener(this);

        //Bundle bundle = getArguments();
        if (getIntent().getExtras() != null) {
            blankName = getIntent().getExtras().getString("NAME");
            health_name = getIntent().getExtras().getString("NAME");
            health_email = getIntent().getExtras().getString("EMAIL");
            health_mobile = getIntent().getExtras().getString("MOBILE");
            uhidItemsChecks = getIntent().getExtras().getParcelableArrayList("UHID");
            txt_we.setText("We have found matching records with " + health_mobile + ".Please select if records related to you otherwise skip.");
            showAdpter();
        }
        AppPreference.getInstance().setcf_uuhidSignUp("");


    }


    public void showAdpter() {
        if (uhidItemsChecks != null && uhidItemsChecks.size() > 0) {
            recyclerView_notes.setVisibility(View.VISIBLE);
            uhid_listAdpter = new UHID_Sign_ListAdpter(FragmentUHIDSignUp.this, CureFull.getInstanse().getActivityIsntanse(), uhidItemsChecks);
            recyclerView_notes.setAdapter(uhid_listAdpter);
            uhid_listAdpter.notifyDataSetChanged();
        }
    }

    public void checkedUHID(String getId, String name) {
        realUHID = getId;
        health_name = name;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_contiune:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                if (realUHID.equalsIgnoreCase("")) {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(coordinatorLayout, "Please select UHID to Continue");
                } else {
                    btn_contiune.setEnabled(false);
                    btn_skip.setEnabled(false);
                    sendOTPService(realUHID);
                }

                break;
            case R.id.btn_skip:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(view, 400, 0.9f, 0.9f);
                btn_contiune.setEnabled(false);
                btn_skip.setEnabled(false);
                sendOTPService("");
                break;
        }

    }

    private void sendOTPService(final String realUHID) {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + health_mobile + MyConstants.WebUrls.OTP_MESSAGE + "Dear%20User%20,%0AYour%20verification%20code%20is%20" + String.valueOf(n) + "%0AThanx%20for%20using%20Curefull.%20Stay%20Relief." + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        btn_contiune.setEnabled(true);
                        btn_skip.setEnabled(true);
//                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        /*Bundle bundle = new Bundle();
                        if (realUHID.equalsIgnoreCase("")) {
                            bundle.putString("NAME", blankName);
                        } else {
                            bundle.putString("NAME", health_name);
                        }

                        bundle.putString("EMAIL", health_email);
                        bundle.putString("MOBILE", health_mobile);
                        bundle.putInt("otp", n);
                        bundle.putString("UHID", realUHID);*/

                        Intent intent_UHIDSignUP=new Intent(FragmentUHIDSignUp.this,FragmentOTPCheck.class);
                        if (realUHID.equalsIgnoreCase("")) {
                            intent_UHIDSignUP.putExtra("NAME", blankName);
                        }else{
                            intent_UHIDSignUP.putExtra("NAME", health_name);
                        }
                        intent_UHIDSignUP.putExtra("EMAIL", health_email);
                        intent_UHIDSignUP.putExtra("MOBILE", health_mobile);
                        intent_UHIDSignUP.putExtra("otp", n);
                        intent_UHIDSignUP.putExtra("UHID", realUHID);
                        startActivity(intent_UHIDSignUP);

                       /* CureFull.getInstanse().getFlowInstanse()
                                .addWithBottomTopAnimation(new FragmentOTPCheck(), bundle, true);*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        btn_contiune.setEnabled(true);
                        btn_skip.setEnabled(true);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

                        error.printStackTrace();
                    }
                }
        ) {
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


}
