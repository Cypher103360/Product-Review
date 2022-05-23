package com.pr.productkereview.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Spannable;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.pr.productkereview.R;
import com.pr.productkereview.databinding.ActivitySignupBinding;

public class SignupActivity extends AppCompatActivity {
    ActivitySignupBinding binding;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;
    int RC_SIGN_IN = 1000;
    FirebaseAnalytics mFirebaseAnalytics;

    // Client Id
    // 419867264283-7qaeuoq80dap4vnucfatmebhlmu0lkoi.apps.googleusercontent.com

    // Client Secret
    // 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

    // ed0db632-e10f-4d79-9b48-df7c033aaa7a

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
//        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // Google SignIn
//        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
//        gsc = GoogleSignIn.getClient(this, gso);



//        // test link span
//        TextView tv =  findViewById(R.id.terms_text);
//        Spannable span = Spannable.Factory.getInstance().newSpannable(
//                "By continuing, you agree to Loan Guide's\n Terms of Service and acknowledge that you've read our Privacy Policy");
//        span.setSpan(new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this,PrivacyPolicy.class);
//                intent.putExtra("key","terms");
//                startActivity(intent);
//            }
//        }, 42, 58, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        // All the rest will have the same spannable.
//        ClickableSpan cs = new ClickableSpan() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(SignupActivity.this,PrivacyPolicy.class);
//                intent.putExtra("key","policy");
//                startActivity(intent);
//            }
//        };

        // set the "test " spannable.
//        span.setSpan(cs, 96, span.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//
//        tv.setText(span);
//
//        tv.setMovementMethod(LinkMovementMethod.getInstance());
//
//        binding.withGoogle.setOnClickListener(view -> {
//            signIn();
//        });
    }
//    private void signIn() {
//        Intent signInIntent = gsc.getSignInIntent();
//        startActivityForResult(signInIntent, RC_SIGN_IN);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == RC_SIGN_IN) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
//
//            try {
//                task.getResult(ApiException.class);
//                navigateToNextActivity();
//            } catch (ApiException e) {
//                Toast.makeText(this, "Something went wrong", Toast.LENGTH_SHORT).show();
//            }
//
//        }
//    }
//    private void navigateToNextActivity() {
//        finish();
//        Intent intent = new Intent(SignupActivity.this, WelcomeActivity.class);
//        startActivity(intent);
//        Bundle bundle = new Bundle();
//        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Google Sign in");
//        mFirebaseAnalytics.logEvent("Clicked_On_Google_SignIn", bundle);
//    }
}