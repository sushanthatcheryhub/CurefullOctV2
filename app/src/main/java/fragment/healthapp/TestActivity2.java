//package fragment.healthapp;
//
//import android.os.Bundle;
//import android.os.PersistableBundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.github.mikephil.charting.charts.BarChart;
//import com.github.mikephil.charting.data.BarData;
//import com.github.mikephil.charting.data.BarDataSet;
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
//import com.github.mikephil.charting.utils.ColorTemplate;
//
//import java.util.ArrayList;
//
//import curefull.healthapp.R;
//import widgets.MyBarChart;
//
///**
// * Created by Sushant Hatcheryhub on 18-12-2016.
// */
//
//public class TestActivity2 extends AppCompatActivity {
//    MyBarChart mChart;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.vertical_menu);
//
//        mChart = (MyBarChart) findViewById(R.id.chart1);
//
//        setData(100,3,mChart);
//
//        mChart.setScrollContainer(true);
//    }
//
//
//    private void setData(int count, float range, BarChart mChart) {
//        float start = 1f;
//        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();
//
//        for (int i = (int) start; i < start + count + 1; i++) {
//            float mult = (range + 1);
//            float val = (float) (Math.random() * mult);
//            yVals1.add(new BarEntry(i, val));
//        }
//
//        BarDataSet set1;
//
//        if (mChart.getData() != null &&
//                mChart.getData().getDataSetCount() > 0) {
//            set1 = (BarDataSet) mChart.getData().getDataSetByIndex(0);
//            set1.setValues(yVals1);
//            mChart.getData().notifyDataChanged();
//            mChart.notifyDataSetChanged();
//        } else {
//            set1 = new BarDataSet(yVals1, "");
//            set1.setColors(ColorTemplate.MATERIAL_COLORS);
//
//            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
//            dataSets.add(set1);
//
//            BarData data = new BarData(dataSets);
//            data.setValueTextSize(10f);
////            data.setValueTypeface(mTfLight);
//            data.setBarWidth(0.4f);
//            mChart.animateXY(3000, 3000);
//            mChart.setData(data);
//        }
//    }
//}
