package com.example.lh.step20ex3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private RequestQueue mQueue;
    private LinearLayout mLinearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLinearLayout =  (LinearLayout)findViewById(R.id.myLinearLayout);
        mQueue = Volley.newRequestQueue(getApplicationContext());

        loadWeatherForecast();
    }

    private void loadWeatherForecast() {
        Log.d(TAG, "loadWeatherForecast");
        mLinearLayout.removeAllViews();

        // connect to
        //     replace the "AAAAAAA" to your KEY
        String url = "http://api.wunderground.com/api/AAAAAAA/forecast10day/lang:JP/q/Japan/Tokyo.json";

        // insert request into queues
        mQueue.add(new JsonObjectRequest(Request.Method.GET, url, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    JSONArray getWeather = response.getJSONObject("forecast")
                            .getJSONObject("txt_forecast").getJSONArray("forecastday");
                    for (int i = 0; i < getWeather.length(); i++) {
                        JSONObject weather = getWeather.getJSONObject(i);
                        String day = weather.getString("title");
                        String myWeather = weather.getString("fcttext_metric");

                        Log.d(TAG, day + ": " + myWeather);
                        addItem(day, myWeather);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, error.toString());
            }
        }));
    }

//    private void showTxt (String day, String weather) {
//        TextView textView = (TextView)findViewById(R.id.txtView);
//        textView.setText(String.format("the weather in %s is %s", day, weather));
//    }

    private void addItem(String day, String weather) {
        TextView item = (TextView)getLayoutInflater().inflate(R.layout.weather_cast, null, false);

        item.setText(String.format("時間: %s\n 天気: %s\n", day, weather));

        LinearLayout.LayoutParams params =
                new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT);
        mLinearLayout.addView(item,params);
    }
}
