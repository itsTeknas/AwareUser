package com.blackcurrantapps.awareuser.activities;

/**
 * Created by Sanket on 19/09/16.
 * Copyright (c) BlackcurrantApps LLP.
 */
public interface MainActivityConnect {
    void addFragment(android.support.v4.app.Fragment fragment, boolean isRoot);

    void onUpwardNavigation();

    void setToolbarTitle(String title);

    void showActivityToast(String message);
}
