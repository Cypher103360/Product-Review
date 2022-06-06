package com.pr.productkereview.utils;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;

import com.applovin.mediation.MaxAd;
import com.applovin.mediation.MaxAdFormat;
import com.applovin.mediation.MaxAdViewAdListener;
import com.applovin.mediation.MaxError;
import com.applovin.mediation.ads.MaxAdView;
import com.applovin.mediation.ads.MaxInterstitialAd;
import com.applovin.mediation.nativeAds.MaxNativeAdListener;
import com.applovin.mediation.nativeAds.MaxNativeAdLoader;
import com.applovin.mediation.nativeAds.MaxNativeAdView;
import com.applovin.sdk.AppLovinSdk;
import com.applovin.sdk.AppLovinSdkUtils;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdOptionsView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAdLayout;
import com.facebook.ads.NativeAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdLoader;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.VideoOptions;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback;
import com.google.android.gms.ads.nativead.NativeAd;
import com.google.android.gms.ads.nativead.NativeAdOptions;
import com.google.android.gms.ads.nativead.NativeAdView;
import com.ironsource.mediationsdk.ISBannerSize;
import com.ironsource.mediationsdk.IronSource;
import com.ironsource.mediationsdk.IronSourceBannerLayout;
import com.ironsource.mediationsdk.logger.IronSourceError;
import com.ironsource.mediationsdk.sdk.BannerListener;
import com.ironsource.mediationsdk.sdk.InterstitialListener;
import com.pr.productkereview.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.paperdb.Paper;

public class Ads implements MaxAdViewAdListener, LifecycleObserver {


    public static InterstitialAd mInterstitialAd;
    static boolean checkAdLoad = false;
    private static MaxAd nativeAd;
    public IronSourceBannerLayout banner;
    public NativeAd nativeAds;
    MaxAdView maxAdView;
    MaxInterstitialAd interstitialAd;
    private int retryAttempt;
    private com.facebook.ads.NativeAd fbNativeAd;
    private NativeAdLayout nativeAdLayout;
    private LinearLayout adView;

    public void destroyBanner() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Objects.equals(Paper.book().read(Prevalent.bannerBottomNetworkName), "IronSourceWithMeta")) {
                IronSource.destroyBanner(banner);
                Log.d("destroy", "banner");
            } else if (Objects.equals(Paper.book().read(Prevalent.bannerTopNetworkName), "IronSourceWithMeta")) {
                IronSource.destroyBanner(banner);
                Log.d("destroy", "banner");
            }
        }
    }

    public void showBannerAd(Activity context, RelativeLayout container, String networkName, String bannerAdId) {
        switch (networkName) {
            case "AdmobWithMeta":
                MobileAds.initialize(context);
                AudienceNetworkAds.initialize(context);

                AdRequest adRequest = new AdRequest.Builder().build();
                AdView adView = new AdView(context);
                container.addView(adView);
                adView.setAdUnitId(bannerAdId);
                adView.setAdSize(AdSize.BANNER);
                adView.loadAd(adRequest);
                container.setVisibility(View.VISIBLE);

                break;
            case "IronSourceWithMeta":

                IronSource.init(context, bannerAdId);
                AudienceNetworkAds.initialize(context);
                IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");

                banner = IronSource.createBanner(context, ISBannerSize.BANNER);
                container.addView(banner);

                if (banner != null) {
                    // set the banner listener
                    Log.d("banner", String.valueOf(banner.getId()));
                    IronSource.loadBanner(banner);

                    banner.setBannerListener(new BannerListener() {
                        @Override
                        public void onBannerAdLoaded() {
                            Log.d(TAG, "onBannerAdLoaded");
                            container.setVisibility(View.VISIBLE);
                            // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                        }

                        @Override
                        public void onBannerAdLoadFailed(IronSourceError error) {
                            Log.d(TAG, "onBannerAdLoadFailed" + " " + error);
                            context.runOnUiThread(container::removeAllViews);

                        }

                        @Override
                        public void onBannerAdClicked() {
                            Log.d(TAG, "onBannerAdClicked");
                        }

                        @Override
                        public void onBannerAdScreenPresented() {
                            Log.d(TAG, "onBannerAdScreenPresented");
                        }

                        @Override
                        public void onBannerAdScreenDismissed() {
                            Log.d(TAG, "onBannerAdScreenDismissed");
                        }

                        @Override
                        public void onBannerAdLeftApplication() {
                            Log.d(TAG, "onBannerAdLeftApplication");
                        }
                    });

                    // load ad into the created banner
                } else {
                    Toast.makeText(context, "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
                }

                break;
            case "AppLovinWithMeta":
                AppLovinSdk.initializeSdk(context);
                AppLovinSdk.getInstance(context).setMediationProvider("max");
                AudienceNetworkAds.initialize(context);

                maxAdView = new MaxAdView(bannerAdId, context);
                maxAdView.setListener(this);
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = context.getResources().getDimensionPixelSize(R.dimen.banner_height);
                maxAdView.setLayoutParams(new FrameLayout.LayoutParams(width, height));
                maxAdView.setBackgroundColor(Color.WHITE);
                container.addView(maxAdView);
                maxAdView.loadAd();
                //To show a banner, make the following calls:
                maxAdView.startAutoRefresh();

                break;

            case "Meta":
                AudienceNetworkAds.initialize(context);
                com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(context, bannerAdId, com.facebook.ads.AdSize.BANNER_HEIGHT_50);
                container.addView(adView1);
                adView1.loadAd();
                break;
        }
    }

    public void admobInterstitialAd(Activity context, String interstitialId) {
        MobileAds.initialize(context);
        AudienceNetworkAds.initialize(context);
        // Initialize an InterstitialAd.
        AdRequest adRequest = new AdRequest.Builder().build();

        InterstitialAd.load(context, interstitialId, adRequest,
                new InterstitialAdLoadCallback() {
                    @Override
                    public void onAdLoaded(@NonNull InterstitialAd interstitialAd) {
                        // The mInterstitialAd reference will be null until
                        // an ad is loaded.
                        mInterstitialAd = interstitialAd;
                        mInterstitialAd.show(context);
                        Log.i(TAG, "onAdLoaded");
                    }

                    @Override
                    public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                        // Handle the error
                        Log.i(TAG, loadAdError.getMessage());
                        mInterstitialAd = null;
                    }
                });

    }

    public MaxInterstitialAd appLovinAdInterstitialAd(Activity context, String interstitialId) {
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.initializeSdk(context);
        AudienceNetworkAds.initialize(context);

        interstitialAd = new MaxInterstitialAd(interstitialId, context);
        interstitialAd.setListener(this);
        // Load the first ad
        interstitialAd.loadAd();
        if (interstitialAd.isReady()) {
            interstitialAd.showAd();
            Toast.makeText(context, "ads  ready", Toast.LENGTH_SHORT).show();

        }
        return interstitialAd;
    }

    public void ironSourceInterstitialAd(Activity context, String interstitialId) {
        IronSource.init(context, interstitialId);
        AudienceNetworkAds.initialize(context);

        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");
        IronSource.loadInterstitial();
        IronSource.showInterstitial();
        IronSource.setInterstitialListener(new InterstitialListener() {
            @Override
            public void onInterstitialAdReady() {
                IronSource.showInterstitial();
            }

            @Override
            public void onInterstitialAdLoadFailed(IronSourceError ironSourceError) {
                Log.d(TAG, ironSourceError.getErrorMessage());
            }

            @Override
            public void onInterstitialAdOpened() {
            }

            @Override
            public void onInterstitialAdClosed() {
            }

            @Override
            public void onInterstitialAdShowSucceeded() {
            }

            @Override
            public void onInterstitialAdShowFailed(IronSourceError ironSourceError) {

            }

            @Override
            public void onInterstitialAdClicked() {

            }
        });
    }

    public void metaInterstitialAd(Activity context, String interstitialId) {
        AudienceNetworkAds.initialize(context);
        com.facebook.ads.InterstitialAd interstitialAd = new com.facebook.ads.InterstitialAd(context, interstitialId);
        // Create listeners for the Interstitial Ad
        InterstitialAdListener interstitialAdListener = new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
                interstitialAd.show();
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        };

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        interstitialAd.loadAd(
                interstitialAd.buildLoadAdConfig()
                        .withAdListener(interstitialAdListener)
                        .build());
    }


    //  Admob Native Ads
    public void loadadmobNativeAd(final Activity context, final FrameLayout frameLayout) {
        refreshAd(context, frameLayout);
    }

    public void refreshAd(final Activity context, final FrameLayout frameLayout) {

        AdLoader.Builder builder = new AdLoader.Builder(context, Objects.requireNonNull(Paper.book().read(Prevalent.nativeAds)));

        builder.forNativeAd(nativeAd -> {
            if (nativeAds != null) {
                nativeAds.destroy();
            }
            nativeAds = nativeAd;
            NativeAdView adView = (NativeAdView) context.getLayoutInflater()
                    .inflate(R.layout.ad_unified, null);
            populateUnifiedNativeAdView(nativeAd, adView);
            frameLayout.removeAllViews();
            frameLayout.addView(adView);
        });

        VideoOptions videoOptions = new VideoOptions.Builder()
                .setStartMuted(false)
                .build();

        NativeAdOptions adOptions = new NativeAdOptions.Builder()
                .setVideoOptions(videoOptions)
                .build();

        builder.withNativeAdOptions(adOptions);

        AdLoader adLoader = builder.withAdListener(new AdListener() {

            @Override
            @Deprecated
            public void onAdFailedToLoad(@NonNull LoadAdError loadAdError) {
                super.onAdFailedToLoad(loadAdError);
            }
        }).build();
        AdRequest adRequest = new AdRequest.Builder()
                .build();

        adLoader.loadAd(adRequest);

    }

    private void populateUnifiedNativeAdView(NativeAd nativeAd, NativeAdView adView) {

        com.google.android.gms.ads.nativead.MediaView mediaView = adView.findViewById(R.id.ad_media);
        adView.setMediaView(mediaView);


        adView.setHeadlineView(adView.findViewById(R.id.ad_headline));
        adView.setBodyView(adView.findViewById(R.id.ad_body));
        adView.setCallToActionView(adView.findViewById(R.id.ad_call_to_action));
        adView.setIconView(adView.findViewById(R.id.ad_app_icon));
        adView.setPriceView(adView.findViewById(R.id.ad_price));
        adView.setStarRatingView(adView.findViewById(R.id.ad_stars));
        adView.setStoreView(adView.findViewById(R.id.ad_store));
        adView.setAdvertiserView(adView.findViewById(R.id.ad_advertiser));


        ((TextView) Objects.requireNonNull(adView.getHeadlineView())).setText(nativeAd.getHeadline());


        if (nativeAd.getBody() == null) {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getBodyView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getBodyView()).setText(nativeAd.getBody());
        }

        if (nativeAd.getCallToAction() == null) {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getCallToActionView()).setVisibility(View.VISIBLE);
            ((Button) adView.getCallToActionView()).setText(nativeAd.getCallToAction());
        }

        if (nativeAd.getIcon() == null) {
            Objects.requireNonNull(adView.getIconView()).setVisibility(View.GONE);
        } else {
            ((ImageView) Objects.requireNonNull(adView.getIconView())).setImageDrawable(
                    nativeAd.getIcon().getDrawable());
            adView.getIconView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getPrice() == null) {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getPriceView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getPriceView()).setText(nativeAd.getPrice());
        }

        if (nativeAd.getStore() == null) {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.INVISIBLE);
        } else {
            Objects.requireNonNull(adView.getStoreView()).setVisibility(View.VISIBLE);
            ((TextView) adView.getStoreView()).setText(nativeAd.getStore());
        }

        if (nativeAd.getStarRating() == null) {
            Objects.requireNonNull(adView.getStarRatingView()).setVisibility(View.INVISIBLE);
        } else {
            ((RatingBar) Objects.requireNonNull(adView.getStarRatingView()))
                    .setRating(nativeAd.getStarRating().floatValue());
            adView.getStarRatingView().setVisibility(View.VISIBLE);
        }

        if (nativeAd.getAdvertiser() == null) {
            Objects.requireNonNull(adView.getAdvertiserView()).setVisibility(View.INVISIBLE);
        } else {
            ((TextView) Objects.requireNonNull(adView.getAdvertiserView())).setText(nativeAd.getAdvertiser());
            adView.getAdvertiserView().setVisibility(View.VISIBLE);
        }

        adView.setNativeAd(nativeAd);

    }

    public void IronSourceMRECBanner(Activity context, final FrameLayout container) {
        IronSource.getAdvertiserId(context);
        IronSource.shouldTrackNetworkState(context, true);
        AudienceNetworkAds.initialize(context);
        IronSource.init(context, Paper.book().read(Prevalent.nativeAds));
        IronSource.setMetaData("Facebook_IS_CacheFlag", "IMAGE");
        banner = IronSource.createBanner(context, ISBannerSize.RECTANGLE);
        container.addView(banner);

        if (banner != null) {
            // set the banner listener
            banner.setBannerListener(new BannerListener() {
                @Override
                public void onBannerAdLoaded() {
                    Log.d(TAG, "onBannerAdLoaded");
                    container.setVisibility(View.VISIBLE);
                    // since banner container was "gone" by default, we need to make it visible as soon as the banner is ready
                }

                @Override
                public void onBannerAdLoadFailed(IronSourceError error) {
                    Log.d(TAG, "onBannerAdLoadFailed" + " " + error);
                    context.runOnUiThread(container::removeAllViews);
                }

                @Override
                public void onBannerAdClicked() {
                    Log.d(TAG, "onBannerAdClicked");
                }

                @Override
                public void onBannerAdScreenPresented() {
                    Log.d(TAG, "onBannerAdScreenPresented");
                }

                @Override
                public void onBannerAdScreenDismissed() {
                    Log.d(TAG, "onBannerAdScreenDismissed");
                }

                @Override
                public void onBannerAdLeftApplication() {
                    Log.d(TAG, "onBannerAdLeftApplication");
                }
            });

            // load ad into the created banner
            IronSource.loadBanner(banner);
        } else {
            Toast.makeText(context, "IronSource.createBanner returned null", Toast.LENGTH_LONG).show();
        }
    }

    public void appLovinNativeAds(Activity context, FrameLayout frameLayout) {
        MaxNativeAdLoader nativeAdLoader = new MaxNativeAdLoader(Objects.requireNonNull(Paper.book().read(Prevalent.nativeAds)), context);
        nativeAdLoader.setNativeAdListener(new MaxNativeAdListener() {
            @Override
            public void onNativeAdLoaded(@Nullable MaxNativeAdView maxNativeAdView, MaxAd maxAd) {
                // Cleanup any pre-existing native ad to prevent memory leaks.
                if (nativeAd != null) {
                    nativeAdLoader.destroy(nativeAd);
                }

                // Save ad for cleanup.
                nativeAd = maxAd;

                // Add ad view to view.
                frameLayout.removeAllViews();
                frameLayout.addView(maxNativeAdView);
                super.onNativeAdLoaded(maxNativeAdView, maxAd);

            }

            @Override
            public void onNativeAdLoadFailed(String s, MaxError maxError) {
                super.onNativeAdLoadFailed(s, maxError);
                frameLayout.setVisibility(View.GONE);
                Log.e("aaaaaaaerror", maxError.getMessage());

            }

            @Override
            public void onNativeAdClicked(MaxAd maxAd) {
                super.onNativeAdClicked(maxAd);
            }
        });


        nativeAdLoader.loadAd();


    }

    // Meta NativeAds
    public void metaNativeAds(Activity context, FrameLayout container) {
        AudienceNetworkAds.initialize(context);
        String id = Objects.requireNonNull(Paper.book().read(Prevalent.nativeAds));
        fbNativeAd = new com.facebook.ads.NativeAd(context, id);

        NativeAdListener nativeAdListener = new NativeAdListener() {
            @Override
            public void onMediaDownloaded(Ad ad) {
                // Native ad finished downloading all assets
                Log.e(TAG, "Native ad finished downloading all assets.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                // Native ad failed to load
                Log.e(TAG, "Native ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                if (fbNativeAd == null || fbNativeAd != ad) {
                    return;
                }
                // Inflate Native Ad into Container
                inflateAd(fbNativeAd, container);
                // Native ad is loaded and ready to be displayed
                Log.d(TAG, "Native ad is loaded and ready to be displayed!");
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Native ad clicked
                Log.d(TAG, "Native ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Native ad impression
                Log.d(TAG, "Native ad impression logged!");
            }
        };

        // Request an ad
        fbNativeAd.loadAd(
                fbNativeAd.buildLoadAdConfig()
                        .withAdListener(nativeAdListener)
                        .build());
    }

    private void inflateAd(com.facebook.ads.NativeAd nativeAd, FrameLayout container) {

        nativeAd.unregisterView();

        // Add the Ad view into the ad container.
        nativeAdLayout = new NativeAdLayout(container.getContext());
        container.addView(nativeAdLayout);
        LayoutInflater inflater = LayoutInflater.from(container.getContext());
        // Inflate the Ad view.  The layout referenced should be the one you created in the last step.
        adView = (LinearLayout) inflater.inflate(R.layout.fb_ad_unified, nativeAdLayout, false);
        nativeAdLayout.addView(adView);

        // Add the AdOptionsView
        LinearLayout adChoicesContainer = adView.findViewById(R.id.ad_choices_container);
        AdOptionsView adOptionsView = new AdOptionsView(container.getContext(), nativeAd, nativeAdLayout);
        adChoicesContainer.removeAllViews();
        adChoicesContainer.addView(adOptionsView, 0);

        // Create native UI using the ad metadata.
        MediaView nativeAdIcon = adView.findViewById(R.id.native_ad_icon);
        TextView nativeAdTitle = adView.findViewById(R.id.native_ad_title);
//        MediaView nativeAdMedia = adView.findViewById(R.id.native_ad_media);
        TextView nativeAdSocialContext = adView.findViewById(R.id.native_ad_social_context);
        TextView nativeAdBody = adView.findViewById(R.id.native_ad_body);
        TextView sponsoredLabel = adView.findViewById(R.id.native_ad_sponsored_label);
        Button nativeAdCallToAction = adView.findViewById(R.id.native_ad_call_to_action);

        // Set the Text.
        nativeAdTitle.setText(nativeAd.getAdvertiserName());
        nativeAdBody.setText(nativeAd.getAdBodyText());
        nativeAdSocialContext.setText(nativeAd.getAdSocialContext());
        nativeAdCallToAction.setVisibility(nativeAd.hasCallToAction() ? View.VISIBLE : View.INVISIBLE);
        nativeAdCallToAction.setText(nativeAd.getAdCallToAction());
        sponsoredLabel.setText(nativeAd.getSponsoredTranslation());

        // Create a list of clickable views
        List<View> clickableViews = new ArrayList<>();
        clickableViews.add(nativeAdTitle);
        clickableViews.add(nativeAdCallToAction);

        // Register the Title and CTA button to listen for clicks.
        nativeAd.registerViewForInteraction(
                adView, nativeAdIcon, clickableViews);
    }


    public void appLovinMrec(Activity context, FrameLayout container) {
        AudienceNetworkAds.initialize(context);
        AppLovinSdk.getInstance(context).setMediationProvider("max");
        AppLovinSdk.initializeSdk(context);
        maxAdView = new MaxAdView(Paper.book().read(Prevalent.nativeAds), MaxAdFormat.MREC, context);
        maxAdView.setListener(this);

        // MREC width and height are 300 and 250 respectively, on phones and tablets
        int widthPx = AppLovinSdkUtils.dpToPx(context, 300);
        int heightPx = AppLovinSdkUtils.dpToPx(context, 250);

        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx));

        // Set background or background color for MRECs to be fully functional
        maxAdView.setLayoutParams(new FrameLayout.LayoutParams(widthPx, heightPx));
        maxAdView.setBackgroundColor(Color.WHITE);
        container.addView(maxAdView);
        // Load the ad
        maxAdView.loadAd();
    }

    public void metaMrec(Activity context, FrameLayout frameLayout) {
        AudienceNetworkAds.initialize(context);
        com.facebook.ads.AdView adView1 = new com.facebook.ads.AdView(context, Paper.book().read(Prevalent.nativeAds), com.facebook.ads.AdSize.RECTANGLE_HEIGHT_250);
        frameLayout.addView(adView1);
        adView1.loadAd();
    }


    // maxAds
    @Override
    public void onAdExpanded(MaxAd ad) {

    }

    @Override
    public void onAdCollapsed(MaxAd ad) {

    }

    @Override
    public void onAdLoaded(MaxAd ad) {
        retryAttempt = 0;
        maxAdView.setVisibility(View.VISIBLE);

    }

    @Override
    public void onAdDisplayed(MaxAd ad) {

    }

    @Override
    public void onAdHidden(MaxAd ad) {

    }

    @Override
    public void onAdClicked(MaxAd ad) {

    }

    @Override
    public void onAdLoadFailed(String adUnitId, MaxError error) {
        Log.d(TAG, "applovin" + error.getMessage());
        retryAttempt++;
        long delayMillis = TimeUnit.SECONDS.toMillis((long) Math.pow(2, Math.min(6, retryAttempt)));

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (interstitialAd != null) {

                    interstitialAd.loadAd();
                }
            }
        }, delayMillis);
    }

    @Override
    public void onAdDisplayFailed(MaxAd ad, MaxError error) {
        if (interstitialAd != null) {
            interstitialAd.loadAd();
        }
        Log.d(TAG, error.getMessage());
    }

}
