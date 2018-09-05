package com.nirwal.epoint.services;

import android.net.http.HttpResponseCache;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HtmlDownloadTask extends AsyncTask<String,Integer,String> {

    String file_name ="aaaa.txt";
    int total;
    InputStream inputStream;
    FileOutputStream fileOutputStream;
    HttpURLConnection connection;

    @Override
    protected String doInBackground(String... strings) {
        Log.v("Task:", "Download started");


        try {
            URL url = new URL(strings[0]);
            connection = (HttpURLConnection) url.openConnection();
            if(connection.getResponseCode() != HttpURLConnection.HTTP_OK){
                return "Error "+connection.getResponseCode() +" "+connection.getResponseMessage();
            }

            inputStream=connection.getInputStream();
            fileOutputStream = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() +
             "/www/"+ file_name);

            int fileLenght = connection.getContentLength();
            byte buffer[] = new byte[4096];
            int count;

            while((count = inputStream.read(buffer)) != 1){
                if(isCancelled())return null;

                total = count;
                if(fileLenght>0){
                    publishProgress(total*100/fileLenght);
                }
                fileOutputStream.write(buffer,0 ,count);

                Log.v("Task:", "Doenload complete");
            }


        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(inputStream!=null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if(fileOutputStream!=null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            connection.disconnect();
        }
        return null;
    }


    @Override
    protected void onProgressUpdate(Integer... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }

}
