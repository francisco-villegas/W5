package com.example.pancho.w5.view.mainactivity;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.pancho.w5.R;
import com.example.pancho.w5.injection.mainactivity.DaggerMainActivityComponent;
import com.example.pancho.w5.model.CurrentObservation;
import com.example.pancho.w5.model.HourlyForecastOrdered;
import com.example.pancho.w5.util.CONSTANTS;
import com.example.pancho.w5.view.settingsactivity.SettingsActivity;

import java.util.HashMap;
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
    @BindView(R.id.toolbar_header_view)
    LinearLayout toolbarHeaderView;

    private Toolbar myToolbar;
    private FirstAdapter firstAdapter;

    private CurrentObservation currentObservation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("");

        myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        ButterKnife.bind(this);

        setupDaggerComponent();
        presenter.attachView(this);
        presenter.setContext(getApplicationContext());

        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.gray));

        presenter.makeRestCall(false);
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
                this.startActivityForResult(intent, CONSTANTS.RESULT_BACK);
                break;
        }
        return true;
    }

    @Override
    public void sendCurrentWeather(CurrentObservation currentObservation) {
        this.currentObservation = currentObservation;
        tvConditionTop.setText(currentObservation.getWeather());
        setTitle(currentObservation.getDisplayLocation().getFull());

        SharedPreferences prefs = getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        String unit = prefs.getString(CONSTANTS.MY_PREFS_UNITS, "Fahrenheit");
        changeColor(currentObservation, unit);
    }

    private void changeColor(CurrentObservation currentObservation, String unit) {
        if (unit.equals("Celsius")) {
            tvTemperatureTop.setText(currentObservation.getTempC() + "°C");
        } else {
            tvTemperatureTop.setText(currentObservation.getTempF() + "°F");
        }

        if (Double.parseDouble(currentObservation.getTempF().toString()) > 60) {
            myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), CONSTANTS.max_color));
            toolbarHeaderView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), CONSTANTS.max_color));
        } else {
            myToolbar.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), CONSTANTS.min_color));
            toolbarHeaderView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), CONSTANTS.min_color));
        }
    }

    @Override
    public void sendNextWeather(List<HourlyForecastOrdered> hourlyForecastOrdered) {
        layoutManager = new LinearLayoutManager(getApplicationContext());
        itemAnimator = new DefaultItemAnimator();
        recycler.setLayoutManager(layoutManager);
        recycler.setItemAnimator(itemAnimator);
        recycler.setHasFixedSize(true);
        recycler.setItemViewCacheSize(20);
        recycler.setDrawingCacheEnabled(true);
        recycler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

        SharedPreferences prefs = getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
        String unit = prefs.getString(CONSTANTS.MY_PREFS_UNITS, "Fahrenheit");
        firstAdapter = new FirstAdapter(hourlyForecastOrdered);
        firstAdapter.setUnits(unit);
        recycler.setAdapter(firstAdapter);
        firstAdapter.notifyDataSetChanged();
    }

    @Override
    public void InvalidOrNullZip() {
        Intent intent = new Intent(this, SettingsActivity.class);
        intent.setAction("forceZip");
        this.startActivityForResult(intent, CONSTANTS.RESULT_BACK);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case CONSTANTS.RESULT_BACK:
                SharedPreferences prefs = getSharedPreferences(CONSTANTS.MY_PREFS, MODE_PRIVATE);
                String zip = prefs.getString(CONSTANTS.MY_PREFS_ZIP, "");
                if (zip.equals("")) {
//                    Toast.makeText(this, "Empty zip code", Toast.LENGTH_SHORT).show();
//                    InvalidOrNullZip();
                    finish();
                } else {
                    HashMap<String, String> changes = (HashMap<String, String>) data.getSerializableExtra(CONSTANTS.RESULT_BACK_VALUE);
                    String zip_changes = changes.get(CONSTANTS.MY_PREFS_ZIP);
                    String unit_changes = changes.get(CONSTANTS.MY_PREFS_UNITS);
                    if (zip_changes != null && !zip_changes.equals(""))
                        presenter.makeRestCall(true);
                    else if (unit_changes != null && !unit_changes.equals("")) {
                        firstAdapter.setUnits(unit_changes);
                        firstAdapter.notifyDataSetChanged();
                        changeColor(currentObservation, unit_changes);
                    }
                }
                break;
        }
    }
}
