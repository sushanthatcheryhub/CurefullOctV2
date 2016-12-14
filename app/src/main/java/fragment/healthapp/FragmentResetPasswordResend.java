package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Random;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import interfaces.SmsListener;
import utils.IncomingSms;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentResetPasswordResend extends Fragment {


    private View rootView;
    private TextView btn_next, btn_resend, txt_top, txt_below;
    private RequestQueue requestQueue;
    private String mob_number;
    private LinearLayout liner_resend, liner_next;
    private String checkEmail;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_reset_password_check,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().disableDrawer();
        CureFull.getInstanse().getActivityIsntanse().showLogo(true);
        txt_top = (TextView) rootView.findViewById(R.id.txt_top);
        txt_below = (TextView) rootView.findViewById(R.id.txt_below);
        btn_next = (TextView) rootView.findViewById(R.id.btn_next);
        btn_resend = (TextView) rootView.findViewById(R.id.btn_resend);
        liner_resend = (LinearLayout) rootView.findViewById(R.id.liner_resend);
        liner_next = (LinearLayout) rootView.findViewById(R.id.liner_next);

        Bundle bundle = getArguments();
        if (bundle != null) {
            mob_number = bundle.getString("mobile");
            txt_top.setText("A otp has been sent to your mobile number. please check your mobile for reset your password");
            txt_below.setText("If you have not received any otp. please press resend button to resend the otp.");

//            if (checkEmail.equalsIgnoreCase("email")) {
//                txt_top.setText("A link has been sent to your Email Id. please check your email for reset your password");
//                txt_below.setText("If you have not received any email. please press resend button to resend the link.");
//            }
        }

        IncomingSms.bindListener(new SmsListener() {
            @Override
            public void messageReceived(String messageText) {
                liner_resend.setVisibility(View.GONE);
                liner_next.setVisibility(View.VISIBLE);
//                edt_otp_password.setText(messageText.replace("OTP_IS", ""));
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putString("email","no");
                CureFull.getInstanse().getFlowInstanse()
                        .addWithBottomTopAnimation(new FragmentResetNewPassword(), bundle, true);
            }
        });


        btn_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendOTPService();
            }
        });


        return rootView;
    }

    private void sendOTPService() {
        Random rnd = new Random();
        final int n = 100000 + rnd.nextInt(900000);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.OTP_WEB_SERVICE + mob_number + MyConstants.WebUrls.OTP_MESSAGE + "CureFull" + n + MyConstants.WebUrls.OTP_LAST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("getSymptomsList, URL 1.", response);
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        CureFull.getInstanse().getFlowInstanse()
//                                .addWithBottomTopAnimation(new FragmentResetPasswordResend(), null, true);
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
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }
}