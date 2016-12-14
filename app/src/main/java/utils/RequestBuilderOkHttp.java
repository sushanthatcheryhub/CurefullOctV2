package utils;

/**
 * Created by esec-0052 on 25/5/16.
 */

import android.util.Log;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class RequestBuilderOkHttp {
    final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");

    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");

    OkHttpClient client = new OkHttpClient();

    public String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    public String post(String url, HashMap<String, String> params) throws Exception {
        FormBody.Builder builder = new FormBody.Builder();

        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.add(entry.getKey(), entry.getValue());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    String post(String url, JSONObject json) throws Exception {
        RequestBody body = RequestBody.create(JSON, json.toString());
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public String post(String url, HashMap<String, String> params, HashMap<String, List<File>> fileParams, String doctorName, String dieaseName, String prescriptionDate, String removeSyptoms) throws Exception {
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildernew.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        Log.e("fileParams", ":- " + fileParams.size());
        if (fileParams != null && fileParams.size() > 0) {
            for (Map.Entry<String, List<File>> entry : fileParams.entrySet()) {
                if (entry.getValue() != null) {
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        buildernew.addFormDataPart(entry.getKey(), entry.getValue().get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, entry.getValue().get(i)));
                    }

                }
            }
        }

        RequestBody body = buildernew.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("a_t", AppPreference.getInstance().getAt())
                .addHeader("r_t", AppPreference.getInstance().getRt())
                .addHeader("user_name", AppPreference.getInstance().getUserName())
                .addHeader("email_id", AppPreference.getInstance().getUserID())
                .addHeader("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew())
                .addHeader("healthRecordDate", prescriptionDate)
                .addHeader("doctorName", doctorName)
                .addHeader("disease", dieaseName)
                .addHeader("imageOrder", removeSyptoms)
                .post(body)
                .build();

        Log.e("request", " " + request.body().toString());

        try

        {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        return null;
    }

    public String postR(String url, HashMap<String, String> params, HashMap<String, List<File>> fileParams, String doctorName, String dieaseName, String prescriptionDate, String removeSyptoms) throws Exception {
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildernew.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }
        Log.e("fileParams", ":- " + fileParams.size());
        if (fileParams != null && fileParams.size() > 0) {
            for (Map.Entry<String, List<File>> entry : fileParams.entrySet()) {
                if (entry.getValue() != null) {
                    for (int i = 0; i < entry.getValue().size(); i++) {
                        buildernew.addFormDataPart(entry.getKey(), entry.getValue().get(i).getName(), RequestBody.create(MEDIA_TYPE_PNG, entry.getValue().get(i)));
                    }

                }
            }
        }

        RequestBody body = buildernew.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("a_t", AppPreference.getInstance().getAt())
                .addHeader("r_t", AppPreference.getInstance().getRt())
                .addHeader("user_name", AppPreference.getInstance().getUserName())
                .addHeader("email_id", AppPreference.getInstance().getUserID())
                .addHeader("cf_uuhid", AppPreference.getInstance().getcf_uuhidNeew())
                .addHeader("healthRecordDate", prescriptionDate)
                .addHeader("doctorName", doctorName)
                .addHeader("testName", dieaseName)
                .addHeader("imageOrder", removeSyptoms)
                .post(body)
                .build();

        Log.e("request", " " + request.body().toString());

        try

        {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        return null;
    }


    public String postProfile(String url, HashMap<String, String> params, HashMap<String, File> fileParams) throws Exception {
        MultipartBody.Builder buildernew = new MultipartBody.Builder()
                .setType(MultipartBody.FORM);
        if (params != null && params.size() > 0) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                buildernew.addFormDataPart(entry.getKey(), entry.getValue());
            }
        }

        if (fileParams != null && fileParams.size() > 0) {
            for (Map.Entry<String, File> entry : fileParams.entrySet()) {
                if (entry.getValue() != null && entry.getValue().exists()) {
                    buildernew.addFormDataPart(entry.getKey(), entry.getValue().getName(), RequestBody.create(MEDIA_TYPE_PNG, entry.getValue()));
                }
            }
        }

        RequestBody body = buildernew.build();
        Request request = new Request.Builder()
                .url(url)
                .addHeader("a_t", AppPreference.getInstance().getAt())
                .addHeader("r_t", AppPreference.getInstance().getRt())
                .addHeader("user_name", AppPreference.getInstance().getUserName())
                .addHeader("email_id", AppPreference.getInstance().getUserID())
                .addHeader("cf_uuhid", AppPreference.getInstance().getcf_uuhid())
                .post(body)
                .build();

        Log.e("request", " " + request.body().toString());

        try

        {
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful())
                throw new IOException("Unexpected code" + response.toString());
            return response.body().string();
        } catch (
                Exception e
                )

        {
            e.printStackTrace();
        }

        return null;
    }
}