package com.example.pancho.w5.view.mainactivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.pancho.w5.R;
import com.example.pancho.w5.injection.mainactivity.DaggerMainActivityComponent;
import com.example.pancho.w5.model.CurrentObservation;
import com.example.pancho.w5.model.HourlyForecast;
import com.example.pancho.w5.model.HourlyForecastOrdered;
import com.example.pancho.w5.view.settingsactivity.SettingsActivity;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity implements MainActivityContract.View {
    private static final String TAG = "MainActivity";

    @Inject
    MainActivityPresenter presenter;
    @BindView(R.id.tvTemperatureTop)
    TextView tvTemperatureTop;
    @BindView(R.id.tvConditionTop)
    TextView tvConditionTop;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    RecyclerView.LayoutManager layoutManager;
    RecyclerView.ItemAnimator itemAnimator;

    private Toolbar myToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ButterKnife.bind(this);

        setupDaggerComponent();
        presenter.attachView(this);
        presenter.setContext(getApplicationContext());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray));

        presenter.makeRestCall();
    }

    private void setupDaggerComponent() {
        DaggerMainActivityComponent.create().insert(this);
    }

    @Override
    public void showError(String s) {

    }

    //create action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    //options for action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings_sub:
                Intent intent = new Intent(this, SettingsActivity.class);
                startActivity(intent);
                break;
        }
        return true;
    }

    @Override
    public void sendCurrentWeather(CurrentObservation currentObservation) {
        tvTemperatureTop.setText(currentObservation.getTempF() + "°F");
        tvConditionTop.setText(currentObservation.getWeather());
        setTitle(currentObservation.getDisplayLocation().getFull());
    }

    @Override
    public void sendNextWeather(List<HourlyForecastOrdered> hourlyForecastOrdered) {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemAnimator = new DefaultItemAnimator();
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(itemAnimator);

        FirstAdapter firstAdapter = new FirstAdapter(hourlyForecastOrdered);
        recycler.setAdapter(firstAdapter);
        firstAdapter.notifyDataSetChanged();
    }

}
