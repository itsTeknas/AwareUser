package com.blackcurrantapps.awareuser.util;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.blackcurrantapps.awareuser.R;


/**
 * Created by Sanket on 18/10/16.
 * Copyright (c) BlackcurrantApps LLP.
 */

public class EmptyViewSmall extends RecyclerView.ViewHolder {

    public TextView primaryText;

    public EmptyViewSmall(View itemView) {
        super(itemView);
        primaryText = (TextView) itemView.findViewById(R.id.nothingPrimary);
    }
}