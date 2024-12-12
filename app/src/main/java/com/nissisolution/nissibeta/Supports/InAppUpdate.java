package com.nissisolution.nissibeta.Supports;

import static android.telephony.SmsManager.RESULT_CANCELLED;

import android.app.Activity;
import android.content.IntentSender;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.nissisolution.nissibeta.R;

public class InAppUpdate {

    private Activity activity;
    private final AppUpdateManager appUpdateManager;
    private final int appUpdateType = AppUpdateType.FLEXIBLE;
    private final int MY_REQUEST_CODE = 500;

    public InAppUpdate(Activity activity) {
        this.activity = activity;
        appUpdateManager = AppUpdateManagerFactory.create(activity);
    }

    public void checkForAppUpdate() {
        appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
            boolean isUpdateAvailable = appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE;
            boolean isUpdateAllowed = appUpdateInfo.isUpdateTypeAllowed(appUpdateType);

            if (isUpdateAvailable && isUpdateAllowed) {
                try {
                    appUpdateManager.startUpdateFlowForResult(appUpdateInfo, appUpdateType, activity, MY_REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        appUpdateManager.registerListener(updatedListener);
    }

    InstallStateUpdatedListener updatedListener = installState -> {
        if (installState.installStatus() == InstallStatus.DOWNLOADED) {
            update_popup();
        }
    };

    public void update_popup() {
        Snackbar.make(activity.findViewById(R.id.postsFrameLayout), "An update has just been downloaded",
                Snackbar.LENGTH_INDEFINITE).setAction("RESTART", view -> {
                    if (appUpdateManager != null) {
                        appUpdateManager.completeUpdate();
                    }
        }).show();
    }

    public void onActivityResult(int requestCode, int resultCode) {
        if (requestCode == MY_REQUEST_CODE) {
            if (resultCode == RESULT_CANCELLED){
                Toast.makeText(activity, "Update cancelled", Toast.LENGTH_SHORT).show();
            } else if (resultCode != AppCompatActivity.RESULT_OK) {
                checkForAppUpdate();
            }
        }
    }

    public void onResume() {
        if (appUpdateManager != null) {
            appUpdateManager.getAppUpdateInfo().addOnSuccessListener(appUpdateInfo -> {
                if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                    update_popup();
                }
            });
        }
    }

    public void onDestroy() {
        if (appUpdateManager != null) {
            appUpdateManager.unregisterListener(updatedListener);
        }
    }

}
