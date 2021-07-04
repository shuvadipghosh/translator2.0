package com.example.assistance;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class temperature extends AppCompatActivity {

    public final static String MESSAGE_KEY = "ganeshannt.senddata.message_key";
    TextView t1, t2, t3;
    TextView temp_minTxt, temp_maxTxt, sunriseTxt,
            sunsetTxt, windTxt, pressureTxt, humidityTxt, tx1;
    private String address = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        t1 = findViewById(R.id.address);
        t2 = findViewById(R.id.status);
        t3 = findViewById(R.id.temp);
        temp_minTxt = findViewById(R.id.temp_min);
        temp_maxTxt = findViewById(R.id.temp_max);
        sunriseTxt = findViewById(R.id.sunrise);
        sunsetTxt = findViewById(R.id.sunset);
        windTxt = findViewById(R.id.wind);
        pressureTxt = findViewById(R.id.pressure);
        humidityTxt = findViewById(R.id.humidity);
        Intent i = getIntent();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                    WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

            try {
                Bundle extras = i.getExtras();
                String json = extras.getString("JSON");
                address = extras.getString("address");
                t1.setText(address);
                dataextraction(json);
            } catch (Exception e) {
                Toast.makeText(temperature.this, "Loading please go to the previous page", Toast.LENGTH_SHORT).show();
            }

        }
    }


    public  void dataextraction(String json){

        try {
            JSONObject jsonObj = new JSONObject(json);
            JSONObject main = jsonObj.getJSONObject("main");
            JSONObject sys = jsonObj.getJSONObject("sys");
            JSONObject wind = jsonObj.getJSONObject("wind");
            JSONObject weather = jsonObj.getJSONArray("weather").getJSONObject(0);

            Long updatedAt = jsonObj.getLong("dt");
            String updatedAtText = "Updated at: " + new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.ENGLISH).format(new Date(updatedAt * 1000));
            String temp = main.getString("temp");
           double t=Double.parseDouble(temp);
            t=t-273;
            temp=""+t;
            temp=temp.charAt(0)+""+temp.charAt(1)+"°C";
            String tempMin = main.getString("temp_min");
            double tmin=Double.parseDouble(tempMin);
            tmin=tmin-273;
            tempMin=""+tmin;
            tempMin="Min temp : "+tempMin.charAt(0)+""+tempMin.charAt(1)+"°C";
            String tempMax = main.getString("temp_max");
            double tmax=Double.parseDouble(tempMax);
            tmax=tmax-273;
            tempMax=""+tmax;
            tempMax="Max temp : "+tempMax.charAt(0)+""+temp.charAt(1)+"°C";
            String pressure = main.getString("pressure");
            String humidity = main.getString("humidity");

            Long sunrise = sys.getLong("sunrise");
            Long sunset = sys.getLong("sunset");
            String windSpeed = wind.getString("speed");
            String weatherDescription = weather.getString("description");

            String address = jsonObj.getString("name") + ", " + sys.getString("country");


            /* Populating extracted data into our views */
           // t1.setText(address);
           // updated_atTxt.setText(updatedAtText);
            t2.setText(weatherDescription.toUpperCase());
            t3.setText(temp);
            temp_minTxt.setText(tempMin);
           temp_maxTxt.setText(tempMax);
            sunriseTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunrise * 1000)));
            sunsetTxt.setText(new SimpleDateFormat("hh:mm a", Locale.ENGLISH).format(new Date(sunset * 1000)));
            windTxt.setText(windSpeed);
            pressureTxt.setText(pressure);
            humidityTxt.setText(humidity);

            /* Views populated, Hiding the loader, Showing the main design */
            findViewById(R.id.loader).setVisibility(View.GONE);
            findViewById(R.id.mainContainer).setVisibility(View.VISIBLE);


        } catch (JSONException e) {
            findViewById(R.id.loader).setVisibility(View.GONE);
            findViewById(R.id.errorText).setVisibility(View.VISIBLE);
        }
    }
}