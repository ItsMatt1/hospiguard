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

public class TemperatureSensorActivity extends AppCompatActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor temperatureSensor;
    private TextView temperatureValueTextView;
    private float[] temperatureArray;
    private int arrayIndex = 0;

    float temperatureValue = 0;
    private static final float UPPER_THRESHOLD = 60;
    private static final float LOWER_THRESHOLD = 5;

    private LinearLayout valueContainer;

    private static final String CHANNEL_ID = "temperature_sensor_channel";
    private static final int NOTIFICATION_ID_HIGH = 1;
    private static final int NOTIFICATION_ID_LOW = 2;

    private static final int PERMISSION_REQUEST_NOTIFICATION = 123; // Choose any unique value
    private static final int NOTIFICATION_ID = 456; // Choose any unique value

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_temperature_sensor);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        temperatureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE);
        if (temperatureSensor == null) {
            return;
        }

        valueContainer = findViewById(R.id.valueContainer);

        createNotificationChannel();
        initializeViews();
    }

    private void initializeViews() {
        temperatureValueTextView = findViewById(R.id.temperatureValueTextView);
        temperatureArray = new float[100];

        new CountDownTimer(600000, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (arrayIndex < temperatureArray.length) {
                    temperatureArray[arrayIndex++] = getCurrentTemperature();
                    Log.d("deu??", "" + getCurrentTemperature());
                    checkThresholds(temperatureValue);

                    // Display the value in a box on the UI
                    displayValue(temperatureValue);
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

        valueTextView.setText(String.format(Locale.getDefault(), "Temperatura: %s: %.2f °C", currentTime, luxValue));

        valueContainer.addView(valueTextView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, temperatureSensor, SensorManager.SENSOR_DELAY_NORMAL);
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
        temperatureValue = event.values[0];
        temperatureValueTextView.setText("Temperature: " + temperatureValue);
        checkThresholds(temperatureValue);
    }

    private void checkThresholds(float temperatureValue) {
        if (temperatureValue > UPPER_THRESHOLD) {
            displayWarning("High Temperature Warning");
            sendNotification(NOTIFICATION_ID_HIGH, "High Temperature Warning", "The temperature is too high!");
        } else if (temperatureValue < LOWER_THRESHOLD) {
            displayWarning("Low Temperature Warning");
            sendNotification(NOTIFICATION_ID_LOW, "Low Temperature Warning", "The temperature is too low!");
        } else {
            temperatureValueTextView.setTextColor(Color.WHITE);
            temperatureValueTextView.setText("Temperature: " + temperatureValue);
        }
    }

    private float getCurrentTemperature() {
        return temperatureValue;
    }

    private void displayWarning(String warningMessage) {
        temperatureValueTextView.setTextColor(Color.RED);
        temperatureValueTextView.setText(warningMessage);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Temperature Sensor Channel";
            String description = "Channel for Temperature Sensor Notifications";
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
