package com.hudzah.wearamask;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.parse.ParseInstallation;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private ViewPager sliderViewPager;
    private LinearLayout dotsLayout;

    private TextView[] dots;

    private SliderAdapter sliderAdapter;

    private Button nextButton;
    private Button backButton;
    private Button skipButton;

    private int currentPage;
    private boolean firstTime;

    private SharedPreferences sharedPreferences;

    private ConstraintLayout layout;

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences(getPackageName(), Context.MODE_PRIVATE);

        layout = (ConstraintLayout) findViewById(R.id.layout);

        firstTime = sharedPreferences.getBoolean("firstTime", true);
        Log.d(TAG, "onCreate: firstTime is " + firstTime);
        if(!firstTime){
            if(ParseUser.getCurrentUser() != null){
                goToMaps();
            }

            else{
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                this.overridePendingTransition(0, 0);

            }
        }


        ParseInstallation.getCurrentInstallation().saveInBackground();
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        // TODO: 8/6/2020 FIX GOOGLE SIGN IN


        sliderViewPager = (ViewPager) findViewById(R.id.slideViewPager);
        dotsLayout = (LinearLayout) findViewById(R.id.dotsLayout);

        nextButton = (Button) findViewById(R.id.nextButton);
        backButton =(Button) findViewById(R.id.backButton);
        skipButton = (Button) findViewById(R.id.skipButton);

        sliderAdapter = new SliderAdapter(this);


        sliderViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        sliderViewPager.addOnPageChangeListener(viewListener);

        nextButton.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 sliderViewPager.setCurrentItem(currentPage + 1);
                 if(nextButton.getText() == getResources().getString(R.string.onboarding_finish)){
                     goToMaps();
                 }
             }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sliderViewPager.setCurrentItem(currentPage - 1);
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: In here");
                goToMaps();
            }
        });

    }

    public void addDotsIndicator(int position){

        dots = new TextView[3];
        dotsLayout.removeAllViews();

        for(int i = 0; i < dots.length; i++){
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(getResources().getColor(R.color.colorPrimaryDark));

            dotsLayout.addView(dots[i]);
        }

        if(dots.length > 0){

            dots[position].setTextColor(getResources().getColor(R.color.light_blue));
        }
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndicator(position);
            currentPage = position;

            if(position == 0){
                nextButton.setEnabled(true);
                backButton.setEnabled(false);
                backButton.setVisibility(View.INVISIBLE);

                nextButton.setText(getResources().getString(R.string.onboarding_next));
                backButton.setText("");

            }
            else if(position == dots.length - 1){
                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText(getResources().getString(R.string.onboarding_finish));
                backButton.setText(getResources().getString(R.string.onboarding_back));
            }
            else{

                nextButton.setEnabled(true);
                backButton.setEnabled(true);
                backButton.setVisibility(View.VISIBLE);

                nextButton.setText(getResources().getString(R.string.onboarding_next));
                backButton.setText(getResources().getString(R.string.onboarding_back));
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    private void goToMaps(){
        if(firstTime) {
            sharedPreferences.edit().putBoolean("firstTime", false).apply();
        }
        Intent mapIntent = new Intent(this, MapsActivity.class);
        mapIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mapIntent);
        this.overridePendingTransition(0, 0);
    }
}
