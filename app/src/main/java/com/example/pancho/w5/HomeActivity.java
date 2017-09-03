package com.example.pancho.w5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.crashlytics.android.Crashlytics;
import com.crashlytics.android.answers.ContentViewEvent;
import com.crashlytics.android.answers.CustomEvent;
import com.example.pancho.w5.view.mainactivity.MainActivity;
import com.flurry.android.FlurryAgent;
import com.crashlytics.android.answers.Answers;
import io.fabric.sdk.android.Fabric;


public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String FLURRY_API_KEY = "67DXWDX6YXNZDMW6HXMY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Answers(), new Crashlytics());
        setContentView(R.layout.activity_home);

        new FlurryAgent.Builder()
                .withLogEnabled(false)
                .build(this, FLURRY_API_KEY);

        // TODO: Use your own attributes to track content views in your app
        Answers.getInstance().logContentView(new ContentViewEvent()
                .putContentName("Tweet")
                .putContentType("Video")
                .putContentId("1234")
                .putCustomAttribute("Favorites Count", 20)
                .putCustomAttribute("Screen Orientation", "Landscape"));

    }

    public void onKeyMetric(View view) {
        // TODO: Use your own string attributes to track common values over time
        // TODO: Use your own number attributes to track median value over time
        Answers.getInstance().logCustom(new CustomEvent("Video Played")
                .putCustomAttribute("Category", "Comedy")
                .putCustomAttribute("Length", 350));
    }

    public void GoToSecond(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
