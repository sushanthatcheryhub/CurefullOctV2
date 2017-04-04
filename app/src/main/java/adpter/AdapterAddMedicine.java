package adpter;

import android.content.Context;
import android.graphics.Paint;
import android.os.Build;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import fragment.healthapp.FragmentReminderSetMedicine;
import item.property.MedicineReminderItem;

public class AdapterAddMedicine extends BaseAdapter {
    Context applicationContext;
    List<MedicineReminderItem> medicineReminderItems;
    FragmentReminderSetMedicine fragmentReminderMedicine;
    private boolean isFirstTime = false;
    private int intervalCunt = 0;

    public AdapterAddMedicine(FragmentReminderSetMedicine fragmentReminderMedicine, Context applicationContext,
                              List<MedicineReminderItem> medicineReminderItems) {
        this.fragmentReminderMedicine = fragmentReminderMedicine;
        this.applicationContext = applicationContext;
        this.medicineReminderItems = medicineReminderItems;
    }

    @Override
    public int getCount() {
        if (medicineReminderItems == null) {
            return 0;
        }

        return medicineReminderItems.size();
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return medicineReminderItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    /*
     * (non-Javadoc)
     *
     * @see android.widget.Adapter#getView(int, android.view.View,
     * android.view.ViewGroup)
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        LayoutInflater inflater = (LayoutInflater) applicationContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.adpter_medicine_list,
                    null);
            holder = new ViewHolder();
            holder.edt_doctor_name = (EditText) convertView.findViewById(R.id.edt_doctor_name);
            holder.edt_ml = (EditText) convertView.findViewById(R.id.edt_ml);
            holder.edt_medicine_name = (EditText) convertView.findViewById(R.id.edt_medicine_name);
            holder.image_tablet = (ImageView) convertView.findViewById(R.id.image_tablet);
            holder.img_capsule = (ImageView) convertView.findViewById(R.id.img_capsule);
            holder.img_syrup = (ImageView) convertView.findViewById(R.id.img_syrup);
            holder.img_drops = (ImageView) convertView.findViewById(R.id.img_drops);
            holder.img_injection = (ImageView) convertView.findViewById(R.id.img_injection);

            holder.img_minus_icon = (ImageView) convertView.findViewById(R.id.img_minus_icon);
            holder.img_plus_icon = (ImageView) convertView.findViewById(R.id.img_plus_icon);

            holder.txt_tablet = (TextView) convertView.findViewById(R.id.txt_tablet);
            holder.txt_capsule = (TextView) convertView.findViewById(R.id.txt_capsule);
            holder.txt_syrup = (TextView) convertView.findViewById(R.id.txt_syrup);
            holder.txt_drop = (TextView) convertView.findViewById(R.id.txt_drop);
            holder.txt_injection = (TextView) convertView.findViewById(R.id.txt_injection);
            holder.txt_interval_count = (TextView) convertView.findViewById(R.id.txt_interval_count);
            holder.txt_btn_add_more_med = (TextView) convertView.findViewById(R.id.txt_btn_add_more_med);
            holder.txt_btn_remove = (TextView) convertView.findViewById(R.id.txt_btn_remove);

            holder.layout_tablet = (RelativeLayout) convertView.findViewById(R.id.layout_tablet);
            holder.layout_capsule = (RelativeLayout) convertView.findViewById(R.id.layout_capsule);
            holder.layout_syrup = (RelativeLayout) convertView.findViewById(R.id.layout_syrup);
            holder.layout_drops = (RelativeLayout) convertView.findViewById(R.id.layout_drops);
            holder.layout_injection = (RelativeLayout) convertView.findViewById(R.id.layout_injection);

            holder.radioMeal = (RadioGroup) convertView.findViewById(R.id.radioMeal);
            holder.radioAfter = (RadioButton) convertView.findViewById(R.id.radioAfter);
            holder.radioBefore = (RadioButton) convertView.findViewById(R.id.radioBefore);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        if (position == 0) {
            holder.txt_btn_remove.setVisibility(View.GONE);
        } else {
            holder.txt_btn_remove.setPaintFlags(holder.txt_btn_remove.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            holder.txt_btn_remove.setVisibility(View.VISIBLE);
        }


        if (!medicineReminderItems.get(position).getType().equalsIgnoreCase("")) {
            String types = medicineReminderItems.get(position).getType();
            if (types.equalsIgnoreCase("TAB")) {
                holder.layout_tablet.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);
                holder.image_tablet.setImageResource(R.drawable.tablet_red);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
            } else if (types.equalsIgnoreCase("CAP")) {
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);
                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_red);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
            } else if (types.equalsIgnoreCase("SYR")) {
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);
                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_red);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
            } else if (types.equalsIgnoreCase("DRO")) {
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);

                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_red);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
            } else if (types.equalsIgnoreCase("INJ")) {
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.buttonclick);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_red);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
            }

        }


        holder.layout_tablet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                medicineReminderItems.get(position).setType("TAB");
                holder.layout_tablet.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);

                holder.image_tablet.setImageResource(R.drawable.tablet_red);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));

            }
        });
        holder.layout_capsule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                medicineReminderItems.get(position).setType("CAP");
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);

                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_red);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));

            }
        });
        holder.layout_syrup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                medicineReminderItems.get(position).setType("SYR");
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);

                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_red);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));

            }
        });
        holder.layout_drops.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                medicineReminderItems.get(position).setType("DRO");
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.buttonclick);
                holder.layout_injection.setBackgroundResource(R.drawable.button_unclick);

                holder.image_tablet.setImageResource(R.drawable.tablet_yellow);
                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_red);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
                holder.img_injection.setImageResource(R.drawable.injection_yellow);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
            }
        });
        holder.layout_injection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                medicineReminderItems.get(position).setType("INJ");
                holder.layout_tablet.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_capsule.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_syrup.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_drops.setBackgroundResource(R.drawable.button_unclick);
                holder.layout_injection.setBackgroundResource(R.drawable.buttonclick);

                holder.txt_tablet.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_capsule.setImageResource(R.drawable.capsule_yellow);
                holder.txt_capsule.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_syrup.setImageResource(R.drawable.syrup_yellow);
                holder.txt_syrup.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_drops.setImageResource(R.drawable.drops_yellow);
                holder.txt_drop.setTextColor(applicationContext.getResources().getColor(R.color.health_dark_gray));
                holder.img_injection.setImageResource(R.drawable.injection_red);
                holder.txt_injection.setTextColor(applicationContext.getResources().getColor(R.color.health_yellow));
            }
        });


        holder.txt_interval_count.setText("" + medicineReminderItems.get(position).getInterval());

        holder.img_minus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                intervalCunt = medicineReminderItems.get(position).getInterval();
                if (intervalCunt != 0) {
                    --intervalCunt;
                    holder.txt_interval_count.setText("" + intervalCunt);
                    medicineReminderItems.get(position).setInterval(intervalCunt);
                }
            }
        });
        holder.img_plus_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT)
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                intervalCunt = medicineReminderItems.get(position).getInterval();
                ++intervalCunt;
                holder.txt_interval_count.setText("" + intervalCunt);
                medicineReminderItems.get(position).setInterval(intervalCunt);
            }
        });


        holder.edt_doctor_name.setText("" + medicineReminderItems.get(position).getDoctorName());

        holder.edt_medicine_name.setText(medicineReminderItems.get(position).getMedicineName());

        holder.edt_doctor_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String type = holder.edt_doctor_name.getText().toString().trim();
                medicineReminderItems.get(position).setDoctorName("" + type);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        holder.edt_ml.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String type = holder.edt_ml.getText().toString().trim();
                medicineReminderItems.get(position).setQuantityType("" + type);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        holder.edt_medicine_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String type = holder.edt_medicine_name.getText().toString().trim();
                medicineReminderItems.get(position).setMedicineName("" + type);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        if (medicineReminderItems.get(position).isBaMealAfter()) {
            holder.radioAfter.setChecked(true);
        } else {
            holder.radioAfter.setChecked(false);
        }

        if (medicineReminderItems.get(position).isBaMealBefore()) {
            holder.radioBefore.setChecked(true);
        } else {
            holder.radioBefore.setChecked(false);
        }

        holder.radioMeal.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });

        holder.radioMeal.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioAfter) {
                    medicineReminderItems.get(position).setBaMealAfter(true);
                    medicineReminderItems.get(position).setBaMealBefore(false);
                } else if (checkedId == R.id.radioBefore) {
                    medicineReminderItems.get(position).setBaMealAfter(false);
                    medicineReminderItems.get(position).setBaMealBefore(true);
                }

            }
        });

        holder.txt_btn_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentReminderMedicine.updateRemove(position);

            }
        });

        holder.txt_btn_add_more_med.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (medicineReminderItems.get(position).getType().equalsIgnoreCase("")) {
                    Toast.makeText(applicationContext, "Please Select Type", Toast.LENGTH_SHORT).show();
                } else if (medicineReminderItems.get(position).getDoctorName().equalsIgnoreCase("")) {
                    Toast.makeText(applicationContext, "Please Fill Doctor Name", Toast.LENGTH_SHORT).show();
                } else if (medicineReminderItems.get(position).getMedicineName().equalsIgnoreCase("")) {
                    Toast.makeText(applicationContext, "Please Fill Medicine Name", Toast.LENGTH_SHORT).show();
                } else if (medicineReminderItems.get(position).getInterval() == 0) {
                    Toast.makeText(applicationContext, "Please Select Quantity", Toast.LENGTH_SHORT).show();
                } else if (medicineReminderItems.get(position).isBaMealBefore() == false && medicineReminderItems.get(position).isBaMealAfter() == false) {
                    Toast.makeText(applicationContext, "Please Select Meal", Toast.LENGTH_SHORT).show();
                } else {
                    CureFull.getInstanse().getActivityIsntanse().hideVirtualKeyboard();
                    fragmentReminderMedicine.updateAdd(medicineReminderItems.get(position).getId());
                }

            }
        });

        if (position == medicineReminderItems.size() - 1) {
            if (medicineReminderItems.get(position).isShow()) {
                holder.txt_btn_add_more_med.setVisibility(View.INVISIBLE);
            } else {
                holder.txt_btn_add_more_med.setVisibility(View.VISIBLE);
                holder.txt_btn_add_more_med.setPaintFlags(holder.txt_btn_add_more_med.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
            }

        }
        return convertView;
    }

    public static class ViewHolder {
        public RelativeLayout layout_tablet, layout_capsule, layout_syrup, layout_drops, layout_injection;
        public ImageView image_tablet, img_capsule, img_syrup, img_drops, img_injection, img_minus_icon, img_plus_icon;
        public TextView txt_tablet, txt_capsule, txt_syrup, txt_drop, txt_injection, txt_interval_count, txt_btn_add_more_med, txt_btn_remove;
        private RadioGroup radioMeal;
        private RadioButton radioAfter, radioBefore;
        private EditText edt_doctor_name, edt_medicine_name, edt_ml;
    }


}