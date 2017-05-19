package adpter;

import android.content.ContentValues;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;
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
import fragment.healthapp.FragmentLabReportImageFullView;
import fragment.healthapp.FragmentLabReportImageView;
import fragment.healthapp.FragmentLabTestReport;
import interfaces.IOnOtpDoneDelete;
import item.property.LabReportListView;
import operations.DbOperations;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UploadLabTestReportAdpter extends RecyclerView.Adapter<UploadLabTestReportAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<LabReportListView> labReportListViews;
    private FragmentLabTestReport fragmentLabTestReports;
    private Uri uri;
    ArrayList<Uri> files = null;
    int size = 1;
    int pos;
    private View rootView;

    public UploadLabTestReportAdpter(FragmentLabTestReport fragmentLabTestReport, Context applicationContexts,
                                     List<LabReportListView> labReportListViews, View rootView) {
        this.labReportListViews = labReportListViews;
        this.applicationContext = applicationContexts;
        this.fragmentLabTestReports = fragmentLabTestReport;
        this.rootView = rootView;
    }

    @Override
    public int getItemCount() {
        return (null != labReportListViews ? labReportListViews.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_lab_test_report, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_date = holder.txt_date;
        TextView text_doctor_name = holder.text_doctor_name;

        TextView txt_disease_name = holder.txt_disease_name;
        final ImageView img_delete = holder.img_delete;
        ImageView image_item = holder.image_item;
        final ImageView img_share = holder.img_share;
        TextView txt_count_file = holder.txt_count_file;
        RelativeLayout relative_card_view = holder.relative_card_view;
        final ProgressBar progressBar = holder.progress_bar_one;


        if (labReportListViews.get(position).getUploadedBy().equalsIgnoreCase("curefull")) {
            img_delete.setVisibility(View.GONE);
        } else {
            img_delete.setVisibility(View.VISIBLE);
        }

        String date = labReportListViews.get(position).getReportDate();


        if (!date.equalsIgnoreCase("null")) {
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
        txt_count_file.setText(labReportListViews.get(position).getCountOfFiles());
        //txt_date.setText("" + labReportListViews.get(position).getReportDate());
        text_doctor_name.setText("" + labReportListViews.get(position).getDoctorName());
        txt_disease_name.setText("" + labReportListViews.get(position).getTestName());
        if (labReportListViews.get(position).getLabReportImageListViews().size() > 0) {
            Collections.sort(labReportListViews.get(position).getLabReportImageListViews());
            Glide.with(applicationContext).load(labReportListViews.get(position).getLabReportImageListViews().get(0).getReportImage())
                    .thumbnail(0.1f)
                    .crossFade()
                    .override((int) applicationContext.getResources().getDimension(R.dimen._140dp), (int) applicationContext.getResources().getDimension(R.dimen._140dp))
                    .priority(Priority.HIGH)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
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
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(img_delete, 400, 0.9f, 0.9f);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Test Report ?", "Test Report", position);
                dialogDeleteAll.setiOnOtpDoneDelete(UploadLabTestReportAdpter.this);
                dialogDeleteAll.show();
                /*} else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                }*/
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                    CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
                    if (labReportListViews.get(position).getLabReportImageListViews().size() > 0) {
                        files = new ArrayList<Uri>();
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                            ElasticAction.doAction(img_share, 400, 0.9f, 0.9f);
                        pos = position;
                        for (int i = 0; i < labReportListViews.get(position).getLabReportImageListViews().size(); i++) {
                            if (labReportListViews.get(position).getLabReportImageListViews().get(i).getReportImage().contains("https://s3.ap-south-1.amazonaws.com/")) {
                                new LongOperation().execute(labReportListViews.get(position).getLabReportImageListViews().get(i).getReportImage());
                            } else {
                                files.add(Uri.fromFile(new File("" + labReportListViews.get(position).getLabReportImageListViews().get(i).getReportImage())));
                                if (size == labReportListViews.get(pos).getLabReportImageListViews().size()) {
                                    prepareShareIntent(files);
                                }
                                size += 1;
                            }


                        }

                    }
                } else {
                    CureFull.getInstanse().getActivityIsntanse().showSnackbar(rootView, MyConstants.CustomMessages.OFFLINE_MODE);
                }
            }
        });

        relative_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (labReportListViews.get(position).getLabReportImageListViews().size() > 1) {
                    Bundle bundle = new Bundle();
                    bundle.putString("doctorName", labReportListViews.get(position).getDoctorName());
                    bundle.putString("dieaseName", labReportListViews.get(position).getTestName());
                    bundle.putString("date", labReportListViews.get(position).getReportDate());
                    bundle.putString("id", labReportListViews.get(position).getReportId());
                    bundle.putString("uploadedBy", labReportListViews.get(position).getUploadedBy());
                    bundle.putParcelableArrayList("imageList", labReportListViews.get(position).getLabReportImageListViews());
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentLabReportImageView(), bundle, true);
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("prescriptionId", labReportListViews.get(position).getReportId());
                    bundle.putString("iPrescriptionId", labReportListViews.get(position).getLabReportImageListViews().get(0).getReportImageId());
                    bundle.putString("doctorName", labReportListViews.get(position).getDoctorName());
                    bundle.putString("dieaseName", labReportListViews.get(position).getTestName());
                    bundle.putString("uploadedBy", labReportListViews.get(position).getUploadedBy());
                    bundle.putString("date", labReportListViews.get(position).getReportDate());
                    bundle.putString("imageList", labReportListViews.get(position).getLabReportImageListViews().get(0).getReportImage());
                    CureFull.getInstanse().getFlowInstanse()
                            .replace(new FragmentLabReportImageFullView(), bundle, true);
                }


            }
        });

        if (position == labReportListViews.size() - 1) {
            fragmentLabTestReports.callWebServiceAgain(labReportListViews.size());
        }

        txt_disease_name.setSelected(true);
        text_doctor_name.setSelected(true);
    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())) {
                DbOperations.clearLabDataFromLocal(labReportListViews.get(pos).getReportId(), labReportListViews.get(pos).getDoctorName(), "labreport");
                getPrescriptionDelete(labReportListViews.get(pos).getReportId(), labReportListViews.get(pos).getDoctorName(), pos);
            } else {
                if (labReportListViews.get(pos).getReportId().length() > 9) {
                    //create in offline mode and not sync to server and delete from local
                    DbOperations.clearLabDataFromLocal(labReportListViews.get(pos).getReportId(), labReportListViews.get(pos).getDoctorName(), "labreport");
                    labReportListViews.remove(pos);
                    notifyDataSetChanged();

                } else {
                    ContentValues cv = new ContentValues();
                    cv.put("reportId", labReportListViews.get(pos).getReportId());
                    cv.put("status", "deleted");
                    cv.put("isUploaded", "1");

                    DbOperations.insertLabTestReportList(CureFull.getInstanse().getActivityIsntanse(), cv, AppPreference.getInstance().getcf_uuhid(), labReportListViews.get(pos).getReportId());
                    labReportListViews.remove(pos);
                    notifyDataSetChanged();
                }
                if (labReportListViews.size() == 0) {
                    fragmentLabTestReports.checkList();
                }
            }
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date, text_doctor_name, txt_disease_name, txt_count_file;
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
            this.txt_disease_name = (TextView) itemView
                    .findViewById(R.id.txt_disease_name);
            this.img_delete = (ImageView) itemView
                    .findViewById(R.id.img_delete);
            this.img_share = (ImageView) itemView
                    .findViewById(R.id.img_share);
            this.image_item = (ImageView) itemView
                    .findViewById(R.id.image_item);
            this.progress_bar_one = (ProgressBar) itemView.findViewById(R.id.progress_bar_one);
            this.relative_card_view = (RelativeLayout) itemView.findViewById(R.id.relative_card_view);
        }
    }

    private void getPrescriptionDelete(String id, String name, final int pos) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.Delete_Report + id + "&doctor_name=" + name.replace(" ", "%20"),
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
                            labReportListViews.remove(pos);
                            notifyDataSetChanged();
                            if (labReportListViews.size() == 0) {
                                fragmentLabTestReports.checkList();
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

    private void prepareShareIntent(ArrayList<Uri> files) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(false);
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName() + " Lab Report");
        shareIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        shareIntent.setType("image/*");
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        applicationContext.startActivity(Intent.createChooser(shareIntent, "Share Opportunity"));
    }


    private Uri getLocalBitmapUri(Bitmap bmp) {
        Uri bmpUri = null;
        File file = new File(applicationContext.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "share_image_" + System.currentTimeMillis() + ".jpeg");
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.JPEG, 50, out);
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
            if (size == labReportListViews.get(pos).getLabReportImageListViews().size()) {
                prepareShareIntent(files);
            }
            size += 1;
        }
    }


}