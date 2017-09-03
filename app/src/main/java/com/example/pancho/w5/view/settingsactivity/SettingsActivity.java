package com.example.pancho.w5.view.settingsactivity;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.WindowManager;

import com.example.pancho.w5.R;
import com.example.pancho.w5.injection.settingsactivity.DaggerSettingsActivityComponent;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;


public class SettingsActivity extends AppCompatActivity implements SettingsActivityContract.View {
    private static final String TAG = "SettingsActivity";

    @Inject
    SettingsActivityPresenter presenter;
    @BindView(R.id.toolbar2)
    Toolbar toolbar;
    @BindView(R.id.recycler)
    RecyclerView recycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        ButterKnife.bind(this);

        setupDaggerComponent();
        presenter.attachView(this);

        setToolbarBackPressed();
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().setStatusBarColor(ContextCompat.getColor(this,R.color.gray));
    }

    private void setupDaggerComponent() {
        DaggerSettingsActivityComponent.create().insert(this);
    }

    @Override
    public void showError(String s) {

    }

    private void setToolbarBackPressed(){
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setTitle("Umbrella");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
