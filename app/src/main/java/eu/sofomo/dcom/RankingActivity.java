package eu.sofomo.dcom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;

import eu.sofomo.dcom.common.Results;

/**
 * Created by gavu on 7/10/15.
 */
public class RankingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.results);

        Results res = new Results(this);


        ArrayList<Results.Entity> resultList = res.readReults();

        Collections.sort(resultList);

        TableLayout ll = (TableLayout) findViewById(R.id.result);


        for (Results.Entity tmpRes : resultList) {

            TableRow row = new TableRow(this);

            TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f);
            params.width = 0;


            TextView t1v = new TextView(this);
            t1v.setText(tmpRes.getUserName());
            t1v.setLayoutParams(params);
            row.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setText(tmpRes.getScore());
            t2v.setLayoutParams(params);
            row.addView(t2v);

            TextView t3v = new TextView(this);
            SimpleDateFormat formatter = new SimpleDateFormat(Results.Entity.DATE_FORMAT);
            String tmpDate = formatter.format(tmpRes.getDate());
            t3v.setText(tmpDate);
            t3v.setLayoutParams(params);
            row.addView(t3v);


            ll.addView(row);
        }

    }
}
