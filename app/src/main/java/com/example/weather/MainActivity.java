package com.example.weather;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView result;
    EditText input;
    Button show;
    String failedMsg = "Ooooooooops!!!! \nCheck spell and mind the spaces!";
    public void getWeather(View view){
        String city = input.getText().toString();
        try {
            Weather task = new Weather();
            String encodedCityName = URLEncoder.encode(city,"UTF-8");
            task.execute("https://openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager ip = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            ip.hideSoftInputFromWindow(input.getWindowToken(), 0);
        }catch(Exception e){
            result.setText(failedMsg);
            Toast.makeText(this,"Can't show weather,Check input again!",Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }
    public class Weather extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... urls) {
            try{
                URL url = new URL(urls[0]);
                HttpURLConnection con = (HttpURLConnection) url.openConnection();
                con.connect();
                InputStream inp = con.getInputStream();
                InputStreamReader reader = new InputStreamReader(inp);
                int data = reader.read();
                char c;
                StringBuilder builder = new StringBuilder();
                while(data !=-1){
                    c = (char)data;
                    builder.append(c);
                    data = reader.read();
                }

                return builder.toString();

            }catch(Exception e){
                e.printStackTrace();
                return null;
            }

        }
        @Override
        public void onPostExecute(String s){
            try {
                JSONObject obj = new JSONObject(s);
                String weather = obj.getString("weather");

                JSONArray details = new JSONArray(weather);
                String main="",desc="";

                for(int i=0;i<details.length();i++){
                   JSONObject tmp = details.getJSONObject(i);
                   main = tmp.getString("main");
                   desc = tmp.getString("description");
                }
                /*for getting the name of country */
                String place = obj.getString("sys");
                JSONObject info = new JSONObject(place);
                String country = info.getString("country");
                String nameOfPlace = obj.getString("name");


                Log.i("Main==",main);
                Log.i("Desc===",desc);
                String text = "Place: "+nameOfPlace+" ,"+country+"\nMain : "+main.toUpperCase()+"\ndescription : "+desc.toUpperCase();
                result.setText(text );
            }catch(Exception e){
                result.setText(failedMsg);
                Toast.makeText(MainActivity.this,"Can't show weather",Toast.LENGTH_SHORT).show();
                e.printStackTrace();
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        result = findViewById(R.id.result);
        input = findViewById(R.id.input);
        show = findViewById(R.id.show) ;


    }
}
