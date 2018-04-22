package com.example.johitgarg.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView printWeather;
    public void getWeather(View view){
        DownloadData downloadData = new DownloadData();
        String encodedCityName = null;
        try {
            encodedCityName = URLEncoder.encode(editText.getText().toString(),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(MainActivity.this, "Enter an appropriate text", Toast.LENGTH_SHORT).show();
        }
        downloadData.execute("http://api.openweathermap.org/data/2.5/weather?q="+encodedCityName+"&appid=76604764bf114d53671e83adfee611ad");  //Enter the URL of the JSON code and divide it in parts
        InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(editText.getWindowToken(),0);
    }

    public class DownloadData extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... urls) {
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {
                url = new URL(urls[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(inputStream);
                int data = reader.read();

                while (data != -1) {
                    char current = (char) data;
                    result += current;
                    data = reader.read();
                }
                return result;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Enter an appropriate text", Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("JSON Code", s);
            try {
                JSONObject jsonObject = new JSONObject(s);
                String weatherInfo = jsonObject.getString("weather");
                //The name argument inside the method getString() should be same as a JSON object//
                Log.i("Weather Content", weatherInfo);
                JSONArray array = new JSONArray(weatherInfo);
                String message = "";
                for (int i = 0; i < array.length(); i++) {
                    JSONObject jsonPart = array.getJSONObject(i);
                    String main = jsonPart.getString("main");
                    String description = jsonPart.getString("description");
                    if (!(main.equals("")&&!(description.equals("")))){
                        message += main + ": " + description + "\n";
                    }
                }
                if(!(message.equals(""))){
                    printWeather.setText(message);
                } else {
                    Toast.makeText(MainActivity.this, "Enter an appropriate text", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(MainActivity.this, "Enter an appropriate text", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = findViewById(R.id.editText);
        printWeather = findViewById(R.id.printWeather);
    }
}
