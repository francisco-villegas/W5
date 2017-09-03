package com.example.pancho.w5.view.mainactivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.pancho.w5.model.HourlyForecast;
import com.example.pancho.w5.model.HourlyForecastOrdered;
import com.example.pancho.w5.model.Weather;
import com.example.pancho.w5.util.CONSTANTS;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
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
    public void makeRestCall() {
        SharedPreferences prefs = context.getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        String zip = prefs.getString(CONSTANTS.MY_PREFS_ZIP, null);
        zip = "30008";
        OkHttpClient client = new OkHttpClient();
        HttpUrl url = new HttpUrl.Builder()
                .scheme(CONSTANTS.BASE_SCHEMA_WND)
                .host(CONSTANTS.BASE_URL_WND)
                .addPathSegments(CONSTANTS.PATH_WND + zip + EXT_WND)
                .build();
        Log.d(TAG, "getGeocodeAddress: " + url.toString());
        Request request = new Request.Builder()
                .url(url)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(TAG, "onFailure: " + e.toString());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Gson gson = new Gson();
                final Weather weather = gson.fromJson(response.body().string(), Weather.class);

                ((MainActivity) view).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        view.sendCurrentWeather(weather.getCurrentObservation());
                        OrderHourlyForecast(weather.getHourlyForecast());
                    }
                });
            }
        });
    }

    public void OrderHourlyForecast(List<HourlyForecast> hourlyForecast){
        List<HourlyForecastOrdered> hourlyForecastOrdered_list = new ArrayList<>();

        List<HourlyForecast> hourlyForecasts_to_order = new ArrayList<>();
        for (int i = 0; i < hourlyForecast.size(); i++) {
            if(hourlyForecast.get(i).getFCTTIME().getCivil().equals("11:00 PM")) {
                hourlyForecasts_to_order.add(hourlyForecast.get(i));
                String label = hourlyForecast.get(i).getFCTTIME().getMonPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getMdayPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getYear() + "-" + hourlyForecast.get(i).getFCTTIME().getWeekdayName();
                hourlyForecastOrdered_list.add(new HourlyForecastOrdered(label, hourlyForecasts_to_order));
                hourlyForecasts_to_order = new ArrayList<>();
            }
            else
                hourlyForecasts_to_order.add(hourlyForecast.get(i));
        }

        if(!hourlyForecasts_to_order.isEmpty()){
            int i = hourlyForecast.size()-1;
            String label = hourlyForecast.get(i).getFCTTIME().getMonPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getMdayPadded() + "/" + hourlyForecast.get(i).getFCTTIME().getYear() + "-" + hourlyForecast.get(i).getFCTTIME().getWeekdayName();
            hourlyForecastOrdered_list.add(new HourlyForecastOrdered(label, hourlyForecasts_to_order));
        }

        hourlyForecastOrdered_list.get(0).setLabel("Today");
        hourlyForecastOrdered_list.get(1).setLabel("Tomorrow");
        view.sendNextWeather(hourlyForecastOrdered_list);
    }
}
