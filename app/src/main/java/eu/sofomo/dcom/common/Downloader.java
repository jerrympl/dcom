package eu.sofomo.dcom.common;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Base64;
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
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SignatureException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import eu.sofomo.dcom.R;


public class Downloader extends AsyncTask<Void, Void, String>
{
    public static final String AUTH_METHOD = "HMAC-SHA256";
    public static final String API_URL = "http://restapi.lion.dictionary.com";
    private static String API_KEY = "898989898989898";
    private static String API_SECRET = "O6Tjyx8iLCAqdooH";
    private static final String HASH_ALGORITHM = "HmacSHA256";

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
        String sum = apiMethod+"+"+apiUri+"+"+apiQueryString+"&apiKey="+API_KEY;

        String secret = API_SECRET;

        Log.i("Info apiMethod: ", apiMethod);
        Log.i("Info apiURI: ", apiUri);
        Log.i("Info apiQueryString: ", apiQueryString);
        Log.i("Info sum: ", sum);
        Log.i("Info secret: ", secret);

        try {
            String hmac = hashMac(sum, secret);
            Log.i("hmac: ", hmac);
            return hmac;
        } catch (SignatureException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Encryption of a given text using the provided secretKey
     *
     * @param text
     * @param secretKey
     * @return the encoded string
     * @throws SignatureException
     */
    public static String hashMac(String text, String secretKey)
            throws SignatureException {

        try {
            Key sk = new SecretKeySpec(secretKey.getBytes(), HASH_ALGORITHM);
            Mac mac = Mac.getInstance(sk.getAlgorithm());
            mac.init(sk);
            final byte[] hmac = mac.doFinal(text.getBytes());
            return byteArrayToHex(hmac);
        } catch (NoSuchAlgorithmException e1) {
            // throw an exception or pick a different encryption method
            throw new SignatureException(
                    "error building signature, no such algorithm in device "
                            + HASH_ALGORITHM);
        } catch (InvalidKeyException e) {
            throw new SignatureException(
                    "error building signature, invalid key " + HASH_ALGORITHM);
        }
    }

    public static String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b & 0xff));
        return sb.toString();
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
            Log.e("error code: ", connection.getResponseCode()+"");
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
        Log.e("Resutl error: ", result);

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