package eu.sofomo.dcom.common;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import eu.sofomo.dcom.R;

/**
 * Created by jarek on 10.07.15.
 */
public class Word extends Downloader
{
    private Activity mActivity;
    public Word(String word, Activity activity)
    {
        super.setWord(word);
        mActivity = activity;

        setApiMethod("GET");
        setApiUri("/v3/words.json/"+word);
        setApiQueryString("include=dictionaryData");

    }

    @Override
    protected void onPostExecute(String result)
    {
        TextView question = (TextView) mActivity.findViewById(R.id.questionField);
        Button submit = (Button) mActivity.findViewById(R.id.submitAnswer);
        Log.i("json", result);

        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(result);
            JSONObject jsonMainNode = jsonResponse
                    .optJSONObject("dcomApiResponse").optJSONObject("responseBody");
            JSONArray definitions = jsonMainNode.getJSONArray("definitions");
            JSONObject definitionContent = definitions.getJSONObject(1);
//            if(definitions.get(0) != null) {
//                CurrentQuestion currentQuestion = new CurrentQuestion();
//
//                Log.i("json definitions", definitions.getString(0));
//            }
            int lengthJsonArr = jsonMainNode.length();

        } catch (JSONException e) {
            e.printStackTrace();
        }




        question.setText("sample data downloaded");

    }

    @Override
    protected void onPreExecute()
    {

    }

}
