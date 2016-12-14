package adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

import curefull.healthapp.R;
import item.property.PrescriptionImageList;
import item.property.PrescriptionUploadItems;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class AddImageAdpter extends RecyclerView.Adapter<AddImageAdpter.ItemViewHolder> {

    Context applicationContext;
    List<PrescriptionImageList> prescriptionImageLists;
    private RequestQueue requestQueue;

    public AddImageAdpter(Context applicationContexts,
                          List<PrescriptionImageList> patientList) {
        this.prescriptionImageLists = patientList;
        this.applicationContext = applicationContexts;
    }

    @Override
    public int getItemCount() {
        return (null != prescriptionImageLists ? prescriptionImageLists.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_add_image, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, final int position) {
        final ImageView image_item = holder.image_item;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(prescriptionImageLists.get(position).getPrescriptionImage(), options);
        image_item.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));


//        txt_view_symptoms.setText("" + symptomsGrids.get(position).getSymptomsName());
//
//
//        check_box.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
//                if (check_box.isChecked()) {
//                    symptomsGrids.get(position).setChecked(true);
//                    fragmentDoctorPrescriptions.addRemoveSysmptoms(false);
//                } else {
//                    symptomsGrids.get(position).setChecked(false);
//                    fragmentDoctorPrescriptions.addRemoveSysmptoms(false);
//                }
//            }
//        });
//
//        txt_view_symptoms.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (check_box.isChecked()) {
//                    check_box.setChecked(false);
//                    symptomsGrids.get(position).setChecked(false);
//                    fragmentDoctorPrescriptions.addRemoveSysmptoms(false);
//                } else {
//                    check_box.setChecked(true);
//                    symptomsGrids.get(position).setChecked(true);
//                    fragmentDoctorPrescriptions.addRemoveSysmptoms(true);
//                }
//            }
//        });
//
//        if (symptomsGrids.get(position).isBlocked()) {
//            holder.check_box.setChecked(true);
//            holder.check_box.setClickable(false);
//            holder.check_box.setSelected(false);
//            holder.txt_view_symptoms.setClickable(false);
//        } else {
//            holder.check_box.setChecked(false);
//            holder.check_box.setClickable(true);
//            holder.check_box.setSelected(true);
//            holder.txt_view_symptoms.setClickable(true);
//        }
    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_item;

        ItemViewHolder(View view) {
            super(view);

            this.image_item = (ImageView) itemView
                    .findViewById(R.id.image_item);
//            this.check_box = (CheckBox) itemView
//                    .findViewById(R.id.check_box);
        }
    }


}