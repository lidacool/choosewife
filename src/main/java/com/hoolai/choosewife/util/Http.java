package com.hoolai.choosewife.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Http {

    public static String get(String url, Map<String, Object> params) {
        try {
            URIBuilder uriBuilder = new URIBuilder(url);

            if (params != null && !params.isEmpty()) {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                for (Entry<String, Object> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue() == null ? "" : entry.getValue().toString()));
                }
                uriBuilder.addParameters(nameValuePairs);
            }
            return exec0(new HttpGet(uriBuilder.build()));
        } catch (URISyntaxException | IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    public static String post(String url, Map<String, Object> params) {
        try {
            HttpPost post = new HttpPost(url);
            if (params != null && !params.isEmpty()) {
                List<NameValuePair> nameValuePairs = new ArrayList<>();
                for (Entry<String, Object> entry : params.entrySet()) {
                    nameValuePairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue().toString()));
                }
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(nameValuePairs, "UTF-8");
                post.setEntity(entity);
            }
            return exec0(post);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (RuntimeException e) {
            throw e;
        }
    }

    private static String exec0(HttpRequestBase base) throws IOException {
        HttpClientBuilder builder = HttpClientBuilder.create();
        CloseableHttpResponse response = builder.build().execute(base);
        if (response.getStatusLine().getStatusCode() == 200) {
            String s = EntityUtils.toString(response.getEntity());
            JSONObject json = (JSONObject) JSON.parse(s);
            if (json.containsKey("success") && json.getBoolean("success")) {
                return json.getString("data");
            }
            JSONObject err = json.getJSONObject("data");
            if (err.containsKey("code")) {
                throw new RuntimeException(err.getIntValue("code") + "");
            }
            throw new RuntimeException(err.getString("message"));
        }
        throw new RuntimeException("请求错误 url:" + base.getURI() + ", httpStatusCode:" + response.getStatusLine().getStatusCode());
    }
}
