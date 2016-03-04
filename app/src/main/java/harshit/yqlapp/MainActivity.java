package harshit.yqlapp;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
public static final String Search_URL="https://query.yahooapis.com/v1/public/yql?q=select%20*%20from%20local.search%20where%20zip%3D'94085'%20and%20query%3D'pizza'&format=json&callback=";

    ListView list;
    ArrayList<String> pizzalocations;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list=(ListView)findViewById(R.id.listView);
        pizzalocations=new ArrayList<String>();

        DownloadJSON search=new DownloadJSON();
        search.execute();
    }
    public class DownloadJSON extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... params) {

            try {
                URL theurl=new URL(Search_URL);
                try {
                    BufferedReader reader=new BufferedReader(new InputStreamReader(theurl.openConnection().getInputStream(),"UTF-8"));
                    String jsonString=reader.readLine();
                    Log.d("JSON",jsonString);

                    JSONObject jsonobject=new JSONObject(jsonString);
                    JSONObject queryObj=new JSONObject(jsonobject.getString("query"));
                    JSONObject resultsObj=new JSONObject(queryObj.getString("results"));
                    JSONArray resultArray=new JSONArray(resultsObj.getString("Result"));
                    for(int i=0;i<resultArray.length();i++){
                        JSONObject theObj=resultArray.getJSONObject(i);
                        Log.d("Arrayslist",theObj.getString("Title"));
                        pizzalocations.add(theObj.getString("Title"));
                    }



                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } catch (MalformedURLException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            ArrayAdapter adapter=new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,pizzalocations);
            list.setAdapter(adapter);


        }
    }



}
