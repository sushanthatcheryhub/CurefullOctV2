package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.error.AuthFailureError;
import com.android.volley.error.VolleyError;
import com.android.volley.request.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import dialog.DialogDeleteAll;
import fragment.healthapp.FragmentUHID;
import interfaces.IOnOtpDoneDelete;
import item.property.UHIDItems;
import utils.AppPreference;
import utils.CheckNetworkState;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UHID_ListAdpter extends RecyclerView.Adapter<UHID_ListAdpter.ItemViewHolder> implements IOnOtpDoneDelete {


    Context applicationContext;
    List<UHIDItems> healthNoteItemses;
    private FragmentUHID fragmentUHIDs;

    public UHID_ListAdpter(FragmentUHID fragmentUHID, Context applicationContexts,
                           List<UHIDItems> healthNoteItemses) {
        this.healthNoteItemses = healthNoteItemses;
        this.applicationContext = applicationContexts;
        this.fragmentUHIDs = fragmentUHID;
    }

    @Override
    public int getItemCount() {
        return (null != healthNoteItemses ? healthNoteItemses.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_uhid, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_cfuuhid = holder.txt_cfuuhid;
        TextView txt_name = holder.txt_name;
        ImageView image_item = holder.image_item;
        final CheckBox checkBox = holder.checkbox;
        txt_cfuuhid.setText("" + healthNoteItemses.get(position).getCfUuhid());
        txt_name.setText("" + healthNoteItemses.get(position).getName());


        if (healthNoteItemses.get(position).isDefaults()) {
            image_item.setVisibility(View.GONE);
            if (healthNoteItemses.get(position).isDefaults() && healthNoteItemses.get(position).isSelected()) {
                AppPreference.getInstance().setcf_uuhidNeew(healthNoteItemses.get(position).getCfUuhid());
            } else {
                AppPreference.getInstance().setcf_uuhidNeew("");
            }
        } else {
            if (healthNoteItemses.get(position).isSelected()) {
                AppPreference.getInstance().setcf_uuhidNeew(healthNoteItemses.get(position).getCfUuhid());
                image_item.setVisibility(View.GONE);
            } else {
                image_item.setVisibility(View.VISIBLE);
            }

        }
        txt_name.setSelected(true);
        if (AppPreference.getInstance().getcf_uuhidNeew().equalsIgnoreCase(healthNoteItemses.get(position).getCfUuhid())) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }


        image_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogDeleteAll dialogDeleteAll = new DialogDeleteAll(CureFull.getInstanse().getActivityIsntanse(), "Do you want to delete " + healthNoteItemses.get(position).getName() + " CFUUHID ?", "UHID", position);
                dialogDeleteAll.setiOnOtpDoneDelete(UHID_ListAdpter.this);
                dialogDeleteAll.show();

            }
        });


        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
//                    healthNoteItemses.get(position).setSelected(true);
                    AppPreference.getInstance().setcf_uuhidNeew(healthNoteItemses.get(position).getCfUuhid());
                    getSelectedUserList(healthNoteItemses.get(position).getCfUuhid());
                } else {
//                    healthNoteItemses.get(position).setSelected(false);
                }

            }
        });

    }

    @Override
    public void optDoneDelete(String messsage, String dialogName, int pos) {
        if (messsage.equalsIgnoreCase("OK")) {
            getSelectedUserListDelete(healthNoteItemses.get(pos).getCfUuhid(), pos);
        }

    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_cfuuhid;
        public TextView txt_name;
        public ImageView image_item;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.txt_cfuuhid = (TextView) itemView
                    .findViewById(R.id.txt_cfuuhid);
            this.txt_name = (TextView) itemView
                    .findViewById(R.id.txt_name);
            this.image_item = (ImageView) itemView.findViewById(R.id.image_item_delete);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }


    private void getSelectedUserList(String cfUuhid) {
        if (CheckNetworkState.isNetworkAvailable(CureFull.getInstanse().getActivityIsntanse())){
            CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.GET, MyConstants.WebUrls.SELECTED_USER_LIST + cfUuhid,
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
                            fragmentUHIDs.getcheck();
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
    }else
    {
        fragmentUHIDs.getcheck(cfUuhid);
    }
    }


    private void getSelectedUserListDelete(String cfUuhid, final int position) {
        CureFull.getInstanse().getActivityIsntanse().showProgressBar(true);
        StringRequest postRequest = new StringRequest(Request.Method.DELETE, MyConstants.WebUrls.DELETE_SELECTE_USER + cfUuhid,
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
                            healthNoteItemses.remove(position);
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
                headers.put("cf_uuhid", AppPreference.getInstance().getcf_uuhid());
                headers.put("user_id", AppPreference.getInstance().getUserIDProfile());
                return headers;
            }
        };

        CureFull.getInstanse().getRequestQueue().add(postRequest);
    }


}