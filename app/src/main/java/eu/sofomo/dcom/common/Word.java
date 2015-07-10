package eu.sofomo.dcom.common;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import eu.sofomo.dcom.QuestionsActivity;
import eu.sofomo.dcom.R;
import eu.sofomo.dcom.RankingActivity;

/**
 * Created by jarek on 10.07.15.
 */
public class Word extends Downloader
{
    public static final String USER_POINTS = "userPoints";
    private Activity mActivity;
    private String word;
    public Word(String word, Activity activity)
    {
        super.setWord(word);
        mActivity = activity;

        setApiMethod("GET");
        setApiUri("/v3/words.json/" + word);
        setApiQueryString("include=dictionaryData");
        this.word = word;

    }

    @Override
    protected void onPostExecute(String result)
    {
        TextView question = (TextView) mActivity.findViewById(R.id.questionField);
        EditText userAnswerField = (EditText) mActivity.findViewById(R.id.userAnswer);
        Button submit = (Button) mActivity.findViewById(R.id.submitAnswer);
        Log.i("json", result);


        final SharedPreferences points = mActivity.getSharedPreferences("points", Context.MODE_PRIVATE);


        JSONObject jsonResponse = null;
        try {
            jsonResponse = new JSONObject(result);
            JSONObject jsonMainNode = jsonResponse
                    .optJSONObject("dcomApiResponse").optJSONObject("responseBody");
            JSONArray responseBody = jsonMainNode.getJSONArray("dictionaryData");
            JSONObject dictionaryData = responseBody.getJSONObject(0);

            JSONArray posBlocks = dictionaryData.getJSONArray("posBlocks");
            JSONObject definitionsArray = posBlocks.getJSONObject(0);
            JSONArray definition = definitionsArray.getJSONArray("definitions");
            JSONObject firstDefinition = definition.getJSONObject(0);
            String properDefinition = firstDefinition.optString("content");
            final CurrentQuestion currentQuestion = new CurrentQuestion();
            currentQuestion.setQuestion(properDefinition);
            currentQuestion.setAnwser(word);

            ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);

            question.setText(Html.fromHtml(properDefinition));


            submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EditText userAnswerField = (EditText) mActivity.findViewById(R.id.userAnswer);

                    if(TextUtils.isEmpty(userAnswerField.getText())) {
                        Toast t = Toast.makeText(mActivity, "Please type your answer...", Toast.LENGTH_LONG);
                        t.show();
                        return;
                    }

                    if(userAnswerField.getText().toString().equals(currentQuestion.getAnwser())) {
                        Toast t = Toast.makeText(mActivity, "Success!", Toast.LENGTH_LONG);
                        t.show();
                        SharedPreferences.Editor editor = points.edit();
                        int currentUserPoints = points.getInt(USER_POINTS, 0);
                        editor.putInt(USER_POINTS, currentUserPoints + currentQuestion.getPoints());

                        // Commit the edits!
                        editor.commit();

                    } else {
                        Toast t = Toast.makeText(mActivity, "Failure", Toast.LENGTH_LONG);
                        t.show();
                    }
                    try {
                        Intent intent = new Intent(mActivity, QuestionsActivity.class);
                        Bundle extras = mActivity.getIntent().getExtras();
                        int wordIndex = 0;
                        if (extras != null) {
                            wordIndex = extras.getInt("wordIndex");
                        }
                        if(wordIndex >= 2) {
                            //save result
                            SharedPreferences.Editor editor = points.edit();
                            final int currentUserPoints = points.getInt(USER_POINTS, 0);
                            new AlertDialog.Builder(mActivity)
                                    .setTitle("Summary")
                                    .setMessage("Congratulations! You have earned "+currentUserPoints+" points.")
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int which) {
                                            // continue with delete
                                            Results.Entity ent = new Results.Entity("tmp", Integer.toString(currentUserPoints), "devName");
                                            Results results = new Results(mActivity);

                                            results.writeResult(ent);

                                            dialog.dismiss();
                                            Intent intent = new Intent(mActivity, RankingActivity.class);
                                            mActivity.startActivity(intent);

                                        }
                                    })

                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                            return;
                        }
                        intent.putExtra("wordIndex", ++wordIndex);
                        mActivity.startActivity(intent);
                    } catch(Exception e)
                    {

                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    protected void onPreExecute()
    {
        ProgressBar progressBar = (ProgressBar) mActivity.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.VISIBLE);
    }

}
