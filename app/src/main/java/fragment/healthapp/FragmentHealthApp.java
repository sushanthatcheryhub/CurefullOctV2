package fragment.healthapp;


import android.animation.ObjectAnimator;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.TextView;

import curefull.healthapp.CureFull;
import curefull.healthapp.R;
import ticker.TickerUtils;
import ticker.TickerView;
import utils.SeekArc;


/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class FragmentHealthApp extends Fragment implements View.OnClickListener {


    private View rootView;
    private SeekArc seekArcComplete;
    private LinearLayout liner_walking, liner_running, liner_cycling;
    private TickerView ticker1, ticker2, ticker3;
    private TextView txt_walking_km, txt_cycling_km, txt_running_km;
    public static final char EMPTY_CHAR = (char) 0;
    private static final char[] NUMBER_LIST = TickerUtils.getDefaultNumberList();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_health_view,
                container, false);
        CureFull.getInstanse().getActivityIsntanse().changeColorActionBar("#737373");
        liner_walking = (LinearLayout) rootView.findViewById(R.id.liner_walking);
        liner_running = (LinearLayout) rootView.findViewById(R.id.liner_running);
        liner_cycling = (LinearLayout) rootView.findViewById(R.id.liner_cycling);
        txt_running_km = (TextView) rootView.findViewById(R.id.txt_running_km);
        txt_cycling_km = (TextView) rootView.findViewById(R.id.txt_cycling_km);
        txt_walking_km = (TextView) rootView.findViewById(R.id.txt_walking_km);

        ticker1 = (TickerView) rootView.findViewById(R.id.ticker1);
        ticker2 = (TickerView) rootView.findViewById(R.id.ticker2);
        ticker3 = (TickerView) rootView.findViewById(R.id.ticker3);
//        txt_total_steps = (TextView) rootView.findViewById(R.id.txt_total_steps);
        ticker1.setCharacterList(NUMBER_LIST);
        ticker2.setCharacterList(NUMBER_LIST);
        ticker3.setCharacterList(NUMBER_LIST);
        ticker1.setText("571");
        ticker2.setText("1:21:40");
        ticker3.setText("60" + "%");
        seekArcComplete = (SeekArc) rootView.findViewById(R.id.seekArcComplete);
        liner_walking.setOnClickListener(this);
        liner_running.setOnClickListener(this);
        liner_cycling.setOnClickListener(this);

        setProgressUpdateAnimation(60);


        txt_walking_km.setText(Html.fromHtml("" + 3.2 + "<sup><small>" + "km" + "</small></sup>"));
        txt_running_km.setText(Html.fromHtml("" + 1.2 + "<sup><small>" + "km" + "</small></sup>"));
        txt_cycling_km.setText(Html.fromHtml("" + 0.0 + "<sup><small>" + "km" + "</small></sup>"));
        return rootView;
    }


    public void setProgressUpdateAnimation(int value) {
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(seekArcComplete, "progress", 0, value);
        progressAnimator.setDuration(1000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.liner_walking:
                setProgressUpdateAnimation(60);
                ticker3.setText("60" + "%");
                ticker2.setText("1:21:40");
                ticker1.setText("571");
                break;
            case R.id.liner_running:
                setProgressUpdateAnimation(80);
                ticker3.setText("75" + "%");
                ticker2.setText("2:09:20");
                ticker1.setText("590");
                break;
            case R.id.liner_cycling:
                setProgressUpdateAnimation(92);
                ticker3.setText("92" + "%");
                ticker2.setText("3:25:30");
                ticker1.setText("800");
                break;
        }
    }


}