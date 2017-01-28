package com.blackcurrantapps.awareuser;

import android.support.multidex.MultiDexApplication;

import com.digits.sdk.android.Digits;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;

import io.fabric.sdk.android.Fabric;

/**
 * Created by Sanket on 28/01/17.
 * Copyright (c) BlackcurrantApps LLP.
 */

public class Global extends MultiDexApplication {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "2d698eab68d5ae44b0efd7b12313884b0c45f2a3";
    private static final String TWITTER_SECRET = "a0adfd2bfcdc36849387bdb5c7e554d4e4c0aab397d80dcaa1d45126249e0694";

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
    }

}
