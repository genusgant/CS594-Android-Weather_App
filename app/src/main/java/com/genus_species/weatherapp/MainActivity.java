package com.genus_species.weatherapp;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class MainActivity extends ActionBarActivity {


    LocationManager locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        showWeather();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showWeather()
    {
        String OPEN_WEATHER_MAP_API;
        Location cur_location = getCurrentLocation();
        OPEN_WEATHER_MAP_API ="http://api.openweathermap.org/data/2.5/forecast/daily?lat="+cur_location.getLatitude()+"&lon="+cur_location.getLongitude()+"&cnt=10&mode=json";
        getJSON(context,OPEN_WEATHER_MAP_API);

    }

    public static JSONObject getJSON(Context context, String OPEN_WEATHER_MAP_API){
        try {
            URL url = new URL(String.format(OPEN_WEATHER_MAP_API));
            HttpURLConnection connection =
                    (HttpURLConnection)url.openConnection();

            connection.addRequestProperty("x-api-key",
                    context.getString(R.string.open_weather_maps_app_id));

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(connection.getInputStream()));

            StringBuffer json = new StringBuffer(1024);
            String tmp="";
            while((tmp=reader.readLine())!=null)
                json.append(tmp).append("\n");
            reader.close();

            JSONObject data = new JSONObject(json.toString());

            // This value will be 404 if the request was not
            // successful
            if(data.getInt("cod") != 200){
                return null;
            }

            return data;
        }catch(Exception e){
            return null;
        }
    }

    public Location getCurrentLocation()
    {
        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        return location;
    }
}
