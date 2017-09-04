package com.example.pancho.w5.view.mainactivity;

import android.content.Context;

import com.example.pancho.w5.BasePresenter;
import com.example.pancho.w5.BaseView;
import com.example.pancho.w5.model.CurrentObservation;
import com.example.pancho.w5.model.HourlyForecast;
import com.example.pancho.w5.model.HourlyForecastOrdered;

import java.util.List;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public interface MainActivityContract {

    interface View extends BaseView {
        void sendCurrentWeather(CurrentObservation weather);

        void sendNextWeather(List<HourlyForecastOrdered> hourlyForecastOrdered);

        void InvalidOrNullZip();
    }

    interface Presenter extends BasePresenter<View>{
        void setContext(Context context);

        void makeRestCall(boolean force);
    }
}
