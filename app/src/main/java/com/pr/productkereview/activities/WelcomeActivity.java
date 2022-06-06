package com.pr.productkereview.activities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.applovin.sdk.AppLovinSdk;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.ironsource.mediationsdk.IronSource;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityWelcomeBinding;
import com.pr.productkereview.utils.CommonMethods;
import com.pr.productkereview.utils.ShowAds;

import java.util.Calendar;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    MaterialAlertDialogBuilder builder;
    // ApiInterface apiInterface;
    //  Map<String, String> map = new HashMap<>();

    ShowAds showAds = new ShowAds();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
       // AppLovinSdk.getInstance(this).showMediationDebugger();
        // apiInterface = ApiWebServices.getApiInterface();

//        Calendar calendar = Calendar.getInstance();
//        int hours = calendar.get(Calendar.HOUR_OF_DAY);
//
//        if (hours < 12) {
//            binding.greetingText.setText("Good Morning");
//
//        } else if (hours < 16) {
//            binding.greetingText.setText("Good Afternoon");
//
//        } else if (hours < 21) {
//            binding.greetingText.setText("Good Evening");
//
//        } else {
//            binding.greetingText.setText("Good Night");
//        }

//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        if (account != null) {
//            String name = account.getDisplayName();
//            String email = account.getEmail();
//            map.put("name", name);
//            map.put("email", email);
//            uploadUserData(map);
//        }

//        ObjectAnimator animY = ObjectAnimator.ofFloat(binding.getStartedBtn, "translationY", -100f, 0f);
//        animY.setDuration(1000);//1sec
//        animY.setInterpolator(new BounceInterpolator());
//        animY.setRepeatCount(2);
//        animY.start();

        final Animation animation = AnimationUtils.loadAnimation(this, R.anim.bounce);
        binding.getStartedBtn.setAnimation(animation);
        showAds = new ShowAds();
        getLifecycle().addObserver(showAds);
        showAds.showBottomBanner(this, findViewById(R.id.adView_bottom));
        showAds.showTopBanner(this, findViewById(R.id.adView_top));

        new Handler().postDelayed(() -> {

            binding.getStartedBtn.setOnClickListener(v -> {
                CommonMethods.getLoadingDialog(WelcomeActivity.this).show();
                Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                startActivity(intent);
                showAds.destroyBanner();
                showAds.showInterstitialAds(WelcomeActivity.this);
            });
        }, 3000);


    }

//    private void uploadUserData(Map<String, String> map) {
//        Call<MessageModel> call = apiInterface.uploadUserData(map);
//        call.enqueue(new Callback<MessageModel>() {
//            @Override
//            public void onResponse(@NonNull Call<MessageModel> call, @NonNull Response<MessageModel> response) {
//                if (response.isSuccessful()) {
//                    assert response.body() != null;
////                    Toast.makeText(WelcomeActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(@NonNull Call<MessageModel> call, @NonNull Throwable t) {
//            }
//        });
//    }

    @Override
    public void onBackPressed() {

        ShowExitDialog();
    }

    private void ShowExitDialog() {
        Dialog exitDialog = new Dialog(WelcomeActivity.this);
        exitDialog.setContentView(R.layout.exit_dialog_layout);
        exitDialog.getWindow().setLayout(600, ViewGroup.LayoutParams.WRAP_CONTENT);
        exitDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        exitDialog.setCancelable(false);
        exitDialog.show();

        TextView rateNow = exitDialog.findViewById(R.id.rate_now);
        TextView okBtn = exitDialog.findViewById(R.id.ok);
        ImageView cancelBtn = exitDialog.findViewById(R.id.dismiss_btn);

        cancelBtn.setOnClickListener(v -> {
            exitDialog.dismiss();
        });
        okBtn.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
            moveTaskToBack(true);
            System.exit(0);
        });

        rateNow.setOnClickListener(v -> {
            CommonMethods.rateApp(getApplicationContext());
        });

    }

    @Override
    protected void onPause() {
        super.onPause();
        IronSource.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        IronSource.onResume(this);
    }
}