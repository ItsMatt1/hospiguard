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

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;

import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

public class DoctorLightSensorActivity extends AppCompatActivity {
    private TextView lightValueTextView;
    private float[] lightLuxArray;
    private int arrayIndex = 0;

    private LinearLayout valueContainer;

    public float UPPER_THRESHOLD = 60;

    public float LOWER_THRESHOLD = 5;

    public static final String EXTRA_MESSAGE = "com.example.basicandroidmqttclient.MESSAGE";
    public static final String brokerURI = "34.194.22.234";

    float lightValue = 0;

    String topicName = "doctorLightSensorA";

    private static final String CHANNEL_ID = "light_sensor_channel";
    private static final int NOTIFICATION_ID_HIGH = 1;
    private static final int NOTIFICATION_ID_LOW = 2;

    private static final int PERMISSION_REQUEST_NOTIFICATION = 123; // Choose any unique value
    private static final int NOTIFICATION_ID = 456; // Choose any unique value

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_light_sensor);

        valueContainer = findViewById(R.id.valueContainer);
        lightValueTextView = findViewById(R.id.lightValueTextView);

        connectToMQTTBroker();

        createNotificationChannel();
    }

    private void connectToMQTTBroker() {
        new Thread(() -> {
            try {
                Mqtt5BlockingClient client = Mqtt5Client.builder()
                        .identifier(UUID.randomUUID().toString())
                        .serverHost(brokerURI)
                        .buildBlocking();

                client.connect();

                client.toAsync().subscribeWith()
                        .topicFilter(topicName)
                        .qos(MqttQos.AT_LEAST_ONCE)
                        .callback(msg -> {
                            try {
                                // Parse the payload as a float
                                float newLightValue = ByteBuffer.wrap(msg.getPayloadAsBytes()).getFloat();
                                Log.d("valordluz", String.valueOf(newLightValue));
                                runOnUiThread(() -> updateLightValue(newLightValue));
                            } catch (NumberFormatException e) {
                                // Handle parsing errors, e.g., log the error or display a message
                                e.printStackTrace();
                            }
                        })
                        .send();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private void updateLightValue(float newLightValue) {
        lightValue = newLightValue;
        lightValueTextView.setText("Light Lux: " + lightValue);

        // Call checkThresholds to monitor light levels and show warnings/notifications
        checkThresholds(newLightValue);
    }

    private void checkThresholds(float luxValue)
    {
        if (luxValue > UPPER_THRESHOLD) {
            displayWarning("High Light Warning! Light Lux: " + lightValue);
            sendNotification(NOTIFICATION_ID_HIGH, "High Light Warning", "The light level is too high!");
        } else if (luxValue < LOWER_THRESHOLD) {
            displayWarning("Low Light Warning! Light Lux: "+ lightValue);
            sendNotification(NOTIFICATION_ID_LOW, "Low Light Warning", "The light level is too low!");
        } else {
            lightValueTextView.setTextColor(Color.WHITE);
            lightValueTextView.setText("Light Lux: " + lightValue);
        }
    }

    private void displayWarning(String warningMessage) {
        lightValueTextView.setTextColor(Color.RED);
        lightValueTextView.setText(warningMessage);
    }


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Light Sensor Channel";
            String description = "Channel for Light Sensor Notifications";
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
}
