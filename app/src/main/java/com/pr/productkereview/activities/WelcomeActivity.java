package com.pr.productkereview.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivityWelcomeBinding;
import com.pr.productkereview.utils.CommonMethods;

public class WelcomeActivity extends AppCompatActivity {
    ActivityWelcomeBinding binding;
    MaterialAlertDialogBuilder builder;
    // ApiInterface apiInterface;
    //  Map<String, String> map = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWelcomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // apiInterface = ApiWebServices.getApiInterface();

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

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                binding.getStartedBtn.setOnClickListener(v -> {
                    Intent intent = new Intent(WelcomeActivity.this, HomeActivity.class);
                    startActivity(intent);
                });
            }
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
        builder = new MaterialAlertDialogBuilder(this);
        builder.setTitle(R.string.app_name)
                .setIcon(R.mipmap.ic_launcher)
                .setMessage("Do You Really Want To Exit?\nAlso give us your rating")
                .setNeutralButton("CANCEL", (dialog, which) -> {
                });


        builder.setNegativeButton("RATE APP", (dialog, which) -> CommonMethods.rateApp(getApplicationContext()))
                .setPositiveButton("OK!!", (dialog, which) -> {
                    Intent intent = new Intent(Intent.ACTION_MAIN);
                    intent.addCategory(Intent.CATEGORY_HOME);
                    intent.setFlags(Intent.FLAG_ACTIVITY_TASK_ON_HOME | Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                    moveTaskToBack(true);
                    System.exit(0);

                });
        builder.show();
    }
}