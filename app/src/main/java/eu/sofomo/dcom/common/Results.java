package eu.sofomo.dcom.common;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by gavu on 7/10/15.
 */
public class Results {

    private Activity mActivity;

    public Results(Activity activity) {
        mActivity = activity;
    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(mActivity.getApplicationContext().openFileOutput("results.txt", Context.MODE_APPEND));
            outputStreamWriter.write(data + System.getProperty("line.separator"));
            outputStreamWriter.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }

    }


    private String readFromFile() {

        String ret = "";

        String path = mActivity.getApplicationContext().getFilesDir().getAbsolutePath();
        File file = new File(path + "/results.txt");
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        InputStream inputStream = null;
        try {
//            InputStream inputStream = mActivity.getApplicationContext().openFileInput("results.txt");
            inputStream = new BufferedInputStream(new FileInputStream(file));

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                    stringBuilder.append(System.getProperty("line.separator"));
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }

    public void writeResult(Entity result) {
        SimpleDateFormat formatter = new SimpleDateFormat(Entity.DATE_FORMAT);
        String tmpDate = formatter.format(result.getDate());
        this.writeToFile(result.getUserName() + ";" + result.getScore() + ";" + tmpDate + ";" + result.getDeviceName());
    }

    public ArrayList<Entity> readReults() {
        ArrayList<Entity> resultsList = new ArrayList<>();

        String resultsString = this.readFromFile();

        String lines[] = resultsString.split("\\r?\\n");

        for (String line : lines) {

            String[] resultTmp = line.split(";");
            Entity tmpEntity = new Entity(resultTmp[0], resultTmp[1], resultTmp[3], resultTmp[2]);

            resultsList.add(tmpEntity);
        }

        return resultsList;
    }


    public static class Entity implements Comparable {

        public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

        public Entity(String userName, String score, String deviceName) {
            setUserName(userName);
            setDeviceName(deviceName);
            setScore(score);
            setDate(new Date());
        }

        public Entity(String userName, String score, String deviceName, String date) {
            setUserName(userName);
            setDeviceName(deviceName);
            setScore(score);

            SimpleDateFormat ft = new SimpleDateFormat(DATE_FORMAT);
            Date tmpDate = null;
            try {
                tmpDate = ft.parse(date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            setDate(tmpDate);
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getScore() {
            return score;
        }

        public void setScore(String score) {
            this.score = score;
        }

        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getDeviceName() {
            return deviceName;
        }

        public void setDeviceName(String deviceName) {
            this.deviceName = deviceName;
        }

        private String userName;
        private String score;
        private Date date;
        private String deviceName;


        @Override
        public int compareTo(Object another) {
            Entity entity = (Entity) another;
            int entResult = Integer.parseInt(entity.getScore());

            int curResult = Integer.parseInt(this.getScore());
            return entResult - curResult;
        }
    }


}
