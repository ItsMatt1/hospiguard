package com.example.hospiguard;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AccelerationSensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor accelerometerSensor;
    private TextView accelerationValueTextView;
    private float[] accelerationArray;
    private int arrayIndex = 0;
    float accelerationValue = 0;

    private LinearLayout valueContainer;

    private static final float UPPER_THRESHOLD = 15; // Set your upper threshold for acceleration
    private static final float LOWER_THRESHOLD = 5;  // Set your lower threshold for acceleration

    private static final String CHANNEL_ID = "acceleration_sensor_channel";
    private static final int NOTIFICATION_ID_HIGH = 1;
    private static final int NOTIFICATION_ID_LOW = 2;
    private static final int PERMISSION_REQUEST_NOTIFICATION = 124;
    private static final int NOTIFICATION_ID = 457;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_acceleration_sensor);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        accelerometerSensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        if (accelerometerSensor == null) {
            return;
        }

        valueContainer = findViewById(R.id.valueContainer);

        createNotificationChannel();
        initializeViews();
    }

    private void initializeViews() {
        accelerationValueTextView = findViewById(R.id.accelerationValueTextView);
        accelerationArray = new float[100];

        new CountDownTimer(600000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (arrayIndex < accelerationArray.length) {
                    accelerationArray[arrayIndex++] = getCurrentAcceleration();
                    Log.d("Acceleration", "" + getCurrentAcceleration());
                    checkThresholds(accelerationValue);

                    // Display the value in a box on the UI
                    displayValue(accelerationValue);
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();
    }

    private void displayValue(float accelerationValue) {
        TextView valueTextView = new TextView(this);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER_HORIZONTAL;
        valueTextView.setLayoutParams(params);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
        String currentTime = sdf.format(new Date());

        valueTextView.setText(String.format(Locale.getDefault(), "Aceleração: %s: %.2f", currentTime, accelerationValue));

        valueContainer.addView(valueTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        accelerationValue = event.values[0];
        accelerationValueTextView.setText("Aceleração: " + accelerationValue);
        checkThresholds(accelerationValue);
    }

    private void checkThresholds(float accelerationValue) {
        if (accelerationValue > UPPER_THRESHOLD) {
            displayWarning("High Acceleration Warning: " + accelerationValue);
            sendNotification(NOTIFICATION_ID_HIGH, "High Acceleration Warning", "The acceleration is too high!");
        } else {
            accelerationValueTextView.setTextColor(Color.WHITE);
            accelerationValueTextView.setText("Acceleration: " + accelerationValue);
        }
    }

    private float getCurrentAcceleration() {
        return accelerationValue;
    }

    private void displayWarning(String warningMessage) {
        accelerationValueTextView.setTextColor(Color.RED);
        accelerationValueTextView.setText(warningMessage);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Acceleration Sensor Channel";
            String description = "Channel for Acceleration Sensor Notifications";
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
