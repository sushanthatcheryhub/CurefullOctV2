package fragment.healthapp;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentSignUp extends Fragment implements View.OnClickListener {


    private View rootView;
    private TextView btn_signup;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_signup,
                container, false);

        btn_signup = (TextView) rootView.findViewById(R.id.btn_signup);
        btn_signup.setOnClickListener(this);
        return rootView;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_signup:
                CureFull.getInstanse().getFlowInstanse()
                        .replace(new FragmentAuthorizationInformation(), true);
                break;
        }
    }
}