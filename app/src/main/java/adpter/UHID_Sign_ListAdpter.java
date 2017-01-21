package adpter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

import curefull.healthapp.R;
import fragment.healthapp.FragmentUHIDSignUp;
import item.property.UHIDItemsCheck;
import utils.AppPreference;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class UHID_Sign_ListAdpter extends RecyclerView.Adapter<UHID_Sign_ListAdpter.ItemViewHolder> {
    Context applicationContext;
    List<UHIDItemsCheck> healthNoteItemses;
    private RequestQueue requestQueue;
    private FragmentUHIDSignUp fragmentUHIDs;

    public UHID_Sign_ListAdpter(FragmentUHIDSignUp fragmentUHID, Context applicationContexts,
                                List<UHIDItemsCheck> healthNoteItemses) {
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_sign_uhid, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        TextView txt_cfuuhid = holder.txt_cfuuhid;
        TextView txt_name = holder.txt_name;
        TextView txt_generated_by = holder.txt_generated_by;
        final CheckBox checkBox = holder.checkbox;
        txt_cfuuhid.setText("" + healthNoteItemses.get(position).getCfUuhid());
        txt_name.setText("" + healthNoteItemses.get(position).getName());
        txt_generated_by.setText("" + healthNoteItemses.get(position).getCreatedAt());

        if (AppPreference.getInstance().getcf_uuhidSignUp().equalsIgnoreCase(healthNoteItemses.get(position).getCfUuhid())) {
            checkBox.setChecked(true);
        } else {
            checkBox.setChecked(false);
        }

        txt_name.setSelected(true);
        Log.e("yes", ":- yes" + healthNoteItemses.get(position).isChecked());

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("check", ":- right");
                if (checkBox.isChecked()) {
//                    checkBox.setChecked(false);
                    Log.e("check", ":- true");
                    healthNoteItemses.get(position).setChecked(true);
                    AppPreference.getInstance().setcf_uuhidSignUp(healthNoteItemses.get(position).getCfUuhid());
                    fragmentUHIDs.checkedUHID(healthNoteItemses.get(position).getCfUuhid(), healthNoteItemses.get(position).getName());

                } else {
//                    checkBox.setChecked(true);
                    Log.e("check", ":- false");
                    fragmentUHIDs.checkedUHID("", "");
                    healthNoteItemses.get(position).setChecked(false);
                }
                notifyDataSetChanged();
            }
        });

    }


    class ItemViewHolder extends RecyclerView.ViewHolder {

        public TextView txt_cfuuhid, txt_generated_by;
        public TextView txt_name;
        public CheckBox checkbox;

        ItemViewHolder(View view) {
            super(view);
            this.txt_generated_by = (TextView) itemView.findViewById(R.id.txt_generated_by);
            this.txt_cfuuhid = (TextView) itemView
                    .findViewById(R.id.txt_cfuuhid);
            this.txt_name = (TextView) itemView
                    .findViewById(R.id.txt_name);
            this.checkbox = (CheckBox) itemView.findViewById(R.id.checkbox);
        }
    }


}