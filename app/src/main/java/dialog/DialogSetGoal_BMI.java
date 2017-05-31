package dialog;


import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import ElasticVIews.ElasticAction;
import curefull.healthapp.CureFull;
import curefull.healthapp.R;

public class DialogSetGoal_BMI extends Dialog implements View.OnClickListener {

    private View v = null;
    Context context;
    private TextView txt_ideal_weight, txt_BMR, txt_BMI, btn_edit_next;
    private ImageView dialog_cancel;
    String targetStepCount;
    String targetCaloriesToBurn;
    String targetWaterInTake;


    public DialogSetGoal_BMI(Context _activiyt, String BMI, int bmr, String str_ideal_weight, String targetStepCount, String targetCaloriesToBurn, String targetWaterInTake) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.context = _activiyt;
        setCancelable(true);
        setContentView(R.layout.dialog_set_goal_bmi);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        txt_ideal_weight = (TextView) findViewById(R.id.txt_ideal_weight);
        txt_BMI = (TextView) findViewById(R.id.txt_BMI);
        txt_BMR = (TextView) findViewById(R.id.txt_BMR);
        dialog_cancel = (ImageView) findViewById(R.id.dialog_cancel);
        btn_edit_next = (TextView) findViewById(R.id.btn_edit_next);

        txt_BMI.setText(BMI);
        txt_BMR.setText("" + bmr + " Calories/day");
        txt_ideal_weight.setText(str_ideal_weight);
        dialog_cancel.setOnClickListener(this);
        btn_edit_next.setOnClickListener(this);
        this.targetStepCount = targetStepCount;
        this.targetCaloriesToBurn = targetCaloriesToBurn;
        this.targetWaterInTake = targetWaterInTake;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.dialog_cancel:
                dismiss();
                break;

            case R.id.btn_edit_next:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    ElasticAction.doAction(v, 400, 0.9f, 0.9f);
                }

                DialogSetGoal_Recommendation dialogSetGoal_recomm = new DialogSetGoal_Recommendation(CureFull.getInstanse().getActivityIsntanse(), targetStepCount, targetCaloriesToBurn, targetWaterInTake);
                dialogSetGoal_recomm.show();
                dismiss();
                break;
        }
    }
}