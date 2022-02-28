package com.chemistryaid.chemistryquizzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import hotchemi.android.rate.AppRate;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
private Button startQuiz,bookmarkBtn;
private TextView shareapp,logic_resoning,general_mental_ability,bookmarks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logic_resoning=findViewById(R.id.logic_resoning);
        general_mental_ability=findViewById(R.id.general_mental_ability);
        bookmarks=findViewById(R.id.bookmarks);
        shareapp=findViewById(R.id.shareapp);


        AppRate.with(this).setInstallDays(1).setLaunchTimes(1).setRemindInterval(1).monitor();
        AppRate.showRateDialogIfMeetsConditions(this);

        logic_resoning.setOnClickListener(this);
        general_mental_ability.setOnClickListener(this);
        bookmarks.setOnClickListener(this);
        shareapp.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        Intent i ;
        switch (v.getId()) {
            case R.id.shareapp:i=new Intent(MainActivity.this,ChallengeYourselfActivity.class);
            startActivity(i);
            break;

            case R.id.logic_resoning:i=new Intent(this,logic_resoning_category.class);startActivity(i);break;
            case R.id.general_mental_ability:i=new Intent(this,CategoryActivity.class);startActivity(i);break;
            case R.id.bookmarks:i=new Intent(this,BookmarkActivity.class);startActivity(i);break;
            default:break;
        }
    }

}
