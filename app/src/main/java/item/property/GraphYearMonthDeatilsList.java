package item.property;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class GraphYearMonthDeatilsList {
    private ArrayList<GraphViewDetails> graphViewDetailses;
    private String year;

    public GraphYearMonthDeatilsList() {
    }

    public GraphYearMonthDeatilsList(JSONObject jsonObject) {
        if (jsonObject == null)
            return;
        try {
            setYear(jsonObject.getString("year"));
            setGraphViewDetailses(jsonObject.getJSONArray("graphDetailsList"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public ArrayList<GraphViewDetails> getGraphViewDetailses() {
        return graphViewDetailses;
    }

    public void setGraphViewDetailses(ArrayList<GraphViewDetails> graphViewDetailses) {
        this.graphViewDetailses = graphViewDetailses;
    }


    public void setGraphViewDetailses(JSONArray graphlistArray) {
        if (graphlistArray == null)
            return;
        GraphViewDetails card = null;
        this.graphViewDetailses = new ArrayList<GraphViewDetails>();
        for (int i = 0; i < graphlistArray.length(); i++) {
            try {
                card = new GraphViewDetails(graphlistArray.getJSONObject(i));
                this.graphViewDetailses.add(card);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }
}
