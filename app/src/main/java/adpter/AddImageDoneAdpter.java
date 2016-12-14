package adpter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

import curefull.healthapp.CureFull;
import curefull.healthapp.MainActivity;
import curefull.healthapp.R;
import dialog.DialogFullViewClickImage;
import dialog.DialogFullViewImage;
import interfaces.IOnCheckCheckbox;
import item.property.PrescriptionImageList;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class AddImageDoneAdpter extends RecyclerView.Adapter<AddImageDoneAdpter.ItemViewHolder> {


    Context applicationContext;
    List<PrescriptionImageList> prescriptionImageLists;
    private RequestQueue requestQueue;
    private boolean isDelete;
    private IOnCheckCheckbox iOnCheckCheckbox;
    private DialogFullViewClickImage dialogFullViewClickImages;

    public AddImageDoneAdpter(Context applicationContexts,
                              List<PrescriptionImageList> patientList, boolean isDeletes, DialogFullViewClickImage dialogFullViewClickImage) {
        this.prescriptionImageLists = patientList;
        this.applicationContext = applicationContexts;
        this.isDelete = isDeletes;
        this.dialogFullViewClickImages = dialogFullViewClickImage;
    }

    @Override
    public int getItemCount() {
        return (null != prescriptionImageLists ? prescriptionImageLists.size() : 0);
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adpter_add__done_image, null);
        ItemViewHolder holder = new ItemViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, final int position) {
        ImageView image_item = holder.image_item;
        final CheckBox btn_checkbox = holder.btn_checkbox;
        final TextView txt_view = holder.txt_view;

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        Bitmap bitmap = BitmapFactory.decodeFile(prescriptionImageLists.get(position).getPrescriptionImage(), options);
        image_item.setImageBitmap(bitmap);

        if (isDelete) {
            holder.btn_checkbox.setVisibility(View.VISIBLE);
            holder.txt_view.setVisibility(View.GONE);
        } else {
            holder.btn_checkbox.setVisibility(View.GONE);
            holder.txt_view.setVisibility(View.VISIBLE);
        }

        holder.txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("click", "ok");
                if (!isDelete && prescriptionImageLists.get(position).getImageNumber() != 000) {
                    BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inPreferredConfig = Bitmap.Config.ARGB_8888;
                    Bitmap bitmap = BitmapFactory.decodeFile(prescriptionImageLists.get(position).getPrescriptionImage(), options);
                    DialogFullViewImage dialogFullViewImage = new DialogFullViewImage(applicationContext, bitmap);
                    dialogFullViewImage.show();
                } else if (prescriptionImageLists.get(position).getImageNumber() == 000) {
                    Log.e("ok", "ok");
                    dialogFullViewClickImages.isCheck();
                }

            }
        });

        btn_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                if (btn_checkbox.isChecked()) {
                    prescriptionImageLists.get(position).setChecked(true);
                } else {
                    prescriptionImageLists.get(position).setChecked(false);
                }


            }
        });

        if (prescriptionImageLists.get(position).getImageNumber() == 000) {
            holder.txt_view.setText("");
            btn_checkbox.setVisibility(View.GONE);
            image_item.setScaleType(ImageView.ScaleType.CENTER);
            image_item.setImageResource(R.drawable.prescription_red);
        }


    }

    class ItemViewHolder extends RecyclerView.ViewHolder {

        public ImageView image_item;
        public CheckBox btn_checkbox;
        public TextView txt_view;

        ItemViewHolder(View view) {
            super(view);
            this.txt_view = (TextView) itemView
                    .findViewById(R.id.txt_view);
            this.image_item = (ImageView) itemView
                    .findViewById(R.id.image_item);

            this.btn_checkbox = (CheckBox) itemView.findViewById(R.id.btn_checkbox);
        }
    }


    public IOnCheckCheckbox getiOnCheckCheckbox() {
        return iOnCheckCheckbox;
    }

    public void setiOnCheckCheckbox(IOnCheckCheckbox iOnCheckCheckbox) {
        this.iOnCheckCheckbox = iOnCheckCheckbox;
    }
}