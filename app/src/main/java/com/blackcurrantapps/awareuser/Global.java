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
    private static final String TWITTER_KEY = "LezmdGUgTNkEm5TcU2ylFY5YI";
    private static final String TWITTER_SECRET = "hgDWM0B5xR3DuuR7CjmMFlnTe5Ue6ofNqCZ1ap7aOxu4TLg3JS";

    @Override
    public void onCreate() {
        super.onCreate();

        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits.Builder().build());
    }

}
