package com.example.pancho.w5.view.mainactivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import com.example.pancho.w5.model.HourlyForecast;
import com.example.pancho.w5.model.HourlyForecastOrdered;
import com.example.pancho.w5.model.HourlyNeeded;
import com.example.pancho.w5.model.Weather;
import com.example.pancho.w5.util.CONSTANTS;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.content.Context.MODE_PRIVATE;
import static com.example.pancho.w5.util.CONSTANTS.EXT_WND;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public class MainActivityPresenter implements com.example.pancho.w5.view.mainactivity.MainActivityContract.Presenter {
    MainActivityContract.View view;
    private static final String TAG = "MainActivityPresenter";
    private Context context;

    @Override
    public void attachView(MainActivityContract.View view) {
        this.view = view;
    }

    @Override
    public void setContext(Context context) {
        this.context = context;
    }

    @Override
    public void detachView() {
        this.view = null;
    }

    @Override
    public void makeRestCall(boolean force) {
        final Date currentTime = Calendar.getInstance().getTime();

        SharedPreferences prefs = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        Long longd = prefs.getLong(CONSTANTS.MY_PREFS_TIME_REST, -1);
        Date old_time;
        if(longd == -1) {
            old_time = currentTime;
        } else {
            old_time = new Date(longd);
        }

        Log.d(TAG, "onResponse: " + currentTime + " " + old_time);

        if(true || (currentTime.compareTo(old_time)>=0)) {
            String zip = prefs.getString(CONSTANTS.MY_PREFS_ZIP, "");
            Log.i(TAG, "ZIPCode: " + zip);
            if(zip.equals("")) {
                Toast.makeText(context, "Empty zip code", Toast.LENGTH_SHORT).show();
                view.InvalidOrNullZip();
            }
            else {
                OkHttpClient client = new OkHttpClient();
                HttpUrl url = new HttpUrl.Builder()
                        .scheme(CONSTANTS.BASE_SCHEMA_WND)
                        .host(CONSTANTS.BASE_URL_WND)
                        .addPathSegments(CONSTANTS.PATH_WND + zip + EXT_WND)
                        .build();
                Request request = new Request.Builder()
                        .url(url)
                        .build();
                Log.i(TAG, "URL: " + url.toString());
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.d(TAG, "onFailure: " + e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Gson gson = new Gson();
                        String r = response.body().string();
                        try {
                            final Weather weather = gson.fromJson(r, Weather.class);

                            //UpdateUI
                            updateUIRest(weather);

                            if (weather.getCurrentObservation() != null) {
                                //Save cache
                                SharedPreferences.Editor editor = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE).edit();
                                Date current_plus10 = new Date(currentTime.getTime() + CONSTANTS.TIME_UNTIL_NEXT_CALL);
                                Log.d(TAG, "onResponse: " + current_plus10);
                                editor.putLong(CONSTANTS.MY_PREFS_TIME_REST, current_plus10.getTime());

                                //Save json
                                editor.putString(CONSTANTS.MY_PREFS_JSON, r);
                                editor.commit();
                            }

                        } catch (Exception ex) {
                            ex.printStackTrace();
                            Log.d(TAG, "onResponse: " + r);
                            Toast.makeText(context, "Failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }
        else {
            Toast.makeText(context, "Data in cache", Toast.LENGTH_SHORT).show();

            //Get Json from cache
            Weather weather = WeatherFromCache();

            //UpdateUI
            updateUIRest(weather);
        }

    }

    private void updateUIRest(final Weather weather) {
        ((MainActivity) view).runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (weather.getCurrentObservation() == null) {
                    Toast.makeText(context, "Invalid zip code", Toast.LENGTH_SHORT).show();
                    view.InvalidOrNullZip();
                } else {
                    view.sendCurrentWeather(weather.getCurrentObservation());
                    OrderHourlyForecast(weather.getHourlyForecast());
                }
            }
        });
    }

    public Weather WeatherFromCache(){
        SharedPreferences prefs = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = prefs.getString(CONSTANTS.MY_PREFS_JSON, null);
        Type type = new TypeToken<Weather>() {
        }.getType();
        Weather weather = gson.fromJson(json, type);

        return weather;
    }

    public void OrderHourlyForecast(List<HourlyForecast> hourlyForecast){
        List<HourlyForecastOrdered> hourlyForecastOrdered_list = new ArrayList<>();

        List<HourlyNeeded> hourlyForecasts_to_order = new ArrayList<>();
        int maxp = 0;
        int minp = 0;
        for (int i = 0; i < hourlyForecast.size(); i++) {
            HourlyNeeded hourlyNeeded = new HourlyNeeded(hourlyForecast.get(i).getFCTTIME().getCivil(),hourlyForecast.get(i).getTemp().getMetric(),hourlyForecast.get(i).getTemp().getEnglish(),CONSTANTS.icons.get(hourlyForecast.get(i).getIcon()));
            hourlyForecasts_to_order.add(hourlyNeeded);
            if(Double.parseDouble(hourlyForecast.get(i).getTemp().getEnglish()) > Double.parseDouble(hourlyForecasts_to_order.get(maxp).getFahrenheit()))
                maxp = hourlyForecasts_to_order.size()-1;
            if(Double.parseDouble(hourlyForecast.get(i).getTemp().getEnglish()) < Double.parseDouble(hourlyForecasts_to_order.get(minp).getFahrenheit()))
                minp = hourlyForecasts_to_order.size()-1;

            if(hourlyForecast.get(i).getFCTTIME().getCivil().equals("11:00 PM")) {
                String label = hourlyForecast.get(i).getFCTTIME().getMonPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getMdayPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getYear() + "-" + hourlyForecast.get(i).getFCTTIME().getWeekdayName();
                hourlyForecastOrdered_list.add(new HourlyForecastOrdered(label, minp, maxp, hourlyForecasts_to_order));
                hourlyForecasts_to_order = new ArrayList<>();
                maxp=0;
                minp=0;
            }


        }

        if(!hourlyForecasts_to_order.isEmpty()){
            int i = hourlyForecast.size()-1;
            String label = hourlyForecast.get(i).getFCTTIME().getMonPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getMdayPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getYear() + "-" + hourlyForecast.get(i).getFCTTIME().getWeekdayName();
            hourlyForecastOrdered_list.add(new HourlyForecastOrdered(label, minp, maxp, hourlyForecasts_to_order));
        }

        hourlyForecastOrdered_list.get(0).setLabel("Today");
        hourlyForecastOrdered_list.get(1).setLabel("Tomorrow");
        view.sendNextWeather(hourlyForecastOrdered_list);
    }
}
