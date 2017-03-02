package adpter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentPrescriptionImageFullView;
import fragment.healthapp.FragmentPrescriptionImageView;
import interfaces.IOnOtpDoneDelete;
import item.property.PrescriptionImageFollowUpListView;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UploadFollowUpPrescriptionAdpter extends RecyclerView.Adapter<UploadFollowUpPrescriptionAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<PrescriptionImageFollowUpListView> prescriptionListViews;
    private RequestQueue requestQueue;
    private Uri uri;
    ArrayList<Uri> files = null;
    int size = 1;
    int pos;
    private String doctorName, id, dieaseName, dates, uploadedBys;

    public UploadFollowUpPrescriptionAdpter(Context applicationContexts,
                                            List<PrescriptionImageFollowUpListView> prescriptionListViews, String doctorName, String id, String dieaseName, String date, String uploadedBy) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_presriction, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_date = holder.txt_date;
        TextView text_doctor_name = holder.text_doctor_name;
        final ImageView img_delete = holder.img_delete;
        final ImageView image_item = holder.image_item;
        final ImageView img_share = holder.img_share;
        final ProgressBar progressBar = holder.progress_bar_one;
        TextView txt_count_file = holder.txt_count_file;
        RelativeLayout relative_card_view = holder.relative_card_view;

        if (uploadedBys.equalsIgnoreCase("curefull")) {
            img_delete.setVisibility(View.GONE);
        } else {
            img_delete.setVisibility(View.VISIBLE);
        }
        final String date = prescriptionListViews.get(position).getPrescriptionDate();
        if (!date.equalsIgnoreCase("")) {
            String[] dateFormat = date.split("-");
            int mYear = Integer.parseInt(dateFormat[0]);
            int mMonth = Integer.parseInt(dateFormat[1]);
            int mDay = Integer.parseInt(dateFormat[2]);
            try {
                String completeDate = mDay + " " + Utils.formatMonth(String.valueOf(mMonth)) + "," + mYear;
                txt_date.setText("" + completeDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        txt_count_file.setText(prescriptionListViews.get(position).getCountOfFiles());
        text_doctor_name.setText("" + doctorName);
        if (prescriptionListViews.get(position).getPrescriptionImageListViews() != null && prescriptionListViews.get(position).getPrescriptionImageListViews().size() > 0) {
            Collections.sort(prescriptionListViews.get(position).getPrescriptionImageListViews());
            Glide.with(applicationContext).load(prescriptionListViews.get(position).getPrescriptionImageListViews().get(0).getPrescriptionImage())
                    .thumbnail(0.1f)
                    .crossFade()
                    .override((int) applicationContext.getResources().getDimension(R.dimen._140dp), (int) applicationContext.getResources().getDimension(R.dimen._140dp))
                    .priority(Priority.HIGH)
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .listener(new RequestListener<String, GlideDrawable>() {
                        @Override
                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .into(image_item);
//            try {
//                CureFull.getInstanse().getSmallImageLoader().startLazyLoading(prescriptionListViews.get(position).getPrescriptionImageListViews().get(0).getPrescriptionImage(), image_item);
//            } catch (Exception e) {
//
//            }
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                ElasticAction.doAction(img_delete, 400, 0.9f, 0.9f);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected prescription ?", "Prescription", position);
                dialogDeleteAll.setiOnOtpDoneDelete(UploadFollowUpPrescriptionAdpter.this);
                dialogDeleteAll.show();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                size = 1;
                if (prescriptionListViews.get(position).getPrescriptionImageListViews().size() > 0) {
                    files = new ArrayList<Uri>();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_share, 400, 0.9f, 0.9f);
                    pos = position;
                    for (int i = 0; i < prescriptionListViews.get(position).getPrescriptionImageListViews().size(); i++) {
                        new LongOperation().execute(prescriptionListViews.get(position).getPrescriptionImageListViews().get(i).getPrescriptionImage());
                    }
                }


//                shareClick(prescriptionListViews.get(position).getPrescriptionImageListViews());
            }
        });


        relative_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (prescriptionListViews.get(position).getPrescriptionImageListViews().size() > 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("doctorName", doctorName);
                    bundle.putString("date", date);
                    bundle.putString("id", id);
                    bundle.putString("uploadedBy", uploadedBys);
                    bundle.putString("prescriptionFollowupId", prescriptionListViews.get(position).getPrescriptonImageFollowupId());
                    bundle.putParcelableArrayList("imageList", prescriptionListViews.get(position).getPrescriptionImageListViews());
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentPrescriptionImageView(), bundle, true);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("prescriptionId", id);
                    bundle.putString("prescriptionFollowupId", prescriptionListViews.get(position).getPrescriptonImageFollowupId());
                    bundle.putString("prescriptionPartId", prescriptionListViews.get(position).getPrescriptionImageListViews().get(0).getPrescriptionImagePartId());
                    bundle.putString("doctorName", doctorName);
                    bundle.putString("date", date);
                    bundle.putString("uploadedBy", uploadedBys);
                    bundle.putString("imageList", prescriptionListViews.get(position).getPrescriptionImageListViews().get(0).getPrescriptionImage());
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentPrescriptionImageFullView(), bundle, true);
                }

            }
        });


        text_doctor_name.setSelected(true);

    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getPrescriptionDelete(id, doctorName, pos);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date, text_doctor_name, txt_count_file;
        public ImageView img_delete, image_item, img_share;
        public RelativeLayout relative_card_view;
        public ProgressBar progress_bar_one;

        ItemViewHolder(View view) {
            super(view);
            this.txt_count_file = (TextView) itemView
                    .findViewById(R.id.txt_count_file);
            this.txt_date = (TextView) itemView
                    .findViewById(R.id.txt_date);
            this.text_doctor_name = (TextView) itemView
                    .findViewById(R.id.text_doctor_name);
            this.img_delete = (ImageView) itemView
                    .findViewById(R.id.img_delete);
            this.image_item = (ImageView) itemView
                    .findViewById(R.id.image_item);
            this.img_share = (ImageView) itemView
                    .findViewById(R.id.img_share);
            this.progress_bar_one = (ProgressBar) itemView.findViewById(R.id.progress_bar_one);
            this.relative_card_view = (RelativeLayout) itemView.findViewById(R.id.relative_card_view);
        }
    }

    private void getPrescriptionDelete(String id, String name, final int pos) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse());
        }
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DELETE_PRESCRIPTION + id + "&doctor_name=" + name.replace(" ", "%20"),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);

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
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


    private void prepareShareIntent(ArrayList<Uri> files) {
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName() + " Lab Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        applicationContext.startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));
    }

    public static Bitmap getBitmapFromURL(String src) {
        try {
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            return myBitmap;
        } catch (Throwable ex) {
            ex.printStackTrace();
            return null;
        }
    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".png");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 50, out);
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


    private class LongOperation extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            try {
                URL url = new URL(params[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Throwable ex) {
                ex.printStackTrace();
                return null;
            }

        }

        @Override
        protected void onPostExecute(Bitmap result) {
            uri = getLocalBitmapUri(result);
            files.add(uri);
            if (size == prescriptionListViews.get(pos).getPrescriptionImageListViews().size()) {
                prepareShareIntent(files);
            }
            size += 1;
        }
    }
}