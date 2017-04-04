package adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentLabReportImageFullView;
import interfaces.IOnOtpDoneDelete;
import item.property.LabReportImageListView;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class LabReportImageViewAdpter extends RecyclerView.Adapter<LabReportImageViewAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<LabReportImageListView> prescriptionListViews;
    private String doctorName, id, dieaseName, dates, uploadedBys;


    public LabReportImageViewAdpter(Context applicationContexts,
                                    List<LabReportImageListView> prescriptionListViews, String doctorName, String id, String dieaseName, String date, String uploadedBy) {
        this.prescriptionListViews = prescriptionListViews;
        this.applicationContext = applicationContexts;
        this.doctorName = doctorName;
        this.id = id;
        this.dieaseName = dieaseName;
        this.dates = date;
        this.uploadedBys = uploadedBy;

    }

    @Override
    public int getItemCount() {
        return (null != prescriptionListViews ? prescriptionListViews.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_presriction_image_view, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final ImageView img_delete = holder.img_delete;
        final ImageView image_item = holder.image_item;
        final ImageView img_share = holder.img_share;
        CardView card_view = holder.card_view;
        final ProgressBar progressBar = holder.progress_bar_one;

//        Glide.with(applicationContext).load(prescriptionListViews.get(position).getReportImage())
//                .thumbnail(0.5f)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(image_item);

        Glide.with(applicationContext).load(prescriptionListViews.get(position).getReportImage()).asBitmap().priority(Priority.HIGH)
                .diskCacheStrategy(DiskCacheStrategy.ALL).override((int) applicationContext.getResources().getDimension(R.dimen._140dp), (int) applicationContext.getResources().getDimension(R.dimen._140dp))
                .dontAnimate()
                .listener(new RequestListener<String, Bitmap>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, String model, Target<Bitmap> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(new BitmapImageViewTarget(image_item) {
                    @Override
                    public void onResourceReady(final Bitmap bmp, GlideAnimation anim) {
                        image_item.setImageBitmap(bmp);
                        img_share.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                                    ElasticAction.doAction(img_share, 400, 0.9f, 0.9f);
                                prepareShareIntent(bmp);
                            }
                        });
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        super.onLoadFailed(e, errorDrawable);
                    }
                });

        if (uploadedBys.equalsIgnoreCase("curefull")) {
            img_delete.setVisibility(View.INVISIBLE);
        } else {
            img_delete.setVisibility(View.VISIBLE);
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_delete, 400, 0.9f, 0.9f);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Lab Report ?", "Lab Report", position);
                dialogDeleteAll.setiOnOtpDoneDelete(LabReportImageViewAdpter.this);
                dialogDeleteAll.show();
            }
        });
//        img_share.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_share);
//                shareClick(prescriptionListViews.get(position).getReportImage(), doctorName);
//            }
//        });


        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("prescriptionId", id);
                bundle.putString("iPrescriptionId", prescriptionListViews.get(position).getReportImageId());
                bundle.putString("doctorName", doctorName);
                bundle.putString("dieaseName", dieaseName);
                bundle.putString("date", dates);
                bundle.putString("uploadedBy", uploadedBys);
                bundle.putString("imageList", prescriptionListViews.get(position).getReportImage());
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentLabReportImageFullView(), bundle, true);
            }
        });


    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getPrescriptionDelete(id, prescriptionListViews.get(pos).getReportImageId(), doctorName, pos);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_delete, image_item, img_share;
        public CardView card_view;
        public ProgressBar progress_bar_one;

        ItemViewHolder(View view) {
            super(view);
            this.progress_bar_one = (ProgressBar) itemView.findViewById(R.id.progress_bar_one);
            this.img_delete = (ImageView) itemView
                    .findViewById(R.id.img_delete);
            this.image_item = (ImageView) itemView
                    .findViewById(R.id.image_item);
            this.img_share = (ImageView) itemView
                    .findViewById(R.id.img_share);
            this.card_view = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    private void getPrescriptionDelete(String id, String realId, String name, final int pos) {
//        Log.e("delete", ":- " + id + " name:- " + name + "pos :- " + pos);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DELETE_SUB_LAB_REPORT + id + "&iReportId=" + realId + "&doctor_name=" + name.replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
//                        Log.e("Doctor List, URL 1.", response);

                        int responseStatus = 0;
                        JSONObject json = null;
                        try {
                            json = new JSONObject(response.toString());
                            responseStatus = json.getInt("responseStatus");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if (responseStatus == MyConstants.IResponseCode.RESPONSE_SUCCESS) {
                            prescriptionListViews.remove(pos);
                            notifyDataSetChanged();
                            if (prescriptionListViews.size() == 0) {
                                CureFull.getInstanse().getActivityIsntanse().onBackPressed();
                            }
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
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName() + " Lab Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        applicationContext.startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));
    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
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
}