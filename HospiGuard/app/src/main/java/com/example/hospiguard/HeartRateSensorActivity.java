package com.example.hospiguard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HeartRateSensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView heartRateValueTextView;
    private float[] heartRateArray;
    private int arrayIndex = 0;
    float heartRateValue = 0;

    private LinearLayout valueContainer;

    private static final float UPPER_THRESHOLD = 60;
    private static final float LOWER_THRESHOLD = 5;

    private static final String CHANNEL_ID = "heartrate_sensor_channel";
    private static final int NOTIFICATION_ID_HIGH = 1;
    private static final int NOTIFICATION_ID_LOW = 2;
    private static final int PERMISSION_REQUEST_NOTIFICATION = 124;
    private static final int NOTIFICATION_ID = 457;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_heartrate_sensor);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_HEART_RATE);
        if (heartRateSensor == null) {
            return;
        }

        valueContainer = findViewById(R.id.valueContainer);

        createNotificationChannel();
        initializeViews();
    }

    private void initializeViews() {
        heartRateValueTextView = findViewById(R.id.heartRateValueTextView);
        heartRateArray = new float[100];

        new CountDownTimer(600000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (arrayIndex < heartRateArray.length) {
                    heartRateArray[arrayIndex++] = getCurrentHeartRate();
                    Log.d("deu??", "" + getCurrentHeartRate());
                    checkThresholds(heartRateValue);

                    // Display the value in a box on the UI
                    displayValue(heartRateValue);
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private void displayValue(float luxValue) {
        TextView valueTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        valueTextView.setLayoutParams(params);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        valueTextView.setText(String.format(Locale.getDefault(), "Batimento cardiáco: %s: %.2f", currentTime, luxValue));

        valueContainer.addView(valueTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // Handle accuracy changes if needed
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        heartRateValue = event.values[0];
        heartRateValueTextView.setText("Batimento cardíaco: " + heartRateValue);
        checkThresholds(heartRateValue);
    }

    private void checkThresholds(float rateValue) {
        if (rateValue > UPPER_THRESHOLD) {
            displayWarning("High Heart Rate Warning");
            sendNotification(NOTIFICATION_ID_HIGH, "High Heart Rate Warning", "The heart rate is too high!");
        } else if (rateValue < LOWER_THRESHOLD) {
            displayWarning("Low Heart Rate Warning");
            sendNotification(NOTIFICATION_ID_LOW, "Low Heart Rate Warning", "The heart rate is too low!");
        } else {
            heartRateValueTextView.setTextColor(Color.WHITE);
            heartRateValueTextView.setText("Heart Rate: " + heartRateValue);
        }
    }

    private float getCurrentHeartRate() {
        return heartRateValue;
    }

    private void displayWarning(String warningMessage) {
        heartRateValueTextView.setTextColor(Color.RED);
        heartRateValueTextView.setText(warningMessage);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Heart Sensor Channel";
            String description = "Channel for Heart Rate Sensor Notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(int notificationId, String title, String message) {
        Log.d("Notification", "Sending notification with ID: " + notificationId);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Notification", "Permission not granted for notifications. Requesting permission...");
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, PERMISSION_REQUEST_NOTIFICATION);
            return;
        }

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_2645897)
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(notificationId, builder.build());

        Log.d("Notification", "Notification sent successfully.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_NOTIFICATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Notification", "Notification permission granted. Sending the notification...");
                sendNotification(NOTIFICATION_ID, "Title", "Message");
            } else {
                Log.e("Notification", "Notification permission denied.");
            }
        }
    }
}
