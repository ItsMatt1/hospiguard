package com.example.hospiguard;

import android.annotation.SuppressLint;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;


public class HeartRateSensorFragment extends Fragment implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor heartRateSensor;
    private TextView heartRateValueTextView;
    private float[] heartRateArray;
    private int arrayIndex = 0;
    float heartRateValue = 0;
    private static final float UPPER_THRESHOLD = 60;
    private static final float LOWER_THRESHOLD = 5;

    private static final String CHANNEL_ID = "heartrate_sensor_channel";
    private static final int NOTIFICATION_ID_HIGH = 1;
    private static final int NOTIFICATION_ID_LOW = 2;
    private static final int PERMISSION_REQUEST_NOTIFICATION = 124;
    private static final int NOTIFICATION_ID = 457;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sensorManager = (SensorManager) requireActivity().getSystemService(getActivity().SENSOR_SERVICE);
        heartRateSensor = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        if (heartRateSensor == null) {
            return;
        }

        createNotificationChannel();
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_heartrate_sensor, container, false);
        heartRateValueTextView = rootView.findViewById(R.id.heartRateValueTextView);

        // Initialize heartRateArray
        heartRateArray = new float[100];

        // Start a CountDownTimer to update the array every 5 seconds
        new CountDownTimer(600000, 5000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // This method will be called every 5 seconds
                // Add the current lightLux value to the array
                if (arrayIndex < heartRateArray.length) {
                    heartRateArray[arrayIndex++] = getCurrentHeartRate();
                    Log.d("deu??", "" + getCurrentHeartRate());
                    checkThresholds(heartRateValue);
                }
            }

            @Override
            public void onFinish() {
            }
        }.start();

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        sensorManager.registerListener(this, heartRateSensor, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onPause() {
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

    private void checkThresholds(float rateValue) {  // Changed from luxValue
        if (rateValue > UPPER_THRESHOLD) {
            // Heart rate value is above the upper threshold, display a warning for high rate
            displayWarning("High Heart Rate Warning");  // Changed from High Light Warning
            sendNotification(NOTIFICATION_ID_HIGH, "High Heart Rate Warning", "The heart rate is too high!");  // Changed from High Light Warning
        } else if (rateValue < LOWER_THRESHOLD) {
            // Heart rate value is below the lower threshold, display a warning for low rate
            displayWarning("Low Heart Rate Warning");  // Changed from Low Light Warning
            sendNotification(NOTIFICATION_ID_LOW, "Low Heart Rate Warning", "The heart rate is too low!");  // Changed from Low Light Warning
        } else {
            // Heart rate value is within the normal range, clear any existing warnings
            heartRateValueTextView.setTextColor(Color.WHITE);  // Changed from lightValueTextView
            heartRateValueTextView.setText("Heart Rate: " + heartRateValue);  // Changed from lightValueTextView
        }
    }

    private float getCurrentHeartRate() {
        // You can add any additional logic or filtering here before returning the light value
        return heartRateValue;
    }

    private void displayWarning(String warningMessage) {
        // Set the text color to indicate a warning
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
            NotificationManager notificationManager = requireActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void sendNotification(int notificationId, String title, String message) {
        Log.d("Notification", "Sending notification with ID: " + notificationId);

        // Check if the notification permission is granted
        if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            Log.d("Notification", "Permission not granted for notifications. Requesting permission...");

            // Request the notification permission
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    PERMISSION_REQUEST_NOTIFICATION);

            // After requesting permission, the result will be handled in onRequestPermissionsResult
            return;
        }

        // If the permission is granted, proceed to send the notification
        NotificationCompat.Builder builder = new NotificationCompat.Builder(requireContext(), CHANNEL_ID)
                .setSmallIcon(R.drawable.notification_2645897) // Set your notification icon
                .setContentTitle(title)
                .setContentText(message)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(requireContext());
        notificationManager.notify(notificationId, builder.build());

        Log.d("Notification", "Notification sent successfully.");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSION_REQUEST_NOTIFICATION) {
            // Check if the permission was granted after the request
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Notification", "Notification permission granted. Sending the notification...");
                // Now that the permission is granted, resend the notification
                sendNotification(NOTIFICATION_ID, "Title", "Message");
            } else {
                Log.e("Notification", "Notification permission denied.");
            }
        }
    }
}
