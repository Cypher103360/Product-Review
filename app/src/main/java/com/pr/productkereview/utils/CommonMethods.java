package com.pr.productkereview.utils;

import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerDrawable;
import com.pr.productkereview.BuildConfig;
import com.pr.productkereview.R;
import com.pr.productkereview.models.UrlsModels.UrlModel;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommonMethods {
    static ApiInterface apiInterface = ApiWebServices.getApiInterface();
    static String shareText;

    public static Dialog getLoadingDialog(Context context) {
        Dialog loadingDialog;
        loadingDialog = new Dialog(context);
        loadingDialog.setContentView(R.layout.loading);
        loadingDialog.getWindow().setLayout(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        loadingDialog.getWindow().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.item_bg));
        loadingDialog.setCancelable(false);
        return loadingDialog;
    }

    public static ShimmerDrawable setShimmer(Context context) {
        Shimmer shimmer = new Shimmer.AlphaHighlightBuilder()// The attributes for a ShimmerDrawable is set by this builder
                .setDuration(700) // how long the shimmering animation takes to do one full sweep
                .setBaseAlpha(0.6f) //the alpha of the underlying children
                .setHighlightAlpha(0.8f) // the shimmer alpha amount
                .setDirection(Shimmer.Direction.LEFT_TO_RIGHT)
                .setAutoStart(true)
                .build();

        // This is the placeholder for the imageView
        ShimmerDrawable shimmerDrawable = new ShimmerDrawable();
        shimmerDrawable.setShimmer(shimmer);
        return shimmerDrawable;
    }

    public static void shareApp(Context context, String share) {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, R.string.app_name);
            String shareMessage = "Hii, \n" +
                    "Hope You R Enjoying\uD83E\uDD73\n" +
                    "If Not,\n" +
                    "\n" +
                    "Let Me Share Something....\n" +
                    "It's Really Good!\n" +
                    "\n" + share + "\n";
            shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "\n\n";
            shareIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
            context.startActivity(Intent.createChooser(shareIntent, "choose one"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String fetchUrls(String tips) {
        Call<UrlModel> call = apiInterface.getUrls(tips);
        call.enqueue(new Callback<UrlModel>() {
            @Override
            public void onResponse(@NonNull Call<UrlModel> call, @NonNull Response<UrlModel> response) {
                if (response.isSuccessful()) {
                    assert response.body() != null;
//                    webUrlId = response.body().getId();
                    shareText = response.body().getUrl();
                    //Log.d("urls",weburl);
                }
            }

            @Override
            public void onFailure(@NonNull Call<UrlModel> call, @NonNull Throwable t) {

            }
        });
        return shareText;
    }

    public static void rateApp(Context context) {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        myAppLinkToMarket.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(context, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }

    public static void contactUs(Context context) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setPackage("com.google.android.gm");
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"help.productreviews@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Hello");
        i.putExtra(Intent.EXTRA_TEXT, "Hi Team PR,\n" +
                "Its So Glad to Connect...\n" +
                "\n" +
                "This is \"Your Name\"\uD83D\uDE0A\n" +
                "\n" +
                "And I need Help Regards\n" +
                "\n" +
                "World's Best Product With Unbiased Review..... ");
        try {
            context.startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (ActivityNotFoundException ex) {
            Toast.makeText(context, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public static void whatsApp(Context context) throws UnsupportedEncodingException, PackageManager.NameNotFoundException {
        String contact = "+91 6396869782"; // use country code with your phone number
        String url = "https://api.whatsapp.com/send?phone=" + contact + "&text=" + URLEncoder.encode("Hi Team PR,\n" +
                "This is \"Your Name\"\uD83D\uDE0A\n" +
                "\n" +
                "And I need Help Regards\n" +
                "World's Best Unbiased Product Reviews.....", "UTF-8");
        try {
            PackageManager pm = context.getPackageManager();
            pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setData(Uri.parse(url));
            context.startActivity(i);

        } catch (PackageManager.NameNotFoundException e) {
            try {
                PackageManager pm = context.getPackageManager();
                pm.getPackageInfo("com.whatsapp.w4b", PackageManager.GET_ACTIVITIES);
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.setData(Uri.parse(url));
                context.startActivity(i);
            } catch (PackageManager.NameNotFoundException exception) {
                e.printStackTrace();
                Toast.makeText(context, "WhatsApp is not installed on this Device.", Toast.LENGTH_SHORT).show();

            }

//            whatsApp(context, "com.whatsapp.w4b");
        }


    }
}
