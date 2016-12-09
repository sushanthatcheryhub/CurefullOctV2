package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.PrescriptionImageViewAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import item.property.PrescriptionImageListView;
import utils.AppPreference;
import utils.MyConstants;
import utils.SpacesItemDecoration;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentPrescriptionImageFullView extends Fragment {


    private View rootView;
    private TextView txt_doctor_name, txt_diease_name, txt_date;
    private ImageView image_item, img_delete;
    private String doctoreName, prescriptionId, iPrescriptionId;
    private RequestQueue requestQueue;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_presciption_image_full_view,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        txt_doctor_name = (TextView) rootView.findViewById(R.id.txt_doctor_name);
        txt_diease_name = (TextView) rootView.findViewById(R.id.txt_diease_name);
        txt_date = (TextView) rootView.findViewById(R.id.txt_date);
        image_item = (ImageView) rootView.findViewById(R.id.image_item);
        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            txt_date.setText("" + bundle.getString("date"));
            doctoreName = bundle.getString("doctorName");
            txt_doctor_name.setText("" + doctoreName);
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionId = bundle.getString("prescriptionId");
            iPrescriptionId = bundle.getString("iPrescriptionId");

            try {
                CureFull.getInstanse().getFullImageLoader().startLazyLoading(MyConstants.WebUrls.HOST_IP + "/CurefullWeb-0.0.1/resources/images/uhid/prescriptionimages/" + bundle.getString("imageList"), image_item);
            } catch (Exception e) {

            }
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrescriptionDelete(prescriptionId, iPrescriptionId, doctoreName);
            }
        });

        return rootView;
    }


    public String formatMonth(String month) throws ParseException {

        try {
            SimpleDateFormat monthParse = new SimpleDateFormat("MM");
            SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
            return monthDisplay.format(monthParse.parse(month));
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return "";
    }

    private void getPrescriptionDelete(String id, String realId, String name) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DELETE_SUB_PRESCRIPTION + id + "&iPrescriptionId=" + realId + "&doctor_name=" + name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
                        Log.e("Doctor List, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            CureFull.getInstanse().getActivityIsntanse().onBackPressed();
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

}