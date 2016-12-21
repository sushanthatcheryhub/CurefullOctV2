package adpter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.CardView;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentPrescriptionImageFullView;
import fragment.healthapp.FragmentPrescriptionImageView;
import interfaces.IOnOtpDoneDelete;
import item.property.PrescriptionImageListView;
import item.property.PrescriptionListView;
import utils.AppPreference;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class PrescriptionImageViewAdpter extends RecyclerView.Adapter<PrescriptionImageViewAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<PrescriptionImageListView> prescriptionListViews;
    private RequestQueue requestQueue;
    private String doctorName, id, dieaseName, dates;


    public PrescriptionImageViewAdpter(Context applicationContexts,
                                       List<PrescriptionImageListView> prescriptionListViews, String doctorName, String id, String dieaseName, String date) {
        this.prescriptionListViews = prescriptionListViews;
        this.applicationContext = applicationContexts;
        this.doctorName = doctorName;
        this.id = id;
        this.dieaseName = dieaseName;
        this.dates = date;

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
        ImageView img_delete = holder.img_delete;
        ImageView image_item = holder.image_item;
        ImageView img_share = holder.img_share;
        CardView card_view = holder.card_view;

        try {
            CureFull.getInstanse().getFullImageLoader().startLazyLoading(MyConstants.WebUrls.PRECRIPTION_IMAGE_PATH + prescriptionListViews.get(position).getPrescriptionImage(), image_item);
        } catch (Exception e) {

        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected prescription ?", "Prescription", position);
                dialogDeleteAll.setiOnOtpDoneDelete(PrescriptionImageViewAdpter.this);
                dialogDeleteAll.show();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shareClick(prescriptionListViews.get(position).getPrescriptionImage());
            }
        });


        card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("prescriptionId", id);
                bundle.putString("iPrescriptionId", prescriptionListViews.get(position).getiPrescriptionId());
                bundle.putString("doctorName", doctorName);
                bundle.putString("dieaseName", dieaseName);
                bundle.putString("date", dates);
                bundle.putString("imageList", prescriptionListViews.get(position).getPrescriptionImage());
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentPrescriptionImageFullView(), bundle, true);
            }
        });


    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getPrescriptionDelete(id, prescriptionListViews.get(pos).getiPrescriptionId(), doctorName, pos);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView img_delete, image_item, img_share;
        public CardView card_view;

        ItemViewHolder(View view) {
            super(view);
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
        Log.e("delete", ":- " + id + " name:- " + name + "pos :- " + pos);
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



    public void shareClick(String prescriptionImage) {
        String url = MyConstants.WebUrls.PRECRIPTION_IMAGE_PATH+ prescriptionImage;
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri imageUri = Uri.parse(url);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, doctorName + " Report");
        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + doctorName + "\n" + "Mobile No:- 9654052212" + "\n" + "Email Id:- sushant@gmail.com" + "\n" + "Note : Normal Hai");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imageUri);
        sharingIntent.setType("image/*");
        applicationContext.startActivity(sharingIntent);
    }
}