package com.pr.productkereview.utils;

import static android.content.ContentValues.TAG;

import android.app.Application;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.onesignal.OSNotificationOpenedResult;
import com.onesignal.OneSignal;
import com.pr.productkereview.activities.HomeActivity;
import com.pr.productkereview.activities.WelcomeActivity;
import com.pr.productkereview.models.AdsModel;

import org.json.JSONObject;

import java.util.List;

import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MyApp extends Application {

    private static final String ONESIGNAL_APP_ID = "ed0db632-e10f-4d79-9b48-df7c033aaa7a";
    public static MyApp mInstance;
    ApiInterface apiInterface;
    SharedPreferences.Editor editor;


    public MyApp() {
        mInstance = this;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        // OneSignal Initialization
        editor = PreferenceManager.getDefaultSharedPreferences(mInstance).edit();
        Paper.init(mInstance);
        OneSignal.initWithContext(this);
        OneSignal.setNotificationOpenedHandler(new ExampleNotificationOpenedHandler());
        OneSignal.setAppId(ONESIGNAL_APP_ID);
        apiInterface = ApiWebServices.getApiInterface();
        fetchAds();
    }

    private void fetchAds() {
        apiInterface = ApiWebServices.getApiInterface();
        Call<List<AdsModel>> call = apiInterface.fetchAds("Product Review");
        call.enqueue(new Callback<List<AdsModel>>() {
            @Override
            public void onResponse(@NonNull Call<List<AdsModel>> call, @NonNull Response<List<AdsModel>> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        for (AdsModel ads : response.body()) {
                            Log.d("checkIds",
                                    ads.getId()
                                            + "\n" + ads.getAppId()
                                            + "\n" + ads.getAppLovinAppKey()
                                            + "\n" + ads.getBannerTop()
                                            + "\n" + ads.getBannerTopAdNetwork()
                                            + "\n" + ads.getBannerBottom()
                                            + "\n" + ads.getBannerBottomAdNetwork()
                                            + "\n" + ads.getInterstitial()
                                            + "\n" + ads.getInterstitalAdNetwork()
                                            + "\n" + ads.getNativeAd()
                                            + "\n" + ads.getNativeAdNetwork()
                                            + "\n" + ads.getNativeType()
                                            + "\n" + ads.getRewardAd()
                                            + "\n" + ads.getRewardAdNetwork()
                            );

                            Paper.book().write(Prevalent.id, ads.getId());
                            Paper.book().write(Prevalent.appId, ads.getAppId());
                            Paper.book().write(Prevalent.appLovinId, ads.getAppLovinAppKey());
                            Paper.book().write(Prevalent.bannerTop, ads.getBannerTop());
                            Paper.book().write(Prevalent.bannerTopNetworkName, ads.getBannerTopAdNetwork());
                            Paper.book().write(Prevalent.bannerBottom, ads.getBannerBottom());
                            Paper.book().write(Prevalent.bannerBottomNetworkName, ads.getBannerBottomAdNetwork());
                            Paper.book().write(Prevalent.interstitialAds, ads.getInterstitial());
                            Paper.book().write(Prevalent.interstitialNetwork, ads.getInterstitalAdNetwork());
                            Paper.book().write(Prevalent.nativeAds, ads.getNativeAd());
                            Paper.book().write(Prevalent.nativeAdsNetworkName, ads.getNativeAdNetwork());
                            Paper.book().write(Prevalent.nativeAdsType, ads.getNativeType());
                            Paper.book().write(Prevalent.rewardAds, ads.getRewardAd());
                            Paper.book().write(Prevalent.rewardAdsNetwork, ads.getRewardAdNetwork());

                        }

                        // setting application keys of admob
                        try {
                            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                            Bundle bundle = ai.metaData;
                            String myApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                            Log.d(TAG, "Name Found: " + myApiKey);
                            ai.metaData.putString("com.google.android.gms.ads.APPLICATION_ID", Paper.book().read(Prevalent.appId));//you can replace your key APPLICATION_ID here
                            String ApiKey = bundle.getString("com.google.android.gms.ads.APPLICATION_ID");
                            Log.d(TAG, "ReNamed Found: " + ApiKey);
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                        } catch (NullPointerException e) {
                            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                        }

                        // setting application keys of applovin
                        try {
                            ApplicationInfo ai = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
                            Bundle bundle = ai.metaData;
                            String myApiKey = bundle.getString("applovin.sdk.key");
                            Log.d(TAG, "Name Found: " + myApiKey);
                            ai.metaData.putString("applovin.sdk.key", Paper.book().read(Prevalent.appLovinId));     //you can replace your key APPLICATION_ID here
                            String ApiKey = bundle.getString("applovin.sdk.key");
                            Log.d(TAG, "ReNamed Found: " + ApiKey);
                        } catch (PackageManager.NameNotFoundException e) {
                            Log.e(TAG, "Failed to load meta-data, NameNotFound: " + e.getMessage());
                        } catch (NullPointerException e) {
                            Log.e(TAG, "Failed to load meta-data, NullPointer: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e("adsError", response.message());
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<AdsModel>> call, @NonNull Throwable t) {
                Log.d("adsError", t.getMessage());
            }
        });


    }

    private class ExampleNotificationOpenedHandler implements OneSignal.OSNotificationOpenedHandler {
        @Override
        public void notificationOpened(OSNotificationOpenedResult result) {
            JSONObject data = result.getNotification().getAdditionalData();
            String activityToBeOpened, imgPos, cat_pos, cat_item_pos, sub_cat_item_pos;

            if (data != null) {
                activityToBeOpened = data.optString("action", null);
                imgPos = data.optString("pos", null);
                cat_pos = data.optString("cat_pos", null);
                cat_item_pos = data.optString("cat_item_pos", null);
                sub_cat_item_pos = data.optString("sub_cat_item_pos", null);
                editor.putString("pos", imgPos);
                editor.putString("action", activityToBeOpened);
                editor.putString("cat_pos", cat_pos);
                editor.putString("cat_item_pos", cat_item_pos);
                editor.putString("sub_cat_item_pos", sub_cat_item_pos);
                editor.apply();
                switch (activityToBeOpened) {
                    case "home":
                    case "cat":
                    case "tre":
                    case "lat":
                    case "best":
                    case "top": {
                        Intent intent = new Intent(MyApp.this, HomeActivity.class);
                        intent.putExtra("action", activityToBeOpened);
                        intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        break;
                    }

                }
            } else {
                Intent intent = new Intent(MyApp.this, WelcomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }

        }
    }
}