package com.pr.productkereview.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityMainBinding;
import com.pr.productkereview.utils.MyReceiver;

public class MainActivity extends AppCompatActivity {
    public static final String BroadCastStringForAction = "checkingInternet";
    ActivityMainBinding binding;
    FirebaseAnalytics firebaseAnalytics;
    int REQUEST_CODE = 11;
    int count = 1;
    public BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadCastStringForAction)) {
                if (intent.getStringExtra("online_status").equals("true")) {
                    Set_Visibility_ON();
                    count++;
                } else {
                    Set_Visibility_OFF();
                }
            }
        }
    };
    IntentFilter intentFilter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        firebaseAnalytics = FirebaseAnalytics.getInstance(this);
        inAppUpdate();

        intentFilter = new IntentFilter();
        intentFilter.addAction(BroadCastStringForAction);
        Intent serviceIntent = new Intent(this, MyReceiver.class);
        startService(serviceIntent);
        if (isOnline(getApplicationContext())) {
            Set_Visibility_ON();
        } else {
            Set_Visibility_OFF();
        }


    }

    private void Set_Visibility_OFF() {
        binding.lottieLoading.setVisibility(View.GONE);
        binding.myText.setVisibility(View.GONE);
        binding.lottieNoInternet.setVisibility(View.VISIBLE);
        binding.mainContainer.setBackgroundColor(0);
        binding.tvNotConnected.setVisibility(View.VISIBLE);
    }

    private void Set_Visibility_ON() {
        binding.lottieLoading.setVisibility(View.VISIBLE);
        binding.myText.setVisibility(View.VISIBLE);
        binding.lottieNoInternet.setVisibility(View.GONE);
        binding.tvNotConnected.setVisibility(View.GONE);
        binding.mainContainer.setBackgroundColor(ContextCompat.getColor(this,R.color.bg_color));
        if (count == 2) {
            new Handler().postDelayed(() -> {
//                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(MainActivity.this);
//                if (account != null) {
//                    startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
//                } else {
//                    startActivity(new Intent(MainActivity.this, SignupActivity.class));
//                }
                startActivity(new Intent(MainActivity.this, WelcomeActivity.class));
                finish();
            }, 2400);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            Toast.makeText(MainActivity.this, "Downloading Started", Toast.LENGTH_SHORT).show();
            if (resultCode != RESULT_OK) {
                Log.d("TAG", "Downloading Failed" + resultCode);
            }
        }
    }

    public boolean isOnline(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnectedOrConnecting();
    }

    private void inAppUpdate() {
        AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(MainActivity.this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                    // This example applies an immediate update. To apply a flexible update
                    // instead, pass in AppUpdateType.FLEXIBLE
                    && appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)) {
                // Request the update.
                try {
                    appUpdateManager.startUpdateFlowForResult(
                            appUpdateInfo, AppUpdateType.IMMEDIATE, MainActivity.this, REQUEST_CODE);
                } catch (IntentSender.SendIntentException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //IronSource.onResume(this);
        registerReceiver(receiver, intentFilter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //IronSource.onPause(this);
        unregisterReceiver(receiver);
    }
}