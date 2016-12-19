package curefull.healthapp.models;

/**
 * Created by Sushant Hatcheryhub on 18-12-2016.
 */

public class Day {
    private int date;
    private int value;

    /**
     * @param date
     * @param value
     */
    public Day(int date, int value) {
        this.date = date;
        this.value = value;
    }

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
