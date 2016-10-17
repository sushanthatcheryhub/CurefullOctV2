package curefull.healthapp;

import android.support.v4.app.Fragment;

import interfaces.IOnBackPressed;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class BaseBackHandlerFragment extends Fragment implements IOnBackPressed {


    @Override
    public boolean onBackPressed() {
        return true;
    }
}
