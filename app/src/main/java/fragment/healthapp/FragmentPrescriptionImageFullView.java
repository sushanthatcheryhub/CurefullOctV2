package fragment.healthapp;


import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
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
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import adpter.PrescriptionImageViewAdpter;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import image.zoom.GestureImageView;
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
    private ImageView img_delete, img_share;
    private GestureImageView gestureImageView;
    private String doctoreName, prescriptionId, iPrescriptionId, date;
    private RequestQueue requestQueue;
    private Bundle bundle;
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
        gestureImageView = (GestureImageView) rootView.findViewById(R.id.image_item);
        img_delete = (ImageView) rootView.findViewById(R.id.img_delete);
        img_share = (ImageView) rootView.findViewById(R.id.img_share);
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(true);
        CureFull.getInstanse().getActivityIsntanse().showUpButton(true);

        bundle = getArguments();
        if (bundle != null) {
            date = bundle.getString("date");
            txt_date.setText("" + date);
            doctoreName = bundle.getString("doctorName");
            txt_doctor_name.setText("" + doctoreName);
            txt_diease_name.setText("" + bundle.getString("dieaseName"));
            prescriptionId = bundle.getString("prescriptionId");
            iPrescriptionId = bundle.getString("iPrescriptionId");
            images = bundle.getString("imageList");
            Glide.with(this).load(images)
                    .thumbnail(0.5f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(gestureImageView);
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
                            AppPreference.getInstance().setDelete(true);
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


        Uri bmpUri = getLocalBitmapUri(gestureImageView);
        if (bmpUri != null) {
            // Construct a ShareIntent with link to image
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/*");
            // Launch sharing dialog for image
            startActivity(Intent.createChooser(shareIntent, "Share Image"));
        } else {
            // ...sharing failed, handle error
        }


//        String url = MyConstants.WebUrls.PRECRIPTION_IMAGE_PATH + prescriptionImage;
//        Log.e("url ", "url " + url);
//        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
//        Uri imageUri = Uri.fromFile(new File(url));
//        Log.e("uri ", "uri  " + imageUri.getPath() + " :- " );
//        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, doctoreName + " Report " + date);
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
//        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//        sharingIntent.setType("image/*");
//        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        startActivity(Intent.createChooser(sharingIntent, "Share images..."));


//        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
//        shareIntent.setType("text/plain");
//        shareIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, doctoreName + " Report " + date);
//        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
//        shareIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
//
//        PackageManager pm = getActivity().getPackageManager();
//        List<ResolveInfo> activityList = pm.queryIntentActivities(shareIntent, 0);
//        for (final ResolveInfo app : activityList)
//        {
//            if ((app.activityInfo.name).contains("android.gm"))
//            {
//                final ActivityInfo activity = app.activityInfo;
//                final ComponentName name = new ComponentName(activity.applicationInfo.packageName, activity.name);
//                shareIntent.addCategory(Intent.CATEGORY_LAUNCHER);
//                shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
//                shareIntent.setComponent(name);
//                startActivity(shareIntent);
//                break;
//            }
//        }


    }


    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }


}