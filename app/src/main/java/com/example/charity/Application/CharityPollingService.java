package com.example.charity.Application;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.example.charity.Application.Network.APIClient;
import com.example.charity.Application.Network.APIInterface;
import com.example.charity.Model.Polling;
import com.example.charity.Module.AvailableFood.AvailableFoodActivity;
import com.example.charity.R;
import com.example.charity.Utility.Constants;
import com.example.charity.Utility.Utilities;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CharityPollingService extends Service {

    private Handler handler;
    public static final long DEFAULT_SYNC_INTERVAL = 3 * 1000;
    private APIInterface apiInterface;

    private Runnable runnableService = new Runnable() {
        @Override
        public void run() {
            apiInterface = APIClient.getRetrofitInstance().create(APIInterface.class);
            if (Utilities.isConnected(getApplicationContext())) {
                startPolling();
            }

            handler.postDelayed(runnableService, DEFAULT_SYNC_INTERVAL);
        }
    };

    private void startPolling() {
        final Polling polling = new Polling(Constants.CLIENT_ID);

        Call<Polling> call = apiInterface.checkCharityPoling(polling);
        call.enqueue(new Callback<Polling>() {
            @Override
            public void onResponse(Call<Polling> call, Response<Polling> response) {
                Polling poolingResponse = response.body();

                if (poolingResponse != null) {
                    if (poolingResponse.getResponseCode() == 100) {
                        showNotification();
                    }

                } else {
                    Toast.makeText(getApplicationContext(), "null response", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<Polling> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "PollingService, onFailure called ", Toast.LENGTH_SHORT).show();
                call.cancel();
            }
        });

    }

    private void showNotification() {
        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(CharityPollingService.this, "charity_channel")
                        .setSmallIcon(R.drawable.ic_message_green_24dp)
                        .setContentTitle(getResources().getString(R.string.notification_title_text))
                        .setContentText(getResources().getString(R.string.notification_details_text))
                        .setAutoCancel(true);

        Intent intent = new Intent(this, AvailableFoodActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(
                CharityPollingService.this,
                0,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT
        );

        builder.setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        handler = new Handler();
        handler.post(runnableService);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        handler.removeCallbacks(runnableService);
        stopSelf();

        super.onDestroy();
    }

}
