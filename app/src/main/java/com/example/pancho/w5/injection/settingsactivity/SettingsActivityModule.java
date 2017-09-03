package com.example.pancho.w5.injection.settingsactivity;

import com.example.pancho.w5.view.settingsactivity.SettingsActivityPresenter;

import dagger.Module;
import dagger.Provides;

/**
 * Created by FRANCISCO on 22/08/2017.
 */

@Module
public class SettingsActivityModule {

    @Provides
//    @Singleton this is going to make the class as singleton
    SettingsActivityPresenter providesSettingsActivityPresenter(){

        return new SettingsActivityPresenter();
    }
}
