package eu.sofomo.dcom.common;

import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

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

    }

    @Override
    protected void onPreExecute()
    {

    }

}
