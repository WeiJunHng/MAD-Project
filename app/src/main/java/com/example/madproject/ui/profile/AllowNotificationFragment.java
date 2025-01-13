package com.example.madproject.ui.profile;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.example.madproject.R;

public class AllowNotificationFragment extends Fragment {

    private static final int NOTIFICATION_PERMISSION_REQUEST_CODE = 1;

    private Switch showNotificationSwitch, floatingNotificationSwitch, lockScreenNotificationSwitch, allowVibrationSwitch, allowSoundSwitch;

    private ActivityResultLauncher<Intent> appSettingsLauncher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_allow_notification, container, false);

        NotificationUtils.createNotificationChannel(requireContext());

        // Initialize switches
        showNotificationSwitch = view.findViewById(R.id.ShowNotification);
        floatingNotificationSwitch = view.findViewById(R.id.FloatingNotification);
        lockScreenNotificationSwitch = view.findViewById(R.id.LockScreenNotification);
        allowVibrationSwitch = view.findViewById(R.id.AllowVibration);
        allowSoundSwitch = view.findViewById(R.id.AllowSound);

        appSettingsLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> updateSwitchStates()
        );

        updateSwitchStates();

        // Main notification switch listener
        showNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (isChecked) {
                if (manager != null && !manager.areNotificationsEnabled()) {
                    enableNotificationDependentSwitches(true);
                    openAppSettings();
                } else {
                    Toast.makeText(getContext(), "Notifications enabled", Toast.LENGTH_SHORT).show();
                }
            } else {
                openAppSettings();
                enableNotificationDependentSwitches(false);
            }
            updateSwitchStates();
        });

        // Floating notification switch listener
        floatingNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (manager != null) {
                NotificationChannel generalChannel = manager.getNotificationChannel("general_notification_channel");

                if (generalChannel != null) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "general_notification_channel");
                    appSettingsLauncher.launch(intent);

                    int newImportance = isChecked ? NotificationManager.IMPORTANCE_HIGH : NotificationManager.IMPORTANCE_DEFAULT;

                    if (generalChannel.getImportance() != newImportance) {
                        generalChannel.setImportance(newImportance);  // Set the new importance value
                        manager.createNotificationChannel(generalChannel);  // Apply the change
                    }
                }
            }
        });


        // Lock screen notification switch listener
        lockScreenNotificationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (manager != null) {
                NotificationChannel generalChannel = manager.getNotificationChannel("general_notification_channel");

                if (generalChannel != null) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "general_notification_channel");
                    appSettingsLauncher.launch(intent);

                    int newLockscreenVisibility = isChecked ? Notification.VISIBILITY_PRIVATE : Notification.VISIBILITY_SECRET;

                    if (generalChannel.getLockscreenVisibility() != newLockscreenVisibility) {
                        generalChannel.setLockscreenVisibility(newLockscreenVisibility); // Set the new lockscreen visibility
                        manager.createNotificationChannel(generalChannel); // Apply the change
                    }
                }
            }
        });

        // Set OnClickListener for Vibration Switch
        allowVibrationSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (manager != null) {
                NotificationChannel generalChannel = manager.getNotificationChannel("general_notification_channel");

                if (generalChannel != null) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "general_notification_channel");
                    appSettingsLauncher.launch(intent);
                }
            }
        });

        allowSoundSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);

            if (manager != null) {
                NotificationChannel generalChannel = manager.getNotificationChannel("general_notification_channel");

                if (generalChannel != null) {
                    Intent intent = new Intent(Settings.ACTION_CHANNEL_NOTIFICATION_SETTINGS);
                    intent.putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
                    intent.putExtra(Settings.EXTRA_CHANNEL_ID, "general_notification_channel");
                    appSettingsLauncher.launch(intent);
                }
            }
        });

        return view;
    }

    private void updateSwitchStates() {
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        boolean notificationsEnabled = manager != null && manager.areNotificationsEnabled();

        showNotificationSwitch.setChecked(notificationsEnabled);
        enableNotificationDependentSwitches(notificationsEnabled);

        // Get the current status of the "General" notification channel
        if (manager != null) {
            NotificationChannel generalChannel = manager.getNotificationChannel("general_notification_channel");
            if (generalChannel != null) {
                // Set the floating notification switch state based on the banner status in the general notification channel
                boolean isBannerEnabled = generalChannel.getImportance() >= NotificationManager.IMPORTANCE_HIGH;
                floatingNotificationSwitch.setChecked(isBannerEnabled);

                // Set the lock screen notification switch state based on the lock screen visibility
                boolean isLockscreenNotVisible = generalChannel.getLockscreenVisibility() == Notification.VISIBILITY_SECRET;
                lockScreenNotificationSwitch.setChecked(!isLockscreenNotVisible);

                // Check if vibration is enabled by verifying the pattern length or if it's set to a default value
                boolean isVibrationNotEnabled = generalChannel.shouldVibrate();
                allowVibrationSwitch.setChecked(isVibrationNotEnabled);

                // Check if sound is enabled (it can be silent, default, or custom)
                boolean isSoundEnabled = generalChannel.getSound() != null;
                allowSoundSwitch.setChecked(isSoundEnabled);
            }
        }
    }

    private void enableNotificationDependentSwitches(boolean enabled) {
        floatingNotificationSwitch.setEnabled(enabled);
        lockScreenNotificationSwitch.setEnabled(enabled);
        allowVibrationSwitch.setEnabled(enabled);
        allowSoundSwitch.setEnabled(enabled);
    }

    private void openAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", requireContext().getPackageName(), null);
        intent.setData(uri);
        appSettingsLauncher.launch(intent);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == NOTIFICATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Notification permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Notification permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        updateSwitchStates();
    }

}

