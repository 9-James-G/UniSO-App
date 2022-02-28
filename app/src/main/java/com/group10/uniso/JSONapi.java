package com.group10.uniso;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.os.Bundle;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class JSONapi extends AppCompatActivity {
    private static String JSON_URL = "https://run.mocky.io/v3/06376b15-4539-4bd5-9473-8456ca87c4b3";
    List<DataFromAPI> dataFromJson;
    RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jsonapi);
        dataFromJson = new ArrayList<>();
        recyclerView = findViewById(R.id.jsonRecyclerView);


        GetData getData = new GetData();
        getData.execute();


    }
    public class GetData extends AsyncTask<String , String ,String>{

        @Override
        protected String doInBackground(String... String ){
            String current ="";
            try{
                URL url;
                HttpURLConnection urlConnection = null;
                try{
                    url = new URL(JSON_URL);
                    urlConnection = (HttpURLConnection)  url.openConnection();

                    InputStream is = urlConnection.getInputStream();
                    InputStreamReader isr = new InputStreamReader(is);
                    int data = isr.read();
                    while(data!=-1){
                        current += (char) data;
                        data = isr.read();
                    }
                    return current;

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }finally{
                    if(urlConnection !=null){
                        urlConnection.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return current;
        }

        @Override
        protected void onPostExecute(String s) {
            try{
                JSONObject jsonObject = new JSONObject(s);
                JSONArray jsonArray = jsonObject.getJSONArray("developerz");
                for(int  i = 0; i <jsonArray.length(); i++){

                    JSONObject jsonObject1= jsonArray.getJSONObject(i);
                    DataFromAPI dataFromAPI= new DataFromAPI();
                    dataFromAPI.setId(jsonObject1.getString("contact"));
                    dataFromAPI.setName(jsonObject1.getString("name"));
                    dataFromAPI.setImage(jsonObject1.getString("image"));

                    dataFromJson.add(dataFromAPI);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            PutDataRecyclerView(dataFromJson);
        }
    }
    private void PutDataRecyclerView(List<DataFromAPI>dataFromJson){
        MyAPIAdapter adapter = new MyAPIAdapter(this,dataFromJson);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);
    }
}