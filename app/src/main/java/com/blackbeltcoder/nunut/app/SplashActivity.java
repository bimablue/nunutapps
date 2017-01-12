package com.blackbeltcoder.nunut.app;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;

import com.blackbeltcoder.nunut.R;
import com.blackbeltcoder.nunut.global.App;
import com.blackbeltcoder.nunut.global.ConstantVariable;
import com.blackbeltcoder.nunut.model.NuterModel;
import com.blackbeltcoder.nunut.service.RestService;
import com.crystal.crystalpreloaders.widgets.CrystalPreloader;

public class SplashActivity extends AppCompatActivity {

    private Handler h;
    private Intent i;
    private ProgressBar progressBar;
    private CrystalPreloader progressCircle;

    private static int current_progress = 0;
    private static int status_progress  = 0;
    private static int end_progress     = 0;
    private static int percent_progress = 100;
    private static int delay_progress 	= 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        progressCircle = (CrystalPreloader) findViewById(R.id.progressCircle);
        progressCircle.setVisibility(View.GONE);

        h = new Handler();

        Intent intent = new Intent(getApplicationContext(), RestService.class);
        intent.putExtra(ConstantVariable.BROADCAST_KEY, ConstantVariable.BROADCAST_KEY_ROUTES);

        if(App.getNuter() != null)
            intent.putExtra(ConstantVariable.BROADCAST_OBJ, App.getNuter());
        else
            intent.putExtra(ConstantVariable.BROADCAST_OBJ, new NuterModel());

        startService(intent);

        /*if(App.getNuter() != null){
            i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
        } else {*/
            progressCircle.setVisibility(View.VISIBLE);
            //loadProgressBar();
        //}
    }

    private void loadProgressBar() {

        new Thread(new Runnable(){

            @Override
            public void run() {
                try {
                    Thread.sleep(6000);
                    startProgress();
                } catch (InterruptedException ex) {
                    ex.printStackTrace();
                }

                h.post(new Runnable(){
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            endCondition();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    private void endCondition() {
                        i = new Intent(SplashActivity.this, MainActivity.class);
                        startActivity(i);
                        overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);

                        current_progress=end_progress;
                        status_progress=end_progress;
                    }
                });
            }

            /* current progress */
            private void startProgress() {
                for(;status_progress < percent_progress;){
                    status_progress = delayProgress();
                    h.post(new Runnable(){

                        @Override
                        public void run() {
                            if(status_progress == 99)
                                progressCircle.setVisibility(View.GONE);
                    }
                    });
                }
            }

            /* delay progress */
            private int delayProgress() {
                int incProgress = ++current_progress;
                try {
                    Thread.sleep(delay_progress);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }
                return incProgress;
            }
        })
                .start();
    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        super.onPause();
    }

    @Override
    public void onResume() {
        IntentFilter iFilter = new IntentFilter(ConstantVariable.BROADCAST_KEY_ROUTES_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, iFilter);
        super.onResume();
    }

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            String status = intent.getStringExtra(ConstantVariable.BROADCAST_STATUS);
            //Log.d("receiver", "Got message: " + status);

            if (status != null) {
                progressCircle.setVisibility(View.GONE);
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.pull_in_right, R.anim.push_out_left);
            }
        }
    };

}
