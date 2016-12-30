//package curefull.healthapp;
//
//import android.annotation.SuppressLint;
//import android.graphics.RectF;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//
//import com.github.mikephil.charting.data.Entry;
//import com.github.mikephil.charting.highlight.Highlight;
//import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
//
//import java.util.ArrayList;
//
//import adpter.HorizontalAdapter;
//import curefull.healthapp.models.Month;
//import utils.MyConstants;
//import widgets.HorizontalRecyclerView;
//
///**
// * Created by Sushant Hatcheryhub on 17-12-2016.
// */
//
//public class TestActivity extends AppCompatActivity implements OnChartValueSelectedListener {
//
//    HorizontalRecyclerView horizontal_recycler_view;
//    HorizontalAdapter horizontalAdapter;
//
//    ArrayList<Month> data;
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.test_activity);
//
//        horizontal_recycler_view = (HorizontalRecyclerView) findViewById(R.id.horizontal_recycler_view);
//        data = createSampleYear();
//
//        horizontalAdapter = new HorizontalAdapter(data, this, FragmentHealthAppNew.this);
//
////        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.HORIZONTAL, false);
////        horizontal_recycler_view.setLayoutManager(horizontalLayoutManager);
//        horizontal_recycler_view.setAdapter(horizontalAdapter);
//        horizontal_recycler_view.setOnLoadMoreListener(new HorizontalRecyclerView.IOnLoadMoreListener() {
//            @Override
//            public void onLoadMore(int page, int totalItemsCount) {
//               // data.addAll(createSampleYear());
//                //horizontalAdapter.notifyDataSetChanged();
//            }
//        });
//
//
//    }
//
//
//    /*private void setData(int count, float range, BarChart mChart) {
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
//    }*/
//
//
//    private ArrayList<Month> createSampleYear() {
//        ArrayList<Month> months = new ArrayList<>();
//        for (int day : MyConstants.IArrayData.YEAR_2016) {
//            months.add(new Month(day));
//        }
//        return months;
//    }
//
//
//    protected RectF mOnValueSelectedRectF = new RectF();
//
//    @SuppressLint("NewApi")
//    @Override
//    public void onValueSelected(Entry e, Highlight h) {
//
//        if (e == null)
//            return;
//
////        RectF bounds = mOnValueSelectedRectF;
//////        mChart.getBarBounds((BarEntry) e, bounds);
////        MPPointF position = mChart.getPosition(e, YAxis.AxisDependency.LEFT);
////
////        Log.i("bounds", bounds.toString());
////        Log.i("position", position.toString());
////        Log.i("x-index",
////                "low: " + mChart.getLowestVisibleX() + ", high: "
////                        + mChart.getHighestVisibleX());
////
////        MPPointF.recycleInstance(position);
//    }
//
//    @Override
//    public void onNothingSelected() {
//    }
//
//}
