package eu.sofomo.dcom;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

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

//        Results.Entity ent = new Results.Entity("te2mdsfp1", "200", "and233");
//        res.writeResult(ent);

        ArrayList<Results.Entity> resultList = res.readReults();

        for(Results.Entity tmpRes : resultList) {
            Log.i("Info res: ", tmpRes.toString());
        }

        TableLayout ll = (TableLayout) findViewById(R.id.result);


        for(Results.Entity tmpRes : resultList) {

            TableRow row = new TableRow(this);

            TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT);
            TableRow.LayoutParams params = new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f);


            TextView t1v = new TextView(this);
            t1v.setLayoutParams(params);
            t1v.setText(tmpRes.getUserName());
            row.addView(t1v);

            TextView t2v = new TextView(this);
            t2v.setLayoutParams(params);
            t2v.setText(tmpRes.getScore());
            row.addView(t2v);

            TextView t3v = new TextView(this);
            t3v.setLayoutParams(params);
            t3v.setText(tmpRes.getDate().toString());
            row.addView(t3v);


            ll.setLayoutParams(lp);
            ll.addView(row);
        }

    }
}
