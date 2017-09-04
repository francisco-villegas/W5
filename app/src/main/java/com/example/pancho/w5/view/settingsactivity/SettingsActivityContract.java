package com.example.pancho.w5.view.settingsactivity;

import android.content.Context;

import com.example.pancho.w5.BasePresenter;
import com.example.pancho.w5.BaseView;
import com.example.pancho.w5.model.Settings;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

public interface SettingsActivityContract {

    interface View extends BaseView {

        void sendMenu(List<Settings> settingsList);
    }

    interface Presenter extends BasePresenter<View>{
        void getMenu();
        void setContext(Context context);
    }
}
