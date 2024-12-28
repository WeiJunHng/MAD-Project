package com.example.madproject;

import android.app.Notification;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.widget.Switch;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AllowNotification#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AllowNotification extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private NotificationManager notificationManager;

    public AllowNotification() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllowNotification.
     */
    // TODO: Rename and change types and number of parameters
    public static AllowNotification newInstance(String param1, String param2) {
        AllowNotification fragment = new AllowNotification();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_allow_notification, container, false);

        notificationManager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

        // Create Notification Channel (Android 8.0 and above)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    "default_channel",
                    "Default Notifications",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        // Get Switches from layout
        Switch showNotificationSwitch = view.findViewById(R.id.ShowNotification);
        Switch floatingNotificationSwitch = view.findViewById(R.id.Photo);
        Switch lockScreenNotificationSwitch = view.findViewById(R.id.Camera);
        Switch allowVibrationSwitch = view.findViewById(R.id.FileAndFolder);
        Switch allowSoundSwitch = view.findViewById(R.id.AllowSound);

        // Initially disable floating and lock screen switches
        floatingNotificationSwitch.setEnabled(false);
        lockScreenNotificationSwitch.setEnabled(false);

        // Handle Show Notifications logic
        showNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                // Enable other switches
                floatingNotificationSwitch.setEnabled(true);
                lockScreenNotificationSwitch.setEnabled(true);
            } else {
                // Disable and uncheck other switches
                floatingNotificationSwitch.setEnabled(false);
                floatingNotificationSwitch.setChecked(false);
                lockScreenNotificationSwitch.setEnabled(false);
                lockScreenNotificationSwitch.setChecked(false);
            }
        });

        // Handle Floating Notifications
        floatingNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int importance = isChecked ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_LOW;
                updateNotificationChannelImportance("default_channel", importance);
            }
        });

        // Handle Lock Screen Notifications
        lockScreenNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                int visibility = isChecked ? Notification.VISIBILITY_PUBLIC : Notification.VISIBILITY_SECRET;
                updateNotificationChannelLockScreenVisibility("default_channel", visibility);
            }
        });

        // Handle Vibration
        allowVibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateNotificationChannelVibration("default_channel", isChecked);
            }
        });

        // Handle Sound
        allowSoundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                updateNotificationChannelSound("default_channel", isChecked);
            }
        });

        return view;
    }

    private void enableNotifications() {
        // Logic to enable notifications
    }

    private void disableNotifications() {
        // Logic to disable notifications
    }

    private void updateNotificationChannelImportance(String channelId, int importance) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                channel.setImportance(importance);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void updateNotificationChannelLockScreenVisibility(String channelId, int visibility) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                channel.setLockscreenVisibility(visibility);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void updateNotificationChannelVibration(String channelId, boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                channel.enableVibration(enabled);
                notificationManager.createNotificationChannel(channel);
            }
        }
    }

    private void updateNotificationChannelSound(String channelId, boolean enabled) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = notificationManager.getNotificationChannel(channelId);
            if (channel != null) {
                if (enabled) {
                    channel.setSound(null, null); // Use default sound
                } else {
                    channel.setSound(null, null); // Disable sound
                }
                notificationManager.createNotificationChannel(channel);
            }
        }
    }
}