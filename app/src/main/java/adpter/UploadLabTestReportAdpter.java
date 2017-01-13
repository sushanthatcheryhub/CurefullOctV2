package adpter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentLabReportImageView;
import fragment.healthapp.FragmentLabTestReport;
import interfaces.IOnOtpDoneDelete;
import item.property.LabReportImageListView;
import item.property.LabReportListView;
import item.property.PrescriptionImageListView;
import utils.AppPreference;
import utils.MyConstants;
import utils.Utils;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UploadLabTestReportAdpter extends RecyclerView.Adapter<UploadLabTestReportAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<LabReportListView> labReportListViews;
    private RequestQueue requestQueue;
    private FragmentLabTestReport fragmentLabTestReports;

    public UploadLabTestReportAdpter(FragmentLabTestReport fragmentLabTestReport, Context applicationContexts,
                                     List<LabReportListView> labReportListViews) {
        this.labReportListViews = labReportListViews;
        this.applicationContext = applicationContexts;
        this.fragmentLabTestReports = fragmentLabTestReport;
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

        Log.e("position", position + "");

        String date = labReportListViews.get(position).getReportDate();

        Log.e("date ", date);

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
            Glide.with(applicationContext).load(labReportListViews.get(position).getLabReportImageListViews().get(0).getReportImage())
                    .thumbnail(0.5f)
                    .crossFade()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(image_item);
        }


        img_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_delete);
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to remove selected Test Report ?", "Test Report", position);
                dialogDeleteAll.setiOnOtpDoneDelete(UploadLabTestReportAdpter.this);
                dialogDeleteAll.show();
            }
        });
        img_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getActivityIsntanse().iconAnim(img_share);
                shareClick(labReportListViews.get(position).getLabReportImageListViews());
            }
        });

        relative_card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putString("doctorName", labReportListViews.get(position).getDoctorName());
                bundle.putString("dieaseName", labReportListViews.get(position).getTestName());
                bundle.putString("date", labReportListViews.get(position).getReportDate());
                bundle.putString("id", labReportListViews.get(position).getReportId());
                bundle.putParcelableArrayList("imageList", labReportListViews.get(position).getLabReportImageListViews());
                CureFull.getInstanse().getFlowInstanseAll()
                        .replace(new FragmentLabReportImageView(), bundle, true);
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
            getPrescriptionDelete(labReportListViews.get(pos).getReportId(), labReportListViews.get(pos).getDoctorName(), pos);
        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_date, text_doctor_name, txt_disease_name, txt_count_file;
        public ImageView img_delete, image_item, img_share;
        public RelativeLayout relative_card_view;

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
            this.relative_card_view = (RelativeLayout) itemView.findViewById(R.id.relative_card_view);
        }
    }

    private void getPrescriptionDelete(String id, String name, final int pos) {
        Log.e("delete", ":- " + id + " name:- " + name + "pos :- " + pos);
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        requestQueue = Volley.newRequestQueue(CureFull.getInstanse().getActivityIsntanse().getApplicationContext());
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.Delete_Report + id + "&doctor_name=" + name,
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

    public void shareClick(ArrayList<LabReportImageListView> prescriptionImageListViews) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND_MULTIPLE);
        ArrayList<Uri> files = new ArrayList<Uri>();
        ArrayList<String> filesName = new ArrayList<String>();
        for (int i = 0; i < prescriptionImageListViews.size(); i++) {
            filesName.add(prescriptionImageListViews.get(i).getReportImage());
        }

        Log.e("name ", "fileNames " + filesName.get(0).toString());
        for (String path : filesName/* List of the files you want to send */) {
            Uri uri = Uri.parse(path);
            Log.e("uri ", "uri " + uri.toString().length());
            files.add(uri);
        }
        Log.e("name ", "fileNames " + files.size());
        sharingIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        sharingIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        sharingIntent.putExtra(Intent.EXTRA_SUBJECT, " " + AppPreference.getInstance().getUserName() + " Report");
//        sharingIntent.putExtra(Intent.EXTRA_TEXT, "Name:- " + AppPreference.getInstance().getUserName() + "\n" + "Mobile No:- " + AppPreference.getInstance().getMobileNumber() + "\n" + "Email Id:- " + AppPreference.getInstance().getUserID());
        sharingIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, files);
        sharingIntent.setType("image/*");
        applicationContext.startActivity(Intent.createChooser(sharingIntent, "Share Image"));
    }


}