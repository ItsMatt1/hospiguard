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

    public static final String EXTRA_MESSAGE = "com.example.basicandroidmqttclient.MESSAGE";
    public static final String brokerURI = "34.194.22.234";

    float lightValue = 0;

    String topicName = "doctorLightSensorA";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_light_sensor);

        valueContainer = findViewById(R.id.valueContainer);
        lightValueTextView = findViewById(R.id.lightValueTextView);

        connectToMQTTBroker();
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
    }
}
