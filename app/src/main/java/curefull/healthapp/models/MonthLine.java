//package curefull.healthapp.models;
//
//import com.github.mikephil.charting.data.BarEntry;
//import com.github.mikephil.charting.data.Entry;
//
//import java.util.ArrayList;
//import java.util.Random;
//
///**
// * Created by Sushant Hatcheryhub on 18-12-2016.
// */
//
//public class MonthLine {
//    private ArrayList<Entry> days = new ArrayList<>();
//
//    /**
//     * @param numberOfDays
//     */
//    public MonthLine(int numberOfDays) {
//        Random rnd = new Random();
//        for (int i = 0; i < numberOfDays; i++) {
//            int randomNum = rnd.nextInt((3000 - 500) + 1) + 500;
//            days.add(new BarEntry(i + 1, randomNum));
//        }
//    }
//
//    public ArrayList<Entry> getDays() {
//        return days;
//    }
//
//    public void setDays(ArrayList<Entry> days) {
//        this.days = days;
//    }
//}
