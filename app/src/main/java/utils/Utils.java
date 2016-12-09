package utils;

public class Utils {

    /**
     * Returns a proportion (n out of a total) as a percentage, in a float.
     */
    public static float getPercentage(int n, int total) {
        float proportion = ((float) n) / ((float) total);
        return proportion * 100;
    }

    public static float getPercentageValue(float percent, int total) {
        float proportion = percent / 100;
        return proportion * total;
    }

    public static double getCaloriesBurnt(int pounds, int steps) {
        double cal = (pounds * 0.30 * (0.0005 * steps));
        return cal;
    }

    public static float getIdealWeightMen(int inch) {
        float cal = (float) (50 + (2.3 * inch));
        return cal;
    }

    public static float getIdealWeightWomen(int inch) {
        float cal = (float) (45.5 + (2.3 * inch));
        return cal;
    }
}
