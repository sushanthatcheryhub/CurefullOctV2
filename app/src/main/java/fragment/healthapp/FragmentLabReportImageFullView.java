package fragment.healthapp;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import utils.AppPreference;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLabReportImageFullView extends Fragment {


    private View rootView;
    private TextView txt_doctor_name, txt_diease_name, txt_date;
    private ImageView image_item, img_delete, img_share;
    private String doctoreName, prescriptionId, iPrescriptionId,date;
    private RequestQueue requestQueue;
    private String images;

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
        img_share = (ImageView) rootView.findViewById(R.id.img_share);
        image_item = (ImageView) rootView.findViewById(R.id.image_item);
        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            date=bundle.getString("date");
            txt_date.setText("" + bundle.getString("date"));
            doctoreName = bundle.getString("doctorName");
            txt_doctor_name.setText("" + doctoreName);
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionId = bundle.getString("prescriptionId");
            iPrescriptionId = bundle.getString("iPrescriptionId");
            images = bundle.getString("imageList");
            try {
                CureFull.getInstanse().getFullImageLoader().startLazyLoading(MyConstants.WebUrls.HOST_IP + "/CurefullWeb-0.0.1/resources/images/labReport/" + images, image_item);
            } catch (Exception e) {

            }
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getPrescriptionDelete(prescriptionId, iPrescriptionId, doctoreName);
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareClick(images);
            }
        });
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }




    private void getPrescriptionDelete(String id, String realId, String name) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DELETE_SUB_LAB_REPORT + id + "&iReportId=" + realId + "&doctor_name=" + name,
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    public void shareClick(String prescriptionImage) {
        String url = MyConstants.WebUrls.HOST_IP + "/CurefullWeb-0.0.1/resources/images/labReport/" + prescriptionImage;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri imageUri = Uri.parse(url);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, doctoreName + " Report " +date);
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:-"+ doctoreName + "\n" + "Mobile No:- 9654052212" + "\n" + "Email Id:- sushant@gmail.com" + "\n" + "Note : Normal Hai");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharingIntent.setType("image/*");
        CureFull.getInstanse().getActivityIsntanse().startActivity(sharingIntent);
    }

}