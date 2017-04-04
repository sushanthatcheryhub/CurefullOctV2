package dialog;

import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;

import curefull.healthapp.R;
import interfaces.IOnRedmiDone;


public class DialogRedmiMessage extends Dialog implements View.OnClickListener {

    private View v = null;
    private Context context;

    public DialogRedmiMessage(Context _activiyt) {
        super(_activiyt, R.style.MyTheme);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        setContentView(R.layout.dialog_redmi);
        v = getWindow().getDecorView();
        v.setBackgroundResource(android.R.color.transparent);
        getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        findViewById(R.id.btn_done).setOnClickListener(this);
        this.context = _activiyt;
    }


    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.btn_done) {
            //this will open auto start screen where user can enable permission for your app
            Intent intent = new Intent();
            intent.setComponent(new ComponentName("com.miui.securitycenter", "com.miui.permcenter.autostart.AutoStartManagementActivity"));
            context.startActivity(intent);
            dismiss();
        }

    }

}
