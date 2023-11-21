package com.example.appsweathers;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.graphics.Color;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.appsweathers.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;




public class MainActivity extends AppCompatActivity {
    ImageView img;
    TextView dayTimeTxt, predictTxt, humidityTxt, temperatureTxt, pressureTxt, statusTxt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dayTimeTxt = findViewById(R.id.dayTime);
        predictTxt = findViewById(R.id.predict);
        humidityTxt = findViewById(R.id.humidity);
        temperatureTxt = findViewById(R.id.temperature);
        pressureTxt = findViewById(R.id.pressure);
        img = findViewById(R.id.imageView1);
        statusTxt = findViewById(R.id.status1);
        GetCurrentWeatherData();

    }
    public void GetCurrentWeatherData(){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        String url = "https://serverweather.azurewebsites.net/api/weatherShow/1";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String day = jsonObject.getString("dayTime");
                            String pred = jsonObject.getString("predict");
                            if("Sunny".equals(pred)){
                                img.setImageResource(R.drawable.cloudy_sunny);
                            } else if ("Rainy".equals(pred)) {
                                img.setImageResource(R.drawable.rainy);
                            } else if ("blazing_sun".equals(pred)) {
                                img.setImageResource(R.drawable.sunny);
                            } else if ("lightly_sunlit".equals(pred)) {
                                img.setImageResource(R.drawable.cloudy);
                            } else if ("pleasantly cool".equals(pred)) {
                                img.setImageResource(R.drawable.snowy);
                            }
                            String humi = jsonObject.getString("humidity");
                            String temp = jsonObject.getString("temp");
                            String pres = jsonObject.getString("pressure");
                            dayTimeTxt.setText(day);
                            Date date=java.util.Calendar.getInstance().getTime();
                            String date1 = date.toString();
                            String[] parts = day.split("\\s+");
                            String[] parts1 = date1.split("\\s+");
                            // convert to Second in database
                            String[] units = parts[1].split(":");
                            int hours = Integer.parseInt(units[0]);
                            int minutes = Integer.parseInt(units[1]);
                            int seconds = Integer.parseInt(units[2]);
                            long duration = hours * 3600 + 60 * minutes + seconds + 60; //add up our values
                            // convert to Second in realtime
                            String[] units1 = parts1[3].split(":");
                            int hours1 = Integer.parseInt(units1[0]);
                            int minutes1 = Integer.parseInt(units1[1]); //first element
                            int seconds1 = Integer.parseInt(units1[2]); //second element
                            long duration1 = hours1 * 3600 + 60 * minutes + seconds; //add up our values
                            // kiem tra online hay off line
                            if(duration > duration1) {
                                statusTxt.setText("Online");
                                statusTxt.setTextColor(Color.parseColor("#00FF00"));
                            }
                            else {
                                statusTxt.setText("Offline");
                                statusTxt.setTextColor(Color.parseColor("#FF0000"));
                            }
                            predictTxt.setText(pred);
                            humidityTxt.setText(humi + "%");
                            temperatureTxt.setText(temp + "°C");
                            pressureTxt.setText(pres + "hPa");

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        requestQueue.add(stringRequest);
    }
}