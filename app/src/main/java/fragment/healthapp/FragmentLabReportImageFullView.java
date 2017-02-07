package fragment.healthapp;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import adpter.UploadLabTestReportAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import image.zoom.GestureImageView;
import interfaces.IOnOtpDoneDelete;
import utils.AppPreference;
import utils.MyConstants;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentLabReportImageFullView extends Fragment implements IOnOtpDoneDelete {


    private View rootView;
    private TextView txt_doctor_name, txt_diease_name, txt_date;
    private ImageView img_delete, img_share;
    private GestureImageView image_item;
    private String doctoreName, prescriptionId, iPrescriptionId, date, uploadedBy;
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
        image_item = (GestureImageView) rootView.findViewById(R.id.image_item);
        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            date = bundle.getString("date");
            txt_date.setText("" + bundle.getString("date"));
            doctoreName = bundle.getString("doctorName");
            txt_doctor_name.setText("" + doctoreName);
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionId = bundle.getString("prescriptionId");
            iPrescriptionId = bundle.getString("iPrescriptionId");
            uploadedBy = bundle.getString("uploadedBy");
            if (uploadedBy.equalsIgnoreCase("curefull")) {
                img_delete.setVisibility(View.INVISIBLE);
            } else {
                img_delete.setVisibility(View.VISIBLE);
            }
            images = bundle.getString("imageList");
//            Glide.with(this).load(images)
//                    .crossFade()
//                    .diskCacheStrategy(DiskCacheStrategy.ALL)
//                    .into(image_item);

            Glide.with(CureFull.getInstanse().getActivityIsntanse()).load(images).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL)
                    .dontAnimate().into(new BitmapImageViewTarget(image_item) {
                @Override
                public void onResourceReady(final Bitmap bmp, GlideAnimation anim) {
                    image_item.setImageBitmap(bmp);
                    img_share.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            CureFull.getInstanse().getActivityIsntanse().iconAnim(img_share);
                            prepareShareIntent(bmp);
                        }
                    });
                }

                @Override
                public void onLoadFailed(Exception e, Drawable errorDrawable) {
                    super.onLoadFailed(e, errorDrawable);
                }
            });
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_delete);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Test Report ?", "Test Report", 0);
                dialogDeleteAll.setiOnOtpDoneDelete(FragmentLabReportImageFullView.this);
                dialogDeleteAll.show();

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


    private void prepareShareIntent(Bitmap bmp) {
        Uri bmpUri = getLocalBitmapUri(bmp); // see previous remote images section
        // Construct share intent as described above based on bitmap
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName() + " Lab Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));

    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(CureFull.getInstanse().getActivityIsntanse().getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            bmpUri = Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getPrescriptionDelete(prescriptionId, iPrescriptionId, doctoreName);
        }
    }
}