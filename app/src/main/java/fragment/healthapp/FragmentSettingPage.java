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
public class FragmentSettingPage extends Fragment {


    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_setting,
                container, false);
        if (CureFull.getInstanse().getiGlobalIsbackButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalIsbackButtonVisible().isbackButtonVisible(true,"");
        }
        if (CureFull.getInstanse().getiGlobalTopBarButtonVisible() != null) {
            CureFull.getInstanse().getiGlobalTopBarButtonVisible().isTobBarButtonVisible(true);
        }
        CureFull.getInstanse().getActivityIsntanse().selectedNav(5);
        CureFull.getInstanse().getActivityIsntanse().activateDrawer();
        CureFull.getInstanse().getActivityIsntanse().showActionBarToggle(false);
        CureFull.getInstanse().getActivityIsntanse().clickImage(rootView);
        return rootView;
    }


}