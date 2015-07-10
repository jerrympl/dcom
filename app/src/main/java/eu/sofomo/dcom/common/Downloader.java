package eu.sofomo.dcom.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import eu.sofomo.dcom.R;


public class Downloader extends AsyncTask<Void, Void, String>
{
    public static final String AUTH_METHOD = "HMAC-SHA256";
    public static final String API_URL = "http://restapi.lion.dictionary.com";
    private static String API_KEY = "898989898989898";
    private static String API_SECRET = "O6Tjyx8iLCAqdooH";

    private Fragment mFragment;
    private Activity mActivity;
    private String authorizationSum;
    private String apiQueryString;
    private String apiUri;
    private String apiMethod;

    public Downloader()
    {
        super();
    }

    public Downloader(Fragment fragment)
    {
        mFragment = fragment;
    }

    public Downloader(Activity activity)
    {
        mActivity = activity;
    }

    public void setApiQueryString(String apiQueryString) {
        this.apiQueryString = apiQueryString;
    }

    public void setApiUri(String apiUri) {
        this.apiUri = apiUri;
    }

    public void setApiMethod(String apiMethod) {
        this.apiMethod = apiMethod;
    }

    private String generateAuthorizationSum()
    {
        String sum = apiMethod+"+"+apiUri+"+"+apiQueryString;

        return sha256(sum);
    }

    private String getUrl()
    {
        return API_URL+apiUri+"?"+apiQueryString+"&apiKey="+API_KEY;
    }

    @Override
    protected String doInBackground(Void... params)
    {
        HttpURLConnection connection = null;
        String result = null;
        try {
            String u = getUrl();
            URL url = new URL(u);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(70000);
            connection.setReadTimeout(70000);
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Authorization", AUTH_METHOD + " " + generateAuthorizationSum());
            connection.setInstanceFollowRedirects(true);
            result = new String(readFully(connection.getInputStream()));

        } catch (Exception ex) {
            result = ex.toString();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

        return result;
    }

    public static String sha256(String base) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes("UTF-8"));
            StringBuffer hexString = new StringBuffer();

            for (int i = 0; i < hash.length; i++) {
                String hex = Integer.toHexString(0xff & hash[i]);
                if(hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }

    @Override
    protected void onPreExecute()
    {
        ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.loading);
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onPostExecute(String result)
    {
        TextView out = (TextView) mActivity.findViewById(R.id.out);
        out.setText(result);
        Log.e("qqqq", result);

        if(result != null) {
            ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.loading);
            progressBar.setVisibility(View.GONE);
        }
    }


    public  byte[] readFully(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        for (int count; (count = in.read(buffer)) != -1;) {
            out.write(buffer, 0, count);
        }
        return out.toByteArray();
    }
}