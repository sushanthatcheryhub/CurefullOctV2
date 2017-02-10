//package utils;
//
//import android.util.Log;
//
//import com.github.mikephil.charting.charts.BarLineChartBase;
//import com.github.mikephil.charting.components.AxisBase;
//import com.github.mikephil.charting.formatter.IAxisValueFormatter;
//
//import java.util.List;
//
//import curefull.healthapp.CureFull;
//import item.property.GraphYearMonthDeatilsList;
//import widgets.MyCombinedChart;
//
///**
// * Created by Sushant Hatcheryhub on 18-12-2016.
// */
//
//public class MyDayAxisValueFormatter implements IAxisValueFormatter {
//
//    private int month = 0;
//    int year = 2016;
//    private String freq;
//    private List<GraphYearMonthDeatilsList> graphYearMonthDeatilsLists;
//    private int pos;
//
//    protected String[] mMonths = new String[]{"",
//            "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"
//    };
//
//    private BarLineChartBase<?> chart;
//
//    public MyDayAxisValueFormatter(BarLineChartBase<?> chart, int month, int year, String frequencys, List<GraphYearMonthDeatilsList> horizontalList, int position) {
//        this.chart = chart;
//        this.month = month;
//        this.year = year;
//        this.freq = frequencys;
//        this.graphYearMonthDeatilsLists = horizontalList;
//        this.pos = position;
//    }
//
//
//    @Override
//    public String getFormattedValue(float value, AxisBase axis) {
//        Log.e("value", " " + value);
//        Log.e("postion", " " + pos);
//        CureFull.getInstanse().setPostionGet(pos);
//        int days = (int) value;
//        // int year = determineYear(days);
//        // int month = determineMonth(days);
//        String monthName = mMonths[month % mMonths.length];
//        String yearName = String.valueOf(year);
//
//       /* if (chart.getVisibleXRange() > 30 * 6) {
//            return monthName + " " + yearName;
//        } else*/
//
//        if (freq.equalsIgnoreCase("daily")) {
//            int dayOfMonth = days;/*determineDayOfMonth(days, month + 12 * (year - 2016));*/
//            String appendix = "th";
//            switch (dayOfMonth) {
//                case 1:
//                    appendix = "st";
//                    break;
//                case 2:
//                    appendix = "nd";
//                    break;
//                case 3:
//                    appendix = "rd";
//                    break;
//                case 21:
//                    appendix = "st";
//                    break;
//                case 22:
//                    appendix = "nd";
//                    break;
//                case 23:
//                    appendix = "rd";
//                    break;
//                case 31:
//                    appendix = "st";
//                    break;
//            }
//
//            return dayOfMonth == 0 ? "" : dayOfMonth + " " + monthName;
//        } else if (freq.equalsIgnoreCase("monthly")) {
//            int dayOfMonth = days;/*determineDayOfMonth(days, month + 12 * (year - 2016));*/
//            String appendix = "th";
//            switch (dayOfMonth) {
//                case 1:
//                    appendix = "Jan";
//                    break;
//                case 2:
//                    appendix = "Feb";
//                    break;
//                case 3:
//                    appendix = "Mar";
//                    break;
//                case 4:
//                    appendix = "Apr";
//                    break;
//                case 5:
//                    appendix = "May";
//                    break;
//                case 6:
//                    appendix = "Jun";
//                    break;
//                case 7:
//                    appendix = "Jul";
//                    break;
//                case 8:
//                    appendix = "Aug";
//                    break;
//                case 9:
//                    appendix = "Sep";
//                    break;
//                case 10:
//                    appendix = "Oct";
//                    break;
//                case 11:
//                    appendix = "Nov";
//                    break;
//                case 12:
//                    appendix = "Dec";
//                    break;
//            }
//
//            return dayOfMonth == 0 ? "" : appendix;
//        } else {
////            Log.e("days"," "+graphYearMonthDeatilsLists.get(pos).getGraphViewDetailses().get(days).getDate());
//            try {
//                if (days > 0) {
//                    String dateTime = graphYearMonthDeatilsLists.get(month - 1).getGraphViewDetailses().get(days - 1).getDate();
//                    String[] dateParts = dateTime.split("to");
//                    String day1 = dateParts[0];
//                    String day2 = dateParts[1];
//
//                    String[] real1 = day1.split("-");
//                    String years = real1[0];
//                    String months = real1[1];
//                    String days1 = real1[2];
//
//                    String[] real2 = day2.split("-");
//                    String years1 = real2[0];
//                    String months1 = real2[1];
//                    String days2 = real2[2];
//
//                    String all = days1.trim() + days2.trim();
//
//
//                    int dayOfMonth = days;/*determineDayOfMonth(days, month + 12 * (year - 2016));*/
//                    String appendix = "th";
//                    switch (dayOfMonth) {
//                        case 1:
//                            appendix = "st";
//                            break;
//                        case 2:
//                            appendix = "nd";
//                            break;
//                        case 3:
//                            appendix = "rd";
//                            break;
//                        case 21:
//                            appendix = "st";
//                            break;
//                        case 22:
//                            appendix = "nd";
//                            break;
//                        case 23:
//                            appendix = "rd";
//                            break;
//                        case 31:
//                            appendix = "st";
//                            break;
//                    }
//                    return dayOfMonth == 0 ? "" : "(" + days1.trim() + "-" + (days2.trim()) + ")" + monthName;
//                } else {
//                    return "";
//                }
//
//
//            } catch (Exception e) {
//
//            }
//
//            return "";
//        }
//    }
//
//    private int getDaysForMonth(int month, int year) {
//        // month is 0-based
//
//        if (month == 1) {
//            int x400 = month % 400;
//            if (x400 < 0) {
//                x400 = -x400;
//            }
//            boolean is29 = (month % 4) == 0 && x400 != 100 && x400 != 200 && x400 != 300;
//            return is29 ? 29 : 28;
//        }
//
//        if (month == 3 || month == 5 || month == 8 || month == 10)
//            return 30;
//        else
//            return 31;
//    }
//
//    private int determineMonth(int dayOfYear) {
//        int month = -1;
//        int days = 0;
//        while (days < dayOfYear) {
//            month = month + 1;
//            if (month >= 12)
//                month = 0;
//
//            int year = determineYear(days);
//            days += getDaysForMonth(month, year);
//        }
//
//        return Math.max(month, 0);
//    }
//
//    private int determineDayOfMonth(int days, int month) {
//
//        int count = 0;
//        int daysForMonths = 0;
//
//        while (count < month) {
//
//            int year = determineYear(daysForMonths);
//            daysForMonths += getDaysForMonth(count % 12, year);
//            count++;
//        }
//
//        return days - daysForMonths;
//    }
//
//    private int determineYear(int days) {
//        if (days <= 366)
//            return 2016;
//        else if (days <= 730)
//            return 2017;
//        else if (days <= 1094)
//            return 2018;
//        else if (days <= 1458)
//            return 2019;
//        else
//            return 2020;
//
//    }
//}
