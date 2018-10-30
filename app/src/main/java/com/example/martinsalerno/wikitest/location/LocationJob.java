package com.example.martinsalerno.wikitest.location;

import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class LocationJob extends JobService {
    public static final Integer LOCATION_JOB_ID = 1;

    @Override
    public boolean onStartJob(final JobParameters jobParameters) {
        new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if(intent.getAction().equals(LocationIntentService.ACTION_END)) {
                    jobFinished(jobParameters, true);
                }
            }
        };
        Log.d("LOCATIONJOB", "STARTING INTENT SERVICE");
        Intent intent = new Intent(this, LocationIntentService.class);
        startService(intent);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        return false;
    }
}
