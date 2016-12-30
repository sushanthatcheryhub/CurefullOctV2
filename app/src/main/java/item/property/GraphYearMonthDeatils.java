package item.property;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GraphYearMonthDeatils {
    String years;
    private ArrayList<GraphYearMonthDeatilsList> graphViewDetailses;

    public GraphYearMonthDeatils() {
    }

    public GraphYearMonthDeatils(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setYears(jsonObject.getString("year"));
            setGraphViewDetailses(jsonObject.getJSONArray("graphDetailsGroupByMonth"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getYears() {
        return years;
    }

    public void setYears(String years) {
        this.years = years;
    }

    public ArrayList<GraphYearMonthDeatilsList> getGraphViewDetailses() {
        return graphViewDetailses;
    }

    public void setGraphViewDetailses(ArrayList<GraphYearMonthDeatilsList> graphViewDetailses) {
        this.graphViewDetailses = graphViewDetailses;
    }


    public void setGraphViewDetailses(JSONArray graphlistArray) {
        if (graphlistArray == null)
            return;
        GraphYearMonthDeatilsList card = null;
        this.graphViewDetailses = new ArrayList<GraphYearMonthDeatilsList>();
        for (int i = 0; i < graphlistArray.length(); i++) {
            try {
                card = new GraphYearMonthDeatilsList(graphlistArray.getJSONObject(i));
                this.graphViewDetailses.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
