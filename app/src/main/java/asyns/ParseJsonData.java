package asyns;

import org.json.JSONObject;

import item.property.SignUpInfo;
import item.property.UserInfo;
import utils.MyConstants;

/**
 * Created by Sushant Hatcheryhub on 19-07-2016.
 */
public class ParseJsonData implements MyConstants.JsonUtils {

    private static ParseJsonData data;

    public String getHttp_code() {
        return http_code;
    }

    public void setHttp_code(String http_code) {
        this.http_code = http_code;
    }

    private String http_code;

    private ParseJsonData() {

    }

    public static ParseJsonData getInstance() {
        if (data == null)
            data = new ParseJsonData();
        return data;
    }

    public UserInfo getLoginData(String response) {
        UserInfo user = null;
        try {
            JSONObject json = new JSONObject(response);
            setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
            user = new UserInfo(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }

    public SignUpInfo getSignUpData(String response) {
        SignUpInfo user = null;
        try {
            JSONObject json = new JSONObject(response);
            setHttp_code(json.getString(MyConstants.JsonUtils.HTTP_CODE));
            user = new SignUpInfo(json);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return user;
    }
}
